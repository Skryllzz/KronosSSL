package io.ruin.model.entity.npc.actions.portkhazard;

import io.ruin.api.utils.Random;
import io.ruin.cache.ItemID;
import io.ruin.cache.NpcID;
import io.ruin.model.content.achievementdiary.AchievementDiary;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.ItemNPCAction;

public class TindelMarchant {

	private static final int TINDEL_MARCHANT = NpcID.TINDEL_MARCHANT;
	private static final int RUSTY_SWORD = ItemID.RUSTY_SWORD;

	private static final int[] IDENTIFIED_SWORDS = {
			ItemID.BRONZE_SWORD,
			ItemID.IRON_SWORD,
			ItemID.STEEL_SWORD,
			ItemID.MITHRIL_SWORD,
			ItemID.ADAMANT_SWORD,
			ItemID.RUNE_SWORD
	};

	public static void register() {
		NPCAction.register(TINDEL_MARCHANT, "talk-to", (player, npc) -> talkTo(player));
		ItemNPCAction.register(RUSTY_SWORD, TINDEL_MARCHANT, (player, item, npc) -> identify(player));
	}

	private static void talkTo(Player player) {
		player.dialogue(
				new NPCDialogue(TINDEL_MARCHANT, "Hello there, I'm Tindel Marchant. I'm a specialist in weaponry."),
				new NPCDialogue(TINDEL_MARCHANT, "I can identify old, rusty swords if you have any. It'll cost you 100 gold coins per sword."),
				new OptionsDialogue("Select an option",
						new Option("I have a rusty sword I'd like you to look at.", () -> identify(player)),
						new Option("I don't have any right now.", () -> player.dialogue(new PlayerDialogue("I don't have any right now.")))
				)
		);
	}

	private static void identify(Player player) {
		if (!player.getInventory().contains(RUSTY_SWORD, 1)) {
			player.dialogue(new NPCDialogue(TINDEL_MARCHANT, "You don't have any rusty swords for me to identify."));
			return;
		}

		if (!player.getInventory().contains(ItemID.COINS_995, 100)) {
			player.dialogue(new NPCDialogue(TINDEL_MARCHANT, "You need 100 gold coins for me to identify that sword."));
			return;
		}

		player.dialogue(
				new PlayerDialogue("Can you identify this sword for me?"),
				new NPCDialogue(TINDEL_MARCHANT, "Certainly. That'll be 100 gold coins, please."),
				new OptionsDialogue("Pay 100 gold coins?",
						new Option("Yes", () -> {
							if (player.getInventory().contains(RUSTY_SWORD, 1) && player.getInventory().contains(ItemID.COINS_995, 100)) {
								player.getInventory().remove(RUSTY_SWORD, 1);
								player.getInventory().remove(ItemID.COINS_995, 100);
								int resultSword = IDENTIFIED_SWORDS[Random.get(IDENTIFIED_SWORDS.length)];
								player.getInventory().add(resultSword, 1);
								player.dialogue(new NPCDialogue(TINDEL_MARCHANT, "There you go. It's cleaned up quite nicely."));
								AchievementDiary.check(player, AchievementDiary.Task.ARDOUGNE_EASY_7);
							}
						}),
						new Option("No", () -> player.dialogue(new PlayerDialogue("No, thanks.")))
				)
		);
	}

}
