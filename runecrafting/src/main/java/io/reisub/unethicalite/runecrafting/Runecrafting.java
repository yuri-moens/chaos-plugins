package io.reisub.unethicalite.runecrafting;

import com.google.inject.Provides;
import io.reisub.unethicalite.runecrafting.tasks.CraftRunes;
import io.reisub.unethicalite.runecrafting.tasks.GoToAltar;
import io.reisub.unethicalite.runecrafting.tasks.GoToBank;
import io.reisub.unethicalite.runecrafting.tasks.HandleBank;
import io.reisub.unethicalite.runecrafting.tasks.RepairPouches;
import io.reisub.unethicalite.utils.Constants;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.util.Text;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.commons.Predicates;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.client.Static;
import org.pf4j.Extension;
import org.slf4j.Logger;

@PluginDescriptor(
    name = "Chaos Runecrafting",
    description = "Can't believe you'd bot the most fun skill ever",
    enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class Runecrafting extends TickScript {

  @Inject
  private Config config;
  private int lastPouchFullMessageTick;
  private int lastPouchEmptyMessageTick;
  @Setter
  private int fullPouches;
  @Setter
  private int emptyPouches;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  public Logger getLogger() {
    return log;
  }

  @Override
  protected void onStart() {
    super.onStart();

    addTask(CraftRunes.class);
    addTask(RepairPouches.class);
    addTask(HandleBank.class);
    addTask(GoToBank.class);
    addTask(GoToAltar.class);
  }

  @Subscribe
  public void onChatMessage(ChatMessage chatMessage) {
    final String msg = Text.standardize(chatMessage.getMessage());

    if (msg.equals("you cannot add any more essence to the pouch.")) {
      if (Static.getClient().getTickCount() != lastPouchFullMessageTick) {
        fullPouches = 0;
      }

      lastPouchFullMessageTick = Static.getClient().getTickCount();
      fullPouches++;
      emptyPouches = 0;
    } else if (msg.equals("there is no essence in this pouch.")) {
      if (Static.getClient().getTickCount() != lastPouchEmptyMessageTick) {
        emptyPouches = 0;
      }

      lastPouchEmptyMessageTick = Static.getClient().getTickCount();
      emptyPouches++;
      fullPouches = 0;
    }
  }

  public boolean isNearBank() {
    final WorldPoint bankPoint = config.rune().getBankPoint() != null
        ? config.rune().getBankPoint()
        : config.bankLocation().getBankPoint();

    return Players.getLocal().distanceTo(bankPoint) < 10;
  }

  public boolean arePouchesFull() {
    final Set<Integer> normalPouches = new HashSet<>(Constants.ESSENCE_POUCH_IDS);
    normalPouches.removeAll(Constants.DEGRADED_ESSENCE_POUCH_IDS);

    final int pouchesCount = Inventory.getCount(Predicates.ids(normalPouches));

    return fullPouches >= pouchesCount;
  }

  public boolean arePouchesEmpty() {
    final Set<Integer> normalPouches = new HashSet<>(Constants.ESSENCE_POUCH_IDS);
    normalPouches.removeAll(Constants.DEGRADED_ESSENCE_POUCH_IDS);

    final int pouchesCount = Inventory.getCount(Predicates.ids(normalPouches));

    return emptyPouches >= pouchesCount;
  }
}
