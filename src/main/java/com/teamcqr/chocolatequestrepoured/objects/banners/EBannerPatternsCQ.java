package com.teamcqr.chocolatequestrepoured.objects.banners;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BannerPattern;
import net.minecraftforge.common.util.EnumHelper;

public enum EBannerPatternsCQ {
	
	//TODO Wait for Artsquad to finish the textures
	//DONE: Add 'blank' cq pattern
	CQ_BLANK(addPattern("cq_blank", null)),
	WITHER_SKULL(addPattern("cq_wither_skull", null)),
	WITHER_SKULL_EYES(addPattern("cq_wither_eyes", null)),
	FIRE(addPattern("cq_fire", null)),
	MAGIC_SMOKE(addPattern("cq_magic", null)),
	EMERALD(addPattern("cq_emerald", null)),
	BONES(addPattern("cq_bones", null)),
	
	WALKER_BACKGROUND(addPattern("walker_black_bg", null)),
	WALKER_BORDER(addPattern("walker_border", null)),
	//WALKER_EYES(addPattern("walker_eyes", null)),
	WALKER_INNER_BORDER(addPattern("walker_inner_border", null)),
	WALKER_SKULL(addPattern("walker_skull", null)),;
	
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
