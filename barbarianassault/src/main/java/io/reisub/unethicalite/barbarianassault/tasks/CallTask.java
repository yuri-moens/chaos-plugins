package io.reisub.unethicalite.barbarianassault.tasks;

import io.reisub.unethicalite.barbarianassault.BarbarianAssault;
import io.reisub.unethicalite.barbarianassault.data.Call;
import io.reisub.unethicalite.barbarianassault.data.Role;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.Item;
import net.runelite.api.widgets.Widget;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.widgets.Widgets;

public class CallTask extends Task {

  @Inject
  private BarbarianAssault plugin;

  @Override
  public String getStatus() {
    return "Calling";
  }

  @Override
  public boolean validate() {
    final Call currentCall = getCurrentCall();

    return currentCall != null
        && currentCall != plugin.getLastCall();
  }

  @Override
  public void execute() {
    final Call currentCall = getCurrentCall();

    if (currentCall == null) {
      return;
    }

    if (plugin.getLastCall() == null) {
      Time.sleepTicks(2);
    }

    plugin.setLastCall(currentCall);

    final Item horn = Inventory.getFirst(plugin.getRole().getHorn());

    if (horn == null) {
      return;
    }

    Time.sleepTick();
    horn.interact(currentCall.getAction());
  }

  private Call getCurrentCall() {
    final Role role = plugin.getRole();

    if (role ==  null) {
      return null;
    }

    final Widget widget =
        Widgets.get(plugin.getRole().getGroupId(), plugin.getRole().getShoutIndex());

    if (widget == null || widget.getText() == null) {
      return null;
    }

    final String text = widget.getText();

    for (Call call : plugin.getRole().getCalls()) {
      if (text.contains(call.getWidgetString())) {
        return call;
      }
    }

    return null;
  }
}
