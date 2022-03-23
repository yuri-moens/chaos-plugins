package io.reisub.unethicalite.mahoganyhomes;

import com.google.inject.Provides;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Inject;

@PluginDescriptor(
		name = "Chaos Mahogany Homes",
		description = "Can we build it? Yes, we can!",
		enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class MahoganyHomes extends TickScript {
	@Inject
	private Config config;

	@Provides
	public Config getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Config.class);
	}

	@Getter
	@Setter
	private Home currentHome;

	@Override
	protected void onStart() {
		super.onStart();

		currentHome = null;

		// addTask();
	}
}
