package team.cqr.cqrepoured.objects.entity.boss.spectrelord;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntitySpectreLordExplosion extends Entity {

	public EntitySpectreLordExplosion(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {

	}

	@Override
	public boolean writeToNBTAtomically(NBTTagCompound compound) {
		return false;
	}

	@Override
	public boolean writeToNBTOptional(NBTTagCompound compound) {
		return false;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		return compound;
	}

	// TODO implement

}
