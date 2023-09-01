package team.cqr.cqrepoured.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.SpawnData;

@Mixin(BaseSpawner.class)
public interface AccessorAbstractSpawner {
	
	@Accessor
	public SimpleWeightedRandomList<SpawnData> getSpawnPotentials();
	
}
