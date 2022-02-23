package io.reisub.unethicalite.base;

import com.google.inject.Provides;
import io.reisub.unethicalite.utils.TickScript;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Inject;

@PluginDescriptor(
		name = "Chaos Base",
		description = "",
		enabledByDefault = false
)
@Slf4j
@Extension
public class Base extends TickScript {
	@Inject
	private Config config;

	@Inject
	private Client client;

	@Provides
	public Config getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Config.class);
	}

	@Override
	protected void onStart() {
		super.onStart();

		// addTask();
	}
}
