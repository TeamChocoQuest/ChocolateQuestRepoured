package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.GeneratorSurface;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class DungeonSurface extends DungeonBase {

	protected File structureFolderPath;

	public DungeonSurface(String name, Properties prop) {
		super(name, prop);

		this.structureFolderPath = PropertyFileHelper.getFileProperty(prop, "structureFolder", "defaultFolder");
	}

	@Override
	public void generate(World world, int x, int y, int z) {
		File file = this.getStructureFileFromDirectory(this.structureFolderPath);

		if (file != null && file.exists() && file.isFile()) {
			CQStructure structure = new CQStructure(file);
			structure.setDungeonMob(this.dungeonMob);
			PlacementSettings settings = new PlacementSettings();

			if (this.rotateDungeon()) {
				settings.setRotation(Rotation.values()[this.random.nextInt(Rotation.values().length)]);
				settings.setMirror(Mirror.values()[this.random.nextInt(Mirror.values().length)]);
			}

			IDungeonGenerator generator = new GeneratorSurface(this, structure, settings);
			generator.generate(world, world.getChunkFromChunkCoords(x >> 4, z >> 4), x, y, z);
		}
	}

}
