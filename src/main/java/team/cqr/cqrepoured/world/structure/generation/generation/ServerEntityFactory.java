package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

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
	public ILivingEntityData finalizeSpawn(MobEntity entity, BlockPos pos, MobSpawnType pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundTag pDataTag) {
		return entity.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), pReason, pSpawnData, pDataTag);
	}

}
