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

    /**
     * Migrated from ASM, see <a href="https://github.com/TeamChocoQuest/ChocolateQuestRepoured/commit/e3c691d031e5bab2d29083ac11ab51e9797a1430#diff-000f5f54291581b4f9774d4254c96b5d55ac9ec4ad7bf666d96508442fc76131">this GitHub commit</a> (Add EntityPotion#isWaterSensitiveEntity hook)
     */
    @Inject(method = "isWaterSensitiveEntity", at = @At("HEAD"), cancellable = true)
    private static void cqr$waterSensibleMechanical(EntityLivingBase entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof IMechanical) {
            cir.setReturnValue(true);
        }
    }
}
