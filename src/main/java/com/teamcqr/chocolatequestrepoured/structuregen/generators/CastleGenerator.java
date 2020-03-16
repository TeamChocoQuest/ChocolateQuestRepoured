package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.IStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.CastleRoomSelector;

import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 25.05.2019
 * Developed by KalgogSmash
 * GitHub: https://github.com/KalgogSmash
 */
public class CastleGenerator implements IDungeonGenerator {

	private CastleDungeon dungeon;
	private Random random;
	private CastleRoomSelector roomHelper;

	public CastleGenerator(CastleDungeon dungeon) {
		this.dungeon = dungeon;
		this.random = this.dungeon.getRandom();
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		this.roomHelper = new CastleRoomSelector(this.dungeon);
		this.roomHelper.randomizeCastle();

		// Builds the support hill;
		/*
		if (this.dungeon.doBuildSupportPlatform()) {
			PlateauBuilder supportBuilder = new PlateauBuilder();
			supportBuilder.load(this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock());
			List<CastleRoomSelector.SupportArea> supportAreas = this.roomHelper.getSupportAreas();

			for (CastleRoomSelector.SupportArea area : supportAreas) {
				supportBuilder.createSupportHill(this.random, world, area.getNwCorner(), area.getBlocksX(), area.getBlocksZ(), EPosType.CORNER_NW);
			}
		} */
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		final int padding = 5;
		BlockStateGenArray genArray = new BlockStateGenArray(dungeon.getMaxSize() + 25, 256, dungeon.getMaxSize() + 25, padding);
		BlockPos origin = new BlockPos(x, y, z);
		BlockPos corner = genArray.getAdjustedStartPosition(origin);
		ArrayList<String> bossUuids = new ArrayList<>();
		this.roomHelper.generate(world, genArray, this.dungeon, origin, bossUuids);
		this.roomHelper.addDecoration(world, origin, this.dungeon, genArray, bossUuids);

		IBlockState[][][] toBuild = genArray.finalizeArray();
		for (int xb = 0; xb < toBuild.length; xb++) {
			for (int yb = 0; yb < toBuild[0].length; yb++) {
				for (int zb = 0; zb < toBuild[0][0].length; zb++) {
					world.setBlockState(origin.add(xb, yb, zb), toBuild[xb][yb][zb]);
				}
			}
		}

		//CQDungeonStructureGenerateEvent event = new CQDungeonStructureGenerateEvent(this.dungeon, new BlockPos(x, y, z), new BlockPos(x + this.totalX, y + this.totalY, z + this.totalZ), world, bossUuids);
		//MinecraftForge.EVENT_BUS.post(event);
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Does nothing here
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Also does nothing
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Also does nothing
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// TODO Auto-generated method stub

	}
}
