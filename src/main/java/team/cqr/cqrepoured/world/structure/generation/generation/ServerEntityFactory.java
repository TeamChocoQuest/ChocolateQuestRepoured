package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ServerEntityFactory implements IEntityFactory {

	private final ServerWorld level;

	public ServerEntityFactory(ServerWorld level) {
		this.level = level;
	}

	@Override
	public <T extends Entity> T createEntity(EntityType<T> entityType) {
		return entityType.create(level);
	}

	@Override
	public <T extends Entity> T createEntity(Function<World, T> entityConstructor) {
		return entityConstructor.apply(level);
	}

	@Nullable
	public ILivingEntityData finalizeSpawn(MobEntity entity, BlockPos pos, SpawnReason pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundNBT pDataTag) {
		return entity.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), pReason, pSpawnData, pDataTag);
	}

}
