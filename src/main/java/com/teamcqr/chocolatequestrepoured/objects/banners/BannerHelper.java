package com.teamcqr.chocolatequestrepoured.objects.banners;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class BannerHelper {

	public BannerHelper() {
	}

	public static List<ItemStack> addBannersToTabs() {
		List<ItemStack> itemList = new ArrayList<>();
		for (EBanners banner : EBanners.values()) {
			itemList.add(banner.getBanner());
		}
		return itemList;
	}

	public static boolean isCQBanner(TileEntityBanner bannerTile) {
		List<BannerPattern> patterns = new ArrayList<>();
		patterns = ReflectionHelper.getPrivateValue(TileEntityBanner.class, bannerTile, 4);
		if (bannerTile != null && !patterns.isEmpty()) {
			for (EBannerPatternsCQ cqPattern : EBannerPatternsCQ.values()) {
				if (bannerTile.getPatternList().contains(cqPattern.getPattern())) {
					return true;
				}
			}
		}
		return false;
	}

}
