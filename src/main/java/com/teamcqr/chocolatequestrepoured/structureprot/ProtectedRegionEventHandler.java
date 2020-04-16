package com.teamcqr.chocolatequestrepoured.structureprot;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@EventBusSubscriber(modid = Reference.MODID)
public class ProtectedRegionEventHandler {

	private ProtectedRegionEventHandler() {

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
	public static void onBlockEventBreakEvent(BlockEvent.BreakEvent event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);
		EntityPlayer player = event.getPlayer();

		if (!world.isRemote && manager != null && !event.isCanceled() && !player.isCreative()) {
			boolean isBlockDependency = false;
			for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
				if (protectedRegion.isBlockDependency(pos)) {
					protectedRegion.removeBlockDependency(pos);
					isBlockDependency = true;
				}
			}
			if (!isBlockDependency) {
				for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
					if (protectedRegion.preventBlockBreaking() && protectedRegion.isInsideProtectedRegion(pos)) {
						event.setCanceled(true);
						return;
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onBlockEventEntityPlaceEvent(BlockEvent.EntityPlaceEvent event) {
		World world = event.getWorld();
		Entity entity = event.getEntity();
		BlockPos pos = event.getPos();
		ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (!world.isRemote && manager != null && !event.isCanceled() && (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isCreative())) {
			for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
				if (protectedRegion.preventBlockPlacing() && protectedRegion.isInsideProtectedRegion(pos)) {
					event.setCanceled(true);
					return;
				}
			}
		}
	}

	@SubscribeEvent
	public static void onExplosionEventDetonate(ExplosionEvent.Detonate event) {
		World world = event.getWorld();
		Explosion explosion = event.getExplosion();
		boolean isTNT = ProtectedRegionEventHandler.getExploder(explosion) instanceof EntityTNTPrimed;
		ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (!world.isRemote && manager != null && !event.isCanceled()) {
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

	// Disabled for now
	// @SubscribeEvent
	public static void onTickEventWorldTickEvent(TickEvent.WorldTickEvent event) {
		World world = event.world;

		if (!world.isRemote) {
			ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

			if (manager != null) {
				ChunkProviderServer chunkProvider = (ChunkProviderServer) world.getChunkProvider();
				for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
					if (protectedRegion.preventFireSpreading() && world.getWorldType() != WorldType.DEBUG_ALL_BLOCK_STATES) {
						BlockPos startPos = protectedRegion.getStartPos();
						BlockPos endPos = protectedRegion.getEndPos();
						int endX = endPos.getX();
						int endY = endPos.getY();
						int endZ = endPos.getZ();
						int oldChunkX = startPos.getX() >> 4;
						int oldChunkY = startPos.getY() >> 4;
						int oldChunkZ = startPos.getZ() >> 4;
						boolean isLoaded = world.isBlockLoaded(startPos);
						// boolean isLoaded = chunkProvider.chunkExists(oldChunkX, oldChunkZ);
						Chunk chunk = null;
						ExtendedBlockStorage extendedBlockStorage = Chunk.NULL_BLOCK_STORAGE;
						if (isLoaded) {
							chunk = world.getChunkFromChunkCoords(oldChunkX, oldChunkZ);
							// chunk = chunkProvider.getLoadedChunk(oldChunkX, oldChunkZ);
							extendedBlockStorage = chunk.getBlockStorageArray()[oldChunkY >> 4];
						}
						for (int x = startPos.getX(); x <= endPos.getX(); x++) {
							int chunkX = x >> 4;

							for (int z = startPos.getZ(); z <= endPos.getZ(); z++) {
								int chunkZ = z >> 4;

								if (chunkX != oldChunkX || chunkZ != oldChunkZ) {
									oldChunkX = chunkX;
									oldChunkZ = chunkZ;
									isLoaded = world.isBlockLoaded(new BlockPos(x, 0, z));
									// isLoaded = chunkProvider.chunkExists(chunkX, chunkZ);
									if (isLoaded) {
										chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
										// chunk = chunkProvider.getLoadedChunk(chunkX, chunkZ);
										extendedBlockStorage = chunk.getBlockStorageArray()[startPos.getY() >> 4];
									}
								}

								if (isLoaded) {
									for (int y = startPos.getY(); y <= endPos.getY(); y++) {
										int chunkY = y >> 4;

										if (chunkY != oldChunkY) {
											oldChunkY = chunkY;
											extendedBlockStorage = chunk.getBlockStorageArray()[chunkY];
										}

										if (extendedBlockStorage != Chunk.NULL_BLOCK_STORAGE) {
											if (extendedBlockStorage.get(x & 15, y & 15, z & 15).getBlock() == Blocks.FIRE) {
												world.setBlockToAir(new BlockPos(x, y, z));
											}
										} else {
											y += 15 - (y & 15);
										}
									}
								} else {
									z += 15 - (z & 15);
								}
							}
						}
						/*
						 * for (BlockPos.MutableBlockPos mutableBlockPos : BlockPos.getAllInBoxMutable(protectedRegion.getStartPos(), protectedRegion.getEndPos())) {
						 * if (world.isBlockLoaded(mutableBlockPos) && world.getBlockState(mutableBlockPos).getBlock() == Blocks.FIRE) {
						 * world.setBlockToAir(mutableBlockPos.toImmutable());
						 * }
						 * }
						 */
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onWorldEventPotentialSpawns(WorldEvent.PotentialSpawns event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (!world.isRemote && manager != null && !event.isCanceled() && !event.getList().isEmpty()) {
			for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
				if (protectedRegion.preventEntitySpawning() && protectedRegion.isInsideProtectedRegion(pos)) {
					event.setCanceled(true);
					return;
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		Entity entity = event.getEntity();
		World world = entity.world;
		UUID uuid = entity.getPersistentID();
		ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (!world.isRemote && manager != null && !event.isCanceled()) {
			for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
				protectedRegion.removeEntityDependency(uuid);
			}
		}
	}

}
