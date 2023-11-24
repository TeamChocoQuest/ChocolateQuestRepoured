package team.cqr.cqrepoured.entity.mobs;

import java.util.Set;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import mod.azure.azurelib3.core.manager.AnimationData;
import mod.azure.azurelib3.core.manager.AnimationFactory;
import mod.azure.azurelib3.util.AzureLibUtil;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRSounds;

public class EntityCQRGremlin extends AbstractEntityCQR implements IAnimatableCQR {

	public EntityCQRGremlin(EntityType<? extends AbstractEntityCQR> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public double getBaseHealth() {
		return CQRConfig.SERVER_CONFIG.baseHealths.gremlin.get();
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

	// Geckolib
	private AnimationFactory factory = AzureLibUtil.createFactory(this);

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public Set<String> getAlwaysPlayingAnimations() {
		return null;
	}
	
	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	public void registerControllers(AnimationData data) {
		this.registerControllers(this, data);
	}

}
