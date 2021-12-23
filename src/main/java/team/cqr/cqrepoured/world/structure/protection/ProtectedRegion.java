package team.cqr.cqrepoured.world.structure.protection;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.util.ByteBufUtil;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.NBTCollectors;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;

public class ProtectedRegion {

	public static final String PROTECTED_REGION_VERSION = "1.2.0";
	public static boolean logVersionWarnings = true;
	private final World world;
	private UUID uuid = MathHelper.getRandomUUID();
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
		this.pos = pos.toImmutable();
		this.startPos = DungeonGenUtils.getValidMinPos(startPos, endPos);
		this.endPos = DungeonGenUtils.getValidMaxPos(startPos, endPos);
		int sizeX = this.endPos.getX() - this.startPos.getX() + 1;
		int sizeY = this.endPos.getY() - this.startPos.getY() + 1;
		int sizeZ = this.endPos.getZ() - this.startPos.getZ() + 1;
		this.size = new BlockPos(sizeX, sizeY, sizeZ);
		this.protectionStates = new byte[sizeX * sizeY * sizeZ];
	}

	public ProtectedRegion(World world, NBTTagCompound compound) {
		this.world = world;
		this.readFromNBT(compound);
		this.clearNeedsSaving();
		this.clearNeedsSyncing();
	}

	public ProtectedRegion(World world, ByteBuf buf) {
		this.world = world;
		this.readFromByteBuf(buf);
		this.clearNeedsSaving();
		this.clearNeedsSyncing();
	}

	public NBTTagCompound writeToNBT() {
		NBTTagCompound tag = new NBTTagCompound();
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

	public void writeToNBT(NBTTagCompound compound) {
		compound.setString("version", PROTECTED_REGION_VERSION);
		compound.setTag("uuid", NBTUtil.createUUIDTag(this.uuid));
		compound.setString("name", this.name);
		compound.setTag("pos", NBTUtil.createPosTag(this.pos));
		compound.setTag("startPos", NBTUtil.createPosTag(this.startPos));
		compound.setTag("endPos", NBTUtil.createPosTag(this.endPos));
		compound.setByteArray("protectionStates", this.protectionStates);
		compound.setBoolean("preventBlockBreaking", this.preventBlockBreaking);
		compound.setBoolean("preventBlockPlacing", this.preventBlockPlacing);
		compound.setBoolean("preventExplosionsTNT", this.preventExplosionsTNT);
		compound.setBoolean("preventExplosionsOther", this.preventExplosionsOther);
		compound.setBoolean("preventFireSpreading", this.preventFireSpreading);
		compound.setBoolean("preventEntitySpawning", this.preventEntitySpawning);
		compound.setBoolean("ignoreNoBossOrNexus", this.ignoreNoBossOrNexus);
		compound.setBoolean("isGenerating", this.isGenerating);
		NBTTagList nbtTagList1 = new NBTTagList();
		for (UUID entityUuid : this.entityDependencies) {
			nbtTagList1.appendTag(NBTUtil.createUUIDTag(entityUuid));
		}
		compound.setTag("entityDependencies", nbtTagList1);
		NBTTagList nbtTagList2 = new NBTTagList();
		for (BlockPos blockPos : this.blockDependencies) {
			nbtTagList2.appendTag(NBTUtil.createPosTag(blockPos));
		}
		compound.setTag("blockDependencies", nbtTagList2);
	}

	public void readFromNBT(NBTTagCompound compound) {
		String version = compound.getString("version");
		if (logVersionWarnings && !version.equals(PROTECTED_REGION_VERSION)) {
			CQRMain.logger.warn("Warning! Trying to create protected region from file which was created with an older/newer version of CQR! Expected {} but got {}.", PROTECTED_REGION_VERSION, version);
		}

		this.uuid = NBTUtil.getUUIDFromTag(compound.getCompoundTag("uuid"));
		this.name = compound.getString("name");
		this.pos = NBTUtil.getPosFromTag(compound.getCompoundTag("pos"));
		this.startPos = NBTUtil.getPosFromTag(compound.getCompoundTag("startPos"));
		this.endPos = NBTUtil.getPosFromTag(compound.getCompoundTag("endPos"));
		int sizeX = this.endPos.getX() - this.startPos.getX() + 1;
		int sizeY = this.endPos.getY() - this.startPos.getY() + 1;
		int sizeZ = this.endPos.getZ() - this.startPos.getZ() + 1;
		this.size = new BlockPos(sizeX, sizeY, sizeZ);
		if (compound.hasKey("protectionStates", Constants.NBT.TAG_BYTE_ARRAY)) {
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
		NBTTagList nbtTagList1 = compound.getTagList("entityDependencies", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < nbtTagList1.tagCount(); i++) {
			this.entityDependencies.add(NBTUtil.getUUIDFromTag(nbtTagList1.getCompoundTagAt(i)));
		}
		this.blockDependencies.clear();
		NBTTagList nbtTagList2 = compound.getTagList("blockDependencies", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < nbtTagList2.tagCount(); i++) {
			this.blockDependencies.add(NBTUtil.getPosFromTag(nbtTagList2.getCompoundTagAt(i)));
		}

		this.markDirty();
	}

	public void writeToByteBuf(ByteBuf buf) {
		ByteBufUtil.writeUuid(buf, this.uuid);
		ByteBufUtils.writeUTF8String(buf, this.name);
		ByteBufUtil.writeBlockPos(buf, this.pos);
		ByteBufUtil.writeBlockPos(buf, this.startPos);
		ByteBufUtil.writeBlockPos(buf, this.endPos);
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
			ByteBufUtil.writeUuid(buf, entityUuid);
		}

		buf.writeShort(this.blockDependencies.size());
		for (BlockPos blockPos : this.blockDependencies) {
			ByteBufUtil.writeBlockPos(buf, blockPos);
		}
	}

	public void readFromByteBuf(ByteBuf buf) {
		this.uuid = ByteBufUtil.readUuid(buf);
		this.name = ByteBufUtils.readUTF8String(buf);
		this.pos = ByteBufUtil.readBlockPos(buf);
		this.startPos = ByteBufUtil.readBlockPos(buf);
		this.endPos = ByteBufUtil.readBlockPos(buf);
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
			this.entityDependencies.add(ByteBufUtil.readUuid(buf));
		}

		short blockDependenciesCount = buf.readShort();
		for (int i = 0; i < blockDependenciesCount; i++) {
			this.blockDependencies.add(ByteBufUtil.readBlockPos(buf));
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

		if (flag && this.world != null && !this.world.isRemote) {
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

		if (this.blockDependencies.add(pos.toImmutable())) {
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
			this.dungeonPos = dungeonPos.toImmutable();
			this.min = this.dungeonPos;
			this.max = this.dungeonPos;
		}

		public Builder(DungeonBase dungeonConfig, BlockPos dungeonPos) {
			this.dungeonName = dungeonConfig.getDungeonName();
			this.dungeonPos = dungeonPos.toImmutable();
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

		public Builder(NBTTagCompound compound) {
			this.dungeonName = compound.getString("dungeonName");
			this.dungeonPos = NBTUtil.getPosFromTag(compound.getCompoundTag("dungeonPos"));
			this.min = NBTUtil.getPosFromTag(compound.getCompoundTag("min"));
			this.max = NBTUtil.getPosFromTag(compound.getCompoundTag("max"));
			this.protectionSystemEnabled = compound.getBoolean("protectionSystemEnabled");
			this.preventBlockBreaking = compound.getBoolean("preventBlockBreaking");
			this.preventBlockPlacing = compound.getBoolean("preventBlockPlacing");
			this.preventEntitySpawning = compound.getBoolean("preventEntitySpawning");
			this.preventExplosionsOther = compound.getBoolean("preventExplosionsOther");
			this.preventExplosionsTNT = compound.getBoolean("preventExplosionsTNT");
			this.preventFireSpreading = compound.getBoolean("preventFireSpreading");
			this.ignoreNoBossOrNexus = compound.getBoolean("ignoreNoBossOrNexus");
			ByteBuf buf = Unpooled.wrappedBuffer(compound.getByteArray("entityDependencies"));
			while (buf.readerIndex() < buf.writerIndex()) {
				this.entityDependencies.add(ByteBufUtil.readUuid(buf));
			}
			ByteBuf buf1 = Unpooled.wrappedBuffer(compound.getByteArray("entityDependencies"));
			while (buf1.readerIndex() < buf1.writerIndex()) {
				this.blockDependencies.add(ByteBufUtil.readBlockPos(buf1));
			}
			ByteBuf buf2 = Unpooled.wrappedBuffer(compound.getByteArray("entityDependencies"));
			while (buf2.readerIndex() < buf2.writerIndex()) {
				this.unprotectedBlocks.add(ByteBufUtil.readBlockPos(buf2));
			}
		}

		public NBTTagCompound writeToNBT() {
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("dungeonName", this.dungeonName);
			compound.setTag("dungeonPos", NBTUtil.createPosTag(this.dungeonPos));
			compound.setTag("min", NBTUtil.createPosTag(this.min));
			compound.setTag("max", NBTUtil.createPosTag(this.max));
			compound.setBoolean("protectionSystemEnabled", this.protectionSystemEnabled);
			compound.setBoolean("preventBlockBreaking", this.preventBlockBreaking);
			compound.setBoolean("preventBlockPlacing", this.preventBlockPlacing);
			compound.setBoolean("preventEntitySpawning", this.preventEntitySpawning);
			compound.setBoolean("preventExplosionsOther", this.preventExplosionsOther);
			compound.setBoolean("preventExplosionsTNT", this.preventExplosionsTNT);
			compound.setBoolean("preventFireSpreading", this.preventFireSpreading);
			compound.setBoolean("ignoreNoBossOrNexus", this.ignoreNoBossOrNexus);
			compound.setTag("entityDependencies", this.entityDependencies.stream().collect(NBTCollectors.toNBTByteArray(ByteBufUtil::writeUuid)));
			compound.setTag("blockDependencies", this.blockDependencies.stream().collect(NBTCollectors.toNBTByteArray(ByteBufUtil::writeBlockPos)));
			compound.setTag("unprotectedBlocks", this.blockDependencies.stream().collect(NBTCollectors.toNBTByteArray(ByteBufUtil::writeBlockPos)));
			return compound;
		}

		public void addEntity(Entity entity) {
			if (!this.protectionSystemEnabled) {
				return;
			}
			this.entityDependencies.add(entity.getPersistentID());
		}

		public void addBlock(BlockPos pos) {
			if (!this.protectionSystemEnabled) {
				return;
			}
			this.blockDependencies.add(pos.toImmutable());
		}

		public void excludePos(BlockPos pos) {
			if (!this.protectionSystemEnabled) {
				return;
			}
			this.unprotectedBlocks.add(pos.toImmutable());
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