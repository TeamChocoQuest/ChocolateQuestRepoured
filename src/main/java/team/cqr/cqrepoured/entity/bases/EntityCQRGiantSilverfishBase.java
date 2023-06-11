package team.cqr.cqrepoured.entity.bases;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;

public abstract class EntityCQRGiantSilverfishBase extends EntityCQRMountBase {

	public EntityCQRGiantSilverfishBase(EntityType<? extends EntityCQRGiantSilverfishBase> type, Level worldIn) {
		super(type, worldIn);
		//this.setSize(2.0F, 1.0F);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.SILVERFISH_HURT;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.SILVERFISH_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.SILVERFISH_DEATH;
	}

}
