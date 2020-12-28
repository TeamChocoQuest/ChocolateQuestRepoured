package team.cqr.cqrepoured.structureprot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
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
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncProtectedRegions;
import team.cqr.cqrepoured.util.Reference;

@EventBusSubscriber(modid = Reference.MODID)
public class ProtectedRegionEventHandler {

	/*
	 * Possible other events to use (do not delete): PlayerInteractEvent.LeftClickBlock PlayerInteractEvent.RightClickBlock PlayerEvent.BreakSpeed
	 */

	private ProtectedRegionEventHandler() {

	}

	@SubscribeEvent
	public static void onPlayerLoggedInEvent(PlayerLoggedInEvent event) {

		IProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(event.player.world);
		if (protectedRegionManager == null) {
			return;
		}
		List<ProtectedRegion> list = new ArrayList<>();
		protectedRegionManager.getProtectedRegions().forEach(list::add);
		CQRMain.NETWORK.sendTo(new SPacketSyncProtectedRegions(list), (EntityPlayerMP) event.player);
	}

	@SubscribeEvent
	public static void onPlayerChangedDimensionEvent(PlayerChangedDimensionEvent event) {
		IProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(event.player.world);
		if (protectedRegionManager == null) {
			return;
		}
		List<ProtectedRegion> list = new ArrayList<>();
		protectedRegionManager.getProtectedRegions().forEach(list::add);
		CQRMain.NETWORK.sendTo(new SPacketSyncProtectedRegions(list), (EntityPlayerMP) event.player);
	}

	public static void onPlayerDisconnectedEvent(ClientDisconnectionFromServerEvent event) {
		ProtectedRegionManager.getInstance(net.minecraft.client.Minecraft.getMinecraft().world).clearProtectedRegions();
		ProtectedRegionHelper.updateBreakableBlockWhitelist();
		ProtectedRegionHelper.updatePlaceableBlockWhitelist();
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
		if (ProtectedRegionHelper.isBlockPlacingPrevented(event.getWorld(), event.getPos(), event.getEntity(), event.getPlacedBlock().getBlock(), true)) {
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
		if (fluidStack == null || fluidStack.amount <= 0 || fluidStack.getFluid() == null) {
			IBlockState state = world.getBlockState(pos.offset(result.sideHit));
			if (state.getMaterial().isLiquid() && ProtectedRegionHelper.isBlockBreakingPrevented(event.getWorld(), result.getBlockPos(), event.getEntityPlayer(), true, true)) {
				event.setCanceled(true);
			}
		} else if (world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {
			if (ProtectedRegionHelper.isBlockPlacingPrevented(world, pos, event.getEntityPlayer(), stack, true)) {
				event.setCanceled(true);
			}
		} else if (ProtectedRegionHelper.isBlockPlacingPrevented(world, pos.offset(result.sideHit), event.getEntityPlayer(), stack, true)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onLeftClickBlockEvent(PlayerInteractEvent.LeftClickBlock event) {
		EntityPlayer player = event.getEntityPlayer();
		BlockPos pos = event.getPos();

		if (ProtectedRegionHelper.isBlockBreakingPrevented(player.world, pos, player, false, true)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onRightClickBlockEvent(PlayerInteractEvent.RightClickBlock event) {
		EntityPlayer player = event.getEntityPlayer();
		BlockPos pos = event.getPos();
		ItemStack stack = event.getItemStack();

		if (player.world.getBlockState(pos).getBlock().isReplaceable(player.world, pos)) {
			if (ProtectedRegionHelper.isBlockPlacingPrevented(player.world, pos, player, stack, true)) {
				event.setCanceled(true);
			}
		} else if (ProtectedRegionHelper.isBlockPlacingPrevented(player.world, pos.offset(event.getFace()), player, stack, true)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onBreakSpeedEvent(PlayerEvent.BreakSpeed event) {
		EntityPlayer player = event.getEntityPlayer();
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

}
