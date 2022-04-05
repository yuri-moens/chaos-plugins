package io.reisub.unethicalite.mahoganyhomes.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.magic.Lunar;
import dev.hoot.api.widgets.Dialog;
import dev.hoot.api.widgets.DialogOption;
import dev.hoot.api.widgets.Widgets;
import io.reisub.unethicalite.mahoganyhomes.Config;
import io.reisub.unethicalite.mahoganyhomes.MahoganyHomes;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.widgets.Widget;

import javax.inject.Inject;

public class GetTask extends Task {
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
        Widget npcContact = Widgets.get(Lunar.NPC_CONTACT.getWidget());
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
        }

        Dialog.invokeDialog(
            chooseOption
        );
    }
}
