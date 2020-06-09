package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.spiral;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockUnlitTorch;
import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonVolcano;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.AbstractDungeonPart;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartBlock;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.AbstractDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano.StairCaseHelper;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.AbstractBlockInfo;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.BlockInfo;
import com.teamcqr.chocolatequestrepoured.util.ESkyDirection;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class StrongholdBuilder {

	private AbstractDungeonGenerator<DungeonVolcano> generator;
	private DungeonGenerator dungeonGenerator;
	private BlockPos startPos;
	private DungeonVolcano dungeon;
	private int blocksRemainingToWall;
	private EnumFacing direction;
	private World world;
	private List<AbstractDungeonPart> strongholdParts = new ArrayList<>();

	public StrongholdBuilder(AbstractDungeonGenerator<DungeonVolcano> generator, DungeonGenerator dungeonGenerator, BlockPos start, int distanceToWall, DungeonVolcano dungeon, EnumFacing expansionDirection, World world) {
		this.generator = generator;
		this.dungeonGenerator = dungeonGenerator;
		this.startPos = start;
		this.dungeon = dungeon;
		this.blocksRemainingToWall = distanceToWall;
		this.direction = expansionDirection;
		this.world = world;
	}

	public void generate(int cX, int cZ, String mobType) {

		Vec3i expansionVector = new Vec3i(0, 0, 0);
		switch (this.direction) {
		case EAST:
			expansionVector = new Vec3i(3, 0, 0);
			break;
		case NORTH:
			expansionVector = new Vec3i(0, 0, -3);
			break;
		case SOUTH:
			expansionVector = new Vec3i(0, 0, 3);
			break;
		case WEST:
			expansionVector = new Vec3i(-3, 0, 0);
			break;
		default:
			break;
		}
		// DONE: Place fire pots and "porch"
		BlockPos pos = this.startPos;// .add(expansionV);

		List<AbstractBlockInfo> blockInfoList = new ArrayList<>();
		
		for (int i = 0; i < (this.blocksRemainingToWall / 4) + 2; i++) {
			this.buildSegment(pos.subtract(this.startPos), blockInfoList);
			pos = pos.add(expansionVector);
		}
		this.strongholdParts.add(new DungeonPartBlock(world, dungeonGenerator, this.startPos, blockInfoList, new PlacementSettings(), mobType));
		buildStronghold(pos.add(0,-1,0), world, cX, cZ, mobType);
	}

	private void buildStronghold(BlockPos pos, World world2, int cX, int cZ, String mobType) {
		SpiralStrongholdBuilder stronghold = new SpiralStrongholdBuilder(this.generator, this.dungeonGenerator, ESkyDirection.fromFacing(this.direction), this.dungeon, new Random(WorldDungeonGenerator.getSeed(this.world, pos.getX() /16, pos.getZ() /16)));
		stronghold.calculateFloors(pos);
		stronghold.buildFloors(pos.add(0,-1,0), world, cX, cZ, mobType);
		this.strongholdParts.addAll(stronghold.getStrongholdParts());
	}

	private void buildSegment(BlockPos startPosCentered, List<AbstractBlockInfo> blockInfoList) {
		//COrner 2 is always the reference location for the part (!)
		BlockPos corner1, corner2, pillar1, pillar2, torch1, torch2, air1, air2;
		corner1 = null;
		corner2 = null;
		//Pillars are in the middle of the part (on the expansion axis)
		pillar1 = null;
		pillar2 = null;
		//marks the positions of the torches
		torch1 = null;
		torch2 = null;
		//these mark the corners of the complete part
		air1 = null;
		air2 = null;
		switch (this.direction) {
		case EAST:
			corner1 = startPosCentered.add(0, 0, -3);
			corner2 = startPosCentered.add(3, 0, 3);
			air1 = startPosCentered.add(0, 1, -2);
			air2 = startPosCentered.add(3, 5, -2);
			pillar1 = startPosCentered.add(1, 0, 2);
			pillar2 = startPosCentered.add(1, 0, -2);
			torch1 = startPosCentered.add(1, 4, 1);
			torch2 = startPosCentered.add(1, 4, -1);
			break;
		case NORTH:
			corner1 = startPosCentered.add(-3, 0, 0);
			corner2 = startPosCentered.add(3, 0, -3);
			air1 = startPosCentered.add(-2, 1, 0);
			air2 = startPosCentered.add(2, 5, -3);
			pillar1 = startPosCentered.add(2, 0, -1);
			pillar2 = startPosCentered.add(-2, 0, -1);
			torch1 = startPosCentered.add(1, 4, -1);
			torch2 = startPosCentered.add(-1, 4, -1);
			break;
		case SOUTH:
			corner1 = startPosCentered.add(3, 0, 0);
			corner2 = startPosCentered.add(-3, 0, 3);
			air1 = startPosCentered.add(2, 1, 0);
			air2 = startPosCentered.add(-2, 5, 3);
			pillar1 = startPosCentered.add(-2, 0, 1);
			pillar2 = startPosCentered.add(2, 0, 1);
			torch1 = startPosCentered.add(-1, 4, 1);
			torch2 = startPosCentered.add(1, 4, 1);
			break;
		case WEST:
			corner1 = startPosCentered.add(0, 0, 3);
			corner2 = startPosCentered.add(-3, 0, -3);
			air1 = startPosCentered.add(0, 1, 2);
			air2 = startPosCentered.add(-3, 5, 2);
			pillar1 = startPosCentered.add(-1, 0, -2);
			pillar2 = startPosCentered.add(-1, 0, 2);
			torch1 = startPosCentered.add(-1, 4, -1);
			torch2 = startPosCentered.add(-1, 4, 1);
			break;
		default:
			break;
		}
		if (corner1 != null && corner2 != null && pillar1 != null && pillar2 != null) {
			for (BlockPos airPos : BlockPos.getAllInBox(air1, air2)) {
				blockInfoList.add(new BlockInfo(airPos, Blocks.AIR.getDefaultState(), null));
			}
			this.buildFloorAndCeiling(corner1, corner2, 5, blockInfoList);

			// Left torch -> Facing side: rotate right (90.0°)
			this.buildPillar(pillar1, blockInfoList);
			blockInfoList.add(new BlockInfo(torch1, ModBlocks.UNLIT_TORCH.getDefaultState().withProperty(BlockUnlitTorch.FACING, StairCaseHelper.getFacingWithRotation(this.direction, Rotation.COUNTERCLOCKWISE_90)), null));
			// Right torch -> Facing side: rotate left (-90.0°)
			this.buildPillar(pillar2, blockInfoList);
			blockInfoList.add(new BlockInfo(torch2, ModBlocks.UNLIT_TORCH.getDefaultState().withProperty(BlockUnlitTorch.FACING, StairCaseHelper.getFacingWithRotation(this.direction, Rotation.CLOCKWISE_90)), null));
		}
	}

	private void buildPillar(BlockPos bottom, List<AbstractBlockInfo> blockInfoList) {
		for (int iY = 1; iY <= 4; iY++) {
			BlockPos pos = bottom.add(0, iY, 0);
			blockInfoList.add(new BlockInfo(pos, ModBlocks.GRANITE_PILLAR.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), null));
		}
		blockInfoList.add(new BlockInfo(bottom.add(0, 5, 0), ModBlocks.GRANITE_CARVED.getDefaultState(), null));
	}

	private void buildFloorAndCeiling(BlockPos start, BlockPos end, int ceilingHeight, List<AbstractBlockInfo> blockInfoList) {
		BlockPos endP = new BlockPos(end.getX(), start.getY(), end.getZ());

		// Floor
		for (BlockPos p : BlockPos.getAllInBox(start, endP)) {
			blockInfoList.add(new BlockInfo(p, ModBlocks.GRANITE_SMALL.getDefaultState(), null));
		}

		// Ceiling
		for (BlockPos p : BlockPos.getAllInBox(start.add(0, ceilingHeight + 1, 0), endP.add(0, ceilingHeight + 1, 0))) {
			blockInfoList.add(new BlockInfo(p, ModBlocks.GRANITE_SQUARE.getDefaultState(), null));
		}
	}

	public List<AbstractDungeonPart> getStrongholdParts() {
		return strongholdParts;
	}

}
