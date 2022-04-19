package io.reisub.unethicalite.barrows.tasks;

import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.items.Inventory;
import dev.unethicalite.api.widgets.Prayers;
import dev.unethicalite.managers.Static;
import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.barrows.Brother;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import java.time.temporal.ChronoUnit;
import javax.inject.Inject;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.infobox.LoopTimer;

public class DrinkPrayerPotion extends Task {
  private static final long PRAYER_DRAIN_INTERVAL_MS = 18200;
  @Inject private Barrows plugin;
  private LoopTimer prayerDrainTimer;

  @Override
  public String getStatus() {
    return "Drinking prayer potion";
  }

  @Override
  public boolean validate() {
    Brother current = plugin.getCurrentBrother();

    return Prayers.getPoints() == 0
        && Inventory.contains(Predicates.ids(Constants.PRAYER_RESTORE_POTION_IDS))
        && (prayerDrainTimer != null && prayerDrainTimer.getDuration().toMillis() > 4000)
        && (current == Brother.DHAROK || current == Brother.AHRIM || current == Brother.KARIL)
        && Static.getClient().getHintArrowNpc() != null
        && Players.getLocal().getWorldLocation().getPlane() == 0;
  }

  @Override
  public void execute() {
    Inventory.getFirst(Predicates.ids(Constants.PRAYER_RESTORE_POTION_IDS)).interact(0);
  }

  @Subscribe
  private void onGameStateChanged(GameStateChanged event) {
    if (plugin.isRunning() && event.getGameState() == GameState.LOGGED_IN) {
      boolean isInCrypt = Utils.isInRegion(Barrows.CRYPT_REGION);

      if (isInCrypt && prayerDrainTimer == null) {
        prayerDrainTimer =
            new LoopTimer(PRAYER_DRAIN_INTERVAL_MS, ChronoUnit.MILLIS, null, plugin, true);
      } else if (!isInCrypt && prayerDrainTimer != null) {
        prayerDrainTimer = null;
      }
    }
  }
}
