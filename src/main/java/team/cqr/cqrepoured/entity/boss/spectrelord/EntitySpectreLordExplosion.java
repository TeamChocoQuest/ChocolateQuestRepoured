package team.cqr.cqrepoured.entity.boss.spectrelord;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.network.NetworkHooks;

public class EntitySpectreLordExplosion extends Entity {

	public EntitySpectreLordExplosion(EntityType<? extends EntitySpectreLordExplosion> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void defineSynchedData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT pCompound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT pCompound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	// TODO implement

}
