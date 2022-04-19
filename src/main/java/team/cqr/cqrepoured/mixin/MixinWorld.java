package team.cqr.cqrepoured.mixin;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.CQRPartEntity;
import team.cqr.cqrepoured.mixinutil.PartEntityCache;

@Mixin(World.class)
public abstract class MixinWorld {
	
	@Inject(
			at = @At("RETURN"),
			method = "getEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/function/Predicate;)Ljava/util/List;",
			cancellable = true
	)
	private void mixinGetEntitiesExcluding(@Nullable Entity pEntity, AxisAlignedBB pArea, @Nullable Predicate<? super Entity> pPredicate, CallbackInfoReturnable<List<Entity>> cir) {
		synchronized (PartEntityCache.PART_ENTITY_CACHE_CQR) {
			List<CQRPartEntity<?>> parts = PartEntityCache.PART_ENTITY_CACHE_CQR.get((IWorld)this);
			List<Entity> list = cir.getReturnValue();
			if(parts != null) {
				for (CQRPartEntity<?> part : parts) {
					if (part != pEntity &&

							part.getBoundingBox().intersects(pArea) &&

							(pPredicate == null || pPredicate.test(part)) &&

							!list.contains(part))
						list.add(part);
				}
			}
			cir.setReturnValue(list);
		}
		
	}

}
