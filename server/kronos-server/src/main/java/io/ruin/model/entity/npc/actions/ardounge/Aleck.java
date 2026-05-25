package io.ruin.model.entity.npc.actions.ardounge;

import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.shop.ShopManager;
import io.ruin.model.content.achievementdiary.AchievementDiary;


public class Aleck {

	public static void register() {
		NPCAction.register(1501, "Trade", (player, npc) -> {
			ShopManager.openIfExists(player, "mbXtTbg8yhKf9QYxqY0USiEYdmSQfv");
			AchievementDiary.check(player, AchievementDiary.Task.ARDOUGNE_EASY_9);
		});
	}

}
