package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public abstract class AbstractDungeonPart {

	public static final String DUNGEON_PART_BLOCK_ID = "dungeon_part_block";
	public static final String DUNGEON_PART_BLOCK_SPECIAL_ID = "dungeon_part_block_special";
	public static final String DUNGEON_PART_COVER_ID = "dungeon_part_cover";
	public static final String DUNGEON_PART_ENTITY_ID = "dungeon_part_entity";
	public static final String DUNGEON_PART_LIGHT_ID = "dungeon_part_light";
	public static final String DUNGEON_PART_PLATEAU_ID = "dungeon_part_plateau";

	protected final World world;
	protected final DungeonGenerator dungeonGenerator;
	protected BlockPos partPos;
	protected BlockPos minPos;
	protected BlockPos maxPos;

	public AbstractDungeonPart(World world, DungeonGenerator dungeonGenerator) {
		this(world, dungeonGenerator, BlockPos.ORIGIN);
	}

	public AbstractDungeonPart(World world, DungeonGenerator dungeonGenerator, BlockPos partPos) {
		this.world = world;
		this.dungeonGenerator = dungeonGenerator;
		this.partPos = partPos;
		this.minPos = partPos;
		this.maxPos = partPos;
	}

	public static AbstractDungeonPart createDungeonPart(World world, DungeonGenerator dungeonGenerator, NBTTagCompound compound) {
		if (compound.hasKey("id", Constants.NBT.TAG_STRING)) {
			String id = compound.getString("id");
			try {
				AbstractDungeonPart dungeonPart = null;
				switch (id) {
				case DUNGEON_PART_BLOCK_ID:
					dungeonPart = new DungeonPartBlock(world, dungeonGenerator);
					break;
				case DUNGEON_PART_BLOCK_SPECIAL_ID:
					dungeonPart = new DungeonPartBlockSpecial(world, dungeonGenerator);
					break;
				case DUNGEON_PART_COVER_ID:
					dungeonPart = new DungeonPartCover(world, dungeonGenerator);
					break;
				case DUNGEON_PART_ENTITY_ID:
					dungeonPart = new DungeonPartEntity(world, dungeonGenerator);
					break;
				case DUNGEON_PART_LIGHT_ID:
					dungeonPart = new DungeonPartLight(world, dungeonGenerator);
					break;
				case DUNGEON_PART_PLATEAU_ID:
					dungeonPart = new DungeonPartPlateau(world, dungeonGenerator);
					break;
				default:
					break;
				}
				dungeonPart.readFromNBT(compound);
				return dungeonPart;
			} catch (Exception e) {
				CQRMain.logger.error("Failed to create dungeon part for id " + id, e);
			}
		}

		return null;
	}

	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("id", this.getId());
		compound.setTag("partPos", NBTUtil.createPosTag(this.partPos));
		compound.setTag("minPos", NBTUtil.createPosTag(this.minPos));
		compound.setTag("maxPos", NBTUtil.createPosTag(this.maxPos));
		return compound;
	}

	public void readFromNBT(NBTTagCompound compound) {
		if (!compound.hasKey("id") || !compound.getString("id").equals(this.getId())) {
			throw new IllegalArgumentException();
		}
		this.partPos = NBTUtil.getPosFromTag(compound.getCompoundTag("partPos"));
		this.minPos = NBTUtil.getPosFromTag(compound.getCompoundTag("minPos"));
		this.maxPos = NBTUtil.getPosFromTag(compound.getCompoundTag("maxPos"));
	}

	public abstract String getId();

	public abstract void generateNext();

	public abstract boolean isGenerated();

	public BlockPos getPartPos() {
		return this.partPos;
	}

	public BlockPos getMinPos() {
		return this.minPos;
	}

	public BlockPos getMaxPos() {
		return this.maxPos;
	}

	protected void updateMinAndMaxPos(BlockPos pos) {
		this.minPos = DungeonGenUtils.getValidMinPos(pos, this.minPos);
		this.maxPos = DungeonGenUtils.getValidMaxPos(pos, this.maxPos);
	}

	protected BlockPos.MutableBlockPos transformedXYZasMutablePos(BlockPos pos, int x, int y, int z, Mirror mirror, Rotation rotation, BlockPos.MutableBlockPos mutablePos) {
		switch (mirror) {
		case LEFT_RIGHT:
			z = -z;
			break;
		case FRONT_BACK:
			x = -x;
			break;
		default:
		}

		switch (rotation) {
		case COUNTERCLOCKWISE_90:
			return mutablePos.setPos(pos.getX() + z, pos.getY() + y, pos.getZ() - x);
		case CLOCKWISE_90:
			return mutablePos.setPos(pos.getX() - z, pos.getY() + y, pos.getZ() + x);
		case CLOCKWISE_180:
			return mutablePos.setPos(pos.getX() - x, pos.getY() + y, pos.getZ() - z);
		default:
			return mutablePos.setPos(pos);
		}
	}

	protected BlockPos.MutableBlockPos transformedXYZasMutablePos(BlockPos pos, double x, double y, double z, Mirror mirror, Rotation rotation, BlockPos.MutableBlockPos mutablePos) {
		switch (mirror) {
		case LEFT_RIGHT:
			z = -z;
			break;
		case FRONT_BACK:
			x = -x;
			break;
		default:
		}

		switch (rotation) {
		case COUNTERCLOCKWISE_90:
			return mutablePos.setPos(pos.getX() + z, pos.getY() + y, pos.getZ() - x);
		case CLOCKWISE_90:
			return mutablePos.setPos(pos.getX() - z, pos.getY() + y, pos.getZ() + x);
		case CLOCKWISE_180:
			return mutablePos.setPos(pos.getX() - x, pos.getY() + y, pos.getZ() - z);
		default:
			return mutablePos.setPos(pos);
		}
	}

}
