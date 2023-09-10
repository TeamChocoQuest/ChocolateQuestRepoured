package team.cqr.cqrepoured.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class InventoryBlockEntity extends SimpleContainer {

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
		if (pPlayer.level().getBlockEntity(this.blockEntity.getBlockPos()) != this.blockEntity) {
			return false;
		}
		return this.blockEntity.getBlockPos().distSqr(pPlayer.blockPosition()) < 64.0D;
	}

	public CompoundTag save(CompoundTag compound) {
		compound.put("container-content", this.createTag());
		return compound;
	}

	public void load(CompoundTag compound) {
		Tag tag = compound.get("container-content");
		if (tag instanceof ListTag lt) {
			this.fromTag(lt);
		}
	}

	public void write(FriendlyByteBuf buf) {
		for (int i = 0; i < this.getContainerSize(); i++) {
			buf.writeNbt(this.getItem(i).save(new CompoundTag()));
		}
	}

	public void read(FriendlyByteBuf buf) {
		for (int i = 0; i < this.getContainerSize(); i++) {
			this.setItem(i, ItemStack.of(buf.readNbt()));
		}
	}

	public BlockEntity getBlockEntity() {
		return this.blockEntity;
	}

}
