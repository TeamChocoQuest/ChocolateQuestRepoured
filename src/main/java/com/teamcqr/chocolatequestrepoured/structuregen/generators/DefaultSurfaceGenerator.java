package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.API.events.CQDungeonStructureGenerateEvent;
import com.teamcqr.chocolatequestrepoured.structuregen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DefaultSurfaceDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.common.MinecraftForge;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class DefaultSurfaceGenerator implements IDungeonGenerator {

	private CQStructure structure;
	private PlacementSettings placeSettings;
	private DefaultSurfaceDungeon dungeon;
	private Rotation rot = Rotation.values()[new Random().nextInt(4)];

	public DefaultSurfaceGenerator(DefaultSurfaceDungeon dun, CQStructure struct, PlacementSettings settings) {
		this.dungeon = dun;
		this.structure = struct;
		this.placeSettings = settings;
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		// Builds the support hill;
		if (this.dungeon.doBuildSupportPlatform()) {
			int sizeX = this.structure.getSizeX();
			int sizeZ = this.structure.getSizeZ();
			if (this.dungeon.rotateDungeon()) {
				switch (this.rot) {
				case CLOCKWISE_90:
					x -= sizeX;
					break;
				case CLOCKWISE_180:
					x -= sizeX;
					z -= sizeZ;
					break;
				case COUNTERCLOCKWISE_90:
					z -= sizeZ;
					break;
				default:
					break;
				}
			}
			PlateauBuilder supportBuilder = new PlateauBuilder();
			supportBuilder.load(this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock());
			supportBuilder.createSupportHill(new Random(), world, new BlockPos(x, y + this.dungeon.getUnderGroundOffset(), z), sizeX, sizeZ, EPosType.DEFAULT);
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		// Simply puts the structure at x,y,z
		this.structure.placeBlocksInWorld(world, new BlockPos(x, y, z), this.placeSettings.setRotation(this.rot), EPosType.DEFAULT);

		CQDungeonStructureGenerateEvent event = new CQDungeonStructureGenerateEvent(this.dungeon, new BlockPos(x, y, z), new BlockPos(this.structure.getSizeX(), this.structure.getSizeY(), this.structure.getSizeZ()), world);
		event.setShieldCorePosition(this.structure.getShieldCorePosition());
		MinecraftForge.EVENT_BUS.post(event);
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		// Does nothing here
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		// Also does nothing
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		// Also does nothing
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z) {
		if (this.dungeon.isCoverBlockEnabled()) {
			if (this.dungeon.rotateDungeon()) {
				int sizeX = this.structure.getSizeX();
				int sizeZ = this.structure.getSizeZ();
				switch (this.rot) {
				case CLOCKWISE_90:
					x -= sizeX;
					break;
				case CLOCKWISE_180:
					x -= sizeX;
					z -= sizeZ;
					break;
				case COUNTERCLOCKWISE_90:
					z -= sizeZ;
					break;
				default:
					break;
				}
			}
			int startX = x - this.structure.getSizeX() / 3 - Reference.CONFIG_HELPER_INSTANCE.getSupportHillWallSize() / 2;
			int startZ = z - this.structure.getSizeZ() / 3 - Reference.CONFIG_HELPER_INSTANCE.getSupportHillWallSize() / 2;

			int endX = x + this.structure.getSizeX() + this.structure.getSizeX() / 3 + Reference.CONFIG_HELPER_INSTANCE.getSupportHillWallSize() / 2;
			int endZ = z + this.structure.getSizeZ() + this.structure.getSizeZ() / 3 + Reference.CONFIG_HELPER_INSTANCE.getSupportHillWallSize() / 2;

			for (int iX = startX; iX <= endX; iX++) {
				for (int iZ = startZ; iZ <= endZ; iZ++) {
					BlockPos pos = new BlockPos(iX, world.getTopSolidOrLiquidBlock(new BlockPos(iX, 0, iZ)).getY(), iZ);
					if (!Block.isEqualTo(world.getBlockState(pos.subtract(new Vec3i(0, 1, 0))).getBlock(), this.dungeon.getCoverBlock())) {
						world.setBlockState(pos, this.dungeon.getCoverBlock().getDefaultState());
					}
				}
			}
		}

	}

}
