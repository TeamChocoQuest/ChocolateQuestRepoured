package team.cqr.cqrepoured.entity.mobs;

import java.util.Set;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import mod.azure.azurelib3.core.manager.AnimationData;
import mod.azure.azurelib3.core.manager.AnimationFactory;
import mod.azure.azurelib3.util.AzureLibUtil;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;

public class EntityCQRMinotaur extends AbstractEntityCQR implements IAnimatableCQR {

	public EntityCQRMinotaur(EntityType<? extends AbstractEntityCQR> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public double getBaseHealth() {
		return CQRConfig.SERVER_CONFIG.baseHealths.minotaur.get();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.UNDEAD;
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
