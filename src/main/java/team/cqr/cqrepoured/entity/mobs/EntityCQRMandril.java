package team.cqr.cqrepoured.entity.mobs;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import mod.azure.azurelib3.core.manager.AnimationData;
import mod.azure.azurelib3.core.manager.AnimationFactory;
import mod.azure.azurelib3.util.AzureLibUtil;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;

public class EntityCQRMandril extends AbstractEntityCQR implements IAnimatableCQR {

	public EntityCQRMandril(EntityType<? extends AbstractEntityCQR> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(9, new LeapAtTargetGoal(this, 0.6F));
	}

	@Override
	public double getBaseHealth() {
		return CQRConfig.SERVER_CONFIG.baseHealths.mandril.get();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.BEASTS;
	}

	@Override
	public boolean canMountEntity() {
		return false;
	}

	// Geckolib
	private AnimationFactory factory = AzureLibUtil.createFactory(this);

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	static final Set<String> ALWAYS_PLAYING_MANDRIL = new HashSet<>();

	static {
		ALWAYS_PLAYING_MANDRIL.add("animation.biped.tail.idle");
	}

	@Override
	public Set<String> getAlwaysPlayingAnimations() {
		return ALWAYS_PLAYING_MANDRIL;
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
