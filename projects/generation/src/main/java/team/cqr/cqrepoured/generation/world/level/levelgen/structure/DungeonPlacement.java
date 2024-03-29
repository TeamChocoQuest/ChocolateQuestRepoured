package team.cqr.cqrepoured.generation.world.level.levelgen.structure;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.entity.EntityFactory;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.inhabitant.DungeonInhabitant;
import team.cqr.cqrepoured.protection.ProtectedRegion;

public record DungeonPlacement(BlockPos pos, BlockPos partPos, Mirror mirror, Rotation rotation, DungeonInhabitant inhabitant,
		Optional<ProtectedRegion.Builder> protectedRegionBuilder, EntityFactory entityFactory, RandomSource random) {

	public static class MutableVec3d {

		public double x;
		public double y;
		public double z;

		public MutableVec3d set(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
			return this;
		}

	}

	private static final ThreadLocal<MutableBlockPos> LOCAL_MUTABLE_BLOCKPOS = ThreadLocal.withInitial(MutableBlockPos::new);
	private static final ThreadLocal<MutableVec3d> LOCAL_MUTABLE_VEC3D = ThreadLocal.withInitial(MutableVec3d::new);

	public MutableBlockPos transform(BlockPos pos) {
		return transform(pos.getX(), pos.getY(), pos.getZ(), this.partPos, this.mirror, this.rotation);
	}

	public MutableBlockPos transform(int x, int y, int z) {
		return transform(x, y, z, this.partPos, this.mirror, this.rotation);
	}

	public static MutableBlockPos transform(BlockPos pos, Mirror mirror, Rotation rotation) {
		return transform(pos.getX(), pos.getY(), pos.getZ(), BlockPos.ZERO, mirror, rotation);
	}

	public static MutableBlockPos transform(int x, int y, int z, Mirror mirror, Rotation rotation) {
		return transform(x, y, z, BlockPos.ZERO, mirror, rotation);
	}

	public static MutableBlockPos transform(BlockPos pos, BlockPos origin, Mirror mirror, Rotation rotation) {
		return transform(pos.getX(), pos.getY(), pos.getZ(), origin, mirror, rotation);
	}

	public static MutableBlockPos transform(int x, int y, int z, BlockPos origin, Mirror mirror, Rotation rotation) {
		switch (mirror) {
		case LEFT_RIGHT:
			z = -z;
			break;
		case FRONT_BACK:
			x = -x;
			break;
		default:
			break;
		}

		switch (rotation) {
		case COUNTERCLOCKWISE_90:
			return LOCAL_MUTABLE_BLOCKPOS.get().set(origin.getX() + z, origin.getY() + y, origin.getZ() - x);
		case CLOCKWISE_90:
			return LOCAL_MUTABLE_BLOCKPOS.get().set(origin.getX() - z, origin.getY() + y, origin.getZ() + x);
		case CLOCKWISE_180:
			return LOCAL_MUTABLE_BLOCKPOS.get().set(origin.getX() - x, origin.getY() + y, origin.getZ() - z);
		default:
			return LOCAL_MUTABLE_BLOCKPOS.get().set(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
		}
	}

	public MutableVec3d transform(Vec3 vec) {
		return transform(vec.x, vec.y, vec.z, this.partPos, this.mirror, this.rotation);
	}

	public MutableVec3d transform(double x, double y, double z) {
		return transform(x, y, z, this.partPos, this.mirror, this.rotation);
	}

	public static MutableVec3d transform(Vec3 vec, Mirror mirror, Rotation rotation) {
		return transform(vec.x, vec.y, vec.z, BlockPos.ZERO, mirror, rotation);
	}

	public static MutableVec3d transform(double x, double y, double z, Mirror mirror, Rotation rotation) {
		return transform(x, y, z, BlockPos.ZERO, mirror, rotation);
	}

	public static MutableVec3d transform(Vec3 vec, BlockPos origin, Mirror mirror, Rotation rotation) {
		return transform(vec.x, vec.y, vec.z, origin, mirror, rotation);
	}

	public static MutableVec3d transform(double x, double y, double z, BlockPos origin, Mirror mirror, Rotation rotation) {
		switch (mirror) {
		case LEFT_RIGHT:
			z = 1.0D - z;
			break;
		case FRONT_BACK:
			x = 1.0D - x;
			break;
		default:
			break;
		}

		switch (rotation) {
		case COUNTERCLOCKWISE_90:
			return LOCAL_MUTABLE_VEC3D.get().set(origin.getX() + z, origin.getY() + y, origin.getZ() + 1.0D - x);
		case CLOCKWISE_90:
			return LOCAL_MUTABLE_VEC3D.get().set(origin.getX() + 1.0D - z, origin.getY() + y, origin.getZ() + x);
		case CLOCKWISE_180:
			return LOCAL_MUTABLE_VEC3D.get().set(origin.getX() + 1.0D - x, origin.getY() + y, origin.getZ() + 1.0D - z);
		default:
			return LOCAL_MUTABLE_VEC3D.get().set(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
		}
	}

	public float transform(Entity entity) {
		return transform(entity, this.mirror, this.rotation);
	}

	public static float transform(Entity entity, Mirror mirror, Rotation rotation) {
		return entity.getYHeadRot() + entity.mirror(mirror) - entity.rotate(rotation);
	}

	public BlockState transform(BlockState state) {
		return transform(state, this.mirror, this.rotation);
	}

	@SuppressWarnings("deprecation")
	public static BlockState transform(BlockState state, Mirror mirror, Rotation rotation) {
		return state.mirror(mirror).rotate(rotation);
	}

	public void transform(BlockEntity blockEntity) {
		transform(blockEntity, this.mirror, this.rotation);
	}

	public static void transform(BlockEntity blockEntity, Mirror mirror, Rotation rotation) {
		// TODO method not needed anymore
//		blockEntity.mirror(mirror);
//		blockEntity.rotate(rotation);
	}

}
