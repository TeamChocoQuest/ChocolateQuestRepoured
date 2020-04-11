package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.OceanFloorGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class DungeonOceanFloor extends DefaultSurfaceDungeon {

	public DungeonOceanFloor(String name, Properties prop) {
		super(name, prop);
	}

	@Override
	public void generate(World world, int x, int y, int z) {
		File file = this.getStructureFileFromDirectory(this.structureFolderPath);

		if (file != null && file.exists() && file.isFile()) {
			CQStructure structure = new CQStructure(file);
			PlacementSettings settings = new PlacementSettings();

			if (this.rotateDungeon()) {
				settings.setRotation(Rotation.values()[this.random.nextInt(Rotation.values().length)]);
				settings.setMirror(Mirror.values()[this.random.nextInt(Mirror.values().length)]);
			}

			CQRMain.logger.info("Placing dungeon: {}", this.name);
			CQRMain.logger.info("Generating structure {} at X: {}  Y: {}  Z: {}  ...", file.getName(), x, y, z);
			IDungeonGenerator generator = new OceanFloorGenerator(this, structure, settings);
			generator.generate(world, world.getChunkFromChunkCoords(x >> 4, z >> 4), x, y, z);
		}
	}

}
