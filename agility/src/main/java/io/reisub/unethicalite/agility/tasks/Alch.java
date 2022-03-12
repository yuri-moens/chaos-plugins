package io.reisub.unethicalite.agility.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.game.Vars;
import dev.hoot.api.items.Equipment;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.magic.Magic;
import dev.hoot.api.magic.Regular;
import dev.hoot.api.movement.Movement;
import dev.hoot.bot.managers.Static;
import io.reisub.unethicalite.agility.Agility;
import io.reisub.unethicalite.agility.Config;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.api.Varbits;
import net.runelite.api.events.StatChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.RunepouchRune;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class Alch extends Task {
    @Inject
    private Config config;

    private static final Varbits[] AMOUNT_VARBITS = {
            Varbits.RUNE_POUCH_AMOUNT1, Varbits.RUNE_POUCH_AMOUNT2, Varbits.RUNE_POUCH_AMOUNT3
    };
    private static final Varbits[] RUNE_VARBITS = {
            Varbits.RUNE_POUCH_RUNE1, Varbits.RUNE_POUCH_RUNE2, Varbits.RUNE_POUCH_RUNE3
    };

    private List<Integer> ids;
    private List<String> names;
    private int lastTick;
    private boolean ready;

    @Override
    public String getStatus() {
        return "Alching";
    }

    @Override
    public boolean validate() {
        if (!config.highAlch()) {
            return false;
        }

        if (ids == null) {
            parseItems();
        }

        return canCast()
                && hasRunes()
                && (Inventory.contains(Predicates.ids(ids)) || Inventory.contains(Predicates.names(names)));
    }

    @Override
    public void execute() {
        ready = false;
        Item item = null;

        if (Agility.DELAY_POINTS.contains(Players.getLocal().getWorldLocation())) {
            Time.sleepTick();
        }

        if (!ids.isEmpty()) {
            item = Inventory.getFirst((i) -> ids.contains(i.getId()));
        }

        if (item == null && !names.isEmpty()) {
            item = Inventory.getFirst((i) -> names.contains(i.getName()));
        }

        if (item == null) {
            return;
        }

        Magic.cast(Regular.HIGH_LEVEL_ALCHEMY, item);
        lastTick = Static.getClient().getTickCount();
    }

    @Subscribe
    private void onStatChanged(StatChanged event) {
        if (event.getSkill() == Skill.AGILITY) {
            ready = true;
        }
    }

    @Subscribe
    private void onConfigChanged(ConfigChanged event) {
        if (!event.getGroup().equals("chaosagility")) {
            return;
        }

        if (event.getKey().equals("alchItems")) {
            parseItems();
        }
    }

    private void parseItems() {
        ids = new ArrayList<>();
        names = new ArrayList<>();

        if (config.alchItems().equals("")) {
            return;
        }

        String[] items = config.alchItems().split("\n");

        for (String item : items) {
            try {
                int id = Integer.parseInt(item.trim());
                ids.add(id);
            } catch (Exception e) {
                names.add(item.trim());
            }
        }
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
                int amount = Vars.getBit(AMOUNT_VARBITS[i]);
                if (amount <= 0) continue;

                RunepouchRune rune = RunepouchRune.getRune(Vars.getBit(RUNE_VARBITS[i]));

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

    private boolean canCast() {
        if (Static.getClient().getTickCount() - lastTick < 5) {
            return false;
        }

        if (ready) {
            return true;
        }

        if (Movement.getDestination() == null || Players.getLocal().isAnimating()) {
            return false;
        }

        return true;
    }
}
