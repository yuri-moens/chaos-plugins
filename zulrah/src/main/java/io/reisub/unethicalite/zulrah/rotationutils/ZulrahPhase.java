package io.reisub.unethicalite.zulrah.rotationutils;

public final class ZulrahPhase {

  private final ZulrahNpc zulrahNpc;
  private final ZulrahAttributes attributes;

  public ZulrahPhase(ZulrahNpc zulrahNpc, ZulrahAttributes attributes) {
    this.zulrahNpc = zulrahNpc;
    this.attributes = attributes;
  }

  public ZulrahNpc getZulrahNpc() {
    return zulrahNpc;
  }

  public ZulrahAttributes getAttributes() {
    return attributes;
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof ZulrahPhase)) {
      return false;
    } else {
      ZulrahPhase other = (ZulrahPhase) o;
      Object thisZulrahNpc = getZulrahNpc();
      Object otherZulrahNpc = other.getZulrahNpc();
      if (thisZulrahNpc == null) {
        if (otherZulrahNpc != null) {
          return false;
        }
      } else if (!thisZulrahNpc.equals(otherZulrahNpc)) {
        return false;
      }

      Object thisAttributes = getAttributes();
      Object otherAttributes = other.getAttributes();
      if (thisAttributes == null) {
        if (otherAttributes != null) {
          return false;
        }
      } else if (!thisAttributes.equals(otherAttributes)) {
        return false;
      }

      return true;
    }
  }

  public int hashCode() {
    int prime = 59;
    int result = 1;
    ZulrahNpc zulrahNpc = getZulrahNpc();
    result = result * prime + (zulrahNpc == null ? 43 : ((Object) zulrahNpc).hashCode());
    ZulrahAttributes attributes = getAttributes();
    result = result * prime + (attributes == null ? 43 : ((Object) attributes).hashCode());
    return result;
  }

  public String toString() {
    ZulrahNpc zulrahNpc = getZulrahNpc();
    return "ZulrahPhase(zulrahNpc=" + zulrahNpc + ", attributes=" + getAttributes() + ")";
  }
}
