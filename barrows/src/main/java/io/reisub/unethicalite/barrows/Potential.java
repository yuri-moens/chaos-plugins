package io.reisub.unethicalite.barrows;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Percentage {
    WHATEVER(0, 0),
    DEATH_RUNES(1, 1),
    MAX_DEATH_RUNES(2, 1),
    MAX(0, 0);

    private final int skeletonsToKill;
    private final int bloodwormsToKill;
}
