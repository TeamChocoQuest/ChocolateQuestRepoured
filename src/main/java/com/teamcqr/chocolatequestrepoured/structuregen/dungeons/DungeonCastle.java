package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.CastleGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.RandomCastleConfigOptions;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.EnumRoomType;
import com.teamcqr.chocolatequestrepoured.util.CQRWeightedRandom;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

/**
 * Copyright (c) 25.05.2019
 * Developed by KalgogSmash
 * GitHub: https://github.com/KalgogSmash
 */
public class DungeonCastle extends DungeonBase {

	private int maxSize;
	private int roomSize;
	private int floorHeight;
	private Block wallBlock;
	private Block roofBlock;
	private Block floorBlock;
	private Block stairBlock;

	private CQRWeightedRandom<RandomCastleConfigOptions.RoofType> roofTypeRandomizer;
	private CQRWeightedRandom<RandomCastleConfigOptions.WindowType> windowTypeRandomizer;
	private CQRWeightedRandom<EnumRoomType> roomRandomizer;

	private int minSpawnerRolls;
	private int maxSpawnerRolls;
	private int spawnerRollChance;

	public DungeonCastle(String name, Properties prop) {
		super(name, prop);

		this.maxSize = PropertyFileHelper.getIntProperty(prop, "maxSize", 60);
		this.roomSize = PropertyFileHelper.getIntProperty(prop, "roomSize", 10);
		this.floorHeight = PropertyFileHelper.getIntProperty(prop, "floorHeight", 8);

		this.wallBlock = PropertyFileHelper.getBlockProperty(prop, "wallblock", Blocks.STONEBRICK);
		this.floorBlock = PropertyFileHelper.getBlockProperty(prop, "floorblock", Blocks.PLANKS);
		this.roofBlock = PropertyFileHelper.getBlockProperty(prop, "roofblock", Blocks.OAK_STAIRS);
		this.stairBlock = PropertyFileHelper.getBlockProperty(prop, "stairblock", Blocks.STONE_BRICK_STAIRS);

		this.roomRandomizer = new CQRWeightedRandom<>(this.random);
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
		weight = PropertyFileHelper.getIntProperty(prop, "roomWeightPortal", 1);
		this.roomRandomizer.add(EnumRoomType.PORTAL, weight);

		this.roofTypeRandomizer = new CQRWeightedRandom<>(this.random);
		weight = PropertyFileHelper.getIntProperty(prop, "roofWeightTwoSided", 1);
		this.roofTypeRandomizer.add(RandomCastleConfigOptions.RoofType.TWO_SIDED, weight);
		weight = PropertyFileHelper.getIntProperty(prop, "roofWeightFourSided", 1);
		this.roofTypeRandomizer.add(RandomCastleConfigOptions.RoofType.FOUR_SIDED, weight);

		this.windowTypeRandomizer = new CQRWeightedRandom<>(this.random);
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
	}

	@Override
	public void generate(World world, int x, int y, int z) {
		IDungeonGenerator generator = new CastleGenerator(this);
		generator.generate(world, world.getChunkFromChunkCoords(x >> 4, z >> 4), x, y, z);
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
		return this.roomRandomizer.next();
	}

	public RandomCastleConfigOptions.RoofType getRandomRoofType() {
		return this.roofTypeRandomizer.next();
	}

	public RandomCastleConfigOptions.WindowType getRandomWindowType() {
		return this.windowTypeRandomizer.next();
	}

	public int randomizeRoomSpawnerCount() {
		int numRolls;
		int result = 0;
		if (this.minSpawnerRolls >= this.maxSpawnerRolls) {
			numRolls = this.minSpawnerRolls;
		} else {
			numRolls = DungeonGenUtils.getIntBetweenBorders(this.minSpawnerRolls, this.maxSpawnerRolls, this.random);
		}

		for (int i = 0; i < numRolls; i++) {
			if (this.spawnerRollChance > 0) {
				if (DungeonGenUtils.PercentageRandom(this.spawnerRollChance, this.random)) {
					result++;
				}
			}
		}

		return result;
	}
}
