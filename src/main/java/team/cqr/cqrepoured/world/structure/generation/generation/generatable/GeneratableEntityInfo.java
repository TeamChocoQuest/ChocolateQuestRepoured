package team.cqr.cqrepoured.world.structure.generation.generation.generatable;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;

public class GeneratableEntityInfo {

	private final Entity entity;

	public GeneratableEntityInfo(Entity entity) {
		this.entity = entity;
	}

	public void spawn(World world, GeneratableDungeon dungeon) {
		world.spawnEntity(this.entity);
	}

	public Entity getEntity() {
		return this.entity;
	}

}
