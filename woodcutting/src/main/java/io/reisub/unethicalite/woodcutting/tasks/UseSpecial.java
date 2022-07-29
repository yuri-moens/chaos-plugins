package io.reisub.unethicalite.woodcutting.tasks;

import com.google.common.collect.ImmutableSet;
import io.reisub.unethicalite.utils.tasks.Task;
import io.reisub.unethicalite.woodcutting.Config;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.game.Combat;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.game.Skills;
import net.unethicalite.api.items.Equipment;

public class UseSpecial extends Task {

  private static final Set<Integer> SPECIAL_AXE_IDS = ImmutableSet.of(
      ItemID.DRAGON_AXE,
      ItemID.DRAGON_AXE_OR,
      ItemID.INFERNAL_AXE,
      ItemID.INFERNAL_AXE_OR,
      ItemID.CRYSTAL_AXE,
      ItemID.CRYSTAL_AXE_INACTIVE
  );

  @Inject
  private Chop chopTask;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Using special";
  }

  @Override
  public boolean validate() {
    return Skills.getLevel(Skill.WOODCUTTING) != 99
        && Combat.getSpecEnergy() == 100
        && Players.getLocal().isAnimating()
        && Equipment.contains(Predicates.ids(SPECIAL_AXE_IDS))
        && chopTask.getCurrentTreePosition() != null;
  }

  @Override
  public void execute() {
    Combat.toggleSpec();

    final TileObject tree = TileObjects.getFirstAt(
        chopTask.getCurrentTreePosition()
            .dx(config.location().getXoffset())
            .dy(config.location().getYoffset()),
        Predicates.ids(config.location().getTreeIds())
    );

    if (tree == null) {
      chopTask.setCurrentTreePosition(null);
      return;
    }

    GameThread.invoke(() -> tree.interact("Chop down"));
  }
}
