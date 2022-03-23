package team.cqr.cqrepoured.mixin;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.monster.ShulkerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;

@Mixin(ShulkerEntity.class)
public class MixinShulkerEntity {

	@Inject(at = @At("HEAD"), method = "getMobType()Lnet/minecraft/entity/CreatureAttribute;")
	private void mixinGetMobType(CallbackInfoReturnable<CreatureAttribute> cbir) {
		cbir.setReturnValue(CQRCreatureAttributes.VOID);
	}
	
}
