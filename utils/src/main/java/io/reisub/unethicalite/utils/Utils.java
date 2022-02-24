package io.reisub.unethicalite.utils;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Singleton;

@PluginDescriptor(
		name = "Chaos Utils",
		description = "Utilities for Chaos scripts"
)
@Slf4j
@Singleton
@Extension
public class Utils extends Plugin {
	@Override
	public void startUp() {
		log.info(this.getName() + " started");
	}

	@Override
	protected void shutDown() {
		log.info(this.getName() + " stopped");
	}
}
