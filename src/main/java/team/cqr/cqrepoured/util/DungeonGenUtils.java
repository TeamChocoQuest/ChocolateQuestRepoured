package team.cqr.cqrepoured.util;

import java.util.Random;
import java.util.UUID;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import team.cqr.cqrepoured.block.BlockExporterChest;
import team.cqr.cqrepoured.block.banner.BannerHelper;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.world.structure.generation.DungeonRegistry;
import team.cqr.cqrepoured.world.structure.generation.DungeonSpawnPos;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;
import team.cqr.cqrepoured.world.structure.generation.structurefile.Offset;

/**
 * Copyright (c) 29.04.2019<br>
 * Developed by DerToaster98<br>
 * GitHub: https://github.com/DerToaster98
 */
public class DungeonGenUtils {

	private static final Random RAND = new Random();

	public static boolean isInsideCircle(int x, int z, int radius) {
		return x * x + z * z < radius * radius;
	}

	public static boolean isInsideSphere(BlockPos pos, BlockPos center, int radius) {
		return center.distSqr(pos) < radius * radius;
	}

	public static boolean isInsideSphere(int x, int y, int z, int radius) {
		return x * x + y * y + z * z < radius * radius;
	}

	// Center of spheroid is (0/0/0)
	public static boolean isInsideSpheroid(Vec3i pointOnSphere, double radX, double radY, double radZ) {
		double axisX = pointOnSphere.getX();
		axisX *= axisX;
		axisX /= (radX * radX);
		double axisY = pointOnSphere.getY();
		axisY *= axisY;
		axisY /= (radY * radY);
		double axisZ = pointOnSphere.getZ();
		axisZ *= axisZ;
		axisZ /= (radZ * radZ);
		return axisX + axisY + axisZ == 1.0D;
	}

	public static boolean isInsideSpheroid(Vec3i pointInSpace, Vec3i spheroidCenter, double radX, double radY, double radZ) {
		return isInsideSpheroid(new Vec3i(pointInSpace.getX() - spheroidCenter.getX(), pointInSpace.getY() - spheroidCenter.getY(), pointInSpace.getZ() - spheroidCenter.getZ()), radX, radY, radZ);
	}

	public static boolean isInsideSpheroid(Vec3i pointOnSphere, double radWidth, double radHeight) {
		return isInsideSpheroid(pointOnSphere, radWidth, radHeight, radWidth);
	}

	public static boolean isInsideSpheroid(Vec3i pointInSpace, Vec3i spheroidCenter, double radWidth, double radHeight) {
		return isInsideSpheroid(pointInSpace, spheroidCenter, radWidth, radHeight, radWidth);
	}

	public static boolean percentageRandom(int chance) {
		return percentageRandom(chance, RAND);
	}

	public static boolean percentageRandom(int chance, Random rand) {
		if (chance <= 0) {
			return false;
		}
		if (chance >= 100) {
			return true;
		}
		return rand.nextInt(100) < chance;
	}

	public static boolean percentageRandom(double chance) {
		return percentageRandom(chance, RAND);
	}

	public static boolean percentageRandom(double chance, Random rand) {
		if (chance <= 0.0D) {
			return false;
		}
		if (chance >= 1.0D) {
			return true;
		}
		return rand.nextDouble() < chance;
	}

	public static int randomBetween(int min, int max) {
		return randomBetween(min, max, RAND);
	}

	public static int randomBetween(int min, int max, Random rand) {
		if (min >= max) {
			return min;
		}
		return min + rand.nextInt(max - min + 1);
	}

	public static int randomBetweenGaussian(int min, int max) {
		return randomBetweenGaussian(min, max, RAND);
	}

	public static int randomBetweenGaussian(int min, int max, Random rand) {
		if (min >= max) {
			return min;
		}
		double avg = min + ((max - min) / 2.0D);
		double stdDev = (max - avg) / 3.0D; // guarantees that MOST (99.7%) results will be between low & high
		double gaussian = rand.nextGaussian();
		int result = (int) (avg + (gaussian * stdDev) + 0.5D); // 0.5 is added for rounding to nearest whole number
		return Mth.clamp(result, min, max);
	}

	public static boolean isLootChest(Block b) {
		return b instanceof BlockExporterChest;
	}

	public static boolean isCQBanner(BannerBlockEntity banner) {
		return BannerHelper.isCQBanner(banner);
	}

	public static boolean isInWallRange(Level world, ChunkPos chunkPos) {
		// Check if the wall is enabled
		if (!CQRConfig.SERVER_CONFIG.wall.enabled.get()) {
			return false;
		}
		// Check if the world is the overworld
		if (world.dimension() == Level.OVERWORLD) {
			return false;
		}
		// Check the coordinates
		if (chunkPos.z < -CQRConfig.SERVER_CONFIG.wall.distance.get() - 12) {
			return false;
		}
		return chunkPos.z <= -CQRConfig.SERVER_CONFIG.wall.distance.get() + 12;
	}

	public static boolean isFarAwayEnoughFromSpawn(Level world, ChunkPos chunkPos) {
		//Correct replacement?
		if (!world.dimensionType().respawnAnchorWorks()) {
			return true;
		}
		int x = chunkPos.x - (getSpawnX(world) >> 4);
		int z = chunkPos.z - (getSpawnZ(world) >> 4);
		return x * x + z * z >= CQRConfig.SERVER_CONFIG.general.dungeonSpawnDistance.get() * CQRConfig.SERVER_CONFIG.general.dungeonSpawnDistance.get();
	}

	public static boolean isFarAwayEnoughFromLocationSpecifics(Level world, ChunkPos chunkPos, int distance) {
		ResourceLocation dim = world.dimension().location();

		for (DungeonBase dungeon : DungeonRegistry.getInstance().getDungeons()) {
			if (!dungeon.isEnabled()) {
				continue;
			}
			if (dungeon.isModDependencyMissing()) {
				continue;
			}
			if (!dungeon.isValidDim(dim)) {
				continue;
			}
			for (DungeonSpawnPos dungeonSpawnPos : dungeon.getLockedPositions()) {
				int x = chunkPos.x - (dungeonSpawnPos.getX(world) >> 4);
				int z = chunkPos.z - (dungeonSpawnPos.getZ(world) >> 4);
				if (x * x + z * z < distance * distance) {
					return false;
				}
			}
		}

		return true;
	}

	/*
	 * Rotate a vec3i to align with the given side. Assumes that the vec3i is default +x right, +z down coordinate system
	 */
	public static Vec3i rotateVec3i(Vec3i vec, Direction side) {
		if (side == Direction.SOUTH) {
			return new Vec3i(-vec.getX(), vec.getY(), -vec.getZ());
		} else if (side == Direction.WEST) {
			return new Vec3i(vec.getZ(), vec.getY(), -vec.getX());
		} else if (side == Direction.EAST) {
			return new Vec3i(-vec.getZ(), vec.getY(), vec.getX());
		} else {
			// North side, or some other invalid side
			return vec;
		}
	}

	public static Vec3i rotateMatrixOffsetCW(Vec3i offset, int sizeX, int sizeZ, int numRotations) {
		final int maxXIndex = sizeX - 1;
		final int maxZIndex = sizeZ - 1;

		if (numRotations % 4 == 0) {
			return new Vec3i(offset.getX(), offset.getY(), offset.getZ());
		} else if (numRotations % 4 == 1) {
			return new Vec3i(maxZIndex - offset.getZ(), offset.getY(), offset.getX());
		} else if (numRotations % 4 == 2) {
			return new Vec3i(maxXIndex - offset.getX(), offset.getY(), maxZIndex - offset.getZ());
		} else {
			return new Vec3i(offset.getZ(), offset.getY(), maxXIndex - offset.getX());
		}
	}

	public static int getCWRotationsBetween(Direction start, Direction end) {
		int rotations = 0;
		if (start.getAxis().isHorizontal() && end.getAxis().isHorizontal()) {
			while (start != end) {
				start = start.getClockWise();
				rotations++;
			}
		}
		return rotations;
	}

	public static Direction rotateFacingNTimesAboutY(Direction facing, int n) {
		for (int i = 0; i < n; i++) {
			facing = facing.getClockWise();
		}
		return facing;
	}

	public static BlockPos getMinPos(BlockPos pos1, BlockPos pos2) {
		return new BlockPos(Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY()), Math.min(pos1.getZ(), pos2.getZ()));
	}

	public static BlockPos getMaxPos(BlockPos pos1, BlockPos pos2) {
		return new BlockPos(Math.max(pos1.getX(), pos2.getX()), Math.max(pos1.getY(), pos2.getY()), Math.max(pos1.getZ(), pos2.getZ()));
	}

	public static BlockPos getValidMinPos(BlockPos pos1, BlockPos pos2) {
		return new BlockPos(Math.max(Math.min(pos1.getX(), pos2.getX()), -30_000_000), Math.max(Math.min(pos1.getY(), pos2.getY()), 0), Math.max(Math.min(pos1.getZ(), pos2.getZ()), -30_000_000));
	}

	public static BlockPos getValidMaxPos(BlockPos pos1, BlockPos pos2) {
		return new BlockPos(Math.min(Math.max(pos1.getX(), pos2.getX()), 30_000_000), Math.min(Math.max(pos1.getY(), pos2.getY()), 255), Math.min(Math.max(pos1.getZ(), pos2.getZ()), 30_000_000));
	}

	public static BlockPos getTransformedStartPos(BlockPos startPos, BlockPos size, PlacementSettings settings) {
		if (settings.getMirror() == Mirror.NONE && settings.getRotation() == Rotation.NONE) {
			return startPos;
		}
		//Source pos, mirror, rot, offset
		//Zero offset is correct?
		BlockPos pos = Template.transform(size, settings.getMirror(), settings.getRotation(), BlockPos.ZERO);
		int x = startPos.getX();
		int y = startPos.getY();
		int z = startPos.getZ();
		boolean flag = false;
		if (pos.getX() < 0) {
			x -= pos.getX() + 1;
			flag = true;
		}
		if (pos.getZ() < 0) {
			z -= pos.getZ() + 1;
			flag = true;
		}
		return flag ? new BlockPos(x, y, z) : startPos;
	}

	public static int getYForPos(ChunkGenerator chunkGenerator, int x, int z, boolean ignoreWater) {
		if (ignoreWater) {
			return chunkGenerator.getBaseHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
		}
		return chunkGenerator.getBaseHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
	}

	public static Vec3 transformedVec3d(Vec3 vec, PlacementSettings settings) {
		return transformedVec3d(vec, settings.getMirror(), settings.getRotation());
	}

	public static Vec3 transformedVec3d(Vec3 vec, Mirror mirror, Rotation rotation) {
		double i = vec.x;
		double j = vec.y;
		double k = vec.z;
		boolean flag = true;

		switch (mirror) {
		case LEFT_RIGHT:
			k = 1.0D - k;
			break;
		case FRONT_BACK:
			i = 1.0D - i;
			break;
		default:
			flag = false;
		}

		switch (rotation) {
		case COUNTERCLOCKWISE_90:
			return new Vec3(k, j, 1.0D - i);
		case CLOCKWISE_90:
			return new Vec3(1.0D - k, j, i);
		case CLOCKWISE_180:
			return new Vec3(1.0D - i, j, 1.0D - k);
		default:
			return flag ? new Vec3(i, j, k) : vec;
		}
	}

	public static ListTag writePosToList(BlockPos pos) {
		ListTag nbtTagList = new ListTag();
		nbtTagList.add(IntNBT.valueOf(pos.getX()));
		nbtTagList.add(IntNBT.valueOf(pos.getY()));
		nbtTagList.add(IntNBT.valueOf(pos.getZ()));
		return nbtTagList;
	}

	public static BlockPos readPosFromList(ListTag nbtTagList) {
		return new BlockPos(nbtTagList.getInt(0), nbtTagList.getInt(1), nbtTagList.getInt(2));
	}

	public static ListTag writeVecToList(Vec3 vec) {
		ListTag nbtTagList = new ListTag();
		nbtTagList.add(DoubleNBT.valueOf(vec.x));
		nbtTagList.add(DoubleNBT.valueOf(vec.y));
		nbtTagList.add(DoubleNBT.valueOf(vec.z));
		return nbtTagList;
	}

	public static Vec3 readVecFromList(ListTag nbtTagList) {
		return new Vec3(nbtTagList.getDouble(0), nbtTagList.getDouble(1), nbtTagList.getDouble(2));
	}

	public static ListTag writeUUIDToList(UUID uuid) {
		ListTag nbtTagList = new ListTag();
		nbtTagList.add(LongNBT.valueOf(uuid.getMostSignificantBits()));
		nbtTagList.add(LongNBT.valueOf(uuid.getLeastSignificantBits()));
		return nbtTagList;
	}

	public static UUID readUUIDFromList(ListTag nbtTagList) {
		INBT nbtM = nbtTagList.get(0);
		INBT nbtL = nbtTagList.get(1);
		return new UUID(nbtM instanceof LongNBT ? ((LongNBT) nbtM).getAsLong() : 0, nbtM instanceof LongNBT ? ((LongNBT) nbtL).getAsLong() : 0);
	}

	/**
	 * @deprecated Use {@link Offset} instead!
	 * 
	 * @return the passed position with the half of the transformed structure sizeX and sizeZ subtracted.
	 */
	@Deprecated
	public static BlockPos getCentralizedPosForStructure(BlockPos pos, CQStructure structure, PlacementSettings settings) {
		//Source pos, mirror, rot, offset
		//Zero offset is correct?
		BlockPos transformedSize = Template.transform(structure.getSize(), settings.getMirror(), settings.getRotation(), BlockPos.ZERO);
		return pos.offset(-(transformedSize.getX() >> 1), 0, -(transformedSize.getZ() >> 1));
	}

	public static int getSpawnX(Level world) {
		int x = world.getLevelData().getXSpawn();
		return x >= world.getWorldBorder().getMinX() && x < world.getWorldBorder().getMaxX() ? x : Mth.floor(world.getWorldBorder().getCenterX());
	}

	public static int getSpawnY(Level world) {
		return world.getLevelData().getYSpawn();
	}

	public static int getSpawnZ(Level world) {
		int z = world.getLevelData().getYSpawn();
		return z >= world.getWorldBorder().getMinZ() && z < world.getWorldBorder().getMaxZ() ? z : Mth.floor(world.getWorldBorder().getCenterZ());
	}

}
