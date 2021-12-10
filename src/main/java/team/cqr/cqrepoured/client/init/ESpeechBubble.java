package team.cqr.cqrepoured.client.init;

import java.util.Random;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;

public enum ESpeechBubble {

	BLOCK_BED,
	BLOCK_CASTLE,
	BLOCK_FLOWER,
	BLOCK_PORTAL_NETHER,

	EMOTE_CLOUDY,
	EMOTE_MAZE,
	EMOTE_O,
	EMOTE_RAGE,
	EMOTE_RAINY,
	EMOTE_SMILE,
	EMOTE_SMIRK,
	EMOTE_THUNDER,

	ENTITY_ALEX,
	ENTITY_CAT,
	ENTITY_CREEPER,
	ENTITY_DRAGON,
	ENTITY_ENDER_DRAGON,
	ENTITY_PARROT,
	ENTITY_SKELETON,
	ENTITY_STEVE,
	ENTITY_VILLAGER,
	ENTITY_WALKER,
	ENTITY_WITHER,
	ENTITY_WOLF,
	ENTITY_ZOMBIE,

	ITEM_BEER,
	ITEM_BREAD,
	ITEM_CHOCOLATE,
	ITEM_COCOA,
	ITEM_EMERALD,
	ITEM_FLINTLOCK,
	ITEM_MAP,
	ITEM_NUGGET,
	ITEM_PIZZA,
	ITEM_POTION,
	ITEM_SWORD,

	// Traders only!!
	TRADE_EMERALD("trader/trade_0"),
	TRADE_BAG("trader/trade_1"),
	TRADE_HAND("trader/trade_2");

	private final ResourceLocation resLoc;
	static final String folderPath = "textures/misc/speechbubbles/";

	ESpeechBubble() {
		this.resLoc = new ResourceLocation(CQRMain.MODID, folderPath + "bubble_" + this.name().toLowerCase() + ".png");
	}

	ESpeechBubble(String filename) {
		this.resLoc = new ResourceLocation(CQRMain.MODID, folderPath + filename + ".png");
	}

	public ResourceLocation getResourceLocation() {
		return this.resLoc;
	}

	public static ESpeechBubble getRandom(Random rdm) {
		return values()[rdm.nextInt(values().length)];
	}

}
