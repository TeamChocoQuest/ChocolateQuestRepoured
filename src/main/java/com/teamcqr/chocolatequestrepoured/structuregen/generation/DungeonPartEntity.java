package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;

import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EntityInfo;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.common.util.Constants;

public class DungeonPartEntity extends AbstractDungeonPart {

	protected final Deque<EntityInfo> entityInfoList = new LinkedList<>();
	protected PlacementSettings settings;
	protected EDungeonMobType dungeonMobType;

	public DungeonPartEntity(World world, DungeonGenerator dungeonGenerator) {
		this(world, dungeonGenerator, BlockPos.ORIGIN, Collections.emptyList(), new PlacementSettings(), EDungeonMobType.DEFAULT);
	}

	public DungeonPartEntity(World world, DungeonGenerator dungeonGenerator, BlockPos partPos, Collection<EntityInfo> entities, PlacementSettings settings, EDungeonMobType dungeonMobType) {
		super(world, dungeonGenerator, partPos);
		for (EntityInfo entityInfo : entities) {
			if (entityInfo != null) {
				this.updateMinAndMaxPos(partPos.add(entityInfo.getPos()));
				this.entityInfoList.add(entityInfo);
			}
		}
		this.settings = settings;
		this.dungeonMobType = dungeonMobType;
	}

	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = super.writeToNBT();

		compound.setInteger("mirror", this.settings.getMirror().ordinal());
		compound.setInteger("rotation", this.settings.getRotation().ordinal());
		compound.setInteger("mob", this.dungeonMobType.ordinal());

		// Save entities
		NBTTagList nbtTagList = new NBTTagList();
		for (EntityInfo entityInfo : this.entityInfoList) {
			nbtTagList.appendTag(entityInfo.getEntityData());
		}
		compound.setTag("entityInfoList", nbtTagList);

		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.entityInfoList.clear();
		this.settings = new PlacementSettings();
		this.settings.setMirror(Mirror.values()[compound.getInteger("mirror")]);
		this.settings.setRotation(Rotation.values()[compound.getInteger("rotation")]);
		this.dungeonMobType = EDungeonMobType.values()[compound.getInteger("mob")];

		// Load entities
		for (NBTBase nbt : compound.getTagList("entityInfoList", Constants.NBT.TAG_COMPOUND)) {
			this.entityInfoList.add(new EntityInfo((NBTTagCompound) nbt));
		}
	}

	@Override
	public String getId() {
		return DUNGEON_PART_ENTITY_ID;
	}

	@Override
	public void generateNext() {
		if (!this.entityInfoList.isEmpty()) {
			EntityInfo entityInfo = this.entityInfoList.removeFirst();
			entityInfo.generate(this.world, this.dungeonGenerator.getPos(), this.partPos, this.settings, this.dungeonMobType, this.dungeonGenerator.getProtectedRegion());
		}
	}

	@Override
	public boolean isGenerated() {
		return this.entityInfoList.isEmpty();
	}

}
