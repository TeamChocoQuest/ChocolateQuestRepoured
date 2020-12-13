package team.cqr.cqrepoured.structuregen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.util.ChunkUtil;
import team.cqr.cqrepoured.util.Reference;
import team.cqr.cqrepoured.util.reflection.ReflectionField;

@EventBusSubscriber(modid = Reference.MODID)
public class DungeonGenerationHelper {

	private static class TravelingPlayer {
		private final EntityPlayer player;
		private boolean flag;

		public TravelingPlayer(EntityPlayer player) {
			this.player = player;
		}
	}

	private static final ReflectionField<Boolean> FINDING_SPAWN_POINT = new ReflectionField<>(World.class, "field_72987_B", "findingSpawnPoint");
	private static final Map<Integer, Set<TravelingPlayer>> TRAVELING_PLAYERS = new HashMap<>();
	private static final Map<Integer, Set<ChunkPos>> DELAYED_CHUNKS = new HashMap<>();
	private static boolean isGeneratingDelayedChunks = false;

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onWorldUnloadEvent(WorldEvent.Unload event) {
		if (!event.getWorld().isRemote) {
			Integer dim = event.getWorld().provider.getDimension();
			if (TRAVELING_PLAYERS.containsKey(dim)) {
				TRAVELING_PLAYERS.get(dim).clear();
			}

			generateDelayedChunks(event.getWorld());
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onWorldTickEvent(TickEvent.WorldTickEvent event) {
		generateDelayedChunks(event.world);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onEntityTravelToDimensionEvent(EntityTravelToDimensionEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			TRAVELING_PLAYERS.computeIfAbsent(event.getDimension(), key -> new HashSet<>()).add(new TravelingPlayer((EntityPlayer) event.getEntity()));
		}
	}

	@SubscribeEvent
	public static void onChunkLoadEvent(ChunkEvent.Load event) {
		World world = event.getWorld();

		if (!world.isRemote) {
			Integer dim = world.provider.getDimension();

			if (TRAVELING_PLAYERS.containsKey(dim) && !TRAVELING_PLAYERS.get(dim).isEmpty()) {
				for (TravelingPlayer travelingPlayer : TRAVELING_PLAYERS.get(dim)) {
					if (travelingPlayer.flag) {
						continue;
					}

					travelingPlayer.flag = true;
					EntityPlayer player = travelingPlayer.player;
					int chunkX = MathHelper.floor(player.posX) >> 4;
					int chunkZ = MathHelper.floor(player.posZ) >> 4;
					int radius = 4;
					ForgeChunkManager.Ticket ticket = ChunkUtil.getTicket(world, chunkX - radius, chunkZ - radius, chunkX + radius + 1, chunkZ + radius + 1, true);

					for (int x = -radius; x <= radius + 1; x++) {
						for (int z = -radius; z <= radius + 1; z++) {
							world.getChunk(chunkX + x, chunkZ + z);
						}
					}

					if (ticket != null) {
						ForgeChunkManager.releaseTicket(ticket);
					}
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPlayerChangedDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (TRAVELING_PLAYERS.containsKey(event.toDim)) {
			TRAVELING_PLAYERS.get(event.toDim).removeIf(travellingPlayer -> travellingPlayer.player.equals(event.player));
		}
	}

	public static boolean shouldDelayDungeonGeneration(World world) {
		return Boolean.TRUE.equals(FINDING_SPAWN_POINT.get(world));
	}

	public static void addDelayedChunk(World world, int chunkX, int chunkZ) {
		DELAYED_CHUNKS.computeIfAbsent(world.provider.getDimension(), key -> new HashSet<>()).add(new ChunkPos(chunkX, chunkZ));
	}

	public static boolean shouldGenerateDungeonImmediately(World world) {
		if (isGeneratingDelayedChunks) {
			return true;
		}
		if (world.playerEntities.isEmpty()) {
			return true;
		}
		Integer dim = world.provider.getDimension();
		return TRAVELING_PLAYERS.containsKey(dim) && !TRAVELING_PLAYERS.get(dim).isEmpty();
	}

	private static void generateDelayedChunks(World world) {
		if (world.isRemote) {
			return;
		}
		Integer dim = world.provider.getDimension();
		if (!DELAYED_CHUNKS.containsKey(dim)) {
			return;
		}
		for (ChunkPos chunkPos : DELAYED_CHUNKS.get(dim)) {
			long worldSeed = world.getSeed();
			Random fmlRandom = new Random(worldSeed);
			long xSeed = fmlRandom.nextLong() >> 2 + 1L;
			long zSeed = fmlRandom.nextLong() >> 2 + 1L;
			long chunkSeed = (xSeed * chunkPos.x + zSeed * chunkPos.z) ^ worldSeed;
			fmlRandom.setSeed(chunkSeed);
			isGeneratingDelayedChunks = true;
			CQRMain.DUNGEON_GENERATOR.generate(fmlRandom, chunkPos.x, chunkPos.z, world, ((WorldServer) world).getChunkProvider().chunkGenerator, world.getChunkProvider());
			CQRMain.WALL_GENERATOR.generate(fmlRandom, chunkPos.x, chunkPos.z, world, ((WorldServer) world).getChunkProvider().chunkGenerator, world.getChunkProvider());
			isGeneratingDelayedChunks = false;
		}
		DELAYED_CHUNKS.remove(dim);
	}

}
