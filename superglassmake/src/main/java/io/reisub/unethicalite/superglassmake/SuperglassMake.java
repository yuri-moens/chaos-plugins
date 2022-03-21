package io.reisub.unethicalite.superglassmake;

import com.google.inject.Provides;
import io.reisub.unethicalite.superglassmake.tasks.CastSuperglassMake;
import io.reisub.unethicalite.superglassmake.tasks.HandleBank;
import io.reisub.unethicalite.superglassmake.tasks.PickupGlass;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Inject;

@PluginDescriptor(
		name = "Chaos Superglass Make",
		description = "A super Superglass Make caster",
		enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class SuperglassMake extends TickScript {
	@Inject
	private Config config;

	@Provides
	public Config getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Config.class);
	}

	@Override
	protected void onStart() {
		super.onStart();

		addTask(PickupGlass.class);
		addTask(HandleBank.class);
		addTask(CastSuperglassMake.class);
	}
}