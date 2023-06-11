package team.cqr.cqrepoured.entity.mobs;

import java.util.Set;

import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;

public class EntityCQRDummy extends AbstractEntityCQR implements IAnimatableCQR {

	public EntityCQRDummy(EntityType<? extends AbstractEntityCQR> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public double getBaseHealth() {
		return CQRConfig.SERVER_CONFIG.baseHealths.dummy.get();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.ALL_ALLY;
	}

	@Override
	protected void registerGoals() {

	}

	@Override
	public boolean isPushable() {
		return false;
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return false;
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
