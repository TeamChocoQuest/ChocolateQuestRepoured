package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCavern;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartBlock;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.AbstractBlockInfo;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.BlockInfo;
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
import net.minecraft.world.gen.structure.template.PlacementSettings;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class GeneratorCavern extends AbstractDungeonGenerator<DungeonCavern> {

	private int sizeX;
	private int sizeZ;
	private int height;

	private int centerX;
	private int centerZ;
	private int centerY;

	private DungeonGenerator customGenerator;

	public GeneratorCavern(World world, BlockPos pos, DungeonCavern dungeon, DungeonGenerator customGenerator) {
		super(world, pos, dungeon);
		this.customGenerator = customGenerator;
	}

	@Override
	public void preProcess() {
		// DONE: calculate air blocks
		Perlin3D perlin1 = new Perlin3D(world.getSeed(), 4, new Random());
		Perlin3D perlin2 = new Perlin3D(world.getSeed(), 32, new Random());

		Map<BlockPos, IBlockState> stateMap = new HashMap<>();

		int centerX = this.sizeX / 2;
		int centerY = this.height / 2;
		int centerZ = this.sizeZ / 2;

		this.centerX = centerX + this.pos.getX();
		this.centerY = centerY + this.pos.getY();
		this.centerZ = centerZ + this.pos.getZ();

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

					double noise = (perlin1.getNoiseAt(this.pos.getX() + iX, this.pos.getY() + iY, this.pos.getZ() + iZ) + perlin2.getNoiseAt(this.pos.getX() + iX, this.pos.getY() + iY, this.pos.getZ() + iZ)) / 2.0D * (noiseAtPos * 2.5D);

					if (noise < 0.75D) {
						BlockPos block = this.pos.add(iX, iY, iZ);
						stateMap.put(block, dungeon.getAirBlock().getDefaultState());
						if (iY == 0) {
							stateMap.put(block, dungeon.getFloorBlock().getDefaultState());
						}
					}
				}
			}
		}
		List<AbstractBlockInfo> blockInfoList = new ArrayList<>();
		for (Map.Entry<BlockPos, IBlockState> entry : stateMap.entrySet()) {
			blockInfoList.add(new BlockInfo(entry.getKey().subtract(this.pos), entry.getValue(), null));
		}
		this.customGenerator.add(new DungeonPartBlock(world, customGenerator, pos, blockInfoList, new PlacementSettings(), "ZOMBIE"));
	}

	@Override
	public void buildStructure() {
		// Not needed
	}

	@Override
	public void postProcess() {
		// Not needed
	}

	public void fillChests() {
		BlockPos start = this.pos;
		IBlockState state = Blocks.CHEST.getDefaultState();
		List<AbstractBlockInfo> list = new ArrayList<>();
		TileEntityChest chest = (TileEntityChest) Blocks.CHEST.createTileEntity(world, state);
		ResourceLocation[] chestIDs = this.dungeon.getChestIDs();
		if (chest != null) {
			ResourceLocation resLoc = chestIDs[new Random().nextInt(chestIDs.length)];
			if (resLoc != null) {
				long seed = WorldDungeonGenerator.getSeed(world, this.pos.getX(), this.pos.getZ());
				chest.setLootTable(resLoc, seed);
			}
			list.add(new BlockInfo(BlockPos.ORIGIN, state, chest.writeToNBT(new NBTTagCompound())));
		}
		this.customGenerator.add(new DungeonPartBlock(world, customGenerator, start, list, new PlacementSettings(), "ZOMBIE"));
	}

	public void placeSpawners() {
		BlockPos spawnerPos = this.pos;
		List<AbstractBlockInfo> list = new ArrayList<>();

		IBlockState state = Blocks.MOB_SPAWNER.getDefaultState();
		TileEntityMobSpawner spawner = (TileEntityMobSpawner) Blocks.MOB_SPAWNER.createTileEntity(world, state);
		spawner.getSpawnerBaseLogic().setEntityId(dungeon.getMob());
		spawner.updateContainingBlockInfo();
		list.add(new BlockInfo(BlockPos.ORIGIN, state, spawner.writeToNBT(new NBTTagCompound())));

		this.customGenerator.add(new DungeonPartBlock(world, customGenerator, spawnerPos, list, new PlacementSettings(), "ZOMBIE"));
	}

	public void generateTunnel(BlockPos start, BlockPos end, World world, Map<BlockPos, IBlockState> stateMap) {
		this.generateTunnel(new Random().nextBoolean(), start, end, world, stateMap);
	}

	private void generateTunnel(boolean xFirst, BlockPos start, BlockPos target, World world, Map<BlockPos, IBlockState> stateMap) {
		if (start.getX() == target.getX() && start.getZ() == target.getZ()) {
			return;
		} else if (start.getX() == target.getX() && xFirst) {
			this.generateTunnel(false, start, target, world, stateMap);
		} else if (start.getZ() == target.getZ() && !xFirst) {
			this.generateTunnel(true, start, target, world, stateMap);
		}
		/*
		 * else if(DungeonGenUtils.PercentageRandom(25, world.getSeed()) && !(start.getX() == target.getX() || start.getZ() == target.getZ())) { generateTunnel(!xFirst,
		 * start, target, world); }
		 */
		else {
			int v = 0;
			this.buildTunnelSegment(start, stateMap);
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

	private void buildTunnelSegment(BlockPos pos, Map<BlockPos, IBlockState> stateMap) {
		Block airBlock = this.dungeon.getAirBlock();
		Block floorMaterial = this.dungeon.getFloorBlock();

		stateMap.put(pos, airBlock.getDefaultState());
		stateMap.put(pos.down(), airBlock.getDefaultState());
		stateMap.put(pos.down().down(), floorMaterial.getDefaultState());
		stateMap.put(pos.up(), airBlock.getDefaultState());

		stateMap.put(pos.north(), airBlock.getDefaultState());
		stateMap.put(pos.north().down(), airBlock.getDefaultState());
		stateMap.put(pos.north().down().down(), floorMaterial.getDefaultState());
		stateMap.put(pos.north().up(), airBlock.getDefaultState());

		stateMap.put(pos.north().east(), airBlock.getDefaultState());
		stateMap.put(pos.north().east().down(), airBlock.getDefaultState());
		stateMap.put(pos.north().east().down().down(), floorMaterial.getDefaultState());
		stateMap.put(pos.north().east().up(), airBlock.getDefaultState());

		stateMap.put(pos.north().west(), airBlock.getDefaultState());
		stateMap.put(pos.north().west().down(), airBlock.getDefaultState());
		stateMap.put(pos.north().west().down().down(), floorMaterial.getDefaultState());
		stateMap.put(pos.north().west().up(), airBlock.getDefaultState());

		stateMap.put(pos.east(), airBlock.getDefaultState());
		stateMap.put(pos.east().down(), airBlock.getDefaultState());
		stateMap.put(pos.east().down().down(), floorMaterial.getDefaultState());
		stateMap.put(pos.east().up(), airBlock.getDefaultState());

		stateMap.put(pos.south(), airBlock.getDefaultState());
		stateMap.put(pos.south().down(), airBlock.getDefaultState());
		stateMap.put(pos.south().down().down(), floorMaterial.getDefaultState());
		stateMap.put(pos.south().up(), airBlock.getDefaultState());

		stateMap.put(pos.south().east(), airBlock.getDefaultState());
		stateMap.put(pos.south().east().down(), airBlock.getDefaultState());
		stateMap.put(pos.south().east().down().down(), floorMaterial.getDefaultState());
		stateMap.put(pos.south().east().up(), airBlock.getDefaultState());

		stateMap.put(pos.south().west(), airBlock.getDefaultState());
		stateMap.put(pos.south().west().down(), airBlock.getDefaultState());
		stateMap.put(pos.south().west().down().down(), floorMaterial.getDefaultState());
		stateMap.put(pos.south().west().up(), airBlock.getDefaultState());

		stateMap.put(pos.west(), airBlock.getDefaultState());
		stateMap.put(pos.west().down(), airBlock.getDefaultState());
		stateMap.put(pos.west().down().down(), floorMaterial.getDefaultState());
		stateMap.put(pos.west().up(), airBlock.getDefaultState());
	}

	public void setSizeAndHeight(int sX, int sZ, int h) {
		this.sizeX = sX;
		this.sizeZ = sZ;
		this.height = h;
	}

	public BlockPos getCenter() {
		return new BlockPos(this.centerX, this.centerY, this.centerZ);
	}

}
