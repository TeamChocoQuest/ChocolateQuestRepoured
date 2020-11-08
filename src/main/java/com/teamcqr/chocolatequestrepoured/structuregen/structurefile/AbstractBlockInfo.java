package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import java.util.Random;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitant;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;

import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class AbstractBlockInfo implements IGeneratable {

	protected static final Random RANDOM = new Random();
	protected static final BlockPos.MutableBlockPos MUTABLE_POS = new BlockPos.MutableBlockPos();

	public static final byte NULL_INFO_ID = -1;
	public static final byte EMPTY_INFO_ID = 0;
	public static final byte BLOCK_INFO_ID = 1;
	public static final byte BANNER_INFO_ID = 2;
	public static final byte BOSS_INFO_ID = 3;
	public static final byte NEXUS_INFO_ID = 4;
	public static final byte CHEST_INFO_ID = 5;
	public static final byte SPAWNER_INFO_ID = 6;

	private short x;
	private short y;
	private short z;

	public AbstractBlockInfo(int x, int y, int z) {
		this.x = (short) x;
		this.y = (short) y;
		this.z = (short) z;
	}

	public AbstractBlockInfo(BlockPos pos) {
		this(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void generate(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, DungeonInhabitant dungeonMob, ProtectedRegion protectedRegion) {
		BlockPos.MutableBlockPos transformedPos = this.getTransformedMutableBlockPos(dungeonPartPos, settings.getMirror(), settings.getRotation(), MUTABLE_POS);

		if (world.isValid(transformedPos)) {
			this.clearInventoryAt(world, transformedPos);
			this.generateAt(world, dungeonPos, dungeonPartPos, settings, dungeonMob, protectedRegion, transformedPos);
		}
	}

	protected void clearInventoryAt(World world, BlockPos pos) {
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity != null) {
			if (tileentity instanceof IInventory) {
				((IInventory) tileentity).clear();
			}
			if (tileentity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
				IItemHandler itemhandler = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				for (int i = 0; i < itemhandler.getSlots(); i++) {
					itemhandler.extractItem(i, Integer.MAX_VALUE, false);
				}
			}
		}
	}

	protected abstract void generateAt(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, DungeonInhabitant dungeonMob, ProtectedRegion protectedRegion, BlockPos pos);

	public abstract byte getId();

	public final void writeToByteBuf(ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		buf.writeByte(this.getId());
		this.writeToByteBufInternal(buf, blockStatePalette, compoundTagList);
	}

	public final void readFromByteBuf(ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		int id = buf.readByte();
		if (id != this.getId()) {
			throw new IllegalArgumentException(String.format("Invalid id to read %s. Expected %d but got %d.", this.getClass().getSimpleName(), this.getId(), id));
		}
		this.readFromByteBufInternal(buf, blockStatePalette, compoundTagList);
	}

	protected abstract void writeToByteBufInternal(ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList);

	protected abstract void readFromByteBufInternal(ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList);

	@Nullable
	public static AbstractBlockInfo create(BlockPos pos, ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		return create(pos.getX(), pos.getY(), pos.getZ(), buf, blockStatePalette, compoundTagList);
	}

	@Nullable
	public static AbstractBlockInfo create(int x, int y, int z, ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		AbstractBlockInfo blockInfo;
		byte id = buf.readByte();

		switch (id) {
		case NULL_INFO_ID:
			return null;
		case EMPTY_INFO_ID:
			blockInfo = new BlockInfoEmpty(x, y, z);
			break;
		case BLOCK_INFO_ID:
			blockInfo = new BlockInfo(x, y, z);
			break;
		case BANNER_INFO_ID:
			blockInfo = new BlockInfoBanner(x, y, z);
			break;
		case BOSS_INFO_ID:
			blockInfo = new BlockInfoBoss(x, y, z);
			break;
		case NEXUS_INFO_ID:
			blockInfo = new BlockInfoForceFieldNexus(x, y, z);
			break;
		case CHEST_INFO_ID:
			blockInfo = new BlockInfoLootChest(x, y, z);
			break;
		case SPAWNER_INFO_ID:
			blockInfo = new BlockInfoSpawner(x, y, z);
			break;
		default:
			throw new IllegalArgumentException(String.format("Can't create AbstractBlockInfo with id %d", id));
		}

		blockInfo.readFromByteBufInternal(buf, blockStatePalette, compoundTagList);

		return blockInfo;
	}

	public BlockPos getPos() {
		return new BlockPos(this.x, this.y, this.z);
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getZ() {
		return this.z;
	}

	public BlockPos getTransformedBlockPos(BlockPos origin, Mirror mirrorIn, Rotation rotationIn) {
		int i = this.x;
		int j = this.y;
		int k = this.z;

		switch (mirrorIn) {
		case LEFT_RIGHT:
			k = -k;
			break;
		case FRONT_BACK:
			i = -i;
			break;
		default:
			break;
		}

		switch (rotationIn) {
		case COUNTERCLOCKWISE_90:
			return origin.add(k, j, -i);
		case CLOCKWISE_90:
			return origin.add(-k, j, i);
		case CLOCKWISE_180:
			return origin.add(-i, j, -k);
		default:
			return origin.add(i, j, k);
		}
	}

	public BlockPos.MutableBlockPos getTransformedMutableBlockPos(BlockPos origin, Mirror mirrorIn, Rotation rotationIn, BlockPos.MutableBlockPos mutablePos) {
		int i = this.x;
		int j = this.y;
		int k = this.z;

		switch (mirrorIn) {
		case LEFT_RIGHT:
			k = -k;
			break;
		case FRONT_BACK:
			i = -i;
			break;
		default:
			break;
		}

		switch (rotationIn) {
		case COUNTERCLOCKWISE_90:
			return mutablePos.setPos(origin.getX() + k, origin.getY() + j, origin.getZ() - i);
		case CLOCKWISE_90:
			return mutablePos.setPos(origin.getX() - k, origin.getY() + j, origin.getZ() + i);
		case CLOCKWISE_180:
			return mutablePos.setPos(origin.getX() - i, origin.getY() + j, origin.getZ() - k);
		default:
			return mutablePos.setPos(origin.getX() + i, origin.getY() + j, origin.getZ() + k);
		}
	}

	public BlockPos.PooledMutableBlockPos getTransformedPooledMutableBlockPos(BlockPos origin, Mirror mirrorIn, Rotation rotationIn) {
		int i = this.x;
		int j = this.y;
		int k = this.z;

		switch (mirrorIn) {
		case LEFT_RIGHT:
			k = -k;
			break;
		case FRONT_BACK:
			i = -i;
			break;
		default:
			break;
		}

		switch (rotationIn) {
		case COUNTERCLOCKWISE_90:
			return BlockPos.PooledMutableBlockPos.retain(origin.getX() + k, origin.getY() + j, origin.getZ() - i);
		case CLOCKWISE_90:
			return BlockPos.PooledMutableBlockPos.retain(origin.getX() - k, origin.getY() + j, origin.getZ() + i);
		case CLOCKWISE_180:
			return BlockPos.PooledMutableBlockPos.retain(origin.getX() - i, origin.getY() + j, origin.getZ() - k);
		default:
			return BlockPos.PooledMutableBlockPos.retain(origin.getX() + i, origin.getY() + j, origin.getZ() + k);
		}
	}

	@Deprecated
	public static AbstractBlockInfo create(int x, int y, int z, NBTTagIntArray nbtTagIntArray, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		if (nbtTagIntArray.getIntArray().length == 0) {
			return new BlockInfoEmpty(x, y, z);
		}
		switch (nbtTagIntArray.getIntArray()[0]) {
		case 0:
			return new BlockInfo(x, y, z, nbtTagIntArray, blockStatePalette, compoundTagList);
		case 1:
			return new BlockInfoBanner(x, y, z, nbtTagIntArray, blockStatePalette, compoundTagList);
		case 2:
			return new BlockInfoBoss(x, y, z);
		case 3:
			return new BlockInfoForceFieldNexus(x, y, z);
		case 4:
			return new BlockInfoLootChest(x, y, z, nbtTagIntArray);
		case 5:
			return new BlockInfoSpawner(x, y, z, nbtTagIntArray, blockStatePalette, compoundTagList);
		default:
			return new BlockInfoEmpty(x, y, z);
		}
	}

}
