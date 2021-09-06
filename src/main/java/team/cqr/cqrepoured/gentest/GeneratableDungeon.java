package team.cqr.cqrepoured.gentest;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
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
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.util.Constants;
import scala.actors.threadpool.Arrays;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.gentest.ChunkInfo.ChunkInfoMap;
import team.cqr.cqrepoured.gentest.part.DungeonPart;
import team.cqr.cqrepoured.gentest.part.IDungeonPartBuilder;
import team.cqr.cqrepoured.gentest.util.BlockLightUtil;
import team.cqr.cqrepoured.gentest.util.NeighborNotifyUtil;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonBase;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import team.cqr.cqrepoured.structureprot.IProtectedRegionManager;
import team.cqr.cqrepoured.structureprot.ProtectedRegion;
import team.cqr.cqrepoured.structureprot.ProtectedRegionManager;
import team.cqr.cqrepoured.util.ChunkUtil;
import team.cqr.cqrepoured.util.NBTCollectors;

public class GeneratableDungeon {

	private static final MutableBlockPos MUTABLE = new MutableBlockPos();

	private final UUID uuid;
	private final String dungeonName;
	private final BlockPos pos;
	private final Queue<DungeonPart> parts;
	private final ProtectedRegion.Builder protectedRegionBuilder;
	private final ChunkInfoMap chunkInfoMap = new ChunkInfoMap();
	private GenerationState state = GenerationState.PRE_GENERATION;
	private int nextCheckBlockLightIndex;
	private int nextGenerateSkylightMapIndex;
	private int nextCheckSkyLightIndex;
	private int nextMarkBlockForUpdateIndex;
	private int nextNotifyNeighborsRespectDebugIndex;

	private long tickTime;
	private ForgeChunkManager.Ticket chunkTicket;
	private boolean ticketRequested;

	public enum GenerationState {
		PRE_GENERATION,
		GENERATION,
		POST_GENERATION;
	}

	protected GeneratableDungeon(String dungeonName, BlockPos pos, Collection<DungeonPart> parts, ProtectedRegion.Builder protectedRegionBuilder) {
		this.uuid = UUID.randomUUID();
		this.dungeonName = dungeonName;
		this.pos = pos;
		this.parts = new ArrayDeque<>(parts);
		this.protectedRegionBuilder = protectedRegionBuilder;
	}

	public GeneratableDungeon(World world, NBTTagCompound compound) {
		this.uuid = NBTUtil.getUUIDFromTag(compound.getCompoundTag("uuid"));
		this.dungeonName = compound.getString("dungeonName");
		this.pos = NBTUtil.getPosFromTag(compound.getCompoundTag("pos"));
		this.parts = new ArrayDeque<>();
		compound.getTagList("parts", Constants.NBT.TAG_COMPOUND).forEach(nbt -> this.parts.add(DungeonPart.Registry.read(world, (NBTTagCompound) nbt)));
		this.protectedRegionBuilder = new ProtectedRegion.Builder(compound.getCompoundTag("protectedRegion"));
		this.state = GenerationState.values()[compound.getInteger("state")];
		this.nextCheckBlockLightIndex = compound.getInteger("nextCheckBlockLightIndex");
		this.nextGenerateSkylightMapIndex = compound.getInteger("nextGenerateSkylightMapIndex");
		this.nextCheckSkyLightIndex = compound.getInteger("nextCheckSkyLightIndex");
		this.nextMarkBlockForUpdateIndex = compound.getInteger("nextMarkBlockForUpdateIndex");
		this.nextNotifyNeighborsRespectDebugIndex = compound.getInteger("nextNotifyNeighborsRespectDebugIndex");
	}

	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setTag("uuid", NBTUtil.createUUIDTag(this.uuid));
		compound.setString("dungeonName", this.dungeonName);
		compound.setTag("pos", NBTUtil.createPosTag(this.pos));
		compound.setTag("parts", this.parts.stream().map(DungeonPart.Registry::write).collect(NBTCollectors.toList()));
		compound.setTag("protectedRegionBuilder", this.protectedRegionBuilder.writeToNBT());
		compound.setTag("chunkInfoMap", this.chunkInfoMap.writeToNBT());
		compound.setInteger("state", this.state.ordinal());
		compound.setInteger("nextCheckBlockLightIndex", this.nextCheckBlockLightIndex);
		compound.setInteger("nextGenerateSkylightMapIndex", this.nextGenerateSkylightMapIndex);
		compound.setInteger("nextCheckSkyLightIndex", this.nextCheckSkyLightIndex);
		compound.setInteger("nextMarkBlockForUpdateIndex", this.nextMarkBlockForUpdateIndex);
		compound.setInteger("nextNotifyNeighborsRespectDebugIndex", this.nextNotifyNeighborsRespectDebugIndex);
		return compound;
	}

	public void mark(int chunkX, int chunkY, int chunkZ) {
		this.chunkInfoMap.mark(chunkX, chunkY, chunkZ);
	}

	public void markRemovedLight(int x, int y, int z, int light) {
		for (int ix = -1; ix <= 1; ix++) {
			int chunkX = (x >> 4) + ix;
			int distX = 0;
			if (ix < 0) {
				distX = x - ((chunkX << 4) + 15);
			}
			if (ix > 0) {
				distX = (chunkX << 4) - x;
			}
			if (distX >= light) {
				continue;
			}

			for (int iy = -1; iy <= 1; iy++) {
				int chunkY = (y >> 4) + iy;
				int distY = 0;
				if (iy < 0) {
					distY = y - ((chunkY << 4) + 15);
				}
				if (iy > 0) {
					distY = (chunkY << 4) - y;
				}
				if (distX + distY >= light) {
					continue;
				}

				for (int iz = -1; iz <= 1; iz++) {
					int chunkZ = (z >> 4) + iz;
					int distZ = 0;
					if (iz < 0) {
						distZ = z - ((chunkZ << 4) + 15);
					}
					if (iz > 0) {
						distZ = (chunkZ << 4) - z;
					}
					if (distX + distY + distZ >= light) {
						continue;
					}

					this.chunkInfoMap.mark(chunkX, chunkY, chunkZ);
				}
			}
		}
	}

	public void tick(World world) {
		this.tickTime = Math.min(this.tickTime + CQRConfig.advanced.generationSpeed * 1_000_000, CQRConfig.advanced.generationSpeed * 1_000_000);

		int partsGenerated = 0;
		while (!this.isGenerated() && this.tickTime > 0 && partsGenerated < CQRConfig.advanced.generationLimit) {
			long start = System.nanoTime();
			this.generateNext(world);
			this.tickTime -= System.nanoTime() - start;
			partsGenerated++;
		}
	}

	public void generateNext(World world) {
		if (this.state == GenerationState.PRE_GENERATION) {
			CQRMain.logger.info("Started generating dungeon {} at {}", this.dungeonName, this.pos);
			// TODO chunkInfoMap might be unreliable -> pass start and end position in constructor?
			IProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(world);
			BlockPos start = new BlockPos(this.chunkInfoMap.getMinChunkX() << 4, this.chunkInfoMap.getMinChunkY() << 4, this.chunkInfoMap.getMinChunkZ() << 4);
			BlockPos end = new BlockPos(this.chunkInfoMap.getMaxChunkX() << 4, this.chunkInfoMap.getMaxChunkY() << 4, this.chunkInfoMap.getMaxChunkZ() << 4);
			protectedRegionManager.addProtectedRegion(this.protectedRegionBuilder.build(world, start, start, end));
			this.state = GenerationState.GENERATION;
			return;
		}
		if (!this.ticketRequested) {
			this.chunkTicket = ChunkUtil.getTicket(world, 0, 0, 0, 0);
			this.ticketRequested = true;
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
		if (this.tryMarkBlockForUpdate(world)) {
			return;
		}
		if (this.tryNotifyNeighboursRespectDebug(world)) {
			return;
		}
		if (this.state == GenerationState.GENERATION) {
			if (this.chunkTicket != null) {
				ForgeChunkManager.releaseTicket(this.chunkTicket);
			}
			CQRMain.logger.info("Generated dungeon {} at {}", this.dungeonName, this.pos);
			this.state = GenerationState.POST_GENERATION;
		}
	}

	private boolean tryGeneratePart(World world) {
		if (this.parts.isEmpty()) {
			return false;
		}
		this.parts.element().generate(world, this);
		if (this.parts.element().isGenerated()) {
			this.parts.remove();
		}
		if (this.parts.isEmpty()) {
			long t = System.currentTimeMillis();
			this.chunkInfoMap.forEach(chunkInfo -> {
				Chunk chunk = world.getChunk(chunkInfo.getChunkX(), chunkInfo.getChunkZ());
				chunkInfo.forEach(chunkY -> {
					ExtendedBlockStorage blockStorage = chunk.getBlockStorageArray()[chunkY];
					if (blockStorage == Chunk.NULL_BLOCK_STORAGE) {
						return;
					}
					Arrays.fill(blockStorage.getBlockLight().getData(), (byte) 0);
				});
			});
			ChunkInfoMap temp = new ChunkInfoMap();
			this.chunkInfoMap.forEach(chunkInfo -> chunkInfo.forEach(chunkY -> {
				int r = 1;
				for (int x = -r; x <= r; x++) {
					for (int y = -r; y <= r; y++) {
						for (int z = -r; z <= r; z++) {
							temp.mark(chunkInfo.getChunkX() + x, chunkY + y, chunkInfo.getChunkZ() + z);
						}
					}
				}
			}));
			this.chunkInfoMap.markAll(temp);
			this.chunkInfoMap.forEach(chunkInfo -> {
				Chunk chunk = world.getChunk(chunkInfo.getChunkX(), chunkInfo.getChunkZ());
				chunkInfo.forEachReversed(chunkY -> {
					if (chunkY == 0) {
						return;
					}
					if (chunk.getBlockStorageArray()[chunkY - 1] == Chunk.NULL_BLOCK_STORAGE) {
						chunkInfo.mark(chunkY - 1);
					}
				});
			});
			CQRMain.logger.info(System.currentTimeMillis() - t);
		}
		return true;
	}

	private boolean tryCheckBlockLight(World world) {
		if (this.nextCheckBlockLightIndex >= this.chunkInfoMap.size()) {
			return false;
		}
		ChunkInfo chunkInfo = this.chunkInfoMap.get(this.nextCheckBlockLightIndex);
		Chunk chunk = world.getChunk(chunkInfo.getChunkX(), chunkInfo.getChunkZ());
		chunkInfo.forEachReversed(chunkY -> {
			ExtendedBlockStorage blockStorage = chunk.getBlockStorageArray()[chunkY];
			if (blockStorage == Chunk.NULL_BLOCK_STORAGE || blockStorage.isEmpty()) {
				return;
			}
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					for (int y = 15; y >= 0; y--) {
						MUTABLE.setPos((chunk.x << 4) + x, (chunkY << 4) + y, (chunk.z << 4) + z);
						IBlockState state = blockStorage.get(x, y, z);
						if (state.getLightValue(world, MUTABLE) <= 0) {
							continue;
						}
						BlockLightUtil.relightBlock(world, MUTABLE);
					}
				}
			}
		});
		this.nextCheckBlockLightIndex++;
		return true;
	}

	private boolean tryGenerateSkylightMap(World world) {
		if (this.nextGenerateSkylightMapIndex >= this.chunkInfoMap.size()) {
			return false;
		}
		ChunkInfo chunkInfo = this.chunkInfoMap.get(this.nextGenerateSkylightMapIndex);
		Chunk chunk = world.getChunk(chunkInfo.getChunkX(), chunkInfo.getChunkZ());
		chunk.generateSkylightMap();
		this.nextGenerateSkylightMapIndex++;
		return true;
	}

	private boolean tryCheckSkyLight(World world) {
		if (this.nextCheckSkyLightIndex >= this.chunkInfoMap.size()) {
			return false;
		}
		if (world.provider.hasSkyLight()) {
			ChunkInfo chunkInfo = this.chunkInfoMap.get(this.nextCheckSkyLightIndex);
			Chunk chunk = world.getChunk(chunkInfo.getChunkX(), chunkInfo.getChunkZ());
			chunkInfo.forEachReversed(chunkY -> {
				ExtendedBlockStorage blockStorage = chunk.getBlockStorageArray()[chunkY];
				for (int x = 0; x < 16; x++) {
					for (int z = 0; z < 16; z++) {
						int heightmap = chunk.getHeightMap()[(z << 4) | x];
						for (int y = 15; y >= 0; y--) {
							if ((chunkY << 4) + y >= heightmap) {
								continue;
							}
							MUTABLE.setPos((chunk.x << 4) + x, (chunkY << 4) + y, (chunk.z << 4) + z);
							if (blockStorage != Chunk.NULL_BLOCK_STORAGE) {
								IBlockState state = blockStorage.get(x, y, z);
								if (state.getLightOpacity(world, MUTABLE) >= 15) {
									continue;
								}
							}
							world.checkLightFor(EnumSkyBlock.SKY, MUTABLE);
						}
					}
				}
			});
		}
		this.nextCheckSkyLightIndex++;
		return true;
	}

	private boolean tryMarkBlockForUpdate(World world) {
		if (this.nextMarkBlockForUpdateIndex >= this.chunkInfoMap.size()) {
			return false;
		}
		ChunkInfo chunkInfo = this.chunkInfoMap.get(this.nextMarkBlockForUpdateIndex);
		Chunk chunk = world.getChunk(chunkInfo.getChunkX(), chunkInfo.getChunkZ());
		PlayerChunkMapEntry entry = ((WorldServer) world).getPlayerChunkMap().getEntry(chunkInfo.getChunkX(), chunkInfo.getChunkZ());
		if (entry != null) {
			entry.sendPacket(new SPacketChunkData(chunk, chunkInfo.getMarked()));
		}
		this.nextMarkBlockForUpdateIndex++;
		return true;
	}

	private boolean tryNotifyNeighboursRespectDebug(World world) {
		if (this.nextNotifyNeighborsRespectDebugIndex >= this.chunkInfoMap.size()) {
			return false;
		}
		ChunkInfo chunkInfo = this.chunkInfoMap.get(this.nextNotifyNeighborsRespectDebugIndex);
		chunkInfo.forEachReversed(chunkY -> {
			NeighborNotifyUtil.notifyNeighbors(world, chunkInfo.getChunkX(), chunkY, chunkInfo.getChunkZ());
		});
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
		private final List<Function<World, DungeonPart>> partBuilders = new ArrayList<>();

		public Builder(World world, BlockPos pos, DungeonBase dungeonConfig) {
			this.dungeonName = dungeonConfig.getDungeonName();
			this.pos = pos;
			this.defaultInhabitant = DungeonInhabitantManager.instance().getInhabitantByDistanceIfDefault(dungeonConfig.getDungeonMob(), world, pos.getX(),
					pos.getZ());
			this.protectedRegionBuilder = new ProtectedRegion.Builder(dungeonConfig);
		}

		public GeneratableDungeon build(World world) {
			List<DungeonPart> parts = this.partBuilders.stream().map(builder -> builder.apply(world)).collect(Collectors.toList());
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
