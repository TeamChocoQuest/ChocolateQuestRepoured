package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DefaultSurfaceDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.IStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class DefaultSurfaceGenerator implements IDungeonGenerator {

	private CQStructure structure;
	private PlacementSettings placeSettings;
	private DefaultSurfaceDungeon dungeon;

	public DefaultSurfaceGenerator(DefaultSurfaceDungeon dun, CQStructure struct, PlacementSettings settings) {
		this.dungeon = dun;
		this.structure = struct;
		this.placeSettings = settings;
	}

	@Override
	public void generate(World world, Chunk chunk, int x, int y, int z) {
		BlockPos pos1 = this.structure.getSize();
		BlockPos pos2 = Template.transformedBlockPos(this.placeSettings, new BlockPos(pos1.getX() >> 1, 0, pos1.getZ() >> 1));
		x -= pos2.getX();
		z -= pos2.getZ();
		IDungeonGenerator.super.generate(world, chunk, x, y, z);
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Builds the support hill
		if (this.dungeon.doBuildSupportPlatform()) {
			BlockPos pos1 = new BlockPos(x, y + this.dungeon.getUnderGroundOffset(), z);
			BlockPos pos2 = pos1.add(Template.transformedBlockPos(this.placeSettings, this.structure.getSize()));
			BlockPos start = DungeonGenUtils.getMinPos(pos1, pos2);
			BlockPos end = DungeonGenUtils.getMaxPos(pos1, pos2);

			PlateauBuilder supportBuilder = new PlateauBuilder();
			supportBuilder.load(this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock());
			lists.add(supportBuilder.createSupportHillList(new Random(), world, start, end.getX() - start.getX(), end.getZ() - start.getZ(), EPosType.DEFAULT));
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Simply puts the structure at x,y,z
		for (List<? extends IStructure> list : this.structure.addBlocksToWorld(world, new BlockPos(x, y, z), this.placeSettings, EPosType.DEFAULT, this.dungeon, chunk.x, chunk.z)) {
			lists.add(list);
		}

		/*
		 * List<String> bosses = new ArrayList<>(); for(UUID id : structure.getBossIDs()) { bosses.add(id.toString()); }
		 * 
		 * CQDungeonStructureGenerateEvent event = new CQDungeonStructureGenerateEvent(this.dungeon, new BlockPos(x, y, z), this.structure.getSize(), world, bosses); event.setShieldCorePosition(this.structure.getShieldCorePosition());
		 * MinecraftForge.EVENT_BUS.post(event);
		 */
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
		if (this.dungeon.isCoverBlockEnabled()) {
			Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> stateMap = new HashMap<>();
			
			int sizeX = this.structure.getSize().getX();
			int sizeZ = this.structure.getSize().getZ();
			switch (this.placeSettings.getRotation()) {
			case CLOCKWISE_90:
				x -= sizeZ; {
				int i = sizeX;
				int j = sizeZ;
				sizeX = j;
				sizeZ = i;
			}
				break;
			case CLOCKWISE_180:
				x -= sizeX;
				z -= sizeZ;
				break;
			case COUNTERCLOCKWISE_90:
				z -= sizeX; {
				int i = sizeX;
				int j = sizeZ;
				sizeX = j;
				sizeZ = i;
			}
				break;
			default:
				break;
			}
			int startX = x - sizeX / 3 - CQRConfig.general.supportHillWallSize / 2;
			int startZ = z - sizeZ / 3 - CQRConfig.general.supportHillWallSize / 2;

			int endX = x + sizeX + sizeX / 3 + CQRConfig.general.supportHillWallSize / 2;
			int endZ = z + sizeZ + sizeZ / 3 + CQRConfig.general.supportHillWallSize / 2;

			for (int iX = startX; iX <= endX; iX++) {
				for (int iZ = startZ; iZ <= endZ; iZ++) {
					BlockPos pos = new BlockPos(iX, world.getTopSolidOrLiquidBlock(new BlockPos(iX, 0, iZ)).getY(), iZ);
					if (!Block.isEqualTo(world.getBlockState(pos.subtract(new Vec3i(0, 1, 0))).getBlock(), this.dungeon.getCoverBlock())) {
						stateMap.put(pos, new ExtendedBlockStatePart.ExtendedBlockState(this.dungeon.getCoverBlock().getDefaultState(), null));
					}
				}
			}
			lists.add(ExtendedBlockStatePart.splitExtendedBlockStateMap(stateMap));
		}

	}

	@Override
	public DungeonBase getDungeon() {
		return this.dungeon;
	}

}
