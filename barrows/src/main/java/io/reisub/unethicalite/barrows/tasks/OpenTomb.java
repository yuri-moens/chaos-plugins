package io.reisub.unethicalite.barrows.tasks;

import dev.hoot.api.commons.Time;
import dev.hoot.api.entities.Players;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.movement.Movement;
import dev.hoot.api.widgets.Dialog;
import dev.hoot.api.widgets.DialogOption;
import dev.hoot.bot.managers.Static;
import io.reisub.unethicalite.barrows.Barrows;
import io.reisub.unethicalite.barrows.Brother;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.api.Predicates;
import io.reisub.unethicalite.utils.tasks.Task;
import net.runelite.api.TileObject;

import javax.inject.Inject;

public class OpenTomb extends Task {
    @Inject
    private Barrows plugin;

    @Override
    public String getStatus() {
        return "Opening tomb";
    }

    @Override
    public boolean validate() {
        Brother brother = Brother.getBrotherByCrypt();

        return brother != null
                && !brother.isDead()
                && Static.getClient().getHintArrowNpc() == null;
    }

    @Override
    public void execute() {
        plugin.setNewRun(false);

        TileObject sarcophagus = TileObjects.getNearest(Predicates.ids(Barrows.SARCOPHAGUS_IDS));
        if (sarcophagus == null) {
            return;
        }

        sarcophagus.interact(0);

        Time.sleepTicksUntil(() -> Dialog.isOpen() || Static.getClient().getHintArrowNpc() != null, 20);

        if (Dialog.isOpen()) {
            plugin.getCurrentBrother().setInTunnel(true);

            boolean isLastBrother = true;

            for (Brother brother : Brother.values()) {
                if (!brother.isDead() && !brother.isInTunnel()) {
                    isLastBrother = false;
                }
            }

            if (isLastBrother) {
                Dialog.invokeDialog(
                        DialogOption.PLAIN_CONTINUE,
                        DialogOption.CHAT_OPTION_ONE
                );

                Time.sleepTicksUntil(() -> Players.getLocal().getWorldLocation().getPlane() != 3, 5);
            } else {
                TileObject stairs = TileObjects.getNearest(Predicates.ids(Barrows.STAIRCASE_IDS));
                if (stairs == null) {
                    return;
                }

                stairs.interact(0);
                Time.sleepTicksUntil(() -> Utils.isInRegion(Barrows.BARROWS_REGION), 10);
            }
        } else {
            if (plugin.getCurrentBrother() != Brother.KARIL) {
                Movement.walk(plugin.getCurrentBrother().getPointNextToStairs());
            }
        }
    }
}