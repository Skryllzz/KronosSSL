package io.ruin.model;

import io.ruin.model.activities.newshop.NewShopHandler;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.questtab.JournalTab;
import io.ruin.model.item.actions.ItemAction;

public class TrainingAmulet {

	public static void register() {
		ItemAction.registerInventory(30477, "info", (player, item) -> {
			player.dialogue(new ItemDialogue().one(30477, "This amulet will upgrade your starter items as you level up. (E.g. Iron scimitar will turn to steel scimitar at 5 attack)."));
		});
	}
}
