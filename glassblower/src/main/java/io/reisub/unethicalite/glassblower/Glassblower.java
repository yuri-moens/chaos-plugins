package io.reisub.unethicalite.glassblower;

import com.google.inject.Provides;
import io.reisub.unethicalite.glassblower.tasks.Blow;
import io.reisub.unethicalite.glassblower.tasks.HandleBank;
import io.reisub.unethicalite.glassblower.tasks.PickupSeed;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.enums.Activity;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Skill;
import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Glassblower",
    description = "Huffs and puffs",
    enabledByDefault = false)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class Glassblower extends TickScript {
  public static final int FOSSIL_ISLAND_SMALL_ISLAND_REGION = 14908;
  public static final int FOSSIL_ISLAND_UNDERWATER_REGION = 15008;
  @Inject private Config config;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    idleCheckSkills.put(Skill.CRAFTING, Activity.GLASSBLOWING);

    addTask(PickupSeed.class);
    addTask(HandleBank.class);
    addTask(Blow.class);
  }

  @Subscribe
  private void onStatChanged(StatChanged event) {
    if (event.getSkill() == Skill.CRAFTING) {
      setActivity(Activity.GLASSBLOWING);
    }
  }
}
