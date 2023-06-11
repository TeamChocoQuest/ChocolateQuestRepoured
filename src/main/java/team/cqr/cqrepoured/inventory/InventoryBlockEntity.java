package team.cqr.cqrepoured.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class InventoryBlockEntity extends Inventory {

	private final BlockEntity blockEntity;
	private final boolean creativeOnly;

	public InventoryBlockEntity(BlockEntity blockEntity, int size, boolean creativeOnly) {
		super(size);
		this.blockEntity = blockEntity;
		this.creativeOnly = creativeOnly;
		this.addListener(inv -> blockEntity.setChanged());
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		if (this.creativeOnly && !pPlayer.isCreative()) {
			return false;
		}
		if (pPlayer.level.getBlockEntity(this.blockEntity.getBlockPos()) != this.blockEntity) {
			return false;
		}
		return this.blockEntity.getBlockPos().distSqr(pPlayer.position(), true) < 64.0D;
	}

	public CompoundTag save(CompoundTag compound) {
		ItemStackHelper.saveAllItems(compound, this.items);
		return compound;
	}

	public void load(CompoundTag compound) {
		ItemStackHelper.loadAllItems(compound, this.items);
	}

	public void write(FriendlyByteBuf buf) {
		for (int i = 0; i < this.getContainerSize(); i++) {
			buf.writeNbt(this.items.get(i).save(new CompoundTag()));
		}
	}

	public void read(FriendlyByteBuf buf) {
		for (int i = 0; i < this.getContainerSize(); i++) {
			this.items.set(i, ItemStack.of(buf.readNbt()));
		}
	}

	public BlockEntity getBlockEntity() {
		return this.blockEntity;
	}

}
