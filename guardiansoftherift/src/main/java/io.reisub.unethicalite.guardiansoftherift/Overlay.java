package io.reisub.unethicalite.guardiansoftherift;

import com.openosrs.client.ui.overlay.components.table.TableAlignment;
import com.openosrs.client.ui.overlay.components.table.TableComponent;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.util.ColorUtil;


@Slf4j
@Singleton
public class Overlay extends OverlayPanel {
  @Inject
  private GuardiansOfTheRift plugin;
  @Inject
  private Config config;

  @Override
  public Dimension render(Graphics2D graphics) {
    TableComponent tableComponent = new TableComponent();
    tableComponent.setColumnAlignments(TableAlignment.LEFT, TableAlignment.RIGHT);


    if (!tableComponent.isEmpty()) {
      panelComponent.setBackgroundColor(ColorUtil.fromHex("#121212")); //Material Dark default
      panelComponent.setPreferredSize(new Dimension(200, 200));
      panelComponent.setBorder(new Rectangle(5, 5, 5, 5));
      panelComponent.getChildren().add(TitleComponent.builder()
          .text("Chaos Guardians of the Rift")
          .color(ColorUtil.fromHex("#40C4FF"))
          .build());
      panelComponent.getChildren().add(tableComponent);
    }

    return super.render(graphics);
  }
}
