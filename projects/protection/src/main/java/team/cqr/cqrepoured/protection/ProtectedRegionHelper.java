package team.cqr.cqrepoured.protection;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import team.cqr.cqrepoured.common.services.CQRServices;
import team.cqr.cqrepoured.protection.init.CQRProtectionBlockTags;
import team.cqr.cqrepoured.protection.init.CQRProtectionEntityTags;

public class ProtectedRegionHelper {

	private ProtectedRegionHelper() {

	}

	public static boolean isBlockBreakingPrevented(Level world, BlockPos pos, @Nullable Entity entity, boolean updateProtectedRegions, boolean addOrResetProtectedRegionIndicator) {
		return isBlockBreakingPrevented(world, pos, entity, updateProtectedRegions, addOrResetProtectedRegionIndicator, null);
	}

	public static boolean isBlockBreakingPrevented(Level world, BlockPos pos, @Nullable Entity entity, boolean updateProtectedRegions, boolean addOrResetProtectedRegionIndicator, @Nullable Direction clickedFace) {
		IProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager == null) {
			return false;
		}

		List<ProtectedRegion> protectedRegions = manager.getProtectedRegionsAt(pos).collect(Collectors.toList());

		if (protectedRegions.isEmpty()) {
			return false;
		}

		boolean isBlockDependency = false;

		for (ProtectedRegion protectedRegion : protectedRegions) {
			if (protectedRegion.isBlockDependency(pos)) {
				if (updateProtectedRegions) {
					protectedRegion.removeBlockDependency(pos);
				}
				isBlockDependency = true;
			}
		}

		boolean isBreakingPrevented = false;

		if (!isBlockDependency && CQRServices.CONFIG.protectionConfig().protectionSystemEnabled() && CQRServices.CONFIG.protectionConfig().preventBlockBreakingInActiveRegions() && (!(entity instanceof Player) || !((Player) entity).isCreative()) && !isBlockBreakingWhitelisted(world.getBlockState(pos))) {
			for (ProtectedRegion protectedRegion : protectedRegions) {
				if (protectedRegion.preventBlockBreaking() && !protectedRegion.isBreakable(pos)) {
					if (addOrResetProtectedRegionIndicator) {
						ProtectionIndicatorHelper.addOrResetProtectedRegionIndicator(world, protectedRegion.uuid(), protectedRegion.boundingBox(), pos, entity instanceof ServerPlayer ? (ServerPlayer) entity : null);
					}
					isBreakingPrevented = true;
					break;
				}
			}
		}

		if (updateProtectedRegions && !isBreakingPrevented) {
			for (ProtectedRegion protectedRegion : protectedRegions) {
				protectedRegion.setProtectionState(pos, ProtectionState.PROTECTED);
			}
		}

		return isBreakingPrevented;
	}

	private static boolean isBlockBreakingWhitelisted(BlockState state) {
		return state.is(CQRProtectionBlockTags.BREAKABLE_IN_ACTIVE_REGION);
	}

	public static boolean isBlockPlacingPrevented(Level world, BlockPos pos, @Nullable Entity entity, BlockState state, boolean updateProtectedRegions, boolean addOrResetProtectedRegionIndicator) {
		IProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager == null) {
			return false;
		}

		List<ProtectedRegion> protectedRegions = manager.getProtectedRegionsAt(pos).collect(Collectors.toList());

		if (protectedRegions.isEmpty()) {
			return false;
		}

		boolean isPlacingPrevented = false;

		if (CQRServices.CONFIG.protectionConfig().protectionSystemEnabled() && CQRServices.CONFIG.protectionConfig().preventBlockPlacingInActiveRegions() && (!(entity instanceof Player) || !((Player) entity).isCreative()) && !isBlockPlacingWhitelisted(state)) {
			for (ProtectedRegion protectedRegion : protectedRegions) {
				if (protectedRegion.preventBlockPlacing()) {
					if (addOrResetProtectedRegionIndicator) {
						ProtectionIndicatorHelper.addOrResetProtectedRegionIndicator(world, protectedRegion.uuid(), protectedRegion.boundingBox(), pos, entity instanceof ServerPlayer ? (ServerPlayer) entity : null);
					}
					isPlacingPrevented = true;
					break;
				}
			}
		}

		if (updateProtectedRegions && !isPlacingPrevented) {
			for (ProtectedRegion protectedRegion : protectedRegions) {
				protectedRegion.setProtectionState(pos, ProtectionState.UNPROTECTED_PLAYER_PLACED);
			}
		}

		return isPlacingPrevented;
	}

	private static boolean isBlockPlacingWhitelisted(BlockState state) {
		return state.is(CQRProtectionBlockTags.PLACABLE_IN_ACTIVE_REGION);
	}

	public static BlockState getBlockFromItem(ItemStack stack, Level world, BlockPos pos, Direction facing, @Nullable Vec3 hitVec, LivingEntity placer, InteractionHand hand) {
		if (stack.isEmpty()) {
			return null;
		}
		Item item = stack.getItem();
		if (item instanceof BlockItem) {
			Block block = ((BlockItem) item).getBlock();
			if (hitVec == null) {
				return block.defaultBlockState();
			}
			/*if (!block.canPlaceBlockOnSide(world, pos, facing)) {
				return block.defaultBlockState();
			}
			int meta = ((BlockItem) item).getMetadata(stack.getItemDamage());
			return block.getStateForPlacement(world, pos, facing, (float) hitVec.x, (float) hitVec.y, (float) hitVec.z, meta, placer, hand);*/
		}
		Optional<FluidStack> opFluidStack = FluidUtil.getFluidContained(stack);
		if(opFluidStack.isPresent()) {
			FluidStack fluidStack = opFluidStack.get();
			/*if (fluidStack.getAmount() != 0 && fluidStack.getFluid() != null && fluidStack.getFluid().canBePlacedInWorld()) {
				return fluidStack.getFluid().getBlock().getDefaultState();
			}*/
		}
		return null;
	}

	public static boolean isExplosionTNTPrevented(Level world, BlockPos pos, @Nullable BlockPos origin, boolean checkForOrigin) {
		if (!CQRServices.CONFIG.protectionConfig().protectionSystemEnabled() || !CQRServices.CONFIG.protectionConfig().preventTNTExplosionsInActiveRegions()) {
			return false;
		}

		IProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager == null) {
			return false;
		}

		for (ProtectedRegion protectedRegion : manager.getProtectedRegions().collect(Collectors.toList())) {
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

	public static boolean isExplosionOtherPrevented(Level world, BlockPos pos, @Nullable BlockPos origin, boolean checkForOrigin) {
		if (!CQRServices.CONFIG.protectionConfig().protectionSystemEnabled() || !CQRServices.CONFIG.protectionConfig().preventOtherExplosionsInActiveRegions()) {
			return false;
		}

		IProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager == null) {
			return false;
		}

		for (ProtectedRegion protectedRegion : manager.getProtectedRegions().collect(Collectors.toList())) {
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

	public static void removeExplosionPreventedPositions(Level world, Explosion explosion, boolean checkForOrigin) {
		IProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager == null) {
			return;
		}

		List<BlockPos> affectedBlockPositions = explosion.getToBlow();

		if (affectedBlockPositions.isEmpty()) {
			return;
		}

		// If the exploder is our own custom tnt => let it blow!
		if (explosion.getExploder().getType().is(CQRProtectionEntityTags.IGNORED_EXPLOSION_SOURCE)) {
			return;
		}

		boolean flag = explosion.getExploder() instanceof PrimedTnt;
		boolean flag1 = (flag && CQRServices.CONFIG.protectionConfig().preventTNTExplosionsInActiveRegions()) || (!flag && CQRServices.CONFIG.protectionConfig().preventOtherExplosionsInActiveRegions());
		boolean flag2 = CQRServices.CONFIG.protectionConfig().protectionSystemEnabled() && flag1;

		if (flag2) {
			BlockPos pos = BlockPos.containing(explosion.getPosition());

			for (ProtectedRegion protectedRegion : manager.getProtectedRegions().collect(Collectors.toList())) {
				if ((flag && protectedRegion.preventExplosionsTNT()) || (!flag && protectedRegion.preventExplosionsOther())) {
					if (checkForOrigin && protectedRegion.isInsideProtectedRegion(pos)) {
						affectedBlockPositions.clear();
					} else {
						for (int i = 0; i < affectedBlockPositions.size(); i++) {
							BlockPos pos1 = affectedBlockPositions.get(i);
							if (protectedRegion.isInsideProtectedRegion(pos1)) {
								affectedBlockPositions.remove(i--);
							}
						}
					}
				}

				if (affectedBlockPositions.isEmpty()) {
					return;
				}
			}
		}
		
		// TODO: What was the purpose of this??
		for (ProtectedRegion protectedRegion : manager.getProtectedRegions().collect(Collectors.toList())) {
			for (BlockPos pos1 : affectedBlockPositions) {
				protectedRegion.setProtectionState(pos1, ProtectionState.PROTECTED);
			}
		}
	}

	public static boolean isFireSpreadingPrevented(Level world, BlockPos pos, @Nullable BlockPos origin, boolean checkForOrigin) {
		if (!CQRServices.CONFIG.protectionConfig().protectionSystemEnabled() || !CQRServices.CONFIG.protectionConfig().preventFireSpreadingInActiveRegions()) {
			return false;
		}

		IProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager == null) {
			return false;
		}

		for (ProtectedRegion protectedRegion : manager.getProtectedRegions().collect(Collectors.toList())) {
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

	public static boolean isEntitySpawningPrevented(Level world, BlockPos pos) {
		if (!CQRServices.CONFIG.protectionConfig().protectionSystemEnabled() || !CQRServices.CONFIG.protectionConfig().preventEntitySpawningInActiveRegions()) {
			return false;
		}

		IProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager == null) {
			return false;
		}

		for (ProtectedRegion protectedRegion : manager.getProtectedRegions().collect(Collectors.toList())) {
			if (protectedRegion.preventEntitySpawning() && protectedRegion.isInsideProtectedRegion(pos)) {
				return true;
			}
		}

		return false;
	}

	public static <T extends Entity> Set<T> getEntitiesInProtectedRegionAt(Class<T> entityClass, BlockPos position, Level world) {
		Set<T> set = new HashSet<>();

		IProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager != null && manager instanceof ServerProtectedRegionManager) {
			for (ProtectedRegion region : manager.getProtectedRegionsAt(position).collect(Collectors.toList())) {
				AABB aabb = new AABB(region.boundingBox().minX(), region.boundingBox().minY(), region.boundingBox().minZ(), region.boundingBox().maxX(), region.boundingBox().maxY(), region.boundingBox().maxZ());
				set.addAll(world.getEntitiesOfClass(entityClass, aabb));
			}
		}

		return set;
	}

}
