package com.teamcqr.chocolatequestrepoured.objects.banners;

import com.teamcqr.chocolatequestrepoured.util.Reference;
import com.teamcqr.chocolatequestrepoured.init.ModItems;

import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.tileentity.BannerPattern;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraft.init.Blocks;

public enum EBanner {
	
	CQR_BANNER_BULL("bull", new ItemStack(ModItems.HORN_BULL)),
	CQR_BANNER_CHOCOLATE("chocolate_bar", new ItemStack(Items.DIAMOND)),
	CQR_BANNER_DRAGON("dragon", new ItemStack(Blocks.DRAGON_EGG)),
	CQR_BANNER_DWARF("dwarf", new ItemStack(Items.IRON_PICKAXE)),
	CQR_BANNER_ELEMENTS("elements", new ItemStack(Items.BLAZE_POWDER)),
	CQR_BANNER_ENDERMAN("enderman", new ItemStack(Items.ENDER_PEARL)),
	CQR_BANNER_ENDERMEN("endermen", new ItemStack(Items.ENDER_EYE)),
	CQR_BANNER_FIRE("fire", new ItemStack(Items.FIRE_CHARGE)),
	CQR_BANNER_FLAME("flame", new ItemStack(Items.FLINT_AND_STEEL)),
	CQR_BANNER_INQUISITION("inquisition", new ItemStack(Items.GOLDEN_SWORD)),
	CQR_BANNER_LOGO("logo", new ItemStack(ModItems.BONE_MONKING)),
	CQR_BANNER_OGRE("ogre", new ItemStack(Items.SLIME_BALL)),
	CQR_BANNER_PIRATE("pirate", new ItemStack(Items.BONE)),
	CQR_BANNER_SPECTER("specter", new ItemStack(Items.GHAST_TEAR)),
	CQR_BANNER_SPIDER("spider", new ItemStack(ModItems.LEATHER_SPIDER)),
	CQR_BANNER_TREE("tree", new ItemStack(Blocks.LEAVES)),
	CQR_BANNER_WALKER("walker", new ItemStack(Items.DIAMOND_SWORD)),
	CQR_BANNER_GOLEM("white_orb", new ItemStack(Blocks.IRON_BLOCK)),
	CQR_BANNER_WING_L("wing_left", new ItemStack(Items.LEATHER)),
	CQR_BANNER_WING_R("wing_right", new ItemStack(Items.LEATHER)),
	CQR_BANNER_WOOD("wood", new ItemStack(Blocks.PLANKS)),
	CQR_BANNER_ZOMBIE("zombie_hat", new ItemStack(Items.ROTTEN_FLESH));
	
	
	private String fileName;
	private ItemStack craftingComponent;
	private BannerPattern bannerPattern;
	
	EBanner(String fName, ItemStack component) {
		this.fileName = fName;
		this.craftingComponent = component;
		
		 final Class<?>[] paramTypes = { String.class, String.class, ItemStack.class };
	     final Object[] paramValues = { Reference.MODID + "_" + this.fileName, Reference.MODID + "." + this.fileName, this.craftingComponent};
	     this.bannerPattern = EnumHelper.addEnum(BannerPattern.class, this.fileName.toUpperCase(), paramTypes, paramValues);
	}
	
	public BannerPattern getBannerPattern() {
		return this.bannerPattern;
	}

}
