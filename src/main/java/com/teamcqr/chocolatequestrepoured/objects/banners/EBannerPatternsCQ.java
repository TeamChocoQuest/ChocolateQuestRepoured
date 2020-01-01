package com.teamcqr.chocolatequestrepoured.objects.banners;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BannerPattern;
import net.minecraftforge.common.util.EnumHelper;

public enum EBannerPatternsCQ {
	
	//TODO Wait for Artsquad to finish the textures
	//DONE: Add 'blank' cq pattern
	CQ_BLANK(addPattern("cq_blank", ItemStack.EMPTY)),
	WITHER_SKULL(addPattern("cq_wither_skull", ItemStack.EMPTY)),
	WITHER_SKULL_EYES(addPattern("cq_wither_eyes", ItemStack.EMPTY)),
	FIRE(addPattern("cq_fire", ItemStack.EMPTY)),
	MAGIC_SMOKE(addPattern("cq_magic", ItemStack.EMPTY)),
	EMERALD(addPattern("cq_emerald", ItemStack.EMPTY)),
	BONES(addPattern("cq_bones", ItemStack.EMPTY)),
	
	WALKER_BACKGROUND(addPattern("walker_black_bg", ItemStack.EMPTY)),
	WALKER_BORDER(addPattern("walker_border", ItemStack.EMPTY)),
	//WALKER_EYES(addPattern("walker_eyes", null)),
	WALKER_INNER_BORDER(addPattern("walker_inner_border", ItemStack.EMPTY)),
	WALKER_SKULL(addPattern("walker_skull", ItemStack.EMPTY)),;
	
	private BannerPattern mcPattern;
	
	private EBannerPatternsCQ(BannerPattern pattern) {
		this.mcPattern = pattern;
	}
	
	public BannerPattern getPattern() {
		return this.mcPattern;
	}
	
	/*public void changePattern(BannerPattern newPattern) {
		this.mcPattern = newPattern;
	}*/
	
	private static BannerPattern addPattern(String name, ItemStack craftingComponent) {
		
		final Class<?>[] paramTypes = {String.class, String.class, ItemStack.class};
		final Object[] paramVals = {Reference.MODID + "_" + name, Reference.MODID + "." + name, craftingComponent};
		BannerPattern pattern = EnumHelper.addEnum(BannerPattern.class, name.toUpperCase(), paramTypes, paramVals);
		
		return pattern;
	}

}
