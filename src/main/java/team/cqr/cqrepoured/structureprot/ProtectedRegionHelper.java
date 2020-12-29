package team.cqr.cqrepoured.structureprot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import team.cqr.cqrepoured.client.structureprot.ProtectedRegionClientEventHandler;
import team.cqr.cqrepoured.util.CQRConfig;
import team.cqr.cqrepoured.util.reflection.ReflectionField;

public class ProtectedRegionHelper {

	public static final Set<Block> BREAKABLE_BLOCK_WHITELIST = new HashSet<>();
	public static final Set<Material> BREAKABLE_MATERIAL_WHITELIST = new HashSet<>();
	public static final Set<Block> PLACEABLE_BLOCK_WHITELIST = new HashSet<>();
	public static final Set<Material> PLACEABLE_MATERIAL_WHITELIST = new HashSet<>();

	private static final ReflectionField<Entity> EXPLODER_FIELD = new ReflectionField<>(Explosion.class, "field_77283_e", "exploder");

	private ProtectedRegionHelper() {

	}

	public static void updateWhitelists() {
		BREAKABLE_BLOCK_WHITELIST.clear();
		BREAKABLE_MATERIAL_WHITELIST.clear();
		PLACEABLE_BLOCK_WHITELIST.clear();
		PLACEABLE_MATERIAL_WHITELIST.clear();
		for (String s : CQRConfig.dungeonProtection.protectionSystemBreakableBlockWhitelist) {
			Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
			if (b != null) {
				BREAKABLE_BLOCK_WHITELIST.add(b);
			}
		}
		for (String s : CQRConfig.dungeonProtection.protectionSystemBreakableMaterialWhitelist) {
			try {
				BREAKABLE_MATERIAL_WHITELIST.add((Material) Material.class.getField(s.toUpperCase()).get(null));
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | ClassCastException e) {
				// ignore
			}
		}
		for (String s : CQRConfig.dungeonProtection.protectionSystemPlaceableBlockWhitelist) {
			Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
			if (b != null) {
				PLACEABLE_BLOCK_WHITELIST.add(b);
			}
		}
		for (String s : CQRConfig.dungeonProtection.protectionSystemPlaceableMaterialWhitelist) {
			try {
				PLACEABLE_MATERIAL_WHITELIST.add((Material) Material.class.getField(s.toUpperCase()).get(null));
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | ClassCastException e) {
				// ignore
			}
		}
	}

	public static boolean isBlockBreakingPrevented(World world, BlockPos pos, @Nullable Entity entity, boolean updateProtectedRegions, boolean addOrResetProtectedRegionIndicator) {
		IProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager == null) {
			return false;
		}

		List<ProtectedRegion> protectedRegions = manager.getProtectedRegionsAt(pos);

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

		if (!isBlockDependency
				&& CQRConfig.dungeonProtection.protectionSystemEnabled
				&& CQRConfig.dungeonProtection.preventBlockBreaking
				&& (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isCreative())
				&& !isBlockBreakingWhitelisted(world, pos)) {
			for (ProtectedRegion protectedRegion : protectedRegions) {
				if (protectedRegion.preventBlockBreaking() && !protectedRegion.isBreakable(pos)) {
					if (addOrResetProtectedRegionIndicator) {
						ProtectedRegionClientEventHandler.addOrResetProtectedRegionIndicator(world, protectedRegion.getUuid(), protectedRegion.getStartPos(), protectedRegion.getEndPos(), pos, entity instanceof EntityPlayerMP ? (EntityPlayerMP) entity : null);
					}
					isBreakingPrevented = true;
					break;
				}
			}
		}

		if (updateProtectedRegions && !isBreakingPrevented) {
			for (ProtectedRegion protectedRegion : protectedRegions) {
				protectedRegion.setProtectionState(pos, 0);
			}
		}

		return isBreakingPrevented;
	}

	private static boolean isBlockBreakingWhitelisted(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if (BREAKABLE_BLOCK_WHITELIST.contains(state.getBlock())) {
			return true;
		}
		if (BREAKABLE_MATERIAL_WHITELIST.contains(state.getMaterial())) {
			return true;
		}
		if (CQRConfig.dungeonProtection.protectionSystemReplaceableBlocksWhitelisted) {
			return state.getBlock().isReplaceable(world, pos);
		}
		return false;
	}

	public static boolean isBlockPlacingPrevented(World world, BlockPos pos, @Nullable Entity entity, IBlockState state, boolean updateProtectedRegions, boolean addOrResetProtectedRegionIndicator) {
		IProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager == null) {
			return false;
		}

		List<ProtectedRegion> protectedRegions = manager.getProtectedRegionsAt(pos);

		if (protectedRegions.isEmpty()) {
			return false;
		}

		boolean isPlacingPrevented = false;

		if (CQRConfig.dungeonProtection.protectionSystemEnabled
				&& CQRConfig.dungeonProtection.preventBlockPlacing
				&& (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isCreative())
				&& !isBlockPlacingWhitelisted(state)) {
			for (ProtectedRegion protectedRegion : protectedRegions) {
				if (protectedRegion.preventBlockPlacing()) {
					if (addOrResetProtectedRegionIndicator) {
						ProtectedRegionClientEventHandler.addOrResetProtectedRegionIndicator(world, protectedRegion.getUuid(), protectedRegion.getStartPos(), protectedRegion.getEndPos(), pos, entity instanceof EntityPlayerMP ? (EntityPlayerMP) entity : null);
					}
					isPlacingPrevented = true;
					break;
				}
			}
		}

		if (updateProtectedRegions && !isPlacingPrevented) {
			for (ProtectedRegion protectedRegion : protectedRegions) {
				protectedRegion.setProtectionState(pos, 1);
			}
		}

		return isPlacingPrevented;
	}

	private static boolean isBlockPlacingWhitelisted(IBlockState state) {
		if (PLACEABLE_BLOCK_WHITELIST.contains(state.getBlock())) {
			return true;
		}
		return PLACEABLE_MATERIAL_WHITELIST.contains(state.getMaterial());
	}

	public static boolean isBlockPlacingPrevented(World world, BlockPos pos, @Nullable Entity entity, ItemStack stack, boolean updateProtectedRegions, boolean addOrResetProtectedRegionIndicator) {
		IBlockState state = getBlockFromItem(stack);
		if (state == null) {
			return false;
		}
		return isBlockPlacingPrevented(world, pos, entity, state, updateProtectedRegions, addOrResetProtectedRegionIndicator);
	}

	@SuppressWarnings("deprecation")
	private static IBlockState getBlockFromItem(ItemStack stack) {
		if (stack.isEmpty()) {
			return null;
		}
		Item item = stack.getItem();
		if (item instanceof ItemBlock) {
			return ((ItemBlock) item).getBlock().getStateFromMeta(((ItemBlock) item).getMetadata(stack.getItemDamage()));
		}
		FluidStack fluidStack = FluidUtil.getFluidContained(stack);
		if (fluidStack != null && fluidStack.amount != 0 && fluidStack.getFluid() != null) {
			return fluidStack.getFluid().getBlock().getDefaultState();
		}
		return null;
	}

	public static boolean isExplosionTNTPrevented(World world, BlockPos pos, @Nullable BlockPos origin, boolean checkForOrigin) {
		if (!CQRConfig.dungeonProtection.protectionSystemEnabled || !CQRConfig.dungeonProtection.preventExplosionTNT) {
			return false;
		}

		IProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

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

		IProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

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
		IProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager == null) {
			return;
		}

		List<BlockPos> affectedBlockPositions = explosion.getAffectedBlockPositions();

		if (affectedBlockPositions.isEmpty()) {
			return;
		}

		Entity exploder = EXPLODER_FIELD.get(explosion);
		boolean flag = exploder instanceof EntityTNTPrimed;
		boolean flag1 = (flag && CQRConfig.dungeonProtection.preventExplosionTNT) || (!flag && CQRConfig.dungeonProtection.preventExplosionOther);
		boolean flag2 = CQRConfig.dungeonProtection.protectionSystemEnabled && flag1;

		if (flag2) {
			BlockPos pos = new BlockPos(explosion.getPosition());

			for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
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

		for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
			for (BlockPos pos1 : affectedBlockPositions) {
				protectedRegion.setProtectionState(pos1, 0);
			}
		}
	}

	public static boolean isFireSpreadingPrevented(World world, BlockPos pos, @Nullable BlockPos origin, boolean checkForOrigin) {
		if (!CQRConfig.dungeonProtection.protectionSystemEnabled || !CQRConfig.dungeonProtection.preventFireSpreading) {
			return false;
		}

		IProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

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

		IProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

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
