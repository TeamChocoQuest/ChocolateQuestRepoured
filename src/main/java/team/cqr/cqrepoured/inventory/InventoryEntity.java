package team.cqr.cqrepoured.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;

public class InventoryEntity extends Inventory {

	private final Entity entity;
	private final boolean creativeOnly;

	public InventoryEntity(Entity entity, int size, boolean creativeOnly) {
		super(size);
		this.entity = entity;
		this.creativeOnly = creativeOnly;
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		if (this.creativeOnly && !pPlayer.isCreative()) {
			return false;
		}
		if (!this.entity.isAlive()) {
			return false;
		}
		return this.entity.closerThan(pPlayer, 8.0D);
	}

	public CompoundTag save(CompoundTag compound) {
		ItemStackHelper.saveAllItems(compound, this.items);
		return compound;
	}

	public void load(CompoundTag compound) {
		ItemStackHelper.loadAllItems(compound, this.items);
	}

	public Entity getEntity() {
		return entity;
	}

}
