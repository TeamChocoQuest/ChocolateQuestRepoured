package team.cqr.cqrepoured.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.entity.player.Inventory;

public class InventoryEntity extends Inventory {

	private final Entity entity;
	private final boolean creativeOnly;

	public InventoryEntity(Entity entity, int size, boolean creativeOnly) {
		super(size);
		this.entity = entity;
		this.creativeOnly = creativeOnly;
	}

	@Override
	public boolean stillValid(PlayerEntity pPlayer) {
		if (this.creativeOnly && !pPlayer.isCreative()) {
			return false;
		}
		if (!this.entity.isAlive()) {
			return false;
		}
		return this.entity.closerThan(pPlayer, 8.0D);
	}

	public CompoundNBT save(CompoundNBT compound) {
		ItemStackHelper.saveAllItems(compound, this.items);
		return compound;
	}

	public void load(CompoundNBT compound) {
		ItemStackHelper.loadAllItems(compound, this.items);
	}

	public Entity getEntity() {
		return entity;
	}

}
