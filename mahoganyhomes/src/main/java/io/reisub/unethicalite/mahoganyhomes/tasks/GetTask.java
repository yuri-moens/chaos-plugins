package io.reisub.unethicalite.mahoganyhomes.tasks;

import io.reisub.unethicalite.mahoganyhomes.Config;
import io.reisub.unethicalite.mahoganyhomes.Home;
import io.reisub.unethicalite.mahoganyhomes.MahoganyHomes;
import io.reisub.unethicalite.utils.tasks.Task;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import net.runelite.api.DialogOption;
import net.runelite.api.util.Text;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.unethicalite.api.commons.Time;
import net.unethicalite.api.magic.SpellBook;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.api.widgets.Widgets;

public class GetTask extends Task {

  private static final Pattern CONTRACT_PATTERN = Pattern.compile(
      "(Please could you g|G)o see (\\w*)[ ,][\\w\\s,-]*[?.] "
          + "You can get another job once you have furnished \\w* home\\.");

  @Inject
  private MahoganyHomes plugin;
  @Inject
  private Config config;

  @Override
  public String getStatus() {
    return "Getting a new task";
  }

  @Override
  public boolean validate() {
    return plugin.getCurrentHome() == null;
  }

  @Override
  public void execute() {
    plugin.setLastStairsUsed(0);
    plugin.setFixed(false);

    final Widget npcContact = Widgets.get(SpellBook.Lunar.NPC_CONTACT.getWidget());
    if (npcContact == null) {
      return;
    }

    if (npcContact.hasAction("Amy")) {
      npcContact.interact("Amy");
    } else {
      npcContact.interact(0);
      Time.sleepTicksUntil(() -> Widgets.isVisible(Widgets.get(75, 12)), 5);

      Widgets.get(75, 12).interact("Amy");
    }

    Time.sleepTicksUntil(Dialog::canContinue, 20);

    DialogOption chooseOption = null;

    switch (config.plank()) {
      case NORMAL:
        chooseOption = DialogOption.CHAT_OPTION_ONE;
        break;
      case OAK:
        chooseOption = DialogOption.CHAT_OPTION_TWO;
        break;
      case TEAK:
        chooseOption = DialogOption.CHAT_OPTION_THREE;
        break;
      case MAHOGANY:
        chooseOption = DialogOption.CHAT_OPTION_FOUR;
        break;
      default:
    }

    Dialog.invokeDialog(
        DialogOption.PLAYER_CONTINUE,
        DialogOption.NPC_CONTINUE,
        chooseOption,
        DialogOption.PLAYER_CONTINUE
    );

    Time.sleepTicks(2);

    final Home home = getHome();

    if (home != null) {
      plugin.setCurrentHome(home);
    }
  }

  private Home getHome() {

    final Widget dialog = Widgets.get(WidgetInfo.DIALOG_NPC_TEXT);

    if (dialog == null) {
      return null;
    }

    final String npcText = Text.sanitizeMultilineText(dialog.getText());
    final Matcher startContractMatcher = CONTRACT_PATTERN.matcher(npcText);

    if (startContractMatcher.matches()) {
      final String name = startContractMatcher.group(2);
      for (final Home h : Home.values()) {
        if (h.getName().equalsIgnoreCase(name)) {
          return h;
        }
      }
    }

    return null;
  }
}
