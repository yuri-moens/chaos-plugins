package io.reisub.unethicalite.zulrah.rotationutils;

import io.reisub.unethicalite.zulrah.constants.StandLocation;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.runelite.api.Prayer;

public final class ZulrahAttributes {

  @Nonnull
  private final StandLocation standLocation;
  @Nullable
  private final StandLocation stallLocation;
  @Nullable
  private final Prayer prayer;
  private final int phaseTicks;

  public ZulrahAttributes(@Nonnull StandLocation standLocation,
      @Nullable StandLocation stallLocation, @Nullable Prayer prayer, int phaseTicks) {
    if (standLocation == null) {
      throw new NullPointerException("standLocation is marked non-null but is null");
    } else {
      this.standLocation = standLocation;
      this.stallLocation = stallLocation;
      this.prayer = prayer;
      this.phaseTicks = phaseTicks;
    }
  }

  @Nonnull
  public StandLocation getStandLocation() {
    return this.standLocation;
  }

  @Nullable
  public StandLocation getStallLocation() {
    return this.stallLocation;
  }

  @Nullable
  public Prayer getPrayer() {
    return this.prayer;
  }

  public int getPhaseTicks() {
    return this.phaseTicks;
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof ZulrahAttributes)) {
      return false;
    } else {
      ZulrahAttributes other = (ZulrahAttributes) o;
      Object thisStandLocation = getStandLocation();
      Object otherStandLocation = other.getStandLocation();
      if (thisStandLocation == null) {
        if (otherStandLocation != null) {
          return false;
        }
      } else if (!thisStandLocation.equals(otherStandLocation)) {
        return false;
      }

      label41: {
        Object thisStallLocation = getStallLocation();
        Object otherStallLocation = other.getStallLocation();
        if (thisStallLocation == null) {
          if (otherStallLocation == null) {
            break label41;
          }
        } else if (thisStallLocation.equals(otherStallLocation)) {
          break label41;
        }

        return false;
      }

      Object thisPrayer = getPrayer();
      Object otherPrayer = other.getPrayer();
      if (thisPrayer == null) {
        if (otherPrayer != null) {
          return false;
        }
      } else if (!thisPrayer.equals(otherPrayer)) {
        return false;
      }

      if (getPhaseTicks() != other.getPhaseTicks()) {
        return false;
      } else {
        return true;
      }
    }
  }

  public int hashCode() {
    byte prime = 59;
    int result = 1;
    Object standLocation = getStandLocation();
    result = result * prime + standLocation.hashCode();
    Object stallLocation = getStallLocation();
    result = result * prime + (stallLocation == null ? 43 : stallLocation.hashCode());
    Object prayer = getPrayer();
    result = result * prime + (prayer == null ? 43 : prayer.hashCode());
    result = result * prime + getPhaseTicks();
    return result;
  }

  public String toString() {
    StandLocation standLocation = getStandLocation();
    return "ZulrahAttributes(standLocation=" + standLocation + ", stallLocation="
        + getStallLocation() + ", prayer=" + getPrayer() + ", phaseTicks=" + getPhaseTicks() + ")";
  }
}
