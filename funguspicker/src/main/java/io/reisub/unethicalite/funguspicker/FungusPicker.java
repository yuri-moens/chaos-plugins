package io.reisub.unethicalite.funguspicker;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import io.reisub.unethicalite.funguspicker.tasks.CastBloom;
import io.reisub.unethicalite.funguspicker.tasks.GoToFungus;
import io.reisub.unethicalite.funguspicker.tasks.HandleBank;
import io.reisub.unethicalite.funguspicker.tasks.Pick;
import io.reisub.unethicalite.funguspicker.tasks.RestorePrayer;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Inject;
import java.util.Set;

@PluginDescriptor(
		name = "Chaos Fungus Picker",
		description = "This is so fun, guy!",
		enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class FungusPicker extends TickScript {
	@Inject
	private Config config;

	@Provides
	public Config getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Config.class);
	}

	public static final WorldPoint FUNGUS_LOCATION = new WorldPoint(0, 0, 0);
	public static final int MONASTERY_REGION = 10290;
	public static final Set<Integer> VER_SINHAZA_REGION_IDS = ImmutableSet.of(
			14386,
			14642
	);

	@Override
	protected void onStart() {
		super.onStart();

		addTask(RestorePrayer.class);
		addTask(HandleBank.class);
		addTask(GoToFungus.class);
		addTask(CastBloom.class);
		addTask(Pick.class);
	}
}
