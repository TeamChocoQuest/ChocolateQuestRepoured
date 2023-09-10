package team.cqr.cqrepoured.inventory;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class InventoryEntity extends SimpleContainer {

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
		ContainerHelper.saveAllItems(compound, this.items);
		return compound;
	}

	public void load(CompoundTag compound) {
		ContainerHelper.loadAllItems(compound, this.getItems());
	}

	public Entity getEntity() {
		return entity;
	}

}
