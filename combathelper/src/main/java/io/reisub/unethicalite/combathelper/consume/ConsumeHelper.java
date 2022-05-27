package io.reisub.unethicalite.combathelper.consume;

import io.reisub.unethicalite.combathelper.Helper;
import io.reisub.unethicalite.utils.Constants;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.GameState;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.api.VarPlayer;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.ItemStatChanges;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.StatsChanges;
import net.runelite.client.plugins.itemstats.stats.Stats;
import net.unethicalite.api.commons.Rand;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.game.Game;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.game.Skills;
import net.unethicalite.api.game.Vars;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.utils.MessageUtils;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.client.Static;

@Singleton
public class ConsumeHelper extends Helper {

  private static final Set<Integer> IGNORE_FOOD =
      Set.of(ItemID.DWARVEN_ROCK_CAKE, ItemID.DWARVEN_ROCK_CAKE_7510);
  private static final Set<Integer> NON_EAT_FOOD_IDS = Set.of(ItemID.JUG_OF_WINE, ItemID.BANDAGES);

  @Inject
  private ItemStatChanges statChanges;
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
  private long lastEnergy;

  @Override
  public void startUp() {
    generateNewEatThreshold();
    generateNewPrayerThreshold();
  }

  @Subscribe
  private void onGameTick(GameTick event) {
    if (Bank.isOpen() || Dialog.isOpen()) {
      return;
    }

    if (timeout > 0) {
      timeout--;
      return;
    }

    plugin.schedule(this::tick, Rand.nextInt(50, 80));
  }

  @Subscribe
  private void onVarbitChanged(VarbitChanged event) {
    if (Game.getState() != GameState.LOGGED_IN) {
      return;
    }

    if (config.drinkAntiPoison()
        && config.drinkPotions()
        && event.getIndex() == VarPlayer.POISON.getId()
        && Vars.getVarp(VarPlayer.POISON.getId()) > 0) {
      if (config.onlyCureVenom()) {
        shouldDrinkAntiPoison = Vars.getVarp(VarPlayer.POISON.getId()) >= 1000000;
      } else {
        shouldDrinkAntiPoison = true;
      }
    }
  }

  @Subscribe
  private void onChatMessage(ChatMessage event) {
    if (Game.getState() != GameState.LOGGED_IN) {
      return;
    }

    String burnMessage = ("You're horribly burnt by the dragon fire!");
    String burnExpire = ("antifire potion is about to expire.");

    String message = event.getMessage();

    if (config.drinkAntiFire()
        && config.drinkPotions()
        && (message.contains(burnMessage) || message.contains(burnExpire))) {
      shouldDrinkAntiFire = true;
    }
  }

  @Subscribe
  private void onStatChanged(StatChanged event) {
    if (Game.getState() != GameState.LOGGED_IN || timeout > 0) {
      return;
    }

    Skill skill = event.getSkill();
    int level = event.getBoostedLevel();

    checkSkill(skill, level);
  }

  @Subscribe
  private void onGameStateChanged(GameStateChanged event) {
    if (event.getGameState() == GameState.LOGGED_IN) {
      timeout = 5;
    }
  }

  @Subscribe
  private void onConfigChanged(ConfigChanged event) {
    if (!event.getGroup().equals("chaoscombathelper")) {
      return;
    }

    switch (event.getKey()) {
      case "minEatHp":
      case "maxEatHp":
        generateNewEatThreshold();
        break;
      case "minPrayerPoints":
      case "maxPrayerPoints":
        generateNewPrayerThreshold();
        // fall through
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
      default:
    }
  }

  private int getHealed(int itemId) {
    Effect effect = statChanges.get(itemId);
    if (effect != null) {
      StatsChanges statsChanges = effect.calculate(Static.getClient());
      for (StatChange statChange : statsChanges.getStatChanges()) {
        if (statChange.getStat().getName().equals(Stats.HITPOINTS.getName())) {
          return statChange.getTheoretical();
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
      Item food =
          Inventory.getFirst(
              (i) ->
                  (i.hasAction("Eat") || NON_EAT_FOOD_IDS.contains(i.getId()))
                      && !IGNORE_FOOD.contains(i.getId()));
      if (food != null) {
        missingHp += getHealed(food.getId());

        food.interact(1);
        lastAte = Static.getClient().getTickCount();
        didAction = true;
      }

      if (!didAction && config.eatWarnings()) {
        MessageUtils.addMessage("HP below threshold but you don't have any food!");
      }
    }

    if (config.enableEating() && hp <= eatThreshold && canPot()) {
      if ((lastAte == Static.getClient().getTickCount() && config.comboBrew())
          || (lastAte != Static.getClient().getTickCount())) {

        if (drinkPotion(Constants.BREW_POTION_IDS)) {
          didAction = true;
          missingHp += getHealed(ItemID.SARADOMIN_BREW1);
          if (config.restoreAfterBrew()) {
            shouldDrinkRestore = true;
          }
        }
      }
    }

    if (shouldDrinkRestore && canPot()) {
      if (drinkPotion(Constants.RESTORE_POTION_IDS)) {
        didAction = true;
      }

      shouldDrinkRestore = false;
      shouldDrinkPrayer = false;
    } else if (shouldDrinkAntiPoison && canPot()) {
      if (drinkPotion(Constants.ANTI_POISON_POTION_IDS)) {
        didAction = true;
      } else {
        if (config.antiPoisonWarnings()) {
          MessageUtils.addMessage("Poisoned but you don't have anti-poison!");
        }
      }

      shouldDrinkAntiPoison = false;
      lastAntiPoison = Static.getClient().getTickCount();
    }

    if (shouldDrinkAntiFire && canPot()) {
      if (drinkPotion(Constants.ANTI_FIRE_POTION_IDS)) {
        didAction = true;
      } else {
        if (config.antiFireWarnings()) {
          MessageUtils.addMessage(
              "Sustaining dragon fire damage but you don't have an anti-dragon fire potion!");
        }
      }

      shouldDrinkAntiFire = false;
      lastAntiFire = Static.getClient().getTickCount();
    }

    if (shouldDrinkPrayer && canPot()) {
      if (drinkPotion(Constants.PRAYER_RESTORE_POTION_IDS)) {
        didAction = true;
      } else {
        if (config.prayerWarnings()) {
          MessageUtils.addMessage(
              "Prayer points below threshold but you don't have any prayer potions!");
        }
      }

      shouldDrinkPrayer = false;
      lastPrayer = Static.getClient().getTickCount();
    }

    if (shouldDrinkStrength && canPot()) {
      if (drinkPotion(Constants.STRENGTH_POTION_IDS)) {
        didAction = true;
      } else {
        if (config.strengthWarnings()) {
          MessageUtils.addMessage(
              "Strength below threshold but you don't have any strength potions!");
        }
      }

      shouldDrinkStrength = false;
      lastStrength = Static.getClient().getTickCount();
    }

    if (shouldDrinkAttack && canPot()) {
      if (drinkPotion(Constants.ATTACK_POTION_IDS)) {
        didAction = true;
      } else {
        if (config.attackWarnings()) {
          MessageUtils.addMessage("Attack below threshold but you don't have any attack potions!");
        }
      }

      shouldDrinkAttack = false;
      lastAttack = Static.getClient().getTickCount();
    }

    if (shouldDrinkDefence && canPot()) {
      if (drinkPotion(Constants.DEFENCE_POTION_IDS)) {
        didAction = true;
      } else {
        if (config.defenceWarnings()) {
          MessageUtils.addMessage(
              "Defence below threshold but you don't have any defence potions!");
        }
      }

      shouldDrinkDefence = false;
      lastDefence = Static.getClient().getTickCount();
    }

    if (shouldDrinkRanged && canPot()) {
      if (drinkPotion(Constants.RANGED_POTION_IDS)) {
        didAction = true;
      } else {
        if (config.rangedWarnings()) {
          MessageUtils.addMessage("Ranged below threshold but you don't have any ranged potions!");
        }
      }

      shouldDrinkRanged = false;
      lastRanged = Static.getClient().getTickCount();
    }

    if (shouldDrinkMagic && canPot()) {
      if (drinkPotion(Constants.MAGIC_POTION_IDS)) {
        didAction = true;
      } else {
        if (config.magicWarnings()) {
          MessageUtils.addMessage("Magic below threshold but you don't have any magic potions!");
        }
      }

      shouldDrinkMagic = false;
      lastMagic = Static.getClient().getTickCount();
    }

    if (config.drinkEnergy()
        && canPot()
        && lastEnergy + 5 < Static.getClient().getTickCount()
        && Movement.getRunEnergy() <= config.energyLevel()) {
      if (drinkPotion(Constants.ENERGY_POTION_IDS)) {
        didAction = true;
      } else {
        if (config.energyWarnings()) {
          MessageUtils.addMessage("Energy below threshold but you don't have any energy potions!");
        }
      }

      lastEnergy = Static.getClient().getTickCount();
    }

    if (config.enableEating() && hp < eatThreshold && missingHp >= 18 && config.comboKarambwan()) {
      Item karambwan =
          Inventory.getFirst(
              ItemID.COOKED_KARAMBWAN,
              ItemID.COOKED_KARAMBWAN_3147,
              ItemID.COOKED_KARAMBWAN_23533,
              ItemID.BLIGHTED_KARAMBWAN
          );
      if (karambwan != null) {
        karambwan.interact(1);
        lastAte = Static.getClient().getTickCount();
        didAction = true;
      }
    }

    if (didAction && plugin.getLastTarget() != null && !shouldDrinkAnything()) {
      GameThread.invoke(() -> plugin.getLastTarget().interact("Attack"));
    }
  }

  private void generateNewEatThreshold() {
    eatThreshold = Rand.nextInt(config.minEatHp(), config.maxEatHp() + 1);
  }

  private void generateNewPrayerThreshold() {
    prayerThreshold = Rand.nextInt(config.minPrayerPoints(), config.maxPrayerPoints() + 1);
  }

  private boolean canPot() {
    return Static.getClient().getTickCount() > lastPot + 3;
  }

  private boolean canEat() {
    return Static.getClient().getTickCount() > lastAte + 3;
  }

  private void checkSkill(Skill skill) {
    checkSkill(skill, Skills.getBoostedLevel(skill));
  }

  private void checkSkill(Skill skill, int level) {
    if (Game.getState() != GameState.LOGGED_IN) {
      return;
    }

    if (config.drinkPotions()) {
      switch (skill) {
        case PRAYER:
          if (config.drinkPrayer()
              && level <= prayerThreshold
              && Static.getClient().getTickCount() > lastPrayer + 5) {
            shouldDrinkPrayer = true;
          }
          break;
        case STRENGTH:
          if (config.drinkStrength()
              && level <= config.strengthLevel()
              && Static.getClient().getTickCount() > lastStrength + 5) {
            shouldDrinkStrength = true;
          }
          break;
        case ATTACK:
          if (config.drinkAttack()
              && level <= config.attackLevel()
              && Static.getClient().getTickCount() > lastAttack + 5) {
            shouldDrinkAttack = true;
          }
          break;
        case DEFENCE:
          if (config.drinkDefence()
              && level <= config.defenceLevel()
              && Static.getClient().getTickCount() > lastDefence + 5) {
            shouldDrinkDefence = true;
          }
          break;
        case RANGED:
          if (config.drinkRanged()
              && level <= config.rangedLevel()
              && Static.getClient().getTickCount() > lastRanged + 5) {
            shouldDrinkRanged = true;
          }
          break;
        case MAGIC:
          if (config.drinkMagic()
              && level <= config.magicLevel()
              && Static.getClient().getTickCount() > lastMagic + 5) {
            shouldDrinkMagic = true;
          }
          break;
        default:
      }
    }
  }

  private boolean drinkPotion(Set<Integer> ids) {
    Item potion = Inventory.getFirst((i) -> ids.contains(i.getId()));
    if (potion == null) {
      return false;
    }

    if (potion.getName().contains("ombat potion")) {
      shouldDrinkAttack = false;
      shouldDrinkStrength = false;
      shouldDrinkDefence = false;
    }

    potion.interact(1);
    lastPot = Static.getClient().getTickCount();
    return true;
  }

  private boolean shouldDrinkAnything() {
    return shouldDrinkStrength
        || shouldDrinkAttack
        || shouldDrinkDefence
        || shouldDrinkMagic
        || shouldDrinkRanged
        || shouldDrinkAntiFire
        || shouldDrinkAntiPoison
        || shouldDrinkPrayer;
  }
}
