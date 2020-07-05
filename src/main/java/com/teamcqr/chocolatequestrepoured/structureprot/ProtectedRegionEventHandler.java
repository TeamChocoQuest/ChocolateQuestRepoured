package com.teamcqr.chocolatequestrepoured.structureprot;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.SPacketSyncProtectedRegions;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
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

@EventBusSubscriber(modid = Reference.MODID)
public class ProtectedRegionEventHandler {

	/*
	 * Possible other events to use (do not delete): PlayerInteractEvent.LeftClickBlock PlayerInteractEvent.RightClickBlock
	 * PlayerEvent.BreakSpeed
	 */

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
		if (ProtectedRegionHelper.isBlockBreakingPrevented(event.getWorld(), event.getPos(), event.getEntityPlayer())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onPlayerRightClickBlockEvent(PlayerInteractEvent.RightClickBlock event) {
		if (ProtectedRegionHelper.isBlockPlacingPrevented(event.getWorld(), event.getPos(), event.getEntityPlayer(), event.getItemStack())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onExplosionEventDetonate(ExplosionEvent.Detonate event) {
		ProtectedRegionHelper.removeExplosionPreventedPositions(event.getWorld(), event.getExplosion(), true);
	}

	@SubscribeEvent
	public static void onWorldEventPotentialSpawns(WorldEvent.PotentialSpawns event) {
		if (event.getList().isEmpty()) {
			return;
		}

		if (ProtectedRegionHelper.isEntitySpawningPrevented(event.getWorld(), event.getPos())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onBlockBreakEvent(BlockEvent.BreakEvent event) {
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

	@SubscribeEvent
	public static void onLivingDeathEvent(LivingDeathEvent event) {
		Entity entity = event.getEntity();
		World world = entity.world;
		UUID uuid = entity.getPersistentID();
		ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager != null) {
			for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
				if (protectedRegion.isEntityDependency(uuid)) {
					protectedRegion.removeEntityDependency(uuid);
				}
			}
		}
	}

}
