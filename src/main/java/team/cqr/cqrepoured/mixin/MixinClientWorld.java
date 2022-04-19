package team.cqr.cqrepoured.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraftforge.entity.PartEntity;
import team.cqr.cqrepoured.entity.CQRPartEntity;

@Mixin(ClientWorld.class)
public abstract class MixinClientWorld {
	
	@Inject(
			at = @At("RETURN"),
			method = "entitiesForRendering()Ljava/lang/Iterable;",
			cancellable = true
	)
	private void mixinRenderLevel(CallbackInfoReturnable<Iterable<Entity>> cir) {
		List<Entity> list = new ArrayList<>();
		Iterable<Entity> iter = cir.getReturnValue();
		//COmmented out until fixed, currently causes the game to crash cause it leads to it assigning the geckolib renderer to the golem parts?!?!
		iter.forEach(entity -> {
			list.add(entity);
			if(entity.isMultipartEntity() && entity.getParts() != null) {
				for(PartEntity<?> part : entity.getParts()) {
					if(part instanceof CQRPartEntity) {
						list.add(part);
					}
				}
			}
		});
		cir.setReturnValue(iter);
	}

}
