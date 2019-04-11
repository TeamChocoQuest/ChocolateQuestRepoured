package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.DefaultGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.SimplePasteGenerator;
import com.teamcqr.chocolatequestrepoured.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.init.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class DefaultSurfaceDungeon extends DungeonBase {
	
	protected File structureFolderPath; 
	
	public DefaultSurfaceDungeon(File configFile) {
		super(configFile);
		Properties prop = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(configFile);
			prop.load(fis);
		} catch (FileNotFoundException e) {
			System.out.println("Unable to read config file: " + configFile.getName());
			e.printStackTrace();
			prop = null;
			configFile = null;
		} catch (IOException e) {
			System.out.println("Unable to read config file: " + configFile.getName());
			e.printStackTrace();
			prop = null;
			configFile = null;
		}
		if(prop != null && configFile != null && fis != null) {
			super.chance = PropertyFileHelper.getIntProperty(prop, "chance", 0);
			super.name = configFile.getName().replaceAll(".prop", "");
			super.allowedDims = PropertyFileHelper.getIntArrayProperty(prop, "allowedDims", new int[]{0});
			super.unique = PropertyFileHelper.getBooleanProperty(prop, "unique", false);
			this.structureFolderPath = new File(prop.getProperty("structurefolder", "defaultFolder"));
			
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public IDungeonGenerator getGenerator() {
		return new DefaultGenerator();
	}
	
	private File pickStructure(Random random) {
		//Random rdm = new Random();
		//rdm.setSeed(worldSeed);
		File chosenStructure = this.structureFolderPath;
		if(this.structureFolderPath.isDirectory()) {
			chosenStructure = this.structureFolderPath.listFiles()[random.nextInt(this.structureFolderPath.listFiles().length)];
		}
		if(chosenStructure != null) {
			return chosenStructure;
		}
		return null;
	}
	
	@Override
	protected void generate(int x, int z, World world, Chunk chunk, Random random) {
		super.generate(x, z, world, chunk, random);
		File structure = pickStructure(random);
		CQStructure dungeon = new CQStructure(structure);
		
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
		
		System.out.println("Placing dungeon: " + this.name);
		System.out.println("Generating structure " + structure.getName() + " at X: " + x + "  Y: " + y + "  Z: " + z + "  ...");
		SimplePasteGenerator generator = new SimplePasteGenerator(this, dungeon, settings);
		generator.generate(world, chunk, x, y, z);
	}
	
}
