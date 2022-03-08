package io.reisub.unethicalite.cooking;

import com.google.inject.Provides;
import dev.hoot.api.entities.Players;
import dev.hoot.api.items.Inventory;
import io.reisub.unethicalite.cooking.tasks.Cook;
import io.reisub.unethicalite.cooking.tasks.Drop;
import io.reisub.unethicalite.cooking.tasks.HandleBank;
import io.reisub.unethicalite.cooking.tasks.SonicCook;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.enums.Activity;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.AnimationID;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Inject;

@PluginDescriptor(
		name = "Chaos Cooking",
		description = "It's fucking raw!",
		enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class Cooking extends TickScript {
	@Inject
	private Config config;

	@Provides
	public Config getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Config.class);
	}

	@Getter
	@Setter
	private int lastBank;

	@Getter
	@Setter
	private int lastDrop;

	@Override
	protected void onStart() {
		super.onStart();

		tasks.add(new HandleBank(this, config));
		tasks.add(new Drop(this, config));
		tasks.add(new SonicCook(this, config));
		tasks.add(new Cook(this, config));
	}

	@Subscribe
	private void onAnimationChanged(AnimationChanged event) {
		if (!isLoggedIn() || event.getActor() != Players.getLocal()) return;

		switch (Players.getLocal().getAnimation()) {
			case AnimationID.COOKING_FIRE:
			case AnimationID.COOKING_RANGE:
				setActivity(Activity.COOKING);
		}
	}

	@Subscribe
	private void onItemContainerChanged(ItemContainerChanged event) {
		if (!isLoggedIn()) return;

		if (currentActivity == Activity.COOKING && !Inventory.contains(config.foodId())) {
			setActivity(Activity.IDLE);
		}
	}

}
