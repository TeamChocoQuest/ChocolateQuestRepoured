package com.teamcqr.chocolatequestrepoured.objects.banners;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BannerPattern;
import net.minecraftforge.common.util.EnumHelper;

public enum EBannerPatternsCQ {
	
	//TODO Wait for Artsquad to finish the textures
	CQ_BLANK(addPattern("cq_blank", new ItemStack(Blocks.BARRIER, 0))),
	WITHER_SKULL(addPattern("cq_wither_skull", new ItemStack(Items.SKULL))),
	WITHER_SKULL_EYES(addPattern("cq_wither_eyes", new ItemStack(Items.GHAST_TEAR, 0))),
	FIRE(addPattern("cq_fire", new ItemStack(Items.FIRE_CHARGE))),
	MAGIC_SMOKE(addPattern("cq_magic", new ItemStack(Items.BLAZE_POWDER))),
	EMERALD(addPattern("cq_emerald", new ItemStack(Items.EMERALD))),
	BONES(addPattern("cq_bones", new ItemStack(Items.BONE))),;
	
	private BannerPattern mcPattern;
	
	private EBannerPatternsCQ(BannerPattern pattern) {
		this.mcPattern = pattern;
	}
	
	public BannerPattern getPattern() {
		return this.mcPattern;
	}
	
	public void changePattern(BannerPattern newPattern) {
		this.mcPattern = newPattern;
	}
	
	private static BannerPattern addPattern(String name, ItemStack craftingComponent) {
		
		final Class<?>[] paramTypes = {String.class, String.class, ItemStack.class};
		final Object[] paramVals = {Reference.MODID + "_" + name, Reference.MODID + "." + name, craftingComponent};
		BannerPattern pattern = EnumHelper.addEnum(BannerPattern.class, name.toUpperCase(), paramTypes, paramVals);
		
		return pattern;
	}

}
