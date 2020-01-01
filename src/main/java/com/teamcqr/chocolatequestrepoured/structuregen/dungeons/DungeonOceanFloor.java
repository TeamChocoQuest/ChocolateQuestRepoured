package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.OceanFloorGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.init.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class DungeonOceanFloor extends DefaultSurfaceDungeon {

	public DungeonOceanFloor(File configFile) {
		super(configFile);
	}

	@Override
	public IDungeonGenerator getGenerator() {
		return new OceanFloorGenerator();
	}

	@Override
	protected void generate(int x, int z, World world, Chunk chunk, Random random) {
		// super.generate(x, z, world, chunk, random);

		File structure = this.pickStructure();
		CQStructure dungeon = new CQStructure(structure, this, chunk.x, chunk.z, this.protectFromDestruction);

		PlacementSettings settings = new PlacementSettings();
		settings.setMirror(Mirror.NONE);
		settings.setRotation(Rotation.NONE);
		settings.setReplacedBlock(Blocks.STRUCTURE_VOID);
		settings.setIntegrity(1.0F);

		int y = DungeonGenUtils.getHighestYAt(chunk, x, z, true);
		// For position locked dungeons, use the positions y
		if (this.isPosLocked()) {
			y = this.getLockedPos().getY();
		}

		System.out.println("Placing dungeon: " + this.name);
		System.out.println("Generating structure " + structure.getName() + " at X: " + x + "  Y: " + y + "  Z: " + z + "  ...");
		OceanFloorGenerator generator = new OceanFloorGenerator(this, dungeon, settings);
		generator.generate(world, chunk, x, y, z);
	}

}
