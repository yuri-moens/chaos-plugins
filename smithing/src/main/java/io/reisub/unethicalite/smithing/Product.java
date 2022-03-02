package io.reisub.unethicalite.smithing;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Product { // TODO: add all items
    PLATEBODY(5, 22);

    private final int requiredBars;
    private final int interfaceId;
}
