package io.reisub.unethicalite.giantsfoundry;

import com.google.inject.Provides;
import io.reisub.unethicalite.giantsfoundry.tasks.*;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.entities.Players;
import org.pf4j.Extension;

import javax.inject.Inject;

@PluginDescriptor(
        name = "Chaos GiantsFoundry",
        description = "",
        enabledByDefault = false
)
@PluginDependency(Utils.class)
@Slf4j
@Extension
public class GiantsFoundry extends TickScript {
    @Inject
    private Config config;

    @Inject
    GiantsFoundryState giantsFoundryState;

    @Inject
    GiantsFoundryHelper giantsFoundryHelper;

    @Getter
    private boolean hasCommission;

    @Provides
    public Config getConfig(ConfigManager configManager) {
        return configManager.getConfig(Config.class);
    }

    @Override
    protected void onStart() {

        if (!Utils.isInRegion(13491)) {
            return;
        }
        super.onStart();

        addTask(GetCommission.class);
        addTask(HeatUp.class);
        addTask(CoolDown.class);
        addTask(Hammer.class);
        addTask(Grind.class);
        addTask(Polish.class);

    }

    private void reset() {
        giantsFoundryState.reset();
        hasCommission = false;
    }
}
