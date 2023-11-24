package team.cqr.cqrepoured.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import mod.azure.azurelib.animatable.GeoBlockEntity;
import mod.azure.azurelib.constant.DefaultAnimations;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.util.GeckoLibUtil;
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
