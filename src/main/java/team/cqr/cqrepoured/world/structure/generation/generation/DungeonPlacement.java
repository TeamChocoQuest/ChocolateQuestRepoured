package team.cqr.cqrepoured.world.structure.generation.generation;

import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mirror;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
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

	private static final ThreadLocal<BlockPos.Mutable> LOCAL_MUTABLE_BLOCKPOS = ThreadLocal.withInitial(BlockPos.Mutable::new);
	private static final ThreadLocal<MutableVec3d> LOCAL_MUTABLE_VEC3D = ThreadLocal.withInitial(MutableVec3d::new);
	private final BlockPos pos;
	private final BlockPos partPos;
	private final Mirror mirror;
	private final Rotation rotation;
	private final DungeonInhabitant inhabitant;
	private final ProtectedRegion.Builder protectedRegionBuilder;
	private final ServerEntityFactory entityFactory;


	public DungeonPlacement(BlockPos pos, BlockPos partPos, Mirror mirror, Rotation rotation, DungeonInhabitant inhabitant, ProtectedRegion.Builder protectedRegionBuilder, ServerEntityFactory entityFactory) {
		this.pos = pos;
		this.partPos = partPos;
		this.mirror = mirror;
		this.rotation = rotation;
		this.inhabitant = inhabitant;
		this.protectedRegionBuilder = protectedRegionBuilder;
		this.entityFactory = entityFactory;
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

	public ServerEntityFactory getEntityFactory() {
		return entityFactory;
	}

	public BlockPos.Mutable transform(BlockPos pos) {
		return transform(pos.getX(), pos.getY(), pos.getZ(), this.partPos, this.mirror, this.rotation);
	}

	public BlockPos.Mutable transform(int x, int y, int z) {
		return transform(x, y, z, this.partPos, this.mirror, this.rotation);
	}

	public static BlockPos.Mutable transform(BlockPos pos, Mirror mirror, Rotation rotation) {
		return transform(pos.getX(), pos.getY(), pos.getZ(), BlockPos.ZERO, mirror, rotation);
	}

	public static BlockPos.Mutable transform(int x, int y, int z, Mirror mirror, Rotation rotation) {
		return transform(x, y, z, BlockPos.ZERO, mirror, rotation);
	}

	public static BlockPos.Mutable transform(BlockPos pos, BlockPos origin, Mirror mirror, Rotation rotation) {
		return transform(pos.getX(), pos.getY(), pos.getZ(), origin, mirror, rotation);
	}

	public static BlockPos.Mutable transform(int x, int y, int z, BlockPos origin, Mirror mirror, Rotation rotation) {
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
		blockEntity.mirror(mirror);
		blockEntity.rotate(rotation);
	}

}
