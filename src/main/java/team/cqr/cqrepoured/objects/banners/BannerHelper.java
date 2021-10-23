package team.cqr.cqrepoured.objects.banners;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBanner;

public class BannerHelper {

	public static List<ItemStack> addBannersToTabs() {
		List<ItemStack> itemList = new ArrayList<>();
		for (EBanners banner : EBanners.values()) {
			itemList.add(banner.getBanner());
		}
		return itemList;
	}

	public static boolean isCQBanner(TileEntityBanner bannerTile) {
		if (bannerTile.patternList == null) {
			return false;
		}
		if (bannerTile.patternList.isEmpty()) {
			return false;
		}
		for (EBannerPatternsCQ cqPattern : EBannerPatternsCQ.values()) {
			if (bannerTile.patternList.contains(cqPattern.getPattern())) {
				return true;
			}
		}
		return false;
	}

}
