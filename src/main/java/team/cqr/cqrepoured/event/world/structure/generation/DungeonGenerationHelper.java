package team.cqr.cqrepoured.event.world.structure.generation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.ServerWorld;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import team.cqr.cqrepoured.CQRMain;

@EventBusSubscriber(modid = CQRMain.MODID)
public class DungeonGenerationHelper {

	private static final Map<Integer, Set<PlayerEntity>> TRAVELING_PLAYERS = new HashMap<>();
	private static final Map<World, Set<ChunkPos>> DELAYED_CHUNKS = new HashMap<>();
	private static boolean isGeneratingDelayedChunks = false;

	public static void onWorldUnloadEvent(World world) {
		generateDelayedChunks(world);
		TRAVELING_PLAYERS.remove(world.provider.getDimension());
		DELAYED_CHUNKS.remove(world);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onWorldTickEvent(TickEvent.WorldTickEvent event) {
		generateDelayedChunks(event.world);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onEntityTravelToDimensionEvent(EntityTravelToDimensionEvent event) {
		if (event.getEntity() instanceof PlayerEntity) {
			TRAVELING_PLAYERS.computeIfAbsent(event.getDimension(), k -> new HashSet<>()).add((PlayerEntity) event.getEntity());
		}
	}

	@SubscribeEvent
	public static void onChunkLoadEvent(ChunkEvent.Load event) {
		TRAVELING_PLAYERS.computeIfPresent(event.getWorld().provider.getDimension(), (k, v) -> {
			Iterator<PlayerEntity> iterator = v.iterator();
			while (iterator.hasNext()) {
				PlayerEntity player = iterator.next();

				int chunkX = MathHelper.floor(player.posX) >> 4;
				int chunkZ = MathHelper.floor(player.posZ) >> 4;
				int radius = 4;

				for (int x = -radius; x <= radius + 1; x++) {
					for (int z = -radius; z <= radius + 1; z++) {
						player.world.getChunk(chunkX + x, chunkZ + z);
					}
				}

				iterator.remove();
			}
			return v;
		});
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPlayerChangedDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
		TRAVELING_PLAYERS.computeIfPresent(event.toDim, (k, v) -> {
			v.remove(event.player);
			return v;
		});
	}

	public static boolean shouldDelayDungeonGeneration(World world) {
		return world.findingSpawnPoint;
	}

	public static void addDelayedChunk(World world, int chunkX, int chunkZ) {
		DELAYED_CHUNKS.computeIfAbsent(world, k -> new HashSet<>()).add(new ChunkPos(chunkX, chunkZ));
	}

	public static boolean shouldGenerateDungeonImmediately(World world) {
		if (isGeneratingDelayedChunks) {
			return true;
		}
		if (world.playerEntities.isEmpty()) {
			return true;
		}
		Set<PlayerEntity> set = TRAVELING_PLAYERS.get(world.provider.getDimension());
		return set != null && !set.isEmpty();
	}

	private static void generateDelayedChunks(World world) {
		DELAYED_CHUNKS.computeIfPresent(world, (k, v) -> {
			for (ChunkPos chunkPos : v) {
				long worldSeed = world.getSeed();
				Random fmlRandom = new Random(worldSeed);
				long xSeed = fmlRandom.nextLong() >> 2 + 1L;
				long zSeed = fmlRandom.nextLong() >> 2 + 1L;
				long chunkSeed = (xSeed * chunkPos.x + zSeed * chunkPos.z) ^ worldSeed;
				fmlRandom.setSeed(chunkSeed);
				isGeneratingDelayedChunks = true;
				CQRMain.DUNGEON_GENERATOR.generate(fmlRandom, chunkPos.x, chunkPos.z, world, ((ServerWorld) world).getChunkProvider().chunkGenerator,
						world.getChunkProvider());
				CQRMain.WALL_GENERATOR.generate(fmlRandom, chunkPos.x, chunkPos.z, world, ((ServerWorld) world).getChunkProvider().chunkGenerator,
						world.getChunkProvider());
				isGeneratingDelayedChunks = false;
			}

			v.clear();
			return v;
		});
	}

}
