package com.teamcqr.chocolatequestrepoured.structureprot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class ProtectedRegion {

	private final World world;
	private UUID uuid = MathHelper.getRandomUUID();
	private BlockPos startPos;
	private BlockPos endPos;
	private boolean preventBlockBreaking = false;
	private boolean preventBlockPlacing = false;
	private boolean preventExplosions = false;
	private boolean preventFireSpreading = false;
	private boolean preventEntitySpawning = false;
	private boolean isGenerating = true;
	private final Set<UUID> entityDependencies = new HashSet<>();
	private final Set<BlockPos> blockDependencies = new HashSet<>();

	public ProtectedRegion(World world, BlockPos startPos, BlockPos endPos) {
		this.world = world;
		this.startPos = DungeonGenUtils.getValidMinPos(startPos, endPos);
		this.endPos = DungeonGenUtils.getValidMaxPos(startPos, endPos);
	}

	public ProtectedRegion(World world, NBTTagCompound compound) {
		this.world = world;
		this.readFromNBT(compound);
	}

	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setTag("uuid", NBTUtil.createUUIDTag(this.uuid));
		compound.setTag("startPos", NBTUtil.createPosTag(this.startPos));
		compound.setTag("endPos", NBTUtil.createPosTag(this.endPos));
		compound.setBoolean("preventBlockBreaking", this.preventBlockBreaking);
		compound.setBoolean("preventBlockPlacing", this.preventBlockPlacing);
		compound.setBoolean("preventExplosions", this.preventExplosions);
		compound.setBoolean("preventFireSpreading", this.preventFireSpreading);
		compound.setBoolean("preventEntitySpawning", this.preventEntitySpawning);
		compound.setBoolean("isGenerating", this.isGenerating);
		NBTTagList nbtTagList1 = new NBTTagList();
		for (UUID entityUuid : this.entityDependencies) {
			nbtTagList1.appendTag(NBTUtil.createUUIDTag(entityUuid));
		}
		compound.setTag("entityDependencies", nbtTagList1);
		NBTTagList nbtTagList2 = new NBTTagList();
		for (BlockPos pos : this.blockDependencies) {
			nbtTagList1.appendTag(NBTUtil.createPosTag(pos));
		}
		compound.setTag("blockDependencies", nbtTagList2);
		return new NBTTagCompound();
	}

	public void readFromNBT(NBTTagCompound compound) {
		this.uuid = NBTUtil.getUUIDFromTag(compound.getCompoundTag("uuid"));
		this.startPos = NBTUtil.getPosFromTag(compound.getCompoundTag("startPos"));
		this.endPos = NBTUtil.getPosFromTag(compound.getCompoundTag("endPos"));
		this.preventBlockBreaking = compound.getBoolean("preventBlockBreaking");
		this.preventBlockPlacing = compound.getBoolean("preventBlockPlacing");
		this.preventExplosions = compound.getBoolean("preventExplosions");
		this.preventFireSpreading = compound.getBoolean("preventFireSpreading");
		this.preventEntitySpawning = compound.getBoolean("preventEntitySpawning");
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
		return pos.getZ() < this.endPos.getZ();
	}

	public boolean isValid() {
		return this.isGenerating || !this.entityDependencies.isEmpty() || !this.blockDependencies.isEmpty();
	}

	public void setup(boolean preventBlockBreaking, boolean preventBlockPlacing, boolean preventExplosions, boolean preventFireSpreading, boolean preventEntitySpawning) {
		this.preventBlockBreaking = preventBlockBreaking;
		this.preventBlockPlacing = preventBlockPlacing;
		this.preventExplosions = preventExplosions;
		this.preventFireSpreading = preventFireSpreading;
		this.preventEntitySpawning = preventEntitySpawning;
	}

	public World getWorld() {
		return this.world;
	}

	public UUID getUuid() {
		return this.uuid;
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

	public void setPreventExplosions(boolean preventExplosions) {
		this.preventExplosions = preventExplosions;
	}

	public boolean preventExplosions() {
		return this.preventExplosions;
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

	public void addEntityDependency(UUID uuid) {
		this.entityDependencies.add(uuid);
	}

	public void removeEntityDependency(UUID uuid) {
		this.entityDependencies.remove(uuid);
		if (!this.isValid()) {
			ProtectedRegionManager.getInstance(this.world).removeProtectedRegion(this.uuid);
		}
	}

	public boolean isEntityDependency(UUID uuid) {
		return this.entityDependencies.contains(uuid);
	}

	public void addBlockDependency(BlockPos pos) {
		this.blockDependencies.add(pos);
	}

	public void removeBlockDependency(BlockPos pos) {
		this.blockDependencies.remove(pos);
		if (!this.isValid()) {
			ProtectedRegionManager.getInstance(this.world).removeProtectedRegion(this.uuid);
		}
	}

	public boolean isBlockDependency(BlockPos pos) {
		return this.blockDependencies.contains(pos);
	}

	public void setGenerating(boolean isGenerating) {
		this.isGenerating = isGenerating;
	}

	public boolean isGenerating() {
		return this.isGenerating;
	}

}
