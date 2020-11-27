package com.teamcqr.chocolatequestrepoured.structuregen.inhabitants;

import com.teamcqr.chocolatequestrepoured.init.CQRItems;
import com.teamcqr.chocolatequestrepoured.objects.banners.EBanners;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

enum EDefaultInhabitants {

	DEFAULT(new ResourceLocation[] { new ResourceLocation(Reference.MODID, "dummy") }, new ResourceLocation[] { new ResourceLocation(Reference.MODID, "dummy") }, null, Items.SHIELD), DWARF(new ResourceLocation[] {
			new ResourceLocation(Reference.MODID, "dwarf") }, null, null, CQRItems.SHIELD_CARL), SKELETON(new ResourceLocation[] { new ResourceLocation(Reference.MODID, "skeleton") }, new ResourceLocation[] {
					new ResourceLocation(Reference.MODID, "necromancer") }, EBanners.SKELETON_BANNER, CQRItems.SHIELD_SKELETON_FRIENDS), ZOMBIE(new ResourceLocation[] { new ResourceLocation(Reference.MODID, "zombie") }, new ResourceLocation[] {
							new ResourceLocation(Reference.MODID, "lich") }, null, CQRItems.SHIELD_ZOMBIE), PIRATE(new ResourceLocation[] { new ResourceLocation(Reference.MODID, "pirate") }, new ResourceLocation[] {
									new ResourceLocation(Reference.MODID, "pirate_captain") }, EBanners.PIRATE_BANNER, CQRItems.SHIELD_PIRATE2), ILLAGER(new ResourceLocation[] { new ResourceLocation(Reference.MODID, "illager") }, null,
											EBanners.ILLAGER_BANNER), WALKER(new ResourceLocation[] { new ResourceLocation(Reference.MODID, "walker") }, new ResourceLocation[] { new ResourceLocation(Reference.MODID, "walker_king") },
													EBanners.WALKER_ORDO, CQRItems.SHIELD_WALKER), SPECTER(new ResourceLocation[] { new ResourceLocation(Reference.MODID, "spectre") }, null, null, CQRItems.SHIELD_SPECTER), ENDERMAN(
															new ResourceLocation[] { new ResourceLocation(Reference.MODID, "enderman") }, null, EBanners.ENDERMAN_BANNER), BOARMAN(new ResourceLocation[] {
																	new ResourceLocation(Reference.MODID, "boarman") }, new ResourceLocation[] { new ResourceLocation(Reference.MODID, "boar_mage") }, EBanners.PIGMAN_BANNER, CQRItems.SHIELD_FIRE),
	MINOTAUR(new ResourceLocation[] { new ResourceLocation(Reference.MODID, "minotaur") }, null, null, CQRItems.SHIELD_PIGMAN), ORC(new ResourceLocation[] { new ResourceLocation(Reference.MODID, "orc") }, null, null, CQRItems.SHIELD_WARPED), GOLEM(
			new ResourceLocation[] { new ResourceLocation(Reference.MODID, "golem") }, null, null), MUMMY(new ResourceLocation[] { new ResourceLocation(Reference.MODID, "mummy") }, null, null, CQRItems.SHIELD_MUMMY), OGRE(new ResourceLocation[] {
					new ResourceLocation(Reference.MODID, "ogre") }, null, null, CQRItems.SHIELD_TOMB), GREMLIN(new ResourceLocation[] { new ResourceLocation(Reference.MODID, "gremlin") }, null, EBanners.GREMLIN_BANNER, CQRItems.SHIELD_RUSTED),
	TRITON(new ResourceLocation[] { new ResourceLocation(Reference.MODID, "triton") }, null, null);

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
		return this.resLoc;
	}

	public ResourceLocation[] getBossIDs() {
		return this.bossResLoc;
	}

}
