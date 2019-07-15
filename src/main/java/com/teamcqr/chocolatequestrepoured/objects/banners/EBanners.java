package com.teamcqr.chocolatequestrepoured.objects.banners;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.BannerPattern;

public enum EBanners {
	
	//DONE: Add the cq-blank design to all!!
	PIRATE_BANNER(EnumDyeColor.BLACK, new BannerPattern[] {EBannerPatternsCQ.CQ_BLANK.getPattern(), BannerPattern.SKULL}, new EnumDyeColor[] {EnumDyeColor.WHITE, EnumDyeColor.WHITE}, "Flag Of Piracy"),
	WALKER_BANNER(EnumDyeColor.SILVER, new BannerPattern[] {EBannerPatternsCQ.CQ_BLANK.getPattern(), BannerPattern.BRICKS, EBannerPatternsCQ.WITHER_SKULL_EYES.getPattern(), EBannerPatternsCQ.WITHER_SKULL.getPattern(), EBannerPatternsCQ.WITHER_SKULL.getPattern()}, new EnumDyeColor[] {EnumDyeColor.WHITE, EnumDyeColor.GRAY, EnumDyeColor.CYAN, EnumDyeColor.BLACK, EnumDyeColor.BLACK}, "Nightwatch"),
	PIGMAN_BANNER(EnumDyeColor.RED, new BannerPattern[] {EBannerPatternsCQ.CQ_BLANK.getPattern(), EBannerPatternsCQ.FIRE.getPattern()}, new EnumDyeColor[] {EnumDyeColor.YELLOW}, "Combine"),
	ENDERMEN_BANNER(EnumDyeColor.MAGENTA, new BannerPattern[] {EBannerPatternsCQ.CQ_BLANK.getPattern(), BannerPattern.TRIANGLE_BOTTOM, BannerPattern.TRIANGLE_TOP}, new EnumDyeColor[] {EnumDyeColor.WHITE, EnumDyeColor.BLACK, EnumDyeColor.BLACK}, "Enderman"),
	ENDERMEN_BANNER2(EnumDyeColor.MAGENTA, new BannerPattern[] {EBannerPatternsCQ.CQ_BLANK.getPattern(), BannerPattern.SQUARE_TOP_RIGHT, BannerPattern.SQUARE_BOTTOM_LEFT}, new EnumDyeColor[] {EnumDyeColor.WHITE, EnumDyeColor.BLACK, EnumDyeColor.BLACK}, "Enderman"),
	NPC_BANNER(EnumDyeColor.RED, new BannerPattern[] {EBannerPatternsCQ.CQ_BLANK.getPattern(), BannerPattern.FLOWER, BannerPattern.STRIPE_CENTER, EBannerPatternsCQ.EMERALD.getPattern()}, new EnumDyeColor[] {EnumDyeColor.WHITE, EnumDyeColor.YELLOW, EnumDyeColor.RED, EnumDyeColor.LIME}, "Merchants"),
	SKELETON_BANNER(EnumDyeColor.BLACK, new BannerPattern[] {EBannerPatternsCQ.CQ_BLANK.getPattern(), BannerPattern.CROSS, EBannerPatternsCQ.BONES.getPattern()}, new EnumDyeColor[] {EnumDyeColor.WHITE, EnumDyeColor.RED, EnumDyeColor.WHITE}, "Undead Bones"),
	//GREMLIN_BANNER(EnumDyeColor.SILVER, new BannerPattern[] { }, new EnumDyeColor[] { }),
	//WINGS_OF_FREEDOM(EnumDyeColor.WHITE, new BannerPattern[] {BannerPattern.DIAGONAL_LEFT_MIRROR, BannerPattern.BRICKS, BannerPattern.TRIANGLE_TOP, BannerPattern.GRADIENT, BannerPattern.BORDER}, new EnumDyeColor[] {EnumDyeColor.BLUE, EnumDyeColor.SILVER, EnumDyeColor.SILVER, EnumDyeColor.SILVER, EnumDyeColor.SILVER}),
	//GERMANY(EnumDyeColor.RED, new BannerPattern[] {BannerPattern.STRIPE_MIDDLE, BannerPattern.STRIPE_CENTER, BannerPattern.STRAIGHT_CROSS, BannerPattern.CIRCLE_MIDDLE, BannerPattern.STRAIGHT_CROSS}, new EnumDyeColor[] {EnumDyeColor.WHITE, EnumDyeColor.WHITE, EnumDyeColor.BLACK, EnumDyeColor.WHITE, EnumDyeColor.BLACK}),
	ILLAGER_BANNER(EnumDyeColor.WHITE, new BannerPattern[] {EBannerPatternsCQ.CQ_BLANK.getPattern(), BannerPattern.RHOMBUS_MIDDLE, BannerPattern.STRIPE_BOTTOM, BannerPattern.STRIPE_CENTER, BannerPattern.BORDER, BannerPattern.STRIPE_MIDDLE, BannerPattern.HALF_HORIZONTAL}, new EnumDyeColor[] {EnumDyeColor.WHITE, EnumDyeColor.RED, EnumDyeColor.SILVER, EnumDyeColor.GRAY, EnumDyeColor.SILVER, EnumDyeColor.BLACK, EnumDyeColor.SILVER}, "Pillagers"),;
	
	private BannerPattern[] patternList;
	private EnumDyeColor[] colorList;
	private EnumDyeColor mainColor;
	private String name;
	
	private EBanners(EnumDyeColor mainColor, BannerPattern[] patterns, EnumDyeColor[] colors, String name) {
		this.mainColor = mainColor;
		this.colorList = colors;
		this.patternList = patterns;
		this.name = name;
	}

	public ItemStack getBanner() {
		final NBTTagList patternList = new NBTTagList();
		
		for(int i = 0; i < this.patternList.length; i++) {
			BannerPattern currPatt = this.patternList[i];
			EnumDyeColor currCol = this.colorList[i];
			
			final NBTTagCompound tag = new NBTTagCompound();
			tag.setString("Pattern", currPatt.getHashname());
			tag.setInteger("Color", currCol.getDyeDamage());
			
			patternList.appendTag(tag);
		}
		
		ItemStack item = ItemBanner.makeBanner(this.mainColor, patternList);
		item = item.setStackDisplayName(this.name);
		return item;
	}
	
}
