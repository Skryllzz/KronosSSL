package io.ruin.model.map.object.actions.impl;

import io.ruin.model.content.achievementdiary.AchievementDiary;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.skills.Tool;

public class Gangplank {

	public static void register() {
		ObjectAction.register(4977, 1, (player, obj) -> {
			if (player.getPosition().inBounds(PORT_KHAZARD)) {
				AchievementDiary.check(player, AchievementDiary.Task.ARDOUGNE_EASY_5);
				player.sendMessage("You attempt to cross the gangplank, however fishing trawler is currently docked.");
			}
		});
	}


private static final Bounds PORT_KHAZARD = new Bounds(2665, 3160, 2676, 3173, 0);

}
