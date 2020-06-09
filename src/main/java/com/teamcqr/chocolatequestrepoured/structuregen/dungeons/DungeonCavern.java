package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.init.ModLoottables;
import com.teamcqr.chocolatequestrepoured.objects.factories.SpawnerFactory;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonGenerationManager;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartBlock;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.AbstractDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.GeneratorCavern;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.EDefaultInhabitants;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.AbstractBlockInfo;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.BlockInfo;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.storage.loot.LootTableList;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class DungeonCavern extends DungeonBase {

	private int minRooms = 1;
	private int maxRooms = 10;
	private int minY = 10;
	private int maxY = 40;
	private int minCaveSize = 10;
	private int maxCaveSize = 28;
	private int minHeight = 6;
	private int maxHeight = 12;
	private int maxRoomDistance = 16;
	private int minRoomDistance = 10;
	private int chestChancePerRoom = 100;

	private boolean placeSpawners = true;
	private boolean placeBoss = true;
	private boolean lootChests = true;
	private String mobName = "minecraft:zombie";
	private String bossMobName = "minecraft:wither";
	private Block floorMaterial = Blocks.STONE;
	private Block airBlock = Blocks.AIR;
	private ResourceLocation[] chestIDs;

	public DungeonCavern(String name, Properties prop) {
		super(name, prop);

		this.enableProtectionSystem = false;

		this.minRooms = PropertyFileHelper.getIntProperty(prop, "minRooms", 1);
		this.maxRooms = PropertyFileHelper.getIntProperty(prop, "maxRooms", 8);

		this.minY = PropertyFileHelper.getIntProperty(prop, "minY", 30);
		this.maxY = PropertyFileHelper.getIntProperty(prop, "maxY", 50);

		this.minCaveSize = PropertyFileHelper.getIntProperty(prop, "minCaveSize", 5);
		this.maxCaveSize = PropertyFileHelper.getIntProperty(prop, "maxCaveSize", 15);

		this.minHeight = PropertyFileHelper.getIntProperty(prop, "minCaveHeight", 4);
		this.maxHeight = PropertyFileHelper.getIntProperty(prop, "maxCaveHeight", 12);

		this.maxRoomDistance = PropertyFileHelper.getIntProperty(prop, "maxRoomDistance", 20);
		this.minRoomDistance = PropertyFileHelper.getIntProperty(prop, "minRoomDistance", 12);

		this.chestChancePerRoom = PropertyFileHelper.getIntProperty(prop, "chestChancePerRoom", 50);

		this.placeBoss = PropertyFileHelper.getBooleanProperty(prop, "spawnBoss", false);
		this.placeSpawners = PropertyFileHelper.getBooleanProperty(prop, "placeSpawners", true);
		this.lootChests = PropertyFileHelper.getBooleanProperty(prop, "lootchests", true);

		this.mobName = prop.getProperty("mobname", "minecraft:zombie");
		this.bossMobName = prop.getProperty("bossmobname", "minecraft:pig");

		this.underGroundOffset = 0;

		this.floorMaterial = PropertyFileHelper.getBlockProperty(prop, "floorblock", Blocks.STONE);

		this.airBlock = PropertyFileHelper.getBlockProperty(prop, "airblock", Blocks.AIR);
		this.chestIDs = PropertyFileHelper.getResourceLocationArrayProperty(prop, "chestIDs", new ResourceLocation[] { LootTableList.CHESTS_ABANDONED_MINESHAFT, LootTableList.CHESTS_NETHER_BRIDGE, ModLoottables.CHESTS_FOOD });
	}

	@Override
	public AbstractDungeonGenerator<DungeonCavern> createDungeonGenerator(World world, int x, int y, int z) {
		return null;
	}

	@Override
	public void generate(World world, int x, int z) {
		this.generate(world, x, DungeonGenUtils.getIntBetweenBorders(this.minY, this.maxY, this.random), z);
	}

	@Override
	public void generate(World world, int x, int y, int z) {
		List<GeneratorCavern> caves = new ArrayList<>();
		Map<GeneratorCavern, Integer> xMap = new HashMap<>();
		Map<GeneratorCavern, Integer> zMap = new HashMap<>();

		int rooms = DungeonGenUtils.getIntBetweenBorders(this.minRooms, this.maxRooms, this.random);
		int roomIndex = 1;

		BlockPos centerLoc = new BlockPos(x, y, z);
		DungeonGenerator dungeonGenerator = new DungeonGenerator(world, centerLoc, this.name);

		// int OrigX = new Integer(x);
		// int OrigZ = new Integer(z);

		Vec3i distance = new Vec3i(0, 0, 0);

		do {
			x += distance.getX();
			z += distance.getZ();

			GeneratorCavern cave = new GeneratorCavern(world, centerLoc.add(distance.getX(), 0, distance.getZ()), this, dungeonGenerator);
			// Let the cave calculate its air blocks...
			cave.setSizeAndHeight(DungeonGenUtils.getIntBetweenBorders(this.minCaveSize, this.maxCaveSize, this.random), DungeonGenUtils.getIntBetweenBorders(this.minCaveSize, this.maxCaveSize, this.random), DungeonGenUtils.getIntBetweenBorders(this.minHeight, this.maxHeight, this.random));
			cave.preProcess();

			int vLength = DungeonGenUtils.getIntBetweenBorders(this.minRoomDistance, this.maxRoomDistance, this.random);
			distance = new Vec3i(vLength, 0, 0);
			double angle = ((Integer) new Random().nextInt(360)).doubleValue();
			distance = VectorUtil.rotateVectorAroundY(distance, angle);

			caves.add(cave);
			xMap.put(cave, x);
			zMap.put(cave, z);
			// System.out.println("cave #" + roomIndex + " @ x=" + x + " z=" + z);
			roomIndex++;
		} while (roomIndex < rooms);

		for (int i = 0; i < caves.size(); i++) {
			GeneratorCavern cave = caves.get(i);
			// Dig out the cave...
			cave.buildStructure();

			// connect the tunnels
			Map<BlockPos, IBlockState> stateMap = new HashMap<>();
			cave.generateTunnel(centerLoc.add(0, 1, 0), cave.getCenter(), world, stateMap);

			List<AbstractBlockInfo> blockInfoList = new ArrayList<>();
			for (Map.Entry<BlockPos, IBlockState> entry : stateMap.entrySet()) {
				blockInfoList.add(new BlockInfo(entry.getKey().subtract(cave.getCenter()), entry.getValue(), null));
			}
			dungeonGenerator.add(new DungeonPartBlock(world, dungeonGenerator, cave.getCenter(), blockInfoList, new PlacementSettings(), EDefaultInhabitants.ZOMBIE));
		}
		for (int i = 0; i < caves.size(); i++) {
			GeneratorCavern cave = caves.get(i);

			// Place a loot chest....
			if (this.lootChests && DungeonGenUtils.PercentageRandom(this.chestChancePerRoom, world.getSeed())) {
				cave.fillChests();
			}

			// Place a spawner...
			if (this.placeSpawners) {
				cave.placeSpawners();
			}
		}
		int bossCaveIndx = this.random.nextInt(caves.size());
		if (this.placeBoss) {
			List<AbstractBlockInfo> blockInfoList = new ArrayList<>();

			BlockPos bossPos = new BlockPos(xMap.get(caves.get(bossCaveIndx)), y + 1, zMap.get(caves.get(bossCaveIndx)));

			// BOSS CHEST
			IBlockState state = Blocks.CHEST.getDefaultState();
			TileEntityChest bossChest = (TileEntityChest) Blocks.CHEST.createTileEntity(world, state);
			bossChest.setLootTable(LootTableList.CHESTS_END_CITY_TREASURE, world.getSeed());
			blockInfoList.add(new BlockInfo(BlockPos.ORIGIN.down(), state, bossChest.writeToNBT(new NBTTagCompound())));

			// BOSS SPAWNER
			// DONE: spawn the boss
			IBlockState state2 = ModBlocks.SPAWNER.getDefaultState();
			TileEntitySpawner tileSpawner = (TileEntitySpawner) ModBlocks.SPAWNER.createTileEntity(world, state2);
			tileSpawner.inventory.setStackInSlot(0, SpawnerFactory.getSoulBottleItemStackForEntity(EntityList.createEntityByIDFromName(this.getBossMob(), world)));
			blockInfoList.add(new BlockInfo(BlockPos.ORIGIN, state, tileSpawner.writeToNBT(new NBTTagCompound())));

			dungeonGenerator.add(new DungeonPartBlock(world, dungeonGenerator, bossPos, blockInfoList, new PlacementSettings(), EDefaultInhabitants.ZOMBIE));
		}
		DungeonGenerationManager.addStructure(world, dungeonGenerator, this);
	}

	int getMinCaveHeight() {
		return this.minHeight;
	}

	int getMaxCaveHeight() {
		return this.maxHeight;
	}

	int getMinCaveSize() {
		return this.minCaveSize;
	}

	int getMaxCaveSize() {
		return this.maxCaveSize;
	}

	public Block getAirBlock() {
		return this.airBlock;
	}

	public Block getFloorBlock() {
		return this.floorMaterial;
	}

	public ResourceLocation getBossMob() {
		String[] bossString = this.bossMobName.split(":");
		// System.out.println("Domain: " + bossString[0]);
		// System.out.println("Path: " + bossString[1]);

		return new ResourceLocation(bossString[0], bossString[1]);
	}

	public ResourceLocation getMob() {
		String[] bossString = this.mobName.split(":");
		// System.out.println("Domain: " + bossString[0]);
		// System.out.println("Path: " + bossString[1]);

		return new ResourceLocation(bossString[0], bossString[1]);
	}

	public ResourceLocation[] getChestIDs() {
		return this.chestIDs;
	}

}
