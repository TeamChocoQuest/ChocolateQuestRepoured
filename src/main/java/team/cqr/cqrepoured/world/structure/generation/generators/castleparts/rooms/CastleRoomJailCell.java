package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

import net.minecraft.block.*;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;
import team.cqr.cqrepoured.util.BlockStateGenArray;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.GenerationTemplate;
import team.cqr.cqrepoured.util.SpawnerFactory;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitant;

public class CastleRoomJailCell extends CastleRoomDecoratedBase {
	private Direction doorSide;
	private List<BlockPos> prisonerSpawnerPositions = new ArrayList<>();

	public CastleRoomJailCell(int sideLength, int height, int floor, Random rand) {
		super(sideLength, height, floor, rand);
		this.roomType = EnumRoomType.JAIL;
		this.defaultCeiling = true;
		this.defaultFloor = true;
		this.doorSide = Direction.HORIZONTALS[this.random.nextInt(Direction.HORIZONTALS.length)];
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
	protected void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		int endX = this.getDecorationLengthX() - 1;
		int endZ = this.getDecorationLengthZ() - 1;

		Predicate<Vector3i> northRow = (v -> ((v.getZ() == 1) && ((v.getX() >= 1) && (v.getX() <= endX - 1))));
		Predicate<Vector3i> southRow = (v -> ((v.getZ() == endZ - 1) && ((v.getX() >= 1) && (v.getX() <= endX - 1))));
		Predicate<Vector3i> westRow = (v -> ((v.getX() == 1) && ((v.getZ() >= 1) && (v.getZ() <= endZ - 1))));
		Predicate<Vector3i> eastRow = (v -> ((v.getX() == endX - 1) && ((v.getZ() >= 1) && (v.getZ() <= endZ - 1))));

		GenerationTemplate template = new GenerationTemplate(this.getDecorationLengthX(), this.getDecorationLengthY(), this.getDecorationLengthZ());
		// here we take advantage of the fact that rules added to the template earlier will take priority
		// so we add in the order of door -> frame -> cell

		if (this.doorSide == Direction.NORTH) {
			int half = this.getDecorationLengthX() / 2;
			Predicate<Vector3i> doorFrame = (v -> ((v.getY() >= 0) && (v.getY() <= 2) && (v.getZ() == 1) && (v.getX() >= half - 1) && (v.getX() <= half + 2)));
			Predicate<Vector3i> doorLower1 = (v -> ((v.getY() == 0) && (v.getZ() == 1) && (v.getX() == half)));
			Predicate<Vector3i> doorLower2 = (v -> ((v.getY() == 0) && (v.getZ() == 1) && (v.getX() == half + 1)));
			Predicate<Vector3i> doorUpper1 = (v -> ((v.getY() == 1) && (v.getZ() == 1) && (v.getX() == half)));
			Predicate<Vector3i> doorUpper2 = (v -> ((v.getY() == 1) && (v.getZ() == 1) && (v.getX() == half + 1)));
			Predicate<Vector3i> levers = (v -> ((v.getY() == 1) && (v.getZ() == 0) && ((v.getX() == half - 1) || (v.getX() == half + 2))));

			template.addRule(doorLower1, Blocks.IRON_DOOR.getDefaultState().withProperty(DoorBlock.FACING, Direction.SOUTH).withProperty(DoorBlock.HALF, DoorBlock.EnumDoorHalf.LOWER).withProperty(DoorBlock.HINGE, DoorBlock.EnumHingePosition.RIGHT));
			template.addRule(doorLower2, Blocks.IRON_DOOR.getDefaultState().withProperty(DoorBlock.FACING, Direction.SOUTH).withProperty(DoorBlock.HALF, DoorBlock.EnumDoorHalf.LOWER).withProperty(DoorBlock.HINGE, DoorBlock.EnumHingePosition.LEFT));
			template.addRule(doorUpper1, Blocks.IRON_DOOR.getDefaultState().withProperty(DoorBlock.FACING, Direction.SOUTH).withProperty(DoorBlock.HALF, DoorBlock.EnumDoorHalf.UPPER).withProperty(DoorBlock.HINGE, DoorBlock.EnumHingePosition.RIGHT));
			template.addRule(doorUpper2, Blocks.IRON_DOOR.getDefaultState().withProperty(DoorBlock.FACING, Direction.SOUTH).withProperty(DoorBlock.HALF, DoorBlock.EnumDoorHalf.UPPER).withProperty(DoorBlock.HINGE, DoorBlock.EnumHingePosition.LEFT));
			template.addRule(doorFrame, Blocks.IRON_BLOCK.getDefaultState());
			template.addRule(levers, Blocks.LEVER.getDefaultState().withProperty(LeverBlock.FACING, LeverBlock.EnumOrientation.NORTH));
		} else if (this.doorSide == Direction.SOUTH) {
			int half = this.getDecorationLengthX() / 2;
			Predicate<Vector3i> doorFrame = (v -> ((v.getY() >= 0) && (v.getY() <= 2) && (v.getZ() == endZ - 1) && (v.getX() >= half - 1) && (v.getX() <= half + 2)));
			Predicate<Vector3i> doorLower1 = (v -> ((v.getY() == 0) && (v.getZ() == endZ - 1) && (v.getX() == half)));
			Predicate<Vector3i> doorLower2 = (v -> ((v.getY() == 0) && (v.getZ() == endZ - 1) && (v.getX() == half + 1)));
			Predicate<Vector3i> doorUpper1 = (v -> ((v.getY() == 1) && (v.getZ() == endZ - 1) && (v.getX() == half)));
			Predicate<Vector3i> doorUpper2 = (v -> ((v.getY() == 1) && (v.getZ() == endZ - 1) && (v.getX() == half + 1)));
			Predicate<Vector3i> levers = (v -> ((v.getY() == 1) && (v.getZ() == endZ) && ((v.getX() == half - 1) || (v.getX() == half + 2))));

			template.addRule(doorLower1, Blocks.IRON_DOOR.getDefaultState().withProperty(DoorBlock.FACING, Direction.NORTH).withProperty(DoorBlock.HALF, DoorBlock.EnumDoorHalf.LOWER).withProperty(DoorBlock.HINGE, DoorBlock.EnumHingePosition.LEFT));
			template.addRule(doorLower2, Blocks.IRON_DOOR.getDefaultState().withProperty(DoorBlock.FACING, Direction.NORTH).withProperty(DoorBlock.HALF, DoorBlock.EnumDoorHalf.LOWER).withProperty(DoorBlock.HINGE, DoorBlock.EnumHingePosition.RIGHT));
			template.addRule(doorUpper1, Blocks.IRON_DOOR.getDefaultState().withProperty(DoorBlock.FACING, Direction.NORTH).withProperty(DoorBlock.HALF, DoorBlock.EnumDoorHalf.UPPER).withProperty(DoorBlock.HINGE, DoorBlock.EnumHingePosition.LEFT));
			template.addRule(doorUpper2, Blocks.IRON_DOOR.getDefaultState().withProperty(DoorBlock.FACING, Direction.NORTH).withProperty(DoorBlock.HALF, DoorBlock.EnumDoorHalf.UPPER).withProperty(DoorBlock.HINGE, DoorBlock.EnumHingePosition.RIGHT));
			template.addRule(doorFrame, Blocks.IRON_BLOCK.getDefaultState());
			template.addRule(levers, Blocks.LEVER.getDefaultState().withProperty(LeverBlock.FACING, LeverBlock.EnumOrientation.SOUTH));
		} else if (this.doorSide == Direction.WEST) {
			int half = this.getDecorationLengthZ() / 2;
			Predicate<Vector3i> doorFrame = (v -> ((v.getY() >= 0) && (v.getY() <= 2) && (v.getX() == 1) && (v.getZ() >= half - 1) && (v.getZ() <= half + 2)));
			Predicate<Vector3i> doorLower1 = (v -> ((v.getY() == 0) && (v.getX() == 1) && (v.getZ() == half)));
			Predicate<Vector3i> doorLower2 = (v -> ((v.getY() == 0) && (v.getX() == 1) && (v.getZ() == half + 1)));
			Predicate<Vector3i> doorUpper1 = (v -> ((v.getY() == 1) && (v.getX() == 1) && (v.getZ() == half)));
			Predicate<Vector3i> doorUpper2 = (v -> ((v.getY() == 1) && (v.getX() == 1) && (v.getZ() == half + 1)));
			Predicate<Vector3i> levers = (v -> ((v.getY() == 1) && (v.getX() == 0) && ((v.getZ() == half - 1) || (v.getZ() == half + 2))));

			template.addRule(doorLower1, Blocks.IRON_DOOR.getDefaultState().withProperty(DoorBlock.FACING, Direction.EAST).withProperty(DoorBlock.HALF, DoorBlock.EnumDoorHalf.LOWER).withProperty(DoorBlock.HINGE, DoorBlock.EnumHingePosition.LEFT));
			template.addRule(doorLower2, Blocks.IRON_DOOR.getDefaultState().withProperty(DoorBlock.FACING, Direction.EAST).withProperty(DoorBlock.HALF, DoorBlock.EnumDoorHalf.LOWER).withProperty(DoorBlock.HINGE, DoorBlock.EnumHingePosition.RIGHT));
			template.addRule(doorUpper1, Blocks.IRON_DOOR.getDefaultState().withProperty(DoorBlock.FACING, Direction.EAST).withProperty(DoorBlock.HALF, DoorBlock.EnumDoorHalf.UPPER).withProperty(DoorBlock.HINGE, DoorBlock.EnumHingePosition.LEFT));
			template.addRule(doorUpper2, Blocks.IRON_DOOR.getDefaultState().withProperty(DoorBlock.FACING, Direction.EAST).withProperty(DoorBlock.HALF, DoorBlock.EnumDoorHalf.UPPER).withProperty(DoorBlock.HINGE, DoorBlock.EnumHingePosition.RIGHT));
			template.addRule(doorFrame, Blocks.IRON_BLOCK.getDefaultState());
			template.addRule(levers, Blocks.LEVER.getDefaultState().withProperty(LeverBlock.FACING, LeverBlock.EnumOrientation.WEST));
		} else if (this.doorSide == Direction.EAST) {
			int half = this.getDecorationLengthZ() / 2;
			Predicate<Vector3i> doorFrame = (v -> ((v.getY() >= 0) && (v.getY() <= 2) && (v.getX() == endX - 1) && (v.getZ() >= half - 1) && (v.getZ() <= half + 2)));
			Predicate<Vector3i> doorLower1 = (v -> ((v.getY() == 0) && (v.getX() == endX - 1) && (v.getZ() == half)));
			Predicate<Vector3i> doorLower2 = (v -> ((v.getY() == 0) && (v.getX() == endX - 1) && (v.getZ() == half + 1)));
			Predicate<Vector3i> doorUpper1 = (v -> ((v.getY() == 1) && (v.getX() == endX - 1) && (v.getZ() == half)));
			Predicate<Vector3i> doorUpper2 = (v -> ((v.getY() == 1) && (v.getX() == endX - 1) && (v.getZ() == half + 1)));
			Predicate<Vector3i> levers = (v -> ((v.getY() == 1) && (v.getX() == endX) && ((v.getZ() == half - 1) || (v.getZ() == half + 2))));

			template.addRule(doorLower1, Blocks.IRON_DOOR.getDefaultState().withProperty(DoorBlock.FACING, Direction.WEST).withProperty(DoorBlock.HALF, DoorBlock.EnumDoorHalf.LOWER).withProperty(DoorBlock.HINGE, DoorBlock.EnumHingePosition.RIGHT));
			template.addRule(doorLower2, Blocks.IRON_DOOR.getDefaultState().withProperty(DoorBlock.FACING, Direction.WEST).withProperty(DoorBlock.HALF, DoorBlock.EnumDoorHalf.LOWER).withProperty(DoorBlock.HINGE, DoorBlock.EnumHingePosition.LEFT));
			template.addRule(doorUpper1, Blocks.IRON_DOOR.getDefaultState().withProperty(DoorBlock.FACING, Direction.WEST).withProperty(DoorBlock.HALF, DoorBlock.EnumDoorHalf.UPPER).withProperty(DoorBlock.HINGE, DoorBlock.EnumHingePosition.RIGHT));
			template.addRule(doorUpper2, Blocks.IRON_DOOR.getDefaultState().withProperty(DoorBlock.FACING, Direction.WEST).withProperty(DoorBlock.HALF, DoorBlock.EnumDoorHalf.UPPER).withProperty(DoorBlock.HINGE, DoorBlock.EnumHingePosition.LEFT));
			template.addRule(doorFrame, Blocks.IRON_BLOCK.getDefaultState());
			template.addRule(levers, Blocks.LEVER.getDefaultState().withProperty(LeverBlock.FACING, LeverBlock.EnumOrientation.EAST));
		}

		template.addRule(northRow, Blocks.IRON_BARS.getDefaultState());
		template.addRule(southRow, Blocks.IRON_BARS.getDefaultState());
		template.addRule(westRow, Blocks.IRON_BARS.getDefaultState());
		template.addRule(eastRow, Blocks.IRON_BARS.getDefaultState());

		Map<BlockPos, BlockState> genMap = template.getGenerationMap(this.getDecorationStartPos(), true);
		genArray.addBlockStateMap(genMap, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
		for (Map.Entry<BlockPos, BlockState> entry : genMap.entrySet()) {
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

			Block spawnerBlock = CQRBlocks.SPAWNER;
			BlockState state = spawnerBlock.getDefaultState();
			TileEntitySpawner spawner = (TileEntitySpawner) spawnerBlock.createTileEntity(world, state);

			if (spawner != null) {
				spawner.inventory.setStackInSlot(0, SpawnerFactory.getSoulBottleItemStackForEntity(mobEntity));

				CompoundNBT spawnerCompound = spawner.writeToNBT(new CompoundNBT());
				genArray.addBlockState(pos, state, spawnerCompound, BlockStateGenArray.GenerationPhase.POST, BlockStateGenArray.EnumPriority.HIGH);

				this.usedDecoPositions.add(pos);
			}
		}
	}
}
