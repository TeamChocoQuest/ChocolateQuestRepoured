package team.cqr.cqrepoured.entity.mobs;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;

public class EntityCQRTriton extends AbstractEntityCQR implements IAnimatableCQR {

	public EntityCQRTriton(EntityType<? extends AbstractEntityCQR> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public double getBaseHealth() {
		return CQRConfig.SERVER_CONFIG.baseHealths.triton.get();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.TRITONS;
	}

	@Override
	public boolean isSitting() {
		return false;
	}

	@Override
	public int getTextureCount() {
		return 2;
	}

	@Override
	public boolean canMountEntity() {
		return false;
	}

	@Override
	protected float getWaterSlowDown() {
		return 0.0F;
	}

	// Geckolib
	private AnimationFactory factory = GeckoLibUtil.createFactory(this);

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	static final Set<String> ALWAYS_PLAYING_TRITON = new HashSet<>();

	static {
		ALWAYS_PLAYING_TRITON.add("animation.triton.idle.beard");
	}

	@Override
	public Set<String> getAlwaysPlayingAnimations() {
		return ALWAYS_PLAYING_TRITON;
	}
	
	@Override
	public <E extends AbstractEntityCQR & IAnimatableCQR> PlayState predicateWalking(AnimationEvent<E> event) {
		if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.triton.walk", ILoopType.EDefaultLoopTypes.LOOP));
			event.getController().setAnimationSpeed(event.getAnimatable().isSprinting() ? 2.0D : 1.0D);
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
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
