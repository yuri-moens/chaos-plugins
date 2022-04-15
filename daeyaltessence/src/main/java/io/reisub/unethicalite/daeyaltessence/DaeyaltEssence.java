package io.reisub.unethicalite.daeyaltessence;

import com.google.inject.Provides;
import dev.unethicalite.api.entities.Players;
import io.reisub.unethicalite.daeyaltessence.tasks.GoToMine;
import io.reisub.unethicalite.daeyaltessence.tasks.HandleBank;
import io.reisub.unethicalite.daeyaltessence.tasks.Mine;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.enums.Activity;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.AnimationID;
import net.runelite.api.events.AnimationChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Inject;

@PluginDescriptor(
		name = "Chaos Daeyalt Essence",
		description = "For people too lazy to click a rock every 30 seconds",
		enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class DaeyaltEssence extends TickScript {
	@Inject
	private Config config;

	@Provides
	public Config getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Config.class);
	}

	public static final int ESSENCE_MINE_REGION = 14744;

	@Override
	protected void onStart() {
		super.onStart();

		tasks.add(new HandleBank(config));
		tasks.add(new GoToMine());
		tasks.add(new Mine(this));
	}

	@Subscribe
	private void onAnimationChanged(AnimationChanged event) {
		if (!Utils.isLoggedIn()) {
			return;
		}

		switch (Players.getLocal().getAnimation()) {
			case AnimationID.MINING_BRONZE_PICKAXE:
			case AnimationID.MINING_IRON_PICKAXE:
			case AnimationID.MINING_STEEL_PICKAXE:
			case AnimationID.MINING_BLACK_PICKAXE:
			case AnimationID.MINING_MITHRIL_PICKAXE:
			case AnimationID.MINING_ADAMANT_PICKAXE:
			case AnimationID.MINING_RUNE_PICKAXE:
			case AnimationID.MINING_DRAGON_PICKAXE:
			case AnimationID.MINING_DRAGON_PICKAXE_OR:
			case AnimationID.MINING_DRAGON_PICKAXE_UPGRADED:
			case AnimationID.MINING_DRAGON_PICKAXE_OR_TRAILBLAZER:
			case AnimationID.MINING_CRYSTAL_PICKAXE:
			case AnimationID.MINING_GILDED_PICKAXE:
			case AnimationID.MINING_INFERNAL_PICKAXE:
			case AnimationID.MINING_3A_PICKAXE:
				setActivity(Activity.MINING);
				break;
			case -1:
				setActivity(Activity.IDLE);
		}
	}
}
