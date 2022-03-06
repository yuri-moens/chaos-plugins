package io.reisub.unethicalite.combathelper.consume;

import dev.hoot.api.commons.Rand;
import dev.hoot.api.game.Combat;
import dev.hoot.api.game.Game;
import dev.hoot.api.game.Skills;
import dev.hoot.api.game.Vars;
import dev.hoot.api.items.Bank;
import dev.hoot.api.items.Equipment;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.movement.Movement;
import dev.hoot.api.utils.MessageUtils;
import dev.hoot.api.widgets.Dialog;
import io.reisub.unethicalite.combathelper.CombatHelper;
import io.reisub.unethicalite.combathelper.Config;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.ItemStatChanges;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.StatsChanges;
import net.runelite.client.plugins.itemstats.stats.Stats;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.event.KeyEvent;
import java.util.Set;

@Singleton
public class ConsumeHelper {
    @Inject
    private CombatHelper plugin;

    @Inject
    private Config config;

    @Inject
    private ItemStatChanges statChanges;

    private final Set<Integer> IGNORE_FOOD = Set.of(ItemID.DWARVEN_ROCK_CAKE, ItemID.DWARVEN_ROCK_CAKE_7510);
    private final Set<Integer> NON_EAT_FOOD_IDS = Set.of(ItemID.JUG_OF_WINE, ItemID.BANDAGES);
    private final Set<Integer> BREW_IDS = Set.of(ItemID.SARADOMIN_BREW1, ItemID.SARADOMIN_BREW2, ItemID.SARADOMIN_BREW3, ItemID.SARADOMIN_BREW4, ItemID.XERICS_AID_1, ItemID.XERICS_AID_2, ItemID.XERICS_AID_3, ItemID.XERICS_AID_4, ItemID.XERICS_AID_1_20977, ItemID.XERICS_AID_2_20978, ItemID.XERICS_AID_3_20979, ItemID.XERICS_AID_4_20980, ItemID.XERICS_AID_1_20981, ItemID.XERICS_AID_2_20982, ItemID.XERICS_AID_3_20983, ItemID.XERICS_AID_4_20984);
    private final Set<Integer> RESTORE_IDS = Set.of(ItemID.RESTORE_POTION1, ItemID.RESTORE_POTION2, ItemID.RESTORE_POTION3, ItemID.RESTORE_POTION4,
            ItemID.SUPER_RESTORE1, ItemID.SUPER_RESTORE2, ItemID.SUPER_RESTORE3, ItemID.SUPER_RESTORE4, ItemID.BLIGHTED_SUPER_RESTORE1,
            ItemID.BLIGHTED_SUPER_RESTORE2, ItemID.BLIGHTED_SUPER_RESTORE3, ItemID.BLIGHTED_SUPER_RESTORE4, ItemID.EGNIOL_POTION_1,
            ItemID.EGNIOL_POTION_2, ItemID.EGNIOL_POTION_3, ItemID.EGNIOL_POTION_4,
            ItemID.SANFEW_SERUM1, ItemID.SANFEW_SERUM2, ItemID.SANFEW_SERUM3, ItemID.SANFEW_SERUM4);
    private final Set<Integer> ANTI_POISON_IDS = Set.of(ItemID.ANTIPOISON1, ItemID.ANTIPOISON2, ItemID.ANTIPOISON3, ItemID.ANTIPOISON4, ItemID.SUPERANTIPOISON1, ItemID.SUPERANTIPOISON2, ItemID.SUPERANTIPOISON3, ItemID.SUPERANTIPOISON4,
            ItemID.ANTIDOTE1, ItemID.ANTIDOTE2, ItemID.ANTIDOTE3, ItemID.ANTIDOTE4, ItemID.ANTIDOTE1_5958, ItemID.ANTIDOTE2_5956, ItemID.ANTIDOTE3_5954, ItemID.ANTIDOTE4_5952,
            ItemID.ANTIVENOM1, ItemID.ANTIVENOM2, ItemID.ANTIVENOM3, ItemID.ANTIVENOM4, ItemID.ANTIVENOM4_12913, ItemID.ANTIVENOM3_12915, ItemID.ANTIVENOM2_12917, ItemID.ANTIVENOM1_12919);
    private final Set<Integer> ANTI_FIRE_IDS = Set.of(ItemID.ANTIFIRE_POTION1, ItemID.ANTIFIRE_POTION2, ItemID.ANTIFIRE_POTION3, ItemID.ANTIFIRE_POTION4, ItemID.SUPER_ANTIFIRE_POTION1, ItemID.SUPER_ANTIFIRE_POTION2, ItemID.SUPER_ANTIFIRE_POTION3, ItemID.SUPER_ANTIFIRE_POTION4,
            ItemID.EXTENDED_ANTIFIRE1, ItemID.EXTENDED_ANTIFIRE2, ItemID.EXTENDED_ANTIFIRE3, ItemID.EXTENDED_ANTIFIRE4, ItemID.EXTENDED_SUPER_ANTIFIRE1, ItemID.EXTENDED_SUPER_ANTIFIRE2, ItemID.EXTENDED_SUPER_ANTIFIRE3, ItemID.EXTENDED_SUPER_ANTIFIRE4);
    private final Set<Integer> PRAYER_IDS = Set.of(ItemID.PRAYER_POTION1, ItemID.PRAYER_POTION2, ItemID.PRAYER_POTION3, ItemID.PRAYER_POTION4,
            ItemID.SUPER_RESTORE1, ItemID.SUPER_RESTORE2, ItemID.SUPER_RESTORE3, ItemID.SUPER_RESTORE4, ItemID.BLIGHTED_SUPER_RESTORE1,
            ItemID.BLIGHTED_SUPER_RESTORE2, ItemID.BLIGHTED_SUPER_RESTORE3, ItemID.BLIGHTED_SUPER_RESTORE4, ItemID.EGNIOL_POTION_1,
            ItemID.EGNIOL_POTION_2, ItemID.EGNIOL_POTION_3, ItemID.EGNIOL_POTION_4);
    private final Set<Integer> STRENGTH_IDS = Set.of(ItemID.STRENGTH_POTION1, ItemID.STRENGTH_POTION2, ItemID.STRENGTH_POTION3, ItemID.STRENGTH_POTION4,
            ItemID.SUPER_STRENGTH1, ItemID.SUPER_STRENGTH2, ItemID.SUPER_STRENGTH3, ItemID.SUPER_STRENGTH4,
            ItemID.DIVINE_SUPER_STRENGTH_POTION1, ItemID.DIVINE_SUPER_STRENGTH_POTION2, ItemID.DIVINE_SUPER_STRENGTH_POTION3, ItemID.DIVINE_SUPER_STRENGTH_POTION4,
            ItemID.DIVINE_SUPER_COMBAT_POTION1, ItemID.DIVINE_SUPER_COMBAT_POTION2, ItemID.DIVINE_SUPER_COMBAT_POTION3, ItemID.DIVINE_SUPER_COMBAT_POTION4,
            ItemID.COMBAT_POTION1, ItemID.COMBAT_POTION2, ItemID.COMBAT_POTION3, ItemID.COMBAT_POTION4,
            ItemID.SUPER_COMBAT_POTION1, ItemID.SUPER_COMBAT_POTION2, ItemID.SUPER_COMBAT_POTION3, ItemID.SUPER_COMBAT_POTION4);
    private final Set<Integer> ATTACK_IDS = Set.of(ItemID.ATTACK_POTION1, ItemID.ATTACK_POTION2, ItemID.ATTACK_POTION3, ItemID.ATTACK_POTION4,
            ItemID.SUPER_ATTACK1, ItemID.SUPER_ATTACK2, ItemID.SUPER_ATTACK3, ItemID.SUPER_ATTACK4,
            ItemID.DIVINE_SUPER_ATTACK_POTION1, ItemID.DIVINE_SUPER_ATTACK_POTION2, ItemID.DIVINE_SUPER_ATTACK_POTION3, ItemID.DIVINE_SUPER_ATTACK_POTION4,
            ItemID.DIVINE_SUPER_COMBAT_POTION1, ItemID.DIVINE_SUPER_COMBAT_POTION2, ItemID.DIVINE_SUPER_COMBAT_POTION3, ItemID.DIVINE_SUPER_COMBAT_POTION4,
            ItemID.COMBAT_POTION1, ItemID.COMBAT_POTION2, ItemID.COMBAT_POTION3, ItemID.COMBAT_POTION4,
            ItemID.SUPER_COMBAT_POTION1, ItemID.SUPER_COMBAT_POTION2, ItemID.SUPER_COMBAT_POTION3, ItemID.SUPER_COMBAT_POTION4);
    private final Set<Integer> DEFENCE_IDS = Set.of(ItemID.DEFENCE_POTION1, ItemID.DEFENCE_POTION2, ItemID.DEFENCE_POTION3, ItemID.DEFENCE_POTION4,
            ItemID.SUPER_DEFENCE1, ItemID.SUPER_DEFENCE2, ItemID.SUPER_DEFENCE3, ItemID.SUPER_DEFENCE4,
            ItemID.DIVINE_SUPER_DEFENCE_POTION1, ItemID.DIVINE_SUPER_DEFENCE_POTION2, ItemID.DIVINE_SUPER_DEFENCE_POTION3, ItemID.DIVINE_SUPER_DEFENCE_POTION4,
            ItemID.DIVINE_SUPER_COMBAT_POTION1, ItemID.DIVINE_SUPER_COMBAT_POTION2, ItemID.DIVINE_SUPER_COMBAT_POTION3, ItemID.DIVINE_SUPER_COMBAT_POTION4,
            ItemID.SUPER_COMBAT_POTION1, ItemID.SUPER_COMBAT_POTION2, ItemID.SUPER_COMBAT_POTION3, ItemID.SUPER_COMBAT_POTION4);
    private final Set<Integer> RANGED_IDS = Set.of(ItemID.RANGING_POTION1, ItemID.RANGING_POTION2, ItemID.RANGING_POTION3, ItemID.RANGING_POTION4,
            ItemID.BASTION_POTION1, ItemID.BASTION_POTION2, ItemID.BASTION_POTION3, ItemID.BASTION_POTION4,
            ItemID.DIVINE_RANGING_POTION1, ItemID.DIVINE_RANGING_POTION2, ItemID.DIVINE_RANGING_POTION3, ItemID.DIVINE_RANGING_POTION4,
            ItemID.DIVINE_BASTION_POTION1, ItemID.DIVINE_BASTION_POTION2, ItemID.DIVINE_BASTION_POTION3, ItemID.DIVINE_BASTION_POTION4,
            ItemID.SUPER_RANGING_1, ItemID.SUPER_RANGING_2, ItemID.SUPER_RANGING_3, ItemID.SUPER_RANGING_4);
    private final Set<Integer> MAGIC_IDS = Set.of(ItemID.MAGIC_POTION1, ItemID.MAGIC_POTION2, ItemID.MAGIC_POTION3, ItemID.MAGIC_POTION4,
            ItemID.BATTLEMAGE_POTION1, ItemID.BATTLEMAGE_POTION2, ItemID.BATTLEMAGE_POTION3, ItemID.BATTLEMAGE_POTION4,
            ItemID.DIVINE_MAGIC_POTION1, ItemID.DIVINE_MAGIC_POTION2, ItemID.DIVINE_MAGIC_POTION3, ItemID.DIVINE_MAGIC_POTION4,
            ItemID.DIVINE_BATTLEMAGE_POTION1, ItemID.DIVINE_BATTLEMAGE_POTION2, ItemID.DIVINE_BATTLEMAGE_POTION3, ItemID.DIVINE_BATTLEMAGE_POTION4);
    private final Set<Integer> ENERGY_IDS = Set.of(ItemID.ENERGY_POTION1, ItemID.ENERGY_POTION2, ItemID.ENERGY_POTION3, ItemID.ENERGY_POTION4,
            ItemID.ENERGY_MIX1, ItemID.ENERGY_MIX2, ItemID.SUPER_ENERGY_MIX1, ItemID.SUPER_ENERGY_MIX2,
            ItemID.SUPER_ENERGY1, ItemID.SUPER_ENERGY2, ItemID.SUPER_ENERGY3, ItemID.SUPER_ENERGY4,
            ItemID.SUPER_ENERGY1_20551, ItemID.SUPER_ENERGY2_20550, ItemID.SUPER_ENERGY3_20549, ItemID.SUPER_ENERGY4_20548);

    private long lastAte;
    private long lastPot;
    private int timeout;
    private int eatThreshold;
    private boolean shouldDrinkRestore;
    private int prayerThreshold;
    private boolean shouldDrinkAntiPoison;
    private long lastAntiPoison;
    private boolean shouldDrinkAntiFire;
    private long lastAntiFire;
    private long shouldDrinkBrew;
    private boolean shouldDrinkPrayer;
    private long lastPrayer;
    private boolean shouldDrinkStrength;
    private long lastStrength;
    private boolean shouldDrinkAttack;
    private long lastAttack;
    private boolean shouldDrinkDefence;
    private long lastDefence;
    private boolean shouldDrinkRanged;
    private long lastRanged;
    private boolean shouldDrinkMagic;
    private long lastMagic;
    private boolean shouldUseSpecial;
    private boolean shouldSwap;
    private boolean shouldSwapBack;
    private int originalWeaponId;
    private long lastSpecial;
    private long lastEnergy;

    public void startUp() {
        generateNewEatThreshold();
        generateNewPrayerThreshold();
    }

    public void onGameTick() {
        if (Bank.isOpen() || Dialog.isOpen()) return;

        if (config.useSpecial()
                && config.activationMethod() == SpecialActivation.AUTOMATIC
                && Combat.getSpecEnergy() >= config.specialCost()
                && lastSpecial + 1800 < System.currentTimeMillis()) {
            shouldUseSpecial = true;
        }

        if (config.useSpecial()
                && originalWeaponId != 0
                && Combat.getSpecEnergy() < config.specialCost()) {
            shouldSwapBack = true;
        }

        if (timeout > 0) {
            timeout--;
            return;
        }

        plugin.schedule(this::tick, Rand.nextInt(50, 80));
    }

    public void onVarbitChanged(VarbitChanged event) {
        if (Game.getState() != GameState.LOGGED_IN) return;

        if (config.drinkAntiPoison()
                && config.drinkPotions()
                && event.getIndex() == VarPlayer.POISON.getId()
                && Vars.getVarp(VarPlayer.POISON.getId()) > 0) {
            shouldDrinkAntiPoison = true;
        }
    }

    public void onChatMessage(ChatMessage event) {
        if (Game.getState() != GameState.LOGGED_IN) return;

        String BURN_MESSAGE = ("You're horribly burnt by the dragon fire!");
        String BURN_EXPIRE = ("antifire potion is about to expire.");

        String message = event.getMessage();

        if (config.drinkAntiFire() && config.drinkPotions() && (message.contains(BURN_MESSAGE) || message.contains(BURN_EXPIRE))) {
            shouldDrinkAntiFire = true;
        }
    }

    public void onStatChanged(StatChanged event) {
        if (Game.getState() != GameState.LOGGED_IN || timeout > 0) return;

        Skill skill = event.getSkill();
        int level = event.getBoostedLevel();

        checkSkill(skill, level);
    }

    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGGED_IN) {
            timeout = 5;
        }
    }

    public void onConfigChanged(ConfigChanged event) {
        switch (event.getKey()) {
            case "minEatHP":
            case "maxEatHP":
                generateNewEatThreshold();
                break;
            case "minPrayerPoints":
            case "maxPrayerPoints":
                generateNewPrayerThreshold();
            case "drinkPrayer":
                checkSkill(Skill.PRAYER);
                break;
            case "strengthLevel":
                checkSkill(Skill.STRENGTH);
                break;
            case "attackLevel":
                checkSkill(Skill.ATTACK);
                break;
            case "defenceLevel":
                checkSkill(Skill.DEFENCE);
                break;
            case "rangedLevel":
                checkSkill(Skill.RANGED);
                break;
            case "magicLevel":
                checkSkill(Skill.MAGIC);
                break;
        }
    }

    public void keyPressed(KeyEvent e) {
        if (config.specialHotkey().matches(e)
                && config.useSpecial()
                && config.activationMethod() == SpecialActivation.HOTKEY
                && Combat.getSpecEnergy() >= config.specialCost()
                && lastSpecial + 1200 < System.currentTimeMillis()) {
            shouldUseSpecial = true;
            e.consume();
        }
    }

    private int getHealed(int itemId) {
        Effect effect = statChanges.get(itemId);
        if (effect != null) {
            StatsChanges statsChanges = effect.calculate(Game.getClient());
            for (StatChange statChange : statsChanges.getStatChanges()) {
                if (statChange.getStat().getName().equals(Stats.HITPOINTS.getName())) {
                    return statChange.getAbsolute();
                }
            }
        }

        return 0;
    }

    private void tick() {
        int hp = Combat.getCurrentHealth();
        int missingHp = Combat.getMissingHealth();
        boolean didAction = false;

        if (config.enableEating() && hp <= eatThreshold && canEat()) {
            Item food = Inventory.getFirst((i) -> (i.hasAction("Eat") || NON_EAT_FOOD_IDS.contains(i.getId())) && !IGNORE_FOOD.contains(i.getId()));
            if (food != null) {
                missingHp += getHealed(food.getId());

                food.interact(0);
                lastAte = Game.getClient().getTickCount();
                didAction = true;
            }

            if (!didAction && config.eatWarnings()) {
                MessageUtils.addMessage("HP below threshold but you don't have any food!");
            }
        }

        if (shouldSwapBack) {
            Item weapon = Inventory.getFirst(originalWeaponId);
            if (weapon != null) {
                weapon.interact("Wield");
                didAction = true;
                shouldSwapBack = false;
                originalWeaponId = 0;
            }
        }

        if (shouldUseSpecial && !Combat.isSpecEnabled()) {
            if (!config.specialWeapon().equals("")
                    && !Equipment.contains(config.specialWeapon())
                    && Inventory.contains(config.specialWeapon())
                    && (config.swapFromWeapon().equals("") || Equipment.contains(config.swapFromWeapon()))) {
                originalWeaponId = Equipment.fromSlot(EquipmentInventorySlot.WEAPON).getId();

                Item weapon = Inventory.getFirst(config.specialWeapon());
                if (weapon != null) {
                    weapon.interact("Wield");
                    didAction = true;
                }
            }

            if (!config.swapFromWeapon().equals("") && !Equipment.contains(config.swapFromWeapon(), config.specialWeapon())) {
                shouldUseSpecial = false;
            }

            if (Vars.getBit(Varbits.PVP_SPEC_ORB.getId()) == 0 && shouldUseSpecial) {
                Combat.toggleSpec();
            }

            shouldUseSpecial = false;
            lastSpecial = System.currentTimeMillis();
        }

        if (config.enableEating() && hp <= eatThreshold && canPot()) {
            if ((lastAte == Game.getClient().getTickCount() && config.comboBrew())
                    || (lastAte != Game.getClient().getTickCount())) {

                if (drinkPotion(BREW_IDS)) {
                    didAction = true;
                    missingHp += getHealed(ItemID.SARADOMIN_BREW1);
                    if (config.restoreAfterBrew()) {
                        shouldDrinkRestore = true;
                    }
                }
            }
        }

        if (shouldDrinkRestore && canPot()) {
            if (drinkPotion(RESTORE_IDS)) {
                didAction = true;
            }

            shouldDrinkRestore = false;
            shouldDrinkPrayer = false;
        } else if (shouldDrinkAntiPoison && canPot()) {
            if (drinkPotion(ANTI_POISON_IDS)) {
                didAction = true;
            } else {
                if (config.antiPoisonWarnings()) {
                    MessageUtils.addMessage("Poisoned but you don't have anti-poison!");
                }
            }

            shouldDrinkAntiPoison = false;
            lastAntiPoison = Game.getClient().getTickCount();
        }

        if (shouldDrinkAntiFire && canPot()) {
            if (drinkPotion(ANTI_FIRE_IDS)) {
                didAction = true;
            } else {
                if (config.antiFireWarnings()) {
                    MessageUtils.addMessage("Sustaining dragon fire damage but you don't have an anti-dragon fire potion!");
                }
            }

            shouldDrinkAntiFire = false;
            lastAntiFire = Game.getClient().getTickCount();
        }

        if (shouldDrinkPrayer && canPot()) {
            if (drinkPotion(PRAYER_IDS)) {
                didAction = true;
            } else {
                if (config.prayerWarnings()) {
                    MessageUtils.addMessage("Prayer points below threshold but you don't have any prayer potions!");
                }
            }

            shouldDrinkPrayer = false;
            lastPrayer = Game.getClient().getTickCount();
        }

        if (shouldDrinkStrength && canPot()) {
            if (drinkPotion(STRENGTH_IDS)) {
                didAction = true;
            } else {
                if (config.strengthWarnings()) {
                    MessageUtils.addMessage("Strength below threshold but you don't have any strength potions!");
                }
            }

            shouldDrinkStrength = false;
            lastStrength = Game.getClient().getTickCount();
        }

        if (shouldDrinkAttack && canPot()) {
            if (drinkPotion(ATTACK_IDS)) {
                didAction = true;
            } else {
                if (config.attackWarnings()) {
                    MessageUtils.addMessage("Attack below threshold but you don't have any attack potions!");
                }
            }

            shouldDrinkAttack = false;
            lastAttack = Game.getClient().getTickCount();
        }

        if (shouldDrinkDefence && canPot()) {
            if (drinkPotion(DEFENCE_IDS)) {
                didAction = true;
            } else {
                if (config.defenceWarnings()) {
                    MessageUtils.addMessage("Defence below threshold but you don't have any defence potions!");
                }
            }

            shouldDrinkDefence = false;
            lastDefence = Game.getClient().getTickCount();
        }

        if (shouldDrinkRanged && canPot()) {
            if (drinkPotion(RANGED_IDS)) {
                didAction = true;
            } else {
                if (config.rangedWarnings()) {
                    MessageUtils.addMessage("Ranged below threshold but you don't have any ranged potions!");
                }
            }

            shouldDrinkRanged = false;
            lastRanged = Game.getClient().getTickCount();
        }

        if (shouldDrinkMagic && canPot()) {
            if (drinkPotion(MAGIC_IDS)) {
                didAction = true;
            } else {
                if (config.magicWarnings()) {
                    MessageUtils.addMessage("Magic below threshold but you don't have any magic potions!");
                }
            }

            shouldDrinkMagic = false;
            lastMagic = Game.getClient().getTickCount();
        }

        if (config.drinkEnergy() && canPot() && lastEnergy + 5 < Game.getClient().getTickCount()  && Movement.getRunEnergy() <= config.energyLevel()) {
            if (drinkPotion(ENERGY_IDS)) {
                didAction = true;
            } else {
                if (config.energyWarnings()) {
                    MessageUtils.addMessage("Energy below threshold but you don't have any energy potions!");
                }
            }

            lastEnergy = Game.getClient().getTickCount();
        }

        if (config.enableEating() && hp < eatThreshold && missingHp <= 18 && config.comboKarambwan()) {
            Item karambwan = Inventory.getFirst(ItemID.COOKED_KARAMBWAN, ItemID.COOKED_KARAMBWAN_3147, ItemID.COOKED_KARAMBWAN_23533, ItemID.BLIGHTED_KARAMBWAN);
            if (karambwan != null) {
                karambwan.interact(0);
                lastAte = Game.getClient().getTickCount();
                didAction = true;
            }
        }

        if (didAction && plugin.getLastTarget() != null && !shouldDrinkAnything()) {
            plugin.getLastTarget().interact("Attack");
        }
    }

    private void generateNewEatThreshold() {
        eatThreshold = Rand.nextInt(config.minEatHP(), config.maxEatHP() + 1);
    }

    private void generateNewPrayerThreshold() {
        prayerThreshold = Rand.nextInt(config.minPrayerPoints(), config.maxPrayerPoints() + 1);
    }

    private boolean canPot() {
        return Game.getClient().getTickCount() > lastPot + 3;
    }

    private boolean canEat() {
        return Game.getClient().getTickCount() > lastAte + 3;
    }

    private void checkSkill(Skill skill) {
        checkSkill(skill, Skills.getBoostedLevel(skill));
    }

    private void checkSkill(Skill skill, int level) {
        if (Game.getState() != GameState.LOGGED_IN) return;

        if (config.drinkPotions()) {
            switch (skill) {
                case PRAYER:
                    if (config.drinkPrayer() && level <= prayerThreshold && Game.getClient().getTickCount() > lastPrayer + 5) {
                        shouldDrinkPrayer = true;
                    }
                    break;
                case STRENGTH:
                    if (config.drinkStrength() && level <= config.strengthLevel() && Game.getClient().getTickCount() > lastStrength + 5) {
                        shouldDrinkStrength = true;
                    }
                    break;
                case ATTACK:
                    if (config.drinkAttack() && level <= config.attackLevel() && Game.getClient().getTickCount() > lastAttack + 5) {
                        shouldDrinkAttack = true;
                    }
                    break;
                case DEFENCE:
                    if (config.drinkDefence() && level <= config.defenceLevel() && Game.getClient().getTickCount() > lastDefence + 5) {
                        shouldDrinkDefence = true;
                    }
                    break;
                case RANGED:
                    if (config.drinkRanged() && level <= config.rangedLevel() && Game.getClient().getTickCount() > lastRanged + 5) {
                        shouldDrinkRanged = true;
                    }
                    break;
                case MAGIC:
                    if (config.drinkMagic() && level <= config.magicLevel() && Game.getClient().getTickCount() > lastMagic + 5) {
                        shouldDrinkMagic = true;
                    }
                    break;
            }
        }
    }

    private boolean drinkPotion(Set<Integer> ids) {
        Item potion = Inventory.getFirst((i) -> ids.contains(i.getId()));
        if (potion == null) {
            return false;
        }

        potion.interact(0);
        lastPot = Game.getClient().getTickCount();
        return true;
    }

    private boolean shouldDrinkAnything() {
        return shouldDrinkStrength || shouldDrinkAttack || shouldDrinkDefence || shouldDrinkMagic || shouldDrinkRanged || shouldDrinkAntiFire || shouldDrinkAntiPoison || shouldDrinkPrayer;
    }
}
