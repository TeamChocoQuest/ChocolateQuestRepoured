package team.cqr.cqrepoured.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.Entity;
import net.minecraft.world.TrackedEntity;

@Mixin(TrackedEntity.class)
public interface AccessorTrackedEntity {

	@Accessor()
	public Entity getEntity();
	
}
