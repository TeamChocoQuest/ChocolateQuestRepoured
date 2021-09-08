package team.cqr.cqrepoured.gentest;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
import team.cqr.cqrepoured.gentest.part.IProtectable;
import team.cqr.cqrepoured.gentest.util.BlockLightUtil;
import team.cqr.cqrepoured.gentest.util.NeighborNotifyUtil;
import team.cqr.cqrepoured.gentest.util.SkyLightUtil;
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
	private final ChunkInfoMap chunkInfoMap;
	private final ChunkInfoMap chunkInfoMapExtended;
	private final Queue<LightInfo> removedLights = new ArrayDeque<>();
	private GenerationState state = GenerationState.PRE_GENERATION;
	private int nextCheckBlockLightIndex;
	private int nextGenerateSkylightMapIndex;
	private int nextCheckSkyLightIndex;
	private int nextMarkBlockForUpdateIndex;
	private int nextNotifyNeighborsRespectDebugIndex;

	private long tickTime;
	private ForgeChunkManager.Ticket chunkTicket;
	private boolean ticketRequested;

	private static class LightInfo {

		public final BlockPos pos;
		public final int light;

		public LightInfo(BlockPos pos, int light) {
			this.pos = pos.toImmutable();
			this.light = light;
		}

		public static void write(ByteBuf buf, LightInfo lightInfo) {
			buf.writeInt(lightInfo.pos.getX());
			buf.writeInt(lightInfo.pos.getY());
			buf.writeInt(lightInfo.pos.getZ());
			buf.writeByte(lightInfo.light);
		}

	}

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
		chunkInfoMap = new ChunkInfoMap();
		chunkInfoMapExtended = new ChunkInfoMap();
	}

	public GeneratableDungeon(World world, NBTTagCompound compound) {
		this.uuid = NBTUtil.getUUIDFromTag(compound.getCompoundTag("uuid"));
		this.dungeonName = compound.getString("dungeonName");
		this.pos = NBTUtil.getPosFromTag(compound.getCompoundTag("pos"));
		this.parts = new ArrayDeque<>();
		compound.getTagList("parts", Constants.NBT.TAG_COMPOUND).forEach(nbt -> this.parts.add(DungeonPart.Registry.read(world, (NBTTagCompound) nbt)));
		this.protectedRegionBuilder = new ProtectedRegion.Builder(compound.getCompoundTag("protectedRegion"));
		this.state = GenerationState.values()[compound.getInteger("state")];
		this.chunkInfoMap = new ChunkInfoMap(compound.getCompoundTag("chunkInfoMap"));
		this.chunkInfoMapExtended = new ChunkInfoMap(compound.getCompoundTag("chunkInfoMapExtended"));
		ByteBuf buf = Unpooled.wrappedBuffer(compound.getByteArray("removedLights"));
		while (buf.readableBytes() > 0) {
			this.removedLights.add(new LightInfo(new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()), buf.readByte()));
		}
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
		compound.setTag("chunkInfoMapExtended", this.chunkInfoMapExtended.writeToNBT());
		compound.setTag("removedLights", this.removedLights.stream().collect(NBTCollectors.toNBTByteArray(LightInfo::write)));
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
		this.markRemovedLight(new BlockPos(x, y, z), light);
	}

	public void markRemovedLight(BlockPos pos, int light) {
		this.removedLights.add(new LightInfo(pos, light));
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
			IProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(world);
			ProtectedRegion protectedRegion = this.protectedRegionBuilder.build(world);
			if (protectedRegion != null) {
				protectedRegion.markDirty();
				protectedRegionManager.addProtectedRegion(protectedRegion);
			}
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
			this.chunkInfoMap.forEach(chunkInfo -> chunkInfo.forEach(chunkY -> {
				Chunk chunk = world.getChunk(chunkInfo.getChunkX(), chunkInfo.getChunkZ());
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
			}));
		}
		return true;
	}

	private boolean tryCheckBlockLight(World world) {
		if (this.nextCheckBlockLightIndex >= this.chunkInfoMapExtended.size()) {
			return false;
		}
		ChunkInfo chunkInfo = this.chunkInfoMapExtended.get(this.nextCheckBlockLightIndex);
		BlockLightUtil.checkBlockLight(world, chunkInfo);
		this.nextCheckBlockLightIndex++;
		return true;
	}

	private boolean tryGenerateSkylightMap(World world) {
		if (this.nextGenerateSkylightMapIndex >= this.chunkInfoMapExtended.size()) {
			return false;
		}
		ChunkInfo chunkInfo = this.chunkInfoMapExtended.get(this.nextGenerateSkylightMapIndex);
		Chunk chunk = world.getChunk(chunkInfo.getChunkX(), chunkInfo.getChunkZ());
		chunk.generateSkylightMap();
		this.nextGenerateSkylightMapIndex++;
		return true;
	}

	private boolean tryCheckSkyLight(World world) {
		if (this.nextCheckSkyLightIndex >= this.chunkInfoMapExtended.size()) {
			return false;
		}
		ChunkInfo chunkInfo = this.chunkInfoMapExtended.get(this.nextCheckSkyLightIndex);
		SkyLightUtil.checkSkyLight(world, chunkInfo);
		this.nextCheckSkyLightIndex++;
		return true;
	}

	private boolean tryCheckRemovedBlockLight(World world) {
		if (this.removedLights.isEmpty()) {
			return false;
		}
		LightInfo removedLight = this.removedLights.remove();
		for (int x = -14; x <= 14; x++) {
			for (int y = -14; y <= 14; y++) {
				for (int z = -14; z <= 14; z++) {
					MUTABLE.setPos(removedLight.pos.getX() + x, removedLight.pos.getY() + y, removedLight.pos.getZ() + z);
					world.checkLightFor(EnumSkyBlock.BLOCK, MUTABLE);
				}
			}
		}
		return true;
	}

	private boolean tryMarkBlockForUpdate(World world) {
		if (this.nextMarkBlockForUpdateIndex >= this.chunkInfoMapExtended.size()) {
			return false;
		}
		ChunkInfo chunkInfo = this.chunkInfoMapExtended.get(this.nextMarkBlockForUpdateIndex);
		Chunk chunk = world.getChunkProvider().getLoadedChunk(chunkInfo.getChunkX(), chunkInfo.getChunkZ());
		if (chunk != null) {
			PlayerChunkMapEntry entry = ((WorldServer) world).getPlayerChunkMap().getEntry(chunkInfo.getChunkX(), chunkInfo.getChunkZ());
			if (entry != null) {
				entry.sendPacket(new SPacketChunkData(chunk, 0xFFFF >> (15 - chunkInfo.topMarked())));
			}
		}
		this.nextMarkBlockForUpdateIndex++;
		return true;
	}

	private boolean tryNotifyNeighboursRespectDebug(World world) {
		if (this.nextNotifyNeighborsRespectDebugIndex >= this.chunkInfoMap.size()) {
			return false;
		}
		ChunkInfo chunkInfo = this.chunkInfoMap.get(this.nextNotifyNeighborsRespectDebugIndex);
		chunkInfo.forEachReversed(chunkY -> NeighborNotifyUtil.notifyNeighbors(world, chunkInfo.getChunkX(), chunkY, chunkInfo.getChunkZ()));
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
			this.protectedRegionBuilder = new ProtectedRegion.Builder(dungeonConfig, pos);
		}

		public Builder(World world, BlockPos pos, String dungeonName, String defaultnhabitant) {
			this.dungeonName = dungeonName;
			this.pos = pos;
			this.defaultInhabitant = DungeonInhabitantManager.instance().getInhabitantByDistanceIfDefault(defaultnhabitant, world, pos.getX(), pos.getZ());
			this.protectedRegionBuilder = new ProtectedRegion.Builder(dungeonName, pos);
		}

		public GeneratableDungeon build(World world) {
			List<DungeonPart> parts = this.partBuilders.stream().map(builder -> builder.apply(world)).filter(part -> !part.isGenerated())
					.collect(Collectors.toList());
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
