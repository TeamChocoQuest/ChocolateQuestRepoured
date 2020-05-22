package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import java.util.Collection;

import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.AbstractBlockInfo;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class DungeonPartBlockSpecial extends DungeonPartBlock {

	public static final String ID = "dungeon_part_block_special";

	public DungeonPartBlockSpecial(World world, DungeonGenerator dungeonGenerator, BlockPos partPos, Collection<AbstractBlockInfo> blocks, PlacementSettings settings, EDungeonMobType dungeonMobType) {
		super(world, dungeonGenerator, partPos, blocks, settings, dungeonMobType);
	}

	public DungeonPartBlockSpecial(World world, DungeonGenerator dungeonGenerator, NBTTagCompound compound) {
		super(world, dungeonGenerator, compound);
	}

	@Override
	public String getId() {
		return DungeonPartBlockSpecial.ID;
	}

	@Override
	public void generateNext() {
		while (!this.blockInfoList.isEmpty()) {
			AbstractBlockInfo blockInfo = this.blockInfoList.removeFirst();
			blockInfo.generate(this.world, this.dungeonGenerator.getPos(), this.partPos, this.settings, this.dungeonMobType, this.dungeonGenerator.getProtectedRegion());
		}
	}

}
