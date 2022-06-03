package io.reisub.unethicalite.dialogcontinue;

import io.reisub.unethicalite.utils.Utils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.widgets.Dialog;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Dialog Continue",
    description = "",
    enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class DialogContinue extends Plugin {

  @Subscribe
  private void onGameTick(GameTick event) {
    //Choose Quest Helper option
    if (Dialog.isViewingOptions()) {
      List<Widget> options = Dialog.getOptions();

      for (Widget opt : options) {
        if (opt.getText().startsWith("[")) {
          Dialog.chooseOption(opt.getIndex());
          return;
        }
      }
    }

    //Spacebar continue any dialog
    if (Dialog.canContinue()) {
      Dialog.continueSpace();
    }
  }
}
