package io.ruin.api.protocol.world;

public enum WorldType {
	ECO("SkyScapeLive", "https://skryllz.com/"),
	BETA("SkyScapeLive", "https://reasonps.com/"),
	PVP("SkyScapeLive", "https://reasonps.com/"),

	DEADMAN("SkyScapeLive", "https://reasonps.com/"),
	DEV("SkyScapeLive", "https://reasonps.com/");

	WorldType(String worldName, String websiteUrl) {
		this.worldName = worldName;
		this.websiteUrl = websiteUrl;
	}

	public boolean isDeadman() {
		return this == DEADMAN;
	}

	private String worldName, websiteUrl;

	public String getWorldName() {
		return worldName;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}
}