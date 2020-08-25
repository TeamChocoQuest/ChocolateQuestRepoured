package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonGuardedCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartBlock;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartCover;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartEntity;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartPlateau;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class GeneratorGuardedStructure extends AbstractDungeonGenerator<DungeonGuardedCastle> {

	// DONE? remake the part where the dungeons are chosen and the support hills are being built, it does not work how it should atm...

	private List<File> chosenStructures = new ArrayList<>();
	private File centerStructure;

	private BlockPos startPos;
	private List<BlockPos> structurePosList = new ArrayList<>();
	private List<Rotation> rotList = new ArrayList<>();

	private Map<CQStructure, BlockPos> toGenerate = new HashMap<>();

	public GeneratorGuardedStructure(World world, BlockPos pos, DungeonGuardedCastle dungeon) {
		super(world, pos, dungeon);
	}

	@Override
	public void preProcess() {
		int buildings = DungeonGenUtils.randomBetween(this.dungeon.getMinBuildings(), this.dungeon.getMaxBuilding(), this.random);
		this.centerStructure = this.dungeon.getStructureFileFromDirectory(this.dungeon.getCenterStructureFolder());
		for (int i = 0; i < buildings; i++) {
			this.chosenStructures.add(this.dungeon.getStructureFileFromDirectory(this.dungeon.getStructureFileFromDirectory(this.dungeon.getStructureFolder())));
		}

		// DONE: Calculate positions of structures, then build the support platforms, then calculate
		// !! IN BUILD STEP !! PATH BUILDING: First: Chose whether to build x or z first. then build x/z until the destination x/z is reached. then switch to the
		// remaining component and wander to the destination
		int vX = DungeonGenUtils.randomBetween(this.dungeon.getMinDistance(), this.dungeon.getMaxDistance());
		for (int i = 0; i < this.chosenStructures.size(); i++) {
			if (!this.dungeon.placeInCircle() && i > 0) {
				vX = DungeonGenUtils.randomBetween(this.dungeon.getMinDistance(), this.dungeon.getMaxDistance());
			}
			Vec3i v = new Vec3i(vX, 0, 0);
			Double degrees = ((Integer) new Random().nextInt(360)).doubleValue();
			if (this.dungeon.placeInCircle()) {
				degrees = 360.0 / this.chosenStructures.size();
				degrees *= i;

				// System.out.println("Angle: " + degrees);
			}
			v = VectorUtil.rotateVectorAroundY(v, degrees);
			// System.out.println("Vector: " + v.toString());
			BlockPos newPos = this.pos.add(v);
			while (this.positionConflicts(newPos) && i > 0 && !this.dungeon.placeInCircle()) {
				degrees = ((Integer) new Random().nextInt(360)).doubleValue();
				if (this.dungeon.placeInCircle()) {
					degrees = 360.0 / this.chosenStructures.size();
					degrees *= i;
				}
				v = VectorUtil.rotateVectorAroundY(v, degrees);

				newPos = this.pos.add(v);
			}
			int yNew = DungeonGenUtils.getYForPos(this.world, newPos.getX(), newPos.getZ(), true);

			BlockPos calculatedPos = new BlockPos(newPos.getX(), yNew, newPos.getZ());
			if (!this.structurePosList.contains(calculatedPos)) {
				this.structurePosList.add(calculatedPos);
			}
		}
	}

	private boolean positionConflicts(BlockPos newPos) {
		for (BlockPos pIn : this.structurePosList) {
			if (Math.abs(pIn.getDistance(newPos.getX(), pIn.getY(), newPos.getZ())) < (new Double((new Integer(this.dungeon.getMinDistance()).doubleValue() * 0.9D)))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void buildStructure() {
		if (this.centerStructure == null) {
			CQRMain.logger.error("No center building for guarded castle: {}", this.dungeon.getDungeonName());
			return;
		}
		CQStructure centerDun = this.loadStructureFromFile(this.centerStructure);

		this.dungeonGenerator.add(new DungeonPartPlateau(this.world, this.dungeonGenerator, this.pos.getX(), this.pos.getZ(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock(), 8));
		this.toGenerate.put(centerDun, this.pos);

		// First, build all the support platforms
		for (int i = 0; i < this.structurePosList.size(); i++) {
			if (i < this.chosenStructures.size()) {
				// CQRMain.logger.info("Building support platform {}...", i + 1);
				// DONE: Load structures from file method !!HIGH PRIORITY!!
				File file = this.chosenStructures.get(i);
				CQStructure structure = this.loadStructureFromFile(file);

				if (structure != null) {
					// Build the support platform...
					BlockPos pos = this.structurePosList.get(i);

					int xT = pos.getX();
					int zT = pos.getZ();

					Rotation rot = this.dungeon.rotateDungeon() ? Rotation.values()[new Random().nextInt(4)] : Rotation.NONE;
					int sizeX = structure.getSize().getX();
					int sizeZ = structure.getSize().getZ();
					this.rotList.set(i, rot);
					if (this.dungeon.rotateDungeon()) {
						switch (rot) {
						case CLOCKWISE_90:
							xT -= sizeX;
							this.rotList.set(i, Rotation.CLOCKWISE_90);
							break;
						case CLOCKWISE_180:
							xT -= sizeX;
							zT -= sizeZ;
							this.rotList.set(i, Rotation.CLOCKWISE_180);
							break;
						case COUNTERCLOCKWISE_90:
							zT -= sizeZ;
							this.rotList.set(i, Rotation.COUNTERCLOCKWISE_90);
							break;
						default:
							break;
						}
						pos = new BlockPos(xT, pos.getY(), zT);
						this.structurePosList.set(i, pos);
					}

					this.dungeonGenerator
							.add(new DungeonPartPlateau(this.world, this.dungeonGenerator, pos.getX(), pos.getZ(), pos.getX() + structure.getSize().getX(), pos.getY() + this.dungeon.getUnderGroundOffset(), pos.getZ() + structure.getSize().getZ(), this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock(), 8));

					// Build the structure...
					/*
					 * int Y = pos.getY() - this.dungeon.getUnderGroundOffset(); int X = pos.getX() - (dungeonToSpawn.getSizeX() /2); int Z = pos.getZ() -
					 * (dungeonToSpawn.getSizeZ() /2); //pos = pos.add(- dungeonToSpawn.getSizeX() /2, 0, -
					 * dungeonToSpawn.getSizeZ() /2); pos = new BlockPos(X, Y, Z);
					 */

					this.toGenerate.put(structure, pos);
				}
			}
		}
		// then build the paths...
		if (this.structurePosList != null && !this.structurePosList.isEmpty() && this.startPos != null && this.dungeon.buildPaths()) {
			// CQRMain.logger.info("Building {} roads...", this.structurePosList.size());
			for (BlockPos end : this.structurePosList) {
				// CQRMain.logger.info("Building road {} of {}...", this.structurePosList.indexOf(end) + 1, this.structurePosList.size());
				this.buildPath(end, this.pos /* this.startPos */);
			}
			// CQRMain.logger.info("Roads built!");
		}
		// And now, build all the structures...
	}

	@Override
	public void postProcess() {
		if (this.toGenerate != null && !this.toGenerate.isEmpty()) {
			String mobType = this.dungeon.getDungeonMob();
			if (mobType.equalsIgnoreCase(DungeonInhabitantManager.DEFAULT_INHABITANT_IDENT)) {
				mobType = DungeonInhabitantManager.getInhabitantDependingOnDistance(this.world, this.pos.getX(), this.pos.getZ()).getName();
			}

			PlacementSettings plcmnt = new PlacementSettings();
			plcmnt.setMirror(Mirror.NONE);
			plcmnt.setRotation(Rotation.NONE);
			if (this.dungeon.rotateDungeon()) {
				plcmnt.setRotation(this.getRandomRotation());
			}
			plcmnt.setIntegrity(1.0f);

			int index = 1;
			BlockPos posLower = this.pos;
			BlockPos posUpper = this.pos;
			// List<String> bosses = new ArrayList<>();
			for (CQStructure structure : this.toGenerate.keySet()) {
				// CQRMain.logger.info("Building house {}...", index);
				BlockPos pos = this.toGenerate.get(structure);
				int X = posLower.getX();
				int Y = posLower.getY();
				int Z = posLower.getZ();
				if (pos.getX() < X) {
					X = pos.getX();
				}
				if (pos.getY() < Y) {
					X = pos.getY();
				}
				if (pos.getZ() < Z) {
					X = pos.getZ();
				}
				posLower = new BlockPos(X, Y, Z);

				int xm = posUpper.getX();
				int ym = posUpper.getY();
				int zm = posUpper.getZ();
				if (pos.getX() + structure.getSize().getX() > xm) {
					xm = pos.getX() + structure.getSize().getX();
				}
				if (pos.getY() + structure.getSize().getY() > ym) {
					ym = pos.getY() + structure.getSize().getY();
				}
				if (pos.getZ() + structure.getSize().getZ() > zm) {
					zm = pos.getZ() + structure.getSize().getZ();
				}
				posUpper = new BlockPos(X, Y, Z);

				plcmnt.setRotation(this.rotList.get(index - 1));

				this.dungeonGenerator.add(new DungeonPartBlock(this.world, this.dungeonGenerator, posLower, structure.getBlockInfoList(), plcmnt, mobType));
				this.dungeonGenerator.add(new DungeonPartBlock(this.world, this.dungeonGenerator, posLower, structure.getSpecialBlockInfoList(), plcmnt, mobType));
				this.dungeonGenerator.add(new DungeonPartEntity(this.world, this.dungeonGenerator, posLower, structure.getEntityInfoList(), plcmnt, mobType));

				index++;
			}

			this.placeCoverBlocks();
		}
	}

	private void buildPath(BlockPos start, BlockPos end) {
		boolean xfirst = new Random().nextBoolean();
		if (xfirst) {
			this.buildPathX(start, end);
			this.buildPathZ(start.add(end.getX() - start.getX(), 0, 0), end);
		} else {
			this.buildPathZ(start, end);
			this.buildPathX(start.add(0, 0, end.getZ() - start.getZ()), end);
		}
	}

	private void buildPathX(BlockPos start, BlockPos end) {
		int vX = end.getX() < start.getX() ? -1 : 1;
		if (end.getX() == start.getX()) {
			vX = 0;
		}
		int currX = start.getX();
		int z = start.getZ();
		int y = 0;
		do {
			y = DungeonGenUtils.getYForPos(world, currX, z, true);
			this.buildPathSegmentX(new BlockPos(currX, y, z));
			currX += vX;
		} while (currX != end.getX());
		/*
		 * if(start.getZ() != end.getZ()) { start = new BlockPos(end.getX(), start.getY(), start.getZ()); buildPathZ(start, end); }
		 */
	}

	private void buildPathZ(BlockPos start, BlockPos end) {
		int vZ = end.getZ() < start.getZ() ? -1 : 1;
		if (end.getZ() == start.getZ()) {
			vZ = 0;
		}
		int currZ = start.getZ();
		int x = start.getX();
		int y = 0;
		do {
			y = DungeonGenUtils.getYForPos(world, x, currZ, true);
			this.buildPathSegmentZ(new BlockPos(x, y, currZ));
			currZ += vZ;
		} while (currZ != end.getZ());
		/*
		 * if(start.getX() != end.getX()) { start = new BlockPos(start.getX(), start.getY(), end.getZ()); buildPathZ(start, end); }
		 */
	}

	private void buildPathSegmentX(BlockPos pos) {
		this.world.setBlockState(pos, this.dungeon.getPathMaterial());
		this.world.setBlockState(pos.north(), this.dungeon.getPathMaterial());
		this.world.setBlockState(pos.south(), this.dungeon.getPathMaterial());

		this.supportBlock(pos);
		this.supportBlock(pos.north());
		this.supportBlock(pos.south());
	}

	private void buildPathSegmentZ(BlockPos pos) {
		this.world.setBlockState(pos, this.dungeon.getPathMaterial());
		this.world.setBlockState(pos.west(), this.dungeon.getPathMaterial());
		this.world.setBlockState(pos.east(), this.dungeon.getPathMaterial());

		this.supportBlock(pos);
		this.supportBlock(pos.west());
		this.supportBlock(pos.east());
	}

	private void supportBlock(BlockPos pos) {
		int i = 0;
		BlockPos tmpPos = pos.up();
		while (!Block.isEqualTo(this.world.getBlockState(tmpPos).getBlock(), Blocks.AIR) && i <= 3) {
			this.world.setBlockToAir(tmpPos);
			tmpPos = tmpPos.up();
		}
		tmpPos = pos.down();
		while (Block.isEqualTo(this.world.getBlockState(tmpPos).getBlock(), Blocks.AIR)) {
			this.world.setBlockState(tmpPos, this.dungeon.getPathMaterial());
			tmpPos = tmpPos.down();
		}
	}

	private Rotation getRandomRotation() {
		int index = new Random().nextInt(4);
		switch (index) {
		case 0:
			return Rotation.CLOCKWISE_90;
		case 1:
			return Rotation.CLOCKWISE_180;
		case 2:
			return Rotation.COUNTERCLOCKWISE_90;
		default:
			return Rotation.NONE;
		}
	}

	public void placeCoverBlocks() {
		if (this.dungeon.isCoverBlockEnabled()) {
			for (CQStructure structure : this.toGenerate.keySet()) {
				int startX = this.toGenerate.get(structure).getX() - structure.getSize().getX() / 3 - CQRConfig.general.supportHillWallSize / 2;
				int startZ = this.toGenerate.get(structure).getZ() - structure.getSize().getZ() / 3 - CQRConfig.general.supportHillWallSize / 2;

				int endX = this.toGenerate.get(structure).getX() + structure.getSize().getX() + structure.getSize().getX() / 3 + CQRConfig.general.supportHillWallSize / 2;
				int endZ = this.toGenerate.get(structure).getZ() + structure.getSize().getZ() + structure.getSize().getZ() / 3 + CQRConfig.general.supportHillWallSize / 2;

				this.dungeonGenerator.add(new DungeonPartCover(this.world, this.dungeonGenerator, startX, startZ, endX, endZ, this.dungeon.getCoverBlock()));
			}
		}
	}

}
