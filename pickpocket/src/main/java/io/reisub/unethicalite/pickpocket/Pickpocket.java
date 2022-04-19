package io.reisub.unethicalite.pickpocket;

import com.google.inject.Provides;
import dev.unethicalite.api.entities.Players;
import io.reisub.unethicalite.pickpocket.tasks.CastShadowVeil;
import io.reisub.unethicalite.pickpocket.tasks.ClearInventory;
import io.reisub.unethicalite.pickpocket.tasks.Eat;
import io.reisub.unethicalite.pickpocket.tasks.EquipDodgyNecklace;
import io.reisub.unethicalite.pickpocket.tasks.HandleBank;
import io.reisub.unethicalite.pickpocket.tasks.TakeWine;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.enums.Activity;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Skill;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.itemstats.ItemStatPlugin;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Pickpocket",
    description = "Cor blimey mate, what are ye doing in me pockets?",
    enabledByDefault = false
)
@PluginDependency(Utils.class)
@PluginDependency(ItemStatPlugin.class)
@Slf4j
@Extension
public class Pickpocket extends TickScript {
  @Inject
  private Config config;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Getter
  private Target.Location nearestLocation;

  @Override
  protected void onStart() {
    super.onStart();

    nearestLocation = config.target().getNearest();

    addTask(ClearInventory.class);
    addTask(TakeWine.class);
    addTask(Eat.class);
    addTask(CastShadowVeil.class);
    addTask(EquipDodgyNecklace.class);
    addTask(HandleBank.class);
    addTask(io.reisub.unethicalite.pickpocket.tasks.Pickpocket.class);
  }

  @Subscribe
  private void onAnimationChanged(AnimationChanged event) {
    if (!isRunning()) {
      return;
    }

    Actor actor = event.getActor();
    if (actor == null || !actor.equals(Players.getLocal())) return;

    switch (Players.getLocal().getAnimation()) {
      case 388:
        setActivity(Activity.IDLE);
        break;
    }
  }

  @Subscribe
  private void onStatChanged(StatChanged event) {
    if (event.getSkill() == Skill.THIEVING) {
      setActivity(Activity.IDLE);
    }
  }
}
