package io.reisub.unethicalite.alchemicalhydrafighter;

import com.google.inject.Provides;
import io.reisub.unethicalite.alchemicalhydra.ChaosAlchemicalHydra;
import io.reisub.unethicalite.alchemicalhydra.entity.Hydra;
import io.reisub.unethicalite.alchemicalhydra.entity.HydraPhase;
import io.reisub.unethicalite.alchemicalhydrafighter.tasks.Attack;
import io.reisub.unethicalite.alchemicalhydrafighter.tasks.DodgeFire;
import io.reisub.unethicalite.alchemicalhydrafighter.tasks.DodgeLightning;
import io.reisub.unethicalite.alchemicalhydrafighter.tasks.DodgePoison;
import io.reisub.unethicalite.alchemicalhydrafighter.tasks.GoToBank;
import io.reisub.unethicalite.alchemicalhydrafighter.tasks.GoToHydra;
import io.reisub.unethicalite.alchemicalhydrafighter.tasks.HandleBank;
import io.reisub.unethicalite.alchemicalhydrafighter.tasks.Loot;
import io.reisub.unethicalite.alchemicalhydrafighter.tasks.MovePhaseOne;
import io.reisub.unethicalite.alchemicalhydrafighter.tasks.MovePhaseTwo;
import io.reisub.unethicalite.alchemicalhydrafighter.tasks.TogglePrayer;
import io.reisub.unethicalite.combathelper.CombatHelper;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ConfigList;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.NPC;
import net.runelite.api.ObjectID;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.movement.Movement;
import org.pf4j.Extension;
import org.slf4j.Logger;

@PluginDescriptor(
    name = "Chaos Alchemical Hydra Fighter",
    description = "Fights the Alchemical Hydra",
    enabledByDefault = false
)
@PluginDependency(Utils.class)
@PluginDependency(ChaosAlchemicalHydra.class)
@Slf4j
@Extension
public class AlchemicalHydraFighter extends TickScript {

  @Inject
  private Config config;
  @Getter
  @Inject
  private CombatHelper combatHelperPlugin;
  @Getter
  @Inject
  private ChaosAlchemicalHydra hydraPlugin;
  @Getter
  private ConfigList equipment;
  @Getter
  private ConfigList inventory;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  public Logger getLogger() {
    return log;
  }

  @Override
  protected void onStart() {
    equipment = ConfigList.parseList(config.equipment());
    inventory = ConfigList.parseList(config.inventory());

    super.onStart();

    addTask(TogglePrayer.class);
    addTask(DodgePoison.class);
    addTask(DodgeLightning.class);
    addTask(DodgeFire.class);
    addTask(MovePhaseOne.class);
    addTask(MovePhaseTwo.class);
    addTask(GoToBank.class);
    addTask(HandleBank.class);
    addTask(GoToHydra.class);
    addTask(Loot.class);
    addTask(Attack.class);
  }

  @Subscribe
  private void onConfigChanged(ConfigChanged event) {
    if (!event.getGroup().equals("chaosalchemicalhydrafighter")) {
      return;
    }

    if (event.getKey().equals("equipment")) {
      equipment = ConfigList.parseList(config.equipment());
    } else if (event.getKey().equals("inventory")) {
      inventory = ConfigList.parseList(config.inventory());
    }
  }

  @Subscribe
  private void onGameTick(GameTick event) {
    NPC hydra = hydraPlugin.getHydra().getNpc();

    if (hydra == null) {
      return;
    }

    WorldPoint red = getRedVentLocation();
    WorldPoint green = getGreenVentLocation();
    WorldPoint blue = getBlueVentLocation();

    if (red != null) {
      log.info("red distance: " + hydra.distanceTo(red));
    }

    if (green != null) {
      log.info("green distance: " + hydra.distanceTo(green));
    }

    if (blue != null) {
      log.info("blue distance: " + hydra.distanceTo(blue));
    }
  }

  private WorldPoint getVentLocation(int id) {
    final TileObject vent = TileObjects.getNearest(id);

    if (vent == null) {
      return null;
    }

    return vent.getWorldLocation();
  }

  public WorldPoint getRedVentLocation() {
    return getVentLocation(ObjectID.CHEMICAL_VENT_RED);
  }

  public WorldPoint getGreenVentLocation() {
    return getVentLocation(ObjectID.CHEMICAL_VENT_GREEN);
  }

  public WorldPoint getBlueVentLocation() {
    return getVentLocation(ObjectID.CHEMICAL_VENT_BLUE);
  }

  public HydraPhase getPhase() {
    final Hydra hydra = hydraPlugin.getHydra();

    if (hydra == null || hydra.getNpc() == null || hydra.getPhase() == null) {
      return null;
    }

    return hydra.getPhase();
  }

  public void walk(final WorldPoint target) {
    do {
      Movement.walk(target);
    } while (!Time.sleepTicksUntil(() -> Players.getLocal().isMoving(), 2));
    Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().equals(target), 20);
  }
}
