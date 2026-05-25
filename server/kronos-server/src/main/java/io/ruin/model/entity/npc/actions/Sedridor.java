package io.ruin.model.entity.npc.actions;

import io.ruin.model.content.achievementdiary.AchievementDiary;
import io.ruin.model.entity.npc.NPCAction;

public class Sedridor {

	public static void register() {
		NPCAction.register(5034, 1, ((player, npc) -> {
			player.sendMessage("Sedridor teleports you to the essence mine.");
			AchievementDiary.check(player, AchievementDiary.Task.LUMBRIDGE_EASY_3);
			player.getMovement().teleport(2910, 4830, 0);
		}));
	}

}
