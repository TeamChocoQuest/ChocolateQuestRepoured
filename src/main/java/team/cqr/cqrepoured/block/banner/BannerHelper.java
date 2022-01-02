package team.cqr.cqrepoured.block.banner;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.BannerTileEntity;

public class BannerHelper {

	public static List<ItemStack> addBannersToTabs() {
		List<ItemStack> itemList = new ArrayList<>();
		for (EBanners banner : EBanners.values()) {
			itemList.add(banner.getBanner());
		}
		return itemList;
	}

	public static boolean isCQBanner(BannerTileEntity bannerTile) {
		if (bannerTile.itemPatterns == null) {
			return false;
		}
		if (bannerTile.itemPatterns.isEmpty()) {
			return false;
		}
		for (int i = 0; i < bannerTile.itemPatterns.size(); i++) {
			CompoundNBT nbttagcompound = bannerTile.itemPatterns.getCompound(i);
			if (nbttagcompound.getString("Pattern").equals(EBannerPatternsCQ.CQ_BLANK.getPattern().getHashname())) {
				return true;
			}
		}
		return false;
	}

}
