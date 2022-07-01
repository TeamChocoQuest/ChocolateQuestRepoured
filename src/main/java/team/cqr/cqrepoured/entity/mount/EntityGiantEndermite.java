package team.cqr.cqrepoured.entity.mount;

import net.minecraft.entity.EntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.bases.EntityCQRMountBase;

public class EntityGiantEndermite extends EntityCQRMountBase {

	public EntityGiantEndermite(EntityType<? extends EntityGiantEndermite> type, World worldIn) {
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
