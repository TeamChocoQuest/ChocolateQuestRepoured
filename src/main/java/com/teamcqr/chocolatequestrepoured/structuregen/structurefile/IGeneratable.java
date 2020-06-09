package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public interface IGeneratable {

	public void generate(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, String dungeonMob, ProtectedRegion protectedRegion);

}
