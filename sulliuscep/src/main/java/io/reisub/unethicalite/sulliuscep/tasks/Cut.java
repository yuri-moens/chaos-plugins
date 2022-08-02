package io.reisub.unethicalite.sulliuscep.tasks;

import com.google.common.collect.ImmutableSet;
import io.reisub.unethicalite.sulliuscep.Sulliuscep;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.AnimationID;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.client.Static;

public class Cut extends Task {

  private static final ImmutableSet<Integer> ANIMATION_IDS = ImmutableSet.of(
      AnimationID.WOODCUTTING_BRONZE,
      AnimationID.WOODCUTTING_IRON,
      AnimationID.WOODCUTTING_STEEL,
      AnimationID.WOODCUTTING_BLACK,
      AnimationID.WOODCUTTING_MITHRIL,
      AnimationID.WOODCUTTING_ADAMANT,
      AnimationID.WOODCUTTING_RUNE,
      AnimationID.WOODCUTTING_GILDED,
      AnimationID.WOODCUTTING_DRAGON,
      AnimationID.WOODCUTTING_DRAGON_OR,
      AnimationID.WOODCUTTING_INFERNAL,
      AnimationID.WOODCUTTING_3A_AXE,
      AnimationID.WOODCUTTING_CRYSTAL,
      AnimationID.WOODCUTTING_TRAILBLAZER
  );

  @Inject
  private Sulliuscep plugin;
  @Inject
  private CurePoison curePoisonTask;
  @Inject
  private Eat eatTask;

  @Override
  public String getStatus() {
    return "Cutting sulliuscep";
  }

  @Override
  public boolean validate() {
    return !Inventory.isFull()
        && (!isWoodcutting()
        || Static.getClient().getTickCount() - plugin.getLastDrop() <= 1)
        && plugin.getCurrentSulliuscep().isReachable();
  }

  @Override
  public int execute() {
    final TileObject sulliuscep = plugin.getCurrentSulliuscep().getObject();

    if (sulliuscep == null) {
      return 1;
    }

    if (Combat.getSpecEnergy() == 100 && Equipment.contains(ItemID.DRAGON_AXE)) {
      Combat.toggleSpec();
    }

    GameThread.invoke(() -> sulliuscep.interact("Cut"));

    if (!Time.sleepTicksUntil(() -> Players.getLocal().isMoving()
        || isWoodcutting(), 3)) {
      return 1;
    }

    Time.sleepTicksUntil(() -> isWoodcutting()
        || Combat.isPoisoned()
        || Combat.getCurrentHealth() < 30, 35);

    if (Combat.isPoisoned()) {
      curePoisonTask.execute();
      Time.sleepTick();

      GameThread.invoke(() -> sulliuscep.interact("Cut"));
      Time.sleepTicks(5);

      Time.sleepTicksUntil(this::isWoodcutting, 35);
    }

    if (Combat.getCurrentHealth() < 30) {
      eatTask.execute();
      Time.sleepTick();

      GameThread.invoke(() -> sulliuscep.interact("Cut"));
      Time.sleepTicks(5);

      Time.sleepTicksUntil(this::isWoodcutting, 35);
    }

    return 4;
  }

  private boolean isWoodcutting() {
    return ANIMATION_IDS.contains(Players.getLocal().getAnimation());
  }
}
