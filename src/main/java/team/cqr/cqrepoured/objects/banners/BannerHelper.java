package team.cqr.cqrepoured.objects.banners;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.BannerPattern;
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
		if (bannerTile.patterns == null) {
			return false;
		}
		if (bannerTile.patterns.isEmpty()) {
			return false;
		}
		for(int i = 0; i < bannerTile.patterns.tagCount(); i++) {
			NBTTagCompound nbttagcompound = bannerTile.patterns.getCompoundTagAt(i);
            BannerPattern bannerpattern = BannerPattern.byHash(nbttagcompound.getString("Pattern"));
            if(bannerpattern == EBannerPatternsCQ.CQ_BLANK.getPattern()) {
            	return true;
            }
		}
		return false;
	}

}
