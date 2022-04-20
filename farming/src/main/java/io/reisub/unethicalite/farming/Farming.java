package io.reisub.unethicalite.farming;

import com.google.inject.Provides;
import dev.unethicalite.api.entities.NPCs;
import dev.unethicalite.api.entities.TileObjects;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.game.Vars;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.managers.Static;
import io.reisub.unethicalite.farming.tasks.Clear;
import io.reisub.unethicalite.farming.tasks.Cure;
import io.reisub.unethicalite.farming.tasks.DepositTools;
import io.reisub.unethicalite.farming.tasks.GoToPatch;
import io.reisub.unethicalite.farming.tasks.HandleBank;
import io.reisub.unethicalite.farming.tasks.Note;
import io.reisub.unethicalite.farming.tasks.PickHerb;
import io.reisub.unethicalite.farming.tasks.PickLimpwurt;
import io.reisub.unethicalite.farming.tasks.PlantHerb;
import io.reisub.unethicalite.farming.tasks.PlantLimpwurt;
import io.reisub.unethicalite.farming.tasks.WithdrawTools;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.ConfigList;
import io.reisub.unethicalite.utils.api.Predicates;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;
import net.runelite.api.Varbits;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.util.Text;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyListener;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.timetracking.farming.CropState;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Farming",
    description = "It's not much but it's honest work",
    enabledByDefault = false)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class Farming extends TickScript implements KeyListener {
  private static final int FARMING_GUILD_REGION = 4922;
  @Getter private final Queue<Location> locationQueue = new LinkedList<>();
  @Inject private Config config;
  @Getter private Location currentLocation;
  private ConfigList compostProduceConfigList;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    buildLocationQueue();

    addTask(HandleBank.class);
    addTask(GoToPatch.class);
    addTask(WithdrawTools.class);
    addTask(Note.class);
    addTask(Cure.class);
    addTask(Clear.class);
    addTask(PickLimpwurt.class);
    addTask(PlantLimpwurt.class);
    addTask(PickHerb.class);
    addTask(PlantHerb.class);
    addTask(DepositTools.class);
  }

  @Override
  protected void onStop() {
    super.onStop();

    for (Location location : Location.values()) {
      location.setDone(false);
    }

    locationQueue.clear();
    currentLocation = null;
  }

  @Subscribe
  private void onGameTick(GameTick event) {
    if (compostProduceConfigList == null) {
      compostProduceConfigList = ConfigList.parseList(config.oneClickCompostProduce());
    }

    if (locationQueue.isEmpty() || !isRunning()) {
      return;
    }

    if (currentLocation == null || currentLocation.isDone()) {
      while (!locationQueue.isEmpty()) {
        Location location = locationQueue.poll();

        if (location != null) {
          currentLocation = location;
          break;
        }
      }
    }
  }

  @Subscribe
  private void onMenuEntryAdded(MenuEntryAdded event) {
    if (!config.oneClickMode() || event.getType() != MenuAction.EXAMINE_ITEM.getId()) {
      return;
    }

    CropState compostBinState = getCompostBinState();

    if (OneClick.ONE_CLICK_GAME_OBJECTS_MAP.containsKey(event.getIdentifier())) {
      if (TileObjects.getNearest(
              Predicates.ids(OneClick.ONE_CLICK_GAME_OBJECTS_MAP.get(event.getIdentifier())))
          == null) {
        return;
      }
    } else if (OneClick.ONE_CLICK_ITEMS_MAP.containsKey(event.getIdentifier())) {
      if (!Inventory.contains(
          Predicates.ids(OneClick.ONE_CLICK_ITEMS_MAP.get(event.getIdentifier())))) {
        return;
      }
    } else if (OneClick.ONE_CLICK_NPCS_MAP.containsKey(event.getIdentifier())) {
      if (config.oneClickNote()
          && NPCs.getNearest(Predicates.ids(OneClick.ONE_CLICK_NPCS_MAP.get(event.getIdentifier())))
              == null) {
        return;
      }
    } else if (event.getIdentifier() == ItemID.VOLCANIC_ASH) {
      if (compostBinState != CropState.HARVESTABLE) {
        return;
      }
    } else {
      String name = Text.removeTags(event.getTarget());

      if (!compostProduceConfigList.getStrings().contains(name)
          || compostBinState == null
          || compostBinState == CropState.GROWING
          || compostBinState == CropState.HARVESTABLE) {
        return;
      }
    }

    Static.getClient()
        .insertMenuItem(
            OneClick.ONE_CLICK_FARMING,
            "",
            MenuAction.UNKNOWN.getId(),
            event.getIdentifier(),
            event.getActionParam0(),
            event.getActionParam1(),
            true);
  }

  @Subscribe
  private void onMenuOptionClicked(MenuOptionClicked event) {
    if (!event.getMenuOption().equals(OneClick.ONE_CLICK_FARMING)) {
      return;
    }

    Item item = Inventory.getFirst(event.getId());
    if (item == null) {
      return;
    }

    CropState compostBinState = getCompostBinState();

    if (OneClick.ONE_CLICK_GAME_OBJECTS_MAP.containsKey(event.getId())) {
      TileObject nearest =
          TileObjects.getNearest(
              Predicates.ids(OneClick.ONE_CLICK_GAME_OBJECTS_MAP.get(event.getId())));
      if (nearest == null) {
        return;
      }

      GameThread.invoke(() -> item.useOn(nearest));
    } else if (OneClick.ONE_CLICK_ITEMS_MAP.containsKey(event.getId())) {
      Item other =
          Inventory.getFirst(Predicates.ids(OneClick.ONE_CLICK_ITEMS_MAP.get(event.getId())));
      if (other == null) {
        return;
      }

      GameThread.invoke(() -> item.useOn(other));
    } else if (event.getId() == ItemID.VOLCANIC_ASH && compostBinState == CropState.HARVESTABLE) {
      TileObject bin = TileObjects.getNearest(Predicates.ids(Constants.COMPOST_BIN_IDS));
      if (bin == null) {
        return;
      }

      GameThread.invoke(() -> item.useOn(bin));
    } else if (compostProduceConfigList.getStrings().contains(item.getName())
        && (compostBinState == CropState.EMPTY || compostBinState == CropState.FILLING)) {
      TileObject bin = TileObjects.getNearest(Predicates.ids(Constants.COMPOST_BIN_IDS));
      if (bin == null) {
        return;
      }

      GameThread.invoke(() -> item.useOn(bin));
    } else if (config.oneClickNote() && OneClick.ONE_CLICK_NPCS_MAP.containsKey(event.getId())) {
      NPC nearest = NPCs.getNearest(Predicates.ids(OneClick.ONE_CLICK_NPCS_MAP.get(event.getId())));
      if (nearest == null) {
        return;
      }

      GameThread.invoke(() -> item.useOn(nearest));
    }
  }

  @Subscribe
  private void onConfigChanged(ConfigChanged event) {
    if (!event.getGroup().equals("chaosfarming")) {
      return;
    }

    if (event.getKey().equals("oneClickCompostProduce")) {
      compostProduceConfigList = ConfigList.parseList(config.oneClickCompostProduce());
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyPressed(KeyEvent e) {
    if (config.farmingHotkey().matches(e)) {
      e.consume();
      start();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {}

  private CropState getCompostBinState() {
    PatchState patchState;

    if (Utils.isInRegion(FARMING_GUILD_REGION)) {
      int varbit = Vars.getBit(Varbits.FARMING_7912);
      patchState = PatchImplementation.GIANT_COMPOST.forVarbitValue(varbit);
    } else {
      int varbit = Vars.getBit(Varbits.FARMING_4775);
      patchState = PatchImplementation.COMPOST.forVarbitValue(varbit);
    }

    if (patchState == null) {
      return null;
    } else {
      return patchState.getCropState();
    }
  }

  private void buildLocationQueue() {
    ConfigList herbOrder = ConfigList.parseList(config.herbOrder());

    for (String name : herbOrder.getStrings()) {
      for (Location location : Location.values()) {
        if (location.isEnabled(config) && name.equalsIgnoreCase(location.getName())) {
          locationQueue.add(location);
          break;
        }
      }
    }

    for (Location location : Location.values()) {
      if (location.isEnabled(config) && !locationQueue.contains(location)) {
        locationQueue.add(location);
      }
    }
  }
}
