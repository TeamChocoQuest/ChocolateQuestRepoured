package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;
import team.cqr.cqrepoured.world.structure.generation.generation.ChunkInfo.ChunkInfoMap;
import team.cqr.cqrepoured.world.structure.generation.generation.part.IDungeonPart;
import team.cqr.cqrepoured.world.structure.generation.generation.part.IDungeonPartBuilder;
import team.cqr.cqrepoured.world.structure.generation.generation.part.IProtectable;
import team.cqr.cqrepoured.world.structure.generation.generation.util.BlockAddedUtil;
import team.cqr.cqrepoured.world.structure.generation.generation.util.BlockLightUtil;
import team.cqr.cqrepoured.world.structure.generation.generation.util.SkyLightUtil;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitantManager;
import team.cqr.cqrepoured.world.structure.protection.IProtectedRegionManager;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegion;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegionManager;

public class GeneratableDungeon {

	private static final MutableBlockPos MUTABLE = new MutableBlockPos();

	private final UUID uuid;
	private final String dungeonName;
	private final BlockPos pos;
	private final Queue<IDungeonPart> parts;
	private final ProtectedRegion.Builder protectedRegionBuilder;
	private final ChunkInfoMap chunkInfoMap;
	private final ChunkInfoMap chunkInfoMapExtended;
	private final Queue<LightInfo> removedLights = new ArrayDeque<>();
	private GenerationState state = GenerationState.PRE_GENERATION;
	private int nextCheckBlockLightIndex;
	private int nextGenerateSkylightMapIndex;
	private int nextCheckSkyLightIndex;
	private int nextMarkBlockForUpdateIndex;
	private int nextNotifyNeighborsRespectDebugIndex;

	private final long[] generationTimes = new long[8];

	private static class LightInfo {

		public final BlockPos pos;
		public final int light;

		public LightInfo(BlockPos pos, int light) {
			this.pos = pos.toImmutable();
			this.light = light;
		}

	}

	public enum GenerationState {
		PRE_GENERATION, GENERATION, POST_GENERATION;
	}

	protected GeneratableDungeon(String dungeonName, BlockPos pos, Collection<IDungeonPart> parts, ProtectedRegion.Builder protectedRegionBuilder) {
		this.uuid = UUID.randomUUID();
		this.dungeonName = dungeonName;
		this.pos = pos;
		this.parts = new ArrayDeque<>(parts);
		this.protectedRegionBuilder = protectedRegionBuilder;
		this.chunkInfoMap = new ChunkInfoMap();
		this.chunkInfoMapExtended = new ChunkInfoMap();
	}

	public void mark(int chunkX, int chunkY, int chunkZ) {
		this.chunkInfoMap.mark(chunkX, chunkY, chunkZ);
	}

	public void markRemovedLight(int x, int y, int z, int light) {
		this.markRemovedLight(new BlockPos(x, y, z), light);
	}

	public void markRemovedLight(BlockPos pos, int light) {
		this.removedLights.add(new LightInfo(pos, light));
	}

	public void tick(World world) {
		long t = System.nanoTime();

		while (!this.isGenerated()) {
			this.generateNext(world);
		}

		this.generationTimes[0] = System.nanoTime() - t;
		CQRMain.logger.debug("Total: {} secs {} millis", this.generationTimes[0] / 1_000_000_000, this.generationTimes[0] / 1_000_000 % 1_000);
		CQRMain.logger.debug("Parts: {} secs {} millis", this.generationTimes[1] / 1_000_000_000, this.generationTimes[1] / 1_000_000 % 1_000);
		CQRMain.logger.debug("Blocklight: {} secs {} millis", this.generationTimes[2] / 1_000_000_000, this.generationTimes[2] / 1_000_000 % 1_000);
		CQRMain.logger.debug("SkylightMap: {} secs {} millis", this.generationTimes[3] / 1_000_000_000, this.generationTimes[3] / 1_000_000 % 1_000);
		CQRMain.logger.debug("Skylight: {} secs {} millis", this.generationTimes[4] / 1_000_000_000, this.generationTimes[4] / 1_000_000 % 1_000);
		CQRMain.logger.debug("RemovedBlocklight: {} secs {} millis", this.generationTimes[5] / 1_000_000_000, this.generationTimes[5] / 1_000_000 % 1_000);
		CQRMain.logger.debug("Sync: {} secs {} millis", this.generationTimes[6] / 1_000_000_000, this.generationTimes[6] / 1_000_000 % 1_000);
		CQRMain.logger.debug("Updates: {} secs {} millis", this.generationTimes[7] / 1_000_000_000, this.generationTimes[7] / 1_000_000 % 1_000);
	}

	public void generateNext(World world) {
		if (this.state == GenerationState.PRE_GENERATION) {
			CQRMain.logger.info("Generating dungeon {} at {}", this.dungeonName, this.pos);
			IProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(world);
			ProtectedRegion protectedRegion = this.protectedRegionBuilder.build(world);
			if (protectedRegion != null) {
				protectedRegion.markDirty();
				protectedRegionManager.addProtectedRegion(protectedRegion);
			}
			this.state = GenerationState.GENERATION;
			return;
		}
		if (this.tryGeneratePart(world)) {
			return;
		}
		if (this.tryCheckBlockLight(world)) {
			return;
		}
		if (this.tryGenerateSkylightMap(world)) {
			return;
		}
		if (this.tryCheckSkyLight(world)) {
			return;
		}
		if (this.tryCheckRemovedBlockLight(world)) {
			return;
		}
		if (this.tryMarkBlockForUpdate(world)) {
			return;
		}
		if (this.tryNotifyNeighboursRespectDebug(world)) {
			return;
		}
		if (this.state == GenerationState.GENERATION) {
			CQRMain.logger.info("Generated dungeon {} at {}", this.dungeonName, this.pos);
			this.state = GenerationState.POST_GENERATION;
		}
	}

	private boolean tryGeneratePart(World world) {
		if (this.parts.isEmpty()) {
			return false;
		}
		long t = System.nanoTime();

		this.parts.element().generate(world, this);
		if (this.parts.element().isGenerated()) {
			this.parts.remove();
		}
		if (this.parts.isEmpty()) {
			this.chunkInfoMap.forEach(chunkInfo -> {
				Chunk chunk = world.getChunk(chunkInfo.getChunkX(), chunkInfo.getChunkZ());
				if (world.provider.hasSkyLight()) {
					for (int chunkY = chunkInfo.topMarked(); chunkY >= 0; chunkY--) {
						ExtendedBlockStorage blockStorage = chunk.getBlockStorageArray()[chunkY];
						if (blockStorage == Chunk.NULL_BLOCK_STORAGE) {
							blockStorage = new ExtendedBlockStorage(chunkY << 4, true);
							chunk.getBlockStorageArray()[chunkY] = blockStorage;
						}
						Arrays.fill(blockStorage.getSkyLight().getData(), (byte) 0);
					}
				}
				chunkInfo.forEach(chunkY -> {
					ExtendedBlockStorage blockStorage = chunk.getBlockStorageArray()[chunkY];
					if (blockStorage != Chunk.NULL_BLOCK_STORAGE) {
						Arrays.fill(blockStorage.getBlockLight().getData(), (byte) 0);
					}
					int r = 1;
					for (int x = -r; x <= r; x++) {
						for (int y = -r; y <= r; y++) {
							for (int z = -r; z <= r; z++) {
								this.chunkInfoMapExtended.mark(chunkInfo.getChunkX() + x, chunkY + y, chunkInfo.getChunkZ() + z);
							}
						}
					}
				});
			});
		}

		this.generationTimes[1] += System.nanoTime() - t;
		return true;
	}

	private boolean tryCheckBlockLight(World world) {
		if (this.nextCheckBlockLightIndex >= this.chunkInfoMapExtended.size()) {
			return false;
		}
		long t = System.nanoTime();

		ChunkInfo chunkInfo = this.chunkInfoMapExtended.get(this.nextCheckBlockLightIndex);
		BlockLightUtil.checkBlockLight(world, chunkInfo);

		this.generationTimes[2] += System.nanoTime() - t;
		this.nextCheckBlockLightIndex++;
		return true;
	}

	private boolean tryGenerateSkylightMap(World world) {
		if (this.nextGenerateSkylightMapIndex >= this.chunkInfoMap.size()) {
			return false;
		}
		long t = System.nanoTime();

		ChunkInfo chunkInfo = this.chunkInfoMap.get(this.nextGenerateSkylightMapIndex);
		Chunk chunk = world.getChunk(chunkInfo.getChunkX(), chunkInfo.getChunkZ());
		chunk.generateSkylightMap();

		this.generationTimes[3] += System.nanoTime() - t;
		this.nextGenerateSkylightMapIndex++;
		return true;
	}

	private boolean tryCheckSkyLight(World world) {
		if (this.nextCheckSkyLightIndex >= this.chunkInfoMapExtended.size()) {
			return false;
		}
		long t = System.nanoTime();

		ChunkInfo chunkInfo = this.chunkInfoMapExtended.get(this.nextCheckSkyLightIndex);
		SkyLightUtil.checkSkyLight(world, chunkInfo);

		this.generationTimes[4] += System.nanoTime() - t;
		this.nextCheckSkyLightIndex++;
		return true;
	}

	private boolean tryCheckRemovedBlockLight(World world) {
		if (this.removedLights.isEmpty()) {
			return false;
		}
		long t = System.nanoTime();

		// TODO this could be improved further
		Set<BlockPos> updated = new HashSet<>();
		for (LightInfo removedLight : this.removedLights) {
			int r = removedLight.light - 1;
			for (int y = -r; y <= r; y++) {
				if (world.isOutsideBuildHeight(MUTABLE.setPos(0, removedLight.pos.getY() + y, 0))) {
					continue;
				}
				for (int x = -r; x <= r; x++) {
					for (int z = -r; z <= r; z++) {
						BlockPos p = new BlockPos(removedLight.pos.add(x, y, z));
						if (!updated.add(p)) {
							continue;
						}
						world.checkLightFor(EnumSkyBlock.BLOCK, p);
					}
				}
			}
		}
		this.removedLights.clear();

		this.generationTimes[5] += System.nanoTime() - t;
		return true;
	}

	private boolean tryMarkBlockForUpdate(World world) {
		if (this.nextMarkBlockForUpdateIndex >= this.chunkInfoMapExtended.size()) {
			return false;
		}
		long t = System.nanoTime();

		ChunkInfo chunkInfo = this.chunkInfoMapExtended.get(this.nextMarkBlockForUpdateIndex);
		Chunk chunk = world.getChunkProvider().getLoadedChunk(chunkInfo.getChunkX(), chunkInfo.getChunkZ());
		if (chunk != null) {
			PlayerChunkMapEntry entry = ((WorldServer) world).getPlayerChunkMap().getEntry(chunkInfo.getChunkX(), chunkInfo.getChunkZ());
			if (entry != null) {
				entry.sendPacket(new SPacketChunkData(chunk, 0xFFFF >> (15 - chunkInfo.topMarked())));
			}
		}

		this.generationTimes[6] += System.nanoTime() - t;
		this.nextMarkBlockForUpdateIndex++;
		return true;
	}

	private boolean tryNotifyNeighboursRespectDebug(World world) {
		if (this.nextNotifyNeighborsRespectDebugIndex >= this.chunkInfoMap.size()) {
			return false;
		}
		long t = System.nanoTime();

		ChunkInfo chunkInfo = this.chunkInfoMap.get(this.nextNotifyNeighborsRespectDebugIndex);
		BlockAddedUtil.onBlockAdded(world, chunkInfo);

		this.generationTimes[7] += System.nanoTime() - t;
		this.nextNotifyNeighborsRespectDebugIndex++;
		return true;
	}

	public boolean isGenerated() {
		return this.state == GenerationState.POST_GENERATION;
	}

	public UUID getUuid() {
		return this.uuid;
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public static class Builder {

		private final String dungeonName;
		private final BlockPos pos;
		private final DungeonInhabitant defaultInhabitant;
		private final ProtectedRegion.Builder protectedRegionBuilder;
		private final List<Function<World, IDungeonPart>> partBuilders = new ArrayList<>();

		public Builder(World world, BlockPos pos, DungeonBase dungeonConfig) {
			this.dungeonName = dungeonConfig.getDungeonName();
			this.pos = pos;
			this.defaultInhabitant = DungeonInhabitantManager.instance().getInhabitantByDistanceIfDefault(dungeonConfig.getDungeonMob(), world, pos.getX(), pos.getZ());
			this.protectedRegionBuilder = new ProtectedRegion.Builder(dungeonConfig, pos);
		}

		public Builder(World world, BlockPos pos, String dungeonName, String defaultnhabitant) {
			this.dungeonName = dungeonName;
			this.pos = pos;
			this.defaultInhabitant = DungeonInhabitantManager.instance().getInhabitantByDistanceIfDefault(defaultnhabitant, world, pos.getX(), pos.getZ());
			this.protectedRegionBuilder = new ProtectedRegion.Builder(dungeonName, pos);
		}

		public GeneratableDungeon build(World world) {
			List<IDungeonPart> parts = this.partBuilders.stream().map(builder -> builder.apply(world)).filter(part -> !part.isGenerated()).collect(Collectors.toList());
			parts.stream().filter(IProtectable.class::isInstance).map(IProtectable.class::cast).forEach(part -> {
				this.protectedRegionBuilder.updateMin(part.minPos());
				this.protectedRegionBuilder.updateMax(part.maxPos());
			});
			return new GeneratableDungeon(this.dungeonName, this.pos, parts, this.protectedRegionBuilder);
		}

		public void add(IDungeonPartBuilder partBuilder) {
			this.add(partBuilder, this.getPlacement(this.pos, this.defaultInhabitant));
		}

		public void add(IDungeonPartBuilder partBuilder, BlockPos partPos) {
			this.add(partBuilder, this.getPlacement(partPos));
		}

		public void add(IDungeonPartBuilder partBuilder, BlockPos partPos, Mirror mirror, Rotation rotation) {
			this.add(partBuilder, this.getPlacement(partPos, mirror, rotation));
		}

		public void add(IDungeonPartBuilder partBuilder, BlockPos partPos, DungeonInhabitant inhabitant) {
			this.add(partBuilder, this.getPlacement(partPos, inhabitant));
		}

		public void add(IDungeonPartBuilder partBuilder, BlockPos partPos, Mirror mirror, Rotation rotation, DungeonInhabitant inhabitant) {
			this.add(partBuilder, this.getPlacement(partPos, mirror, rotation, inhabitant));
		}

		public void add(IDungeonPartBuilder partBuilder, DungeonPlacement placement) {
			this.partBuilders.add(world1 -> partBuilder.build(world1, placement));
		}

		public void addAll(Collection<IDungeonPartBuilder> partBuilders) {
			partBuilders.forEach(this::add);
		}

		public DungeonPlacement getPlacement(BlockPos partPos) {
			return this.getPlacement(partPos, Mirror.NONE, Rotation.NONE, this.defaultInhabitant);
		}

		public DungeonPlacement getPlacement(BlockPos partPos, Mirror mirror, Rotation rotation) {
			return this.getPlacement(partPos, mirror, rotation, this.defaultInhabitant);
		}

		public DungeonPlacement getPlacement(BlockPos partPos, DungeonInhabitant inhabitant) {
			return this.getPlacement(partPos, Mirror.NONE, Rotation.NONE, inhabitant);
		}

		public DungeonPlacement getPlacement(BlockPos partPos, Mirror mirror, Rotation rotation, DungeonInhabitant inhabitant) {
			return new DungeonPlacement(this.pos, partPos, mirror, rotation, inhabitant, this.protectedRegionBuilder);
		}

	}

}
