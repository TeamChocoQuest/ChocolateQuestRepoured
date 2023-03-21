package team.cqr.cqrepoured.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegionHelper;

@Mixin(FireBlock.class)
public class MixinFireBlock {

	// TODO enable when protection system works

	@Inject(method = "tick", cancellable = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/server/ServerWorld;isHumidAt(Lnet/minecraft/util/math/BlockPos;)Z"))
	public void tick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRand, CallbackInfo info) {
		if (ProtectedRegionHelper.isFireSpreadingPrevented(pLevel, pPos, null, false)) {
			info.cancel();
		}
	}

	@Inject(
			remap = false,
			method = "tryCatchFire(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;ILjava/util/Random;ILnet/minecraft/util/Direction;)V",
			cancellable = true,
			at = @At(
					"HEAD"
			)
	)
	public void tryCatchFire(World pLevel, BlockPos pPos, int pChance, Random pRandom, int pAge, Direction face, CallbackInfo info) {
		if (ProtectedRegionHelper.isFireSpreadingPrevented(pLevel, pPos, null, false)) {
			info.cancel();
		}
	}
	
}
