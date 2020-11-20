package com.teamcqr.chocolatequestrepoured.structureprot;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketProtectedRegionRemoveBlockDependency;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketProtectedRegionRemoveEntityDependency;
import com.teamcqr.chocolatequestrepoured.util.ByteBufUtil;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class ProtectedRegion {

	private static final String PROTECTED_REGION_VERSION = "1.0.1";
	private final World world;
	private UUID uuid = MathHelper.getRandomUUID();
	private String name;
	private BlockPos pos;
	private BlockPos startPos;
	private BlockPos endPos;
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

	public ProtectedRegion(World world, String dungeonName, BlockPos pos, BlockPos startPos, BlockPos endPos) {
		this.world = world;
		this.name = dungeonName;
		this.pos = pos.toImmutable();
		this.startPos = DungeonGenUtils.getValidMinPos(startPos, endPos);
		this.endPos = DungeonGenUtils.getValidMaxPos(startPos, endPos);
	}

	public ProtectedRegion(World world, NBTTagCompound compound) {
		this.world = world;
		this.readFromNBT(compound);
	}

	public ProtectedRegion(World world, ByteBuf buf) {
		this.world = world;
		this.readFromByteBuf(buf);
	}

	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("version", PROTECTED_REGION_VERSION);
		compound.setTag("uuid", NBTUtil.createUUIDTag(this.uuid));
		compound.setString("name", this.name);
		compound.setTag("pos", NBTUtil.createPosTag(this.pos));
		compound.setTag("startPos", NBTUtil.createPosTag(this.startPos));
		compound.setTag("endPos", NBTUtil.createPosTag(this.endPos));
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
			nbtTagList1.appendTag(NBTUtil.createPosTag(blockPos));
		}
		compound.setTag("blockDependencies", nbtTagList2);
		return compound;
	}

	public void readFromNBT(NBTTagCompound compound) {
		String version = compound.getString("version");
		if (!version.equals(PROTECTED_REGION_VERSION)) {
			CQRMain.logger.warn("Warning! Trying to create protected region from file which was created with an older/newer version of CQR! Expected {} but got {}.", PROTECTED_REGION_VERSION, version);
		}

		this.uuid = NBTUtil.getUUIDFromTag(compound.getCompoundTag("uuid"));
		this.name = compound.getString("name");
		this.pos = NBTUtil.getPosFromTag(compound.getCompoundTag("pos"));
		this.startPos = NBTUtil.getPosFromTag(compound.getCompoundTag("startPos"));
		this.endPos = NBTUtil.getPosFromTag(compound.getCompoundTag("endPos"));
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

		// TODO Fix for protected regions not deleting themselves. Should be removed in the future.
		if (!version.equals(PROTECTED_REGION_VERSION)) {
			this.isGenerating = false;
		}
	}

	public void writeToByteBuf(ByteBuf buf) {
		ByteBufUtil.writeUuid(buf, this.uuid);
		ByteBufUtils.writeUTF8String(buf, this.name);
		ByteBufUtil.writeBlockPos(buf, this.pos);
		ByteBufUtil.writeBlockPos(buf, this.startPos);
		ByteBufUtil.writeBlockPos(buf, this.endPos);

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

	public boolean isValid() {
		return this.isGenerating || !this.entityDependencies.isEmpty() || !this.blockDependencies.isEmpty() || this.ignoreNoBossOrNexus;
	}

	public void setup(boolean preventBlockBreaking, boolean preventBlockPlacing, boolean preventExplosionsTNT, boolean preventExplosionsOther, boolean preventFireSpreading, boolean preventEntitySpawning, boolean ignoreNoBossOrNexus) {
		this.preventBlockBreaking = preventBlockBreaking;
		this.preventBlockPlacing = preventBlockPlacing;
		this.preventExplosionsTNT = preventExplosionsTNT;
		this.preventExplosionsOther = preventExplosionsOther;
		this.preventFireSpreading = preventFireSpreading;
		this.preventEntitySpawning = preventEntitySpawning;
		this.ignoreNoBossOrNexus = ignoreNoBossOrNexus;
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

	public void setPreventBlockBreaking(boolean preventBlockBreaking) {
		this.preventBlockBreaking = preventBlockBreaking;
	}

	public boolean preventBlockBreaking() {
		return this.preventBlockBreaking;
	}

	public void setPreventBlockPlacing(boolean preventBlockPlacing) {
		this.preventBlockPlacing = preventBlockPlacing;
	}

	public boolean preventBlockPlacing() {
		return this.preventBlockPlacing;
	}

	public void setPreventExplosionsTNT(boolean preventExplosionsTNT) {
		this.preventExplosionsTNT = preventExplosionsTNT;
	}

	public boolean preventExplosionsTNT() {
		return this.preventExplosionsTNT;
	}

	public void setPreventExplosionsOther(boolean preventExplosionsOther) {
		this.preventExplosionsOther = preventExplosionsOther;
	}

	public boolean preventExplosionsOther() {
		return this.preventExplosionsOther;
	}

	public void setPreventFireSpreading(boolean preventFireSpreading) {
		this.preventFireSpreading = preventFireSpreading;
	}

	public boolean preventFireSpreading() {
		return this.preventFireSpreading;
	}

	public void setPreventEntitySpawning(boolean preventEntitySpawning) {
		this.preventEntitySpawning = preventEntitySpawning;
	}

	public boolean preventEntitySpawning() {
		return this.preventEntitySpawning;
	}

	public void setIgnoreNoBossOrNexus(boolean ignoreNoBossOrNexus) {
		this.ignoreNoBossOrNexus = ignoreNoBossOrNexus;
	}

	public boolean ignoreNoBossOrNexus() {
		return this.ignoreNoBossOrNexus;
	}

	public void addEntityDependency(UUID uuid) {
		if (!this.isGenerating) {
			return;
		}
		this.entityDependencies.add(uuid);
	}

	public void removeEntityDependency(UUID uuid) {
		boolean flag = this.entityDependencies.remove(uuid);

		if (flag && this.world != null && !this.world.isRemote) {
			if (!this.isValid()) {
				ProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(this.world);
				if (protectedRegionManager != null) {
					ProtectedRegionManager.getInstance(this.world).removeProtectedRegion(this);
				}
			} else {
				CQRMain.NETWORK.sendToDimension(new SPacketProtectedRegionRemoveEntityDependency(this.uuid, uuid), this.world.provider.getDimension());
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
		this.blockDependencies.add(pos);
	}

	public void removeBlockDependency(BlockPos pos) {
		boolean flag = this.blockDependencies.remove(pos);

		if (flag && this.world != null && !this.world.isRemote) {
			if (!this.isValid()) {
				ProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(this.world);
				if (protectedRegionManager != null) {
					ProtectedRegionManager.getInstance(this.world).removeProtectedRegion(this);
				}
			} else {
				CQRMain.NETWORK.sendToDimension(new SPacketProtectedRegionRemoveBlockDependency(this.uuid, pos), this.world.provider.getDimension());
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
		this.isGenerating = false;
	}

	public boolean isGenerating() {
		return this.isGenerating;
	}

}
