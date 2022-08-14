package io.reisub.unethicalite.barbarianassault.tasks;

import com.google.common.collect.ImmutableMap;
import io.reisub.unethicalite.barbarianassault.BarbarianAssault;
import io.reisub.unethicalite.barbarianassault.data.Role;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Widgets;

public class EquipArrows extends Task {

  @Inject
  private BarbarianAssault plugin;
  private final Map<String, Integer> arrowsMap = ImmutableMap.of(
      "Bullet", ItemID.BULLET_ARROW,
      "Field", ItemID.FIELD_ARROW,
      "Blunt", ItemID.BLUNT_ARROW,
      "Barbed", ItemID.BARBED_ARROW
  );

  @Override
  public String getStatus() {
    return "Equipping arrows";
  }

  @Override
  public boolean validate() {
    if (plugin.getRole() != Role.ATTACKER) {
      return false;
    }

    return shouldSwapArrows();
  }

  @Override
  public void execute() {
    final Widget widget =
        Widgets.get(plugin.getRole().getGroupId(), plugin.getRole().getListenIndex());

    for (Map.Entry<String, Integer> entry : arrowsMap.entrySet()) {
      if (widget.getText().contains(entry.getKey())
          && Inventory.contains(entry.getValue())) {
        Inventory.getFirst(entry.getValue()).interact("Wield");
      }
    }

    final NPC target = NPCs.getNearest("Penance Ranger", "Penance Fighter");

    if (target != null) {
      GameThread.invoke(() -> target.interact("Attack"));
    }
  }

  private boolean shouldSwapArrows() {
    final Widget widget =
        Widgets.get(plugin.getRole().getGroupId(), plugin.getRole().getListenIndex());

    for (Map.Entry<String, Integer> entry : arrowsMap.entrySet()) {
      if (widget.getText().contains(entry.getKey())
          && Inventory.contains(entry.getValue())) {
        return true;
      }
    }

    return false;
  }
}
