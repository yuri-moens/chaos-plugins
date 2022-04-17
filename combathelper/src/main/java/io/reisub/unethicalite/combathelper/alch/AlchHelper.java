package io.reisub.unethicalite.combathelper.alch;

import dev.unethicalite.api.commons.Rand;
import dev.unethicalite.api.commons.Time;
import dev.unethicalite.api.game.Game;
import dev.unethicalite.api.items.Equipment;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.magic.Magic;
import dev.unethicalite.api.magic.Regular;
import dev.unethicalite.api.utils.MessageUtils;
import dev.unethicalite.api.widgets.Tab;
import dev.unethicalite.api.widgets.Tabs;
import io.reisub.unethicalite.combathelper.Helper;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.util.Text;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.RunepouchRune;

import javax.inject.Singleton;
import java.awt.event.KeyEvent;
import java.util.function.Predicate;

@Singleton
public class AlchHelper extends Helper {
    private final static String FINISHED_TASK_MSG = "You have completed your task!";
    private static final int[] AMOUNT_VARBITS = {
            Varbits.RUNE_POUCH_AMOUNT1, Varbits.RUNE_POUCH_AMOUNT2, Varbits.RUNE_POUCH_AMOUNT3
    };
    private static final int[] RUNE_VARBITS = {
            Varbits.RUNE_POUCH_RUNE1, Varbits.RUNE_POUCH_RUNE2, Varbits.RUNE_POUCH_RUNE3
    };

    private boolean active;
    private long last;
    private String[] names;
    private String[] blacklistNames;

    private final Predicate<Item> itemPredicate = new Predicate<>() {
        @Override
        public boolean test(Item i) {
            String name = i.getName();

            if (blacklistNames != null && blacklistNames.length > 0) {
                for (String blacklistName : blacklistNames) {
                    if (name.contains(blacklistName)) {
                        return false;
                    }
                }
            }

            if (!config.alchNoted() && i.isNoted()) {
                return false;
            }

            for (String n : names) {
                if (name.contains(n)) {
                    return true;
                }
            }

            return false;
        }
    };

    @Override
    public void startUp() {
        names = parseItems(config.alchItems());
        blacklistNames = parseItems(config.alchItemsBlacklist());
    }

    @Subscribe
    private void onGameTick(GameTick event) {
        if (!active || last + 5 > Game.getClient().getTickCount()) return;

        if (names == null || names.length == 0) return;

        if (Inventory.contains(itemPredicate) && hasRunes()) {
            plugin.schedule(this::alch, Rand.nextInt(100, 200));
        }
    }

    @Subscribe
    private void onConfigChanged(ConfigChanged event) {
        if (!event.getGroup().equals("chaoscombathelper")) {
            return;
        }

        if (event.getKey().equals("alchItems")) {
            names = parseItems(config.alchItems());
        }

        if (event.getKey().equals("alchItemsBlacklist")) {
            blacklistNames = parseItems(config.alchItemsBlacklist());
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
        Item item = Inventory.getFirst(itemPredicate);
        if (item == null) return;

        Magic.cast(Regular.HIGH_LEVEL_ALCHEMY, item);
        last = Game.getClient().getTickCount();

        Time.sleepTicks(3);
        Tabs.openInterface(Tab.INVENTORY);
    }

    private String[] parseItems(String items) {
        String[] itemsArray = items.split(";");

        for (int i = 0; i < itemsArray.length; i++) {
            itemsArray[i] = itemsArray[i].trim();
        }

        return itemsArray;
    }

    private boolean hasRunes() {
        int fireRunes = Inventory.getCount(ItemID.FIRE_RUNE);
        int natureRunes = Inventory.getCount(ItemID.NATURE_RUNE);

        if (Equipment.contains(ItemID.FIRE_BATTLESTAFF, ItemID.MYSTIC_FIRE_STAFF, ItemID.LAVA_BATTLESTAFF, ItemID.MYSTIC_LAVA_STAFF, ItemID.TOME_OF_FIRE, ItemID.STAFF_OF_FIRE)) {
            fireRunes = 5;
        }

        if (Equipment.contains(ItemID.BRYOPHYTAS_STAFF)) {
            natureRunes = 1;
        }

        if (Inventory.contains(ItemID.RUNE_POUCH)) {
            for (int i = 0; i < AMOUNT_VARBITS.length; i++) {
                int amount = Game.getClient().getVar(AMOUNT_VARBITS[i]);
                if (amount <= 0) continue;

                RunepouchRune rune = RunepouchRune.getRune(Game.getClient().getVar(RUNE_VARBITS[i]));

                switch (rune) {
                    case NATURE:
                        natureRunes += amount;
                        break;
                    case FIRE:
                        fireRunes += amount;
                        break;
                }
            }
        }

        return fireRunes >= 5 && natureRunes >= 1;
    }
}
