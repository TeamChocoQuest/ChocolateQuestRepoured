package team.cqr.cqrepoured.event.world.structure.generation;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.tuple.Triple;
import team.cqr.cqrepoured.CQRMain;

import java.util.*;

//@EventBusSubscriber(modid = CQRMain.MODID)
public class DungeonGenerationHelper {

	private static final Map<DimensionType, Set<PlayerEntity>> TRAVELING_PLAYERS = new HashMap<>();
	private static final Map<IWorld, Set<ChunkPos>> DELAYED_CHUNKS = new HashMap<>();
	private static boolean isGeneratingDelayedChunks = false;

	public static void onWorldUnloadEvent(IWorld iWorld) {
		generateDelayedChunks(iWorld);
		TRAVELING_PLAYERS.remove(iWorld.dimensionType());
		DELAYED_CHUNKS.remove(iWorld);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onWorldTickEvent(TickEvent.WorldTickEvent event) {
		generateDelayedChunks(event.world);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onEntityTravelToDimensionEvent(EntityTravelToDimensionEvent event) {
		if (event.getEntity() instanceof PlayerEntity) {
			TRAVELING_PLAYERS.computeIfAbsent(event.getEntity().level.dimensionType(), k -> new HashSet<>()).add((PlayerEntity) event.getEntity());
		}
	}

	@SubscribeEvent
	public static void onChunkLoadEvent(ChunkEvent.Load event) {
		TRAVELING_PLAYERS.computeIfPresent(event.getWorld().dimensionType(), (k, v) -> {
			Iterator<PlayerEntity> iterator = v.iterator();

			Set<Triple<World, Integer, Integer>> chunksToProcess = new HashSet<>();
			while (iterator.hasNext()) {
				PlayerEntity player = iterator.next();

				int chunkX = MathHelper.floor(player.getX()) >> 4;
				int chunkZ = MathHelper.floor(player.getZ()) >> 4;
				int radius = 4;

				for (int x = -radius; x <= radius + 1; x++) {
					for (int z = -radius; z <= radius + 1; z++) {
						//warning: This can cause a concurrent access modification cause this causes another chunkLoad event!
						//player.level.getChunk(chunkX + x, chunkZ + z);
						chunksToProcess.add(Triple.of(player.level, chunkX + x, chunkZ + z));
					}
				}

				iterator.remove();
			}
			
			for(Triple<World, Integer, Integer> chunkPos : chunksToProcess) {
				chunkPos.getLeft().getChunk(chunkPos.getMiddle(), chunkPos.getRight());
			}
			
			return playersInDimension;
		});
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPlayerChangedDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
		TRAVELING_PLAYERS.computeIfPresent(event.getTo(), (k, v) -> {
			v.remove(event.getPlayer());
			return v;
		});
	}

	public static boolean shouldDelayDungeonGeneration(IWorld world) {
		return world.findingSpawnPoint;
	}

	public static void addDelayedChunk(IWorld world, int chunkX, int chunkZ) {
		DELAYED_CHUNKS.computeIfAbsent(world, k -> new HashSet<>()).add(new ChunkPos(chunkX, chunkZ));
	}

	public static boolean shouldGenerateDungeonImmediately(World world) {
		if (isGeneratingDelayedChunks) {
			return true;
		}
		if (world.players().isEmpty()) {
			return true;
		}
		Set<PlayerEntity> set = TRAVELING_PLAYERS.get(world.dimensionType());
		return set != null && !set.isEmpty();
	}

	private static void generateDelayedChunks(IWorld world) {
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
