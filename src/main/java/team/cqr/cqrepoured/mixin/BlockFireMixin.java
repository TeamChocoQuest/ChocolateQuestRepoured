package team.cqr.cqrepoured.mixin;

import net.minecraft.block.BlockFire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegionHelper;

import java.util.Random;

/**
 * @author ZZZank
 */
@Mixin(BlockFire.class)
public abstract class BlockFireMixin {

    /**
     * Migrated from ASM, see <a href="https://github.com/TeamChocoQuest/ChocolateQuestRepoured/commit/de90412436e00c87c66b4603ec3854fc12f42e22#diff-429454fa0ac34f3a65eb80b2a7d6ef1a61b93489b8a5f55535d480b6a39632df">this GitHub commit</a>
     */
    @Inject(method = "updateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockFire;tryCatchFire(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;ILjava/util/Random;ILnet/minecraft/util/EnumFacing;)V"), cancellable = true)
    private void cqr$noFireSpreadInProtectedRegion1(World world, BlockPos pos, IBlockState blockState, Random random, CallbackInfo ci) {
        if (ProtectedRegionHelper.isFireSpreadingPrevented(world, pos, null, false)) {
            ci.cancel();
        }
    }

    /**
     * Migrated from ASM, see <a href="https://github.com/TeamChocoQuest/ChocolateQuestRepoured/commit/de90412436e00c87c66b4603ec3854fc12f42e22#diff-429454fa0ac34f3a65eb80b2a7d6ef1a61b93489b8a5f55535d480b6a39632df">this GitHub commit</a>
     */
    @Inject(method = "tryCatchFire", at = @At("HEAD"), cancellable = true)
    private void cqr$noFireSpreadInProtectedRegion2(
        World world,
        BlockPos pos,
        int p_tryCatchFire_3_,
        Random p_tryCatchFire_4_,
        int p_tryCatchFire_5_,
        EnumFacing p_tryCatchFire_6_,
        CallbackInfo ci
    ) {
        if (ProtectedRegionHelper.isFireSpreadingPrevented(world, pos, null, false)) {
            ci.cancel();
        }
    }
}
