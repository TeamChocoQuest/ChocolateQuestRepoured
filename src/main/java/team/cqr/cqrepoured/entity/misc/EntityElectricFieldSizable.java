package team.cqr.cqrepoured.entity.misc;

import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.entity.ISizable;

public class EntityElectricFieldSizable extends EntityElectricField implements ISizable {

	private float sizeScaling = 1.0F;

	public EntityElectricFieldSizable(Level worldIn, int charge, UUID ownerId) {
		super(worldIn, charge, ownerId);

		this.initializeSize();
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.callOnReadFromNBT(compound);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		this.callOnWriteToNBT(compound);
	}

	@Override
	public float getSizeVariation() {
		return this.sizeScaling;
	}

	@Override
	public void applySizeVariation(float value) {
		this.sizeScaling = value;
	}

}
