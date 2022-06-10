package io.reisub.unethicalite.giantsfoundry;

import com.google.inject.Provides;
import io.reisub.unethicalite.giantsfoundry.tasks.*;
import io.reisub.unethicalite.utils.TickScript;
import io.reisub.unethicalite.utils.Utils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.InventoryID;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
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
    private static final int PREFORM = 27010;
    @Inject
    private Config config;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    GiantsFoundryState giantsFoundryState;

    @Inject
    Overlay overlay;

    @Getter
    private boolean hasCommission;

    @Provides
    public Config getConfig(ConfigManager configManager) {
        return configManager.getConfig(Config.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        overlayManager.add(overlay);

        addTask(GetCommission.class);
        addTask(HandIn.class);
        addTask(SetMoulds.class);
        addTask(Bank.class);
        addTask(AddBars.class);
        addTask(Pour.class);
        addTask(PickUpPreform.class);
        addTask(HeatUp.class);
        addTask(CoolDown.class);
        addTask(Hammer.class);
        addTask(Grind.class);
        addTask(Polish.class);

        log.info(String.valueOf(tasks.size()));

    }

    @Override
    protected void onStop() {
        reset();
        overlayManager.remove(overlay);
        super.onStop();
    }

    private void reset() {
        giantsFoundryState.reset();
        hasCommission = false;
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event)
    {
        if (event.getContainerId() == InventoryID.EQUIPMENT.getId()
                && event.getItemContainer().count(PREFORM) == 0)
        {
            reset();
        }
    }
}
