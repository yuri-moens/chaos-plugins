package io.reisub.unethicalite.shafter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Type {
  MAPLE("Maple tree");

  private final String name;
}
