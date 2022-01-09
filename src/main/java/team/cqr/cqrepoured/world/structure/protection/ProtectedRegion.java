package team.cqr.cqrepoured.world.structure.protection;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.util.ByteBufUtil;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.NBTCollectors;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;

public class ProtectedRegion {

	public static final String PROTECTED_REGION_VERSION = "1.2.0";
	public static boolean logVersionWarnings = true;
	private final World world;
	private UUID uuid = MathHelper.createInsecureUUID();
	private String name;
	private BlockPos pos;
	private BlockPos startPos;
	private BlockPos endPos;
	private BlockPos size;
	/**
	 * 0=protected
	 * 1=unprotected
	 * 2=unprotected player placed block
	 */
	private byte[] protectionStates;
	private boolean preventBlockBreaking = false;
	private boolean preventBlockPlacing = false;
	private boolean preventExplosionsTNT = false;
	private boolean preventExplosionsOther = false;
	private boolean preventFireSpreading = false;
	private boolean preventEntitySpawning = false;
	private boolean ignoreNoBossOrNexus = false;
	private boolean isGenerating = true;
	private final Set<UUID> entityDependencies = new HashSet<>();
	private final Set<BlockPos> blockDependencies = new HashSet<>();
	// Save handling
	private boolean needsSaving = false;
	private boolean needsSyncing = false;

	public ProtectedRegion(World world, String dungeonName, BlockPos pos, BlockPos startPos, BlockPos endPos) {
		this.world = world;
		this.name = dungeonName;
		this.pos = pos.immutable();
		this.startPos = DungeonGenUtils.getValidMinPos(startPos, endPos);
		this.endPos = DungeonGenUtils.getValidMaxPos(startPos, endPos);
		int sizeX = this.endPos.getX() - this.startPos.getX() + 1;
		int sizeY = this.endPos.getY() - this.startPos.getY() + 1;
		int sizeZ = this.endPos.getZ() - this.startPos.getZ() + 1;
		this.size = new BlockPos(sizeX, sizeY, sizeZ);
		this.protectionStates = new byte[sizeX * sizeY * sizeZ];
	}

	public ProtectedRegion(World world, CompoundNBT compound) {
		this.world = world;
		this.readFromNBT(compound);
		this.clearNeedsSaving();
		this.clearNeedsSyncing();
	}

	public ProtectedRegion(World world, PacketBuffer buf) {
		this.world = world;
		this.readFromByteBuf(buf);
		this.clearNeedsSaving();
		this.clearNeedsSyncing();
	}

	public CompoundNBT writeToNBT() {
		CompoundNBT tag = new CompoundNBT();
		this.writeToNBT(tag);
		return tag;
	}

	public void markDirty() {
		this.needsSaving = true;
		this.needsSyncing = true;
	}

	public void clearNeedsSaving() {
		this.needsSaving = false;
	}

	public void clearNeedsSyncing() {
		this.needsSyncing = false;
	}

	public boolean needsSaving() {
		return this.needsSaving;
	}

	public boolean needsSyncing() {
		return this.needsSyncing;
	}

	public void writeToNBT(CompoundNBT compound) {
		compound.putString("version", PROTECTED_REGION_VERSION);
		compound.put("uuid", NBTUtil.createUUID(this.uuid));
		compound.putString("name", this.name);
		compound.put("pos", NBTUtil.writeBlockPos(this.pos));
		compound.put("startPos", NBTUtil.writeBlockPos(this.startPos));
		compound.put("endPos", NBTUtil.writeBlockPos(this.endPos));
		compound.putByteArray("protectionStates", this.protectionStates);
		compound.putBoolean("preventBlockBreaking", this.preventBlockBreaking);
		compound.putBoolean("preventBlockPlacing", this.preventBlockPlacing);
		compound.putBoolean("preventExplosionsTNT", this.preventExplosionsTNT);
		compound.putBoolean("preventExplosionsOther", this.preventExplosionsOther);
		compound.putBoolean("preventFireSpreading", this.preventFireSpreading);
		compound.putBoolean("preventEntitySpawning", this.preventEntitySpawning);
		compound.putBoolean("ignoreNoBossOrNexus", this.ignoreNoBossOrNexus);
		compound.putBoolean("isGenerating", this.isGenerating);
		ListNBT nbtTagList1 = new ListNBT();
		for (UUID entityUuid : this.entityDependencies) {
			nbtTagList1.add(NBTUtil.createUUID(entityUuid));
		}
		compound.put("entityDependencies", nbtTagList1);
		ListNBT nbtTagList2 = new ListNBT();
		for (BlockPos blockPos : this.blockDependencies) {
			nbtTagList2.add(NBTUtil.writeBlockPos(blockPos));
		}
		compound.put("blockDependencies", nbtTagList2);
	}

	public void readFromNBT(CompoundNBT compound) {
		String version = compound.getString("version");
		if (logVersionWarnings && !version.equals(PROTECTED_REGION_VERSION)) {
			CQRMain.logger.warn("Warning! Trying to create protected region from file which was created with an older/newer version of CQR! Expected {} but got {}.", PROTECTED_REGION_VERSION, version);
		}

		this.uuid = NBTUtil.loadUUID(compound.getCompound("uuid"));
		this.name = compound.getString("name");
		this.pos = NBTUtil.readBlockPos(compound.getCompound("pos"));
		this.startPos = NBTUtil.readBlockPos(compound.getCompound("startPos"));
		this.endPos = NBTUtil.readBlockPos(compound.getCompound("endPos"));
		int sizeX = this.endPos.getX() - this.startPos.getX() + 1;
		int sizeY = this.endPos.getY() - this.startPos.getY() + 1;
		int sizeZ = this.endPos.getZ() - this.startPos.getZ() + 1;
		this.size = new BlockPos(sizeX, sizeY, sizeZ);
		if (compound.contains("protectionStates", Constants.NBT.TAG_BYTE_ARRAY)) {
			this.protectionStates = compound.getByteArray("protectionStates");
		} else {
			this.protectionStates = new byte[sizeX * sizeY * sizeZ];
		}
		this.preventBlockBreaking = compound.getBoolean("preventBlockBreaking");
		this.preventBlockPlacing = compound.getBoolean("preventBlockPlacing");
		this.preventExplosionsTNT = compound.getBoolean("preventExplosionsTNT");
		this.preventExplosionsOther = compound.getBoolean("preventExplosionsOther");
		this.preventFireSpreading = compound.getBoolean("preventFireSpreading");
		this.preventEntitySpawning = compound.getBoolean("preventEntitySpawning");
		this.ignoreNoBossOrNexus = compound.getBoolean("ignoreNoBossOrNexus");
		this.isGenerating = compound.getBoolean("isGenerating");
		this.entityDependencies.clear();
		ListNBT nbtTagList1 = compound.getList("entityDependencies", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < nbtTagList1.size(); i++) {
			this.entityDependencies.add(NBTUtil.loadUUID(nbtTagList1.getCompound(i)));
		}
		this.blockDependencies.clear();
		ListNBT nbtTagList2 = compound.getList("blockDependencies", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < nbtTagList2.size(); i++) {
			this.blockDependencies.add(NBTUtil.readBlockPos(nbtTagList2.getCompound(i)));
		}

		this.markDirty();
	}

	public void writeToByteBuf(PacketBuffer buf) {
		/*ByteBufUtil.writeUuid(buf, this.uuid);
		ByteBufUtils.writeUTF8String(buf, this.name);
		ByteBufUtil.writeBlockPos(buf, this.pos);
		ByteBufUtil.writeBlockPos(buf, this.startPos);
		ByteBufUtil.writeBlockPos(buf, this.endPos);*/
		
		buf.writeUUID(this.uuid);
		buf.writeUtf(this.name);
		buf.writeBlockPos(this.pos);
		buf.writeBlockPos(this.startPos);
		buf.writeBlockPos(this.endPos);
		
		
		buf.writeBytes(this.protectionStates);

		byte flags = 0;
		flags |= this.preventBlockBreaking ? 1 : 0;
		flags |= this.preventBlockPlacing ? (1 << 1) : 0;
		flags |= this.preventExplosionsTNT ? (1 << 2) : 0;
		flags |= this.preventExplosionsOther ? (1 << 3) : 0;
		flags |= this.preventFireSpreading ? (1 << 4) : 0;
		flags |= this.preventEntitySpawning ? (1 << 5) : 0;
		flags |= this.ignoreNoBossOrNexus ? (1 << 6) : 0;
		flags |= this.isGenerating ? (1 << 7) : 0;
		buf.writeByte(flags);

		buf.writeShort(this.entityDependencies.size());
		for (UUID entityUuid : this.entityDependencies) {
			buf.writeUUID(entityUuid);
		}

		buf.writeShort(this.blockDependencies.size());
		for (BlockPos blockPos : this.blockDependencies) {
			buf.writeBlockPos(blockPos);
		}
	}

	public void readFromByteBuf(PacketBuffer buf) {
		this.uuid = buf.readUUID();
		this.name = buf.readUtf();
		this.pos = buf.readBlockPos();
		this.startPos = buf.readBlockPos();
		this.endPos = buf.readBlockPos();
		int sizeX = this.endPos.getX() - this.startPos.getX() + 1;
		int sizeY = this.endPos.getY() - this.startPos.getY() + 1;
		int sizeZ = this.endPos.getZ() - this.startPos.getZ() + 1;
		this.size = new BlockPos(sizeX, sizeY, sizeZ);
		this.protectionStates = new byte[sizeX * sizeY * sizeZ];
		buf.readBytes(this.protectionStates);

		byte flags = buf.readByte();
		this.preventBlockBreaking = (flags & 1) == 1;
		this.preventBlockPlacing = ((flags >> 1) & 1) == 1;
		this.preventExplosionsTNT = ((flags >> 2) & 1) == 1;
		this.preventExplosionsOther = ((flags >> 3) & 1) == 1;
		this.preventFireSpreading = ((flags >> 4) & 1) == 1;
		this.preventEntitySpawning = ((flags >> 5) & 1) == 1;
		this.ignoreNoBossOrNexus = ((flags >> 6) & 1) == 1;
		this.isGenerating = ((flags >> 7) & 1) == 1;

		short entityDependenciesCount = buf.readShort();
		for (int i = 0; i < entityDependenciesCount; i++) {
			this.entityDependencies.add(buf.readUUID());
		}

		short blockDependenciesCount = buf.readShort();
		for (int i = 0; i < blockDependenciesCount; i++) {
			this.blockDependencies.add(buf.readBlockPos());
		}

		this.markDirty();
	}

	public boolean isInsideProtectedRegion(BlockPos pos) {
		if (pos.getX() < this.startPos.getX()) {
			return false;
		}
		if (pos.getY() < this.startPos.getY()) {
			return false;
		}
		if (pos.getZ() < this.startPos.getZ()) {
			return false;
		}
		if (pos.getX() > this.endPos.getX()) {
			return false;
		}
		if (pos.getY() > this.endPos.getY()) {
			return false;
		}
		return pos.getZ() <= this.endPos.getZ();
	}

	public boolean isBreakable(BlockPos pos) {
		if (!this.isInsideProtectedRegion(pos)) {
			return true;
		}
		int x = (pos.getX() - this.startPos.getX()) * this.size.getY() * this.size.getZ();
		int y = (pos.getY() - this.startPos.getY()) * this.size.getZ();
		int z = pos.getZ() - this.startPos.getZ();
		return this.protectionStates[x + y + z] != 0;
	}

	public int getProtectionState(BlockPos pos) {
		if (!this.isInsideProtectedRegion(pos)) {
			return -1;
		}
		int x = (pos.getX() - this.startPos.getX()) * this.size.getY() * this.size.getZ();
		int y = (pos.getY() - this.startPos.getY()) * this.size.getZ();
		int z = pos.getZ() - this.startPos.getZ();
		return this.protectionStates[x + y + z];
	}

	public void setProtectionState(BlockPos pos, int i) {
		if (!this.isInsideProtectedRegion(pos)) {
			return;
		}
		int x = (pos.getX() - this.startPos.getX()) * this.size.getY() * this.size.getZ();
		int y = (pos.getY() - this.startPos.getY()) * this.size.getZ();
		int z = pos.getZ() - this.startPos.getZ();
		byte newState = (byte) (i & 255);
		if (this.protectionStates[x + y + z] != newState) {
			this.protectionStates[x + y + z] = newState;
			this.markDirty();
		}
	}

	public byte[] getProtectionStates() {
		return this.protectionStates;
	}

	public boolean isValid() {
		return this.isGenerating || !this.entityDependencies.isEmpty() || !this.blockDependencies.isEmpty() || this.ignoreNoBossOrNexus;
	}

	public void setup(boolean preventBlockBreaking, boolean preventBlockPlacing, boolean preventExplosionsTNT, boolean preventExplosionsOther, boolean preventFireSpreading, boolean preventEntitySpawning, boolean ignoreNoBossOrNexus) {
		if (!this.isGenerating) {
			return;
		}

		this.preventBlockBreaking = preventBlockBreaking;
		this.preventBlockPlacing = preventBlockPlacing;
		this.preventExplosionsTNT = preventExplosionsTNT;
		this.preventExplosionsOther = preventExplosionsOther;
		this.preventFireSpreading = preventFireSpreading;
		this.preventEntitySpawning = preventEntitySpawning;
		this.ignoreNoBossOrNexus = ignoreNoBossOrNexus;

		this.markDirty();
	}

	public World getWorld() {
		return this.world;
	}

	public UUID getUuid() {
		return this.uuid;
	}

	public String getName() {
		return this.name;
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public BlockPos getStartPos() {
		return this.startPos;
	}

	public BlockPos getEndPos() {
		return this.endPos;
	}

	public boolean preventBlockBreaking() {
		return this.preventBlockBreaking;
	}

	public boolean preventBlockPlacing() {
		return this.preventBlockPlacing;
	}

	public boolean preventExplosionsTNT() {
		return this.preventExplosionsTNT;
	}

	public boolean preventExplosionsOther() {
		return this.preventExplosionsOther;
	}

	public boolean preventFireSpreading() {
		return this.preventFireSpreading;
	}

	public boolean preventEntitySpawning() {
		return this.preventEntitySpawning;
	}

	public boolean ignoreNoBossOrNexus() {
		return this.ignoreNoBossOrNexus;
	}

	public void addEntityDependency(UUID uuid) {
		if (!this.isGenerating) {
			return;
		}

		if (this.entityDependencies.add(uuid)) {
			this.markDirty();
		}
	}

	public void removeEntityDependency(UUID uuid) {
		boolean flag = this.entityDependencies.remove(uuid);

		if (flag && this.world != null && !this.world.isClientSide) {
			this.markDirty();

			if (!this.isValid()) {
				IProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(this.world);
				if (protectedRegionManager != null) {
					protectedRegionManager.removeProtectedRegion(this);
				}
			}
		}
	}

	public boolean isEntityDependency(UUID uuid) {
		return this.entityDependencies.contains(uuid);
	}

	public Set<UUID> getEntityDependencies() {
		return Collections.unmodifiableSet(this.entityDependencies);
	}

	public void addBlockDependency(BlockPos pos) {
		if (!this.isGenerating) {
			return;
		}

		if (this.blockDependencies.add(pos.immutable())) {
			this.markDirty();
		}
	}

	public void removeBlockDependency(BlockPos pos) {
		if (this.blockDependencies.remove(pos)) {
			this.markDirty();

			if (!this.isValid()) {
				IProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(this.world);
				if (protectedRegionManager != null) {
					protectedRegionManager.removeProtectedRegion(this);
				}
			}
		}
	}

	public boolean isBlockDependency(BlockPos pos) {
		return this.blockDependencies.contains(pos);
	}

	public Set<BlockPos> getBlockDependencies() {
		return Collections.unmodifiableSet(this.blockDependencies);
	}

	public void finishGenerating() {
		if (!this.isGenerating) {
			return;
		}

		this.isGenerating = false;
		this.markDirty();
	}

	public boolean isGenerating() {
		return this.isGenerating;
	}

	public static class Builder {

		private final String dungeonName;
		private final BlockPos dungeonPos;
		private BlockPos min;
		private BlockPos max;
		private boolean protectionSystemEnabled;
		private boolean preventBlockBreaking;
		private boolean preventBlockPlacing;
		private boolean preventEntitySpawning;
		private boolean preventExplosionsOther;
		private boolean preventExplosionsTNT;
		private boolean preventFireSpreading;
		private boolean ignoreNoBossOrNexus;
		private final Set<UUID> entityDependencies = new HashSet<>();
		private final Set<BlockPos> blockDependencies = new HashSet<>();
		private final Set<BlockPos> unprotectedBlocks = new HashSet<>();

		public Builder(String dungeonName, BlockPos dungeonPos) {
			this.dungeonName = dungeonName;
			this.dungeonPos = dungeonPos.immutable();
			this.min = this.dungeonPos;
			this.max = this.dungeonPos;
		}

		public Builder(DungeonBase dungeonConfig, BlockPos dungeonPos) {
			this.dungeonName = dungeonConfig.getDungeonName();
			this.dungeonPos = dungeonPos.immutable();
			this.min = this.dungeonPos;
			this.max = this.dungeonPos;
			this.setup(dungeonConfig);
		}

		public void setup(boolean enabled, boolean breaking, boolean placing, boolean spawning, boolean explosions, boolean tnt, boolean fire, boolean persistent) {
			this.protectionSystemEnabled = enabled;
			this.preventBlockBreaking = breaking;
			this.preventBlockPlacing = placing;
			this.preventEntitySpawning = spawning;
			this.preventExplosionsOther = explosions;
			this.preventExplosionsTNT = tnt;
			this.preventFireSpreading = fire;
			this.ignoreNoBossOrNexus = persistent;
		}

		public void setup(DungeonBase dungeonConfig) {
			this.protectionSystemEnabled = dungeonConfig.isProtectionSystemEnabled();
			this.preventBlockBreaking = dungeonConfig.preventBlockBreaking();
			this.preventBlockPlacing = dungeonConfig.preventBlockPlacing();
			this.preventEntitySpawning = dungeonConfig.preventEntitySpawning();
			this.preventExplosionsOther = dungeonConfig.preventExplosionsOther();
			this.preventExplosionsTNT = dungeonConfig.preventExplosionsTNT();
			this.preventFireSpreading = dungeonConfig.preventFireSpreading();
			this.ignoreNoBossOrNexus = dungeonConfig.ignoreNoBossOrNexus();
		}

		public Builder(CompoundNBT compound) {
			this.dungeonName = compound.getString("dungeonName");
			this.dungeonPos = NBTUtil.readBlockPos(compound.getCompound("dungeonPos"));
			this.min = NBTUtil.readBlockPos(compound.getCompound("min"));
			this.max = NBTUtil.readBlockPos(compound.getCompound("max"));
			this.protectionSystemEnabled = compound.getBoolean("protectionSystemEnabled");
			this.preventBlockBreaking = compound.getBoolean("preventBlockBreaking");
			this.preventBlockPlacing = compound.getBoolean("preventBlockPlacing");
			this.preventEntitySpawning = compound.getBoolean("preventEntitySpawning");
			this.preventExplosionsOther = compound.getBoolean("preventExplosionsOther");
			this.preventExplosionsTNT = compound.getBoolean("preventExplosionsTNT");
			this.preventFireSpreading = compound.getBoolean("preventFireSpreading");
			this.ignoreNoBossOrNexus = compound.getBoolean("ignoreNoBossOrNexus");
			PacketBuffer buf = new PacketBuffer(Unpooled.wrappedBuffer(compound.getByteArray("entityDependencies")));
			while (buf.readerIndex() < buf.writerIndex()) {
				this.entityDependencies.add(buf.readUUID());
			}
			PacketBuffer buf1 = new PacketBuffer(Unpooled.wrappedBuffer(compound.getByteArray("blockDependencies")));
			while (buf1.readerIndex() < buf1.writerIndex()) {
				this.blockDependencies.add(buf1.readBlockPos());
			}
			PacketBuffer buf2 = new PacketBuffer(Unpooled.wrappedBuffer(compound.getByteArray("unprotectedBlocks")));
			while (buf2.readerIndex() < buf2.writerIndex()) {
				this.unprotectedBlocks.add(buf2.readBlockPos());
			}
		}

		public CompoundNBT writeToNBT() {
			CompoundNBT compound = new CompoundNBT();
			compound.putString("dungeonName", this.dungeonName);
			compound.put("dungeonPos", NBTUtil.writeBlockPos(this.dungeonPos));
			compound.put("min", NBTUtil.writeBlockPos(this.min));
			compound.put("max", NBTUtil.writeBlockPos(this.max));
			compound.putBoolean("protectionSystemEnabled", this.protectionSystemEnabled);
			compound.putBoolean("preventBlockBreaking", this.preventBlockBreaking);
			compound.putBoolean("preventBlockPlacing", this.preventBlockPlacing);
			compound.putBoolean("preventEntitySpawning", this.preventEntitySpawning);
			compound.putBoolean("preventExplosionsOther", this.preventExplosionsOther);
			compound.putBoolean("preventExplosionsTNT", this.preventExplosionsTNT);
			compound.putBoolean("preventFireSpreading", this.preventFireSpreading);
			compound.putBoolean("ignoreNoBossOrNexus", this.ignoreNoBossOrNexus);
			compound.put("entityDependencies", this.entityDependencies.stream().collect(NBTCollectors.toNBTByteArray(ByteBufUtil::writeUuid)));
			compound.put("blockDependencies", this.blockDependencies.stream().collect(NBTCollectors.toNBTByteArray(ByteBufUtil::writeBlockPos)));
			compound.put("unprotectedBlocks", this.blockDependencies.stream().collect(NBTCollectors.toNBTByteArray(ByteBufUtil::writeBlockPos)));
			return compound;
		}

		public void addEntity(Entity entity) {
			if (!this.protectionSystemEnabled) {
				return;
			}
			this.entityDependencies.add(entity.getUUID());
		}

		public void addBlock(BlockPos pos) {
			if (!this.protectionSystemEnabled) {
				return;
			}
			this.blockDependencies.add(pos.immutable());
		}

		public void excludePos(BlockPos pos) {
			if (!this.protectionSystemEnabled) {
				return;
			}
			this.unprotectedBlocks.add(pos.immutable());
		}

		public void updateMin(BlockPos pos) {
			this.min = new BlockPos(Math.min(pos.getX(), this.min.getX()), Math.min(pos.getY(), this.min.getY()), Math.min(pos.getZ(), this.min.getZ()));
		}

		public void updateMax(BlockPos pos) {
			this.max = new BlockPos(Math.max(pos.getX(), this.max.getX()), Math.max(pos.getY(), this.max.getY()), Math.max(pos.getZ(), this.max.getZ()));
		}

		@Nullable
		public ProtectedRegion build(World world) {
			if (!this.protectionSystemEnabled) {
				return null;
			}
			ProtectedRegion protectedRegion = new ProtectedRegion(world, this.dungeonName, this.dungeonPos, this.min, this.max);
			protectedRegion.preventBlockBreaking = this.preventBlockBreaking;
			protectedRegion.preventBlockPlacing = this.preventBlockPlacing;
			protectedRegion.preventEntitySpawning = this.preventEntitySpawning;
			protectedRegion.preventExplosionsOther = this.preventExplosionsOther;
			protectedRegion.preventExplosionsTNT = this.preventExplosionsTNT;
			protectedRegion.preventFireSpreading = this.preventFireSpreading;
			protectedRegion.ignoreNoBossOrNexus = this.ignoreNoBossOrNexus;
			protectedRegion.isGenerating = false;
			protectedRegion.blockDependencies.addAll(this.blockDependencies);
			protectedRegion.entityDependencies.addAll(this.entityDependencies);
			this.unprotectedBlocks.forEach(p -> protectedRegion.setProtectionState(p, 1));
			return protectedRegion;
		}

	}

}
