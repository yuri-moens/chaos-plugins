package io.reisub.unethicalite.glassblower;

import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chaosglassblower")
public interface Config extends net.runelite.client.config.Config {
  @ConfigItem(
      keyName = "targetProduct",
      name = "Blow",
      description = "Choose what to blow",
      position = 0)
  default Product targetProduct() {
    return Product.HIGHEST_POSSIBLE;
  }

  @ConfigItem(
      keyName = "pickUpSeaweedSpores",
      name = "Pick up seaweed spores",
      description = "Go underwater at Fossil Island to pick up seaweed spores.",
      position = 1)
  default boolean pickUpSeaweedSpores() {
    return false;
  }

  @ConfigItem(
      keyName = "startButton",
      name = "Start/Stop",
      description = "Start the script",
      position = Integer.MAX_VALUE)
  default Button startButton() {
    return new Button();
  }
}
