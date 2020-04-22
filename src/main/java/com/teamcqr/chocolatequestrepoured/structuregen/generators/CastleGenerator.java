package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.EntityDataPart;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.IStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.CastleRoomSelector;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 25.05.2019
 * Developed by KalgogSmash
 * GitHub: https://github.com/KalgogSmash
 */
public class CastleGenerator implements IDungeonGenerator {
	private BlockPos origin;
	private DungeonCastle dungeon;
	private Random random;
	private CastleRoomSelector roomHelper;

	public CastleGenerator(DungeonCastle dungeon) {
		this.dungeon = dungeon;
		this.random = this.dungeon.getRandom();
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		origin = new BlockPos(x, y, z);
		this.roomHelper = new CastleRoomSelector(origin, this.dungeon);
		this.roomHelper.randomizeCastle();

		if (this.dungeon.doBuildSupportPlatform()) {
			PlateauBuilder supportBuilder = new PlateauBuilder();
			supportBuilder.load(this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock());
			List<CastleRoomSelector.SupportArea> supportAreas = this.roomHelper.getSupportAreas();

			for (CastleRoomSelector.SupportArea area : supportAreas) {
				lists.add(supportBuilder.createSupportHillList(this.random, world, area.getNwCorner(), area.getBlocksX(), area.getBlocksZ(), EPosType.CORNER_NW));
			}
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		BlockStateGenArray genArray = new BlockStateGenArray();
		ArrayList<String> bossUuids = new ArrayList<>();
		this.roomHelper.generate(world, genArray, this.dungeon, origin, bossUuids);

		lists.add(ExtendedBlockStatePart.splitExtendedBlockStateMap(genArray.getMainMap()));
		lists.add(ExtendedBlockStatePart.splitExtendedBlockStateMap(genArray.getPostMap()));
		lists.add(EntityDataPart.splitExtendedEntityDataMap(genArray.getEntityMap()));

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

	@Override
	public DungeonBase getDungeon() {
		return this.dungeon;
	}

}
