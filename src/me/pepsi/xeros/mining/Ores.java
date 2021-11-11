package me.pepsi.xeros.mining;

public enum Ores {
	
	NONE("", -1),
	RUNE_ESSENCE("Rune_Essence", 2005),
	TIN("Tin", 11360),
	COPPER("Copper", 11161),
	IRON("Iron", 11364),
	COAL("Coal", 11366),
	GEM("Gem", 9030),
	MITHRIL("Mithril", 11373),
	ADAMANTITE("Adamantite", 11375),
	RUNITE("Runite", 11376),
	AMETHYST("Amethyst", -2);
	
	private final String name;
	private final int objectId;
	
	Ores(final String name, final int objectId) {
		this.name = name;
		this.objectId = objectId;
	}

	public int getObjectId() {
		return objectId;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
	}
	

}