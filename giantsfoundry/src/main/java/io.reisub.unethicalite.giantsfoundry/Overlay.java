package io.reisub.unethicalite.giantsfoundry;

import com.openosrs.client.ui.overlay.components.table.TableAlignment;
import com.openosrs.client.ui.overlay.components.table.TableComponent;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.util.ColorUtil;
import net.unethicalite.api.game.Vars;


@Slf4j
@Singleton
public class Overlay extends OverlayPanel {
  @Inject
  private GiantsFoundry plugin;
  @Inject
  private GiantsFoundryState state;
  @Inject
  private GiantsFoundryHelper helper;
  @Inject
  private Config config;
  @Inject
  private Client client;


  @Override
  public Dimension render(Graphics2D graphics) {
    TableComponent tableComponent = new TableComponent();
    tableComponent.setColumnAlignments(TableAlignment.LEFT, TableAlignment.RIGHT);


    if (state.getCurrentStage() != null) {
      tableComponent.addRow("Stage", state.getCurrentStage().getName());
    }
    tableComponent.addRow("Heat:", String.valueOf(state.getHeatAmount()));
    tableComponent.addRow("Heat range:", String.valueOf(
        helper.getCurrentHeatRange()[0] + " - " + String.valueOf(helper.getCurrentHeatRange()[1])));
    tableComponent.addRow("Bars in crucible:", String.valueOf(state.getOreCount()));
    tableComponent.addRow("GameStage", String.valueOf(state.getGameStage()));
    tableComponent.addRow("Progress", String.valueOf(state.getProgressAmount()));
    tableComponent.addRow("Points", String.valueOf(Vars.getVarp(3436)));
    tableComponent.getRows().get(tableComponent.getRows().size() - 1)
        .setRowColor(ColorUtil.fromHex("#03fc49"));




    if (!tableComponent.isEmpty()) {
      panelComponent.setBackgroundColor(ColorUtil.fromHex("#121212")); //Material Dark default
      panelComponent.setPreferredSize(new Dimension(200, 200));
      panelComponent.setBorder(new Rectangle(5, 5, 5, 5));
      panelComponent.getChildren().add(TitleComponent.builder()
          .text("Chaos Giants Foundry")
          .color(ColorUtil.fromHex("#40C4FF"))
          .build());
      panelComponent.getChildren().add(tableComponent);

    }

    return super.render(graphics);
  }
}
