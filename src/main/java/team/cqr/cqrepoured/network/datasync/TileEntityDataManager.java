package team.cqr.cqrepoured.network.datasync;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PacketDistributor.TargetPoint;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.client.packet.CPacketSyncTileEntity;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncTileEntity;
import team.cqr.cqrepoured.tileentity.ITileEntitySyncable;

import javax.annotation.Nonnull;
import java.util.*;

public class TileEntityDataManager {

	private final BlockEntity tileEntity;
	private final List<DataEntry<?>> entries = new ArrayList<>();
	private final Set<String> usedNames = new HashSet<>();
	private boolean isDirty = false;

	public TileEntityDataManager(@Nonnull BlockEntity tileEntity) {
		if (tileEntity == null) {
			throw new NullPointerException();
		}
		if (!(tileEntity instanceof ITileEntitySyncable)) {
			throw new IllegalArgumentException();
		}
		this.tileEntity = tileEntity;
	}

	public void register(DataEntry<?> entry) {
		if (entry.isRegistered()) {
			return;
		}
		if (this.usedNames.contains(entry.getName())) {
			return;
		}
		entry.setDataManagerAndId(this, this.entries.size());
		this.entries.add(entry);
		this.usedNames.add(entry.getName());
	}

	public DataEntry<?> getById(int id) {
		if (id < 0 || id >= this.entries.size()) {
			return null;
		}
		return this.entries.get(id);
	}

	public CompoundTag write(CompoundTag compound) {
		for (DataEntry<?> entry : this.entries) {
			compound.put(entry.getName(), entry.write());
		}

		return compound;
	}

	public void read(CompoundTag compound) {
		for (DataEntry<?> entry : this.entries) {
			if (compound.contains(entry.getName())) {
				entry.read(compound.get(entry.getName()));
			}
		}
	}

	public void onDataEntryChanged(DataEntry<?> entry) {
		if (!this.tileEntity.hasLevel()) {
			return;
		}
		this.tileEntity.setChanged();
	}

	public void checkIfDirtyAndSync() {
		if (this.isDirty) {
			Level world = this.tileEntity.getLevel();

			if (world != null) {
				List<DataEntry<?>> entryList = new ArrayList<>();

				for (DataEntry<?> entry : this.entries) {
					if (entry.isDirty()) {
						if (!world.isClientSide || entry.isClientModificationAllowed()) {
							entryList.add(entry);
						} else {
							entry.clearDirty();
						}
					}
				}

				if (!entryList.isEmpty()) {
					if (!world.isClientSide) {
						BlockPos pos = this.tileEntity.getBlockPos();
						/*int dim = world.provider.getDimension();
						
						NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(dim, pos.getX(), pos.getY(), pos.getZ(), 0.0D);*/
						CQRMain.NETWORK.send(PacketDistributor.NEAR.with(TargetPoint.p(pos.getX(), pos.getY(), pos.getZ(), 0.0D, world.dimension())), new SPacketSyncTileEntity(pos, entryList));
					} else {
						CQRMain.NETWORK.sendToServer(new CPacketSyncTileEntity(this.tileEntity.getBlockPos(), entryList));
					}

					for (DataEntry<?> entry : entryList) {
						entry.clearDirty();
					}
				}
			}

			this.clearDirty();
		}
	}

	public BlockEntity getTileEntity() {
		return this.tileEntity;
	}

	public Collection<DataEntry<?>> getEntries() {
		return Collections.unmodifiableCollection(this.entries);
	}

	public void markDirty() {
		if (!this.tileEntity.hasLevel()) {
			return;
		}
		this.isDirty = true;
		this.tileEntity.setChanged();
	}

	public void clearDirty() {
		this.isDirty = false;
	}

	public boolean isDirty() {
		return this.isDirty;
	}

}
