package team.cqr.cqrepoured.entity.boss.spectrelord;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class EntitySpectreLordExplosion extends Entity {

	public EntitySpectreLordExplosion(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(CompoundNBT compound) {

	}

	@Override
	protected void writeEntityToNBT(CompoundNBT compound) {

	}

	@Override
	public boolean writeToNBTAtomically(CompoundNBT compound) {
		return false;
	}

	@Override
	public boolean writeToNBTOptional(CompoundNBT compound) {
		return false;
	}

	@Override
	public CompoundNBT writeToNBT(CompoundNBT compound) {
		return compound;
	}

	// TODO implement

}
