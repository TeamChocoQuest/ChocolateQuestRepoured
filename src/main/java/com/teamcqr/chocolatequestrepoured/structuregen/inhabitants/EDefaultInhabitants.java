package com.teamcqr.chocolatequestrepoured.structuregen.inhabitants;

import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.banners.EBanners;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

enum EDefaultInhabitants {

	DEFAULT(null, null, null),
	// DONT_REPLACE(null),
	DWARF(new ResourceLocation[] {new ResourceLocation(Reference.MODID, "dwarf")}, null, null, ModItems.SHIELD_CARL),
	SKELETON(new ResourceLocation[] {new ResourceLocation(Reference.MODID, "skeleton")}, new ResourceLocation[] {new ResourceLocation(Reference.MODID, "necromancer")}, EBanners.SKELETON_BANNER, ModItems.SHIELD_SKELETON_FRIENDS),
	ZOMBIE(new ResourceLocation[] {new ResourceLocation(Reference.MODID, "zombie")}, new ResourceLocation[] {new ResourceLocation(Reference.MODID, "lich")}, null, ModItems.SHIELD_ZOMBIE),
	PIRATE(new ResourceLocation[] {new ResourceLocation(Reference.MODID, "pirate")}, new ResourceLocation[] {new ResourceLocation(Reference.MODID, "pirate_captain")}, EBanners.PIRATE_BANNER, ModItems.SHIELD_PIRATE2),
	ILLAGER(new ResourceLocation[] {new ResourceLocation(Reference.MODID, "illager")}, null, EBanners.ILLAGER_BANNER),
	WALKER(new ResourceLocation[] {new ResourceLocation(Reference.MODID, "walker")}, new ResourceLocation[] {new ResourceLocation(Reference.MODID, "walker_king")}, EBanners.WALKER_ORDO, ModItems.SHIELD_WALKER),
	SPECTER(new ResourceLocation[] {new ResourceLocation(Reference.MODID, "spectre")}, null, null, ModItems.SHIELD_SPECTER),
	ENDERMAN(new ResourceLocation[] {new ResourceLocation(Reference.MODID, "enderman")}, null, EBanners.ENDERMEN_BANNER),
	BOARMAN(new ResourceLocation[] {new ResourceLocation(Reference.MODID, "boarman")}, new ResourceLocation[] {new ResourceLocation(Reference.MODID, "boar_mage")}, EBanners.PIGMAN_BANNER, ModItems.SHIELD_FIRE),
	MINOTAUR(new ResourceLocation[] {new ResourceLocation(Reference.MODID, "minotaur")}, null, null, ModItems.SHIELD_PIGMAN),
	ORC(new ResourceLocation[] {new ResourceLocation(Reference.MODID, "orc")}, null, null, ModItems.SHIELD_WARPED),
	GOLEM(new ResourceLocation[] {new ResourceLocation(Reference.MODID, "golem")}, null, null),
	MUMMY(new ResourceLocation[] {new ResourceLocation(Reference.MODID, "mummy")}, null, null, ModItems.SHIELD_MUMMY),
	OGRE(new ResourceLocation[] {new ResourceLocation(Reference.MODID, "ogre")}, null, null, ModItems.SHIELD_TOMB),
	GREMLIN(new ResourceLocation[] {new ResourceLocation(Reference.MODID, "gremlin")}, null, EBanners.GREMLIN_BANNER, ModItems.SHIELD_RUSTED),
	TRITON(new ResourceLocation[] {new ResourceLocation(Reference.MODID, "triton")}, null, null);

	private ResourceLocation[] resLoc;
	private ResourceLocation[] bossResLoc;
	private Item shieldItem;
	private EBanners banner;

	private EDefaultInhabitants(ResourceLocation[] resLoc, ResourceLocation[] bossResLoc, EBanners banner, Item shieldItem) {
		this.resLoc = resLoc;
		this.banner = banner;
		this.shieldItem = shieldItem;
		this.bossResLoc = bossResLoc;
	}
	
	private EDefaultInhabitants(ResourceLocation[] resLoc, ResourceLocation[] bossResLoc, EBanners banner) {
		this.resLoc = resLoc;
		this.banner = banner;
		this.shieldItem = Items.SHIELD;
		this.bossResLoc = bossResLoc;
	}

	public EBanners getBanner() {
		return this.banner;
	}
	
	public ItemStack getShieldItem() {
		return new ItemStack(this.shieldItem, 1);
	}
	
	public ResourceLocation[] getEntityIDs() {
		return resLoc;
	}
	
	public ResourceLocation[] getBossIDs() {
		return bossResLoc;
	}

}
