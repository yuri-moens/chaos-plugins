package io.reisub.unethicalite.combathelper.swap;

import dev.hoot.api.commons.Rand;
import dev.hoot.api.entities.NPCs;
import dev.hoot.api.entities.Players;
import dev.hoot.api.game.Combat;
import dev.hoot.api.game.GameThread;
import dev.hoot.api.game.Skills;
import dev.hoot.api.game.Vars;
import dev.hoot.api.items.Equipment;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.combathelper.Helper;
import io.reisub.unethicalite.combathelper.prayer.PrayerHelper;
import io.reisub.unethicalite.combathelper.prayer.QuickPrayer;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.NPC;
import net.runelite.api.Skill;
import net.runelite.api.Varbits;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.zulrah.ZulrahPlugin;
import net.runelite.client.plugins.zulrah.constants.ZulrahType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class SwapHelper extends Helper {
    @Inject
    private PrayerHelper prayerHelper;

    @Inject
    private ZulrahPlugin zulrahPlugin;

    private Set<Integer> meleeIds;
    private Set<Integer> rangedIds;
    private Set<Integer> magicIds;

    @Override
    public void startUp() {
        meleeIds = parseIds(config.meleeGear());
        rangedIds = parseIds(config.rangedGear());
        magicIds = parseIds(config.magicGear());
    }

    @Subscribe
    private void onGameTick(GameTick event) {
        plugin.schedule(this::tick, Rand.nextInt(250, 300));
    }

    @Subscribe
    private void onConfigChanged(ConfigChanged event) {
        if (!event.getGroup().equals("chaoscombathelper")) {
            return;
        }

        meleeIds = parseIds(config.meleeGear());
        rangedIds = parseIds(config.rangedGear());
        magicIds = parseIds(config.magicGear());
    }

    private Set<Integer> parseIds(String idsString) {
        if (idsString.equals("")) return new HashSet<>();

        String[] idStrings = idsString.split(";");
        Set<Integer> ids = new HashSet<>();

        for (String idString : idStrings) {
            ids.add(Integer.parseInt(idString.trim()));
        }

        return ids;
    }

    public void keyPressed(KeyEvent e) {
        if (config.meleeHotkey().matches(e)) {
            plugin.schedule(() -> swap(AttackStyle.MELEE), Rand.nextInt(250, 300));
            e.consume();
        } else if (config.rangedHotkey().matches(e)) {
            plugin.schedule(() -> swap(AttackStyle.RANGED), Rand.nextInt(250, 300));
            e.consume();
        } else if (config.magicHotkey().matches(e)) {
            plugin.schedule(() -> swap(AttackStyle.MAGIC), Rand.nextInt(250, 300));
            e.consume();
        }
    }

    private void tick() {
        if (config.autoSwapZulrah() && !zulrahPlugin.getZulrahData().isEmpty()) {
            zulrahSwap();
        }

        if (plugin.getLastTarget() == null) return;

        NPC target = null;

        List<NPC> npcList = NPCs.getAll((n) -> n.getWorldLocation().equals(plugin.getLastTarget().getWorldLocation()));
        if (npcList != null && npcList.size() > 0) {
            target = npcList.get(0);
        }

        if (target == null || target.getComposition().getOverheadIcon() == null) return;

        int weaponId = Equipment.fromSlot(EquipmentInventorySlot.WEAPON).getId();

        switch (target.getComposition().getOverheadIcon()) {
            case DEFLECT_MAGE:
            case MAGIC:
                if (magicIds.contains(weaponId)) {
                    swap(AttackStyle.RANGED, AttackStyle.MELEE);
                }
                break;
            case DEFLECT_RANGE:
            case RANGED:
                if (rangedIds.contains(weaponId)) {
                    swap(AttackStyle.MAGIC, AttackStyle.MELEE);
                }
                break;
            case DEFLECT_MELEE:
            case MELEE:
                if (meleeIds.contains(weaponId)) {
                    swap(AttackStyle.RANGED, AttackStyle.MAGIC);
                }
                break;
            case MAGE_MELEE:
                if (magicIds.contains(weaponId) || meleeIds.contains(weaponId)) {
                    swap(AttackStyle.RANGED);
                }
                break;
            case RANGE_MAGE:
                if (rangedIds.contains(weaponId) || magicIds.contains(weaponId)) {
                    swap(AttackStyle.MELEE);
                }
                break;
            case RANGE_MELEE:
                if (rangedIds.contains(weaponId) || meleeIds.contains(weaponId)) {
                    swap(AttackStyle.MAGIC);
                }
                break;
        }
    }

    private void swap(AttackStyle... styles) {
        for (AttackStyle style : styles) {
            Set<Integer> ids = new HashSet<>();

            switch(style) {
                case MELEE:
                    ids = meleeIds;
                    break;
                case RANGED:
                    ids = rangedIds;
                    break;
                case MAGIC:
                    ids = magicIds;
                    break;
            }

            final Set<Integer> finalIds = ids;

            if (finalIds.isEmpty()) continue;

            List<Item> items = Inventory.getAll((i) -> finalIds.contains(i.getId()));

            if (!items.isEmpty()) {
                for (Item item : items) {
                    if (item.hasAction("Wield")) {
                        item.interact("Wield");
                    } else {
                        item.interact("Wear");
                    }
                }

                Set<QuickPrayer> prayers = new HashSet<>();
                int level = Skills.getLevel(Skill.PRAYER);

                switch(style) {
                    case MELEE:
                        if (config.swapOffensivePrayers()) {
                            prayers.addAll(QuickPrayer.getBestMeleeBuff(level, Vars.getBit(Varbits.CAMELOT_TRAINING_ROOM_STATUS.getId()) == 8));
                        }

                        if (config.swapDefensivePrayers()) {
                            prayers.add(config.meleePrayer().getQuickPrayer());
                        }
                        break;
                    case RANGED:
                        if (config.swapOffensivePrayers()) {
                            prayers.addAll(QuickPrayer.getBestRangedBuff(level, Vars.getBit(Varbits.RIGOUR_UNLOCKED.getId()) != 0));
                        }

                        if (config.swapDefensivePrayers()) {
                            prayers.add(config.rangedPrayer().getQuickPrayer());
                        }
                        break;
                    case MAGIC:
                        if (config.swapOffensivePrayers()) {
                            prayers.addAll(QuickPrayer.getBestMagicBuff(level, Vars.getBit(Varbits.AUGURY_UNLOCKED.getId()) != 0));
                        }

                        if (config.swapDefensivePrayers()) {
                            prayers.add(config.magicPrayer().getQuickPrayer());
                        }
                        break;
                }

                prayerHelper.setPrayers(prayers, false);
            } else {
                continue;
            }

            break;
        }

        if (plugin.getLastTarget() != null) {
            GameThread.invoke(() -> plugin.getLastTarget().interact("Attack"));
        }
    }

    private void zulrahSwap() {
        AttackStyle current = getCurrentAttackStyle();

        zulrahPlugin.getZulrahData().forEach(data -> {
            data.getCurrentPhase().ifPresent(phase -> {
                switch (phase.getZulrahNpc().getType()) {
                    case MELEE:
                    case RANGE:
                        if (current != AttackStyle.MAGIC) {
                            swap(AttackStyle.MAGIC);
                        }
                        break;
                    case MAGIC:
                        if (current != AttackStyle.RANGED) {
                            swap(AttackStyle.RANGED);
                        }
                        break;
                }
            });
        });
    }

    private AttackStyle getCurrentAttackStyle() {
        Item weapon = Equipment.fromSlot(EquipmentInventorySlot.WEAPON);

        if (weapon != null) {
            if (meleeIds.contains(weapon.getId())) {
                return AttackStyle.MELEE;
            } else if (rangedIds.contains(weapon.getId())) {
                return AttackStyle.RANGED;
            } else if (magicIds.contains(weapon.getId())) {
                return AttackStyle.MAGIC;
            }
        }

        return AttackStyle.MELEE;
    }
}
