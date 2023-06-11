package team.cqr.cqrepoured.world.structure.generation.inhabitants;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.block.banner.EBanners;
import team.cqr.cqrepoured.init.CQRItems;

enum EDefaultInhabitants {

	DEFAULT(new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "dummy") }, new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "dummy") }, null, Items.SHIELD),
	DWARF(new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "dwarf") }, null, null, CQRItems.SHIELD_CARL.get()),
	SKELETON(new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "skeleton") }, new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "necromancer") }, EBanners.SKELETON_BANNER, CQRItems.SHIELD_SKELETON_FRIENDS.get()),
	ZOMBIE(new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "zombie") }, new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "lich") }, null, CQRItems.SHIELD_ZOMBIE.get()),
	PIRATE(new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "pirate") }, new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "pirate_captain") }, EBanners.PIRATE_BANNER, CQRItems.SHIELD_PIRATE2.get()),
	ILLAGER(new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "illager") }, null, EBanners.ILLAGER_BANNER),
	WALKER(new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "walker") }, new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "walker_king") }, EBanners.WALKER_ORDO, CQRItems.SHIELD_WALKER.get()),
	SPECTER(new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "spectre") }, null, null, CQRItems.SHIELD_SPECTER.get()),
	ENDERMAN(new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "enderman") }, null, EBanners.ENDERMAN_BANNER),
	BOARMAN(new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "boarman") }, new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "boar_mage") }, EBanners.PIGMAN_BANNER, CQRItems.SHIELD_FIRE.get()),
	MINOTAUR(new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "minotaur") }, null, null, CQRItems.SHIELD_PIGMAN.get()),
	ORC(new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "orc") }, null, null, CQRItems.SHIELD_WARPED.get()),
	GOLEM(new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "golem") }, null, null),
	MUMMY(new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "mummy") }, null, null, CQRItems.SHIELD_MUMMY.get()),
	OGRE(new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "ogre") }, null, null, CQRItems.SHIELD_TOMB.get()),
	GREMLIN(new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "gremlin") }, null, EBanners.GREMLIN_BANNER, CQRItems.SHIELD_RUSTED.get()),
	TRITON(new ResourceLocation[] { new ResourceLocation(CQRMain.MODID, "triton") }, null, null);

	private ResourceLocation[] resLoc;
	private ResourceLocation[] bossResLoc;
	private Item shieldItem;
	private EBanners banner;

	EDefaultInhabitants(ResourceLocation[] resLoc, ResourceLocation[] bossResLoc, EBanners banner, Item shieldItem) {
		this.resLoc = resLoc;
		this.banner = banner;
		this.shieldItem = shieldItem;
		this.bossResLoc = bossResLoc;
	}

	EDefaultInhabitants(ResourceLocation[] resLoc, ResourceLocation[] bossResLoc, EBanners banner) {
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
