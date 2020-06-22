package com.teamcqr.chocolatequestrepoured.structureprot;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.SPacketSyncProtectedRegions;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@EventBusSubscriber(modid = Reference.MODID)
public class ProtectedRegionEventHandler {

	/*
	 * Possible other events to use (do not delete): PlayerInteractEvent.LeftClickBlock PlayerInteractEvent.RightClickBlock PlayerEvent.BreakSpeed
	 */

	public static final Set<Block> BREAKABLE_BLOCK_WHITELIST = new HashSet<>();
	public static final Set<Block> PLACEABLE_BLOCK_WHITELIST = new HashSet<>();

	private ProtectedRegionEventHandler() {

	}

	@SubscribeEvent
	public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
		ProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(event.player.world);
		List<ProtectedRegion> protectedRegions = protectedRegionManager != null ? protectedRegionManager.getProtectedRegions() : Collections.emptyList();
		CQRMain.NETWORK.sendTo(new SPacketSyncProtectedRegions(protectedRegions), (EntityPlayerMP) event.player);
	}

	@SubscribeEvent
	public static void onPlayerChangedDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
		ProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(event.player.world);
		List<ProtectedRegion> protectedRegions = protectedRegionManager != null ? protectedRegionManager.getProtectedRegions() : Collections.emptyList();
		CQRMain.NETWORK.sendTo(new SPacketSyncProtectedRegions(protectedRegions), (EntityPlayerMP) event.player);
	}

	@SubscribeEvent
	public static void onWorldCreatedEvent(WorldEvent.CreateSpawnPosition event) {
		ProtectedRegionManager.handleWorldLoad(event.getWorld());
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onWorldLoadEvent(WorldEvent.Load event) {
		ProtectedRegionManager.handleWorldLoad(event.getWorld());
	}

	@SubscribeEvent
	public static void onWorldSaveEvent(WorldEvent.Save event) {
		ProtectedRegionManager.handleWorldSave(event.getWorld());
	}

	@SubscribeEvent
	public static void onWorldUnloadEvent(WorldEvent.Unload event) {
		ProtectedRegionManager.handleWorldUnload(event.getWorld());
	}

	@SubscribeEvent
	public static void onPlayerLeftClickBlockEvent(PlayerInteractEvent.LeftClickBlock event) {
		if (CQRConfig.advanced.protectionSystemFeatureEnabled) {

			if (event.isCanceled()) {
				return;
			}

			World world = event.getWorld();
			EntityPlayer player = event.getEntityPlayer();
			BlockPos pos = event.getPos();
			IBlockState state = world.getBlockState(pos);
			ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

			if (player.isCreative() || BREAKABLE_BLOCK_WHITELIST.contains(state.getBlock())) {
				return;
			}

			if (manager != null) {
				for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
					if (protectedRegion.isBlockDependency(pos)) {
						return;
					}
				}

				for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
					// System.out.println(protectedRegion.preventBlockBreaking() && protectedRegion.isInsideProtectedRegion(pos));
					if (protectedRegion.preventBlockBreaking() && protectedRegion.isInsideProtectedRegion(pos)) {
						event.setCanceled(true);
						return;
					}
				}
			}

		}
	}

	@SubscribeEvent
	public static void onPlayerRightClickBlockEvent(PlayerInteractEvent.RightClickBlock event) {
		if (CQRConfig.advanced.protectionSystemFeatureEnabled) {

			if (event.isCanceled()) {
				return;
			}

			World world = event.getWorld();
			EntityPlayer player = event.getEntityPlayer();
			BlockPos pos = event.getPos();
			ItemStack stack = event.getItemStack();
			ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

			if (player.isCreative() || stack.isEmpty() || !(stack.getItem() instanceof ItemBlock) || PLACEABLE_BLOCK_WHITELIST.contains(((ItemBlock) stack.getItem()).getBlock())) {
				return;
			}

			if (manager != null) {
				for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
					if (protectedRegion.preventBlockPlacing() && protectedRegion.isInsideProtectedRegion(pos)) {
						event.setCanceled(true);
						return;
					}
				}
			}

		}
	}

	@SubscribeEvent
	public static void onExplosionEventDetonate(ExplosionEvent.Detonate event) {
		if (CQRConfig.advanced.protectionSystemFeatureEnabled) {

			if (event.isCanceled()) {
				return;
			}

			World world = event.getWorld();
			Explosion explosion = event.getExplosion();
			boolean isTNT = ProtectedRegionEventHandler.getExploder(explosion) instanceof EntityTNTPrimed;
			ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

			if (manager != null) {
				for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
					if ((isTNT && protectedRegion.preventExplosionsTNT()) || (!isTNT && protectedRegion.preventExplosionsOther())) {
						List<BlockPos> affectedBlockPositions = explosion.getAffectedBlockPositions();

						for (int i = 0; i < affectedBlockPositions.size(); i++) {
							if (protectedRegion.isInsideProtectedRegion(affectedBlockPositions.get(i))) {
								affectedBlockPositions.remove(i--);
							}
						}
					}
				}
			}

		}
	}

	private static Field exploderField = null;

	private static Entity getExploder(Explosion explosion) {
		try {
			if (exploderField == null) {
				try {
					exploderField = Explosion.class.getDeclaredField("field_77283_e");
				} catch (NoSuchFieldException e) {
					exploderField = Explosion.class.getDeclaredField("exploder");
				}
				exploderField.setAccessible(true);
			}
			return (Entity) exploderField.get(explosion);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			CQRMain.logger.error("Failed to get value of Explosion.exploder field", e);
		}
		return null;
	}

	@SubscribeEvent
	public static void onWorldEventPotentialSpawns(WorldEvent.PotentialSpawns event) {
		if (CQRConfig.advanced.protectionSystemFeatureEnabled) {

			if (event.isCanceled() || event.getList().isEmpty()) {
				return;
			}

			World world = event.getWorld();
			BlockPos pos = event.getPos();
			ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

			if (manager != null) {
				for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
					if (protectedRegion.preventEntitySpawning() && protectedRegion.isInsideProtectedRegion(pos)) {
						event.setCanceled(true);
						return;
					}
				}
			}

		}
	}

	@SubscribeEvent
	public static void onBlockBreakEvent(BlockEvent.BreakEvent event) {
		if (CQRConfig.advanced.protectionSystemFeatureEnabled) {

			if (event.isCanceled()) {
				return;
			}

			World world = event.getWorld();
			BlockPos pos = event.getPos();
			ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

			if (manager != null) {
				for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
					if (protectedRegion.isBlockDependency(pos)) {
						protectedRegion.removeBlockDependency(pos);
					}
				}
			}

		}
	}

	@SubscribeEvent
	public static void onLivingDeathEvent(LivingDeathEvent event) {
		Entity entity = event.getEntity();
		World world = entity.world;
		UUID uuid = entity.getPersistentID();
		ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager != null) {
			for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
				protectedRegion.removeEntityDependency(uuid);
			}
		}
	}

	public static void updateBreakableBlockWhitelist() {
		ProtectedRegionEventHandler.BREAKABLE_BLOCK_WHITELIST.clear();
		for (String s : CQRConfig.advanced.protectionSystemBreakableBlockWhitelist) {
			Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
			if (b != null) {
				ProtectedRegionEventHandler.BREAKABLE_BLOCK_WHITELIST.add(b);
			}
		}
	}

	public static void updatePlaceableBlockWhitelist() {
		ProtectedRegionEventHandler.PLACEABLE_BLOCK_WHITELIST.clear();
		for (String s : CQRConfig.advanced.protectionSystemPlaceableBlockWhitelist) {
			Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
			if (b != null) {
				ProtectedRegionEventHandler.PLACEABLE_BLOCK_WHITELIST.add(b);
			}
		}
	}

}
