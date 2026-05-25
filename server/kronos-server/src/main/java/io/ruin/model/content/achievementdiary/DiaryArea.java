package io.ruin.model.content.achievementdiary;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DiaryArea {
    ARDOUGNE("Ardougne", 6291),
    DESERT("Desert", 6297),
    FALADOR("Falador", 6301),
    FREMENNIK("Fremennik", 6305),
    KANDARIN("Kandarin", 6309),
    KARAMJA("Karamja", 6311),
    LUMBRIDGE_DRAYNOR("Lumbridge & Draynor", 6315),
    MORYTANIA("Morytania", 6321),
    VARROCK("Varrock", 6325),
    WILDERNESS("Wilderness", 6329),
    WESTERN_PROVINCES("Western Provinces", 6333),
    KOUREND_KEBOS("Kourend & Kebos", 6337);

    @Getter
    private final String name;

    @Getter
    private final int varbitId;
}
