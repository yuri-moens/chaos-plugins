package io.reisub.unethicalite.bankpin;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.WidgetClosed;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.input.Keyboard;
import net.unethicalite.api.widgets.Widgets;
import org.pf4j.Extension;

@PluginDescriptor(
    name = "Chaos Bank PIN",
    description = "Automatically enters your bank PIN",
    enabledByDefault = false)
@Slf4j
@Extension
public class BankPin extends Plugin {
  @Inject private Config config;
  private boolean pinScreenOpen;

  @Provides
  public Config getConfig(ConfigManager configManager) {
    return configManager.getConfig(Config.class);
  }

  @Subscribe
  private void onWidgetLoaded(WidgetLoaded event) {
    if (event.getGroupId() == WidgetID.BANK_PIN_GROUP_ID) {
      pinScreenOpen = true;
    }
  }

  @Subscribe
  private void onWidgetClosed(WidgetClosed event) {
    if (event.getGroupId() == WidgetID.BANK_PIN_GROUP_ID) {
      pinScreenOpen = false;
    }
  }

  @Subscribe
  private void onGameTick(GameTick event) {
    if (!pinScreenOpen) {
      return;
    }

    char[] pin = config.bankPin().toCharArray();

    if (pin.length != 4) {
      return;
    }

    if (Widgets.get(WidgetInfo.BANK_PIN_FIRST_ENTERED).getText().equals("?")) {
      Keyboard.type(pin[0]);
    } else if (Widgets.get(WidgetInfo.BANK_PIN_SECOND_ENTERED).getText().equals("?")) {
      Keyboard.type(pin[1]);
    } else if (Widgets.get(WidgetInfo.BANK_PIN_THIRD_ENTERED).getText().equals("?")) {
      Keyboard.type(pin[2]);
    } else if (Widgets.get(WidgetInfo.BANK_PIN_FOURTH_ENTERED).getText().equals("?")) {
      Keyboard.type(pin[3]);
    }
  }
}
