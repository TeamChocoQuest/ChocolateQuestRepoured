package team.cqr.cqrepoured.client.init;

import java.util.Arrays;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import team.cqr.cqrepoured.CQRConstants;

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
	TRADE_EMERALD(true, "trader/trade_0"),
	TRADE_BAG(true, "trader/trade_1"),
	TRADE_HAND(true, "trader/trade_2");

	private static final ESpeechBubble[] NORMAL = Arrays.stream(ESpeechBubble.values()).filter(ESpeechBubble::isNormalBubble).toArray(ESpeechBubble[]::new);
	private static final ESpeechBubble[] TRADE = Arrays.stream(ESpeechBubble.values()).filter(ESpeechBubble::isTraderBubble).toArray(ESpeechBubble[]::new);
	private static final String folderPath = "textures/misc/speechbubbles/";
	private final boolean isTraderBubble;
	private final ResourceLocation resLoc;

	ESpeechBubble() {
		this.isTraderBubble = false;
		this.resLoc = new ResourceLocation(CQRConstants.MODID, folderPath + "bubble_" + this.name().toLowerCase() + ".png");
	}

	ESpeechBubble(boolean isTraderBubble, String filename) {
		this.isTraderBubble = isTraderBubble;
		this.resLoc = new ResourceLocation(CQRConstants.MODID, folderPath + filename + ".png");
	}

	public boolean isNormalBubble() {
		return !this.isTraderBubble;
	}

	public boolean isTraderBubble() {
		return this.isTraderBubble;
	}

	public ResourceLocation getResourceLocation() {
		return this.resLoc;
	}

	public static ESpeechBubble getRandNormalBubble(RandomSource rand) {
		return NORMAL[rand.nextInt(NORMAL.length)];
	}

	public static ESpeechBubble getRandTraderBubble(RandomSource random) {
		return TRADE[random.nextInt(TRADE.length)];
	}

}
