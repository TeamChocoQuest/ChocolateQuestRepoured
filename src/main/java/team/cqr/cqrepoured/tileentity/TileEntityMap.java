package team.cqr.cqrepoured.tileentity;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.Connection;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.util.Mth;
import team.cqr.cqrepoured.init.CQRBlockEntities;
import team.cqr.cqrepoured.network.datasync.DataEntryBoolean;
import team.cqr.cqrepoured.network.datasync.DataEntryFacing;
import team.cqr.cqrepoured.network.datasync.DataEntryInt;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;

public class TileEntityMap extends BlockEntity implements ITileEntitySyncable {

	private final TileEntityDataManager dataManager = new TileEntityDataManager(this);

	private final DataEntryInt scale = new DataEntryInt("scale", 0, true);
	private final DataEntryFacing orientation = new DataEntryFacing("orientation", Direction.NORTH, true);
	private final DataEntryBoolean lockOrientation = new DataEntryBoolean("lockOrientation", false, true);
	private final DataEntryInt originX = new DataEntryInt("originX", 0, true);
	private final DataEntryInt originZ = new DataEntryInt("originZ", 0, true);
	private final DataEntryInt offsetX = new DataEntryInt("offsetX", 0, true);
	private final DataEntryInt offsetZ = new DataEntryInt("offsetZ", 0, true);
	private final DataEntryBoolean fillMap = new DataEntryBoolean("fillMap", false, true);
	private final DataEntryInt fillRadius = new DataEntryInt("fillRadius", 256, true);

	public TileEntityMap() {
		super(CQRBlockEntities.MAP.get());
		this.dataManager.register(this.scale);
		this.dataManager.register(this.orientation);
		this.dataManager.register(this.lockOrientation);
		this.dataManager.register(this.originX);
		this.dataManager.register(this.originZ);
		this.dataManager.register(this.offsetX);
		this.dataManager.register(this.offsetZ);
		this.dataManager.register(this.fillMap);
		this.dataManager.register(this.fillRadius);
	}

	@Override
	public TileEntityDataManager getDataManager() {
		return this.dataManager;
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
		this.dataManager.write(compound);
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundTag compound) {
		super.load(state, compound);
		this.dataManager.read(compound);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return new ClientboundBlockEntityDataPacket(this.worldPosition, 0, this.dataManager.write(new CompoundTag()));
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.save(new CompoundTag());
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		this.dataManager.read(pkt.getTag());
	}

	public void set(int scale, Direction orientation, boolean lockOrientation, int originX, int originZ, int xOffset, int zOffset, boolean fillMap, int fillRadius) {
		this.scale.set(Mth.clamp(scale, 0, 4));
		if (orientation.getAxis() != Direction.Axis.Y) {
			this.orientation.set(orientation);
		}
		this.lockOrientation.set(lockOrientation);
		this.originX.set(originX);
		this.originZ.set(originZ);
		this.offsetX.set(xOffset);
		this.offsetZ.set(zOffset);
		this.fillMap.set(fillMap);
		this.fillRadius.set(fillRadius);
	}

	public int getScale() {
		return this.scale.getInt();
	}

	public Direction getOrientation() {
		return this.orientation.get();
	}

	public boolean lockOrientation() {
		return this.lockOrientation.getBoolean();
	}

	public int getOriginX() {
		return this.originX.getInt();
	}

	public int getOriginZ() {
		return this.originZ.getInt();
	}

	public int getOffsetX() {
		return this.offsetX.getInt();
	}

	public int getOffsetZ() {
		return this.offsetZ.getInt();
	}

	public boolean fillMap() {
		return this.fillMap.getBoolean();
	}

	public int getFillRadius() {
		return this.fillRadius.getInt();
	}

}
