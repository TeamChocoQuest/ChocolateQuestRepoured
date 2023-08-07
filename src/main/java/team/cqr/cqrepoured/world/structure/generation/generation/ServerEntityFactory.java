package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

public class ServerEntityFactory implements IEntityFactory {

	private final ServerLevel level;

	public ServerEntityFactory(ServerLevel level) {
		this.level = level;
	}

	@Override
	public <T extends Entity> T createEntity(EntityType<T> entityType) {
		return entityType.create(level);
	}

	@Override
	public <T extends Entity> T createEntity(Function<Level, T> entityConstructor) {
		return entityConstructor.apply(level);
	}

	@Nullable
	public SpawnGroupData finalizeSpawn(Mob entity, BlockPos pos, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag spawnTag) {
		return ForgeEventFactory.onFinalizeSpawn(entity, level, level.getCurrentDifficultyAt(pos), spawnType, spawnData, spawnTag);
	}

}
