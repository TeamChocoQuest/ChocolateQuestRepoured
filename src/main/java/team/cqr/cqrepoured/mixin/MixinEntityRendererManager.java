package team.cqr.cqrepoured.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import team.cqr.cqrepoured.entity.CQRPartEntity;

@Mixin(EntityRendererManager.class)
public abstract class MixinEntityRendererManager {
	
	@Inject(
			at = @At("RETURN"),
			method = "getRenderer(Lnet/minecraft/entity/Entity;)Lnet/minecraft/client/renderer/entity/EntityRenderer;",
			cancellable = true
	)
	private void mixinGetRenderer(Entity entityIn, CallbackInfoReturnable<EntityRenderer<? extends Entity>> cir) {
		if(entityIn instanceof CQRPartEntity<?> ) {
			cir.setReturnValue(((CQRPartEntity<?>)entityIn).renderer((EntityRendererManager)(Object)this));
		}
	}

}
