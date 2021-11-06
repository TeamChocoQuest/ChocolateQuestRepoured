package team.cqr.cqrepoured.structuregen.generation.generatable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import team.cqr.cqrepoured.structuregen.generation.GeneratableDungeon;

public class GeneratableEntityInfo implements IGeneratable {

	private final Entity entity;

	public GeneratableEntityInfo(Entity entity) {
		this.entity = entity;
	}

	public GeneratableEntityInfo(World world, NBTTagCompound compound) {
		this.entity = EntityList.createEntityFromNBT(compound, world);
	}

	@Override
	public void generate(World world, GeneratableDungeon dungeon) {
		world.spawnEntity(this.entity);
	}

	public Entity getEntity() {
		return this.entity;
	}

	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		this.entity.writeToNBTAtomically(compound);
		return compound;
	}

}
