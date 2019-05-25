package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.CastleGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.VillageGenerator;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class CastleDungeon extends DungeonBase {
	private int sizeX = 10;
	private int sizeY = 10;
	private int sizeZ = 10;
	private Block mainBlock = Blocks.STONE;


	@Override
	public IDungeonGenerator getGenerator() {
		return new CastleGenerator(this);
	}


	public CastleDungeon(File configFile) {
		super(configFile);
		boolean success = true;
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
			success = false;
		} catch (IOException e) {
			System.out.println("Unable to read config file: " + configFile.getName());
			e.printStackTrace();
			prop = null;
			configFile = null;
			success = false;
		}
		if(prop != null && configFile != null && fis != null)
		{
			this.sizeX = PropertyFileHelper.getIntProperty(prop, "sizeX", 10);
			this.sizeY = PropertyFileHelper.getIntProperty(prop, "sizeY", 10);
			this.sizeZ = PropertyFileHelper.getIntProperty(prop, "sizeZ", 10);

			this.mainBlock = Blocks.STONE;
			try {
				Block tmp = Block.getBlockFromName(prop.getProperty("floorblock", "minecraft:stone"));
				if(tmp != null) {
					this.mainBlock = tmp;
				}
			} catch(Exception ex) {
				System.out.println("couldnt load floor block! using default value (stone block)...");
			}

			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
				success = false;
			}
		} else {
			success = false;
		}
		if(success) {
			this.registeredSuccessful = true;
		}
	}

	@Override
	protected void generate(int x, int z, World world, Chunk chunk, Random random) {
		super.generate(x, z, world, chunk, random);

		this.generator = new CastleGenerator(this);

		//Generating it...
		int y = DungeonGenUtils.getHighestYAt(chunk, x, z, false);
		System.out.println("Generating structure " + this.name + " at X: " + x + "  Y: " + y + "  Z: " + z + "  ...");
		this.generator.generate(world, chunk, x, y, z);
	}

	public Block getMainBlock() {return this.mainBlock;}
	public int getSizeX() {return this.sizeX;}
	public int getSizeY() {return this.sizeY;}
	public int getSizeZ() {return this.sizeZ;}

}
