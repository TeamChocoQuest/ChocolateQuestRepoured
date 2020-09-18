package com.teamcqr.chocolatequestrepoured.objects.banners;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.util.reflection.ReflectionField;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.TileEntityBanner;

public class BannerHelper {

	private static final ReflectionField<List<BannerPattern>> PATTERN_LIST = new ReflectionField<>(TileEntityBanner.class, "field_175122_h", "patternList");

	public static List<ItemStack> addBannersToTabs() {
		List<ItemStack> itemList = new ArrayList<>();
		for (EBanners banner : EBanners.values()) {
			itemList.add(banner.getBanner());
		}
		return itemList;
	}

	public static boolean isCQBanner(TileEntityBanner bannerTile) {
		List<BannerPattern> patterns = PATTERN_LIST.get(bannerTile);
		if (patterns != null && !patterns.isEmpty()) {
			for (EBannerPatternsCQ cqPattern : EBannerPatternsCQ.values()) {
				if (patterns.contains(cqPattern.getPattern())) {
					return true;
				}
			}
		}
		return false;
	}

}
