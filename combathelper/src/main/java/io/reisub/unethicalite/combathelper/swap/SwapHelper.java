package io.reisub.unethicalite.combathelper.swap;

import com.openosrs.client.util.WeaponMap;
import com.openosrs.client.util.WeaponStyle;
import dev.hoot.api.commons.Rand;
import dev.hoot.api.entities.NPCs;
import dev.hoot.api.game.GameThread;
import dev.hoot.api.game.Skills;
import dev.hoot.api.game.Vars;
import dev.hoot.api.items.Equipment;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.combathelper.Helper;
import io.reisub.unethicalite.combathelper.prayer.PrayerHelper;
import io.reisub.unethicalite.combathelper.prayer.QuickPrayer;
import io.reisub.unethicalite.utils.Utils;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.NPC;
import net.runelite.api.Skill;
import net.runelite.api.Varbits;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.zulrah.ZulrahPlugin;

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
    private Set<String> meleeNames;
    private Set<Integer> rangedIds;
    private Set<String> rangedNames;
    private Set<Integer> magicIds;
    private Set<String> magicNames;

    @Override
    public void startUp() {
        parseGear();
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

        parseGear();
    }

    private void parseGear() {
        meleeIds = new HashSet<>();
        meleeNames = new HashSet<>();
        rangedIds = new HashSet<>();
        rangedNames = new HashSet<>();
        magicIds = new HashSet<>();
        magicNames = new HashSet<>();

        Utils.parseStringOrIntegerList(config.meleeGear(), meleeIds, meleeNames);
        Utils.parseStringOrIntegerList(config.rangedGear(), rangedIds, rangedNames);
        Utils.parseStringOrIntegerList(config.magicGear(), magicIds, magicNames);
    }

    public void keyPressed(KeyEvent e) {
        if (config.meleeHotkey().matches(e)) {
            plugin.schedule(() -> swap(WeaponStyle.MELEE), Rand.nextInt(250, 300));
            e.consume();
        } else if (config.rangedHotkey().matches(e)) {
            plugin.schedule(() -> swap(WeaponStyle.RANGE), Rand.nextInt(250, 300));
            e.consume();
        } else if (config.magicHotkey().matches(e)) {
            plugin.schedule(() -> swap(WeaponStyle.MAGIC), Rand.nextInt(250, 300));
            e.consume();
        }
    }

    private void tick() {
        if (config.autoSwapZulrah() && !zulrahPlugin.getZulrahData().isEmpty()) {
            zulrahSwap();
        }

        if (plugin.getLastTarget() == null || !config.autoSwap()) {
            return;
        }

        NPC target = null;

        List<NPC> npcList = NPCs.getAll((n) -> n.getWorldLocation().equals(plugin.getLastTarget().getWorldLocation()));
        if (npcList != null && npcList.size() > 0) {
            target = npcList.get(0);
        }

        if (target == null || target.getComposition().getOverheadIcon() == null) return;

        WeaponStyle currentStyle = getCurrentWeaponStyle();

        switch (target.getComposition().getOverheadIcon()) {
            case DEFLECT_MAGE:
            case MAGIC:
                if (currentStyle == WeaponStyle.MAGIC) {
                    swap(WeaponStyle.RANGE, WeaponStyle.MELEE);
                }
                break;
            case DEFLECT_RANGE:
            case RANGED:
                if (currentStyle == WeaponStyle.RANGE) {
                    swap(WeaponStyle.MAGIC, WeaponStyle.MELEE);
                }
                break;
            case DEFLECT_MELEE:
            case MELEE:
                if (currentStyle == WeaponStyle.MELEE) {
                    swap(WeaponStyle.RANGE, WeaponStyle.MAGIC);
                }
                break;
            case MAGE_MELEE:
                if (currentStyle == WeaponStyle.MAGIC || currentStyle == WeaponStyle.MELEE) {
                    swap(WeaponStyle.RANGE);
                }
                break;
            case RANGE_MAGE:
                if (currentStyle == WeaponStyle.RANGE || currentStyle == WeaponStyle.MAGIC) {
                    swap(WeaponStyle.MELEE);
                }
                break;
            case RANGE_MELEE:
                if (currentStyle == WeaponStyle.RANGE || currentStyle == WeaponStyle.MELEE) {
                    swap(WeaponStyle.MAGIC);
                }
                break;
        }
    }

    private void swap(WeaponStyle... styles) {
        for (WeaponStyle style : styles) {
            Set<Integer> ids;
            Set<String> names;

            switch(style) {
                case MELEE:
                    ids = meleeIds;
                    names = meleeNames;
                    break;
                case RANGE:
                    ids = rangedIds;
                    names = rangedNames;
                    break;
                case MAGIC:
                    ids = magicIds;
                    names = magicNames;
                    break;
                default:
                    ids = null;
                    names = null;
            }

            List<Item> items = Inventory.getAll(i -> {
                for (String name : names) {
                    if (i.getName().contains(name)) {
                        return true;
                    }
                }

                return ids.contains(i.getId());
            });

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
                    case RANGE:
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
        WeaponStyle current = getCurrentWeaponStyle();

        zulrahPlugin.getZulrahData().forEach(data -> data.getCurrentPhase().ifPresent(phase -> {
            switch (phase.getZulrahNpc().getType()) {
                case MELEE:
                case RANGE:
                    if (current != WeaponStyle.MAGIC) {
                        swap(WeaponStyle.MAGIC);
                    }
                    break;
                case MAGIC:
                    if (current != WeaponStyle.RANGE) {
                        swap(WeaponStyle.RANGE);
                    }
                    break;
            }
        }));
    }

    private WeaponStyle getCurrentWeaponStyle() {
        Item weapon = Equipment.fromSlot(EquipmentInventorySlot.WEAPON);

        if (weapon == null) {
            return WeaponStyle.MELEE;
        } else {
            return WeaponMap.StyleMap.get(weapon.getId());
        }
    }
}
