package io.reisub.unethicalite.utils.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PortalTeleport {

  BARROWS("Barrows", 0),
  HARMONY_ISLAND("Harmony Island", 37589),
  TROLL_STRONGHOLD("Troll Stronghold", 33179),
  WEISS("Weiss", 37581),
  FOSSIL_ISLAND("Fossil Island", 0)
  ;

  private final String name;
  private final int portalId;

}
