package team.cqr.cqrepoured.objects.entity.misc;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import team.cqr.cqrepoured.objects.entity.ISizable;

public class EntityElectricFieldSizable extends EntityElectricField implements ISizable {

	private float sizeScaling = 1.0F;
	
	public EntityElectricFieldSizable(World worldIn, int charge) {
		super(worldIn, charge);
		
		this.initializeSize();
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.callOnReadFromNBT(compound);
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
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
