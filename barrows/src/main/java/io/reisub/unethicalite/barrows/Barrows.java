package io.reisub.unethicalite.barrows;

import com.google.inject.Provides;
import dev.hoot.api.entities.Players;
import io.reisub.unethicalite.combathelper.CombatHelper;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Inject;

@PluginDescriptor(
		name = "Chaos Barrows",
		description = "Automated fratricide",
		enabledByDefault = false
)
@PluginDependency(Utils.class)
@PluginDependency(CombatHelper.class)
@Slf4j
@Extension
public class Barrows extends TickScript {
	@Inject
	private Config config;

	@Provides
	public Config getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Config.class);
	}

	private static final int[] FEROX_ENCLAVE_REGIONS = new int[]{12344, 12600};

	@Override
	protected void onStart() {
		super.onStart();

		// addTask();
	}

	public boolean isInFeroxEnclave() {
		if (Utils.isLoggedIn()) {
			for (int id : FEROX_ENCLAVE_REGIONS) {
				if (Players.getLocal().getWorldLocation().getRegionID() == id) {
					return true;
				}
			}
		}

		return false;
	}
}
