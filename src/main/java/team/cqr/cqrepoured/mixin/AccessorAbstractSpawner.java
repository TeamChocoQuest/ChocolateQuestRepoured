package team.cqr.cqrepoured.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.world.spawner.AbstractSpawner;

@Mixin(AbstractSpawner.class)
public interface AccessorAbstractSpawner {
	
	@Accessor
	public List<WeightedSpawnerEntity> getSpawnPotentials();
	
}
