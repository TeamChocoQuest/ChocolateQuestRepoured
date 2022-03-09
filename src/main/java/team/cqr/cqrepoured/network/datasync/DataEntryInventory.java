package team.cqr.cqrepoured.network.datasync;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import team.cqr.cqrepoured.inventory.InventoryBlockEntity;

public class DataEntryInventory extends DataEntryObject<InventoryBlockEntity> {

	public DataEntryInventory(String name, InventoryBlockEntity inventory, boolean isClientModificationAllowed) {
		super(name, inventory, isClientModificationAllowed);
		inventory.addListener(inv -> {
			this.onValueChanged();
			this.markDirty();
		});
	}

	@Override
	public INBT write() {
		// nothing to write here
		return ByteNBT.ZERO;
	}

	@Override
	protected void readInternal(INBT nbt) {
		// nothing to read here
	}

	@Override
	public void writeChanges(PacketBuffer buf) {
		this.value.write(buf);
	}

	@Override
	protected void readChangesInternal(PacketBuffer buf) {
		this.value.read(buf);
	}

	@Override
	public void set(InventoryBlockEntity value) {
		throw new UnsupportedOperationException();
	}

}
