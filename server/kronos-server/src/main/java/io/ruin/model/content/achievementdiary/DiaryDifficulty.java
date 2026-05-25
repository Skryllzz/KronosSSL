package io.ruin.model.content.achievementdiary;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DiaryDifficulty {
    EASY(1, "800000"),
    MEDIUM(2, "000080"),
    HARD(3, "004000"),
    ELITE(4, "600000");

    @Getter
    private final int id;

    @Getter
    private final String color;
}
