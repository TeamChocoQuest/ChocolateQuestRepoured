package team.cqr.cqrepoured.mixin;

import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.world.spawner.AbstractSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(AbstractSpawner.class)
public interface AccessorAbstractSpawner {
	
	@Accessor
	public List<WeightedSpawnerEntity> getSpawnPotentials();
	
}
