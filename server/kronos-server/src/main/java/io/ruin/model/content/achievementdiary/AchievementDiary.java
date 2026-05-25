package io.ruin.model.content.achievementdiary;

import io.ruin.api.utils.StringUtils;
import io.ruin.model.inter.utils.DiaryCompletionPopup;
import io.ruin.model.entity.player.Player;
import io.ruin.model.var.VarPlayerRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class AchievementDiary {

    @RequiredArgsConstructor
    public enum Task {
        // Lumbridge & Draynor Easy
        LUMBRIDGE_EASY_1(DiaryArea.LUMBRIDGE_DRAYNOR, DiaryDifficulty.EASY, "Complete a lap of the Draynor Village Rooftop Agility Course.", new String[]{"10 Agility"}),
        LUMBRIDGE_EASY_2(DiaryArea.LUMBRIDGE_DRAYNOR, DiaryDifficulty.EASY, "Slay a Cave Bug in the Lumbridge Swamp Caves.", new String[]{"15 Slayer"}),
        LUMBRIDGE_EASY_3(DiaryArea.LUMBRIDGE_DRAYNOR, DiaryDifficulty.EASY, "Have Sedridor teleport you to the Essence Mine.", new String[]{"Rune Mysteries"}),
        LUMBRIDGE_EASY_4(DiaryArea.LUMBRIDGE_DRAYNOR, DiaryDifficulty.EASY, "Craft some Water runes.", new String[]{"5 Runecrafting"}),
        LUMBRIDGE_EASY_5(DiaryArea.LUMBRIDGE_DRAYNOR, DiaryDifficulty.EASY, "Catch some Anchovies in Al Kharid.", new String[]{"15 Fishing"}),

        // Ardougne Easy
        ARDOUGNE_EASY_1(DiaryArea.ARDOUGNE, DiaryDifficulty.EASY, "Have Wizard Cromperty teleport you to the Rune Essence Mine", new String[]{}),
        ARDOUGNE_EASY_2(DiaryArea.ARDOUGNE, DiaryDifficulty.EASY, "Steal a cake from the East Ardougne market stalls", new String[]{}),
        ARDOUGNE_EASY_3(DiaryArea.ARDOUGNE, DiaryDifficulty.EASY, "Sell silk to the Silk Trader in East Ardougne for 60 coins each", new String[]{}),
        ARDOUGNE_EASY_4(DiaryArea.ARDOUGNE, DiaryDifficulty.EASY, "Use the altar in East Ardougne church", new String[]{}),
        ARDOUGNE_EASY_5(DiaryArea.ARDOUGNE, DiaryDifficulty.EASY, "Attempt to step onto the Fishing Trawler Boat", new String[]{}),
        ARDOUGNE_EASY_6(DiaryArea.ARDOUGNE, DiaryDifficulty.EASY, "Enter the Combat Training Camp north of West Ardougne", new String[]{}),
        ARDOUGNE_EASY_7(DiaryArea.ARDOUGNE, DiaryDifficulty.EASY, "Have Tindel Marchant identify a rusty sword", new String[]{}),
        ARDOUGNE_EASY_8(DiaryArea.ARDOUGNE, DiaryDifficulty.EASY, "Use the Ardougne lever to teleport to the Wilderness", new String[]{}),
        ARDOUGNE_EASY_9(DiaryArea.ARDOUGNE, DiaryDifficulty.EASY, "View Aleck's Hunter Emporium shop in Yanille", new String[]{}),

        // Ardougne Medium
        ARDOUGNE_MEDIUM_1(DiaryArea.ARDOUGNE, DiaryDifficulty.MEDIUM, "Grapple over Yanille’s south wall", new String[]{}),
        ARDOUGNE_MEDIUM_2(DiaryArea.ARDOUGNE, DiaryDifficulty.MEDIUM, "Harvest strawberries from the Ardougne farming patch", new String[]{}),
        ARDOUGNE_MEDIUM_3(DiaryArea.ARDOUGNE, DiaryDifficulty.MEDIUM, "Cast Ardougne Teleport", new String[]{}),
        ARDOUGNE_MEDIUM_4(DiaryArea.ARDOUGNE, DiaryDifficulty.MEDIUM, "Travel to Castle Wars by hot air balloon", new String[]{}),
        ARDOUGNE_MEDIUM_5(DiaryArea.ARDOUGNE, DiaryDifficulty.MEDIUM, "Claim buckets of sand from Bert", new String[]{}),
        ARDOUGNE_MEDIUM_6(DiaryArea.ARDOUGNE, DiaryDifficulty.MEDIUM, "Pickpocket the master farmer north of East Ardougne", new String[]{}),
        ARDOUGNE_MEDIUM_7(DiaryArea.ARDOUGNE, DiaryDifficulty.MEDIUM, "Equip or upgrade Iban’s staff", new String[]{}),

        // Ardougne Hard
        ARDOUGNE_HARD_1(DiaryArea.ARDOUGNE, DiaryDifficulty.HARD, "Cook a Manta Ray in Port Khazard", new String[]{}),
        ARDOUGNE_HARD_2(DiaryArea.ARDOUGNE, DiaryDifficulty.HARD, "Attempt to picklock the door to the Yanille Agility Dungeon", new String[]{}),
        ARDOUGNE_HARD_3(DiaryArea.ARDOUGNE, DiaryDifficulty.HARD, "Teleport to the Watchtower", new String[]{}),
        ARDOUGNE_HARD_4(DiaryArea.ARDOUGNE, DiaryDifficulty.HARD, "Catch a red salamander", new String[]{}),
        ARDOUGNE_HARD_5(DiaryArea.ARDOUGNE, DiaryDifficulty.HARD, "Check the health of a palm tree near Tree Gnome Village", new String[]{}),
        ARDOUGNE_HARD_6(DiaryArea.ARDOUGNE, DiaryDifficulty.HARD, "Pick poison ivy berries south of East Ardougne", new String[]{}),
        ARDOUGNE_HARD_7(DiaryArea.ARDOUGNE, DiaryDifficulty.HARD, "Smith a mithril platebody near Ardougne", new String[]{}),
        ARDOUGNE_HARD_8(DiaryArea.ARDOUGNE, DiaryDifficulty.HARD, "Enter your Player-Owned House from Yanille", new String[]{}),
        ARDOUGNE_HARD_9(DiaryArea.ARDOUGNE, DiaryDifficulty.HARD, "Smith a dragon sq shield in West Ardougne", new String[]{}),
        ARDOUGNE_HARD_10(DiaryArea.ARDOUGNE, DiaryDifficulty.HARD, "Craft death runes at the Death Altar", new String[]{}),

        // Ardougne Elite
        ARDOUGNE_ELITE_1(DiaryArea.ARDOUGNE, DiaryDifficulty.ELITE, "Equip a salve amulet(i) or imbue one", new String[]{}),
        ARDOUGNE_ELITE_2(DiaryArea.ARDOUGNE, DiaryDifficulty.ELITE, "Pick torstol from the patch north of East Ardougne", new String[]{}),
        ARDOUGNE_ELITE_3(DiaryArea.ARDOUGNE, DiaryDifficulty.ELITE, "Complete a lap of Ardougne rooftop agility course", new String[]{}),

        // Varrock Easy
        VARROCK_EASY_1(DiaryArea.VARROCK, DiaryDifficulty.EASY, "Mine some Iron ore south-east of Varrock.", new String[]{"15 Mining"}),

        // Add more tasks as needed
        ;

        @Getter private final DiaryArea area;
        @Getter private final DiaryDifficulty difficulty;
        @Getter private final String description;
        @Getter private final String[] requirements;
    }

    public static void check(Player player, Task task) {
        if (player.completedDiaryTasks.contains(task.name())) {
            return;
        }
        complete(player, task);
    }

    private static void complete(Player player, Task task) {
        player.completedDiaryTasks.add(task.name());
        player.sendMessage("<col=000080>You have completed a task in the <col=800000>" + task.getArea().getName() + " " + task.getDifficulty().name().toLowerCase() + " diary!");
        updateVarbits(player, task.getArea(), task.getDifficulty());
    }

    public static boolean isCompleted(Player player, DiaryArea area, DiaryDifficulty difficulty) {
        int completedCount = 0;
        int totalCount = 0;
        for (Task t : Task.values()) {
            if (t.getArea() == area && t.getDifficulty() == difficulty) {
                totalCount++;
                if (player.completedDiaryTasks.contains(t.name())) {
                    completedCount++;
                }
            }
        }
        return completedCount == totalCount && totalCount > 0;
    }

    private static void updateVarbits(Player player, DiaryArea area, DiaryDifficulty difficulty) {
        int completedCount = 0;
        for (Task t : Task.values()) {
            if (t.getArea() == area && t.getDifficulty() == difficulty) {
                if (player.completedDiaryTasks.contains(t.name())) {
                    completedCount++;
                }
            }
        }

        VarPlayerRepository varp = getVarp(area, difficulty);
        if (varp != null) {
            varp.set(player, completedCount);
        }

        if (isCompleted(player, area, difficulty)) {
            String key = area.name() + "_" + difficulty.name();
            if (!player.notifiedDiaryCompletions.contains(key)) {
                player.notifiedDiaryCompletions.add(key);
                DiaryCompletionPopup.open(player, area.getName(), difficulty.name());
            }
        }
    }

    private static VarPlayerRepository getVarp(DiaryArea area, DiaryDifficulty difficulty) {
        switch (area) {
            case ARDOUGNE:
                switch (difficulty) {
                    case EASY: return VarPlayerRepository.ARDOUGNE_EASY;
                    case MEDIUM: return VarPlayerRepository.ARDOUGNE_MEDIUM;
                    case HARD: return VarPlayerRepository.ARDOUGNE_HARD;
                    case ELITE: return VarPlayerRepository.ARDOUGNE_ELITE;
                }
                break;
            case DESERT:
                switch (difficulty) {
                    case EASY: return VarPlayerRepository.DESERT_EASY;
                    case MEDIUM: return VarPlayerRepository.DESERT_MEDIUM;
                    case HARD: return VarPlayerRepository.DESERT_HARD;
                    case ELITE: return VarPlayerRepository.DESERT_ELITE;
                }
                break;
            case FALADOR:
                switch (difficulty) {
                    case EASY: return VarPlayerRepository.FALADOR_EASY;
                    case MEDIUM: return VarPlayerRepository.FALADOR_MEDIUM;
                    case HARD: return VarPlayerRepository.FALADOR_HARD;
                    case ELITE: return VarPlayerRepository.FALADOR_ELITE;
                }
                break;
            case FREMENNIK:
                switch (difficulty) {
                    case EASY: return VarPlayerRepository.FREMMY_EASY;
                    case MEDIUM: return VarPlayerRepository.FREMMY_MEDIUM;
                    case HARD: return VarPlayerRepository.FREMMY_HARD;
                    case ELITE: return VarPlayerRepository.FREMMY_ELITE;
                }
                break;
            case KANDARIN:
                switch (difficulty) {
                    case EASY: return VarPlayerRepository.KANDARIN_EASY;
                    case MEDIUM: return VarPlayerRepository.KANDARIN_MEDIUM;
                    case HARD: return VarPlayerRepository.KANDARIN_HARD;
                    case ELITE: return VarPlayerRepository.KANDARIN_ELITE;
                }
                break;
            case KARAMJA:
                switch (difficulty) {
                    case EASY: return VarPlayerRepository.KARAMJA_EASY;
                    case MEDIUM: return VarPlayerRepository.KARAMJA_MEDIUM;
                    case HARD: return VarPlayerRepository.KARAMJA_HARD;
                    case ELITE: return VarPlayerRepository.KARAMJA_ELITE;
                }
                break;
            case LUMBRIDGE_DRAYNOR:
                switch (difficulty) {
                    case EASY: return VarPlayerRepository.LUMBRIDGE_EASY;
                    case MEDIUM: return VarPlayerRepository.LUMBRIDGE_MEDIUM;
                    case HARD: return VarPlayerRepository.LUMBRIDGE_HARD;
                    case ELITE: return VarPlayerRepository.LUMBRIDGE_ELITE;
                }
                break;
            case MORYTANIA:
                switch (difficulty) {
                    case EASY: return VarPlayerRepository.MORYTANIA_EASY;
                    case MEDIUM: return VarPlayerRepository.MORYTANIA_MEDIUM;
                    case HARD: return VarPlayerRepository.MORYTANIA_HARD;
                    case ELITE: return VarPlayerRepository.MORYTANIA_ELITE;
                }
                break;
            case VARROCK:
                switch (difficulty) {
                    case EASY: return VarPlayerRepository.VARROCK_EASY;
                    case MEDIUM: return VarPlayerRepository.VARROCK_MEDIUM;
                    case HARD: return VarPlayerRepository.VARROCK_HARD;
                    case ELITE: return VarPlayerRepository.VARROCK_ELITE;
                }
                break;
            case WILDERNESS:
                switch (difficulty) {
                    case EASY: return VarPlayerRepository.WILDERNESS_EASY;
                    case MEDIUM: return VarPlayerRepository.WILDERNESS_MEDIUM;
                    case HARD: return VarPlayerRepository.WILDERNESS_HARD;
                    case ELITE: return VarPlayerRepository.WILDERNESS_ELITE;
                }
                break;
            case WESTERN_PROVINCES:
                switch (difficulty) {
                    case EASY: return VarPlayerRepository.WESTERN_PROV_EASY;
                    case MEDIUM: return VarPlayerRepository.WESTERN_PROV_MEDIUM;
                    case HARD: return VarPlayerRepository.WESTERN_PROV_HARD;
                    case ELITE: return VarPlayerRepository.WESTERN_PROV_ELITE;
                }
                break;
            case KOUREND_KEBOS:
                switch (difficulty) {
                    case EASY: return VarPlayerRepository.KOUREND_EASY;
                    case MEDIUM: return VarPlayerRepository.KOUREND_MEDIUM;
                    case HARD: return VarPlayerRepository.KOUREND_HARD;
                    case ELITE: return VarPlayerRepository.KOUREND_ELITE;
                }
                break;
        }
        return null;
    }

}
