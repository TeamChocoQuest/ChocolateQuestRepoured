package team.cqr.cqrepoured.entity.mount;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.entity.bases.EntityCQRMountBase;

public class EntityGiantEndermite extends EntityCQRMountBase {

	public EntityGiantEndermite(EntityType<? extends EntityGiantEndermite> type, Level worldIn) {
		super(type, worldIn);
		//this.setSize(1.5F, 1.5F);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENDERMAN_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENDERMITE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENDERMITE_DEATH;
	}

	/*@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_GIANT_ENDERMITE;
	}*/

}
