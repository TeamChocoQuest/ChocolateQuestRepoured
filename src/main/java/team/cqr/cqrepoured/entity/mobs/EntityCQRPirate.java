package team.cqr.cqrepoured.entity.mobs;

import java.util.Set;

import net.minecraft.entity.EntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRSounds;

public class EntityCQRPirate extends AbstractEntityCQR implements IAnimatableCQR {

	public EntityCQRPirate(EntityType<? extends AbstractEntityCQR> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Pirate;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.PIRATE;
	}

	@Override
	public int getTextureCount() {
		return 3;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return CQRSounds.PIRATE_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CQRSounds.PIRATE_DEATH;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return CQRSounds.PIRATE_HURT;
	}

	@Override
	protected float getSoundVolume() {
		return 0.5F * super.getSoundVolume();
	}

	// Geckolib
	private AnimationFactory factory = new AnimationFactory(this);

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public Set<String> getAlwaysPlayingAnimations() {
		return null;
	}

	@Override
	public boolean isSwinging() {
		return this.swinging;
	}

}
