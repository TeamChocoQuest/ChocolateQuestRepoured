package team.cqr.cqrepoured.world.structure.generation.generation.part;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.util.NBTCollectors;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.GeneratableEntityInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.part.IDungeonPart.Registry.ISerializer;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableEntityInfo;

public class EntityDungeonPart implements IDungeonPart {

	private final Queue<GeneratableEntityInfo> entities;

	protected EntityDungeonPart(Collection<GeneratableEntityInfo> entities) {
		this.entities = new ArrayDeque<>(entities);
	}

	@Override
	public void generate(World world, GeneratableDungeon dungeon) {
		if (!this.entities.isEmpty()) {
			this.entities.remove().generate(world, dungeon);
		}
	}

	@Override
	public boolean isGenerated() {
		return this.entities.isEmpty();
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

	public static class Serializer implements ISerializer<EntityDungeonPart> {

		@Override
		public NBTTagCompound write(EntityDungeonPart part, NBTTagCompound compound) {
			compound.setTag("entities", part.entities.stream().map(GeneratableEntityInfo::writeToNBT).collect(NBTCollectors.toList()));
			return compound;
		}

		@Override
		public EntityDungeonPart read(World world, NBTTagCompound compound) {
			List<GeneratableEntityInfo> entities = new ArrayList<>();
			for (NBTBase nbt : compound.getTagList("entities", Constants.NBT.TAG_COMPOUND)) {
				entities.add(new GeneratableEntityInfo(world, (NBTTagCompound) nbt));
			}
			return new EntityDungeonPart(entities);
		}

	}

}
