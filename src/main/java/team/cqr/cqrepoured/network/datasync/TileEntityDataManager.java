package team.cqr.cqrepoured.network.datasync;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkRegistry;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.client.packet.CPacketSyncTileEntity;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncTileEntity;
import team.cqr.cqrepoured.tileentity.ITileEntitySyncable;

public class TileEntityDataManager {

	private final TileEntity tileEntity;
	private final List<DataEntry<?>> entries = new ArrayList<>();
	private final Set<String> usedNames = new HashSet<>();
	private boolean isDirty = false;

	public TileEntityDataManager(@Nonnull TileEntity tileEntity) {
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

	public CompoundNBT write(CompoundNBT compound) {
		for (DataEntry<?> entry : this.entries) {
			compound.put(entry.getName(), entry.write());
		}

		return compound;
	}

	public void read(CompoundNBT compound) {
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
			World world = this.tileEntity.getLevel();

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
						int dim = world.diprovider.getDimension();
						BlockPos pos = this.tileEntity.getBlockPos();
						NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(dim, pos.getX(), pos.getY(), pos.getZ(), 0.0D);
						CQRMain.NETWORK.sendToAllTracking(new SPacketSyncTileEntity(pos, entryList), targetPoint);
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

	public TileEntity getTileEntity() {
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
