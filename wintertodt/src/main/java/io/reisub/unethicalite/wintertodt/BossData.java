package io.reisub.unethicalite.wintertodt;

import javax.annotation.concurrent.Immutable;
import lombok.Value;

@Value
@Immutable
public class BossData {
  int health;
  int world;
  long time;
  int timer;
}
