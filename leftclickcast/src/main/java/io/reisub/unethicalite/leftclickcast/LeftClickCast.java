package io.reisub.unethicalite.leftclickcast;

import static net.runelite.api.MenuAction.WIDGET_TARGET_ON_NPC;
import static net.runelite.api.MenuAction.WIDGET_TARGET_ON_PLAYER;

import com.google.inject.Provides;
import com.openosrs.client.util.WeaponMap;
import com.openosrs.client.util.WeaponStyle;
import io.reisub.unethicalite.utils.Utils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.util.Text;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.HotkeyListener;
import net.unethicalite.api.utils.MessageUtils;
import net.unethicalite.client.Static;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Left Click Cast",
    description = "Manually cast spells with a single click",
    enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class LeftClickCast extends Plugin {

  @Inject
  private Config config;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  private final Set<Integer> whitelist = new HashSet<>();

  private boolean isMage;
  private boolean disabled = false;
  private Spells currentSpell = Spells.ICE_BARRAGE;

  private final HotkeyListener spellOneSwap = new HotkeyListener(() -> config.spellOneSwap()) {
    @Override
    public void hotkeyPressed() {
      currentSpell = config.spellOne();
    }
  };

  private final HotkeyListener spellTwoSwap = new HotkeyListener(() -> config.spellTwoSwap()) {
    @Override
    public void hotkeyPressed() {
      currentSpell = config.spellTwo();
    }
  };

  private final HotkeyListener spellThreeSwap = new HotkeyListener(() -> config.spellThreeSwap()) {
    @Override
    public void hotkeyPressed() {
      currentSpell = config.spellThree();
    }
  };

  private final HotkeyListener spellFourSwap = new HotkeyListener(() -> config.spellFourSwap()) {
    @Override
    public void hotkeyPressed() {
      currentSpell = config.spellFour();
    }
  };

  private final HotkeyListener spellFiveSwap = new HotkeyListener(() -> config.spellFiveSwap()) {
    @Override
    public void hotkeyPressed() {
      currentSpell = config.spellFive();
    }
  };

  private final HotkeyListener spellSixSwap = new HotkeyListener(() -> config.spellSixSwap()) {
    @Override
    public void hotkeyPressed() {
      currentSpell = config.spellSix();
    }
  };

  private final HotkeyListener disable = new HotkeyListener(() -> config.disable()) {
    @Override
    public void hotkeyPressed() {
      disabled = !disabled;
      MessageUtils.addMessage(
          "LeftClickCast has been " + (disabled ? "disabled." : "enabled."),
          ChatMessageType.BROADCAST
      );
    }
  };

  @Override
  public void startUp() {
    if (Static.getClient().getGameState() == GameState.LOGGED_IN) {
      Static.getKeyManager().registerKeyListener(spellOneSwap);
      Static.getKeyManager().registerKeyListener(spellTwoSwap);
      Static.getKeyManager().registerKeyListener(spellThreeSwap);
      Static.getKeyManager().registerKeyListener(spellFourSwap);
      Static.getKeyManager().registerKeyListener(spellFiveSwap);
      Static.getKeyManager().registerKeyListener(spellSixSwap);
      Static.getKeyManager().registerKeyListener(disable);
    }

    updateConfig();
  }

  @Override
  public void shutDown() {
    Static.getKeyManager().unregisterKeyListener(spellOneSwap);
    Static.getKeyManager().unregisterKeyListener(spellTwoSwap);
    Static.getKeyManager().unregisterKeyListener(spellThreeSwap);
    Static.getKeyManager().unregisterKeyListener(spellFourSwap);
    Static.getKeyManager().unregisterKeyListener(spellFiveSwap);
    Static.getKeyManager().unregisterKeyListener(spellSixSwap);
    Static.getKeyManager().unregisterKeyListener(disable);
  }

  @Subscribe
  public void onGameStateChanged(GameStateChanged event) {
    if (event.getGameState() != GameState.LOGGED_IN) {
      Static.getKeyManager().unregisterKeyListener(spellOneSwap);
      Static.getKeyManager().unregisterKeyListener(spellTwoSwap);
      Static.getKeyManager().unregisterKeyListener(spellThreeSwap);
      Static.getKeyManager().unregisterKeyListener(spellFourSwap);
      Static.getKeyManager().unregisterKeyListener(spellFiveSwap);
      Static.getKeyManager().unregisterKeyListener(spellSixSwap);
      Static.getKeyManager().unregisterKeyListener(disable);
      return;
    }
    Static.getKeyManager().registerKeyListener(spellOneSwap);
    Static.getKeyManager().registerKeyListener(spellTwoSwap);
    Static.getKeyManager().registerKeyListener(spellThreeSwap);
    Static.getKeyManager().registerKeyListener(spellFourSwap);
    Static.getKeyManager().registerKeyListener(spellFiveSwap);
    Static.getKeyManager().registerKeyListener(spellSixSwap);
    Static.getKeyManager().registerKeyListener(disable);
  }

  @Subscribe
  public void onConfigChanged(ConfigChanged event) {
    updateConfig();
  }

  @Subscribe
  public void onMenuEntryAdded(MenuEntryAdded event) {
    if (disabled) {
      return;
    }

    if (Static.getClient().isMenuOpen()) {
      return;
    }

    if (event.isForceLeftClick()) {
      return;
    }

    if (event.getType() == MenuAction.PLAYER_SECOND_OPTION.getId() && isMage) {
      final String name = Text.standardize(event.getTarget(), true);

      if (!config.disableFriendlyRegionChecks() && (Static.getClient().getVarbitValue(5314) == 0
          && (Static.getClient().isFriended(name, false)))) {
        return;
      }

      setSelectSpell(currentSpell.getSpell());
      Static.getClient().createMenuEntry(Static.getClient().getMenuOptionCount())
          .setOption("(P) Left Click " + Static.getClient().getSelectedSpellName() + " -> ")
          .setTarget(event.getTarget())
          .setType(WIDGET_TARGET_ON_PLAYER)
          .setIdentifier(event.getIdentifier())
          .setParam0(0)
          .setParam1(0)
          .setForceLeftClick(true);
    } else if (event.getType() == MenuAction.NPC_SECOND_OPTION.getId() && isMage) {
      try {
        NPC npc = validateNpc(event.getIdentifier());

        if (npc == null) {
          return;
        }

        if (config.disableStaffChecks() && !whitelist.contains(npc.getId())) {
          return;
        }

        setSelectSpell(currentSpell.getSpell());
        Static.getClient().createMenuEntry(Static.getClient().getMenuOptionCount())
            .setOption("(N) Left Click " + Static.getClient().getSelectedSpellName() + " -> ")
            .setTarget(event.getTarget())
            .setType(WIDGET_TARGET_ON_NPC)
            .setIdentifier(event.getIdentifier())
            .setParam0(0)
            .setParam1(0)
            .setForceLeftClick(true);
      } catch (IndexOutOfBoundsException ignored) {
        // ignore
      }
    }
  }

  @Subscribe
  public void onItemContainerChanged(ItemContainerChanged event) {
    final ItemContainer ic = event.getItemContainer();

    if (Static.getClient().getItemContainer(InventoryID.EQUIPMENT) != ic) {
      return;
    }

    isMage = false;

    for (Item item : ic.getItems()) {
      if (WeaponMap.StyleMap.get(item.getId()) == WeaponStyle.MAGIC) {
        isMage = true;
        break;
      }
    }

    if (config.disableStaffChecks()) {
      isMage = true;
    }
  }

  private void updateConfig() {
    whitelist.clear();
    if (config.disableStaffChecks()) {
      List<String> string = Text.fromCSV(config.whitelist());
      for (String s : string) {
        try {
          whitelist.add(Integer.parseInt(s));
        } catch (NumberFormatException ignored) {
          // ignore
        }
      }
    }
  }

  private void setSelectSpell(WidgetInfo info) {
    Widget widget = Static.getClient().getWidget(info);
    if (widget == null) {
      log.info("Unable to locate spell widget.");
      return;
    }
    Static.getClient().setSelectedSpellName("<col=00ff00>" + widget.getName() + "</col>");
    Static.getClient().setSelectedSpellWidget(widget.getId());
    Static.getClient().setSelectedSpellChildIndex(-1);
  }

  /**
   * This method is not ideal, as its going to create a ton of junk but its the most reliable method
   * i've found so far for validating NPCs on menu events. Another solution would be to use string
   * comparison, however most users are used to the id concept so this was the path of least
   * resistance. I'm open to suggestions however if anyone wants to offer them. -Ganom
   *
   * @param index Menu event index.
   * @return {@link NPC} object for comparison.
   */
  @Nullable
  private NPC validateNpc(int index) {
    NPC npc = null;

    for (NPC clientNpc : Static.getClient().getNpcs()) {
      if (index == clientNpc.getIndex()) {
        npc = clientNpc;
        break;
      }
    }

    return npc;
  }
}
