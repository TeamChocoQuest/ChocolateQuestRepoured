package team.cqr.cqrepoured.entity.mobs;

import java.util.Set;

import net.minecraft.entity.EntityType;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRSounds;

public class EntityCQROgre extends AbstractEntityCQR implements IAnimatableCQR {

	public EntityCQROgre(EntityType<? extends AbstractEntityCQR> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public double getBaseHealth() {
		return CQRConfig.SERVER_CONFIG.baseHealths.ogre.get();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.GOBLINS;
	}

	@Override
	public boolean canMountEntity() {
		return false;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CQRSounds.OGRE_DEATH;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return CQRSounds.OGRE_AMBIENT;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return CQRSounds.OGRE_HURT;
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
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
