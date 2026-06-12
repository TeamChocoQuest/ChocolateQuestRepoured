package team.cqr.cqrepoured.mixin;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityPotion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.cqr.cqrepoured.entity.IMechanical;

@Mixin(EntityPotion.class)
public abstract class EntityPotionMixin {

    @Inject(method = "isWaterSensitiveEntity", at = @At("HEAD"), cancellable = true)
    private static void cqr$waterSensibleMechanical(EntityLivingBase entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof IMechanical) {
            cir.setReturnValue(true);
        }
    }
}
