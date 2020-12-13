package team.cqr.cqrepoured.objects.mounts;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.objects.entity.bases.EntityCQRMountBase;

public class EntityPollo extends EntityCQRMountBase {

	public EntityPollo(World worldIn) {
		super(worldIn);
		this.setSize(0.7F, 1.5F);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_CHICKEN_HURT;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_CHICKEN_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_CHICKEN_DEATH;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_POLLO;
	}

}
