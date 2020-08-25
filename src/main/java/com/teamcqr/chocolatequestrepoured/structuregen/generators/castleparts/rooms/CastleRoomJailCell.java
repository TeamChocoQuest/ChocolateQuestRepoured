package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.factories.SpawnerFactory;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitant;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.GenerationTemplate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLever;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class CastleRoomJailCell extends CastleRoomDecoratedBase {
	private EnumFacing doorSide;
	private ArrayList<BlockPos> prisonerSpawnerPositions = new ArrayList<>();

	public CastleRoomJailCell(int sideLength, int height, int floor) {
		super(sideLength, height, floor);
		this.roomType = EnumRoomType.JAIL;
		this.defaultCeiling = true;
		this.defaultFloor = true;
		this.doorSide = EnumFacing.HORIZONTALS[this.random.nextInt(EnumFacing.HORIZONTALS.length)];
	}

	@Override
	boolean shouldBuildEdgeDecoration() {
		return false;
	}

	@Override
	boolean shouldBuildWallDecoration() {
		return true;
	}

	@Override
	boolean shouldBuildMidDecoration() {
		return false;
	}

	@Override
	boolean shouldAddSpawners() {
		return false;
	}

	@Override
	boolean shouldAddChests() {
		return false;
	}

	@Override
	protected void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonCastle dungeon) {
		int endX = this.getDecorationLengthX() - 1;
		int endZ = this.getDecorationLengthZ() - 1;

		Predicate<Vec3i> northRow = (v -> ((v.getZ() == 1) && ((v.getX() >= 1) && (v.getX() <= endX - 1))));
		Predicate<Vec3i> southRow = (v -> ((v.getZ() == endZ - 1) && ((v.getX() >= 1) && (v.getX() <= endX - 1))));
		Predicate<Vec3i> westRow = (v -> ((v.getX() == 1) && ((v.getZ() >= 1) && (v.getZ() <= endZ - 1))));
		Predicate<Vec3i> eastRow = (v -> ((v.getX() == endX - 1) && ((v.getZ() >= 1) && (v.getZ() <= endZ - 1))));

		GenerationTemplate template = new GenerationTemplate(this.getDecorationLengthX(), this.getDecorationLengthY(), this.getDecorationLengthZ());
		// here we take advantage of the fact that rules added to the template earlier will take priority
		// so we add in the order of door -> frame -> cell

		if (this.doorSide == EnumFacing.NORTH) {
			int half = this.getDecorationLengthX() / 2;
			Predicate<Vec3i> doorFrame = (v -> ((v.getY() >= 0) && (v.getY() <= 2) && (v.getZ() == 1) && (v.getX() >= half - 1) && (v.getX() <= half + 2)));
			Predicate<Vec3i> doorLower1 = (v -> ((v.getY() == 0) && (v.getZ() == 1) && (v.getX() == half)));
			Predicate<Vec3i> doorLower2 = (v -> ((v.getY() == 0) && (v.getZ() == 1) && (v.getX() == half + 1)));
			Predicate<Vec3i> doorUpper1 = (v -> ((v.getY() == 1) && (v.getZ() == 1) && (v.getX() == half)));
			Predicate<Vec3i> doorUpper2 = (v -> ((v.getY() == 1) && (v.getZ() == 1) && (v.getX() == half + 1)));
			Predicate<Vec3i> levers = (v -> ((v.getY() == 1) && (v.getZ() == 0) && ((v.getX() == half - 1) || (v.getX() == half + 2))));

			template.addRule(doorLower1, Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.SOUTH).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.RIGHT));
			template.addRule(doorLower2, Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.SOUTH).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT));
			template.addRule(doorUpper1, Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.SOUTH).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.RIGHT));
			template.addRule(doorUpper2, Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.SOUTH).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT));
			template.addRule(doorFrame, Blocks.IRON_BLOCK.getDefaultState());
			template.addRule(levers, Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, BlockLever.EnumOrientation.NORTH));
		} else if (this.doorSide == EnumFacing.SOUTH) {
			int half = this.getDecorationLengthX() / 2;
			Predicate<Vec3i> doorFrame = (v -> ((v.getY() >= 0) && (v.getY() <= 2) && (v.getZ() == endZ - 1) && (v.getX() >= half - 1) && (v.getX() <= half + 2)));
			Predicate<Vec3i> doorLower1 = (v -> ((v.getY() == 0) && (v.getZ() == endZ - 1) && (v.getX() == half)));
			Predicate<Vec3i> doorLower2 = (v -> ((v.getY() == 0) && (v.getZ() == endZ - 1) && (v.getX() == half + 1)));
			Predicate<Vec3i> doorUpper1 = (v -> ((v.getY() == 1) && (v.getZ() == endZ - 1) && (v.getX() == half)));
			Predicate<Vec3i> doorUpper2 = (v -> ((v.getY() == 1) && (v.getZ() == endZ - 1) && (v.getX() == half + 1)));
			Predicate<Vec3i> levers = (v -> ((v.getY() == 1) && (v.getZ() == endZ) && ((v.getX() == half - 1) || (v.getX() == half + 2))));

			template.addRule(doorLower1, Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT));
			template.addRule(doorLower2, Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.RIGHT));
			template.addRule(doorUpper1, Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT));
			template.addRule(doorUpper2, Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.RIGHT));
			template.addRule(doorFrame, Blocks.IRON_BLOCK.getDefaultState());
			template.addRule(levers, Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, BlockLever.EnumOrientation.SOUTH));
		} else if (this.doorSide == EnumFacing.WEST) {
			int half = this.getDecorationLengthZ() / 2;
			Predicate<Vec3i> doorFrame = (v -> ((v.getY() >= 0) && (v.getY() <= 2) && (v.getX() == 1) && (v.getZ() >= half - 1) && (v.getZ() <= half + 2)));
			Predicate<Vec3i> doorLower1 = (v -> ((v.getY() == 0) && (v.getX() == 1) && (v.getZ() == half)));
			Predicate<Vec3i> doorLower2 = (v -> ((v.getY() == 0) && (v.getX() == 1) && (v.getZ() == half + 1)));
			Predicate<Vec3i> doorUpper1 = (v -> ((v.getY() == 1) && (v.getX() == 1) && (v.getZ() == half)));
			Predicate<Vec3i> doorUpper2 = (v -> ((v.getY() == 1) && (v.getX() == 1) && (v.getZ() == half + 1)));
			Predicate<Vec3i> levers = (v -> ((v.getY() == 1) && (v.getX() == 0) && ((v.getZ() == half - 1) || (v.getZ() == half + 2))));

			template.addRule(doorLower1, Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.EAST).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT));
			template.addRule(doorLower2, Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.EAST).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.RIGHT));
			template.addRule(doorUpper1, Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.EAST).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT));
			template.addRule(doorUpper2, Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.EAST).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.RIGHT));
			template.addRule(doorFrame, Blocks.IRON_BLOCK.getDefaultState());
			template.addRule(levers, Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, BlockLever.EnumOrientation.WEST));
		} else if (this.doorSide == EnumFacing.EAST) {
			int half = this.getDecorationLengthZ() / 2;
			Predicate<Vec3i> doorFrame = (v -> ((v.getY() >= 0) && (v.getY() <= 2) && (v.getX() == endX - 1) && (v.getZ() >= half - 1) && (v.getZ() <= half + 2)));
			Predicate<Vec3i> doorLower1 = (v -> ((v.getY() == 0) && (v.getX() == endX - 1) && (v.getZ() == half)));
			Predicate<Vec3i> doorLower2 = (v -> ((v.getY() == 0) && (v.getX() == endX - 1) && (v.getZ() == half + 1)));
			Predicate<Vec3i> doorUpper1 = (v -> ((v.getY() == 1) && (v.getX() == endX - 1) && (v.getZ() == half)));
			Predicate<Vec3i> doorUpper2 = (v -> ((v.getY() == 1) && (v.getX() == endX - 1) && (v.getZ() == half + 1)));
			Predicate<Vec3i> levers = (v -> ((v.getY() == 1) && (v.getX() == endX) && ((v.getZ() == half - 1) || (v.getZ() == half + 2))));

			template.addRule(doorLower1, Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.WEST).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.RIGHT));
			template.addRule(doorLower2, Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.WEST).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT));
			template.addRule(doorUpper1, Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.WEST).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.RIGHT));
			template.addRule(doorUpper2, Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.WEST).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT));
			template.addRule(doorFrame, Blocks.IRON_BLOCK.getDefaultState());
			template.addRule(levers, Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, BlockLever.EnumOrientation.EAST));
		}

		template.addRule(northRow, Blocks.IRON_BARS.getDefaultState());
		template.addRule(southRow, Blocks.IRON_BARS.getDefaultState());
		template.addRule(westRow, Blocks.IRON_BARS.getDefaultState());
		template.addRule(eastRow, Blocks.IRON_BARS.getDefaultState());

		HashMap<BlockPos, IBlockState> genMap = template.GetGenerationMap(this.getDecorationStartPos(), true);
		genArray.addBlockStateMap(genMap, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
		for (Map.Entry<BlockPos, IBlockState> entry : genMap.entrySet()) {
			if (entry.getValue().getBlock() != Blocks.AIR) {
				this.usedDecoPositions.add(entry.getKey());
			}
		}

		// Add all spaces inside the cell to the list os possible prisoner spawn locations
		for (int x = 2; x < this.getDecorationLengthX() - 2; x++) {
			for (int z = 2; z < this.getDecorationLengthZ() - 2; z++) {
				this.prisonerSpawnerPositions.add(this.roomOrigin.add(x, 1, z));
			}
		}
	}

	public void addPrisonerSpawners(DungeonInhabitant jailInhabitant, BlockStateGenArray genArray, World world) {
		Collections.shuffle(this.prisonerSpawnerPositions, this.random);

		int spawnerCount = DungeonGenUtils.randomBetween(2, 5, this.random);

		for (int i = 0; (i < spawnerCount && !this.prisonerSpawnerPositions.isEmpty()); i++) {
			BlockPos pos = this.prisonerSpawnerPositions.get(i);

			Entity mobEntity = EntityList.createEntityByIDFromName(jailInhabitant.getEntityID(), world);

			Block spawnerBlock = ModBlocks.SPAWNER;
			IBlockState state = spawnerBlock.getDefaultState();
			TileEntitySpawner spawner = (TileEntitySpawner) spawnerBlock.createTileEntity(world, state);

			if (spawner != null) {
				spawner.inventory.setStackInSlot(0, SpawnerFactory.getSoulBottleItemStackForEntity(mobEntity));

				NBTTagCompound spawnerCompound = spawner.writeToNBT(new NBTTagCompound());
				genArray.addBlockState(pos, state, spawnerCompound, BlockStateGenArray.GenerationPhase.POST, BlockStateGenArray.EnumPriority.HIGH);

				this.usedDecoPositions.add(pos);
			}
		}
	}
}
