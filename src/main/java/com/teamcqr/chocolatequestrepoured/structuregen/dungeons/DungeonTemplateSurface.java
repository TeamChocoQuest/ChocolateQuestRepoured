package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.AbstractDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.GeneratorTemplateSurface;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class DungeonTemplateSurface extends DungeonBase {

	protected File structureFolderPath = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER, "test");
	protected boolean rotateDungeon = true;

	public DungeonTemplateSurface(String name, Properties prop) {
		super(name, prop);

		this.structureFolderPath = PropertyFileHelper.getStructureFolderProperty(prop, "structureFolder", "test");
		this.rotateDungeon = PropertyFileHelper.getBooleanProperty(prop, "rotateDungeon", this.rotateDungeon);
	}

	@Override
	public AbstractDungeonGenerator<DungeonTemplateSurface> createDungeonGenerator(World world, int x, int y, int z) {
		return new GeneratorTemplateSurface(world, new BlockPos(x, y, z), this);
	}

	public File getStructureFolderPath() {
		return this.structureFolderPath;
	}

	public boolean rotateDungeon() {
		return this.rotateDungeon;
	}

}
