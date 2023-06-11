package team.cqr.cqrepoured.entity.mount;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.entity.bases.EntityCQRMountBase;

public class EntityPollo extends EntityCQRMountBase {

	public EntityPollo(EntityType<? extends EntityPollo> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.CHICKEN_HURT;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.CHICKEN_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.CHICKEN_DEATH;
	}

	/*@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_POLLO;
	}*/

}
