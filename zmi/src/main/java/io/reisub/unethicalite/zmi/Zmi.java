package io.reisub.unethicalite.zmi;

import com.google.inject.Provides;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Run;
import io.reisub.unethicalite.zmi.tasks.EmptyPouch;
import io.reisub.unethicalite.zmi.tasks.GoToBank;
import io.reisub.unethicalite.zmi.tasks.HandleBank;
import io.reisub.unethicalite.zmi.tasks.RepairPouch;
import io.reisub.unethicalite.zmi.tasks.RuneCraft;
import io.reisub.unethicalite.zmi.tasks.Teleport;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Inject;

@PluginDescriptor(
		name = "Chaos ZMI",
		description = "Hail Sa- ehr Zamorak!",
		enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class Zmi extends TickScript {
	@Inject
	private Config config;

	@Provides
	public Config getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Config.class);
	}

	public static final WorldPoint NEAR_ALTAR = new WorldPoint(3058, 5579, 0);

	public static boolean pouchesAreEmpty;

	@Override
	protected void onStart() {
		super.onStart();

		addTask(Run.class);
		addTask(RepairPouch.class);
		addTask(EmptyPouch.class);
		addTask(Teleport.class);
		addTask(GoToBank.class);
		addTask(HandleBank.class);
		addTask(RuneCraft.class);
	}
}
