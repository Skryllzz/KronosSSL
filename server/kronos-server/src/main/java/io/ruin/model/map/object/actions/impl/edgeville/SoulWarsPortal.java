package io.ruin.model.map.object.actions.impl.edgeville;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;

public class SoulWarsPortal {

	private static final int EDGEVILLE_SOUL_WARS_PORTAL = 40474;
	private static final int FEROX_SOUL_WARS_PORTAL = 40475;
	private static final int ISLE_OF_SOULS_PORTAL = 40476;

	private static final Position SOUL_WARS_LOBBY = new Position(2206, 2856, 0);
	private static final Position EDGEVILLE = new Position(3082, 3476, 0);
	private static final Position FEROX_ENCLAVE = new Position(3158, 10027, 0);
	private static final Position FEREOX_DUNG = new Position(3164, 10043, 0);

	private static void enterSoulWars(Player player, GameObject obj) {
		teleport(player, SOUL_WARS_LOBBY);
	}

	private static void enterIslePortal(Player player, GameObject obj) {
		player.dialogue(
			new OptionsDialogue("Where would you like to go?",
				new Option("Edgeville.", () -> teleport(player, EDGEVILLE)),
				new Option("Ferox Enclave.", () -> teleport(player, FEROX_ENCLAVE)),
				new Option("Cancel.", player::closeDialogue)
			)
		);
	}

	private static void teleport(Player player, Position position) {
		if (player.teleportListener != null && !player.teleportListener.allow(player)) {
			return;
		}
		player.getMovement().teleport(position);
	}

	public static void register() {
		ObjectAction.register(EDGEVILLE_SOUL_WARS_PORTAL, "Enter", SoulWarsPortal::enterSoulWars);
		ObjectAction.register(FEROX_SOUL_WARS_PORTAL, "Enter", SoulWarsPortal::enterSoulWars);

		ObjectAction.register(ISLE_OF_SOULS_PORTAL, "Enter", SoulWarsPortal::enterIslePortal);
		ObjectAction.register(ISLE_OF_SOULS_PORTAL, "Edgeville", (player, obj) -> teleport(player, EDGEVILLE));
		ObjectAction.register(ISLE_OF_SOULS_PORTAL, "Ferox Enclave", (player, obj) -> teleport(player, FEROX_ENCLAVE));
		ObjectAction.register(39647, "Walk-down", (player, obj) -> teleport(player, FEREOX_DUNG));
	}

}