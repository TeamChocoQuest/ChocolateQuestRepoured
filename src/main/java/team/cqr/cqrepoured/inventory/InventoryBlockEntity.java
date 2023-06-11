package team.cqr.cqrepoured.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class InventoryBlockEntity extends Inventory {

	private final TileEntity blockEntity;
	private final boolean creativeOnly;

	public InventoryBlockEntity(TileEntity blockEntity, int size, boolean creativeOnly) {
		super(size);
		this.blockEntity = blockEntity;
		this.creativeOnly = creativeOnly;
		this.addListener(inv -> blockEntity.setChanged());
	}

	@Override
	public boolean stillValid(PlayerEntity pPlayer) {
		if (this.creativeOnly && !pPlayer.isCreative()) {
			return false;
		}
		if (pPlayer.level.getBlockEntity(this.blockEntity.getBlockPos()) != this.blockEntity) {
			return false;
		}
		return this.blockEntity.getBlockPos().distSqr(pPlayer.position(), true) < 64.0D;
	}

	public CompoundNBT save(CompoundNBT compound) {
		ItemStackHelper.saveAllItems(compound, this.items);
		return compound;
	}

	public void load(CompoundNBT compound) {
		ItemStackHelper.loadAllItems(compound, this.items);
	}

	public void write(PacketBuffer buf) {
		for (int i = 0; i < this.getContainerSize(); i++) {
			buf.writeNbt(this.items.get(i).save(new CompoundNBT()));
		}
	}

	public void read(PacketBuffer buf) {
		for (int i = 0; i < this.getContainerSize(); i++) {
			this.items.set(i, ItemStack.of(buf.readNbt()));
		}
	}

	public TileEntity getBlockEntity() {
		return this.blockEntity;
	}

}
