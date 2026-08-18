package com.example.chocolatequest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import com.example.chocolatequest.registry.ModBlockEntities;

public class TileEntityMap extends BlockEntity {

	private int scale = 0;
	private Direction orientation = Direction.NORTH;
	private boolean lockOrientation = false;
	private int originX = 0;
	private int originZ = 0;
	private int offsetX = 0;
	private int offsetZ = 0;
	private boolean fillMap = false;
	private int fillRadius = 256;

	public TileEntityMap(BlockPos pos, BlockState state) {
		super(ModBlockEntities.MAP_PLACEHOLDER.get(), pos, state);
	}

	@Override
	protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
		super.saveAdditional(pTag, provider);
		pTag.putInt("scale", scale);
		pTag.putInt("orientation", orientation.get3DDataValue());
		pTag.putBoolean("lockOrientation", lockOrientation);
		pTag.putInt("originX", originX);
		pTag.putInt("originZ", originZ);
		pTag.putInt("offsetX", offsetX);
		pTag.putInt("offsetZ", offsetZ);
		pTag.putBoolean("fillMap", fillMap);
		pTag.putInt("fillRadius", fillRadius);
	}

	@Override
	public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
		super.loadAdditional(pTag, provider);
		this.scale = pTag.getInt("scale");
		this.orientation = Direction.from3DDataValue(pTag.getInt("orientation"));
		this.lockOrientation = pTag.getBoolean("lockOrientation");
		this.originX = pTag.getInt("originX");
		this.originZ = pTag.getInt("originZ");
		this.offsetX = pTag.getInt("offsetX");
		this.offsetZ = pTag.getInt("offsetZ");
		this.fillMap = pTag.getBoolean("fillMap");
		this.fillRadius = pTag.getInt("fillRadius");
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
		CompoundTag tag = new CompoundTag();
		saveAdditional(tag, provider);
		return tag;
	}

	public void set(int scale, Direction orientation, boolean lockOrientation, int originX, int originZ, int xOffset, int zOffset, boolean fillMap, int fillRadius) {
		this.scale = Mth.clamp(scale, 0, 4);
		if (orientation.getAxis() != Direction.Axis.Y) {
			this.orientation = orientation;
		}
		this.lockOrientation = lockOrientation;
		this.originX = originX;
		this.originZ = originZ;
		this.offsetX = xOffset;
		this.offsetZ = zOffset;
		this.fillMap = fillMap;
		this.fillRadius = fillRadius;
		this.setChanged();
	}

	public int getScale() {
		return this.scale;
	}

	public Direction getOrientation() {
		return this.orientation;
	}

	public boolean lockOrientation() {
		return this.lockOrientation;
	}

	public int getOriginX() {
		return this.originX;
	}

	public int getOriginZ() {
		return this.originZ;
	}

	public int getOffsetX() {
		return this.offsetX;
	}

	public int getOffsetZ() {
		return this.offsetZ;
	}

	public boolean fillMap() {
		return this.fillMap;
	}

	public int getFillRadius() {
		return this.fillRadius;
	}
}
