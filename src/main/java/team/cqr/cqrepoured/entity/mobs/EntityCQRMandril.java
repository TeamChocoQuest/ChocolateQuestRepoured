package team.cqr.cqrepoured.entity.mobs;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;

public class EntityCQRMandril extends AbstractEntityCQR implements IAnimatableCQR {

	public EntityCQRMandril(EntityType<? extends AbstractEntityCQR> type, World worldIn) {
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
	private AnimationFactory factory = new AnimationFactory(this);

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
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
