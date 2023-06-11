package team.cqr.cqrepoured.block.banner;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import team.cqr.cqrepoured.init.CQRBannerPatterns;

public class BannerHelper {

	public static List<ItemStack> addBannersToTabs() {
		List<ItemStack> itemList = new ArrayList<>();
		for (EBanners banner : EBanners.values()) {
			itemList.add(banner.getBanner());
		}
		return itemList;
	}

	public static boolean isCQBanner(BannerBlockEntity bannerTile) {
		if (bannerTile.itemPatterns == null) {
			return false;
		}
		if (bannerTile.itemPatterns.isEmpty()) {
			return false;
		}
		for (int i = 0; i < bannerTile.itemPatterns.size(); i++) {
			CompoundTag nbttagcompound = bannerTile.itemPatterns.getCompound(i);
			if (nbttagcompound.getString("Pattern").equals(CQRBannerPatterns.CQ_BLANK.getHashname())) {
				return true;
			}
		}
		return false;
	}

}
