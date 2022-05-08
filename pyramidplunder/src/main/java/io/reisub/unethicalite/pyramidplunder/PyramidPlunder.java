package io.reisub.unethicalite.pyramidplunder;

import com.google.inject.Provides;
import dev.unethicalite.api.game.Skills;
import dev.unethicalite.api.game.Vars;
import io.reisub.unethicalite.pyramidplunder.tasks.DrinkAntiPoison;
import io.reisub.unethicalite.pyramidplunder.tasks.GoToBank;
import io.reisub.unethicalite.pyramidplunder.tasks.GoToPyramid;
import io.reisub.unethicalite.pyramidplunder.tasks.HandleBank;
import io.reisub.unethicalite.pyramidplunder.tasks.LeaveWrongGuardianRoom;
import io.reisub.unethicalite.pyramidplunder.tasks.LootChest;
import io.reisub.unethicalite.pyramidplunder.tasks.LootUrn;
import io.reisub.unethicalite.pyramidplunder.tasks.OpenDoor;
import io.reisub.unethicalite.pyramidplunder.tasks.PassTrap;
import io.reisub.unethicalite.pyramidplunder.tasks.StartGame;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Skill;
import net.runelite.api.Varbits;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Pyramid Plunder",
    description = "Plundering that booty",
    enabledByDefault = false)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class PyramidPlunder extends TickScript {

  public static final int PYRAMID_PLUNDER_REGION = 7749;
  public static final int SOPHANEM_REGION = 13099;
  public static final int SOPHANEM_BANK_REGION = 11088;

  @Inject
  private Config config;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    addTask(DrinkAntiPoison.class);
    addTask(PassTrap.class);
    addTask(LootUrn.class);
    addTask(LootChest.class);
    addTask(OpenDoor.class);
    addTask(GoToBank.class);
    addTask(HandleBank.class);
    addTask(GoToPyramid.class);
    addTask(LeaveWrongGuardianRoom.class);
    addTask(StartGame.class);
  }

  public static boolean isInPyramidPlunder() {
    return Utils.isInRegion(PYRAMID_PLUNDER_REGION)
        && Vars.getBit(Varbits.PYRAMID_PLUNDER_TIMER) > 0;
  }

  public static boolean isPenultimateRoom() {
    final int roomLevel = Vars.getBit(Varbits.PYRAMID_PLUNDER_THIEVING_LEVEL);

    return Skills.getLevel(Skill.THIEVING) - roomLevel >= 10
        && Skills.getLevel(Skill.THIEVING) - roomLevel < 20;
  }

  public static boolean isLastRoom() {
    final int roomLevel = Vars.getBit(Varbits.PYRAMID_PLUNDER_THIEVING_LEVEL);

    return Skills.getLevel(Skill.THIEVING) - roomLevel >= 0
        && Skills.getLevel(Skill.THIEVING) - roomLevel < 10;
  }

  public static boolean isPastTraps() {
    return Vars.getBit(Varbits.PYRAMID_PLUNDER_ROOM_LOCATION) == 0;
  }
}
