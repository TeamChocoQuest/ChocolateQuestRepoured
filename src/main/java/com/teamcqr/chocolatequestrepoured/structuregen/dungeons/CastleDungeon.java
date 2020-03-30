package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.CastleGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.RandomCastleConfigOptions;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.EnumRoomType;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import com.teamcqr.chocolatequestrepoured.util.CQRWeightedRandom;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import scala.Int;

/**
 * Copyright (c) 25.05.2019
 * Developed by KalgogSmash
 * GitHub: https://github.com/KalgogSmash
 */
public class CastleDungeon extends DungeonBase {
	private int maxSize = 10;
	private int roomSize = 10;
	private int floorHeight = 8;
	private Random random;
	private Block wallBlock = Blocks.STONEBRICK;
	private Block roofBlock = Blocks.OAK_STAIRS;
	private Block floorBlock = Blocks.PLANKS;
	private Block stairBlock = Blocks.STONE_BRICK_STAIRS;

	private CQRWeightedRandom<RandomCastleConfigOptions.RoofType> roofTypeRandomizer;
	private CQRWeightedRandom<RandomCastleConfigOptions.WindowType> windowTypeRandomizer;
	private CQRWeightedRandom<EnumRoomType> roomRandomizer;

	private int minSpawnerRolls = 1;
	private int maxSpawnerRolls = 3;
	private int spawnerRollChance = 100;

	@Override
	public IDungeonGenerator getGenerator() {
		return new CastleGenerator(this);
	}

	public CastleDungeon(File configFile) {
		super(configFile);
		Properties prop = this.loadConfig(configFile);
		if (prop != null) {
			this.maxSize = PropertyFileHelper.getIntProperty(prop, "maxSize", 60);
			this.roomSize = PropertyFileHelper.getIntProperty(prop, "roomSize", 10);
			this.floorHeight = PropertyFileHelper.getIntProperty(prop, "floorHeight", 8);

			this.wallBlock = PropertyFileHelper.getBlockProperty(prop, "wallblock", Blocks.STONEBRICK);
			this.floorBlock = PropertyFileHelper.getBlockProperty(prop, "floorblock", Blocks.PLANKS);
			this.roofBlock = PropertyFileHelper.getBlockProperty(prop, "roofblock", Blocks.OAK_STAIRS);
			this.stairBlock = PropertyFileHelper.getBlockProperty(prop, "stairblock", Blocks.STONE_BRICK_STAIRS);

			this.random = new Random();

			this.roomRandomizer = new CQRWeightedRandom<>(random);
			int weight = PropertyFileHelper.getIntProperty(prop, "roomWeightAlchemyLab", 1);
			this.roomRandomizer.add(EnumRoomType.ALCHEMY_LAB, weight);
			weight = PropertyFileHelper.getIntProperty(prop, "roomWeightArmory", 1);
			this.roomRandomizer.add(EnumRoomType.ARMORY, weight);
			weight = PropertyFileHelper.getIntProperty(prop, "roomWeightBedroomBasic", 1);
			this.roomRandomizer.add(EnumRoomType.BEDROOM_BASIC, weight);
			weight = PropertyFileHelper.getIntProperty(prop, "roomWeightBedroomFancy", 1);
			this.roomRandomizer.add(EnumRoomType.BEDROOM_FANCY, weight);
			weight = PropertyFileHelper.getIntProperty(prop, "roomWeightKitchen", 1);
			this.roomRandomizer.add(EnumRoomType.KITCHEN, weight);
			weight = PropertyFileHelper.getIntProperty(prop, "roomWeightLibrary", 1);
			this.roomRandomizer.add(EnumRoomType.LIBRARY, weight);
			weight = PropertyFileHelper.getIntProperty(prop, "roomWeightPool", 1);
			this.roomRandomizer.add(EnumRoomType.POOL, weight);

			this.roofTypeRandomizer = new CQRWeightedRandom<>(random);
			weight = PropertyFileHelper.getIntProperty(prop, "roofWeightTwoSided", 1);
			this.roofTypeRandomizer.add(RandomCastleConfigOptions.RoofType.TWO_SIDED, weight);
			weight = PropertyFileHelper.getIntProperty(prop, "roofWeightFourSided", 1);
			this.roofTypeRandomizer.add(RandomCastleConfigOptions.RoofType.FOUR_SIDED, weight);

			this.windowTypeRandomizer = new CQRWeightedRandom<>(random);
			weight = PropertyFileHelper.getIntProperty(prop, "windowWeightBasicGlass", 1);
			this.windowTypeRandomizer.add(RandomCastleConfigOptions.WindowType.BASIC_GLASS, weight);
			weight = PropertyFileHelper.getIntProperty(prop, "windowWeightCrossGlass", 1);
			this.windowTypeRandomizer.add(RandomCastleConfigOptions.WindowType.CROSS_GLASS, weight);
			weight = PropertyFileHelper.getIntProperty(prop, "windowWeightSquareBars", 1);
			this.windowTypeRandomizer.add(RandomCastleConfigOptions.WindowType.SQUARE_BARS, weight);
			weight = PropertyFileHelper.getIntProperty(prop, "windowWeightOpenSlit", 1);
			this.windowTypeRandomizer.add(RandomCastleConfigOptions.WindowType.OPEN_SLIT, weight);

			this.minSpawnerRolls = PropertyFileHelper.getIntProperty(prop, "minSpawnerRolls", 1);
			this.maxSpawnerRolls = PropertyFileHelper.getIntProperty(prop, "maxSpawnerRolls", 3);
			this.spawnerRollChance = PropertyFileHelper.getIntProperty(prop, "spawnerRollChance", 100);

			this.closeConfigFile();
		} else {
			this.registeredSuccessful = false;
		}
	}

	@Override
	protected void generate(int x, int z, World world, Chunk chunk, Random random) {
		super.generate(x, z, world, chunk, random);

		this.generator = new CastleGenerator(this);

		// Generating it...
		int y = this.getHighestAreaY(chunk, x, z);
		System.out.println("Generating structure " + this.name + " at X: " + x + "  Y: " + y + "  Z: " + z + "  ...");
		this.generator.generate(world, chunk, x, y, z);
	}

	public int getHighestAreaY(Chunk chunk, int nwCornerX, int nwCornerZ) {
		int highestY = Int.MinValue();
		for (int x = 0; x < this.maxSize; x++) {
			for (int z = 0; z < this.maxSize; z++) {
				int y = DungeonGenUtils.getHighestYAt(chunk, (nwCornerX + x), (nwCornerZ + z), false);
				if (y > highestY) {
					highestY = y;
				}
			}
		}

		return highestY;
	}

	public Block getWallBlock() {
		return this.wallBlock;
	}

	public Block getFloorBlock() {
		return this.floorBlock;
	}

	public Block getRoofBlock() {
		return this.roofBlock;
	}

	public Block getStairBlock() {
		return this.stairBlock;
	}

	public int getMaxSize() {
		return this.maxSize;
	}

	public int getRoomSize() {
		return this.roomSize;
	}

	public int getFloorHeight() {
		return this.floorHeight;
	}

	public Random getRandom() {
		return this.random;
	}

	public EnumRoomType getRandomRoom() {
		return roomRandomizer.next();
	}

	public RandomCastleConfigOptions.RoofType getRandomRoofType() {
		return roofTypeRandomizer.next();
	}

	public RandomCastleConfigOptions.WindowType getRandomWindowType() {
		return windowTypeRandomizer.next();
	}

	public int randomizeRoomSpawnerCount() {
		int numRolls;
		int result = 0;
		if (minSpawnerRolls >= maxSpawnerRolls) {
			numRolls = minSpawnerRolls;
		} else {
			numRolls = DungeonGenUtils.getIntBetweenBorders(minSpawnerRolls, maxSpawnerRolls, random);
		}

		for (int i = 0; i < numRolls; i++) {
			if (spawnerRollChance > 0) {
				if (DungeonGenUtils.PercentageRandom(spawnerRollChance, random)) {
					result++;
				}
			}
		}

		return result;
	}
}
