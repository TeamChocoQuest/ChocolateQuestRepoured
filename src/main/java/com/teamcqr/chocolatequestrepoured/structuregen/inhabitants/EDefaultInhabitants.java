package com.teamcqr.chocolatequestrepoured.structuregen.inhabitants;

import java.util.Random;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.banners.EBanners;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public enum EDefaultInhabitants {

	DEFAULT(null, null, null),
	// DONT_REPLACE(null),
	DWARF(new ResourceLocation(Reference.MODID, "dwarf"), null, null, ModItems.SHIELD_CARL),
	SKELETON(new ResourceLocation(Reference.MODID, "skeleton"), new ResourceLocation(Reference.MODID, "necromancer"), EBanners.SKELETON_BANNER, ModItems.SHIELD_SKELETON_FRIENDS),
	ZOMBIE(new ResourceLocation(Reference.MODID, "zombie"), new ResourceLocation(Reference.MODID, "lich"), null, ModItems.SHIELD_ZOMBIE),
	PIRATE(new ResourceLocation(Reference.MODID, "pirate"), new ResourceLocation(Reference.MODID, "pirate_captain"), EBanners.PIRATE_BANNER, ModItems.SHIELD_PIRATE2),
	ILLAGER(new ResourceLocation(Reference.MODID, "illager"), null, EBanners.ILLAGER_BANNER),
	WALKER(new ResourceLocation(Reference.MODID, "walker"), new ResourceLocation(Reference.MODID, "walker_king"), EBanners.WALKER_ORDO, ModItems.SHIELD_WALKER),
	SPECTER(new ResourceLocation(Reference.MODID, "spectre"), null, null, ModItems.SHIELD_SPECTER),
	ENDERMAN(new ResourceLocation(Reference.MODID, "enderman"), null, EBanners.ENDERMEN_BANNER),
	BOARMAN(new ResourceLocation(Reference.MODID, "boarman"), new ResourceLocation(Reference.MODID, "boar_mage"), EBanners.PIGMAN_BANNER, ModItems.SHIELD_FIRE),
	MINOTAUR(new ResourceLocation(Reference.MODID, "minotaur"), null, null, ModItems.SHIELD_PIGMAN),
	ORC(new ResourceLocation(Reference.MODID, "orc"), null, null, ModItems.SHIELD_WARPED),
	GOLEM(new ResourceLocation(Reference.MODID, "golem"), null, null),
	GOBLIN(new ResourceLocation(Reference.MODID, "goblin"), null, null, ModItems.SHIELD_GOBLIN),
	MUMMY(new ResourceLocation(Reference.MODID, "mummy"), null, null, ModItems.SHIELD_MUMMY),
	OGRE(new ResourceLocation(Reference.MODID, "ogre"), null, null, ModItems.SHIELD_TOMB),
	GREMLIN(new ResourceLocation(Reference.MODID, "gremlin"), null, EBanners.GREMLIN_BANNER, ModItems.SHIELD_RUSTED),
	TRITON(new ResourceLocation(Reference.MODID, "triton"), null, null);

	private static final Random RAND = new Random();
	private static final EDefaultInhabitants[][] MOB_WHEEL = new EDefaultInhabitants[][] {
			new EDefaultInhabitants[] { SKELETON },
			new EDefaultInhabitants[] { ZOMBIE, MUMMY },
			new EDefaultInhabitants[] { ILLAGER },
			// new EDungeonMobType[] {GOBLIN, ORC, OGRE},
			new EDefaultInhabitants[] { SPECTER },
			new EDefaultInhabitants[] { MINOTAUR },
			/*new EDungeonMobType[] { ENDERMAN }, */};

	private ResourceLocation resLoc;
	private ResourceLocation bossResLoc;
	private Item shieldItem;
	private EBanners banner;

	private EDefaultInhabitants(ResourceLocation resLoc, ResourceLocation bossResLoc, EBanners banner, Item shieldItem) {
		this.resLoc = resLoc;
		this.banner = banner;
		this.shieldItem = shieldItem;
		this.bossResLoc = bossResLoc;
	}
	
	private EDefaultInhabitants(ResourceLocation resLoc, ResourceLocation bossResLoc, EBanners banner) {
		this.resLoc = resLoc;
		this.banner = banner;
		this.shieldItem = Items.SHIELD;
		this.bossResLoc = bossResLoc;
	}

	public ResourceLocation getEntityResourceLocation() {
		return this.resLoc;
	}

	public ResourceLocation getBossResourceLocation() {
		return this.bossResLoc;
	}

	public EBanners getBanner() {
		return this.banner;
	}
	
	public ItemStack getShieldItem() {
		return new ItemStack(this.shieldItem, 1);
	}

	/**
	 * The parameters x and z are block coordinates!<br>
	 * <br>
	 * Mob spawns depending on distance: Raising from near to far:<br>
	 * Skeleton<br>
	 * Zombie/Mummy<br>
	 * Illager<br>
	 * (Goblin/Orc/Ogre currently disabled)<br>
	 * Specter<br>
	 * Minotaur<br>
	 * Endermen<br>
	 */
	public static EDefaultInhabitants getMobTypeDependingOnDistance(World world, int x, int z) {
		BlockPos spawnPoint = world.getSpawnPoint();
		int x1 = x - spawnPoint.getX();
		int z1 = z - spawnPoint.getZ();
		int distToSpawn = (int) Math.sqrt((double) (x1 * x1 + z1 * z1));
		int index = distToSpawn / CQRConfig.mobs.mobTypeChangeDistance;

		if (index >= MOB_WHEEL.length) {
			index = RAND.nextInt(MOB_WHEEL.length);
		}

		return MOB_WHEEL[index][RAND.nextInt(MOB_WHEEL[index].length)];
	}

	@Nullable
	public static EDefaultInhabitants byString(String name) {
		for (EDefaultInhabitants e : EDefaultInhabitants.values()) {
			if (e.name().equalsIgnoreCase(name)) {
				return e;
			}
		}
		return null;
	}

}
