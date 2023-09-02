package team.cqr.cqrepoured.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.util.GeckoLibUtil;
import team.cqr.cqrepoured.init.CQRBlockEntities;

public class TileEntityForceFieldNexus extends BlockEntity implements GeoBlockEntity {

	private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
	
	public TileEntityForceFieldNexus(BlockPos pos, BlockState state) {
		super(CQRBlockEntities.FORCE_FIELD_NEXUS.get(), pos, state);
	}

	@Override
	public void registerControllers(ControllerRegistrar data) {
		data.add(new AnimationController<TileEntityForceFieldNexus>(this, "controller", 0, state -> {
			return state.setAndContinue(DefaultAnimations.IDLE);
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return factory;
	}

}
