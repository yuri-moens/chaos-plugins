package io.reisub.unethicalite.fighter;

import com.google.inject.Provides;
import dev.unethicalite.managers.Static;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Inject;

@PluginDescriptor(
		name = "Chaos Fighter",
		description = "Don't talk about this one",
		enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class Fighter extends TickScript {
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

		// addTask();
	}

	@Subscribe
	private void onMenuOptionClicked(MenuOptionClicked event) {
		if (!config.insertMenu()) {
			return;
		}

		if (event.getMenuOption().equals("Start Fighter")) {

		} else if (event.getMenuOption().equals("Stop Fighter")) {

		}
	}

	@Subscribe
	private void onMenuEntryAdded(MenuEntryAdded event) {
		if (!config.insertMenu() || !event.getOption().equals("Attack")) {
			return;
		}

		if (isRunning()) {
			Static.getClient().createMenuEntry(-1)
					.setOption("Stop Fighter")
					.setTarget(event.getTarget())
					.setIdentifier(0)
					.setParam0(0)
					.setParam1(0)
					.setType(MenuAction.RUNELITE);
		} else {
			Static.getClient().createMenuEntry(-1)
					.setOption("Start Fighter")
					.setTarget(event.getTarget())
					.setIdentifier(0)
					.setParam0(0)
					.setParam1(0)
					.setType(MenuAction.RUNELITE);
		}
	}
}
