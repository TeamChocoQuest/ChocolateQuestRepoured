package team.cqr.cqrepoured.entity.mobs;

import java.util.Set;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.ai.spells.EntityAIAntiAirSpellWalker;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.init.CQRSounds;

public class EntityCQRWalker extends AbstractEntityCQR implements IAnimatableCQR {

	public EntityCQRWalker(EntityType<? extends AbstractEntityCQR> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public double getBaseHealth() {
		return CQRConfig.SERVER_CONFIG.baseHealths.abyssWalker.get();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.WALKERS;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return CQRSounds.WALKER_AMBIENT;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return CQRSounds.WALKER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CQRSounds.WALKER_DEATH;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.getDirectEntity() instanceof SpectralArrowEntity) {
			amount *= 2;
		}
		return super.hurt(source, amount);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.spellHandler.addSpell(0, new EntityAIAntiAirSpellWalker(this));
	}

	@Override
	public void thunderHit(ServerWorld serverLevel,LightningBoltEntity lightningBolt) {
		this.heal(this.getMaxHealth() * 0.025F);
	}

	@Override
	public CreatureAttribute getMobType() {
		return CQRCreatureAttributes.VOID;
	}

	// Geckolib
	private AnimationFactory factory = GeckoLibUtil.createFactory(this);

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public Set<String> getAlwaysPlayingAnimations() {
		return null;
	}
	
	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	public void registerControllers(AnimationData data) {
		this.registerControllers(this, data);
	}

}
