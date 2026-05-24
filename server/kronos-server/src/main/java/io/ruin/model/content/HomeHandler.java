package io.ruin.model.content;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.ObjType;
import io.ruin.content.maps.*;
import io.ruin.model.activities.bosses.instancetoken.InstanceMaps;
import io.ruin.model.activities.newshop.NewShopHandler;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.ThePetHunter;
import io.ruin.model.activities.tempevents.summerevent.SummerEvent;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.content.itembreaking.ItemBreakingHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.Toxins;
import io.ruin.model.inter.Widget;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemNPCAction;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.item.actions.impl.ItemBreaking;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.MapListener;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.shop.ShopManager;
import io.ruin.model.skills.BotPrevention;
import io.ruin.model.skills.magic.SpellBook;
import io.ruin.model.skills.thieving.Stall;
import io.ruin.model.stat.Stat;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;

import static io.ruin.cache.ItemID.COINS_995;

public class HomeHandler {

	static Bounds homeArea = new Bounds(3073, 3462, 3123, 3520, -1);

	private static void openTeleportInterface(Player player) {
		if (player.getPosition().regionId() == 12342) {
			player.teleportInterface.open(player);
		}
	}

	public static void init() {
		//var tradingPost = GameObject.spawn(46241, 3088, 3487, 0, 10, 2);
	}

	public static void switchBook(Player player, SpellBook book, boolean altar) {
		if (book.isActive(player) && altar) {
			player.dialogue(new MessageDialogue("You're already using that spellbook."));
			return;
		}
		if (altar) {
			player.sendMessage("You pray at the altar...");
			player.startEvent(event -> {
				player.animate(645);
				event.delay(1);
				player.sendMessage("... and gain the knowledge of %s magic.".formatted(book.name));
			});
		}
		if (player.getCombat().autocastSpell != null) {
			player.getCombat().autocastSpell = null;
			VarPlayerRepository.AUTOCAST.set(player, 0);
			player.sendMessage("You have stopped casting.");
		}
		book.setActive(player);
		player.homeSpellbookAltarsUsed++;
		if (player.homeSpellbookAltarsUsed == Achievements.OH_CUL.getCompletionAmount())
			player.sendMessage("<col=000080>You have completed the achievement: <col=800000>%s"
					.formatted(Achievements.OH_CUL.getAchievementName()));

	}

	public static void drinkFromPool(Player player) {
		if (player.wildernessLevel > 0 || player.inTob) {
			return; // Exit the method if in the wilderness
		}
		if (player.teleportListener != null && !player.teleportListener.allow(player)) {
			return;
		}

		VarPlayerRepository.SPECIAL_ENERGY.set(player, 1000);
		player.getMovement().restoreEnergy(100);
		player.getStats().get(StatType.Prayer).restore();
		for (Stat stat : player.getStats().get()) {
			if (stat != player.getStats().get(StatType.Hitpoints) && stat.currentLevel < stat.fixedLevel)
				stat.alter(stat.fixedLevel);
		}
		player.setHp(player.getMaxHp());
		player.cureVenom(1);
		player.cureVenom(1);
		VarPlayerRepository.POISONED.set(player, 0);
		if (player.isEnvenomed())
			player.toxins.cure(Toxins.ToxinType.VENOM, 0);
		if (player.isPoisoned())
			player.toxins.cure(Toxins.ToxinType.POSION, 0);
		player.sendFilteredMessage("The effects of the ornate pool replenish you.");
		player.homePoolsUsed++;
		if (player.homePoolsUsed == Achievements.JUST_A_LITTLE_BOOST.getCompletionAmount())
			player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
					+ Achievements.JUST_A_LITTLE_BOOST.getAchievementName());

		// reset veng issue
		VarPlayerRepository.VENG_COOLDOWN.set(player, 0);
		player.vengeanceActive = false;
		player.getPacketSender().sendWidgetTimerCustom(Widget.VENGEANCE, 0);
	}

	public static double getPetDonatorBoost(Player player) {
		switch (player.getSecondaryGroup()) {
			case SUPER_DONATOR -> {
				return 0.98;
			}
			case ELITE_DONATOR -> {
				return 0.96;
			}
			case NOBLE_DONATOR -> {
				return 0.94;
			}
			case GOLD_DONATOR -> {
				return 0.93;
			}
			case PLATINUM_DONATOR -> {
				return 0.92;
			}
			case LEGENDARY_DONATOR -> {
				return 0.91;
			}
			case SUPREME_DONATOR -> {
				return 0.90;
			}
		}
		return 1;
	}

	public static void register() {
		NPCAction.register(2817, 1, (player, npc) -> ShopManager.openIfExists(player, "e522b145-fbaa-4746-8d71-6ad9808d2338"));
		NPCAction.register(822,"Trade", (player, npc) -> ShopManager.openIfExists(player, "qfdqdq9d-37cs-4ed9-qfec-680bqaq4dfa4"));
		ObjectAction.register(33410, 1, (player, obj) -> openTeleportInterface(player));
		/*ObjectAction.register(31861, actions -> {
			actions[1] = (player, obj) -> {
				player.dialogue(
						new OptionsDialogue("Select which prayer book you'd like to switch to:",
								new Option("Modern", () -> switchBook(player, SpellBook.MODERN, true)),
								new Option("Ancient", () -> switchBook(player, SpellBook.ANCIENT, true)),
								new Option("Lunar", () -> switchBook(player, SpellBook.LUNAR, true)),
								new Option("Arceuus", () -> switchBook(player, SpellBook.ARCEUUS, true))));
			};
			actions[2] = (player, obj) -> switchBook(player, SpellBook.MODERN, true);
			actions[3] = (player, obj) -> switchBook(player, SpellBook.ANCIENT, true);
			actions[4] = (player, obj) -> switchBook(player, SpellBook.LUNAR, true);
			actions[5] = (player, obj) -> switchBook(player, SpellBook.ARCEUUS, true);
		});*/
	}
}
