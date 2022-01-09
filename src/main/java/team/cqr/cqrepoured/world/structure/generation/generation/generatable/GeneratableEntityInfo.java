package team.cqr.cqrepoured.world.structure.generation.generation.generatable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;

public class GeneratableEntityInfo implements IGeneratable {

	private final Entity entity;

	public GeneratableEntityInfo(Entity entity) {
		this.entity = entity;
	}

	public GeneratableEntityInfo(World world, CompoundNBT compound) {
		this.entity = EntityList.createEntityFromNBT(compound, world);
	}

	@Override
	public void generate(World world, GeneratableDungeon dungeon) {
		world.addFreshEntity(this.entity);
	}

	public Entity getEntity() {
		return this.entity;
	}

	public CompoundNBT writeToNBT() {
		CompoundNBT compound = new CompoundNBT();
		this.entity.saveWithoutId(compound);
		return compound;
	}

}
