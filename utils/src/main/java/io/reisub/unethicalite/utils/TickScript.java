package io.reisub.unethicalite.utils;

import dev.hoot.api.commons.Rand;
import dev.hoot.api.input.Keyboard;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;

@Slf4j
public abstract class TickScript extends Plugin {
    @Inject
    private Client client;

    @Inject
    private Utils utils;

    @Getter
    private volatile boolean running;

    @Getter
    protected Activity currentActivity;

    @Getter
    protected Activity previousActivity;

    @Getter
    @Setter
    private Instant lastHop;

    private ScheduledExecutorService executor;

    protected final List<Task> tasks = new ArrayList<>();

    protected int minimumDelay = 100;
    protected int maximumDelay = 150;
    protected Instant lastLogin = Instant.EPOCH;
    protected Instant lastActionTime = Instant.EPOCH;
    protected Duration lastActionTimeout = Duration.ofSeconds(3);
    protected Instant lastExperience = Instant.EPOCH;
    protected Instant lastInventoryChange = Instant.EPOCH;
    protected final Map<Skill, Activity> idleCheckSkills = new HashMap<>();
    protected boolean idleCheckInventoryChange = false;

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

        executor.schedule(this::tick, Rand.nextInt(minimumDelay, maximumDelay), TimeUnit.MILLISECONDS);
        checkActionTimeout();
        checkIdleLogout();
    }

    @Subscribe
    private void onChatMessage(ChatMessage event) {
        if (!isRunning() || !isLoggedIn()) return;

        if (event.getType() == ChatMessageType.GAMEMESSAGE) {
            if (event.getMessage().startsWith("Congratulations, you've just advanced your")) {
                setActivity(Activity.IDLE);
            }
        }
    }

    @Subscribe
    private void onStatChanged(StatChanged event) {
        if (!isRunning() || !isLoggedIn()) return;

        for (Skill skill : idleCheckSkills.keySet()) {
            if (event.getSkill() == skill) {
                setActivity(idleCheckSkills.get(skill));
                lastExperience = Instant.now();
            }
        }
    }

    @Subscribe
    private void onItemContainerChanged(ItemContainerChanged event) {
        if (!isRunning() || !isLoggedIn()) return;

        if (event.getItemContainer() != client.getItemContainer(InventoryID.INVENTORY)) return;

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

    public final boolean isLoggedIn() {
        return client != null && client.getGameState() == GameState.LOGGED_IN;
    }

    public final boolean isLoggedInForLongerThan(Duration duration) {
        return Duration.between(lastLogin, Instant.now()).compareTo(duration) >= 0;
    }

    public final boolean isInRegion(int regionId) {
        Player player = client.getLocalPlayer();

        return player != null
                && player.getWorldLocation() != null
                && player.getWorldLocation().getRegionID() == regionId;
    }

    public final boolean isInMapRegion(int regionId) {
        for (int id : client.getMapRegions()) {
            if (id == regionId) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected final void startUp() {
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    protected final void shutDown() {
        stop();
        executor.shutdownNow();
    }

    public void start() {
        running = true;
        onStart();
    }

    public void start(String msg) {
        utils.sendGameMessage(msg);
        start();
    }

    public void stop() {
        running = false;
        onStop();
    }

    public void stop(String msg) {
        utils.sendGameMessage(msg);
        stop();
    }

    protected void onStart() {
        log.info("Starting " + this.getName());

        previousActivity = Activity.IDLE;
        currentActivity = Activity.IDLE;
    }

    protected void onStop() {
        log.info("Stopping " + this.getName());

        tasks.clear();

        previousActivity = Activity.IDLE;
        currentActivity = Activity.IDLE;
    }

    protected final <T extends Task> void addTask(Class<T> type) {
        tasks.add(injector.getInstance(type));
    }

    protected void checkActionTimeout() {
        if (currentActivity == Activity.IDLE || client.getLocalPlayer() == null) return;

        if (Duration.between(lastExperience, Instant.now()).compareTo(lastActionTimeout) < 0) return;

        if (Duration.between(lastInventoryChange, Instant.now()).compareTo(lastActionTimeout) < 0) return;

        if (!client.getLocalPlayer().isIdle() || lastActionTime == null) {
            lastActionTime = Instant.now();
            return;
        }

        Duration sinceAction = Duration.between(lastActionTime, Instant.now());

        if (sinceAction.compareTo(lastActionTimeout) >= 0) {
            setActivity(Activity.IDLE);
        }
    }

    private void checkIdleLogout() {
        int idleClientTicks = client.getKeyboardIdleTicks();

        if (client.getMouseIdleTicks() < idleClientTicks) {
            idleClientTicks = client.getMouseIdleTicks();
        }

        if (idleClientTicks > 12500) {
            Random r = new Random();
            log.info("Resetting idle");

            if (r.nextBoolean()) {
                Keyboard.type((char) VK_LEFT);
            } else {
                Keyboard.type((char) VK_RIGHT);
            }

            client.setKeyboardIdleTicks(0);
            client.setMouseIdleTicks(0);
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
}
