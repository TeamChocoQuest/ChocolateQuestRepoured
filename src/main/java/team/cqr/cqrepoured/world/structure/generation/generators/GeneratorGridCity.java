package team.cqr.cqrepoured.world.structure.generation.generators;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.gen.ChunkGenerator;
import team.cqr.cqrepoured.world.structure.generation.GenerationUtil;
import team.cqr.cqrepoured.world.structure.generation.WorldDungeonGenerator;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonGridCity;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;
import team.cqr.cqrepoured.world.structure.generation.structurefile.Offset;

public class GeneratorGridCity extends LegacyDungeonGenerator<DungeonGridCity> {

	private int longestSide;
	private int distanceBetweenBuildings;

	// TODO: Dont make this a Set, sets are slow as they need to calculate the hash keys every time you add something to
	// them...
	private BlockPos[][] gridPositions;
	private Set<BlockPos> bridgeBuilderStartPositionsX = new HashSet<>();
	private Set<BlockPos> bridgeBuilderStartPositionsZ = new HashSet<>();

	private Set<BlockPos> bridgeBlocks = new HashSet<>();
	private Set<BlockPos> lavaBlocks = new HashSet<>();

	private Map<BlockPos, BlockState> blockMap = new HashMap<>();

	private int minX;
	private int maxX;
	private int minZ;
	private int maxZ;

	// private static int tunnelHeight = 3;

	private CQStructure[][] structures;

	public GeneratorGridCity(ChunkGenerator cg, BlockPos pos, DungeonGridCity dungeon, Random rand) {
		super(cg, pos, dungeon, rand);
	}

	@Override
	public void preProcess() {
		// Calculate the grid for the buildings
		int rowsX = this.dungeon.getXRows(this.random) >> 1;
		int rowsZ = this.dungeon.getZRows(this.random) >> 1;

		this.structures = new CQStructure[(rowsX << 1) + 1][(rowsZ << 1) + 1];
		this.gridPositions = new BlockPos[(rowsX << 1) + 1][(rowsZ << 1) + 1];
		for (int iX = -rowsX; iX <= rowsX; iX++) {
			for (int iZ = -rowsZ; iZ <= rowsZ; iZ++) {
				File file;
				if (this.dungeon.centralBuildingIsSpecial() && iX == 0 && iZ == 0) {
					file = this.dungeon.getRandomCentralBuilding(this.random);
				} else {
					file = this.dungeon.getRandomBuilding(this.random);
				}
				CQStructure structure = CQStructure.createFromFile(file);
				this.structures[iX + rowsX][iZ + rowsZ] = structure;

				if (structure != null) {
					int k = Math.max(structure.getSize().getX(), structure.getSize().getZ());
					if (k > this.longestSide) {
						this.longestSide = k;
					}
				}
			}
		}
		this.distanceBetweenBuildings = (int) (this.longestSide * this.dungeon.getBridgeSizeMultiplier());

		this.minX = this.pos.getX() - (rowsX * this.distanceBetweenBuildings);
		this.minZ = this.pos.getZ() - (rowsZ * this.distanceBetweenBuildings);
		this.maxX = this.pos.getX() + (rowsX * this.distanceBetweenBuildings);
		this.maxZ = this.pos.getZ() + (rowsZ * this.distanceBetweenBuildings);

		// This constructs the grid around the center
		for (int iX = -rowsX; iX <= rowsX; iX++) {
			for (int iZ = -rowsZ; iZ <= rowsZ; iZ++) {

				/*
				 * boolean noAddFlag = false; if (this.dungeon.centralBuildingIsSpecial() && iX == 0 && iZ == 0) { noAddFlag = true; }
				 */

				BlockPos p = this.pos.offset((iX * this.distanceBetweenBuildings), 0, (iZ * this.distanceBetweenBuildings));
				// if (!noAddFlag) {
				this.gridPositions[iX + rowsX][iZ + rowsZ] = p;
				// }
				// Bridge starter positions, in total there will be rowsX +rowsZ -1 bridges
				if (iX == 0) {
					this.bridgeBuilderStartPositionsZ.add(p);
				}
				if (iZ == 0) {
					this.bridgeBuilderStartPositionsX.add(p);
				}
			}
		}
	}

	@Override
	public void buildStructure() {
		// Dig out the big air pocket or the small ones
		if (this.dungeon.makeSpaceForBuildings()) {
			/*
			 * if(dungeon.useSingleAirPocketsForHouses()) { for(BlockPos pocketCenter : gridPositions) { BlockPos cLower = new
			 * BlockPos(pocketCenter.getX() -
			 * dungeon.getLongestSide() /2, y +1, pocketCenter.getZ() - dungeon.getLongestSide()); BlockPos
			 * cUpper = new BlockPos(pocketCenter.getX() + dungeon.getLongestSide() /2, y +dungeon.getCaveHeight(),
			 * pocketCenter.getZ() + dungeon.getLongestSide());
			 * 
			 * PlateauBuilder pB = new PlateauBuilder(); pB.makeRandomBlob(new Random(), dungeon.getAirPocketBlock(), cLower,
			 * cUpper, WorldDungeonGenerator.getSeed(world,
			 * pocketCenter.getX(), pocketCenter.getZ()), world); } } else {
			 */
			BlockPos cLower = new BlockPos(this.minX, this.pos.getY() + 1, this.minZ).offset(-this.distanceBetweenBuildings, 0, -this.distanceBetweenBuildings);
			BlockPos cUpper = new BlockPos(this.maxX, this.pos.getY() + this.dungeon.getCaveHeight(), this.maxZ).offset(this.distanceBetweenBuildings * 0.1, 0, this.distanceBetweenBuildings * 0.05);

			GenerationUtil.makeRandomBlob(this.dungeonBuilder.getLevel(), Blocks.AIR, cLower, cUpper, 4, WorldDungeonGenerator.getSeed(this.dungeonBuilder.getLevel().getSeed(), this.minX, this.maxZ), cLower);

		}

		// Build the roads / bridges and the floors
		for (BlockPos lavaPos : BlockPos.betweenClosed(this.minX - this.distanceBetweenBuildings, this.pos.getY(), this.minZ - this.distanceBetweenBuildings, this.maxX + this.distanceBetweenBuildings, this.pos.getY(), this.maxZ + this.distanceBetweenBuildings)) {
			this.lavaBlocks.add(lavaPos);
		}
		// Bridges from south to north
		for (BlockPos pX : this.bridgeBuilderStartPositionsX) {
			for (int iZ = this.minZ; iZ <= this.maxZ; iZ++) {
				BlockPos pC = new BlockPos(pX.getX(), pX.getY(), iZ);
				BlockPos pCE = pC.east();
				BlockPos pCW = pC.west();
				this.bridgeBlocks.add(pC);
				this.bridgeBlocks.add(pCE);
				this.bridgeBlocks.add(pCW);

				// Tunnels if not big air pocket
				/*
				 * if(dungeon.makeSpaceForBuildings() && dungeon.useSingleAirPocketsForHouses()) { for(int n = 1; n <= tunnelHeight;
				 * n++) { world.setBlockToAir(pC.up(n));
				 * world.setBlockToAir(pCE.up(n)); world.setBlockToAir(pCW.up(n)); } }
				 */
			}
		}
		// Bridges from west to east
		for (BlockPos pZ : this.bridgeBuilderStartPositionsZ) {
			for (int iX = this.minX; iX <= this.maxX; iX++) {
				BlockPos pC = new BlockPos(iX, pZ.getY(), pZ.getZ());
				BlockPos pCN = pC.north();
				BlockPos pCS = pC.south();
				this.bridgeBlocks.add(pC);
				this.bridgeBlocks.add(pCN);
				this.bridgeBlocks.add(pCS);

				// Tunnels if not big air pocket
				/*
				 * if(dungeon.makeSpaceForBuildings() && dungeon.useSingleAirPocketsForHouses()) { for(int n = 1; n <= tunnelHeight;
				 * n++) { world.setBlockToAir(pC.up(n));
				 * world.setBlockToAir(pCN.up(n)); world.setBlockToAir(pCS.up(n)); } }
				 */
			}
		}

		for (BlockPos p : this.lavaBlocks) {
			this.blockMap.put(p, this.dungeon.getFloorBlock());
		}
		for (BlockPos p : this.bridgeBlocks) {
			this.blockMap.put(p, this.dungeon.getBridgeBlock());
		}
	}

	@Override
	public void postProcess() {
		Map<BlockPos, CQStructure> structureMap = new HashMap<>();
		for (int iX = 0; iX < this.gridPositions.length && iX < this.structures.length; iX++) {
			for (int iZ = 0; iZ < this.gridPositions[iX].length && iZ < this.structures[iX].length; iZ++) {
				BlockPos structurePos = this.gridPositions[iX][iZ];
				CQStructure structure = this.structures[iX][iZ];

				if (structurePos != null && structure != null) {
					BlockPos cL = structurePos.subtract(new Vector3i(structure.getSize().getX() / 2 + 2, 0, structure.getSize().getZ() / 2 + 2));
					BlockPos cU = structurePos.offset(structure.getSize().getX() / 2 + 2, 0, structure.getSize().getZ() / 2 + 2);
					BlockPos.betweenClosed(cL, cU).forEach(p -> this.blockMap.put(p, this.dungeon.getBridgeBlock()));

					structureMap.put(structurePos.above(), structure);
				}
			}
		}

		//BlockDungeonPart.Builder partBuilder = new BlockDungeonPart.Builder();
		for (Map.Entry<BlockPos, BlockState> entry : this.blockMap.entrySet()) {
			//partBuilder.add(new PreparableBlockInfo(entry.getKey().subtract(this.pos), entry.getValue(), null));
			this.dungeonBuilder.getLevel().setBlockState(entry.getKey().subtract(this.pos), entry.getValue());
		}
		//this.dungeonBuilder.add(partBuilder);

		for (Map.Entry<BlockPos, CQStructure> entry : structureMap.entrySet()) {
			entry.getValue().addAll(this.dungeonBuilder, entry.getKey(), Offset.CENTER);
		}
	}

}
