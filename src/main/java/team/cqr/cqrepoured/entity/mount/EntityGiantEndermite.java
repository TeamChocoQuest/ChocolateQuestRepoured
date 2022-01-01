package team.cqr.cqrepoured.entity.mount;

import net.minecraft.util.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.bases.EntityCQRMountBase;
import team.cqr.cqrepoured.init.CQRLoottables;

public class EntityGiantEndermite extends EntityCQRMountBase {

	public EntityGiantEndermite(World worldIn) {
		super(worldIn);
		this.setSize(1.5F, 1.5F);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_ENDERMITE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_ENDERMITE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_ENDERMITE_DEATH;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_GIANT_ENDERMITE;
	}

}
