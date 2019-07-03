package com.teamcqr.chocolatequestrepoured.objects.banners;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class BannerHelper {

	public BannerHelper() {}
	
	public static List<ItemStack> addBannersToTabs() {
		List<ItemStack> itemList = new ArrayList<>();
		for(EBanners banner : EBanners.values()) {
			itemList.add(banner.getBanner());
		}
		return itemList;
	}

}
