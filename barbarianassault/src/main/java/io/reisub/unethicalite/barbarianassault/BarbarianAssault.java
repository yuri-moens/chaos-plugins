package io.reisub.unethicalite.barbarianassault;

import com.google.inject.Provides;
import io.reisub.unethicalite.barbarianassault.data.Call;
import io.reisub.unethicalite.barbarianassault.data.Role;
import io.reisub.unethicalite.barbarianassault.tasks.CallTask;
import io.reisub.unethicalite.barbarianassault.tasks.EquipArrows;
import io.reisub.unethicalite.barbarianassault.tasks.SpikeEgg;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import java.awt.event.KeyEvent;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.Varbits;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.game.Vars;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Widgets;
import org.pf4j.Extension;
import org.slf4j.Logger;

@PluginDescriptor(
    name = "Chaos Barbarian Assault",
    description = "Helper for the BA minigame",
    enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class BarbarianAssault extends TickScript {

  @Inject
  private Config config;
  @Getter
  private Role role;
  @Getter
  @Setter
  private Call lastCall;
  private int inGameBit = 0;

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
    super.onStart();

    role = null;
    lastCall = null;

    addTask(CallTask.class);
    addTask(EquipArrows.class);
    addTask(SpikeEgg.class);
  }

  @Subscribe
  private void onWidgetLoaded(WidgetLoaded event) {
    switch (event.getGroupId()) {
      case WidgetID.BA_REWARD_GROUP_ID:
        role = null;
        lastCall = null;
        break;
      case WidgetID.BA_ATTACKER_GROUP_ID:
        role = Role.ATTACKER;
        break;
      case WidgetID.BA_DEFENDER_GROUP_ID:
        role = Role.DEFENDER;
        break;
      case WidgetID.BA_HEALER_GROUP_ID:
        role = Role.HEALER;
        break;
      case WidgetID.BA_COLLECTOR_GROUP_ID:
        role = Role.COLLECTOR;
        break;
      default:
    }
  }

  @Subscribe
  private void onVarbitChanged(VarbitChanged event) {
    final int inGame = Vars.getBit(Varbits.IN_GAME_BA);

    if (inGameBit != inGame) {
      if (inGameBit == 1) {
        role = null;
        lastCall = null;
      }

      inGameBit = inGame;
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (inGameBit == 0) {
      return;
    }

    if (config.dropHotkey().matches(e)) {
      e.consume();
      drop();
    } else if (config.useFoodHotkey().matches(e)) {
      e.consume();
      final Item food = getPoisonedFood();
      final NPC nearest = NPCs.getNearest("Penance Healer");

      if (food == null || nearest == null) {
        return;
      }

      GameThread.invoke(() -> food.useOn(nearest));
    }
  }

  private void drop() {
    Inventory.getAll(ItemID.POISONED_MEAT, ItemID.POISONED_TOFU, ItemID.POISONED_WORMS,
            ItemID.RED_EGG, ItemID.BLUE_EGG, ItemID.GREEN_EGG,
            ItemID.TOFU, ItemID.WORMS, ItemID.CRACKERS)
        .forEach(i -> i.interact("Destroy"));
  }

  private Item getPoisonedFood() {
    final Widget widget = Widgets.get(role.getGroupId(), role.getListenIndex());

    if (widget == null) {
      return null;
    }

    final String food = widget.getText().toLowerCase();

    if (food.contains("meat")) {
      return Inventory.getFirst(ItemID.POISONED_MEAT);
    } else if (food.contains("tofu")) {
      return Inventory.getFirst(ItemID.POISONED_TOFU);
    } else if (food.contains("worms")) {
      return Inventory.getFirst(ItemID.POISONED_WORMS);
    }

    return null;
  }
}
