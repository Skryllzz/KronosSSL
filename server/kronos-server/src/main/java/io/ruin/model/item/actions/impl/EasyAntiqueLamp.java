package io.ruin.model.item.actions.impl;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.StringUtils;
import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.stat.StatType;

public enum EasyAntiqueLamp {

	ATTACK(3, 26, StatType.Attack, true, false),
	STRENGTH(4, 27, StatType.Strength, true, false),
	RANGED(5, 28, StatType.Ranged, true, false),
	MAGIC(6, 29, StatType.Magic, true, false),
	DEFENCE(7, 30, StatType.Defence, true, false),
	HITPOINTS(8, 31, StatType.Hitpoints, true, false),
	PRAYER(9, 32, StatType.Prayer, true, false),
	AGILITY(10, 33, StatType.Agility, false, false),
	HERBLORE(11, 34, StatType.Herblore, false, false),
	THIEVING(12, 35, StatType.Thieving, false, false),
	CRAFTING(13, 36, StatType.Crafting, false, false),
	RUNECRAFTING(14, 37, StatType.Runecrafting, false, false),
	SLAYER(22, 38, StatType.Slayer, false, false),
	FARMING(23, 39, StatType.Farming, false, false),
	MINING(15, 40, StatType.Mining, false, false),
	SMITHING(16, 41, StatType.Smithing, false, false),
	FISHING(17, 42, StatType.Fishing, false, false),
	COOKING(18, 43, StatType.Cooking, false, false),
	FIREMAKING(19, 44, StatType.Firemaking, false, false),
	WOODCUTTING(20, 45, StatType.Woodcutting, false, false),
	FLETCHING(21, 46, StatType.Fletching, false, false),
	CONSTRUCTION(24, 48, StatType.Construction, false, false),
	HUNTER(25, 47, StatType.Hunter, false, false);

	private int childId;
	private StatType statType;
	private boolean combat;
	private boolean disabled;
	private int hiddenChildId;

	EasyAntiqueLamp(int childId, int hiddenChildId, StatType statType, boolean combat, boolean disabled) {
		this.childId = childId;
		this.statType = statType;
		this.combat = combat;
		this.disabled = disabled;
		this.hiddenChildId = hiddenChildId;
	}

	public static final EasyAntiqueLamp[] VALUES = values();

	public static final int LAMP_ID = 13145;
	private static final int XP_REWARD = 2500;
	private static final int MIN_LEVEL = 30;

	public static void register() {
		ItemAction.registerInventory(LAMP_ID, "rub", (player, item) -> {
			if (player.experienceLock) {
				player.sendMessage("Your experience is currently locked.");
				return;
			}
			player.openInterface(ToplevelComponent.MAINMODAL, 1103);
			player.getPacketSender().sendSkillinterface("");
		});

		InterfaceHandler.register(1103, h -> {
			for (EasyAntiqueLamp skill : EasyAntiqueLamp.VALUES) {
				h.actions[skill.childId] = (SimpleAction) player -> {
					if (!player.getInventory().contains(LAMP_ID, 1)) {
						player.closeInterface(ToplevelComponent.MAINMODAL);
						return;
					}
					player.openInterface(ToplevelComponent.MAINMODAL, 1103);
					String skillName = StringUtils.getFormattedEnumName(skill);
					if (skill.disabled) {
						player.sendMessage(Color.DARK_RED.wrap(skillName + " is currently disabled!"));
						return;
					}
					for (EasyAntiqueLamp skills : EasyAntiqueLamp.VALUES) {
						player.getPacketSender().setHidden(1103, skills.hiddenChildId, true);
					}
					player.getPacketSender().setHidden(1103, skill.hiddenChildId, false);
					player.getPacketSender().sendString(1103, 75, "You will receive " + XP_REWARD + " experience in " + skillName + ".");
					player.getPacketSender().setHidden(1103, 75, false);
					player.selectedSkillLampSkill = skill.statType;
					player.getPacketSender().sendSkillinterface(skillName);
				};
			}
			h.actions[72] = (SimpleAction) player -> {
				player.closeInterface(ToplevelComponent.MAINMODAL);
			};
			h.actions[77] = (SimpleAction) player -> {
				Item lamp = player.getInventory().findItem(LAMP_ID);
				if (lamp == null)
					return;
				if (player.selectedSkillLampSkill == null) {
					player.sendMessage(Color.DARK_RED.wrap("You must select a skill before confirming."));
					return;
				}
				if (player.getStats().get(player.selectedSkillLampSkill).fixedLevel < MIN_LEVEL) {
					player.sendMessage("You need a " + player.selectedSkillLampSkill.name() + " level of at least " + MIN_LEVEL + " to use this lamp.");
					return;
				}

				player.closeInterface(ToplevelComponent.MAINMODAL);
				lamp.remove();
				player.getStats().addXp(player.selectedSkillLampSkill, XP_REWARD, true);
				player.sendMessage(Color.DARK_GREEN.wrap("You have been rewarded " + XP_REWARD + " " + player.selectedSkillLampSkill.name() + " experience."));
			};
		});
	}
}
