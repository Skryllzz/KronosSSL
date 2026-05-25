package io.ruin.model.content.achievementdiary;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.actions.SlotAction;
import io.ruin.model.stat.StatType;

import java.util.ArrayList;
import java.util.List;

public class AchievementDiaryInterface {

    public static void register() {
        // Achievement Diary List Interface (259)
        InterfaceHandler.register(259, h -> {
            h.actions[2] = (SlotAction) (player, slot) -> {
                DiaryArea area = null;
                switch (slot) {
                    case 1: area = DiaryArea.ARDOUGNE; break;
                    case 5: area = DiaryArea.DESERT; break;
                    case 2: area = DiaryArea.FALADOR; break;
                    case 3: area = DiaryArea.FREMENNIK; break;
                    case 4: area = DiaryArea.KANDARIN; break;
                    case 0: area = DiaryArea.KARAMJA; break;
                    case 11: area = DiaryArea.KOUREND_KEBOS; break;
                    case 6: area = DiaryArea.LUMBRIDGE_DRAYNOR; break;
                    case 7: area = DiaryArea.MORYTANIA; break;
                    case 8: area = DiaryArea.VARROCK; break;
                    case 10: area = DiaryArea.WESTERN_PROVINCES; break;
                    case 9: area = DiaryArea.WILDERNESS; break;
                }

                if (area != null) {
                    System.out.println("[DEBUG_LOG] Opening diary journal: " + area.getName() + " from slot " + slot);
                    openJournal(player, area);
                } else {
                    System.out.println("[DEBUG_LOG] Unhandled diary slot: " + slot);
                }
            };
        });
    }

    private static void openJournal(Player player, DiaryArea area) {
        List<String> lines = new ArrayList<>();
        lines.add(""); // Top margin
        for (DiaryDifficulty difficulty : DiaryDifficulty.values()) {
            lines.add("<col=800000><u>" + difficulty.name().charAt(0) + difficulty.name().substring(1).toLowerCase() + " Tasks</u></col>");
            boolean found = false;
            for (AchievementDiary.Task task : AchievementDiary.Task.values()) {
                if (task.getArea() == area && task.getDifficulty() == difficulty) {
                    boolean completed = player.completedDiaryTasks.contains(task.name());
                    String text = task.getDescription();
                    if (completed) {
                        text = "<str>" + text + "</str>";
                    }
                    lines.add(text);

                    if (task.getRequirements() != null) {
                        for (String req : task.getRequirements()) {
                            boolean met = hasRequirement(player, req);
                            String color = met ? "<col=0000FF>" : "<col=800000>";
                            String reqText = color + "(" + req + ")</col>";
                            if (met) {
                                reqText = "<str>" + reqText + "</str>";
                            }
                            lines.add("    " + reqText);
                        }
                    }
                    found = true;
                }
            }
            if (!found) {
                lines.add("No tasks defined.");
            }
            lines.add(""); // Spacer
        }
        player.sendScroll("Achievement Diary - " + area.getName(), lines.toArray(new String[0]));
    }

    private static boolean hasRequirement(Player player, String req) {
        String[] split = req.split(" ", 2);
        if (split.length == 2 && isInteger(split[0])) {
            int level = Integer.parseInt(split[0]);
            StatType stat = StatType.get(split[1]);
            if (stat != null) {
                return player.getStats().get(stat).fixedLevel >= level;
            }
        }
        // Fallback for quests - default to true for now since quests aren't fully integrated
        return true;
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
