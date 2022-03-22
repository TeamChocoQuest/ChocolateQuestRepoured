package team.cqr.cqrepoured.world.structure.generation.generation.part;

import net.minecraft.world.World;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.GeneratableEntityInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableEntityInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EntityDungeonPart implements IDungeonPart {

	private final List<GeneratableEntityInfo> entities;

	protected EntityDungeonPart(Collection<GeneratableEntityInfo> entities) {
		this.entities = new ArrayList<>(entities);
	}

	@Override
	public void generate(World world, GeneratableDungeon dungeon) {
		for (GeneratableEntityInfo entity : this.entities) {
			entity.generate(world, dungeon);
		}
	}

	public Collection<GeneratableEntityInfo> getEntities() {
		return Collections.unmodifiableCollection(this.entities);
	}

	public static class Builder implements IDungeonPartBuilder {

		private final List<PreparableEntityInfo> entities = new ArrayList<>();

		public Builder add(PreparableEntityInfo entity) {
			this.entities.add(entity);
			return this;
		}

		public Builder addAll(Collection<PreparableEntityInfo> entitys) {
			this.entities.addAll(entitys);
			return this;
		}

		@Override
		public EntityDungeonPart build(World world, DungeonPlacement placement) {
			return new EntityDungeonPart(this.entities.stream().map(preparable -> preparable.prepare(world, placement)).collect(Collectors.toList()));
		}

	}

}
