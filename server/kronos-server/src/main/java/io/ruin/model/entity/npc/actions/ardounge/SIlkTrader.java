package io.ruin.model.entity.npc.actions.ardounge;

import io.ruin.cache.ItemID;
import io.ruin.model.content.achievementdiary.AchievementDiary;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.ActionDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;

public class SIlkTrader {

	private static final int SILK_ID = ItemID.SILK;
	private static final int COINS_ID = ItemID.COINS_995;
	private static final int SILK_PRICE = 60;
	private static final int SILK_TRADER = 3211;

	public static void register() {
		NPCAction.register(SILK_TRADER, "talk-to", (player, npc) -> talkTo(player));
	}

	public static void talkTo(Player player) {
		if (player.lastSilkSteal.isDelayed()) {
			player.dialogue(
					new NPCDialogue(SILK_TRADER, "I don't want to do business with you. I've heard that you have been seen stealing silk from the market stallholders lately.")
			);
			return;
		}

		player.dialogue(
				new NPCDialogue(SILK_TRADER, "Hello. What can I do for you?"),
				new OptionsDialogue("Select an option",
						new Option("I have some silk I'd like to sell.", () -> sellSilk(player)),
						new Option("Nothing, thanks.")
				)
		);
	}

	public static void sellSilk(Player player) {
		player.dialogue(
				new PlayerDialogue("I have some silk I'd like to sell."),
				new NPCDialogue(SILK_TRADER, "I'll give you 60 gp for each piece of silk you have. Do you want to sell?"),
				new OptionsDialogue("Sell all your silk for 60 gp each?",
						new Option("Yes.", () -> {
							int count = player.getInventory().count(SILK_ID);
							if (count > 0) {
								player.getInventory().remove(SILK_ID, count);
								player.getInventory().add(COINS_ID, count * SILK_PRICE);
								player.dialogue(new NPCDialogue(SILK_TRADER, "Thank you very much. Come back when you have some more."));
								AchievementDiary.check(player, AchievementDiary.Task.ARDOUGNE_EASY_3);
							} else {
								player.dialogue(new NPCDialogue(SILK_TRADER, "You don't have any silk to sell!"));
							}
						}),
						new Option("No.")
				)
		);
	}

}
