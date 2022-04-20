package io.reisub.unethicalite.combathelper.swap;

import com.openosrs.client.util.WeaponStyle;
import dev.unethicalite.api.commons.Rand;
import dev.unethicalite.api.entities.NPCs;
import dev.unethicalite.api.game.Combat;
import dev.unethicalite.api.game.GameThread;
import dev.unethicalite.api.game.Skills;
import dev.unethicalite.api.game.Vars;
import dev.unethicalite.api.items.Inventory;
import io.reisub.unethicalite.combathelper.Helper;
import io.reisub.unethicalite.combathelper.prayer.PrayerHelper;
import io.reisub.unethicalite.combathelper.prayer.QuickPrayer;
import io.reisub.unethicalite.utils.api.ConfigList;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Item;
import net.runelite.api.NPC;
import net.runelite.api.Skill;
import net.runelite.api.Varbits;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;

@Singleton
public class SwapHelper extends Helper {
  @Inject private PrayerHelper prayerHelper;

  private ConfigList meleeList;
  private ConfigList rangedList;
  private ConfigList magicList;

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
    meleeList = ConfigList.parseList(config.meleeGear());
    rangedList = ConfigList.parseList(config.rangedGear());
    magicList = ConfigList.parseList(config.magicGear());
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
    if (plugin.getLastTarget() == null || !config.autoSwap()) {
      return;
    }

    NPC target = null;

    List<NPC> npcList =
        NPCs.getAll((n) -> n.getWorldLocation().equals(plugin.getLastTarget().getWorldLocation()));
    if (npcList != null && npcList.size() > 0) {
      target = npcList.get(0);
    }

    if (target == null || target.getComposition().getOverheadIcon() == null) {
      return;
    }

    WeaponStyle currentStyle = Combat.getCurrentWeaponStyle();

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
      default:
    }
  }

  public void swap(boolean offensivePrayers, boolean defensivePrayers, WeaponStyle... styles) {
    for (WeaponStyle style : styles) {
      Set<Integer> ids;
      Set<String> names;

      switch (style) {
        case MELEE:
          ids = meleeList.getIntegers();
          names = meleeList.getStrings();
          break;
        case RANGE:
          ids = rangedList.getIntegers();
          names = rangedList.getStrings();
          break;
        case MAGIC:
          ids = magicList.getIntegers();
          names = magicList.getStrings();
          break;
        default:
          ids = null;
          names = null;
      }

      List<Item> items =
          Inventory.getAll(
              i -> {
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

        switch (style) {
          case MELEE:
            if (offensivePrayers) {
              prayers.addAll(
                  QuickPrayer.getBestMeleeBuff(
                      level, Vars.getBit(Varbits.CAMELOT_TRAINING_ROOM_STATUS) == 8));
            }

            if (defensivePrayers) {
              prayers.add(config.meleePrayer().getQuickPrayer());
            }
            break;
          case RANGE:
            if (offensivePrayers) {
              prayers.addAll(
                  QuickPrayer.getBestRangedBuff(level, Vars.getBit(Varbits.RIGOUR_UNLOCKED) != 0));
            }

            if (defensivePrayers) {
              prayers.add(config.rangedPrayer().getQuickPrayer());
            }
            break;
          case MAGIC:
            if (offensivePrayers) {
              prayers.addAll(
                  QuickPrayer.getBestMagicBuff(level, Vars.getBit(Varbits.AUGURY_UNLOCKED) != 0));
            }

            if (defensivePrayers) {
              prayers.add(config.magicPrayer().getQuickPrayer());
            }
            break;
          default:
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

  public void swap(WeaponStyle... styles) {
    swap(config.swapOffensivePrayers(), config.swapDefensivePrayers(), styles);
  }
}
