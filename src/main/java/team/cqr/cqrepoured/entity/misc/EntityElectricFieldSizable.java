package team.cqr.cqrepoured.entity.misc;

import java.util.UUID;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.ISizable;

public class EntityElectricFieldSizable extends EntityElectricField implements ISizable {

	private float sizeScaling = 1.0F;

	public EntityElectricFieldSizable(World worldIn, int charge, UUID ownerId) {
		super(worldIn, charge, ownerId);

		this.initializeSize();
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.callOnReadFromNBT(compound);
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		this.callOnWriteToNBT(compound);
	}

	@Override
	public float getDefaultWidth() {
		return 1.0F;
	}

	@Override
	public float getDefaultHeight() {
		return 1.0F;
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
