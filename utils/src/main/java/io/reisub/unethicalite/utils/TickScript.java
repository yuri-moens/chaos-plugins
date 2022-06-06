package io.reisub.unethicalite.utils;

import io.reisub.unethicalite.utils.api.ChaosMovement;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Skill;
import net.runelite.api.events.ConfigButtonClicked;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.WidgetHiddenChanged;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.unethicalite.api.commons.Rand;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.Game;
import net.unethicalite.api.input.Keyboard;
import net.unethicalite.api.utils.MessageUtils;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.api.widgets.Widgets;
import net.unethicalite.client.Static;

@Slf4j
public abstract class TickScript extends Plugin implements KeyListener {
  @Inject
  private KeyManager keyManager;

  protected final List<Task> tasks = new ArrayList<>();
  protected final Map<Skill, Activity> idleCheckSkills = new HashMap<>();
  @Getter protected Activity currentActivity;
  @Getter protected Activity previousActivity;
  protected ScheduledExecutorService executor;
  protected Instant lastLogin = Instant.EPOCH;
  protected Instant lastActionTime = Instant.EPOCH;
  protected Duration lastActionTimeout = Duration.ofSeconds(3);
  protected Instant lastExperience = Instant.EPOCH;
  protected Instant lastInventoryChange = Instant.EPOCH;
  protected boolean idleCheckInventoryChange = false;
  @Inject private Config utilsConfig;
  @Getter private volatile boolean running;
  @Getter @Setter private Instant lastHop;
  private ScheduledFuture<?> current;
  private ScheduledFuture<?> next;

  @Subscribe
  private void onConfigButtonPressed(ConfigButtonClicked event) {
    String name = this.getName().replaceAll(" ", "").toLowerCase(Locale.ROOT);

    if (event.getGroup().equals(name) && event.getKey().equals("startButton")) {
      if (running) {
        stop();
      } else {
        start();
      }
    }
  }

  @Subscribe
  private void onGameTick(GameTick event) {
    if (!running) {
      return;
    }

    int minDelay = Math.min(utilsConfig.minDelay(), utilsConfig.maxDelay());
    int maxDelay = Math.max(utilsConfig.minDelay(), utilsConfig.maxDelay());

    try {
      if (current == null) {
        current =
            executor.schedule(this::tick, Rand.nextInt(minDelay, maxDelay), TimeUnit.MILLISECONDS);
      } else {
        if (current.isDone()) {
          if (next == null) {
            current =
                executor.schedule(
                    this::tick, Rand.nextInt(minDelay, maxDelay), TimeUnit.MILLISECONDS);
          } else {
            current = next;
            next = null;
          }
        } else {
          if (next == null) {
            next =
                executor.schedule(
                    this::tick, Rand.nextInt(minDelay, maxDelay), TimeUnit.MILLISECONDS);
          }
        }
      }

      checkActionTimeout();
      checkIdleLogout();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Subscribe
  private void onStatChanged(StatChanged event) {
    if (!isRunning() || !Utils.isLoggedIn()) {
      return;
    }

    for (Skill skill : idleCheckSkills.keySet()) {
      if (event.getSkill() == skill) {
        setActivity(idleCheckSkills.get(skill));
        lastExperience = Instant.now();
      }
    }
  }

  @Subscribe
  private void onWidgetHiddenChanged(WidgetHiddenChanged event) {
    if (Widgets.isVisible(Widgets.get(WidgetInfo.LEVEL_UP_LEVEL))) {
      Dialog.continueSpace();
      Dialog.continueSpace();
      setActivity(Activity.IDLE);
    }
  }

  @Subscribe
  private void onItemContainerChanged(ItemContainerChanged event) {
    if (!isRunning() || !Utils.isLoggedIn()) {
      return;
    }

    if (event.getItemContainer() != Game.getClient().getItemContainer(InventoryID.INVENTORY)) {
      return;
    }

    if (idleCheckInventoryChange) {
      lastInventoryChange = Instant.now();
    }
  }

  @Subscribe
  private void onGameStateChanged(GameStateChanged event) {
    if (event.getGameState() == GameState.LOGGED_IN) {
      lastLogin = Instant.now();
    }
  }

  public void setActivity(Activity action) {
    if (action == Activity.IDLE && currentActivity != Activity.IDLE) {
      previousActivity = currentActivity;
    }

    currentActivity = action;

    if (action != Activity.IDLE) {
      lastActionTime = Instant.now();
    }
  }

  public final boolean isLoggedInForLongerThan(Duration duration) {
    return Duration.between(lastLogin, Instant.now()).compareTo(duration) >= 0;
  }

  @Override
  protected final void startUp() {
    executor = Executors.newSingleThreadScheduledExecutor();

    Static.getKeyManager().registerKeyListener((KeyListener) this);
  }

  @Override
  protected final void shutDown() {
    stop();
    executor.shutdownNow();

    Static.getKeyManager().unregisterKeyListener((KeyListener) this);
  }

  public void start() {
    running = true;
    onStart();
  }

  public void start(String msg) {
    MessageUtils.addMessage(msg);
    start();
  }

  public void stop() {
    running = false;
    onStop();
  }

  public void stop(String msg) {
    MessageUtils.addMessage(msg);
    stop();
  }

  protected void onStart() {
    log.info("Starting " + this.getName());

    previousActivity = Activity.IDLE;
    currentActivity = Activity.IDLE;
  }

  protected void onStop() {
    log.info("Stopping " + this.getName());

    for (Task task : tasks) {
      Static.getEventBus().unregister(task);
    }

    tasks.clear();

    previousActivity = Activity.IDLE;
    currentActivity = Activity.IDLE;
  }

  protected final void addTask(Task task) {
    Static.getEventBus().register(task);

    tasks.add(task);
  }

  protected final <T extends Task> void addTask(Class<T> type) {
    Task task = injector.getInstance(type);

    Static.getEventBus().register(task);

    tasks.add(task);
  }

  protected void checkActionTimeout() {
    if (currentActivity == Activity.IDLE) {
      return;
    }

    if (Duration.between(lastExperience, Instant.now()).compareTo(lastActionTimeout) < 0) {
      return;
    }

    if (Duration.between(lastInventoryChange, Instant.now()).compareTo(lastActionTimeout) < 0) {
      return;
    }

    if (!Players.getLocal().isIdle() || lastActionTime == null) {
      lastActionTime = Instant.now();
      return;
    }

    Duration sinceAction = Duration.between(lastActionTime, Instant.now());

    if (sinceAction.compareTo(lastActionTimeout) >= 0) {
      setActivity(Activity.IDLE);
    }
  }

  private void checkIdleLogout() {
    int idleClientTicks = Game.getClient().getKeyboardIdleTicks();

    if (Game.getClient().getMouseIdleTicks() < idleClientTicks) {
      idleClientTicks = Game.getClient().getMouseIdleTicks();
    }

    if (idleClientTicks > 12500) {
      log.info("Resetting idle");

      Keyboard.type((char) KeyEvent.VK_BACK_SPACE);

      Game.getClient().setKeyboardIdleTicks(0);
      Game.getClient().setMouseIdleTicks(0);
    }
  }

  protected void tick() {
    for (Task t : tasks) {
      if (t.validate()) {
        log.info(t.getStatus());
        t.execute();
        break;
      }
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (utilsConfig.walkingInterruptHotkey().matches(e)) {
      ChaosMovement.interrupted = true;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {

  }
}
