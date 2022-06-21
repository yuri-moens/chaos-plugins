package io.reisub.unethicalite.sulliuscep;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import io.reisub.unethicalite.sulliuscep.tasks.CurePoison;
import io.reisub.unethicalite.sulliuscep.tasks.Cut;
import io.reisub.unethicalite.sulliuscep.tasks.Drop;
import io.reisub.unethicalite.sulliuscep.tasks.Eat;
import io.reisub.unethicalite.sulliuscep.tasks.FillPit;
import io.reisub.unethicalite.sulliuscep.tasks.GoToBank;
import io.reisub.unethicalite.sulliuscep.tasks.GoToFirst;
import io.reisub.unethicalite.sulliuscep.tasks.GoToFossilIsland;
import io.reisub.unethicalite.sulliuscep.tasks.HandleBank;
import io.reisub.unethicalite.sulliuscep.tasks.HandleObstacle;
import io.reisub.unethicalite.sulliuscep.tasks.Move;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import java.util.Set;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.game.Vars;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Sulliuscep",
    description = "Fungi for a fun guy",
    enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class Sulliuscep extends TickScript {

  public static final int SWAMP_LOWER_REGION_ID = 14650;
  public static final int SWAMP_UPPER_REGION_ID = 14651;
  public static final Set<Integer> SWAMP_REGION_IDS =
      ImmutableSet.of(SWAMP_LOWER_REGION_ID, SWAMP_UPPER_REGION_ID);

  private static final int VARBIT_SULLIUSCEP = 5808;
  private static final int VARBIT_PIT_FILLED = 5809;

  @Inject
  private Config config;
  @Getter
  private SulliuscepObject currentSulliuscep;
  @Getter
  private boolean pitFilled;
  @Getter
  @Setter
  private int lastDrop;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    currentSulliuscep = getCurrentSulliuscepFromVarbit();
    pitFilled = Vars.getBit(VARBIT_PIT_FILLED) == 1;

    addTask(Move.class);
    addTask(HandleBank.class);
    addTask(GoToFossilIsland.class);
    addTask(Eat.class);
    addTask(CurePoison.class);
    addTask(FillPit.class);
    addTask(GoToBank.class);
    addTask(GoToFirst.class);
    addTask(Drop.class);
    addTask(HandleObstacle.class);
    addTask(Cut.class);
  }

  @Subscribe
  private void onVarbitChanged(final VarbitChanged event) {
    if (!isRunning()) {
      return;
    }

    currentSulliuscep = getCurrentSulliuscepFromVarbit();
    pitFilled = Vars.getBit(VARBIT_PIT_FILLED) == 1;
  }

  private SulliuscepObject getCurrentSulliuscepFromVarbit() {
    final int active = Vars.getBit(VARBIT_SULLIUSCEP);

    for (SulliuscepObject sulliuscepObject : SulliuscepObject.values()) {
      if (sulliuscepObject.getVarb() == active) {
        return sulliuscepObject;
      }
    }

    return SulliuscepObject.SULLIUSCEP_1;
  }
}
