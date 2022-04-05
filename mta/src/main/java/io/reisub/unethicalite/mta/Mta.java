package io.reisub.unethicalite.mta;

import com.google.inject.Provides;
import dev.hoot.bot.managers.Static;
import io.reisub.unethicalite.mta.tasks.BonesTo;
import io.reisub.unethicalite.mta.tasks.EatPeach;
import io.reisub.unethicalite.mta.tasks.Enchant;
import io.reisub.unethicalite.mta.tasks.HighAlch;
import io.reisub.unethicalite.mta.tasks.TelekineticGrab;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Inject;

@PluginDescriptor(
		name = "Chaos MTA",
		description = "Like Hogwarts, but in RuneScape",
		enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class Mta extends TickScript {
	@Inject
	private Config config;

	public static final int MTA_REGION = 13462;

	private TelekineticGrab telekineticGrab;
	private BonesTo bonesTo;

	@Provides
	public Config getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Config.class);
	}

	@Override
	protected void onStart() {
		super.onStart();

		telekineticGrab = injector.getInstance(TelekineticGrab.class);
		bonesTo = injector.getInstance(BonesTo.class);

		Static.getKeyManager().registerKeyListener(telekineticGrab);
		Static.getKeyManager().registerKeyListener(bonesTo);

		addTask(telekineticGrab);
		addTask(Enchant.class);
		addTask(HighAlch.class);
		addTask(EatPeach.class);
		addTask(bonesTo);
	}

	@Override
	protected void onStop() {
		super.onStop();

		Static.getKeyManager().unregisterKeyListener(telekineticGrab);
		Static.getKeyManager().unregisterKeyListener(bonesTo);
	}
}
