package io.ruin.model.entity.npc.actions.ardounge;

import io.ruin.cache.ItemID;
import io.ruin.cache.NpcID;
import io.ruin.model.content.achievementdiary.AchievementDiary;
import io.ruin.model.content.achievementdiary.DiaryArea;
import io.ruin.model.content.achievementdiary.DiaryDifficulty;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;

public class TwoPints {

    private static final int TWO_PINTS = NpcID.TWOPINTS;
    private static final int ARDOUGNE_CLOAK_1 = ItemID.ARDOUGNE_CLOAK_1;
    private static final int ARDOUGNE_CLOAK_2 = ItemID.ARDOUGNE_CLOAK_2;
    private static final int ARDOUGNE_CLOAK_3 = ItemID.ARDOUGNE_CLOAK_3;
    private static final int ARDOUGNE_CLOAK_4 = ItemID.ARDOUGNE_CLOAK_4;
    private static final int ANTIQUE_LAMP = 13145;

    public static void register() {
        NPCAction.register(TWO_PINTS, "talk-to", (player, npc) -> talkTo(player));
    }

    private static void talkTo(Player player) {
        if (AchievementDiary.isCompleted(player, DiaryArea.ARDOUGNE, DiaryDifficulty.EASY) && !player.claimedDiaryRewards.contains("ARDOUGNE_EASY")) {
            player.dialogue(
                    new PlayerDialogue("I've completed all of the easy tasks in my Ardougne achievement diary!"),
                    new NPCDialogue(TWO_PINTS, "I can see that, well done! You'll be wanting your reward then!"),
                    new PlayerDialogue("Yes please!").action(() -> claimReward(player))
            );
            return;
        }

        player.dialogue(
                new NPCDialogue(TWO_PINTS, "'elo... *hic*"),
                new OptionsDialogue(
                        new Option("Can I have another cloak?", () -> {
                            if (player.claimedDiaryRewards.contains("ARDOUGNE_EASY")) {
                                if (!hasCloak(player)) {
                                    replaceCloak(player);
                                } else {
                                    player.dialogue(new NPCDialogue(TWO_PINTS, "You already have one! Come back when you've finished more achievements!"));
                                }
                            } else {
                                player.dialogue(new NPCDialogue(TWO_PINTS, "You haven't even earned one yet! Come back when you've finished the Ardougne Easy Diary."));
                            }
                        }),
                        new Option("Who are you?", () -> whoAreYou(player)),
                        new Option("I have a question about my Achievement Diary", () -> question(player)),
                        new Option("Bye!", () -> player.dialogue(new PlayerDialogue("Bye!"), new NPCDialogue(TWO_PINTS, "See you later!")))
                )
        );
    }

    private static void whoAreYou(Player player) {
        player.dialogue(
                new PlayerDialogue("Who are you?"),
                new NPCDialogue(TWO_PINTS, "They call me Two pints... I'm the taskmaster for the Ardougne Achievement Diary."),
                new OptionsDialogue(
                        new Option("Why do they call you Two pints?", () -> {
                            player.dialogue(
                                    new PlayerDialogue("Why do they call you Two pints?"),
                                    new NPCDialogue(TWO_PINTS, "I'm not too sure really... my memory isn't the greatest... *hic*"),
                                    new PlayerDialogue("Maybe you lost a game of Runelink and had to change your name as a forfeit?"),
                                    new NPCDialogue(TWO_PINTS, "Perhaps... *hic* I know a great drinking game for that!"),
                                    new PlayerDialogue("Suddenly it all makes sense..."),
                                    new NPCDialogue(TWO_PINTS, "Mutters incoherently and stumbles away.")
                            );
                        }),
                        new Option("What is the Achievement Diary?", () -> whatIsDiary(player)),
                        new Option("Bye!", () -> player.dialogue(new PlayerDialogue("Bye!"), new NPCDialogue(TWO_PINTS, "See you later!")))
                )
        );
    }

    private static void question(Player player) {
        player.dialogue(
                new PlayerDialogue("I have a question about my Achievement diary."),
                new OptionsDialogue(
                        new Option("What is the Achievement Diary?", () -> whatIsDiary(player)),
                        new Option("What are the rewards?", () -> rewards(player)),
                        new Option("How do I claim the rewards?", () -> {
                            player.dialogue(
                                    new PlayerDialogue("How do I claim the rewards?"),
                                    new NPCDialogue(TWO_PINTS, "Just complete the tasks in Ardougne so they're ticked off, then come and speak to me for your rewards.")
                            );
                        }),
                        new Option("Bye!", () -> player.dialogue(new PlayerDialogue("Bye!"), new NPCDialogue(TWO_PINTS, "See you later!")))
                )
        );
    }

    private static void whatIsDiary(Player player) {
        player.dialogue(
                new PlayerDialogue("What is the Achievement Diary?"),
                new NPCDialogue(TWO_PINTS, "It's a diary that helps you keep track of particular achievements. In and around Ardougne it can help you discover some quite useful things. Eventually, with enough exploration, the inhabitants will reward you."),
                new NPCDialogue(TWO_PINTS, "You can see the list of tasks on the side-panel.")
        );
    }

    private static void rewards(Player player) {
        player.dialogue(
                new PlayerDialogue("What are the rewards?"),
                new NPCDialogue(TWO_PINTS, "Well, there are four different Ardougne cloaks, which match up with the four levels of difficulty. Each has the same rewards as the previous level and some additional benefits too... which tier of rewards would you like to"),
                new NPCDialogue(TWO_PINTS, "know more about?"),
                new OptionsDialogue(
                        new Option("Easy Rewards.", () -> {
                            player.dialogue(
                                    new PlayerDialogue("Tell me more about the Easy rewards please!"),
                                    new NPCDialogue(TWO_PINTS, "If you complete all of the easy tasks in Ardougne: The Citizen in West Ardougne will buy cats for 200 death runes each, some drops in the Tower of Life will be noted and the cape can teleport you to the"),
                                    new NPCDialogue(TWO_PINTS, "Monastery south of Ardougne too."),
                                    new PlayerDialogue("Thanks!")
                            );
                        }),
                        new Option("Medium Rewards.", () -> {
                            player.dialogue(
                                    new PlayerDialogue("Tell me more about the Medium rewards please!"),
                                    new NPCDialogue(TWO_PINTS, "In addition to the easy rewards: Wizard Cromperty will give you 100 noted pure essence each day, even more drops will be noted in the Tower of Life, you've got a 10% better chance at pickpocketing in"),
                                    new NPCDialogue(TWO_PINTS, "Ardougne, you can toggle the ring of life to teleport to Ardougne, you get three teleports each day to the Ardougne farming patch and you can hold up to 56 coin pouches whilst pickpocketing."),
                                    new PlayerDialogue("Thanks!")
                            );
                        }),
                        new Option("Hard Rewards.", () -> {
                            player.dialogue(
                                    new PlayerDialogue("Tell me more about the Hard rewards please!"),
                                    new NPCDialogue(TWO_PINTS, "In addition to the easy and medium benefits: Wizard Cromperty will give you 150 pure essence each day, you can toggle the Watchtower teleport to the centre of Yanille, claim further drops as notes in the Tower of"),
                                    new NPCDialogue(TWO_PINTS, "Life, teleports to the farming patch increased to five per day, have a 10% better chance at pickpocketing everywhere and you can hold up to 84 coin pouches whilst pickpocketing."),
                                    new PlayerDialogue("Thanks!")
                            );
                        }),
                        new Option("Elite Rewards.", () -> {
                            player.dialogue(
                                    new PlayerDialogue("Tell me more about the Elite rewards please!"),
                                    new NPCDialogue(TWO_PINTS, "In addition to the previous tiers of rewards: Wizard Cromperty will give you 250 pure essence every day, you will catch 50% more fish from the Fishing Trawler and receive 25% more marks of grace on the"),
                                    new NPCDialogue(TWO_PINTS, "Ardougne agility course. Your cape will have unlimited teleports to the Ardougne farming patch, Bert will automatically deliver your daily sand and you can hold up to 140 coin pouches whilst pickpocketing."),
                                    new PlayerDialogue("Thanks!")
                            );
                        })
                )
        );
    }

    private static void claimReward(Player player) {
        if (player.getInventory().getFreeSlots() < 2) {
            player.dialogue(new NPCDialogue(TWO_PINTS, "You'll need at least two free inventory slots to claim your reward."));
            return;
        }

        player.claimedDiaryRewards.add("ARDOUGNE_EASY");
        player.getInventory().add(ARDOUGNE_CLOAK_1, 1);
        player.getInventory().add(ANTIQUE_LAMP, 1);

        player.dialogue(
                new NPCDialogue(TWO_PINTS, "This cloak is a symbol of your exploration of Ardougne. The Citizen in West Ardougne will buy cats for 200 death runes each, some drops in the Tower of Life will be noted and the cape can teleport you to the"),
                new NPCDialogue(TWO_PINTS, "Monastery south of Ardougne too."),
                new PlayerDialogue("Wow, thanks!"),
                new NPCDialogue(TWO_PINTS, "If you ever lose your cloak, come back to me to reclaim it.")
        );
    }

    private static void replaceCloak(Player player) {
        if (player.getInventory().isFull()) {
            player.dialogue(new NPCDialogue(TWO_PINTS, "You'll need at least one free inventory slot to receive a replacement cloak."));
            return;
        }

        player.getInventory().add(ARDOUGNE_CLOAK_1, 1);
        player.dialogue(
                new PlayerDialogue("Can I have another cloak?"),
                new NPCDialogue(TWO_PINTS, "There you go. Try not to lose it this time!")
        );
    }

    private static boolean hasCloak(Player player) {
        int[] cloaks = {ARDOUGNE_CLOAK_1, ARDOUGNE_CLOAK_2, ARDOUGNE_CLOAK_3, ARDOUGNE_CLOAK_4};
        for (int id : cloaks) {
            if (player.getInventory().contains(id, 1) || player.getEquipment().hasId(id) || player.getBank().contains(id, 1)) {
                return true;
            }
        }
        return false;
    }
}
