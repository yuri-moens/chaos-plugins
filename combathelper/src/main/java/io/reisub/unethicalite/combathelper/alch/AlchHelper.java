package io.reisub.unethicalite.combathelper.alch;

import io.reisub.unethicalite.combathelper.Helper;
import io.reisub.unethicalite.utils.api.ConfigList;
import java.awt.event.KeyEvent;
import java.util.function.Predicate;
import javax.inject.Singleton;
import net.runelite.api.Item;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.util.Text;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.unethicalite.api.commons.Rand;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.game.Game;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.SpellBook;
import net.unethicalite.api.magic.SpellBook.Standard;
import net.unethicalite.api.utils.MessageUtils;
import net.unethicalite.api.widgets.Tab;
import net.unethicalite.api.widgets.Tabs;
import net.unethicalite.client.Static;

@Singleton
public class AlchHelper extends Helper {
  private static final String FINISHED_TASK_MSG = "You have completed your task!";

  private boolean active;
  private long last;
  private ConfigList configList;
  private ConfigList configBlacklist;

  private final Predicate<Item> itemPredicate =
      new Predicate<>() {
        @Override
        public boolean test(Item i) {
          String name = i.getName();

          if (configBlacklist != null && !configBlacklist.getStrings().isEmpty()) {
            for (String blacklistName : configBlacklist.getStrings()) {
              if (name.contains(blacklistName)) {
                return false;
              }
            }
          }

          if (!config.alchNoted() && i.isNoted()) {
            return false;
          }

          for (String n : configList.getStrings()) {
            if (name.contains(n)) {
              return true;
            }
          }

          return false;
        }
      };

  @Override
  public void startUp() {
    configList = ConfigList.parseList(config.alchItems());
    configBlacklist = ConfigList.parseList(config.alchItemsBlacklist());
  }

  @Subscribe
  private void onGameTick(GameTick event) {
    if (!active || last + 5 > Game.getClient().getTickCount()) {
      return;
    }

    if (configList == null || configList.getStrings().isEmpty()) {
      return;
    }

    if (Inventory.contains(itemPredicate) && SpellBook.Standard.HIGH_LEVEL_ALCHEMY.canCast()) {
      plugin.schedule(this::alch, Rand.nextInt(100, 200));
    }
  }

  @Subscribe
  private void onConfigChanged(ConfigChanged event) {
    if (!event.getGroup().equals("chaoscombathelper")) {
      return;
    }

    if (event.getKey().equals("alchItems")) {
      configList = ConfigList.parseList(config.alchItems());
    }

    if (event.getKey().equals("alchItemsBlacklist")) {
      configBlacklist = ConfigList.parseList(config.alchItemsBlacklist());
    }
  }

  @Subscribe
  private void onChatMessage(ChatMessage event) {
    String msg = event.getMessage();

    if (Text.removeFormattingTags(msg).startsWith(FINISHED_TASK_MSG) && config.disableAfterTask()) {
      active = false;
      MessageUtils.addMessage("Finished Slayer task, disabled Auto Alcher");
    }
  }

  public void keyPressed(KeyEvent e) {
    if (config.autoalchHotkey().matches(e)) {
      e.consume();
      active = !active;

      if (active) {
        MessageUtils.addMessage("Enabled Auto Alcher");
      } else {
        MessageUtils.addMessage("Disabled Auto Alcher");
      }
    }
  }

  private void alch() {
    final Item item = Inventory.getFirst(itemPredicate);
    if (item == null) {
      return;
    }

    Standard.HIGH_LEVEL_ALCHEMY.castOn(item);
    last = Static.getClient().getTickCount();

    Time.sleepTicks(3);
    Tabs.openInterface(Tab.INVENTORY);
  }
}
