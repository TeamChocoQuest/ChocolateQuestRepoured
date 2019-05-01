package com.teamcqr.chocolatequestrepoured.objects.banners;

import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BannerPattern;
import net.minecraftforge.common.util.EnumHelper;

public class BannerHandler {

	public static void initPatterns() {
		addPattern("bull", new ItemStack(ModItems.HORN_BULL));
		addPattern("chocolate_bar", new ItemStack(Items.GOLD_INGOT));
		addPattern("dragon", new ItemStack(Items.DRAGON_BREATH));
		addPattern("dwarf", new ItemStack(Items.IRON_PICKAXE));
		addPattern("elements", new ItemStack(Items.BLAZE_POWDER));
		addPattern("enderman", new ItemStack(Items.ENDER_PEARL));
		addPattern("endermen", new ItemStack(Items.ENDER_EYE));
		addPattern("fire", new ItemStack(Items.FIRE_CHARGE));
		addPattern("flame", new ItemStack(Items.FLINT_AND_STEEL));
		addPattern("inquisition", new ItemStack(Items.GOLDEN_SWORD));
		addPattern("logo", new ItemStack(ModItems.BONE_MONKING));
		addPattern("ogre", new ItemStack(ModItems.SCALE_TURTLE));
		addPattern("pirate", new ItemStack(Items.BONE));
		addPattern("specter", new ItemStack(Items.GHAST_TEAR));
		addPattern("spider", new ItemStack(ModItems.LEATHER_SPIDER));
		addPattern("tree", new ItemStack(Blocks.SAPLING));
		addPattern("walker", new ItemStack(Items.DIAMOND_SWORD));
		addPattern("white_orb", new ItemStack(Items.IRON_INGOT));
		addPattern("wing_left", new ItemStack(Items.LEATHER));
		addPattern("wing_right", new ItemStack(Items.LEATHER, 2));
		addPattern("wood", new ItemStack(Blocks.PLANKS));
		addPattern("zombie_hat", new ItemStack(Items.ROTTEN_FLESH));
	}
	
	private static BannerPattern addPattern(String name, ItemStack craftingComponent) {
		
		final Class<?>[] paramTypes = {String.class, String.class, ItemStack.class};
		final Object[] paramVals = {Reference.MODID + "_" + name, Reference.MODID + "." + name, craftingComponent};
		return EnumHelper.addEnum(BannerPattern.class, name.toUpperCase(), paramTypes, paramVals);
		
	}
	
	public BannerHandler() {
	}

}
