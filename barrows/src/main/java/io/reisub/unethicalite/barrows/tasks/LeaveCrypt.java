package io.reisub.unethicalite.barrows.tasks;

import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.barrows.Brother;
import io.reisub.unethicalite.combathelper.CombatHelper;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.TileObject;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;

public class LeaveCrypt extends Task {
  @Inject private CombatHelper combatHelper;

  @Override
  public String getStatus() {
    return "Leaving crypt";
  }

  @Override
  public boolean validate() {
    return Utils.isInRegion(Barrows.CRYPT_REGION)
        && Players.getLocal().getWorldLocation().getPlane() == 3
        && Brother.getBrotherByCrypt() != null
        && Brother.getBrotherByCrypt().isDead();
  }

  @Override
  public void execute() {
    TileObject stairs = TileObjects.getNearest(Predicates.ids(Barrows.STAIRCASE_IDS));
    if (stairs == null) {
      return;
    }

    if (combatHelper.getPrayerHelper().isFlicking()) {
      combatHelper.getPrayerHelper().toggleFlicking();
    }

    stairs.interact(0);
    Time.sleepTicksUntil(() -> Utils.isInRegion(Barrows.BARROWS_REGION), 10);
  }
}
