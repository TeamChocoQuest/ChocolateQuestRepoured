package team.cqr.cqrepoured.event.world.structure.protection;

import meldexun.reflectionutil.ReflectionField;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.RegionFileCache;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import org.apache.commons.io.FileUtils;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.protectedregions.CapabilityProtectedRegionData;
import team.cqr.cqrepoured.capability.protectedregions.CapabilityProtectedRegionDataProvider;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncProtectedRegions;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncProtectionConfig;
import team.cqr.cqrepoured.util.data.FileIOUtil;
import team.cqr.cqrepoured.world.structure.protection.*;
import team.cqr.cqrepoured.world.structure.protection.ServerProtectedRegionManager.ProtectedRegionContainer;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

//@EventBusSubscriber(modid = CQRMain.MODID)
public class ProtectedRegionEventHandler {

	/*
	 * Possible other events to use (do not delete): PlayerInteractEvent.LeftClickBlock PlayerInteractEvent.RightClickBlock
	 * PlayerEvent.BreakSpeed
	 */

	private ProtectedRegionEventHandler() {

	}

	@SubscribeEvent
	public static void onPlayerLoggedInEvent(PlayerLoggedInEvent event) {
		if (FMLCommonHandler.instance().getSide().isServer() || !CQRMain.PROXY.isOwnerOfIntegratedServer(event.player)) {
			CQRMain.NETWORK.sendTo(new SPacketSyncProtectionConfig(CQRConfig.dungeonProtection), (ServerPlayerEntity) event.player);
		}

		IProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(event.player.world);
		if (protectedRegionManager == null) {
			return;
		}
		syncProtectedRegions(protectedRegionManager, (ServerPlayerEntity) event.player);
	}

	@SubscribeEvent
	public static void onPlayerChangedDimensionEvent(PlayerChangedDimensionEvent event) {
		IProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(event.player.world);
		if (protectedRegionManager == null) {
			return;
		}
		syncProtectedRegions(protectedRegionManager, (ServerPlayerEntity) event.player);
	}

	private static void syncProtectedRegions(IProtectedRegionManager protectedRegionManager, ServerPlayerEntity player) {
		List<ProtectedRegion> list = new ArrayList<>();
		protectedRegionManager.getProtectedRegions().forEach(list::add);
		if (!list.isEmpty()) {
			List<ProtectedRegion> list1 = new ArrayList<>();
			boolean firstPacket = true;
			int sum = 0;
			for (int i = 0; i < list.size(); i++) {
				ProtectedRegion protectedRegion = list.get(i);
				BlockPos size = protectedRegion.getEndPos().subtract(protectedRegion.getStartPos()).add(1, 1, 1);
				int j = size.getX() * size.getY() * size.getZ();
				if (sum + j > 10_000_000) {
					CQRMain.NETWORK.sendTo(new SPacketSyncProtectedRegions(list1, firstPacket), player);
					list1.clear();
					firstPacket = false;
				}
				list1.add(protectedRegion);
				sum += j;
				if (i == list.size() - 1) {
					CQRMain.NETWORK.sendTo(new SPacketSyncProtectedRegions(list1, firstPacket), player);
				}
			}
		} else {
			CQRMain.NETWORK.sendTo(new SPacketSyncProtectedRegions(Collections.emptyList(), true), player);
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static CQRConfig.DungeonProtection cachedProtectionConfig;

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onClientConnectedToServerEvent(ClientConnectedToServerEvent event) {
		if (!Minecraft.getMinecraft().isIntegratedServerRunning()) {
			cachedProtectionConfig = CQRConfig.dungeonProtection;
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onClientDisconnectionFromServerEvent(ClientDisconnectionFromServerEvent event) {
		IProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(Minecraft.getMinecraft().world);
		if (protectedRegionManager == null) {
			return;
		}
		protectedRegionManager.clearProtectedRegions();
		if (!Minecraft.getMinecraft().isIntegratedServerRunning()) {
			CQRConfig.dungeonProtection = cachedProtectionConfig;
			ProtectedRegionHelper.updateWhitelists();
		}
	}

	@SubscribeEvent
	public static void onWorldCreatedEvent(WorldEvent.CreateSpawnPosition event) {
		ProtectedRegionManager.handleWorldLoad(event.getWorld());
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onWorldLoadEvent(WorldEvent.Load event) {
		checkDeprecatedProtectedRegions(event.getWorld());
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
	public static void onChunkUnloadEvent(ChunkEvent.Load event) {
		ProtectedRegionManager.handleChunkLoad(event.getWorld(), event.getChunk());
	}

	@SubscribeEvent
	public static void onChunkUnloadEvent(ChunkEvent.Unload event) {
		ProtectedRegionManager.handleChunkUnload(event.getWorld(), event.getChunk());
	}

	@SubscribeEvent
	public static void onWorldTickEvent(TickEvent.WorldTickEvent event) {
		ProtectedRegionManager.handleWorldTick(event.world);
	}

	@SubscribeEvent
	public static void onBlockBreakEvent(BlockEvent.BreakEvent event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();

		if (ProtectedRegionHelper.isBlockBreakingPrevented(world, pos, event.getPlayer(), true, true)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onBlockPlaceEvent(BlockEvent.EntityPlaceEvent event) {
		if (ProtectedRegionHelper.isBlockPlacingPrevented(event.getWorld(), event.getPos(), event.getEntity(), event.getPlacedBlock(), true, true)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onBucketUseEvent(FillBucketEvent event) {
		RayTraceResult result = event.getTarget();
		if (result == null) {
			return;
		}
		World world = event.getWorld();
		BlockPos pos = result.getBlockPos();
		ItemStack stack = event.getEmptyBucket();
		FluidStack fluidStack = FluidUtil.getFluidContained(stack);
		if (fluidStack == null || fluidStack.amount <= 0 || fluidStack.getFluid() == null || !fluidStack.getFluid().canBePlacedInWorld()) {
			BlockState state = world.getBlockState(pos.offset(result.sideHit));
			if (state.getMaterial().isLiquid() && ProtectedRegionHelper.isBlockBreakingPrevented(event.getWorld(), result.getBlockPos(), event.getEntityPlayer(), true, true)) {
				event.setCanceled(true);
			}
		} else {
			BlockState state = fluidStack.getFluid().getBlock().getDefaultState();
			if (ProtectedRegionHelper.isBlockPlacingPrevented(world, pos.offset(result.sideHit), event.getEntityPlayer(), state, true, true)) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onLeftClickBlockEvent(PlayerInteractEvent.LeftClickBlock event) {
		PlayerEntity player = event.getEntityPlayer();
		BlockPos pos = event.getPos();
		BlockPos offsetPos = pos.offset(event.getFace());

		if (event.getWorld().getBlockState(offsetPos).getBlock() == Blocks.FIRE) {
			if (ProtectedRegionHelper.isBlockBreakingPrevented(player.world, offsetPos, player, false, true, null)) {
				event.setCanceled(true);
			}
		} else if (ProtectedRegionHelper.isBlockBreakingPrevented(player.world, pos, player, false, true, event.getFace())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onRightClickBlockEvent(PlayerInteractEvent.RightClickBlock event) {
		World world = event.getWorld();
		PlayerEntity player = event.getEntityPlayer();
		BlockPos pos = event.getPos();
		Direction facing = event.getFace();
		if (!world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {
			pos = pos.offset(facing);
		}
		ItemStack stack = event.getItemStack();
		Vector3d hitVec = event.getHitVec();
		Hand hand = event.getHand();

		BlockState state = ProtectedRegionHelper.getBlockFromItem(stack, world, pos, facing, hitVec, player, hand);

		if (state == null) {
			return;
		}

		if (ProtectedRegionHelper.isBlockPlacingPrevented(world, pos, player, state, false, true)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onBreakSpeedEvent(PlayerEvent.BreakSpeed event) {
		PlayerEntity player = event.getEntityPlayer();
		BlockPos pos = event.getPos();

		if (ProtectedRegionHelper.isBlockBreakingPrevented(player.world, pos, player, false, true)) {
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
	public static void onLivingDeathEvent(LivingDeathEvent event) {
		Entity entity = event.getEntity();
		World world = entity.world;
		UUID uuid = entity.getPersistentID();
		IProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);

		if (manager != null) {
			for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
				if (protectedRegion.isEntityDependency(uuid)) {
					protectedRegion.removeEntityDependency(uuid);
				}
			}
		}
	}

	@Deprecated
	private static void checkDeprecatedProtectedRegions(World world) {
		if (world.isRemote) {
			return;
		}
		File folder;
		int dim = world.provider.getDimension();
		if (dim == 0) {
			folder = new File(world.getSaveHandler().getWorldDirectory(), "data/CQR/protected_regions");
		} else {
			folder = new File(world.getSaveHandler().getWorldDirectory(), "DIM" + dim + "/data/CQR/protected_regions");
		}
		if (!folder.exists()) {
			return;
		}
		List<File> files = new ArrayList<>(FileUtils.listFiles(folder, new String[] { "nbt" }, false));
		if (files.isEmpty()) {
			return;
		}
		CompoundNBT compound = FileIOUtil.readNBTFromFile(files.get(0));
		if (compound != null && compound.getString("version").equals(ProtectedRegion.PROTECTED_REGION_VERSION)) {
			return;
		}
		boolean flag = ProtectedRegion.logVersionWarnings;
		ProtectedRegion.logVersionWarnings = false;
		CQRMain.logger.info("Updating deprectated protected region files...");
		long t = System.currentTimeMillis();
		for (int i = 0; i < files.size(); i++) {
			if (System.currentTimeMillis() - t > 2000) {
				CQRMain.logger.info(String.format("%.2f%%", (double) i / files.size() * 100.0D));
				t = System.currentTimeMillis();
			}
			File file = files.get(i);
			CompoundNBT tag = FileIOUtil.readNBTFromFile(file);
			ProtectedRegion protectedRegion = new ProtectedRegion(world, tag);
			FileIOUtil.writeNBTToFile(protectedRegion.writeToNBT(), file);
			updateChunkCapabilityEfficiently((ServerWorld) world, protectedRegion);
		}
		ProtectedRegion.logVersionWarnings = flag;
		RegionFileCache.clearRegionFileReferences();
	}

	@Deprecated
	private static final ReflectionField<Map<UUID, ProtectedRegionContainer>> FIELD_PROTECTED_REGIONS = new ReflectionField<>(ServerProtectedRegionManager.class, "protectedRegions", "protectedRegions");
	@Deprecated
	private static final ReflectionField<Set<UUID>> FIELD_PROTECTED_REGION_UUIDS = new ReflectionField<>(CapabilityProtectedRegionData.class, "protectedRegionUuids", "protectedRegionUuids");

	@Deprecated
	private static void updateChunkCapabilityEfficiently(ServerWorld world, ProtectedRegion protectedRegion) {
		IProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);
		BlockPos p1 = protectedRegion.getStartPos();
		BlockPos p2 = protectedRegion.getEndPos();
		for (int x = p1.getX() >> 4; x <= p2.getX() >> 4; x++) {
			for (int z = p1.getZ() >> 4; z <= p2.getZ() >> 4; z++) {
				Chunk chunk = world.getChunkProvider().getLoadedChunk(x, z);
				if (chunk != null) {
					CapabilityProtectedRegionData cap = chunk.getCapability(CapabilityProtectedRegionDataProvider.PROTECTED_REGION_DATA, null);
					cap.addProtectedRegionUuid(protectedRegion.getUuid());
					FIELD_PROTECTED_REGIONS.get(manager).put(protectedRegion.getUuid(), new ProtectedRegionContainer(protectedRegion, false));
				} else {
					CompoundNBT chunkNBT = getChunkNBT(world, x, z);
					if (chunkNBT != null) {
						CompoundNBT levelTag = chunkNBT.getCompoundTag("Level");
						if (!levelTag.hasKey("ForgeCaps", Constants.NBT.TAG_COMPOUND)) {
							levelTag.setTag("ForgeCaps", new CompoundNBT());
						}
						CompoundNBT capabilityTag = levelTag.getCompoundTag("ForgeCaps");
						CapabilityProtectedRegionData cap = new CapabilityProtectedRegionData(null);
						cap.readFromNBT(capabilityTag.getCompoundTag(CapabilityProtectedRegionDataProvider.LOCATION.toString()));
						FIELD_PROTECTED_REGION_UUIDS.get(cap).add(protectedRegion.getUuid());
						capabilityTag.setTag(CapabilityProtectedRegionDataProvider.LOCATION.toString(), cap.writeToNBT());
						try {
							((AnvilChunkLoader) world.getChunkProvider().chunkLoader).writeChunkData(new ChunkPos(x, z), chunkNBT);
						} catch (IOException e) {
							throw new RuntimeException("Failed saving chunk!", e);
						}
					} else {
						chunk = world.getChunk(x, z);
						CapabilityProtectedRegionData cap = chunk.getCapability(CapabilityProtectedRegionDataProvider.PROTECTED_REGION_DATA, null);
						cap.addProtectedRegionUuid(protectedRegion.getUuid());
						FIELD_PROTECTED_REGIONS.get(manager).put(protectedRegion.getUuid(), new ProtectedRegionContainer(protectedRegion, false));
					}
				}
			}
		}
	}

	@Deprecated
	private static CompoundNBT getChunkNBT(ServerWorld world, int chunkX, int chunkZ) {
		AnvilChunkLoader chunkLoader = (AnvilChunkLoader) world.getChunkProvider().chunkLoader;
		try (DataInputStream datainputstream = RegionFileCache.getChunkInputStream(chunkLoader.chunkSaveLocation, chunkX, chunkZ)) {
			if (datainputstream != null) {
				return chunkLoader.fixer.process(FixTypes.CHUNK, CompressedStreamTools.read(datainputstream));
			}
		} catch (Exception e) {
			// ignore
		}
		return null;
	}

}
