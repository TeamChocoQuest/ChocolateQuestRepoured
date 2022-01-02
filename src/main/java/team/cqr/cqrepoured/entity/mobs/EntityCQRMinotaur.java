package team.cqr.cqrepoured.entity.mobs;

import net.minecraft.util.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRLoottables;

public class EntityCQRMinotaur extends AbstractEntityCQR {

	public EntityCQRMinotaur(World worldIn) {
		super(worldIn);
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Minotaur;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.UNDEAD;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_MINOTAUR;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.isFire()) {
			return false;
		}
		return super.hurt(source, amount);
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.COW_DEATH;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.COW_AMBIENT;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.COW_HURT;
	}

}
