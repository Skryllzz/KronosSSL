package io.ruin.model.entity.player;

import io.netty.channel.Channel;
import io.ruin.HooksV2;
import io.ruin.api.protocol.PlatformInfo;
import io.ruin.api.protocol.login.LoginInfo;
import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.ServerWrapper;
import io.ruin.cache.Color;
import io.ruin.cache.InterfaceDef;
import io.ruin.data.impl.npcs.npc_combat;
import io.ruin.db.DatabaseFile;
import io.ruin.db.PlayerDatabase;
import io.ruin.model.ScrollbarClientScript;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.SecurityGuard;
import io.ruin.model.activities.bosses.instancetoken.InstanceHandler;
import io.ruin.model.activities.bosses.instancetoken.InstanceManager;
import io.ruin.model.activities.bosses.instancetoken.InstanceTokenInterface;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.activities.dailytasks.DailyTasksInterface;
import io.ruin.model.activities.duelarena.Duel;
import io.ruin.model.activities.duelarena.DuelArena;
import io.ruin.model.activities.gamble.Gamble;
import io.ruin.model.activities.gamble.GambleGameHandler;
import io.ruin.model.activities.gamble.GambleManager;
import io.ruin.model.activities.moonsofperil.MoonsOfPerilHandler;
import io.ruin.model.activities.newcomertasks.NewcomerTasks;
import io.ruin.model.activities.newcomertasks.NewcomerTasksInterface;
import io.ruin.model.activities.newshop.NewShopInterface;
import io.ruin.model.activities.perktree.PerkSets;
import io.ruin.model.activities.perktree.PlayerPerkHandler;
import io.ruin.model.activities.perktree.PlayerPerkInterface;
import io.ruin.model.activities.perktree.perksets.GoldDigger;
import io.ruin.model.activities.raids.chambersrework.CustomXericRaid;
import io.ruin.model.activities.raids.toa.ToAPartyInterface;
import io.ruin.model.activities.raids.toa.ToASuppliesInterface;
import io.ruin.model.activities.raids.toa.ToaApplicantsInterface;
import io.ruin.model.activities.raids.toa.ToaInvocationsInterface;
import io.ruin.model.activities.raids.toa.ToaMembersInterface;
import io.ruin.model.activities.raids.toa.ToaPartyViewInterface;
import io.ruin.model.activities.raids.toa.TombsOfAmascut;
import io.ruin.model.activities.raids.toa.TombsOfAmascutManager;
import io.ruin.model.activities.raids.tob.dungeon.room.TheatreRoom;
import io.ruin.model.activities.raids.tob.party.TheatrePartyManager;
import io.ruin.model.activities.wilderness.BountyHunter;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.content.DailyVoteInterface;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueInterface;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.content.combatachievements.CombatAchievementInterface;
import io.ruin.model.content.combatachievements.CombatAchievementSystem;
import io.ruin.model.content.equipmentpresets.GearPresetInterface;
import io.ruin.model.content.itembreaking.ItemBreakInterface;
import io.ruin.model.content.itembreaking.ItemUpgradeInterface;
import io.ruin.model.content.upgrade.ItemEffect;
import io.ruin.model.content.upgradesystem.UpgradeSystemInterface;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.HealthHud;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.UpdateMask;
import io.ruin.model.entity.shared.listeners.DailyResetListener;
import io.ruin.model.entity.shared.listeners.LoginListener;
import io.ruin.model.entity.shared.masks.AnimationUpdate;
import io.ruin.model.entity.shared.masks.Appearance;
import io.ruin.model.entity.shared.masks.ChatUpdate;
import io.ruin.model.entity.shared.masks.EntityDirectionUpdate;
import io.ruin.model.entity.shared.masks.ForceMovementUpdate;
import io.ruin.model.entity.shared.masks.ForceTextUpdate;
import io.ruin.model.entity.shared.masks.GraphicsUpdate;
import io.ruin.model.entity.shared.masks.HitsUpdate;
import io.ruin.model.entity.shared.masks.MapDirectionUpdate;
import io.ruin.model.entity.shared.masks.ModelTintUpdate;
import io.ruin.model.entity.shared.masks.MovementModeUpdate;
import io.ruin.model.entity.shared.masks.PlayerOpsUpdate;
import io.ruin.model.entity.shared.masks.TeleportModeUpdate;
import io.ruin.model.inter.AccessMask;
import io.ruin.model.inter.AccessMasks;
import io.ruin.model.inter.ClientInterfaceType;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceEventMask;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.Subcomponent;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.ToplevelInterfaceType;
import io.ruin.model.inter.dialogue.Dialogue;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.handlers.DropViewer;
import io.ruin.model.inter.handlers.LootsViewer;
import io.ruin.model.inter.questtab.presets.PresetCustom;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.ItemContainer;
import io.ruin.model.item.actions.impl.boxes.mystery.SuperMysteryBox;
import io.ruin.model.item.actions.impl.chargable.SerpentineHelm;
import io.ruin.model.item.actions.impl.scratchcard.ScratchCardManager;
import io.ruin.model.item.actions.impl.storage.*;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.item.containers.Inventory;
import io.ruin.model.item.containers.Trade;
import io.ruin.model.item.containers.bank.Bank;
import io.ruin.model.item.containers.bank.BankPin;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.MapListener;
import io.ruin.model.map.Position;
import io.ruin.model.map.Region;
import io.ruin.model.map.Tile;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.map.object.actions.impl.edgeville.Christmas;
import io.ruin.model.map.route.routes.TargetRoute;
import io.ruin.model.skills.construction.House;
import io.ruin.model.skills.construction.room.Room;
import io.ruin.model.skills.farming.Farming;
import io.ruin.model.skills.hunter.Hunter;
import io.ruin.model.skills.magic.spells.modern.ModernTeleport;
import io.ruin.model.stat.Stat;
import io.ruin.model.stat.StatList;
import io.ruin.model.stat.StatType;
import io.ruin.model.tutorial.GameModeInterface;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.network.PacketSender;
import io.ruin.rsprot.RSProtService;
import io.ruin.rsprot.RSProtServiceFactory;
import io.ruin.services.Loggers;
import io.ruin.services.http.hiscores.Hiscores;
import io.ruin.test.ServerTest;
import io.ruin.utility.CS2Script;
import io.ruin.utility.TickDelay;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.internal.utils.Checks;
import net.rsprot.protocol.api.NetworkService;
import net.rsprot.protocol.api.Session;
import net.rsprot.protocol.common.client.OldSchoolClientType;
import net.rsprot.protocol.game.outgoing.info.playerinfo.PlayerAvatar;
import net.rsprot.protocol.game.outgoing.info.playerinfo.PlayerAvatarExtendedInfo;
import org.rsmod.util.EntityHelperKt;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.ruin.cache.ItemID.BLOOD_MONEY;
import static io.ruin.model.var.VarPlayerRepository.PET_NPC_INDEX;

@Slf4j
public class Player extends PlayerAttributes {
	public static interface Hook {
		record OnInit(Player player) implements Hook {
		}

		record OnStart(Player player) implements Hook {
		}

		// When player logic finished logout stage is at 'PostLogout'
		record OnFinish(Player player) implements Hook {
		}

		// When player logs out and gets removed from world
		record OnRemoved(Player player) implements Hook {
		}

		record OnAddCollectionLog(Player player, Item... items) implements Hook {
		}
	}

	public static HooksV2<Hook> hooks = new HooksV2<>(Hook.class);

	public static enum LogoutStage {
		// Normal logged in player
		NoLogout,
		// Logout requested for a player, waiting for combat and other locks
		LogoutRequested,
		// No locks, player logged out, post logout processing
		LogoutAccepted,
		// Player file is being saved, and others post logout processing
		PostLogout,
		// Player logged out and removed completelly
		LoggedOut;

		public boolean isAnyOf(LogoutStage... stages) {
			for (var stage : stages) {
				if (stage == this) {
					return true;
				}
			}
			return false;
		}
	}

	public boolean breakAction;

	public Player() {
		this.player = this;
	}

	private transient int pid;

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public static Bounds EDGEVILLE = new Bounds(3063, 3457, 3121, 3519, -1);

	private static List<Integer> barrowsIds = Arrays.asList(
			4755, 4982, 4983, 4984, 4985, 4759, 4994, 4995, 4996, 4997,
			4757, 4988, 4989, 4990, 4991, 4753, 4976, 4977, 4978, 4979,
			4716, 4980, 4981, 4982, 4983, 4720, 4892, 4893, 4894, 4895,
			4722, 4898, 4899, 4900, 4901, 4718, 4886, 4887, 4888, 4889,
			4724, 4904, 4905, 4906, 4907, 4728, 4916, 4917, 4918, 4919,
			4730, 4922, 4923, 4924, 4925, 4726, 4910, 4911, 4912, 4913,
			4745, 4952, 4953, 4954, 4955, 4749, 4964, 4965, 4966, 4967,
			4751, 4970, 4971, 4972, 4973, 4747, 4958, 4959, 4960, 4961,
			4708, 4856, 4857, 4858, 4859, 4712, 4868, 4869, 4870, 4871,
			4714, 4874, 4875, 4876, 4877, 4710, 4862, 4863, 4864, 4865,
			4732, 4928, 4929, 4930, 4931, 4736, 4940, 4941, 4942, 4943,
			4738, 4946, 4947, 4948, 4949, 4734, 4934, 4935, 4936, 4937, 22516);

	public CustomXericRaid getCustomXericRaid() {
		if (customXericRaid == null)
			customXericRaid = new CustomXericRaid();
		return customXericRaid;
	}

	public void setCustomXericRaid(CustomXericRaid raid) {
		this.customXericRaid = raid;
	}

	public TombsOfAmascut getCurrentToARaid() {
		return TombsOfAmascutManager.getRaid(this);
	}

	public void setMapHandler(MapHandler instance) {
		this.currentInstanceHandler = instance;
	}

	public MoonsOfPerilHandler getMoonsOfPerilHandler() {
		if (moonsOfPerilHandler == null)
			moonsOfPerilHandler = new MoonsOfPerilHandler();
		if (!moonsOfPerilHandler.initialized)
			moonsOfPerilHandler.init();
		return moonsOfPerilHandler;
	}

	public boolean cannonCheck() {
		Instant now = Instant.now();

		Instant lastClaimInstant = Instant.ofEpochSecond(player.lastCannonClaimInEpoch);

		long hoursSinceLastClaim = Duration.between(lastClaimInstant, now).toHours();
		boolean canClaim = hoursSinceLastClaim >= 1;

		if (canClaim || player.lastCannonClaimInEpoch <= 0)
			return true;

		return false;

	}

	public InstanceTokenInterface getInstanceTokenInterface() {
		if (instanceTokenInterface == null)
			instanceTokenInterface = new InstanceTokenInterface();
		return instanceTokenInterface;
	}

	public SecurityGuard getSecurityGuard() {
		if (securityGuard == null)
			securityGuard = new SecurityGuard();
		return securityGuard;
	}

	public CamelStatueInterface getCamelStatueInterface() {
		if (camelStatueInterface == null)
			camelStatueInterface = new CamelStatueInterface();
		return camelStatueInterface;
	}

	public void addToCollectionLog(Item... items) {
		if (hooks.handle(new Hook.OnAddCollectionLog(this, items))) {
			return;
		}
	}

	public boolean isJailed() {
		return this.jailed;
	}

	public MapHandler getMapHandler() {
		return currentInstanceHandler;
	}

	public int getCombatAchievementIndexByOrdinal(int ordinal) {
		for (int i = 0; i < combatAchievementsList.size(); i++) {
			if (combatAchievementsList.get(i).ordinal() == ordinal) {
				return i;
			}
		}
		return -1;
	}

	public boolean completedAchievementTier(CombatAchievement.Tier type) {
		int index = type.ordinal();
		return combatAchievementsClaimed >= index;
	}

	public void updateCombatAchievementPoints(int amount) {
		CombatAchievement.Tier currentTier = CombatAchievementSystem.getTier(combatAchievementPoints);
		CombatAchievement.Tier nextTier = CombatAchievementSystem.getTierByOrdinal(currentTier.ordinal() + 1);
		String tierName = nextTier.name();
		combatAchievementPoints += amount;
		if (combatAchievementPoints >= CombatAchievementSystem.getPointsForTier(nextTier)) {
			sendMessage("You have unlocked the " + tierName + " combat achievement tier.");
		}
	}

	public LootsViewer getLootsViewer() {
		if (lootsViewer == null)
			lootsViewer = new LootsViewer();
		return lootsViewer;
	}

	public DropViewer getDropViewer() {
		if (dropViewer == null)
			dropViewer = new DropViewer();
		return dropViewer;
	}

	public int getDifficultyChangeCount() {
		return difficultyChangeCount;
	}

	public void incrementDifficultyChanges() {
		if (difficultyChangeCount < DifficultyChanger.MAX_DIFFICULTY_CHANGES) {
			difficultyChangeCount++;
		}
	}

	public void resetDifficultyChanges() {
		difficultyChangeCount = 2;
	}

	public PlayerPerkHandler getPlayerPerkHandler() {
		if (playerPerkHandler == null)
			playerPerkHandler = new PlayerPerkHandler();
		playerPerkHandler.updateActivePerks(this);
		return playerPerkHandler;
	}

	public boolean wearingFullRaimentsOfTheEye() {
		return getEquipment().contains(26850) && getEquipment().contains(26852) && getEquipment().contains(26854)
				&& getEquipment().contains(26856);
	}

	public boolean wearingBarrows() {
		return getEquipment().get(Equipment.SLOT_WEAPON) != null
				&& barrowsIds.contains(getEquipment().get(Equipment.SLOT_WEAPON).getId())
				&& getEquipment().get(Equipment.SLOT_CHEST) != null
				&& barrowsIds.contains(getEquipment().get(Equipment.SLOT_CHEST).getId())
				&& getEquipment().get(Equipment.SLOT_LEGS) != null
				&& barrowsIds.contains(getEquipment().get(Equipment.SLOT_LEGS).getId())
				&& getEquipment().get(Equipment.SLOT_HAT) != null
				&& barrowsIds.contains(getEquipment().get(Equipment.SLOT_HAT).getId());
	}

	public float getRunePercentageMultiplication() {
		if (wearingFullRaimentsOfTheEye())
			return 2;
		float addition = 1f;
		if (getEquipment().contains(26850))
			addition += 0.25f;
		if (getEquipment().contains(26852))
			addition += 0.25f;
		if (getEquipment().contains(26854))
			addition += 0.25f;
		if (getEquipment().contains(26856))
			addition += 0.25f;
		return addition;
	}

	public NewcomerTasksInterface getNewcomerTaskInterface() {
		if (newcomerTaskInterface == null)
			newcomerTaskInterface = new NewcomerTasksInterface();
		return newcomerTaskInterface;
	}

	public DailyTasksInterface getDailyTaskInterface() {
		if (dailyTasksInterface == null)
			dailyTasksInterface = new DailyTasksInterface();
		return dailyTasksInterface;
	}

	public NewShopInterface getNewShopInterface() {
		if (newShopInterface == null)
			newShopInterface = new NewShopInterface();
		return newShopInterface;
	}

	public int calculateDropRate() {
		float dropAddition = (1 - getDifficulty().dropRateBonus) * 100;
		if (getEquipment().get(Equipment.SLOT_RING) != null && getEquipment().get(Equipment.SLOT_RING).getId() == 30590) {
			dropAddition += 2;
		}
		if (getPlayerPerkHandler().getActivePerkSets(this).contains(PerkSets.GOLD_DIGGER)) {
			int perkIndex = getPlayerPerkHandler().getActivePerkSetIndex(this, PerkSets.GOLD_DIGGER);
			GoldDigger c = (GoldDigger) getPlayerPerkHandler().getActivePerkSets(this).get(perkIndex).perkSet();
			dropAddition += c.getDropRateBoost();
		}
		if (getEquipment().get(Equipment.SLOT_RING) != null && getEquipment().get(Equipment.SLOT_RING).getId() == 30592) {
			dropAddition += 5;
		}
		if (getEquipment().get(Equipment.SLOT_RING) != null
				&& AttributeExtensions.hasAttribute(getEquipment().get(Equipment.SLOT_RING), AttributeTypes.TREASURE_HUNTER)) {
			int level = AttributeExtensions.getCharges(AttributeTypes.TREASURE_HUNTER,
					getEquipment().get(Equipment.SLOT_RING));
			dropAddition += level;
		}
		if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.DROP_RATE_BOOST))
			dropAddition += 5;
		switch (player.getSecondaryGroup()) {
			case SUPER_DONATOR -> dropAddition += 2;
			case ELITE_DONATOR -> dropAddition += 3;
			case NOBLE_DONATOR -> dropAddition += 5;
			case GOLD_DONATOR -> dropAddition += 6;
			case PLATINUM_DONATOR -> dropAddition += 7;
			case LEGENDARY_DONATOR -> dropAddition += 8;
			case SUPREME_DONATOR -> dropAddition += 10;
		}
		return Math.min(90, (int) (dropAddition + dropRate));
	}

	public UpgradeSystemInterface getUpgradeSystemInterface() {
		if (upgradeSystemInterface == null)
			upgradeSystemInterface = new UpgradeSystemInterface();
		return upgradeSystemInterface;
	}

	public DailyVoteInterface getDailyVote() {
		if (dailyVote == null)
			dailyVote = new DailyVoteInterface(this);
		return dailyVote;
	}

	public void teleportToSlayerTask(Player player) {
		int left = VarPlayerRepository.SLAYER_TASK_AMOUNT.get(player);
		if (left < 1) {
			player.sendMessage("You do not have a slayer task.");
			return;
		}
		if (player.slayerTaskPosition == null) {
			player.sendMessage("This task does not support this.");
			return;
		}
		if (player.taskInWilderness) {
			player.dialogue(new OptionsDialogue("Your task is in the wilderness, are you sure?",
					new Option("Yes, teleport me.", () -> {
						player.getMovement().teleport(player.slayerTaskPosition);
					}),
					new Option("Nevermind.")));
		} else {
			ModernTeleport.teleport(player, player.slayerTaskPosition);
		}
	}

	public GameModeInterface getGameModeInterface() {
		if (gameModeInterface == null)
			gameModeInterface = new GameModeInterface();
		return gameModeInterface;
	}

	public HashMap<Integer, Integer> getActivePerks() {
		return activePerks;
	}

	public HashMap<Integer, Integer> getOwnedPerks() {
		return ownedPerks;
	}

	public Difficulty getDifficulty() {
		return currentDifficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		currentDifficulty = difficulty;
	}

	public void UpdateBountyPoints(int amount) {
		bountyPoints += amount;
	}

	public void SetBountyPoints(int amount) {
		bountyPoints = amount;
	}

	public int GetBountyPoints() {
		return bountyPoints;
	}

	public int getPKPoints() {
		return pkPoints;
	}

	public int getTotalDonated() {
		return totalDonated;
	}

	public void updateTotalDonated(int amount) {
		Loggers.logBond(player.getUserId(), player.getName(), player.getIp(),
				"Total donated before change " + totalDonated);
		totalDonated += amount;
		Loggers.logBond(player.getUserId(), player.getName(), player.getIp(), "Total donated after change " + totalDonated);
	}

	public int getDonatorPoints() {
		return donatorPoints;
	}

	public void updateDonatorPoints(int amount) {
		donatorPoints += amount;
	}

	public int getVotePoints() {
		return votePoints;
	}

	public void updateVotePoints(int amount) {
		votePoints += amount;
	}

	public int getLoyaltyPoints() {
		return loyaltyPoints;
	}

	public void updateLoyaltyPoints(int amount) {
		loyaltyPoints += amount;
	}

	public int getDoubleExpRemaining() {
		return doubleExpRemaining;
	}

	public int getDoubleDropsRemaining() {
		return doubleDropsRemaining;
	}

	public int getDamageBoostRemaining() {
		return damageBoostRemaining;
	}

	public int getDamageReductionBoostRemaining() {
		return damageReductionBoostRemaining;
	}

	public int getBrewImmunityRemaining() {
		return brewImmunityRemaining;
	}

	public int getPrayerBoostBonusRemaining() {
		return prayerBoostBonusRemaining;
	}

	public int getDropRateBoostRemaining() {
		return dropRateBoostRemaining;
	}

	public int getPetBoostRemaining() {
		return petDropBonusTimeLeft;
	}

	public int getSecurityPin() {
		return securityPin;
	}

	public void updatePKPoints(int amount) {
		pkPoints += amount;
	}

	public boolean isRegularDonator() {
		return totalDonated >= 10 && totalDonated < 50;
	}

	public boolean isSuperDonator() {
		return totalDonated >= 50 && totalDonated < 100;
	}

	public boolean isEliteDonator() {
		return totalDonated >= 100 && totalDonated < 250;
	}

	public boolean isNobleDonator() {
		return totalDonated >= 250 && totalDonated < 500;
	}

	public boolean isGoldDonator() {
		return totalDonated >= 500 && totalDonated < 1000;
	}

	public boolean isPlatinumDonator() {
		return totalDonated >= 1000 && totalDonated < 2500;
	}

	public boolean isLegendaryDonator() {
		return totalDonated >= 2500 && totalDonated < 5000;
	}

	public boolean isSupremeDonator() {
		return totalDonated >= 5000 && totalDonated < 7500;
	}

	public int getCurrentItemUpgrades(int currentItem) {
		return currentItem;
	}

	public void SetCurrentMeleeWeaponUpgrade(int number) {
		currentMeleeWeaponUpgrade = number;
	}

	public int getCurrentMeleeWeaponUpgrades() {
		return currentMeleeWeaponUpgrade;
	}

	public int getCurrentMeleeHelmUpgrades() {
		return currentMeleeHelmUpgrade;
	}

	public void SetCurrentMeleeHelmUpgrade(int number) {
		currentMeleeHelmUpgrade = number;
	}

	public int getCurrentMeleeBodyUpgrades() {
		return currentMeleeBodyUpgrade;
	}

	public void SetCurrentMeleeBodyUpgrade(int number) {
		currentMeleeBodyUpgrade = number;
	}

	public int getCurrentMeleeLegsUpgrades() {
		return currentMeleeLegsUpgrade;
	}

	public void SetCurrentMeleeLegsUpgrade(int number) {
		currentMeleeLegsUpgrade = number;
	}

	public int getCurrentRangeWeaponUpgrades() {
		return currentRangeWeaponUpgrade;
	}

	public void SetCurrentRangeWeaponUpgrade(int number) {
		currentRangeWeaponUpgrade = number;
	}

	public int getCurrentRangeHelmUpgrades() {
		return currentRangeHelmUpgrade;
	}

	public void SetCurrentRangeHelmUpgrade(int number) {
		currentRangeHelmUpgrade = number;
	}

	public int getCurrentRangeBodyUpgrades() {
		return currentRangeBodyUpgrade;
	}

	public void SetCurrentRangeBodyUpgrade(int number) {
		currentRangeBodyUpgrade = number;
	}

	public int getCurrentRangeLegsUpgrades() {
		return currentRangeLegsUpgrade;
	}

	public void SetCurrentRangeLegsUpgrade(int number) {
		currentRangeLegsUpgrade = number;
	}

	public int getCurrentMagicHatUpgrades() {
		return currentMageHelmUpgrade;
	}

	public void SetCurrentMagicHatUpgrade(int number) {
		currentMageHelmUpgrade = number;
	}

	public int getCurrentMagicTopUpgrades() {
		return currentMageBodyUpgrade;
	}

	public void SetCurrentMagicTopUpgrade(int number) {
		currentMageBodyUpgrade = number;
	}

	public int getCurrentMagicBottomUpgrades() {
		return currentMageLegsUpgrade;
	}

	public void SetCurrentMagicBottomUpgrade(int number) {
		currentMageLegsUpgrade = number;
	}

	public BeginnerUpgrades getUpgradeItem(BeginnerUpgrades upgradeItem) {
		return upgradeItem;
	}

	public BeginnerUpgrades getMeleeWeapon() {
		return meleeWeapon;
	}

	public BeginnerUpgrades getRangeWeapon() {
		return rangeWeapon;
	}

	public BeginnerUpgrades getMeleeHelm() {
		return meleeHelm;
	}

	public BeginnerUpgrades getMeleeBody() {
		return meleeBody;
	}

	public BeginnerUpgrades getMeleeLegs() {
		return meleeLegs;
	}

	public BeginnerUpgrades getRangeHelm() {
		return rangeHelm;
	}

	public BeginnerUpgrades getRangeTop() {
		return rangeBody;
	}

	public BeginnerUpgrades getRangeLegs() {
		return rangeLegs;
	}

	public BeginnerUpgrades getMagicHat() {
		return mageHelm;
	}

	public BeginnerUpgrades getMagicTop() {
		return mageBody;
	}

	public BeginnerUpgrades getMagicBottom() {
		return mageLegs;
	}

	public Gamble getGamble() {
		return gamble;
	}

	public ScratchCardManager getScratchCard() {
		return scratchCardManager;
	}

	public SuperMysteryBox getBox() {
		return box;
	}

	public int getShopIdentifier() {
		return shopIdentifier;
	}

	public void setShopIdentifier(int shopIdentifier) {
		this.shopIdentifier = shopIdentifier;
	}

	public boolean hasInterfaceOpen(int id, ToplevelComponent type) {
		InterfaceHandler activeHandler = activeInterfaceHandlers[type.ordinal()];
		if (activeHandler == null)
			return false;

		return activeHandler.id == id;
	}

	public String getIp() {
		return ipAddress;
	}

	public int getIpInt() {
		return ipAddressInt;
	}

	public PlatformInfo getPlatformInfo() {
		return platformInfo;
	}

	public int getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		getAppearance().update();
	}

	public String getPassword() {
		return password;
	}

	public PlayerGroup getPrimaryGroup() {
		return primaryGroup;
	}

	public SecondaryGroup getSecondaryGroup() {
		return secondaryGroup;
	}

	public void setPrimaryGroup(PlayerGroup group) {
		this.primaryGroup = group;
	}

	public void setSecondaryGroups(List<Integer> groupIds) {
		for (Integer id : groupIds) {
			SecondaryGroup group = SecondaryGroup.GROUPS_BY_ID[id];
			if (group != null)
				groups[group.id] = true;
		}
		secondaryGroup = SecondaryGroup.GROUPS_BY_ID[groupIds.get(0)];
	}

	public boolean isGroup(PlayerGroup g) {
		if (g.id < this.groups.length && this.groups[g.id]) {
			return true;
		}
		return primaryGroup.equals(g);
	}

	public boolean isGroupIronman() {
		return getGameMode() == GameMode.GROUP_IRONMAN ||
				getGameMode() == GameMode.HARDCORE_GROUP_IRONMAN;
	}

	public boolean isGroup() {
		return isGroupIronman() || isHardcoreGroupIronman();
	}

	public boolean isHardcoreGroupIronman() {
		return getGameMode() == GameMode.HARDCORE_GROUP_IRONMAN;
	}

	public void join(PlayerGroup g) {
		groups[g.id] = true;
	}

	public void join(SecondaryGroup g) {
		groups[g.id] = true;
	}

	public void leave(PlayerGroup g) {
		groups[g.id] = false;
	}

	public boolean isSecondaryGroup(SecondaryGroup s) {
		return secondaryGroup.equals(s);
	}

	public boolean isModerator() {
		return isGroup(PlayerGroup.MODERATOR) || isGroup(PlayerGroup.HEAD_MODERATOR) || isOwner();
	}

	public boolean isHeadModerator() {
		return isGroup(PlayerGroup.HEAD_MODERATOR) || isGroup(PlayerGroup.ADMINISTRATOR) || isOwner();
	}

	public boolean isBetaTester() {
		return isGroup(PlayerGroup.BETA_TESTER) || isOwner() || isModerator();
	}

	public boolean isSupport() {
		return isGroup(PlayerGroup.SUPPORT) || isOwner() || isModerator();
	}

	public boolean isYoutuber() {
		return isGroup(PlayerGroup.YOUTUBER) || isOwner();
	}

	public boolean isDev() {
		return isGroup(PlayerGroup.OWNER) || isGroup(PlayerGroup.DEVELOPER);
	}

	public boolean isManager() {
		return isGroup(PlayerGroup.COMMUNITY_ADMIN) || isAdmin() || isDev() || isOwner();
	}

	public boolean isAdmin() {
		return isGroup(PlayerGroup.DEVELOPER) || isGroup(PlayerGroup.ADMINISTRATOR) || isOwner();
	}

	public boolean isOwner() {
		return isGroup(PlayerGroup.OWNER);
	}

	public boolean isCommunityAdmin() {
		return isGroup(PlayerGroup.COMMUNITY_ADMIN) || isAdmin();
	}

	public boolean isStaff() {
		return isGroup(PlayerGroup.MODERATOR) || isGroup(PlayerGroup.SUPPORT)
				|| isGroup(PlayerGroup.ADMINISTRATOR) || isGroup(PlayerGroup.OWNER) || isGroup(PlayerGroup.DEVELOPER)
				|| isGroup(PlayerGroup.COMMUNITY_ADMIN) || isGroup(PlayerGroup.ADMINISTRATOR);
	}

	public boolean isGroups(SecondaryGroup g) {
		return secondaryGroup.equals(g);
	}

	public boolean isDonator() {
		return isGroups(SecondaryGroup.DONATOR) || isGroups(SecondaryGroup.SUPER_DONATOR)
				|| isGroups(SecondaryGroup.ELITE_DONATOR)
				|| isGroups(SecondaryGroup.NOBLE_DONATOR) || isGroups(SecondaryGroup.GOLD_DONATOR)
				|| isGroups(SecondaryGroup.PLATINUM_DONATOR)
				|| isGroups(SecondaryGroup.LEGENDARY_DONATOR) || isGroups(SecondaryGroup.SUPREME_DONATOR);
	}

	public boolean isSapphire() {
		return isSecondaryGroup(SecondaryGroup.DONATOR) || isEmerald();
	}

	public boolean isEmerald() {
		return isSecondaryGroup(SecondaryGroup.SUPER_DONATOR) || isRuby();
	}

	public boolean isRuby() {
		return isSecondaryGroup(SecondaryGroup.ELITE_DONATOR) || isDiamond();
	}

	public boolean isDiamond() {
		return isSecondaryGroup(SecondaryGroup.NOBLE_DONATOR) || isDragonStone();
	}

	public boolean isDragonStone() {
		return isSecondaryGroup(SecondaryGroup.GOLD_DONATOR) || isOnyx();
	}

	public boolean isOnyx() {
		return isSecondaryGroup(SecondaryGroup.PLATINUM_DONATOR) || isZenyte();
	}

	public boolean isZenyte() {
		return isSecondaryGroup(SecondaryGroup.LEGENDARY_DONATOR);
	}

	public boolean isUber() {
		return isSecondaryGroup(SecondaryGroup.SUPREME_DONATOR);
	}

	public SecondaryGroup getHighestDonatorGroup() {
		if (isGroups(SecondaryGroup.SUPREME_DONATOR)) {
			return SecondaryGroup.SUPREME_DONATOR;
		}
		if (isGroups(SecondaryGroup.LEGENDARY_DONATOR)) {
			return SecondaryGroup.LEGENDARY_DONATOR;
		}
		if (isGroups(SecondaryGroup.PLATINUM_DONATOR)) {
			return SecondaryGroup.PLATINUM_DONATOR;
		}
		if (isGroups(SecondaryGroup.GOLD_DONATOR)) {
			return SecondaryGroup.GOLD_DONATOR;
		}
		if (isGroups(SecondaryGroup.NOBLE_DONATOR)) {
			return SecondaryGroup.NOBLE_DONATOR;
		}
		if (isGroups(SecondaryGroup.ELITE_DONATOR)) {
			return SecondaryGroup.ELITE_DONATOR;
		}
		if (isGroups(SecondaryGroup.SUPER_DONATOR)) {
			return SecondaryGroup.SUPER_DONATOR;
		}
		if (isGroups(SecondaryGroup.DONATOR)) {
			return SecondaryGroup.DONATOR;
		}
		return null;
	}

	public PlayerGroup getHighestAuthorityGroup() {
		if (isGroup(PlayerGroup.OWNER)) {
			return PlayerGroup.OWNER;
		}
		if (isGroup(PlayerGroup.DEVELOPER)) {
			return PlayerGroup.DEVELOPER;
		}
		if (isGroup(PlayerGroup.ADMINISTRATOR)) {
			return PlayerGroup.ADMINISTRATOR;
		}
		if (isGroup(PlayerGroup.MODERATOR)) {
			return PlayerGroup.MODERATOR;
		}
		if (isGroup(PlayerGroup.SUPPORT)) {
			return PlayerGroup.SUPPORT;
		}
		return null;
	}

	public boolean[] getGroups() {
		return groups;
	}

	// NOTE: for chat messages only game_mode rank is sent as it's supposed to be
	// in the protocol. Other crowns are handled through appearance's tags before
	// username.
	public String getNameWithRanks() {
		var tags = "";
		if (this.getSecondaryGroup() != SecondaryGroup.NONE) {
			tags += this.getSecondaryGroup().tag();
		}

		if (this.isGroup(PlayerGroup.YOUTUBER)) {
			tags += PlayerGroup.YOUTUBER.tag();
		}

		return tags + getName();
	}

	// NOTE: Messaging in private/clan chat sets one rank without any other custom
	// tagged username, meaning it has only one icon at most.
	// For normal player's its just their game GameMode
	// For staff and support it's a crown.
	public PlayerClientRank getMessagingRank() {
		if (hidePlayerIcon) {
			return PlayerClientRank.NORMAL;
		}

		if (isOwner()) {
			return PlayerClientRank.J_MOD;
		}

		if (isDev()) {
			return PlayerClientRank.J_MOD;
		}

		if (isAdmin()) {
			return PlayerClientRank.J_MOD;
		}
		if (isCommunityAdmin()) {
			return PlayerClientRank.J_MOD;
		}

		if (isModerator()) {
			return PlayerClientRank.P_MOD;
		}

		if (isSupport()) {
			return PlayerClientRank.IDK_9;
		}

		return getGameMode().clientRank();
	}

	public int getUnreadPMs() {
		return unreadPMs;
	}

	public void sendMessageFormat(String message, Object... args) {
		Checks.notEmpty(message, "Format");
		packetSender.sendMessage(String.format(message, args), null, 0);
	}

	public void sendMessage(String message) {
		if (ServerTest.TEST_MODE) {
			log.debug(player.getName() + ": " + message);
		}
		packetSender.sendMessage(message, null, 0);
	}

	/**
	 * Asserts a specified condition and optionally logs a debug message if the assertion fails. The assertion uses the
	 * `assert` keyword, and if the condition is false, the failure message is used. If an AssertionError is thrown, it is
	 * caught, and its message is passed to the debug method.
	 *
	 * @param condition the boolean condition to be asserted
	 * @param failMessage the message to include in the AssertionError if the condition is false
	 */
	public void assertWithDebug(boolean condition, String failMessage) {
		try {
			assert condition : failMessage;
		} catch (AssertionError e) {
			debug(e.getMessage());
		}
	}

	public void sendMessage(Color color, String message) {
		packetSender.sendMessage(color.wrap(message), null, 0);
	}

	public void sendURL(String message) {
		packetSender.sendMessage(message, World.type.getWebsiteUrl(), 4);
	}

	public void sendNotification(String message) {
		packetSender.sendBroadcast(message);
	}

	public void sendFilteredMessage(String message) {
		packetSender.sendMessage(message, null, 105);
	}

	public void openUrl(String title, String url) {
		dialogue(new MessageDialogue("Opening " + title
				+ "...<br>If this page fails to open please navigate your browser to:<br><col=880088>" + url));
		packetSender.sendUrl(url, false);
	}

	public void openUrl(String url) {
		packetSender.sendUrl(url, false);
	}

	public void sendScroll(String title, String... lines) {
		if (isVisibleInterface(119))
			closeInterface(ToplevelComponent.MAINMODAL);
		packetSender.sendString(119, 2, title);
		int childId = 4;
		packetSender.sendString(119, childId++, "");
		for (String s : lines) {
			if (childId >= 504)
				break;
			packetSender.sendString(119, childId++, s);
		}
		while (childId < 504)
			packetSender.sendString(119, childId++, "");
		packetSender.sendClientScript(917, "ii", -1, -1);
		openInterface(ToplevelComponent.MAINMODAL, 119);
		int scrollHeight = Math.max(215, lines.length * 20 + 20);
		ScrollbarClientScript.create()
				.interfaceId(119)
				.containerId(3)
				.scrollbarChildId(504)
				.childrenCount(scrollHeight)
				.withDarkGraphics()
				.build()
				.send(player);
		packetSender.sendClientScript(2523, "1i", 1, lines.length);
	}

	public void sendHintArrow(Entity target) {
		packetSender.sendHintIcon(target);
	}

	public void sendHintArray(Position tile) {
		packetSender.sendHintIcon(tile);
	}

	public void clearHintArrow() {
		packetSender.resetHintIcon(false);
	}

	public PacketSender getPacketSender() {
		return packetSender;
	}

	public boolean isOnline() {
		// yes.. i know
		if (ServerTest.TEST_MODE) {
			return true;
		}
		return online && rsprotSession != null && rsprotPlayerInfo != null;
	}

	public boolean hasDisplay() {
		return this.toplevel != null;
	}

	public void setToplevelType(ToplevelInterfaceType gameFrameId) {
		this.toplevel = gameFrameId;
	}

	public ToplevelInterfaceType getToplevelType() {
		return toplevel;
	}

	public void setVisibleInterface(int interfaceId, int parentId, int childId) {
		if (visibleInterfaceIds[parentId] == null)
			visibleInterfaceIds[parentId] = new Integer[InterfaceDef.COUNTS[parentId]];
		else if (visibleInterfaceIds[parentId] != null) {
			Integer id = visibleInterfaceIds[parentId][childId];
			if (id != null)
				visibleInterfaces[id] = false;
		}
		if (interfaceId >= visibleInterfaceIds.length) {
			return;
		}
		visibleInterfaces[interfaceId] = true;
		visibleInterfaceIds[parentId][childId] = interfaceId;
	}

	public void removeVisibleInterface(int parentId, int childId) {
		if (visibleInterfaceIds == null || visibleInterfaceIds[parentId] == null) {
			return;
		}

		if (parentId == -1 || childId == -1) {
			return;
		}

		if (visibleInterfaceIds[parentId][childId] != null) {
			visibleInterfaces[visibleInterfaceIds[parentId][childId]] = false;
		}

		visibleInterfaceIds[parentId][childId] = null;
	}

	public void moveVisibleInterface(int fromParentId, int fromChildId, int toParentId, int toChildId) {
		if (fromParentId >= visibleInterfaceIds.length) {
			return;
		}
		if (visibleInterfaceIds[fromParentId] == null) {
			return;
		}
		Integer interfaceId = visibleInterfaceIds[fromParentId][fromChildId];
		if (interfaceId == null)
			return;
		setVisibleInterface(interfaceId, toParentId, toChildId);
		visibleInterfaceIds[fromParentId][fromChildId] = null;
	}

	public boolean isVisibleInterface(int interfaceId) {
		if (toplevel == null) {
			getPacketSender().sendToplevel(ToplevelInterfaceType.FULLSCREEN_INTERFACE);
		}
		return interfaceId == toplevel.getInterfaceId() || visibleInterfaces[interfaceId];
	}

	public void openInterface(ToplevelComponent type, int interfaceId, InterfaceHandler handler) {
		// dupe prevention
		closeChatbox(type == ToplevelComponent.CHATBOX);
		InterfaceHandler activeHandler = activeInterfaceHandlers[type.ordinal()];
		if (activeHandler != null && activeHandler.closedAction != null) {
			activeHandler.closedAction.accept(this, interfaceId);
		}
		var realHandler = handler == null ? InterfaceHandler.EMPTY_HANDLER : handler;
		player.getPacketSender().sendInterface(interfaceId, type);
		if (realHandler.openAction != null) {
			realHandler.openAction.accept(this.player);
		}
		activeInterfaceHandlers[type.ordinal()] = realHandler;
	}

	public void openInterface(int interfaceId) {
		openInterface(ToplevelComponent.MAINMODAL, interfaceId, InterfaceHandler.HANDLERS[interfaceId]);
	}

	public void openInterface(ToplevelComponent component) {
		int interfaceId = component.getInterface(player);
		openInterface(component, interfaceId, InterfaceHandler.HANDLERS[interfaceId]);
	}

	public void openInterface(ToplevelComponent type, int interfaceId) {
		openInterface(type, interfaceId, InterfaceHandler.HANDLERS[interfaceId]);
	}

	public void setInterfaceUnderlay(int color, int transparency) {
		CS2Script.TOPLEVEL_MAINMODAL_OPEN.sendScript(this, color, transparency);
	}

	public void closeInterface(ToplevelComponent type) {
		InterfaceHandler activeHandler = activeInterfaceHandlers[type.ordinal()];

		if (activeHandler != null) {
			if (activeHandler.closedAction != null) {
				activeHandler.closedAction.accept(this, -1);
			}
		}

		if (type == ToplevelComponent.INVENTORY_TAB_AREA) {
			// when inventory closes, we just reset it to default inventory interface
			this.resetInventoryInterface();
		} else {
			type.close(this);
		}
		activeInterfaceHandlers[type.ordinal()] = null;
	}

	public void closeInterfaces() {
		closeInterfaces(false);
	}

	public void closeInterfaces(boolean skipDialogues) {
		for (var type : ToplevelComponent.VALUES) {
			if (type.getInterfaceType() != ClientInterfaceType.OVERLAY) {
				closeInterface(type);
			}
		}
		if (getGamble() != null) {
			if (getGamble().getGambleData() != null) {
				if (getGamble().getGambleData().inBlackJack) {
					getGamble().getGambleGameManager().refreshBlackJackInterface(this);
					return;
				}
			}
		}
		if (getScratchCard().isRunning()) {
			getScratchCard().resetInterface(this);
		}
		if (gamble != null)
			gamble.close();
		if (trade != null)
			trade.close();
		if (gauntlet != null) {
			if (gauntlet.inGauntlet)
				gauntlet.displayTimer(this);
		}
		if (duel != null)
			duel.close();
		closeChatbox(skipDialogues);
	}

	public void closeChatbox(boolean skipDialogues) {
		// if (!skipDialogues && dialogues != null)
		closeDialogue();
		if (consumerInt != null || consumerString != null) {
			consumerInt = null;
			consumerString = null;
			packetSender.sendClientScript(299, "ii", 1, 1);
		}
	}

	public void openDialogue(boolean closeInterfaces, Dialogue... dialogues) {
		// important to be true in almost every case to prevent dupes!
		if (closeInterfaces) {
			closeInterfaces(true);
		}

		this.dialogueStage = 1;
		this.dialogues = dialogues;
		this.optionsDialogue = null;
		this.skillDialogue = null;
		this.yesNoDialogue = null;
		(lastDialogue = dialogues[0]).open(this);
	}

	public void dialogue(Dialogue dialogues, Runnable onContinueClicked) {
		openDialogue(true, dialogues);
		// NOTE: setting dialogue here, because open dialogue closes previous dialogues
		// and removes the listener.
		this.onDialogueContinued = onContinueClicked;
	}

	public void dialogue(Dialogue... dialogues) {
		openDialogue(true, dialogues);
	}

	public void unsafeDialogue(Dialogue... dialogues) {
		openDialogue(false, dialogues);
	}

	public void continueDialogue() {
		onDialogueContinued();
		if (dialogues == null || dialogueStage >= dialogues.length) {
			closeDialogue();
		} else {
			(lastDialogue = dialogues[dialogueStage++]).open(this);
		}
	}

	public void closeDialogue() {
		dialogues = null;
		if (lastDialogue != null) {
			lastDialogue.closed(this);
			lastDialogue = null;
		}
		optionsDialogue = null;
		skillDialogue = null;
		yesNoDialogue = null;
		onDialogueContinued = null;
		removeDialogueInterface();
	}

	public void removeDialogueInterface() {
		player.getPacketSender().removeSubtoplevelInterface(ToplevelComponent.CHATBOX, Subcomponent.DIALOGUE);
		player.getPacketSender().removeSubtoplevelInterface(ToplevelComponent.CHATBOX, Subcomponent.YES_NO_DIALOGUE);
	}

	public boolean hasDialogue() {
		return dialogues != null || optionsDialogue != null || skillDialogue != null || yesNoDialogue != null;
	}

	/**
	 * Integer input (Whole numbers!!)
	 */
	public void inputInt(Consumer<Integer> consumer) {
		integerInput("", consumer);
	}

	public void inputInt(String message, Consumer<Integer> consumer) {
		integerInput(message, consumer);
	}

	public void integerInput(String message, Consumer<Integer> consumer) {
		consumerInt = consumer;
		retryIntConsumer = false;
		packetSender.sendClientScript(108, "s", message);
	}

	public void retryIntegerInput(String message) {
		integerInput(message, consumerInt);
		retryIntConsumer = true;
	}

	/**
	 * Item input
	 */
	public void itemSearch(String message, boolean allItems, Consumer<Integer> consumer) {
		consumerInt = consumer;
		retryIntConsumer = false;
		packetSender.sendClientScript(750, "s1g", message, (allItems ? 2 : 1), -1);
	}

	/**
	 * String input (Basically any group of characters!)
	 */

	public void stringInput(String message, Consumer<String> consumer) {
		consumerString = consumer;
		retryStringConsumer = false;
		packetSender.sendClientScript(110, "s", message);
	}

	public void retryStringInput(String message) {
		stringInput(message, consumerString);
		retryStringConsumer = true;
	}

	/**
	 * Name input (Basically only allows characters used in player names!)
	 */

	public void nameInput(String message, Consumer<String> consumer) {
		consumerString = consumer;
		retryStringConsumer = false;
		packetSender.sendClientScript(109, "s", message);
	}

	public void retryNameInput(String message) {
		nameInput(message, consumerString);
		retryStringConsumer = true;
	}

	public void setAction(int option, PlayerAction action) {
		if (action == null) {
			PlayerAction previousAction = actions[option - 1];
			if (previousAction == null)
				return;
			actions[option - 1] = null;
			packetSender.sendPlayerAction("null", false, option);
		} else {
			actions[option - 1] = action;
			packetSender.sendPlayerAction(action.name, action.top, option);
		}
	}

	public PlayerAction getAction(int option) {
		return actions[option - 1];
	}

	public void updateVarp(int id) {
		if (updatedVarps[id]) {
			return;
		}
		updatedVarps[id] = true;
		updatedVarpIds[updatedVarpCount++] = id;
	}

	public void sendVarps() {
		if (updatedVarpCount == 0) {
			return;
		}
		for (int i = 0; i < updatedVarpCount; i++) {
			int id = updatedVarpIds[i];
			int value = varps[id];
			updatedVarps[id] = false;
			packetSender.sendVarp(id, value);
		}
		updatedVarpCount = 0;
	}

	public void addRegion(Region region) {
		region.players.add(this);
		regions.add(region);
	}

	public void removeFromRegions() {
		regions.removeIf(region -> {
			region.players.remove(this);
			return true;
		});
	}

	public ArrayList<Region> getRegions() {
		return regions;
	}

	@Override
	public List<Player> computeLocalPlayers() {
		final List<Player> players = new ObjectArrayList<>();
		for (final var p : World.players()) {
			if (p == null || !p.isOnline()) {
				continue;
			}

			if (isLocal(p)) {
				players.add(p);
			}
		}
		return players;
	}

	public boolean isLocal(final Player other) {
		return this.getPosition().getDistance(other.getPosition()) <= 14;
	}

	public PlayerUpdater getUpdater() {
		return updater;
	}

	@Override
	public List<NPC> computeLocalNpcs() {
		return EntityHelperKt.searchZoneNpcs(
				this,
				npcUpdater.getMaxDistance())
				.stream()
				.filter(npc -> !npc.isHidden()
						&& !npc.getMovement().hasTeleportUpdate())
				.collect(Collectors.toList());
	}

	public PlayerNPCUpdater getNpcUpdater() {
		return npcUpdater;
	}

	@Override
	public PlayerMovement getMovement() {
		return movement;
	}

	@Override
	public UpdateMask[] getMasks() {
		return masks;
	}

	public Appearance getAppearance() {
		return appearance;
	}

	public ChatUpdate getChatUpdate() {
		return chatUpdate;
	}

	public boolean damageReductionActive() {
		return wildernessLevel <= 0 && damageReductionBoostTimer.remaining() > 0
				&& getDuel().stage < 4;
	}

	public boolean brewImmunityActive() {
		return wildernessLevel <= 0 && brewImmunityTimer.remaining() > 0
				&& getDuel().stage < 4;
	}

	public boolean damageBoostActive() {
		return wildernessLevel <= 0 && damageBoostTimer.remaining() > 0
				&& getDuel().stage < 4;
	}

	public boolean prayerBoostActive() {
		return wildernessLevel <= 0 && prayerBoostTimer.remaining() > 0
				&& getDuel().stage < 4;
	}

	public boolean revivalActive() {
		return wildernessLevel <= 0 && reviveActive
				&& getDuel().stage < 4;
	}

	public int getLastClaimedDailyLogin() {
		return lastClaimedDailyLogin;
	}

	public void setLastClaimedDailyLogin(int lastClaimedDailyLogin) {
		this.lastClaimedDailyLogin = lastClaimedDailyLogin;
	}

	public int getLoyalty() {
		return this.loyalty;
	}

	public void increaseLoyalty(int loyalty) {
		this.loyalty += loyalty;
	}

	public long[] getLastVoted() {
		return lastVoted;
	}

	public void setLastVoted(long time, int idx) {
		lastVoted[idx] = time;
	}

	public void setLastVotedAll(long time) {
		for (int i = 0; i < lastVoted.length; i++) {
			lastVoted[i] = time;
		}
	}

	public RunePouch getTournamentRunePouch() {
		return tournamentRunePouch;
	}

	public Inventory getInventory() {
		return temporaryInventory == null ? inventory : temporaryInventory;
	}

	public Equipment getEquipment() {
		return temporaryEquipment == null ? equipment : temporaryEquipment;
	}

	public Trade getTrade() {
		return trade;
	}

	public Duel getDuel() {
		return duel;
	}

	public Bank getBank() {
		return bank;
	}

	public BankPin getBankPin() {
		return bankPin;
	}

	public MineralBag getMineralBag() {
		return mineralBag;
	}

	public LootingBag getLootingBag() {
		return lootingBag;
	}

	public ScrollSack getScrollSack() {
		return materialBag;
	}

	public RunePouch getRunePouch() {
		return runePouch;
	}

	public SeedBox getSeedBox() {
		if (seedBox == null)
			seedBox = new SeedBox();
		return seedBox;
	}

	public SoulBearer getSoulBearer() {
		if (soulBearer == null)
			soulBearer = new SoulBearer();
		return soulBearer;
	}

	public TheatreRoom getTheatreRoom() {
		return theatreRoom;
	}

	public DeathStorage getDeathStorage() {
		return deathStorage;
	}

	public Farming getFarming() {
		return farming;
	}

	public StatList getStats() {
		return stats;
	}

	public PlayerPrayer getPrayer() {
		return prayer;
	}

	public PlayerCombat getCombat() {
		return combat;
	}

	public BountyHunter getBountyHunter() {
		return bountyHunter;
	}

	@Override
	public int setHp(int newHp) {
		return stats.get(StatType.Hitpoints).alter(newHp);
	}

	@Override
	public int setMaxHp(int newHp) {
		return combat == null ? 0 : stats.get(StatType.Hitpoints.ordinal()).set(newHp);
	}

	@Override
	public int getHp() {
		return stats.get(StatType.Hitpoints).currentLevel;
	}

	@Override
	public int getMaxHp() {
		return stats.get(StatType.Hitpoints).fixedLevel;
	}

	public void registerMapListener(MapListener listener) {
		activeMapListeners.add(listener);
	}

	public void collectResource(int harvestId, int amount) {
		collectResource(new Item(harvestId, amount));
	}

	public void collectResource(Item resource) {
		for (Item item : player.getEquipment().getItems()) {
			if (item != null && item.getDef() != null) {
				List<String> upgrades = AttributeExtensions.getEffectUpgrades(item);
				boolean hasEffect = upgrades != null;
				if (hasEffect) {
					for (String s : upgrades) {
						try {
							if (s.equalsIgnoreCase("empty"))
								continue;
							ItemEffect effect = ItemEffect.valueOf(s);
							effect.getUpgrade().collectResource(player, resource);
						} catch (Exception e) {
							log.error("Unknown upgrade { " + s + " } found!", e);
						}
					}
				}
			}
		}
	}

	public void init(LoginInfo info) {
		this.lastLoginTime = System.currentTimeMillis();
		this.ipAddress = info.ipAddress;
		this.platformInfo = info.platformInfo;
		this.ipAddressInt = info.ipAddressInt;
		this.userId = info.userId;
		this.name = info.name;
		if (this.uuid == null) {
			this.uuid = UUID.randomUUID().toString();
		}
		if (this.password == null || this.password.isEmpty()) {
			this.password = info.password;
		}
		this.tfa = info.tfaCode != 0;
		this.unreadPMs = info.unreadPMs;
		this.hwid = info.hwid;
		this.hwids.add(info.hwid);
		this.logoutProcessStage = Player.LogoutStage.NoLogout;
		this.wildernessLevel = -1;
		this.lastAlchPosition = null;
		this.guardSpawnCoolDown = new TickDelay();
		this.elementalConvergenceDelay = new TickDelay();
		this.soulReaverDelay = new TickDelay();
		this.olmSpecialDelay = new TickDelay();
		this.aoeSwipeDelay = new TickDelay();
		this.doubleTapDelay = new TickDelay();
		this.playerPerkInterface = null;
		this.playerPerkHandler = null;
		this.ownedPlayerPerks = new HashMap<>();
		this.activePerkSetsList = new ArrayList<>();
		this.ownedPerksList = new ArrayList<>();
		this.activePerksList = new ArrayList<>();
		this.dailyVote = null;
		this.currentSection = null;
		this.newcomerTaskInterface = null;
		this.dailyTasksInterface = null;
		this.newShopInterface = new NewShopInterface();
		this.raidPrivateStorageItems = new HashMap<>();
		this.instanceId = 0;
		this.temporossGame = null;
		this.gauntlet = null;
		this.gauntletBossTimer = new TickDelay();
		this.crystallineHunllefTimer = null;
		this.corruptedHunllefTimer = null;
		this.gambleId = 0;
		this.scratchCardManager = new ScratchCardManager();
		this.dropTableSearchResults = null;
		this.onDialogueContinued = null;
		this.targetOverlayTarget = null;
		this.targetOverlayResetTicks = 0;
		this.theatreRoom = null;
		this.inTob = false;
		this.supplyChestDamage = 0;
		this.supplyChestPoints = 0;
		this.theatreReward = new ArrayList<>();
		this.tobPurpleObtained = false;
		this.viewingTheatreSlot = 0;
		this.inTheatreParty = false;
		this.isCheckingDispenser = false;
		this.isOperatingPump = false;
		this.driveBeltBroken = false;
		this.currentBlastFurnaceOre = null;
		this.boneBuryDelay = new TickDelay();
		this.ashScatterDelay = new TickDelay();
		this.doubleDropBonus = new TickDelay();
		this.houseBuildPointX = 0;
		this.houseBuildPointY = 0;
		this.houseBuildPointZ = 0;
		this.houseViewerRooms = null;
		this.houseViewerRoom = null;
		this.specialRestoreTicks = 0;
		this.selectedKeybindConfig = null;
		this.selectedSettingMenu = -1;
		this.selectedSettingChild = -1;
		this.eatDelay = new TickDelay();
		this.karamDelay = new TickDelay();
		this.potDelay = new TickDelay();
		this.smithBar = null;
		this.yesDelay = new TickDelay();
		this.noDelay = new TickDelay();
		this.emoteDelay = new TickDelay();
		this.teleotherActive = null;
		this.examinePlayer = null;
		this.examineMonster = null;
		this.magicImbueEffect = new TickDelay();
		this.teleportsWizard = null;
		this.fairyRing = null;
		this.canoeStation = null;
		this.npcTarget = false;
		this.teleportsInterface = false;
		this.galvekEyeCooldown = new TickDelay();
		this.eneryTransferCooldown = new TickDelay();
		this.superiorPotionCooldown = new TickDelay();
		this.superiorHeartCooldown = new TickDelay();
		this.magicImbueHeartCooldown = new TickDelay();
		this.rangedImbueHeartCooldown = new TickDelay();
		this.strengthImbueHeartCooldown = new TickDelay();
		this.overloadImbueHeartCooldown = new TickDelay();
		this.magicTaintedWandCooldown = new TickDelay();
		this.overloadTaintedWandCooldown = new TickDelay();
		this.overloadHeartCooldown = new TickDelay();
		this.saltCooldown = new TickDelay();
		this.rangersHeartCooldown = new TickDelay();
		this.combatantHeartCooldown = new TickDelay();
		this.crystalSawCooldown = new TickDelay();
		this.inTutorial = false;
		this.inMonsterViewer = false;
		this.singlesPlus = false;
		this.pvpAttackZone = false;
		this.pvpCombatLevel = 0;
		this.acceptDelay = new TickDelay();
		this.totalStaked = 0;
		this.bloodMoneyStaked = 0;
		this.bloodyTokensStaked = 0;
		this.teleportSelectedCategory = -1;
		this.tpWildWarn = true;
		this.spellbookSwapOriginalBook = null;
		this.tokenEvent = new TickDelay();
		this.nextDefenderId = -1;
		this.wintertodtPoints = 0;
		this.traps = new ArrayList<>();
		this.godwarsAltarCooldown = new TickDelay();
		this.slayerTask = null;
		this.blackChinchompaBoost = new TickDelay();
		this.darkCrabBoost = new TickDelay();
		this.alchDelay = new TickDelay();
		this.superheatDelay = new TickDelay();
		this.optionScroll = null;
		this.activeKillLogList = null;
		this.zulrahTimer = null;
		this.solHereditTimer = null;
		this.yamaTimer = null;
		this.araxxorTimer = null;
		this.scurriusTimer = null;
		this.soloOlmTimer = null;
		this.moonsOfPerilTimer = null;
		this.toaRaidTimer = null;
		this.toaChaosLoot = new ArrayList<>();
		this.toaPowerLoot = new ArrayList<>();
		this.toaLifeLoot = new ArrayList<>();
		this.toaReward = new ArrayList<>();
		this.toaInvo = 0;
		this.toaPetRate = 1000;
		this.sotdDelay = new TickDelay();
		this.rockCakeDelay = new TickDelay();
		this.locatorOrbDelay = new TickDelay();
		this.petNPC = null;
		this.familiarNPC = null;
		this.callPet = false;
		this.hidePet = false;
		this.showPet = false;
		this.first3 = new TickDelay();
		this.elderChaosDruidTeleport = new TickDelay();
		this.tempUseRaidPrayers = false;
		this.viewingOrbLocation = null;
		this.usingTournamentOrb = false;
		this.usingTournamentOrbFromHome = false;
		this.cachedRunePouchTypes = 0;
		this.cachedRunePouchAmounts = 0;
		this.tournamentPouch = false;
		this.tournamentAugury = false;
		this.tournamentRigour = false;
		this.tournamentPreserve = false;
		this.bestiarySearchResults = null;
		this.bloodyFragments = 0;
		this.insideRaid = false;
		this.advertisingParty = false;
		this.advertisementStartTick = 0L;
		this.raidsParty = null;
		this.party = null;
		this.viewingParty = null;
		this.selectedCreditPackage = 0;
		this.selectedPaymentMethod = 0;
		this.title = null;
		this.seat = null;
		this.expBonus = new TickDelay();
		this.petDropBonus = new TickDelay();
		this.rareDropBonus = new TickDelay();
		this.recentlyEquipped = new TickDelay();
		this.nurseSpecialRefillCooldown = new TickDelay();
		this.lastPresetUsed = null;
		this.vestasSpearSpecial = new TickDelay();
		this.morrigansAxeSpecial = new TickDelay();
		this.supplyChestRestricted = false;
		this.bloodyKeyRestricted = false;
		this.dragonfireShieldSpecial = false;
		this.dragonfireShieldCooldown = new TickDelay();
		this.insideWildernessAgilityCourse = false;
		this.presetDelay = new TickDelay();
		this.selectedSkillLampSkill = null;
		this.riskProtectionExpirationDelay = new TickDelay();
		this.currentInstance = null;
		this.claimedBox = true;
		this.easterEgg = false;
		this.idleTicks = 0;
		this.isIdle = false;
		this.galvekTimer = null;
		this.snowballCooldown = new TickDelay();
		this.specTeleportDelay = new TickDelay();
		this.edgevilleStallCooldown = new TickDelay();
		this.botPreventionNPC = null;
		this.dismissBotPreventionNPC = false;
		this.botPreventionJailDelay = new TickDelay();
		this.botPreventionNpcShoutDelay = new TickDelay();
		this.thrall = null;
		this.overloadBoostActive = false;
		this.prayerEnhanceBoostActive = false;
		this.pestGame = null;
		this.pestActivityScore = 0;
		this.selectedWidgetId = 0;
		this.lmsSessionKills = 0;
		this.rigging = false;
		this.pkModeTutorialOp = 0;
		this.pyramidPlunderGame = null;
		this.activeStaticMapListeners = new HashSet<>();
		this.visibleInterfaceIds = new Integer[InterfaceDef.COUNTS.length][];
		this.visibleInterfaces = new boolean[InterfaceDef.COUNTS.length];
		this.activeInterfaceHandlers = new InterfaceHandler[ToplevelComponent.VALUES.length];
		this.temporaryInventory = null;
		this.temporaryEquipment = null;

		if (this.nameChange != null) {
			this.name = this.nameChange;
		}

		if (this.position == null) {
			this.position = new Position(3086, 3495, 0);
		} else {
			this.position = new Position(position);
		}

		updateFirstChunk();
		lastPosition = position.copy();

		packetSender = new PacketSender(this);

		updater = new PlayerUpdater(this);
		npcUpdater = new PlayerNPCUpdater(this);
		movement = new PlayerMovement(this);

		trade = new Trade(this);
		duel = new Duel(this);
		gamble = new Gamble(this);

		this.healthHud = new HealthHud(this);

		bank.init(this, bank.getBankCapacity(this), -1, 64207, 95, true);
		familiarStorage.init(this, 28, -1, 63786, 516, false);
		lootingBag.init(this, 28, -1, 63786, 516, false);
		materialBag.init(this, 28, -1, 63786, 516, false);
		inventory.init(this, 28, -1, 0, 93, false);
		equipment.init(this, 15, -1, 64208, 94, false);
		runePouch.init(this, 3, -1, -1, -1, false);
		DivinerunePouch.init(this, 4, -1, -1, -1, false);
		tournamentRunePouch.init(this, 3, -1, -1, -1, false);
		box.init(this, 24, -1, -1, 510, false);
		deathStorage.init(this, 100, -1, -1, 525, false);
		privateRaidStorage.init(player, 90, -1, -1, 583, false);
		raidRewards.init(player, 3, -1, -1, 581, false);
		bankPin.init(this);
		stats.init(this);
		prayer.init(this);
		farming.init(this);
		combat.init(this);
		bountyHunter.init(this);
		birdHouseHandler.init(player);
		upgradeManager.setPlayer(this);
		itemExchange.setPlayer(this);
		appearance.setPlayer(this);

		equipment.update(Equipment.SLOT_WEAPON);
		equipment.sendAll = true;
		inventory.sendAll = true;
		privateRaidStorage.sendAll = true;

		checkMulti();
		Tile.occupy(this);

		this.masks = new UpdateMask[] { // order same way as client reads
				modelTintUpdate = new ModelTintUpdate(),
				teleportModeUpdate = new TeleportModeUpdate(),
				chatUpdate = new ChatUpdate(),
				playerOpsUpdate = new PlayerOpsUpdate(),
				graphicsUpdate = new GraphicsUpdate(),
				appearance,
				forceMovementUpdate = new ForceMovementUpdate(),
				mapDirectionUpdate = new MapDirectionUpdate(),
				forceTextUpdate = new ForceTextUpdate(),
				animationUpdate = new AnimationUpdate(),
				movementModeUpdate = new MovementModeUpdate(),
				entityDirectionUpdate = new EntityDirectionUpdate(),
				hitsUpdate = new HitsUpdate()
		};

		this.setHidden(false);

		if (!this.isBetaTester() && World.isDev()) {
			this.join(PlayerGroup.BETA_TESTER);
		}

		VarPlayerRepository.load(player);
		this.initConstantVarps();

		if (hooks.handle(new Hook.OnInit(this))) {
			return;
		}
	}

	// default varps that are required and fixed (default values settled, but player
	// data might still contain old)
	private void initConstantVarps() {
		VarPlayerRepository.QUEST_POINTS.set(this, 300);
		VarPlayerRepository.CHAT_NAME_SET.set(this, 1);
		VarPlayerRepository.PLAYER_INFO_QUESTS_COMPLETE.set(player, 150);
		VarPlayerRepository.varpbit(11877, false).set(player, 150);
		VarPlayerRepository.varpbit(12933, false).set(player, 1);
		VarPlayerRepository.UNLOCK_BLOCK_TASK_SIX.set(player, 1);
		VarPlayerRepository.LOCK_CAMERA.set(player, 0);

		VarPlayerRepository.save(this);
	}

	public boolean petsCollected() {
		return this.petsCollected;
	}

	public void petsCollected(boolean value) {
		this.petsCollected = value;
	}

	public void start() {
		this.started = true;
		combat.start();
		movement.sendEnergy(-1);
		getCombat().resetTb();
		PresetCustom.check(this);
		bankPin.loggedIn();

		setAction(2, PlayerAction.FOLLOW);
		setAction(3, PlayerAction.TRADE);
		setInvisibleAction(4, PlayerAction.TRADE);

		sendMessage("Welcome to " + World.type.getWorldName() + ".");
		packetSender.sendDiscordPresence("Idle");

		World.sendLoginMessages(this);

		LoginListener.executeAll(this);

		if (pet != null)
			pet.spawn(this);

		Title.load(this);

		/*
		 * Daily
		 */
		if (lastLoginDate == null || LocalDate.parse(lastLoginDate).isBefore(LocalDate.now())) {
			dailyReset();
		}

		/*
		 * Donator Benefit: [1 Daily Lottery Ticket]
		 */
		if (lastLoginDate != null && LocalDate.parse(lastLoginDate).isBefore(LocalDate.now()) && isSapphire()) {
			sendMessage("You have received your daily lottery ticket. Thanks for supporting the server!");
			bank.add(1464, 1);
		}

		// For donators mems
		int daysOfMembership = VarPlayerRepository.MEMBERSHIP_DAYS.get(player);

		packetSender.sendVarp(1737, -2147483648);// dont think its right?
		CS2Script.ACCOUNT_INFO_UPDATE.sendScript(player, daysOfMembership > 0 ? 1 : 0, 0, 0);
		CS2Script.MOWT_SETMODEL.sendScript(player, 24772664, 44185, 0, 115, 2041, 1870, 0, 550, 808);
		CS2Script.MOWT_SETMODEL.sendScript(player, 24772665, 44754, 0, 6, 0, 108, 0, 650, 614);
		CS2Script.WELCOME_MEMBERS_GRAPHIC.sendScript(player, 2243, daysOfMembership > 0 ? "Blow me!" : "Subscribe now!");

		CS2Script.PVP_ICONS_LAYOUT.sendScript(player, 1);
		CS2Script.PVP_ICONS_COMLEVELRANGE.sendScript(player, player.getCombat().getLevel());

		CS2Script.HEALTH_REGEN_TIMER_UPDATE.sendScript(player, 819502);
		CS2Script.SPEC_REGEN_TIMER_UPDATE.sendScript(player, 819502);

		VarPlayerRepository.MEMBERSHIP_DAYS.set(player, player.storeAmountSpent);

		// Why dont these work
		CS2Script.SUMMARY_SIDEPANEL_TIMEPLAYED_TRANSMIT.sendScript(player, 46661634, 46661635, 519503);
		CS2Script.SUMMARY_SIDEPANEL_COMBAT_LEVEL_TRANSMIT.sendScript(player, 712 << 16 | 2, 712 << 16 | 3,
				player.getCombat().getLevel());
		CS2Script.ACCOUNT_INFO_UPDATE.sendScript(player, daysOfMembership > 0 ? 1 : 0, 0, 0);
		CS2Script.HEALTH_REGEN_TIMER_UPDATE.sendScript(player, 819502);
		CS2Script.SPEC_REGEN_TIMER_UPDATE.sendScript(player, 819502);

		CS2Script.STAT_BOOSTS_HUD_STATS_RESTORING_TIMER.sendScript(player, 0, 100, 100);
		CS2Script.STAT_BOOSTS_HUD_BOOSTS_DRAINING_TIMER.sendScript(player, 100, 100);
		CS2Script.HEALTH_REGEN_TIMER.sendScript(player, 0, 819502);
		CS2Script.SETUP_ENHANCED_CLIENT_FEATURES.sendScript(player, 819502, 1, player.getName(), "REGULAR");
		CS2Script.FOSSIL_POOL_UPDATE.sendScript(player, 0, 0, 0, 0, 0, 0);
		CS2Script.FOSSIL_POOL_PROGRESS_UPDATE.sendScript(player, 0);
		VarPlayerRepository.HAS_DISPLAY_NAME.set(player, 1);

		CS2Script.PLAYERMEMBER.sendScript(player, daysOfMembership > 0 ? 1 : 0);

		this.getCombat().updateWeapon(false, true);

		if (hooks.handle(new Hook.OnStart(this))) {
			return;
		}
	}

	/**
	 * Reset Actions
	 */
	public void resetActions(boolean closeInterfaces, boolean resetMovement, boolean resetCombat) {
		if (!isLocked())
			stopEvent(resetCombat);
		if (closeInterfaces)
			closeInterfaces();
		if (resetMovement) {
			movement.reset();
			movement.following = null;
			faceNone(false);
			TargetRoute.reset(this);
			if (seat != null)
				seat.stand(player);
		}
		if (resetCombat && combat.getTarget() != null) {
			combat.reset();
		}
	}

	public void attemptLogout() {
		if (combat.isDead() || combat.isDefending(17)) {
			sendMessage("You can't log out until 10 seconds after the end of combat.");
			return;
		}
		if (player.gauntlet != null && player.gauntlet.inGauntlet) {
			player.sendMessage("You can't logout in here.");
			return;
		}
		if (supplyChestRestricted) {
			sendMessage("The power of the supply chest prevents you from logging out!");
			return;
		}
		if (isLockedExclude(LockType.FULL_ALLOW_LOGOUT) && player.getAppearance().getNpcId() == -1) {
			sendMessage("You cannot logout now.");
			return;
		}
		if (logoutListener != null && logoutListener.attemptAction != null && !logoutListener.attemptAction.allow(this)) {
			sendMessage("Some force prevents you from logging out.");
			return;
		}
		this.requestLogout(LogoutStage.LogoutRequested);
		packetSender.sendDiscordPresence("In Lobby");
		packetSender.sendLogout();
	}

	public void forceLogout() {
		this.requestLogout(LogoutStage.LogoutAccepted);
		packetSender.sendDiscordPresence("In Lobby");
		packetSender.sendLogout();
	}

	public void requestLogout(LogoutStage value) {
		// forcing logout over other stages takes precendence
		if (this.logoutProcessStage == LogoutStage.LogoutAccepted) {
			return;
		}

		if (this.logoutProcessStage == LogoutStage.PostLogout) {
			return;
		}

		if (this.logoutProcessStage == LogoutStage.LoggedOut) {
			return;
		}
		this.logoutProcessStage = value;
	}

	public void checkLogout() {
		/**
		 * Validate channel & decode packets.
		 */
		if (this.logoutProcessStage == LogoutStage.NoLogout) {
			var count = this.rsprotSession == null ? 0 : this.rsprotSession.processIncomingPackets(this);
			if (count >= 255) {
				log.warn(name + " has suspicious packet count!");
			}

			if (count > 0) {
				this.lastPacketTime = System.currentTimeMillis();
			}

			if (this.lastPacketTime == 0) {
				this.lastPacketTime = System.currentTimeMillis();
			}

			if (this.lastPacketTimeElapsed() >= TimeUnit.HOURS.toMillis(6)) {
				this.requestLogout(LogoutStage.LogoutRequested);
			}

			// This player hasn't tried to log out, we good.
			if (this.logoutProcessStage == LogoutStage.NoLogout) {
				return;
			}
		}

		// Attempt to logout if a logout is required.
		if (this.logoutProcessStage == LogoutStage.LogoutRequested) {
			if (xLogDelay == null) {
				// Only apply combat timer if in combat
				if (combat.isDead() || combat.isDefending(17)) {
					xLogDelay = new TickDelay();
					// after 60 seconds of xlogging on rs, no matter if you're in combat you dc.
					xLogDelay.delay(100);
					return;
				} else {
					// after disconnection xlog delay for 5 seconds
					xLogDelay = new TickDelay();
					xLogDelay.delay(5);
				}
			} else if (xLogDelay.isDelayed()) {
				if (combat.isDead() || combat.isDefending(17)) {
					// This player is in combat, wait...
					return;
				}
			}

			if (isLockedExclude(LockType.FULL_ALLOW_LOGOUT)) {
				// This player is locked, we must wait!
				return;
			}
			this.logoutProcessStage = LogoutStage.LogoutAccepted;
		}

		// nothing blocks the logout now, finish the logic and queue the player save
		if (this.logoutProcessStage == LogoutStage.LogoutAccepted) {
			finish();
			this.deallocateRSProt();
			PlayerDatabase.insertQueue(this);
			this.logoutProcessStage = LogoutStage.PostLogout;
		}

		// save got queued, logic finalized, now waiting for the save to commence
		// and after that removing the player from world and caches
		if (this.logoutProcessStage == LogoutStage.PostLogout) {
			// Block logout until file gets saved, the queueing happens on finish
			if (PlayerDatabase.db().hasPendingSave(this.uuid())) {
				return;
			}
			PlayerDatabase.remove(player);
			World.removePlayer(this);
			this.logoutProcessStage = LogoutStage.LoggedOut;
			if (hooks.handle(new Hook.OnRemoved(this))) {
				log.error("Tried to break hook on finish");
			}
		}
	}

	public float pointMultiplier() {
		float multiplier = 1.0f;
		if (isRegularDonator())
			multiplier = 1.02f;
		else if (isSuperDonator())
			multiplier = 1.04f;
		else if (isEliteDonator())
			multiplier = 1.06f;
		else if (isPlatinumDonator())
			multiplier = 1.08f;
		else if (isLegendaryDonator())
			multiplier = 1.10f;
		return multiplier;
	}

	public void process() {
		if (!this.started) {
			return;
		}

		// HOTFIX: deadlock prevention
		if (this.isLocked() && !player.inTutorial && this.lockTimeoutTicks-- <= 0) {
			this.unlock();
		}

		this.updateTimers();

		if (player.soulStacks > 0) {
			if (!combat.isDefending(20)) {
				if (lastSoulStackRemoval++ >= 20) {
					lastSoulStackRemoval = 0;
					player.soulStacks--;
					player.hit(new Hit(HitType.HEAL).fixedDamage(8));
				}
			}
		}
		if (VarPlayerRepository.SUNLIGHT_SPEAR_STACKS.get(player) > 0) {
			if (!combat.isDefending(20)) {
				if (lastSunlightStackRemoval++ >= 20) {
					lastSunlightStackRemoval = 0;
					VarPlayerRepository.SUNLIGHT_SPEAR_STACKS.increment(player, -1);
				}
			}
		}

		if (botWarnings > 0 && System.currentTimeMillis() - lastActionTime > 24 * 60 * 60 * 1000) {
			// Decay warnings after 24h
			botWarnings = Math.max(0, botWarnings - 1);
		}

		if (party != null && customXericRaid != null) {
			customXericRaid.sendTotalPoints();
		}

		if (wildernessLevel > 0) {
			for (StatType stat : combatStats) {
				if (getStats().get(stat).currentLevel > 118) {
					getStats().get(stat).alter(118);
				}
			}
		}

		processEvent();
		packetSender.sendPing();

		if (temporaryEquipment != null) {
			temporaryEquipment.sendUpdates();
		}

		if (temporaryInventory != null) {
			temporaryInventory.sendUpdates();
		}

		inventory.sendUpdates();
		equipment.sendUpdates();
		bank.sendUpdates();
		lootingBag.sendUpdates();
		materialBag.sendUpdates();
		familiarStorage.sendUpdates();
		runePouch.sendUpdates();

		DivinerunePouch.sendUpdates();
		tournamentRunePouch.sendUpdates();
		box.sendUpdates();
		combat.preAttack();
		TargetRoute.beforeMovement(this);
		movement.process();
		TargetRoute.afterMovement(this);

		Region region;
		if (movement.hasMoved() && lastRegion != (region = getPosition().getRegion())) {
			lastRegion = region;
		}

		MapListener.process(this);

		combat.attack();

		for (int i = 0; i < GambleManager.currentId; i++) {
			if (GambleGameHandler.instance.get(i) != null) {
				GambleGameHandler.instance.get(i).process();
			}
		}

		if (gauntlet != null) {
			gauntlet.sendUpdates();
		}
		prayer.process();
		stats.process();

		tick();

		processHits();
	}

	public void claimDonation(Player player, int amount) {
		// player.donationDealPoints += amount;
	}

	/**
	 * Bm
	 */
	public void rewardBm(Player target, int amount) {
		String format = String.format(
				"BloodMoneyKill:[Player:[%s] Position:%s IPAddress:[%s] Target:[%s] IPAddress:[%s] Amount:[%d]]",
				player.getName(), player.getPosition(), player.getIp(), target.getName(), target.getIp(), amount);
		ServerWrapper.log(format);
		if (inventory.add(BLOOD_MONEY, amount) > 0)
			sendFilteredMessage("You received <col=6f0000>" + NumberUtils.formatNumber(amount)
					+ "</col> blood money for killing: " + target.getName() + ".");
		else
			new GroundItem(BLOOD_MONEY, amount).owner(this).position(target.getPosition()).spawn();
	}

	public void rewardBm(NPC target, int amount) {
		String format = String.format("BloodMoneyKillNPC:[Player:[%s] Position:%s IPAddress:[%s] Target:[%s] Amount:[%d]]",
				player.getName(), player.getPosition(), player.getIp(), target.getDef().name, amount);
		ServerWrapper.log(format);
		if (inventory.add(BLOOD_MONEY, amount) > 0)
			sendFilteredMessage("You received <col=6f0000>" + NumberUtils.formatNumber(amount)
					+ "</col> blood money for killing npc: " + target.getDef().name);
		else
			new GroundItem(BLOOD_MONEY, amount).owner(this).position(target.getPosition()).spawn();
	}

	@Override
	public boolean isPoisonImmune() {
		return super.isPoisonImmune() ||
				player.getEquipment().hasId(SerpentineHelm.SERPENTINE.getChargedId()) ||
				player.getEquipment().hasId(SerpentineHelm.MAGMA.getChargedId()) ||
				player.getEquipment().hasId(SerpentineHelm.TANZANITE.getChargedId());
	}

	@Override
	public boolean isVenomImmune() {
		return super.isVenomImmune() ||
				player.getEquipment().hasId(SerpentineHelm.SERPENTINE.getChargedId()) ||
				player.getEquipment().hasId(SerpentineHelm.MAGMA.getChargedId()) ||
				player.getEquipment().hasId(SerpentineHelm.TANZANITE.getChargedId());
	}

	public GameMode getGameMode() {
		return GameMode.values()[VarPlayerRepository.IRONMAN_MODE.get(this)];
	}

	public int getTitleEnumId() {
		return 0;
	}

	public House getCurrentHouse() {
		return getPosition().getRegion().getHouse();
	}

	public Room getCurrentRoom() {
		if (getCurrentHouse() == null)
			return null;
		return getCurrentHouse().getCurrentRoom(this);
	}

	public boolean isInOwnHouse() {
		return house != null && getCurrentHouse() == house;
	}

	public void dailyReset() {
		lastLoginDate = LocalDate.now().toString();
		DailyResetListener.executeAll(this);
	}

	public boolean showHitAsExperience() {
		return showHitAsExperience;
	}

	public ItemContainer getPrivateRaidStorage() {
		return privateRaidStorage;
	}

	public ItemContainer getRaidRewards() {
		return raidRewards;
	}

	@Override
	public boolean isPlayer() {
		return true;
	}

	public long getLoginTime() {
		return loginTime;
	}

	public void onLoginAccepted(Session<Player> session, NetworkService<Player> service, LoginInfo info) {
		this.lastSessionId = info.sessionId;
		this.lastLoginKeys = info.keys;
		this.rsprotSession = session;
		this.setDisconnectionHook(session);

		final int index = this.getIndex();
		this.rsprotPlayerInfo = service.getPlayerInfoProtocol().alloc(index, OldSchoolClientType.DESKTOP);
		this.rsprotNPCInfo = service.getNpcInfoProtocol().alloc(index, OldSchoolClientType.DESKTOP);
		this.online = true;
	}

	public void onReconnectAccepted(Session<Player> session, int[] newKeys) {
		if (this.rsprotSession == null) {
			throw new IllegalStateException("rsprot session null on reconnect: " + this.getName());
		}
		this.lastLoginKeys = newKeys;
		this.lastPacketTime = System.currentTimeMillis();
		this.rsprotSession.clear();
		var pInfo = player.rsprotPlayerInfo;
		var nInfo = player.rsprotNPCInfo;

		this.bank.sendAll = true;
		this.inventory.sendAll = true;

		this.rsprotSession = session;
		this.setDisconnectionHook(session);
	}

	private void setDisconnectionHook(Session<Player> session) {
		session.setDisconnectionHook(() -> {
			// case when session changes on reconnect but still fires disconnection
			if (this.rsprotSession != session) {
				return;
			}
			this.requestLogout(LogoutStage.LogoutRequested);
		});
	}

	public void deallocateRSProt() {
		try {
			RSProtService.removePlayer(this);
			this.rsprotSession = null;
			this.rsprotPlayerInfo = null;
			this.rsprotNPCInfo = null;
		} catch (Throwable e) {
			log.error("Unable to deallocate rsprot properly.", e);
		}
	}

	public PlayerAvatar avatar() {
		return this.rsprotPlayerInfo.getAvatar();
	}

	public PlayerAvatarExtendedInfo avatarExtended() {
		return this.avatar().getExtendedInfo();
	}

	public void onDialogueContinued() {
		if (onDialogueContinued != null) {
			onDialogueContinued.run();
		}
		onDialogueContinued = null;
	}

	public PlayerPerkInterface getPerkInterface() {
		if (playerPerkInterface == null)
			playerPerkInterface = new PlayerPerkInterface();
		return playerPerkInterface;
	}

	public void StartTeleport(int x, int y, int z) {
		if (gauntlet != null) {
			if (gauntlet.inGauntlet) {
				sendMessage("You can't teleport from Gauntlet.");
				return;
			}
		}
		startEvent(p -> {
			lock();
			animate(714);
			graphics(111);
			p.delay(3);
			getMovement().teleport(x, y, z);
			animate(-1);
			unlock();
		});
	}

	public void StartTeleport(Position pos) {
		if (gauntlet != null) {
			if (gauntlet.inGauntlet) {
				sendMessage("You can't teleport from Gauntlet.");
				return;
			}
		}
		startEvent(p -> {
			lock();
			animate(714);
			graphics(111);
			p.delay(3);
			getMovement().teleport(pos);
			animate(-1);
			unlock();
		});
	}

	public int getClueCount(String name) {
		int count = 0;
		if (name.equalsIgnoreCase("master clue"))
			count = masterClueCount;
		else if (name.equalsIgnoreCase("elite clue"))
			count = eliteClueCount;
		else if (name.equalsIgnoreCase("hard clue"))
			count = hardClueCount;
		else if (name.equalsIgnoreCase("medium clue"))
			count = medClueCount;
		else if (name.equalsIgnoreCase("easy clue"))
			count = easyClueCount;

		return count;
	}

	public int getSupplyChestDamage() {
		return supplyChestDamage;
	}

	public CombatAchievementInterface getCombatAchievementInterface() {
		if (combatAchievementInterface == null)
			combatAchievementInterface = new CombatAchievementInterface();
		return combatAchievementInterface;
	}

	public ToAPartyInterface getToaPartyInterface() {
		if (toaPartyInterface == null)
			toaPartyInterface = new ToAPartyInterface();
		return toaPartyInterface;
	}

	public ToaMembersInterface getMembersInterface() {
		if (membersInterface == null)
			membersInterface = new ToaMembersInterface();
		return membersInterface;
	}

	public ToaPartyViewInterface getToaPartyViewInterface() {
		if (toaPartyViewInterface == null)
			toaPartyViewInterface = new ToaPartyViewInterface();
		return toaPartyViewInterface;
	}

	public ToaInvocationsInterface getToaInvocationsInterface() {
		if (toaInvocationsInterface == null)
			toaInvocationsInterface = new ToaInvocationsInterface();
		return toaInvocationsInterface;
	}

	public ToaApplicantsInterface getToaApplicantsInterface() {
		if (toaApplicantsInterface == null)
			toaApplicantsInterface = new ToaApplicantsInterface();
		return toaApplicantsInterface;
	}

	public ToASuppliesInterface getToASuppliesInterface() {
		if (toASuppliesInterface == null)
			toASuppliesInterface = new ToASuppliesInterface();
		return toASuppliesInterface;
	}

	public ItemBreakInterface getItemBreakInterface() {
		if (itemBreakInterface == null)
			itemBreakInterface = new ItemBreakInterface();
		return itemBreakInterface;
	}

	public ItemUpgradeInterface getItemUpgradeInterface() {
		if (itemUpgradeInterface == null)
			itemUpgradeInterface = new ItemUpgradeInterface();
		return itemUpgradeInterface;
	}

	public GearPresetInterface getGearPresetInterface() {
		if (gearPresetInterface == null)
			gearPresetInterface = new GearPresetInterface();
		return gearPresetInterface;
	}

	public boolean admin() {
		return isGroup(PlayerGroup.ADMINISTRATOR);
	}

	public boolean dev() {
		return isGroup(PlayerGroup.DEVELOPER);
	}

	public boolean isServerowner() {
		return isOwner();
	}

	// Shortcuts
	public void sendClientScript(int id) {
		this.sendClientScript(id, new Object[0]);
	}

	public void sendClientScript(int id, Object... params) {
		this.getPacketSender().sendClientScript(id, params);
	}

	public int varbit(int id) {
		return VarPlayerRepository.varpbit(id, true).get(this);
	}

	public int varp(int id) {
		return VarPlayerRepository.varp(id, true).get(this);
	}

	public void varpSend(int id, int value) {
		VarPlayerRepository.varp(id, true).set(this, value);
	}

	public void varbitSend(int id, int value) {
		VarPlayerRepository.varpbit(id, true).set(this, value);
	}

	public void varbitFlip(int id) {
		var value = varbit(id);
		VarPlayerRepository.varpbit(id, true).set(this, ~value);
	}

	public void accessMasks(int interfaceId, int componentId, int min, int max, AccessMask... masks) {
		var mask = AccessMasks.combine(masks);
		this.getPacketSender().sendIfEvents(false, interfaceId, componentId, min, max, mask);
	}

	public void debug(String message) {
		if (!this.debug) {
			return;
		}
		player.sendFilteredMessage(message);
	}

	@Override
	public String captureState() {
		return String.format("%s pos=%s pid=%s hp=%s, tasks=%s, hits=%s, target=%s inv=%s wornItems=%s",
				getName(),
				getPosition(),
				getIndex(),
				this.getHp(),
				backgroundEvents == null ? 0 : backgroundEvents.size(),
				queuedHits.size(),
				combat == null ? "none" : combat.getTarget(),
				Arrays.toString(Arrays.stream(getInventory().getItems())
						.map(e -> e == null ? "" : String.format("%sx %s", e.getAmount(), e.getDef().name)).toList().toArray()),
				Arrays.toString(Arrays.stream(getEquipment().getItems())
						.map(e -> e == null ? "" : String.format("%sx %s", e.getAmount(), e.getDef().name)).toList().toArray()));
	}

	public String uuid() {
		return this.name.toLowerCase().trim();
	}

	public String uuidReal() {
		return this.uuid;
	}

	private void resetInventoryInterface() {
		this.openInterface(ToplevelComponent.INVENTORY_TAB_AREA, Interface.INVENTORY);
		int mask = InterfaceEventMask.getMask(
				InterfaceEventMask.ClickOp2,
				InterfaceEventMask.ClickOp3,
				InterfaceEventMask.ClickOp4,
				InterfaceEventMask.ClickOp6,
				InterfaceEventMask.ClickOp7,
				InterfaceEventMask.ClickOp10,
				InterfaceEventMask.UseOnGroundItem,
				InterfaceEventMask.UseOnNpc,
				InterfaceEventMask.UseOnObject,
				InterfaceEventMask.UseOnPlayer,
				InterfaceEventMask.UseOnInventory,
				InterfaceEventMask.UseOnComponent,
				InterfaceEventMask.DragDepth1,
				InterfaceEventMask.DragTargetable,
				InterfaceEventMask.ComponentTargetable);
		this.getPacketSender().sendIfEvents(149, 0, 0, 27, mask);
	}

	private void setInvisibleAction(int option, PlayerAction action) {
		actions[option - 1] = action;
	}

	private void finish() {
		try {
			MapListener.onLogout(player);
			this.finishMisc();
			this.finishDynamicMap();
			this.finishPet();
			this.finishCox();
			this.finishTob();
			this.finishToa();
			this.finishTrade();
			this.finishEvents();
			this.finishGroupBank();
			this.finishCannon();
			this.finishVars();
			this.finishHunter();
			this.finishBountyHunter();
			this.finishLogoutListeners();
			this.finishRegions();
			this.finishInterfaces();
			this.finishMovement();
			this.finishBankPin();
			this.finishInventories();
			this.prayer.deactivateAll();
			DatabaseFile.service.submit(this::finishDatabase);
			VarPlayerRepository.TELE_BLOCK.set(this, 0);
		} catch (Exception e) {
			log.error("Error finishing up player: " + getName(), e);
		}

		if (hooks.handle(new Hook.OnFinish(this))) {
			log.error("Tried to break hook on finish");
		}
	}

	private void finishInventories() {
		this.temporaryInventory = null;
		this.temporaryEquipment = null;
	}

	private void finishMisc() {
		this.wildernessLevel = -1;
		this.online = false;
		this.started = false;
		this.teleportListener = null;
		this.activatePrayerListener = null;
		this.allowPrayerListener = null;
		this.currentInstanceHandler = null;
		this.xLogDelay = null;
		this.toaSupplies.clear();
		this.inGamble = false;
		this.inCox = false;
		this.godmode = false;
		this.gauntlet = null;
		this.randomEventPrevent = 0;
		this.unlock();
	}

	private void finishInterfaces() {
		this.closeInterfaces();
	}

	private void finishMovement() {
		this.movement.finishTeleport(this.position);
		this.movement.reset();
		this.movement.following = null;
		this.faceNone(false);
		TargetRoute.reset(this);
		if (this.seat != null) {
			this.seat.stand(this);
		}
		this.combat.reset();
	}

	private void finishDatabase() {
		Hiscores.postHiscores(this);
		Loggers.logPlayer(this);
		Loggers.updateItems(this);
		Loggers.removeOnlinePlayer(userId, World.id);
	}

	private void finishLogoutListeners() {
		if (this.logoutListener != null && this.logoutListener.logoutAction != null) {
			this.logoutListener.logoutAction.logout(this);
			this.logoutListener = null;
		}
	}

	private void finishRegions() {
		this.removeFromRegions();
	}

	private void finishBountyHunter() {
		this.bountyHunter.loggedOut();
	}

	private void finishBankPin() {
		this.bankPin.loggedOut();
	}

	private void finishHunter() {
		Hunter.collapseAll(this);
	}

	private void finishVars() {
		VarPlayerRepository.save(player);
	}

	private void finishCannon() {
		World.checkCannon(this);
	}

	private void finishGroupBank() {
		// TODO(polish)
		// getGroupBank().save(this);
	}

	private void finishTrade() {
		if (this.trade != null) {
			this.trade.close();
		}
	}

	private void finishToa() {
		if (getCurrentToARaid() != null) {
			getCurrentToARaid().currentParty.disbandParty(this);
			for (int i = TombsOfAmascutManager.getActiveRaidParties().size() - 1; i >= 0; i--) {
				for (int user = TombsOfAmascutManager.getActiveRaidParties().get(i).getMembers().size()
						- 1; user >= 0; user--) {
					if (TombsOfAmascutManager.getActiveRaidParties().get(i).getMembers().get(user)
							.equalsIgnoreCase(this.getName())) {
						TombsOfAmascutManager.getActiveRaidParties().get(i).getMembers().remove(user);
					}
				}
			}
		}
	}

	private void finishTob() {
		if (this.currentParty != null) {
			TheatrePartyManager.instance().getPartyForPlayer(getName()).ifPresent(party -> {
				if (party.isLeader(this)) {
					TheatrePartyManager.instance().deregister(party);
				} else {
					party.leave(getName(), false);
				}
			});
			this.currentParty = null;
		}
		this.inTob = false;
		this.insideRaid = false;
	}

	private void finishCox() {
		if (getCustomXericRaid().currentParty != null) {
			getCustomXericRaid().exited(this, true);
		}
		this.inCox = false;
		this.insideRaid = false;
	}

	private void finishPet() {
		if (pet != null) { // unregister our pet
			var idx = PET_NPC_INDEX.get(this);
			if (idx > -1) {
				var pet = World.getNpc(idx);
				if (pet != null && pet.ownerId == getUserId()) {
					pet.remove();
				}
			}
		}
	}

	private void finishDynamicMap() {
		if (currentDynamicMap != null || inDynamicMap) {
			String playerName = getName().toLowerCase();
			InstanceHandler instance = InstanceManager.getInstances().get(playerName);
			if (instance != null) {
				instance.destroy();
			} else {
				getMovement().teleport(World.HOME);
			}
			currentDynamicMap = null;
			inDynamicMap = false;
		}
	}

	// NOTE: shitcode, timers use transient field for current server boot context
	// to handle clock stuff, and persistent fields to hold remaining
	// each timer should be 'delayed' on player login
	private void updateTimers() {
		player.rareDropBonusTimeLeft = player.rareDropBonus.remaining();
		player.expBonusTimeLeft = player.expBonus.remaining();
		player.first3TimeLeft = player.first3.remaining();
		player.petDropBonusTimeLeft = player.petDropBonus.remaining();
		player.blackChinchompaBoostTimeLeft = player.blackChinchompaBoost.remaining();
		player.darkCrabBoostTimeLeft = player.darkCrabBoost.remaining();
		player.doubleDropsRemaining = player.doubleDropBonus.remaining();
		player.petDropBonusTimeLeft = player.petDropBonus.remaining();
		player.doubleExpRemaining = player.doubleExpTimer.remaining();
		player.damageReductionBoostRemaining = player.damageReductionBoostTimer.remaining();
		player.damageBoostRemaining = player.damageBoostTimer.remaining();
		player.prayerBoostBonusRemaining = player.prayerBoostTimer.remaining();
		player.brewImmunityRemaining = player.brewImmunityTimer.remaining();
		player.dropRateBoostRemaining = player.dropRateBoostTimer.remaining();
	}

	/**
	 * Tick
	 */

	private void tick() {
		playTime++;

		int specialRestoreMaxTick = 50;
		if (player.getEquipment().get(Equipment.SLOT_RING) != null
				&& player.getEquipment().get(Equipment.SLOT_RING).getId() == 25975)
			specialRestoreMaxTick = 25;
		if (++specialRestoreTicks >= specialRestoreMaxTick) {
			specialRestoreTicks = 0;
			combat.restoreSpecial(10);
		}
		if (antifireTicks > 0) {
			antifireTicks--;
			if (antifireTicks == 30) {
				sendMessage("<col=7f007f>Your antifire potion is about to expire.");
				privateSound(3120, 3, 0);
			} else if (antifireTicks == 0) {
				sendMessage("<col=7f007f>Your antifire potion has expired.");
				privateSound(2607, 1, 0);
			}
		}
		if (superAntifireTicks > 0) {
			superAntifireTicks--;
			if (superAntifireTicks == 30) {
				sendMessage("<col=7f007f>Your super antifire potion is about to expire.");
				privateSound(3120, 3, 0);
			} else if (superAntifireTicks == 0) {
				sendMessage("<col=7f007f>Your super antifire potion has expired.");
				privateSound(2607, 1, 0);
			}
		}
		if (staminaTicks > 0) {
			staminaTicks--;
			if (staminaTicks == 17) {
				sendMessage("<col=8f4808>Your stamina potion is about to expire.");
				privateSound(3120, 3, 0);
			} else if (staminaTicks == 0) {
				sendMessage("<col=8f4808>Your stamina potion has expired.");
				privateSound(2672, 3, 0);
				VarPlayerRepository.STAMINA_POTION.set(this, 0);
			}
		}
		if (dragonAxeSpecial > 0) { // really need a better system to handle things like this :)
			--dragonAxeSpecial;
			if (dragonAxeSpecial == 0) {
				player.sendMessage("<col=ff0000>Your woodcutting buff has expired.</col>");
			}
		}
		if (infernalAxeSpecial > 0) {
			--infernalAxeSpecial;
			if (infernalAxeSpecial == 0) {
				player.sendMessage("<col=ff0000>Your infernal woodcutting buff has expired.</col>");
			}
		}
		if (infernalPickAxeSpecial > 0) {
			--infernalPickAxeSpecial;
			if (infernalPickAxeSpecial == 0) {
				player.sendMessage("<col=ff0000>Your infernal mining buff has expired.</col>");
			}
		}

		if (dragonPickaxeSpecial > 0) { // really need a better system to handle things like this :)
			--dragonPickaxeSpecial;
			if (dragonPickaxeSpecial == 0) {
				player.sendMessage("<col=ff0000>Your mining buff has expired.</col>");
			}
		}
		if (infernalPickaxeSpecial > 0) {
			--infernalPickaxeSpecial;
			if (infernalPickaxeSpecial == 0) {
				player.sendMessage("<col=ff0000>Your infernal mining buff has expired.</col>");
			}
		}

		if (lastTimeKilledDonatorBoss > 0) {
			if (System.currentTimeMillis() >= lastTimeKilledDonatorBoss + 86400000) {
				player.sendMessage("<col=ff0000>You can now kill the donator boss again.</col>");
				lastTimeKilledDonatorBoss = 0;
				timesKilledDonatorBoss = 0;
			}
		}

		if (movement.hasMoved()) {
			idleTicks = 0;
			isIdle = false;
		} else if (++idleTicks >= 1000) {
			isIdle = true;
		}
		for (Item item : equipment.getItems()) {
			if (item != null && item.getDef() != null)
				item.getDef().onTick(this, item);
		}

		if (player.wildernessLevel <= 0 && !player.pvpAttackZone && player.snowballPeltOption &&
				!player.getEquipment().hasId(Christmas.SNOWBALL) && !player.getPosition().inBounds(DuelArena.BOUNDS)) {
			player.setAction(1, null);
			player.snowballPeltOption = false;
		}
	}

	private transient org.rsmod.game.entity.Player rsmodPlayer;

	@Override
	public org.rsmod.game.entity.Player rsmod() {
		return rsmodPlayer;
	}

	public void setRsmodPlayer(final org.rsmod.game.entity.Player rsmodPlayer) {
		this.rsmodPlayer = rsmodPlayer;
	}

	public void copyNpcToPlayerDummy(npc_combat.Info info) {
		if (dummyStats == null)
			dummyStats = new Stat[7];
		this.dummyStats[0] = new Stat(info.attack);
		this.dummyStats[1] = new Stat(info.defence);
		this.dummyStats[2] = new Stat(info.strength);
		this.dummyStats[3] = new Stat(info.hitpoints);
		this.dummyStats[4] = new Stat(info.ranged);
		this.dummyStats[6] = new Stat(info.magic);
	}

	public void startDoeTimer() {
		this.doeTimer = new ActivityTimer();
	}

	public void startDelveTimer() {
		this.delveTimer = new ActivityTimer();
	}

}
