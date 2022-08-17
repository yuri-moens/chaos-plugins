package io.reisub.unethicalite.barbarianassault.data;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.widgets.WidgetID;

@RequiredArgsConstructor
@Getter
public enum Role {
  ATTACKER("Attacker horn", WidgetID.BA_ATTACKER_GROUP_ID, 10, 8,
      ImmutableSet.of(Call.RED, Call.GREEN, Call.BLUE)),
  DEFENDER("Defender horn", WidgetID.BA_DEFENDER_GROUP_ID, 9, 7,
      ImmutableSet.of(Call.POISONED_TOFU, Call.POISONED_MEAT, Call.POISONED_WORMS)),
  COLLECTOR("Collector horn", WidgetID.BA_COLLECTOR_GROUP_ID, 9, 7,
      ImmutableSet.of(Call.ACCURATE, Call.AGGRESSIVE, Call.CONTROLLED, Call.DEFENSIVE)),
  HEALER("Healer horn", WidgetID.BA_HEALER_GROUP_ID, 9, 7,
      ImmutableSet.of(Call.TOFU, Call.CRACKERS, Call.WORMS));

  private final String horn;
  private final int groupId;
  private final int shoutIndex;
  private final int listenIndex;
  private final Set<Call> calls;
}
