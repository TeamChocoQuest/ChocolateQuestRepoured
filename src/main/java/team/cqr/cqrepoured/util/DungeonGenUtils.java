package team.cqr.cqrepoured.util;

import java.util.Random;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
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
	public static boolean isInsideSpheroid(Vector3i pointOnSphere, double radX, double radY, double radZ) {
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

	public static boolean isInsideSpheroid(Vector3i pointInSpace, Vector3i spheroidCenter, double radX, double radY, double radZ) {
		return isInsideSpheroid(new Vector3i(pointInSpace.getX() - spheroidCenter.getX(), pointInSpace.getY() - spheroidCenter.getY(), pointInSpace.getZ() - spheroidCenter.getZ()), radX, radY, radZ);
	}

	public static boolean isInsideSpheroid(Vector3i pointOnSphere, double radWidth, double radHeight) {
		return isInsideSpheroid(pointOnSphere, radWidth, radHeight, radWidth);
	}

	public static boolean isInsideSpheroid(Vector3i pointInSpace, Vector3i spheroidCenter, double radWidth, double radHeight) {
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
		return MathHelper.clamp(result, min, max);
	}

	public static boolean isLootChest(Block b) {
		return b instanceof BlockExporterChest;
	}

	public static boolean isCQBanner(BannerTileEntity banner) {
		return BannerHelper.isCQBanner(banner);
	}

	public static boolean isInWallRange(World world, int chunkX, int chunkZ) {
		// Check if the wall is enabled
		if (!CQRConfig.wall.enabled) {
			return false;
		}
		// Check if the world is the overworld
		if (world.dimension() == World.OVERWORLD) {
			return false;
		}
		// Check the coordinates
		if (chunkZ < -CQRConfig.wall.distance - 12) {
			return false;
		}
		return chunkZ <= -CQRConfig.wall.distance + 12;
	}

	public static boolean isFarAwayEnoughFromSpawn(World world, int chunkX, int chunkZ) {
		//Correct replacement?
		if (!world.dimensionType().respawnAnchorWorks()) {
			return true;
		}
		int x = chunkX - (getSpawnX(world) >> 4);
		int z = chunkZ - (getSpawnZ(world) >> 4);
		return x * x + z * z >= CQRConfig.general.dungeonSpawnDistance * CQRConfig.general.dungeonSpawnDistance;
	}

	public static boolean isFarAwayEnoughFromLocationSpecifics(World world, int chunkX, int chunkZ, int distance) {
		ResourceLocation dim = world.dimension().getRegistryName();

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
				int x = chunkX - (dungeonSpawnPos.getX(world) >> 4);
				int z = chunkZ - (dungeonSpawnPos.getZ(world) >> 4);
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
	public static Vector3i rotateVec3i(Vector3i vec, Direction side) {
		if (side == Direction.SOUTH) {
			return new Vector3i(-vec.getX(), vec.getY(), -vec.getZ());
		} else if (side == Direction.WEST) {
			return new Vector3i(vec.getZ(), vec.getY(), -vec.getX());
		} else if (side == Direction.EAST) {
			return new Vector3i(-vec.getZ(), vec.getY(), vec.getX());
		} else {
			// North side, or some other invalid side
			return vec;
		}
	}

	public static Vector3i rotateMatrixOffsetCW(Vector3i offset, int sizeX, int sizeZ, int numRotations) {
		final int maxXIndex = sizeX - 1;
		final int maxZIndex = sizeZ - 1;

		if (numRotations % 4 == 0) {
			return new Vector3i(offset.getX(), offset.getY(), offset.getZ());
		} else if (numRotations % 4 == 1) {
			return new Vector3i(maxZIndex - offset.getZ(), offset.getY(), offset.getX());
		} else if (numRotations % 4 == 2) {
			return new Vector3i(maxXIndex - offset.getX(), offset.getY(), maxZIndex - offset.getZ());
		} else {
			return new Vector3i(offset.getZ(), offset.getY(), maxXIndex - offset.getX());
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
		BlockPos pos = Template.transformedBlockPos(settings, size);
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

	public static int getYForPos(World world, int x, int z, boolean ignoreWater) {
		Chunk chunk = world.getChunk(x >> 4, z >> 4);
		BlockPos.Mutable mutablePos = new BlockPos.Mutable(x, chunk.getHighestSectionPosition() + 15, z);
		Material material = chunk.getBlockState(mutablePos).getMaterial();
		while (mutablePos.getY() > 0 && (material == Material.AIR || material == Material.WOOD || material == Material.LEAVES || material == Material.PLANTS || (ignoreWater && material == Material.WATER))) {
			mutablePos.setY(mutablePos.getY() - 1);
			material = chunk.getBlockState(mutablePos).getMaterial();
		}
		return mutablePos.getY() + 1;
	}

	public static Vector3d transformedVec3d(Vector3d vec, PlacementSettings settings) {
		return transformedVec3d(vec, settings.getMirror(), settings.getRotation());
	}

	public static Vector3d transformedVec3d(Vector3d vec, Mirror mirror, Rotation rotation) {
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
			return new Vector3d(k, j, 1.0D - i);
		case CLOCKWISE_90:
			return new Vector3d(1.0D - k, j, i);
		case CLOCKWISE_180:
			return new Vector3d(1.0D - i, j, 1.0D - k);
		default:
			return flag ? new Vector3d(i, j, k) : vec;
		}
	}

	public static ListNBT writePosToList(BlockPos pos) {
		ListNBT nbtTagList = new ListNBT();
		nbtTagList.add(IntNBT.valueOf(pos.getX()));
		nbtTagList.add(IntNBT.valueOf(pos.getY()));
		nbtTagList.add(IntNBT.valueOf(pos.getZ()));
		return nbtTagList;
	}

	public static BlockPos readPosFromList(ListNBT nbtTagList) {
		return new BlockPos(nbtTagList.getInt(0), nbtTagList.getInt(1), nbtTagList.getInt(2));
	}

	public static ListNBT writeVecToList(Vector3d vec) {
		ListNBT nbtTagList = new ListNBT();
		nbtTagList.add(DoubleNBT.valueOf(vec.x));
		nbtTagList.add(DoubleNBT.valueOf(vec.y));
		nbtTagList.add(DoubleNBT.valueOf(vec.z));
		return nbtTagList;
	}

	public static Vector3d readVecFromList(ListNBT nbtTagList) {
		return new Vector3d(nbtTagList.getDouble(0), nbtTagList.getDouble(1), nbtTagList.getDouble(2));
	}

	public static ListNBT writeUUIDToList(UUID uuid) {
		ListNBT nbtTagList = new ListNBT();
		nbtTagList.add(LongNBT.valueOf(uuid.getMostSignificantBits()));
		nbtTagList.add(LongNBT.valueOf(uuid.getLeastSignificantBits()));
		return nbtTagList;
	}

	public static UUID readUUIDFromList(ListNBT nbtTagList) {
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
		BlockPos transformedSize = Template.transformedBlockPos(settings, structure.getSize());
		return pos.offset(-(transformedSize.getX() >> 1), 0, -(transformedSize.getZ() >> 1));
	}

	public static int getSpawnX(World world) {
		int x = world.getWorldInfo().getSpawnX();
		return x >= world.getWorldBorder().getMinX() && x < world.getWorldBorder().getMaxX() ? x : MathHelper.floor(world.getWorldBorder().getCenterX());
	}

	public static int getSpawnY(World world) {
		return world.getWorldInfo().getSpawnY();
	}

	public static int getSpawnZ(World world) {
		int z = world.getWorldInfo().getSpawnZ();
		return z >= world.getWorldBorder().getMinZ() && z < world.getWorldBorder().getMaxZ() ? z : MathHelper.floor(world.getWorldBorder().getCenterZ());
	}

}
