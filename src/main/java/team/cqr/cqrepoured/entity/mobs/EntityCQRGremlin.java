package team.cqr.cqrepoured.entity.mobs;

import net.minecraft.entity.EntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRSounds;

public class EntityCQRGremlin extends AbstractEntityCQR {

	public EntityCQRGremlin(EntityType<? extends AbstractEntityCQR> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Gremlin;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.GREMLINS;
	}

	@Override
	public boolean canMountEntity() {
		return false;
	}

	@Override
	public boolean isSitting() {
		return false;
	}

	@Override
	public boolean canOpenDoors() {
		return false;
	}

	@Override
	public boolean canIgniteTorch() {
		return false;
	}

	@Override
	public boolean canTameEntity() {
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return CQRSounds.GREMLIN_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CQRSounds.GREMLIN_DEATH;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return CQRSounds.GREMLIN_HURT;
	}

	@Override
	protected float getSoundVolume() {
		return 0.5F * super.getSoundVolume();
	}

}
