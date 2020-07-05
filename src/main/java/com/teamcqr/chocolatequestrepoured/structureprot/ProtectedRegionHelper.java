package com.teamcqr.chocolatequestrepoured.structureprot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.reflection.ReflectionField;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ProtectedRegionHelper {

	private static final Set<Block> BREAKABLE_BLOCK_WHITELIST = new HashSet<>();
	private static final Set<Block> PLACEABLE_BLOCK_WHITELIST = new HashSet<>();
	private static final ReflectionField<Explosion, Entity> EXPLODER_FIELD = new ReflectionField<>(Explosion.class, "field_77283_e", "exploder");

	private ProtectedRegionHelper() {

	}

	public static void updateBreakableBlockWhitelist() {
		BREAKABLE_BLOCK_WHITELIST.clear();
		for (String s : CQRConfig.advanced.protectionSystemBreakableBlockWhitelist) {
			Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
			if (b != null) {
				BREAKABLE_BLOCK_WHITELIST.add(b);
			}
		}
	}

	public static void updatePlaceableBlockWhitelist() {
		PLACEABLE_BLOCK_WHITELIST.clear();
		for (String s : CQRConfig.advanced.protectionSystemPlaceableBlockWhitelist) {
			Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
			if (b != null) {
				PLACEABLE_BLOCK_WHITELIST.add(b);
			}
		}
	}

	public static boolean isBlockBreakingPrevented(World world, BlockPos pos, @Nullable EntityPlayer player) {
		if (!CQRConfig.advanced.protectionSystemFeatureEnabled) {
			return false;
		}

		if (player != null && player.isCreative()) {
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
			if (protectedRegion.preventBlockBreaking() && protectedRegion.isInsideProtectedRegion(pos)) {
				return true;
			}
		}

		return false;
	}

	public static boolean isBlockPlacingPrevented(World world, BlockPos pos, @Nullable EntityPlayer player, Block block) {
		if (!CQRConfig.advanced.protectionSystemFeatureEnabled) {
			return false;
		}

		if (player != null && player.isCreative()) {
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
			if (protectedRegion.preventBlockPlacing() && protectedRegion.isInsideProtectedRegion(pos)) {
				return true;
			}
		}

		return false;
	}

	public static boolean isBlockPlacingPrevented(World world, BlockPos pos, @Nullable EntityPlayer player, ItemStack stack) {
		if (stack.isEmpty() || !(stack.getItem() instanceof ItemBlock)) {
			return false;
		}

		return isBlockPlacingPrevented(world, pos, player, ((ItemBlock) stack.getItem()).getBlock());
	}

	public static boolean isExplosionTNTPrevented(World world, BlockPos pos, @Nullable BlockPos origin, boolean checkForOrigin) {
		if (!CQRConfig.advanced.protectionSystemFeatureEnabled) {
			return false;
		}

		ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager == null) {
			return false;
		}

		for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
			if (protectedRegion.preventExplosionsTNT() && (protectedRegion.isInsideProtectedRegion(pos) || (checkForOrigin && origin != null && protectedRegion.isInsideProtectedRegion(origin)))) {
				return true;
			}
		}

		return false;
	}

	public static boolean isExplosionOtherPrevented(World world, BlockPos pos, @Nullable BlockPos origin, boolean checkForOrigin) {
		if (!CQRConfig.advanced.protectionSystemFeatureEnabled) {
			return false;
		}

		ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager == null) {
			return false;
		}

		for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
			if (protectedRegion.preventExplosionsOther() && (protectedRegion.isInsideProtectedRegion(pos) || (checkForOrigin && origin != null && protectedRegion.isInsideProtectedRegion(origin)))) {
				return true;
			}
		}

		return false;
	}

	public static void removeExplosionPreventedPositions(World world, Explosion explosion, boolean checkForOrigin) {
		if (!CQRConfig.advanced.protectionSystemFeatureEnabled) {
			return;
		}

		ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager == null) {
			return;
		}

		Entity exploder = EXPLODER_FIELD.get(explosion);
		boolean flag = exploder instanceof EntityTNTPrimed;
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
		if (!CQRConfig.advanced.protectionSystemFeatureEnabled) {
			return false;
		}

		ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager == null) {
			return false;
		}

		for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
			if (protectedRegion.preventFireSpreading() && (protectedRegion.isInsideProtectedRegion(pos) || (checkForOrigin && origin != null && protectedRegion.isInsideProtectedRegion(origin)))) {
				return true;
			}
		}

		return false;
	}

	public static boolean isEntitySpawningPrevented(World world, BlockPos pos) {
		if (!CQRConfig.advanced.protectionSystemFeatureEnabled) {
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
