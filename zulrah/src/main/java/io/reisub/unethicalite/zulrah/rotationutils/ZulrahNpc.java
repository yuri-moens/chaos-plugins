package io.reisub.unethicalite.zulrah.rotationutils;

import io.reisub.unethicalite.zulrah.constants.ZulrahLocation;
import io.reisub.unethicalite.zulrah.constants.ZulrahType;
import javax.annotation.Nonnull;
import net.runelite.api.NPC;

public final class ZulrahNpc {

  @Nonnull
  private final ZulrahType type;
  @Nonnull
  private final ZulrahLocation zulrahLocation;
  private final boolean jad;

  public ZulrahNpc(@Nonnull ZulrahType type, @Nonnull ZulrahLocation zulrahLocation, boolean jad) {
    if (type == null) {
      throw new NullPointerException("type is marked non-null but is null");
    } else if (zulrahLocation == null) {
      throw new NullPointerException("zulrahLocation is marked non-null but is null");
    } else {
      this.type = type;
      this.zulrahLocation = zulrahLocation;
      this.jad = jad;
    }
  }

  public static ZulrahNpc valueOf(NPC zulrah, boolean jad) {
    return new ZulrahNpc(ZulrahType.valueOf(zulrah.getId()),
        ZulrahLocation.valueOf(zulrah.getLocalLocation()), jad);
  }

  @Nonnull
  public ZulrahType getType() {
    return type;
  }

  @Nonnull
  public ZulrahLocation getZulrahLocation() {
    return zulrahLocation;
  }

  public boolean isJad() {
    return jad;
  }

  public String toString() {
    ZulrahType type = getType();
    return "ZulrahNpc(type=" + type + ", zulrahLocation=" + getZulrahLocation() + ", jad=" + isJad()
        + ")";
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof ZulrahNpc)) {
      return false;
    } else {
      ZulrahNpc other = (ZulrahNpc) o;
      Object thisType = getType();
      Object otherType = other.getType();
      if (thisType == null) {
        if (otherType != null) {
          return false;
        }
      } else if (!thisType.equals(otherType)) {
        return false;
      }

      label29: {
        Object thisZulrahLocation = getZulrahLocation();
        Object otherZulrahLocation = other.getZulrahLocation();
        if (thisZulrahLocation == null) {
          if (otherZulrahLocation == null) {
            break label29;
          }
        } else if (thisZulrahLocation.equals(otherZulrahLocation)) {
          break label29;
        }

        return false;
      }

      if (isJad() != other.isJad()) {
        return false;
      } else {
        return true;
      }
    }
  }

  public int hashCode() {
    int prime = 59;
    int result = 1;
    Object type = getType();
    result = result * prime + (type == null ? 43 : type.hashCode());
    Object zulrahLocation = getZulrahLocation();
    result = result * prime + (zulrahLocation == null ? 43 : zulrahLocation.hashCode());
    result = result * prime + (isJad() ? 79 : 97);
    return result;
  }
}
