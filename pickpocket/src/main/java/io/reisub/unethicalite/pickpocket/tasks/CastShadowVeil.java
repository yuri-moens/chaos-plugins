package io.reisub.unethicalite.pickpocket.tasks;

import dev.unethicalite.api.entities.Players;
import dev.unethicalite.api.game.Skills;
import dev.unethicalite.api.magic.SpellBook;
import dev.unethicalite.api.widgets.Widgets;
import dev.unethicalite.client.Static;
import io.reisub.unethicalite.pickpocket.Config;
import io.reisub.unethicalite.pickpocket.Pickpocket;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Task;
import javax.inject.Inject;
import net.runelite.api.MenuAction;
import net.runelite.api.Skill;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

public class CastShadowVeil extends Task {

  @Inject
  private Pickpocket plugin;

  @Inject
  private Config config;

  private int last;

  @Override
  public String getStatus() {
    return "Casting Shadow Veil";
  }

  @Override
  public boolean validate() {
    return config.castShadowVeil()
        && plugin.getCurrentActivity() == Activity.IDLE
        && SpellBook.Necromancy.SHADOW_VEIL.canCast()
        && Players.getLocal().getModelHeight() != 1000
        && Players.getLocal().distanceTo(config.target().getNearest().getPickpocketLocation()) < 5
        && last + Skills.getLevel(Skill.MAGIC) - 1 < Static.getClient().getTickCount();
  }

  @Override
  public void execute() {
    final Widget w = Widgets.get(WidgetInfo.SPELL_SHADOW_VEIL);
    if (w == null) {
      return;
    }

    w.interact(1, MenuAction.CC_OP.getId(), -1,
        SpellBook.Necromancy.SHADOW_VEIL.getWidget().getId());
    last = Static.getClient().getTickCount();
  }
}
