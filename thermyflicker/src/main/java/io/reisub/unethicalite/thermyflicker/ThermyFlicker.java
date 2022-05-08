package io.reisub.unethicalite.thermyflicker;

import com.google.inject.Provides;
import io.reisub.unethicalite.thermyflicker.tasks.Attack;
import io.reisub.unethicalite.thermyflicker.tasks.DrinkPrayerPotion;
import io.reisub.unethicalite.thermyflicker.tasks.MoveUnder;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Thermy Flicker",
    description = "Flicks against the Thermonuclear smoke devil",
    enabledByDefault = false)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class ThermyFlicker extends TickScript {

  @Inject
  private Config config;

  public static int lastAttack;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    addTask(Attack.class);
    addTask(MoveUnder.class);
    addTask(DrinkPrayerPotion.class);
  }
}
