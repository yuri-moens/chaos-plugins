package io.reisub.unethicalite.shopper;

import com.google.inject.Provides;
import io.reisub.unethicalite.shopper.tasks.Buy;
import io.reisub.unethicalite.shopper.tasks.FillBag;
import io.reisub.unethicalite.shopper.tasks.HandleBank;
import io.reisub.unethicalite.shopper.tasks.Hop;
import io.reisub.unethicalite.shopper.tasks.OpenPacks;
import io.reisub.unethicalite.shopper.tasks.OpenShop;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import io.reisub.unethicalite.utils.tasks.Run;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Shopper",
    description = "Hops worlds and buys stuff from NPCs",
    enabledByDefault = false)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class Shopper extends TickScript {
  @Inject private Config config;
  @Getter private List<BuyItem> buyItems;
  @Getter private WorldPoint bankLocation;
  @Getter private WorldPoint npcLocation;
  @Getter @Setter private boolean hop;
  @Getter private int coalInBag;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    hop = false;
    loadItems();
    loadLocations();

    addTask(Run.class);
    addTask(Hop.class);
    addTask(OpenPacks.class);
    addTask(FillBag.class);
    addTask(Buy.class);
    addTask(HandleBank.class);
    addTask(OpenShop.class);
  }

  public void setCoalInBag(int amount) {
    if (amount > 27) {
      coalInBag = 27;
    } else {
      coalInBag = amount;
    }
  }

  private void loadItems() {
    buyItems = new ArrayList<>();

    if (config.itemOneEnabled()) {
      buyItems.add(
          new BuyItem(
              config.itemOneId(),
              config.itemOneAmount(),
              config.itemOneMinInStore(),
              config.itemOneStackable()));
    }

    if (config.itemTwoEnabled()) {
      buyItems.add(
          new BuyItem(
              config.itemTwoId(),
              config.itemTwoAmount(),
              config.itemTwoMinInStore(),
              config.itemTwoStackable()));
    }

    if (config.itemThreeEnabled()) {
      buyItems.add(
          new BuyItem(
              config.itemThreeId(),
              config.itemThreeAmount(),
              config.itemThreeMinInStore(),
              config.itemThreeStackable()));
    }

    if (config.itemFourEnabled()) {
      buyItems.add(
          new BuyItem(
              config.itemFourId(),
              config.itemFourAmount(),
              config.itemFourMinInStore(),
              config.itemFourStackable()));
    }

    if (config.itemFiveEnabled()) {
      buyItems.add(
          new BuyItem(
              config.itemFiveId(),
              config.itemFiveAmount(),
              config.itemFiveMinInStore(),
              config.itemFiveStackable()));
    }
  }

  private void loadLocations() {
    if (config.bankLocation().equals("")
        || config.bankLocation().equals("0,0,0")
        || config.npcLocation().equals("")
        || config.npcLocation().equals("0,0,0")) {
      return;
    }

    String[] bankLocationSplit = config.bankLocation().split(",");
    String[] npcLocationSplit = config.npcLocation().split(",");

    try {
      bankLocation =
          new WorldPoint(
              Integer.parseInt(bankLocationSplit[0]),
              Integer.parseInt(bankLocationSplit[1]),
              Integer.parseInt(bankLocationSplit[2]));

      npcLocation =
          new WorldPoint(
              Integer.parseInt(npcLocationSplit[0]),
              Integer.parseInt(npcLocationSplit[1]),
              Integer.parseInt(npcLocationSplit[2]));
    } catch (Exception e) {
      log.error("Bank or NPC location format is invalid.");
    }
  }
}
