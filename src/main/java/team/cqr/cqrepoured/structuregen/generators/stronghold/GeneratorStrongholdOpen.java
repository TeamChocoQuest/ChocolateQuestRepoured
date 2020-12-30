package team.cqr.cqrepoured.structuregen.generators.stronghold;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.structuregen.DungeonDataManager;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonStrongholdOpen;
import team.cqr.cqrepoured.structuregen.generation.DungeonPartPlateau;
import team.cqr.cqrepoured.structuregen.generators.AbstractDungeonGenerator;
import team.cqr.cqrepoured.structuregen.generators.stronghold.open.StrongholdFloorOpen;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import team.cqr.cqrepoured.structuregen.structurefile.CQStructure;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.data.FileIOUtil;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class GeneratorStrongholdOpen extends AbstractDungeonGenerator<DungeonStrongholdOpen> {

	private List<String> blacklistedRooms = new ArrayList<>();
	private Tuple<Integer, Integer> structureBounds;

	private PlacementSettings settings = new PlacementSettings();

	private StrongholdFloorOpen[] floors;

	private int dunX;
	private int dunZ;

	private int entranceSizeX = 0;
	private int entranceSizeZ = 0;

	public GeneratorStrongholdOpen(World world, BlockPos pos, DungeonStrongholdOpen dungeon, Random rand, DungeonDataManager.DungeonSpawnType spawnType) {
		super(world, pos, dungeon, rand, spawnType);
		this.structureBounds = new Tuple<>(dungeon.getRoomSizeX(), dungeon.getRoomSizeZ());

		this.settings.setMirror(Mirror.NONE);
		this.settings.setRotation(Rotation.NONE);
		this.settings.setReplacedBlock(Blocks.STRUCTURE_VOID);
		this.settings.setIntegrity(1.0F);

		this.floors = new StrongholdFloorOpen[dungeon.getRandomFloorCount(this.random)];
		this.searchStructureBounds();
		this.computeNotFittingStructures();
	}

	private void computeNotFittingStructures() {
		for (File f : this.dungeon.getRoomFolder().listFiles(FileIOUtil.getNBTFileFilter())) {
			CQStructure struct = this.loadStructureFromFile(f);
			if (struct != null && (struct.getSize().getX() != this.structureBounds.getFirst() || struct.getSize().getZ() != this.structureBounds.getSecond())) {
				this.blacklistedRooms.add(f.getParent() + "/" + f.getName());
			}
		}
	}

	public DungeonStrongholdOpen getDungeon() {
		return this.dungeon;
	}

	private void searchStructureBounds() {

	}

	@Override
	public void preProcess() {
		this.dunX = this.pos.getX();
		this.dunZ = this.pos.getZ();
		BlockPos initPos = this.pos;
		// initPos = initPos.subtract(new Vec3i(0,dungeon.getYOffset(),0));
		// initPos = initPos.subtract(new Vec3i(0,dungeon.getUnderGroundOffset(),0));

		int rgd = this.getDungeon().getRandomRoomCountForFloor(this.random);
		if (rgd < 2) {
			rgd = 2;
		}
		if (rgd % 2 != 0) {
			rgd++;
		}
		rgd = (new Double(Math.ceil(Math.sqrt(rgd)))).intValue();
		if (rgd % 2 == 0) {
			rgd++;
		}

		StrongholdFloorOpen prevFloor = null;
		for (int i = 0; i < this.floors.length; i++) {
			boolean isFirst = i == 0;
			StrongholdFloorOpen floor = null;
			if (isFirst) {
				floor = new StrongholdFloorOpen(this, rgd, ((Double) Math.floor(rgd / 2)).intValue(), ((Double) Math.floor(rgd / 2)).intValue(), this.random);
			} else {
				floor = new StrongholdFloorOpen(this, rgd, prevFloor.getExitStairIndexes().getFirst(), prevFloor.getExitStairIndexes().getSecond(), this.random);
			}
			File stair = null;
			if (isFirst) {
				stair = this.dungeon.getEntranceStair(this.random);
				if (stair == null) {
					CQRMain.logger.error("No entrance stair rooms for Stronghold Open Dungeon: {}", this.getDungeon().getDungeonName());
					return;
				}
			} else {
				stair = this.dungeon.getStairRoom(this.random);
				if (stair == null) {
					CQRMain.logger.error("No stair rooms for Stronghold Open Dungeon: {}", this.getDungeon().getDungeonName());
					return;
				}
			}
			floor.setIsFirstFloor(isFirst);
			int dY = initPos.getY() - this.loadStructureFromFile(stair).getSize().getY();
			if (dY <= (this.dungeon.getRoomSizeY() + 2)) {
				this.floors[i - 1].setExitIsBossRoom(true);
			} else {
				initPos = initPos.subtract(new Vec3i(0, this.loadStructureFromFile(stair).getSize().getY(), 0));
				if (!isFirst) {
					initPos = initPos.add(0, this.dungeon.getRoomSizeY(), 0);
				}
				if ((i + 1) == this.floors.length) {
					floor.setExitIsBossRoom(true);
				}

				if (isFirst) {
					floor.setEntranceStairPosition(stair, initPos.getX(), initPos.getY(), initPos.getZ());
				} else {
					floor.setEntranceStairPosition(stair, prevFloor.getExitCoordinates().getFirst(), initPos.getY(), prevFloor.getExitCoordinates().getSecond());
				}

				floor.calculatePositions();
				initPos = new BlockPos(floor.getExitCoordinates().getFirst(), initPos.getY(), floor.getExitCoordinates().getSecond());
			}
			prevFloor = floor;
			this.floors[i] = floor;
		}
	}

	@Override
	public void buildStructure() {
		File building = this.dungeon.getEntranceBuilding(this.random);
		DungeonInhabitant mobType = DungeonInhabitantManager.instance().getInhabitantByDistanceIfDefault(this.dungeon.getDungeonMob(), this.world, this.pos.getX(), this.pos.getZ());
		if (building == null || this.dungeon.getEntranceBuildingFolder().listFiles(FileIOUtil.getNBTFileFilter()).length <= 0) {
			CQRMain.logger.error("No entrance buildings for Open Stronghold dungeon: " + this.getDungeon().getDungeonName());
			return;
		}
		CQStructure structure = this.loadStructureFromFile(building);
		if (this.dungeon.doBuildSupportPlatform()) {
			this.dungeonGenerator.add(new DungeonPartPlateau(this.world, this.dungeonGenerator, this.pos.getX() + 4 + structure.getSize().getX() / 2, this.pos.getZ() + 4 + structure.getSize().getZ() / 2, this.pos.getX() - 4 - structure.getSize().getX() / 2, this.pos.getY(),
					this.pos.getZ() - 4 - structure.getSize().getZ() / 2, this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock(), 8));
		}
		this.entranceSizeX = structure.getSize().getX();
		this.entranceSizeZ = structure.getSize().getX();

		BlockPos p = DungeonGenUtils.getCentralizedPosForStructure(this.pos, structure, this.settings);
		structure.addAll(this.world, this.dungeonGenerator, p, this.settings, mobType);
		/*
		 * CQStructure stairs = new CQStructure(dungeon.getStairRoom(), dungeon, chunk.x, chunk.z, dungeon.isProtectedFromModifications()); BlockPos pastePosForStair =
		 * new BlockPos(x, y - stairs.getSizeY(), z); stairs.placeBlocksInWorld(world,
		 * pastePosForStair, settings, EPosType.CENTER_XZ_LAYER);
		 */
		// Will generate the structure
		// Algorithm: while(genRooms < rooms && genFloors < maxFloors) do {
		// while(genRoomsOnFloor < roomsPerFloor) {
		// choose structure, calculate next pos and chose next structure (System: structures in different folders named to where they may attach
		// build Staircase at next position
		// genRoomsOnFloor++
		// genFloors++
		// build staircase to bossroom at next position, then build boss room

		// Structure gen information: stored in map with location and structure file
		for (StrongholdFloorOpen floor : this.floors) {
			floor.generateRooms(this.world, this.dungeonGenerator, mobType);
		}

		// build all the structures in the map
		for (StrongholdFloorOpen floor : this.floors) {
			if (floor == null) {
				CQRMain.logger.error("Floor is null! Not generating it!");
			} else {
				try {
					floor.buildWalls(this.world, this.dungeonGenerator, mobType);
				} catch (NullPointerException ex) {
					CQRMain.logger.error("Error whilst trying to construct wall in open stronghold at: X {}  Y {}  Z {}", this.pos.getX(), this.pos.getY(), this.pos.getZ());
				}
			}
		}
	}

	@Override
	public void postProcess() {
		if (this.dungeon.isCoverBlockEnabled()) {
			// needs update
			/*
			 * Map<BlockPos, IBlockState> stateMap = new HashMap<>();
			 * 
			 * int startX = this.pos.getX() - this.entranceSizeX / 3 - CQRConfig.general.supportHillWallSize / 2; int startZ = this.pos.getZ() - this.entranceSizeZ / 3 -
			 * CQRConfig.general.supportHillWallSize / 2;
			 * 
			 * int endX = this.pos.getX() + this.entranceSizeX + this.entranceSizeX / 3 + CQRConfig.general.supportHillWallSize / 2; int endZ = this.pos.getZ() +
			 * this.entranceSizeZ + this.entranceSizeZ / 3 + CQRConfig.general.supportHillWallSize / 2;
			 * 
			 * for (int iX = startX; iX <= endX; iX++) { for (int iZ = startZ; iZ <= endZ; iZ++) { BlockPos pos = new BlockPos(iX, this.world.getTopSolidOrLiquidBlock(new
			 * BlockPos(iX, 0, iZ)).getY(), iZ); if
			 * (!Block.isEqualTo(this.world.getBlockState(pos.subtract(new Vec3i(0, 1, 0))).getBlock(), this.dungeon.getCoverBlock())) { stateMap.put(pos,
			 * this.dungeon.getCoverBlock().getDefaultState()); } } } List<AbstractBlockInfo> blockInfoList =
			 * new ArrayList<>(); for (Map.Entry<BlockPos, IBlockState> entry : stateMap.entrySet()) { blockInfoList.add(new BlockInfo(entry.getKey().subtract(this.pos),
			 * entry.getValue(), null)); } this.dungeonGenerator.add(new
			 * DungeonPartBlock(this.world, this.dungeonGenerator, this.pos, blockInfoList, new PlacementSettings(), "ZOMBIE"));
			 */
		}
	}

	public int getDunX() {
		return this.dunX;
	}

	public int getDunZ() {
		return this.dunZ;
	}

	public PlacementSettings getPlacementSettings() {
		return this.settings;
	}

	public BlockPos getPos() {
		return this.pos;
	}

}
