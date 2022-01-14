package team.cqr.cqrepoured.init;

import net.minecraft.tileentity.BannerPattern;
import team.cqr.cqrepoured.CQRMain;

public class CQRBannerPatterns {
	
	public static BannerPattern CQ_BLANK = create("cq_blank", "blk", false);
	public static BannerPattern WITHER_SKULL = create("cq_wither_skull", "wskl", false);
	public static BannerPattern WITHER_SKULL_EYES = create("cq_wither_eyes", "wsey", false);
	public static BannerPattern FIRE = create("cq_fire", "fre", false);
	public static BannerPattern EMERALD = create("cq_emerald", "erld", false);
	public static BannerPattern BONES = create("cq_bbones", "bns", false);
	public static BannerPattern WALKER_BACKGROUND = create("walker_black_bg", "wblbg", false);
	public static BannerPattern WALKER_BORDER = create("walker_border", "wkbd", false);
	public static BannerPattern WALKER_INNER_BORDER = create("walker_inner_border", "wibd", false);
	public static BannerPattern WALKER_SKULL = create("walker_skull", "wlksk", false);
			
	protected static BannerPattern create(String name, String hashName, boolean hasPatternItem) {
		return BannerPattern.create(CQRMain.MODID + "_" + "cq_blank", CQRMain.MODID + "." + name, "cqr." + hashName, hasPatternItem);
	}

}
