package io.ruin.model.inter.utils;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.api.utils.StringUtils;

public class DiaryCompletionPopup {

    public static void open(Player player, String areaName, String difficultyName) {
        String title = "Achievement Diary Completed!";
        String message = "You have completed all of the " + areaName + " " + StringUtils.capitalizeFirst(difficultyName.toLowerCase()) + " Diary tasks.";
        String rewardMessage = "Speak to Two-pints to claim your reward.";

        player.openInterface(ToplevelComponent.MAINMODAL, 153);
        player.getPacketSender().sendString(153, 2, title);
        player.getPacketSender().sendString(153, 3, message + "<br>" + rewardMessage);
    }
}
