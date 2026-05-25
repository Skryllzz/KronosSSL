package io.ruin.model.map.object.actions.impl.ardougne;

import io.ruin.model.content.achievementdiary.AchievementDiary;
import io.ruin.model.map.object.actions.impl.Door;

public class Gate {

    public static void register() {
        Door.hooks.registerReturn(Door.Hook.OnHandle.class, hook -> {
            var player = hook.player();
            var obj = hook.obj();
            if (obj.getId() == 1571 || obj.getId() == 1572) {
                if (obj.x >= 2517 && obj.x <= 2518 && obj.y == 3357) {
                    AchievementDiary.check(player, AchievementDiary.Task.ARDOUGNE_EASY_6);
                }
            }
            return false;
        });
    }

}
