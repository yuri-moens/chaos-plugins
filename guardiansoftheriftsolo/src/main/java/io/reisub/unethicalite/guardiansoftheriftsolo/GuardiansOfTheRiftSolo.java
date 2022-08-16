package io.reisub.unethicalite.guardiansoftheriftsolo;

import com.google.inject.Provides;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.widgets.Widgets;
import net.unethicalite.client.Static;
import org.pf4j.Extension;
import org.slf4j.Logger;

@PluginDescriptor(
    name = "Chaos Guardians of the Rift Solo",
    description = "Plays the Guardians of the Rift minigame solo",
    enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class GuardiansOfTheRiftSolo extends TickScript {

  private static final int WIDGET_GROUP_ID = 746;

  @Inject
  private Config config;
  private int startTick;

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

    // addTask();
  }

  @Subscribe
  private void onChatMessage(ChatMessage event) {
    if (!isRunning()) {
      return;
    }

    if (event.getMessage().contains("The rift becomes active!")) {
      startTick = Static.getClient().getTickCount();
    }
  }

  public int getElapsedTicks() {
    return Static.getClient().getTickCount() - startTick;
  }

  private int getWidgetInteger(int widgetId) {
    final Widget widget = Widgets.get(WIDGET_GROUP_ID, widgetId);

    if (widget == null) {
      return -1;
    }

    final String[] split = widget.getText().split(":");

    if (split.length != 2) {
      return -1;
    }

    try {
      final String string = split[1]
          .replace("%", "")
          .trim();

      return Integer.parseInt(string);
    } catch (NumberFormatException e) {
      log.debug("Failed to parse number from string: {}", split[1]);
      return -1;
    }
  }

  public int getEntranceTimer() {
    return getWidgetInteger(5);
  }

  public int getGuardianPower() {
    return getWidgetInteger(18);
  }

  public int getElementalEnergy() {
    return getWidgetInteger(21);
  }

  public int getCatalyticEnergy() {
    return getWidgetInteger(24);
  }

  public int getPortalTimer() {
    return getWidgetInteger(26);
  }

  public int getGuardianCount() {
    final Widget widget = Widgets.get(WIDGET_GROUP_ID, 30);

    if (widget == null) {
      return -1;
    }

    final String[] split = widget.getText().split("/");

    if (split.length != 2) {
      return -1;
    }

    try {
      final String string = split[0].trim();

      return Integer.parseInt(string);
    } catch (NumberFormatException e) {
      log.debug("Failed to parse number from string: {}", split[0]);
      return -1;
    }
  }
}
