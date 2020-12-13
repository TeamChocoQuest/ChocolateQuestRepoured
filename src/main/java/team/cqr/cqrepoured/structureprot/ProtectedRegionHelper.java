package team.cqr.cqrepoured.structureprot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import team.cqr.cqrepoured.client.structureprot.ProtectedRegionClientEventHandler;
import team.cqr.cqrepoured.util.CQRConfig;
import team.cqr.cqrepoured.util.reflection.ReflectionField;

public class ProtectedRegionHelper {

	private static final Set<Block> BREAKABLE_BLOCK_WHITELIST = new HashSet<>();
	private static final Set<Block> PLACEABLE_BLOCK_WHITELIST = new HashSet<>();
	private static final ReflectionField<Block> CONTAINED_BLOCK_FIELD = new ReflectionField<>(ItemBucket.class, "field_77876_a", "containedBlock");
	private static final ReflectionField<Entity> EXPLODER_FIELD = new ReflectionField<>(Explosion.class, "field_77283_e", "exploder");

	private ProtectedRegionHelper() {

	}

	public static void updateBreakableBlockWhitelist() {
		BREAKABLE_BLOCK_WHITELIST.clear();
		for (String s : CQRConfig.dungeonProtection.protectionSystemBreakableBlockWhitelist) {
			Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
			if (b != null) {
				BREAKABLE_BLOCK_WHITELIST.add(b);
			}
		}
	}

	public static void updatePlaceableBlockWhitelist() {
		PLACEABLE_BLOCK_WHITELIST.clear();
		for (String s : CQRConfig.dungeonProtection.protectionSystemPlaceableBlockWhitelist) {
			Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
			if (b != null) {
				PLACEABLE_BLOCK_WHITELIST.add(b);
			}
		}
	}

	public static boolean isBlockBreakingPrevented(World world, BlockPos pos, @Nullable Entity entity) {
		return isBlockBreakingPrevented(world, pos, entity, false);
	}

	public static boolean isBlockBreakingPrevented(World world, BlockPos pos, @Nullable Entity entity, boolean addOrResetProtectedRegionIndicator) {
		if (!CQRConfig.dungeonProtection.protectionSystemEnabled || !CQRConfig.dungeonProtection.preventBlockBreaking) {
			return false;
		}

		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative()) {
			return false;
		}

		if (BREAKABLE_BLOCK_WHITELIST.contains(world.getBlockState(pos).getBlock())) {
			return false;
		}

		ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager == null) {
			return false;
		}

		for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
			if (protectedRegion.isBlockDependency(pos)) {
				return false;
			}
		}

		for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
			if (protectedRegion.preventBlockBreaking() && protectedRegion.isProtected(pos)) {
				if (addOrResetProtectedRegionIndicator) {
					ProtectedRegionClientEventHandler.addOrResetProtectedRegionIndicator(protectedRegion, pos, entity instanceof EntityPlayerMP ? (EntityPlayerMP) entity : null);
				}
				return true;
			}
		}

		return false;
	}

	public static boolean isBlockPlacingPrevented(World world, BlockPos pos, @Nullable Entity entity, Block block) {
		return isBlockPlacingPrevented(world, pos, entity, block, false);
	}

	public static boolean isBlockPlacingPrevented(World world, BlockPos pos, @Nullable Entity entity, Block block, boolean addOrResetProtectedRegionIndicator) {
		if (!CQRConfig.dungeonProtection.protectionSystemEnabled || !CQRConfig.dungeonProtection.preventBlockPlacing) {
			return false;
		}

		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative()) {
			return false;
		}

		if (PLACEABLE_BLOCK_WHITELIST.contains(block)) {
			return false;
		}

		ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager == null) {
			return false;
		}

		for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
			if (protectedRegion.preventBlockPlacing() && protectedRegion.isProtected(pos)) {
				if (addOrResetProtectedRegionIndicator) {
					ProtectedRegionClientEventHandler.addOrResetProtectedRegionIndicator(protectedRegion, pos, entity instanceof EntityPlayerMP ? (EntityPlayerMP) entity : null);
				}
				return true;
			}
		}

		return false;
	}

	public static boolean isBlockPlacingPrevented(World world, BlockPos pos, @Nullable Entity entity, ItemStack stack) {
		return isBlockPlacingPrevented(world, pos, entity, stack, false);
	}

	public static boolean isBlockPlacingPrevented(World world, BlockPos pos, @Nullable Entity entity, ItemStack stack, boolean addOrResetProtectedRegionIndicator) {
		if (stack.isEmpty()) {
			return false;
		}
		Block block = getBlockFromItem(stack.getItem());
		if (block == null) {
			return false;
		}
		return isBlockPlacingPrevented(world, pos, entity, block, addOrResetProtectedRegionIndicator);
	}

	private static Block getBlockFromItem(Item item) {
		if (item instanceof ItemBlock) {
			return ((ItemBlock) item).getBlock();
		}
		if (item instanceof ItemBucket) {
			return CONTAINED_BLOCK_FIELD.get((ItemBucket) item);
		}
		return null;
	}

	public static boolean isExplosionTNTPrevented(World world, BlockPos pos, @Nullable BlockPos origin, boolean checkForOrigin) {
		if (!CQRConfig.dungeonProtection.protectionSystemEnabled || !CQRConfig.dungeonProtection.preventExplosionTNT) {
			return false;
		}

		ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager == null) {
			return false;
		}

		for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
			if (protectedRegion.preventExplosionsTNT()) {
				if (protectedRegion.isInsideProtectedRegion(pos)) {
					return true;
				}
				if (checkForOrigin && origin != null && protectedRegion.isInsideProtectedRegion(origin)) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean isExplosionOtherPrevented(World world, BlockPos pos, @Nullable BlockPos origin, boolean checkForOrigin) {
		if (!CQRConfig.dungeonProtection.protectionSystemEnabled || !CQRConfig.dungeonProtection.preventExplosionOther) {
			return false;
		}

		ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager == null) {
			return false;
		}

		for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
			if (protectedRegion.preventExplosionsOther()) {
				if (protectedRegion.isInsideProtectedRegion(pos)) {
					return true;
				}
				if (checkForOrigin && origin != null && protectedRegion.isInsideProtectedRegion(origin)) {
					return true;
				}
			}
		}

		return false;
	}

	public static void removeExplosionPreventedPositions(World world, Explosion explosion, boolean checkForOrigin) {
		if (!CQRConfig.dungeonProtection.protectionSystemEnabled) {
			return;
		}

		ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager == null) {
			return;
		}

		Entity exploder = EXPLODER_FIELD.get(explosion);
		boolean flag = exploder instanceof EntityTNTPrimed;
		if ((flag && !CQRConfig.dungeonProtection.preventExplosionTNT) || (!flag && !CQRConfig.dungeonProtection.preventExplosionOther)) {
			return;
		}
		List<BlockPos> affectedBlockPositions = explosion.getAffectedBlockPositions();
		BlockPos pos = new BlockPos(explosion.getPosition());

		for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
			if (affectedBlockPositions.isEmpty()) {
				break;
			}

			if ((flag && protectedRegion.preventExplosionsTNT()) || (!flag && protectedRegion.preventExplosionsOther())) {
				if (checkForOrigin && protectedRegion.isInsideProtectedRegion(pos)) {
					affectedBlockPositions.clear();
				}

				for (int i = 0; i < affectedBlockPositions.size(); i++) {
					BlockPos pos1 = affectedBlockPositions.get(i);
					if (protectedRegion.isInsideProtectedRegion(pos1)) {
						affectedBlockPositions.remove(i--);
					}
				}
			}
		}
	}

	public static boolean isFireSpreadingPrevented(World world, BlockPos pos, @Nullable BlockPos origin, boolean checkForOrigin) {
		if (!CQRConfig.dungeonProtection.protectionSystemEnabled || !CQRConfig.dungeonProtection.preventFireSpreading) {
			return false;
		}

		ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager == null) {
			return false;
		}

		for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
			if (protectedRegion.preventFireSpreading()) {
				if (protectedRegion.isInsideProtectedRegion(pos)) {
					return true;
				}
				if (checkForOrigin && origin != null && protectedRegion.isInsideProtectedRegion(origin)) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean isEntitySpawningPrevented(World world, BlockPos pos) {
		if (!CQRConfig.dungeonProtection.protectionSystemEnabled || !CQRConfig.dungeonProtection.preventEntitySpawning) {
			return false;
		}

		ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager == null) {
			return false;
		}

		for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
			if (protectedRegion.preventEntitySpawning() && protectedRegion.isInsideProtectedRegion(pos)) {
				return true;
			}
		}

		return false;
	}

}
