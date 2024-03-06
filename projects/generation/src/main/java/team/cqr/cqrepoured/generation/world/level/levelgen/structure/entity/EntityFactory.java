package team.cqr.cqrepoured.generation.world.level.levelgen.structure.entity;

import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import team.cqr.cqrepoured.CQRMain;

public class EntityFactory {

	private final ServerLevel level;

	public EntityFactory(ServerLevel level) {
		this.level = level;
	}

	public <T extends Entity> T createEntity(EntityType<T> entityType) {
		return entityType.create(level);
	}

	public <T extends Entity> T createEntity(Function<Level, T> entityConstructor) {
		return entityConstructor.apply(level);
	}

	@Nullable
	public Entity createEntity(ResourceLocation registryName) {
		Entity entity = EntityType.byString(registryName.toString()).map(this::createEntity).orElse(null);
		if (entity == null) {
			CQRMain.logger.warn("Skipping Entity with id {}", registryName);
			return null;
		}
		return entity;
	}

	@Nullable
	public Entity createEntity(CompoundTag nbt) {
		Entity entity = EntityType.by(nbt).map(this::createEntity).orElse(null);
		if (entity == null) {
			CQRMain.logger.warn("Skipping Entity with id {}", nbt.getString("id"));
			return null;
		}
		entity.load(nbt);
		return entity;
	}

	@Nullable
	public SpawnGroupData finalizeSpawn(Mob entity, BlockPos pos, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag spawnTag) {
		return ForgeEventFactory.onFinalizeSpawn(entity, level, level.getCurrentDifficultyAt(pos), spawnType, spawnData, spawnTag);
	}

	@Nullable
	public static CompoundTag save(Entity entity) {
		CompoundTag nbt = new CompoundTag();
		return entity.save(nbt) ? nbt : null;
	}

}
