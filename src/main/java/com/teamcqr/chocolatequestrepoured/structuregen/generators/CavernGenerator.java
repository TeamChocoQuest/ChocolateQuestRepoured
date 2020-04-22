package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCavern;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart.ExtendedBlockState;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.IStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.lootchests.ELootTable;
import com.teamcqr.chocolatequestrepoured.util.Perlin3D;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class CavernGenerator implements IDungeonGenerator {

	public CavernGenerator() {
	};

	public CavernGenerator(DungeonCavern dungeon) {
		this.dungeon = dungeon;
	}

	private int sizeX;
	private int sizeZ;
	private int height;

	private int centerX;
	private int centerZ;
	private int centerY;

	private DungeonCavern dungeon;

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// DONE: calculate air blocks
		Perlin3D perlin1 = new Perlin3D(world.getSeed(), 4, new Random());
		Perlin3D perlin2 = new Perlin3D(world.getSeed(), 32, new Random());
		
		Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> stateMap = new HashMap<>();

		int centerX = this.sizeX / 2;
		int centerY = this.height / 2;
		int centerZ = this.sizeZ / 2;

		this.centerX = centerX + x;
		this.centerY = centerY + y;
		this.centerZ = centerZ + z;

		int centerDistSquared = centerX * centerX + centerY * centerY + centerZ * centerZ;

		float scaleX = 1.0F;
		float scaleY = 1.0F;
		float scaleZ = 1.0F;

		float maxSize = Math.max(this.sizeX, Math.max(this.height, this.sizeZ));

		scaleX += ((maxSize - this.sizeX) / maxSize);
		scaleY += ((maxSize - this.height) / maxSize);
		scaleZ += ((maxSize - this.sizeZ) / maxSize);

		for (int iX = 0; iX < this.sizeX; iX++) {
			for (int iY = 0; iY < this.height; iY++) {
				for (int iZ = 0; iZ < this.sizeZ; iZ++) {
					float noiseAtPos = (iX - centerX) * (iX - centerX) * scaleX * scaleX;
					noiseAtPos += ((iY - centerY) * (iY - centerY) * scaleY * scaleY);
					noiseAtPos += ((iZ - centerZ) * (iZ - centerZ) * scaleZ * scaleZ);

					noiseAtPos /= centerDistSquared;

					double noise = (perlin1.getNoiseAt(x + iX, y + iY, z + iZ) + perlin2.getNoiseAt(x + iX, y + iY, z + iZ)) / 2.0D * (noiseAtPos * 2.5D);

					if (noise < 0.75D) {
						BlockPos block = new BlockPos(x + iX, y + iY, z + iZ);
						stateMap.put(block, new ExtendedBlockStatePart.ExtendedBlockState(dungeon.getAirBlock().getDefaultState(), null));
						if (iY == 0) {
							stateMap.put(block, new ExtendedBlockStatePart.ExtendedBlockState(dungeon.getFloorBlock().getDefaultState(), null));
						}
					}
				}
			}
		}
		lists.add(ExtendedBlockStatePart.splitExtendedBlockStateMap(stateMap));
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Not needed
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Not needed
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		BlockPos start = new BlockPos(x, y, z);
		IBlockState state = Blocks.CHEST.getDefaultState();
		Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> stateMap = new HashMap<>();
		TileEntityChest chest = (TileEntityChest) Blocks.CHEST.createTileEntity(world, state);
		int eltID = new Random().nextInt(14) + 4;
		if (chest != null) {
			ResourceLocation resLoc = null;
			try {
				resLoc = ELootTable.values()[eltID].getResourceLocation();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (resLoc != null) {
				long seed = WorldDungeonGenerator.getSeed(world, x, z);
				chest.setLootTable(resLoc, seed);
			}
			stateMap.put(start, new ExtendedBlockStatePart.ExtendedBlockState(state, chest.writeToNBT(new NBTTagCompound())));
		}
		lists.add(ExtendedBlockStatePart.splitExtendedBlockStateMap(stateMap));
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		BlockPos spawnerPos = new BlockPos(x, y, z);
		Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> stateMap = new HashMap<>();
		
		IBlockState state = Blocks.MOB_SPAWNER.getDefaultState();
		TileEntityMobSpawner spawner = (TileEntityMobSpawner)Blocks.MOB_SPAWNER.createTileEntity(world, state);
		spawner.getSpawnerBaseLogic().setEntityId(dungeon.getMob());
		spawner.updateContainingBlockInfo();
		stateMap.put(spawnerPos, new ExtendedBlockStatePart.ExtendedBlockState(state, spawner.writeToNBT(new NBTTagCompound())));
		
		lists.add(ExtendedBlockStatePart.splitExtendedBlockStateMap(stateMap));
	}

	public void generateTunnel(BlockPos start, BlockPos end, World world, Map<BlockPos, ExtendedBlockState> stateMap) {
		this.generateTunnel(new Random().nextBoolean(), start, end, world, stateMap);
	}

	private void generateTunnel(boolean xFirst, BlockPos start, BlockPos target, World world, Map<BlockPos, ExtendedBlockState> stateMap) {
		if (start.getX() == target.getX() && start.getZ() == target.getZ()) {
			return;
		} else if (start.getX() == target.getX() && xFirst) {
			this.generateTunnel(false, start, target, world, stateMap);
		} else if (start.getZ() == target.getZ() && !xFirst) {
			this.generateTunnel(true, start, target, world, stateMap);
		}
		/*
		 * else if(DungeonGenUtils.PercentageRandom(25, world.getSeed()) && !(start.getX() == target.getX() || start.getZ() == target.getZ())) {
		 * generateTunnel(!xFirst, start, target, world);
		 * }
		 */
		else {
			int v = 0;
			this.buildTunnelSegment(start, world, stateMap);
			if (xFirst) {
				v = start.getX() < target.getX() ? 1 : -1;
				if (start.getX() == target.getX()) {
					v = 0;
				}
				start = start.add(v, 0, 0);
			} else {
				v = start.getZ() < target.getZ() ? 1 : -1;
				if (start.getZ() == target.getZ()) {
					v = 0;
				}
				start = start.add(0, 0, v);
			}
			this.generateTunnel(xFirst, start, target, world, stateMap);
		}
	}

	private void buildTunnelSegment(BlockPos pos, World world, Map<BlockPos, ExtendedBlockState> stateMap) {
		Block airBlock = this.dungeon.getAirBlock();
		Block floorMaterial = this.dungeon.getFloorBlock();

		stateMap.put(pos, new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));
		stateMap.put(pos.down(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));
		stateMap.put(pos.down().down(), new ExtendedBlockStatePart.ExtendedBlockState(floorMaterial.getDefaultState(), null));
		stateMap.put(pos.up(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));

		stateMap.put(pos.north(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));
		stateMap.put(pos.north().down(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));
		stateMap.put(pos.north().down().down(), new ExtendedBlockStatePart.ExtendedBlockState(floorMaterial.getDefaultState(), null));
		stateMap.put(pos.north().up(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));
		
		stateMap.put(pos.north().east(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));
		stateMap.put(pos.north().east().down(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));
		stateMap.put(pos.north().east().down().down(), new ExtendedBlockStatePart.ExtendedBlockState(floorMaterial.getDefaultState(), null));
		stateMap.put(pos.north().east().up(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));

		stateMap.put(pos.north().west(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));
		stateMap.put(pos.north().west().down(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));
		stateMap.put(pos.north().west().down().down(), new ExtendedBlockStatePart.ExtendedBlockState(floorMaterial.getDefaultState(), null));
		stateMap.put(pos.north().west().up(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));

		stateMap.put(pos.east(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));
		stateMap.put(pos.east().down(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));
		stateMap.put(pos.east().down().down(), new ExtendedBlockStatePart.ExtendedBlockState(floorMaterial.getDefaultState(), null));
		stateMap.put(pos.east().up(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));

		stateMap.put(pos.south(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));
		stateMap.put(pos.south().down(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));
		stateMap.put(pos.south().down().down(), new ExtendedBlockStatePart.ExtendedBlockState(floorMaterial.getDefaultState(), null));
		stateMap.put(pos.south().up(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));
		
		stateMap.put(pos.south().east(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));
		stateMap.put(pos.south().east().down(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));
		stateMap.put(pos.south().east().down().down(), new ExtendedBlockStatePart.ExtendedBlockState(floorMaterial.getDefaultState(), null));
		stateMap.put(pos.south().east().up(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));

		stateMap.put(pos.south().west(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));
		stateMap.put(pos.south().west().down(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));
		stateMap.put(pos.south().west().down().down(), new ExtendedBlockStatePart.ExtendedBlockState(floorMaterial.getDefaultState(), null));
		stateMap.put(pos.south().west().up(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));

		stateMap.put(pos.west(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));
		stateMap.put(pos.west().down(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));
		stateMap.put(pos.west().down().down(), new ExtendedBlockStatePart.ExtendedBlockState(floorMaterial.getDefaultState(), null));
		stateMap.put(pos.west().up(), new ExtendedBlockStatePart.ExtendedBlockState(airBlock.getDefaultState(), null));
	}

	public void setSizeAndHeight(int sX, int sZ, int h) {
		this.sizeX = sX;
		this.sizeZ = sZ;
		this.height = h;
	}

	public BlockPos getCenter() {
		return new BlockPos(this.centerX, this.centerY, this.centerZ);
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// MAKES NO SENSE FOR A CAVE
	}

	@Override
	public DungeonBase getDungeon() {
		return this.dungeon;
	}

}
