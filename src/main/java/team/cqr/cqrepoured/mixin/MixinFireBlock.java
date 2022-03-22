package team.cqr.cqrepoured.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(FireBlock.class)
public class MixinFireBlock {

	// TODO enable when protection system works

	@Inject(method = "tick", cancellable = true, at = @At(value = "INVOKE", target = "isHumidAt"))
	public void tick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRand, CallbackInfo info) {
		/*if (ProtectedRegionHelper.isFireSpreadingPrevented(pLevel, pPos, null, false)) {
			info.cancel();
		}*/
	}

	@Inject(method = "tryCatchFire", cancellable = true, at = @At("HEAD"))
	public void tryCatchFire(World pLevel, BlockPos pPos, int pChance, Random pRandom, int pAge, Direction face, CallbackInfo info) {
		/*if (ProtectedRegionHelper.isFireSpreadingPrevented(pLevel, pPos, null, false)) {
			info.cancel();
		}*/
	}
	
}
