package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.DefaultSurfaceGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

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
public class DefaultSurfaceDungeon extends DungeonBase {
	
	protected File structureFolderPath; 
	
	public DefaultSurfaceDungeon(File configFile) {
		super(configFile);
		Properties prop = loadConfig(configFile);
		if(prop != null) {
			this.structureFolderPath = PropertyFileHelper.getFileProperty(prop, "structurefolder", "defaultFolder");/*new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER.getAbsolutePath() +  "/" + prop.getProperty("structurefolder", "defaultFolder"));

			if(!this.structureFolderPath.exists() || !this.structureFolderPath.isDirectory()) {
				if(this.structureFolderPath.exists() && !this.structureFolderPath.isDirectory()) {
					this.structureFolderPath.delete();
				}
				this.structureFolderPath.mkdirs();
			}*/
			
			closeConfigFile();
		} else {
			registeredSuccessful = false;
		}
	}
	
	@Override
	public IDungeonGenerator getGenerator() {
		return new DefaultSurfaceGenerator(null, null, null);
	}
	
	protected File pickStructure(Random random) {
		if(this.structureFolderPath == null) {
			return null;
		}
		File chosenStructure = this.structureFolderPath;
		while(chosenStructure.isDirectory()) {
			if(chosenStructure.listFiles().length <= 0) {
				return null;
			}
			File[] files = chosenStructure.listFiles();
			int index = random.nextInt(files.length);
			chosenStructure = files[index];
		}
		return chosenStructure;
	}
	
	@Override
	protected void generate(int x, int z, World world, Chunk chunk, Random random) {
		super.generate(x, z, world, chunk, random);
		File structure = pickStructure(new Random());
		if(structure != null) {
			CQStructure dungeon = new CQStructure(structure, this.protectFromDestruction);
			
			PlacementSettings settings = new PlacementSettings();
			settings.setMirror(Mirror.NONE);
			settings.setRotation(Rotation.NONE);
			settings.setReplacedBlock(Blocks.STRUCTURE_VOID);
			settings.setIntegrity(1.0F);
			
			int y = DungeonGenUtils.getHighestYAt(chunk, x, z, false);
			//For position locked dungeons, use the positions y
			if(this.isPosLocked()) {
				y = this.getLockedPos().getY();
			}
			
			if(this.getUnderGroundOffset() != 0) {
				y -= this.getUnderGroundOffset();
			}
			if(this.yOffset != 0) {
				y += Math.abs(this.yOffset);
			}
			
			System.out.println("Placing dungeon: " + this.name);
			System.out.println("Generating structure " + structure.getName() + " at X: " + x + "  Y: " + y + "  Z: " + z + "  ...");
			DefaultSurfaceGenerator generator = new DefaultSurfaceGenerator(this, dungeon, settings);
			generator.generate(world, chunk, x, y, z);
		}
	}
	
}
