package io.reisub.unethicalite.tempoross;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import dev.hoot.api.coords.RectangularArea;
import dev.hoot.api.entities.TileObjects;
import dev.hoot.api.game.Vars;
import dev.hoot.api.items.Inventory;
import dev.hoot.api.widgets.Widgets;
import io.reisub.unethicalite.tempoross.tasks.*;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.enums.Activity;
import io.reisub.unethicalite.utils.tasks.Run;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.util.Text;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Inject;
import java.time.Instant;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@PluginDescriptor(
		name = "Chaos Tempoross",
		description = "Plays the Tempoross minigame",
		enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class Tempoross extends TickScript {
	@Inject
	private Config config;

	@Inject
	private Client client;

	@Provides
	public Config getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Config.class);
	}

	private static final String WAVE_INCOMING_MESSAGE = "a colossal wave closes in...";
	private static final String TETHER_MESSAGE = "you securely tether yourself";
	private static final String UNTETHER_MESSAGE = "you untether yourself";
	private static final String TEMPOROSS_VULNERABLE_MESSAGE = "tempoross is vulnerable";
	private static final String FINISHED_GAME = "the spirit anglers will ferry you back";

	private static final int VARB_IS_TETHERED = 11895;
	private static final int TEMPOROSS_REGION = 12078;
	private static final int UNKAH_REWARD_POOL_REGION = 12588;
	private static final int UNKAH_BOAT_REGION = 12332;

	@Getter
	private boolean waveIncoming;

	@Getter
	private int phase = 1;

	@Getter
	private int playersReady;

	@Getter
	private int energy;

	@Getter
	private int essence;

	@Getter
	private int stormIntensity;

	@Getter
	private int rawFish;

	@Getter
	private int cookedFish;

	@Getter
	private WorldPoint dudiPos = null;

	@Getter
	private boolean finished;

	@Getter
	@Setter
	private int cookedFishRequired;

	@Override
	protected void onStart() {
		super.onStart();

		reset();

		tasks.add(new Run());

		addTask(DodgeFire.class);
		addTask(LeaveGame.class);
		addTask(EnterBoat.class);
		addTask(FillBuckets.class);
		addTask(LeaveBoat.class);
		addTask(Repair.class);
		addTask(Tether.class);
		addTask(DouseFire.class);
		addTask(Attack.class);
		addTask(Stock.class);
		addTask(Cook.class);
		addTask(Fish.class);
	}

	@Subscribe(priority = 1)
	private void onGameTick(GameTick event) {
		if (isInDesert() || isOnBoat()) {
			if (dudiPos != null) {
				reset();
			}

			playersReady = parseWidget(687, 3);
		} else if (isInTemporossArea()) {
			energy = parseWidget(437, 35);
			essence = parseWidget(437, 45);
			stormIntensity = parseWidget(437, 55);
		}
	}

	@Subscribe
	private void onChatMessage(ChatMessage chatMessage) {
		if (chatMessage.getType() != ChatMessageType.GAMEMESSAGE
				&& chatMessage.getType() != ChatMessageType.SPAM) return;

		String message = Text.standardize(chatMessage.getMessage());
		if (message.contains(WAVE_INCOMING_MESSAGE)) {
			waveIncoming = true;
		} else if (message.contains(TETHER_MESSAGE)) {
			setActivity(Activity.TETHERING_MAST);
		} else if (message.contains(UNTETHER_MESSAGE)) {
			waveIncoming = false;
			setActivity(Activity.IDLE);
		} else if (message.contains(TEMPOROSS_VULNERABLE_MESSAGE)) {
			phase++;
		} else if (message.contains(FINISHED_GAME)) {
			finished = true;
		}
	}

	@Subscribe
	private void onAnimationChanged(AnimationChanged event) {
		if (!isLoggedIn()) return;

		if (client.getLocalPlayer() == null || event.getActor() != client.getLocalPlayer()) return;

		switch (client.getLocalPlayer().getAnimation()) {
			case AnimationID.COOKING_RANGE:
				TileObject shrine = TileObjects.getNearest(ObjectID.SHRINE_41236);
				if (shrine != null && shrine.distanceTo(client.getLocalPlayer()) <= 3) {
					setActivity(Activity.COOKING);
				} else {
					setActivity(Activity.STOCKING_CANNON);
				}
				break;
			case AnimationID.CONSTRUCTION:
			case AnimationID.CONSTRUCTION_IMCANDO:
				setActivity(Activity.REPAIRING);
				break;
			case AnimationID.LOOKING_INTO:
				if (previousActivity != Activity.TETHERING_MAST) {
					setActivity(Activity.FILLING_BUCKETS);
				}
				break;
		}
	}

	@Subscribe
	private void onInteractingChanged(InteractingChanged event) {
		if (event.getSource() == client.getLocalPlayer()) {
			if (event.getTarget() == null) {
				if (currentActivity == Activity.FISHING
						|| currentActivity == Activity.DOUSING_FIRE
						|| currentActivity == Activity.ATTACKING) {
					setActivity(Activity.IDLE);
				}
			} else {
				String name = event.getTarget().getName();

				if (name != null) {
					if (name.equals("Fishing spot")) {
						setActivity(Activity.FISHING);
					} else if (name.contains("Fire")) {
						setActivity(Activity.DOUSING_FIRE);
					} else if (name.contains("Spirit pool")) {
						setActivity(Activity.ATTACKING);
					}
				}
			}
		}
	}

	@Subscribe
	private void onItemContainerChanged(ItemContainerChanged event) {
		final ItemContainer container = event.getItemContainer();

		if (container.count(ItemID.HARPOONFISH) < cookedFish) {
			cookedFishRequired--;
		}

		rawFish = container.count(ItemID.RAW_HARPOONFISH);
		cookedFish = container.count(ItemID.HARPOONFISH);
		int emptyBuckets = container.count(ItemID.BUCKET);

		if (rawFish == 0 && currentActivity == Activity.COOKING) {
			setActivity(Activity.IDLE);
		} else if (Inventory.isFull() && currentActivity == Activity.FISHING) {
			setActivity(Activity.IDLE);
		} else if ((cookedFish == 0 || cookedFishRequired == 0) && currentActivity == Activity.STOCKING_CANNON) {
			if (cookedFishRequired == 0) {
				if (phase == 1) {
					cookedFishRequired = 19;
				} else if (phase == 2) {
					cookedFishRequired = 19;
				} else if (phase >= 3) {
					cookedFishRequired = 28;
				}
			}

			setActivity(Activity.IDLE);
		} else if (emptyBuckets == 0 && currentActivity == Activity.FILLING_BUCKETS) {
			setActivity(Activity.IDLE);
		}
	}

	@Subscribe
	private void onGameObjectDespawned(GameObjectDespawned gameObjectDespawned) {
		if (!isInTemporossArea()) return;

		int id = gameObjectDespawned.getGameObject().getId();

		Set<Integer> brokenMastsTotems = ImmutableSet.of(
				ObjectID.DAMAGED_MAST_40996,
				ObjectID.DAMAGED_MAST_40997,
				ObjectID.DAMAGED_TOTEM_POLE,
				ObjectID.DAMAGED_TOTEM_POLE_41011
		);

		if (brokenMastsTotems.contains(id)) {
			if (currentActivity == Activity.REPAIRING) {
				setActivity(Activity.IDLE);
			}
		}
	}

	@Subscribe
	private void onNpcSpawned(NpcSpawned npcSpawned) {
		if (npcSpawned.getNpc().getId() == NpcID.CAPTAIN_DUDI_10587 && dudiPos == null) {
			dudiPos = npcSpawned.getNpc().getWorldLocation();
		}
	}

	@Subscribe
	private void onHitsplatApplied(HitsplatApplied event) {
		if (event.getActor().getName().contains("Spirit pool") && event.getHitsplat().getHitsplatType() == Hitsplat.HitsplatType.DAMAGE_ME) {
			if (currentActivity == Activity.ATTACKING && phase <= 3 && essence <= 10) {
				setActivity(Activity.IDLE);
			}
		}
	}

	public boolean isOnBoat() {
		return isInRegion(UNKAH_BOAT_REGION);
	}

	public boolean isInDesert() {
		return isInRegion(UNKAH_REWARD_POOL_REGION);
	}

	public boolean isInTemporossArea() {
		return isInMapRegion(TEMPOROSS_REGION) || isInRegion(TEMPOROSS_REGION);
	}

	public RectangularArea getBoatArea() {
		return new RectangularArea(
				dudiPos.getX() - 3,
				dudiPos.getY() - 13,
				dudiPos.getX() + 3,
				dudiPos.getY() + 9
		);
	}

	public RectangularArea getIslandArea() {
		return new RectangularArea(
				dudiPos.getX() - 3,
				dudiPos.getY() - 1,
				dudiPos.getX() + 16,
				dudiPos.getY() + 27
		);
	}

	@Override
	protected void checkActionTimeout() {
		if (Vars.getBit(VARB_IS_TETHERED) > 0) {
			lastActionTime = Instant.now();
			return;
		}

		super.checkActionTimeout();
	}

	private void reset() {
		waveIncoming = false;
		phase = 1;
		rawFish = 0;
		cookedFish = 0;
		cookedFishRequired = 17;
		dudiPos = null;
		finished = false;
	}

	private int parseWidget(int group, int id) {
		Widget widget = Widgets.get(group, id);
		if (widget == null || widget.getText().equals("")) return 0;

		Pattern regex = Pattern.compile("\\d+|None");
		Matcher matcher = regex.matcher(widget.getText());

		if (matcher.find()) {
			String match = matcher.group(0);
			if (match.equals("None")) return 0;

			return Integer.parseInt(match);
		}

		return 0;
	}
}
