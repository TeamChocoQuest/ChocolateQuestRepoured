package team.cqr.cqrepoured.tileentity;

import net.minecraft.tileentity.TileEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;
import team.cqr.cqrepoured.init.CQRBlockEntities;

public class TileEntityForceFieldNexus extends TileEntity implements IAnimatable {

	private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
	
	public TileEntityForceFieldNexus() {
		super(CQRBlockEntities.FORCE_FIELD_NEXUS.get());
	}

	private <E extends TileEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		event.getController().setAnimation(new AnimationBuilder().addAnimation("idle.loop", EDefaultLoopTypes.LOOP));
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<TileEntityForceFieldNexus>(this, "controller", 0, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

}
