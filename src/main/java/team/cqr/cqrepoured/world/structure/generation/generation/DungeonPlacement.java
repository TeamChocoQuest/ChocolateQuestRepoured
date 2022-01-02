package team.cqr.cqrepoured.world.structure.generation.generation;

import net.minecraft.entity.Entity;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegion;

public class DungeonPlacement {

	public static class MutableVec3d {

		public double x;
		public double y;
		public double z;

		public MutableVec3d() {

		}

		public MutableVec3d(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public MutableVec3d set(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
			return this;
		}

	}

	private static final ThreadLocal<MutableBlockPos> LOCAL_MUTABLE_BLOCKPOS = ThreadLocal.withInitial(MutableBlockPos::new);
	private static final ThreadLocal<MutableVec3d> LOCAL_MUTABLE_VEC3D = ThreadLocal.withInitial(MutableVec3d::new);
	private final BlockPos pos;
	private final BlockPos partPos;
	private final Mirror mirror;
	private final Rotation rotation;
	private final DungeonInhabitant inhabitant;
	private final ProtectedRegion.Builder protectedRegionBuilder;

	public DungeonPlacement(BlockPos pos, BlockPos partPos, Mirror mirror, Rotation rotation, DungeonInhabitant inhabitant, ProtectedRegion.Builder protectedRegionBuilder) {
		this.pos = pos;
		this.partPos = partPos;
		this.mirror = mirror;
		this.rotation = rotation;
		this.inhabitant = inhabitant;
		this.protectedRegionBuilder = protectedRegionBuilder;
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public BlockPos getPartPos() {
		return this.partPos;
	}

	public Mirror getMirror() {
		return this.mirror;
	}

	public Rotation getRotation() {
		return this.rotation;
	}

	public DungeonInhabitant getInhabitant() {
		return this.inhabitant;
	}

	public ProtectedRegion.Builder getProtectedRegionBuilder() {
		return this.protectedRegionBuilder;
	}

	public MutableBlockPos transform(BlockPos pos) {
		return transform(pos.getX(), pos.getY(), pos.getZ(), this.partPos, this.mirror, this.rotation);
	}

	public MutableBlockPos transform(int x, int y, int z) {
		return transform(x, y, z, this.partPos, this.mirror, this.rotation);
	}

	public static MutableBlockPos transform(BlockPos pos, Mirror mirror, Rotation rotation) {
		return transform(pos.getX(), pos.getY(), pos.getZ(), BlockPos.ORIGIN, mirror, rotation);
	}

	public static MutableBlockPos transform(int x, int y, int z, Mirror mirror, Rotation rotation) {
		return transform(x, y, z, BlockPos.ORIGIN, mirror, rotation);
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
			return LOCAL_MUTABLE_BLOCKPOS.get().setPos(origin.getX() + z, origin.getY() + y, origin.getZ() - x);
		case CLOCKWISE_90:
			return LOCAL_MUTABLE_BLOCKPOS.get().setPos(origin.getX() - z, origin.getY() + y, origin.getZ() + x);
		case CLOCKWISE_180:
			return LOCAL_MUTABLE_BLOCKPOS.get().setPos(origin.getX() - x, origin.getY() + y, origin.getZ() - z);
		default:
			return LOCAL_MUTABLE_BLOCKPOS.get().setPos(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
		}
	}

	public MutableVec3d transform(Vector3d vec) {
		return transform(vec.x, vec.y, vec.z, this.partPos, this.mirror, this.rotation);
	}

	public MutableVec3d transform(double x, double y, double z) {
		return transform(x, y, z, this.partPos, this.mirror, this.rotation);
	}

	public static MutableVec3d transform(Vector3d vec, Mirror mirror, Rotation rotation) {
		return transform(vec.x, vec.y, vec.z, BlockPos.ORIGIN, mirror, rotation);
	}

	public static MutableVec3d transform(double x, double y, double z, Mirror mirror, Rotation rotation) {
		return transform(x, y, z, BlockPos.ORIGIN, mirror, rotation);
	}

	public static MutableVec3d transform(Vector3d vec, BlockPos origin, Mirror mirror, Rotation rotation) {
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
		return entity.rotationYaw + entity.getMirroredYaw(mirror) - entity.getRotatedYaw(rotation);
	}

}
