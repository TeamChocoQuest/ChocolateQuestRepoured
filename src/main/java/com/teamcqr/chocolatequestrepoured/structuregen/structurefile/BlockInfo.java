package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitant;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;
import com.teamcqr.chocolatequestrepoured.util.BlockPlacingHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class BlockInfo extends AbstractBlockInfo {

	protected IBlockState blockstate;
	protected NBTTagCompound tileentityData;

	public BlockInfo(int x, int y, int z) {
		super(x, y, z);
	}

	public BlockInfo(BlockPos pos) {
		super(pos);
	}

	public BlockInfo(int x, int y, int z, IBlockState blockstate, @Nullable NBTTagCompound tileentityData) {
		super(x, y, z);
		this.blockstate = blockstate;
		this.tileentityData = tileentityData;
		if (this.tileentityData != null) {
			this.tileentityData.removeTag("x");
			this.tileentityData.removeTag("y");
			this.tileentityData.removeTag("z");
		}
	}

	public BlockInfo(BlockPos pos, IBlockState blockstate, @Nullable NBTTagCompound tileentityData) {
		this(pos.getX(), pos.getY(), pos.getZ(), blockstate, tileentityData);
	}

	@Override
	public void generateAt(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, DungeonInhabitant dungeonMob, ProtectedRegion protectedRegion, BlockPos pos) {
		IBlockState iblockstate = this.blockstate.withMirror(settings.getMirror()).withRotation(settings.getRotation());
		BlockPlacingHelper.setBlockState(world, pos, iblockstate, 18, false);

		if (this.tileentityData != null) {
			TileEntity tileentity = world.getTileEntity(pos);

			if (tileentity != null) {
				this.tileentityData.setInteger("x", pos.getX());
				this.tileentityData.setInteger("y", pos.getY());
				this.tileentityData.setInteger("z", pos.getZ());
				tileentity.readFromNBT(this.tileentityData);
				this.tileentityData.removeTag("x");
				this.tileentityData.removeTag("y");
				this.tileentityData.removeTag("z");
				tileentity.mirror(settings.getMirror());
				tileentity.rotate(settings.getRotation());
			}
		}
	}

	@Override
	public byte getId() {
		return BLOCK_INFO_ID;
	}

	@Override
	protected void writeToByteBufInternal(ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		int i = (blockStatePalette.idFor(this.blockstate) << 1) | (this.tileentityData != null ? 1 : 0);
		ByteBufUtils.writeVarInt(buf, i, 5);
		if (this.tileentityData != null) {
			ByteBufUtils.writeVarInt(buf, compoundTagList.tagCount(), 5);
			compoundTagList.appendTag(this.tileentityData);
		}
	}

	@Override
	protected void readFromByteBufInternal(ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		int i = ByteBufUtils.readVarInt(buf, 5);
		this.blockstate = blockStatePalette.stateFor(i >>> 1);
		if ((i & 1) == 1) {
			this.tileentityData = compoundTagList.getCompoundTagAt(ByteBufUtils.readVarInt(buf, 5));
		}
	}

	public IBlockState getBlockstate() {
		return this.blockstate;
	}

	public NBTTagCompound getTileentityData() {
		return this.tileentityData;
	}

	@Deprecated
	public BlockInfo(int x, int y, int z, NBTTagIntArray nbtTagIntArray, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		super(x, y, z);
		int[] ints = nbtTagIntArray.getIntArray();
		this.blockstate = blockStatePalette.stateFor(ints[1]);
		if (ints.length > 2) {
			this.tileentityData = compoundTagList.getCompoundTagAt(ints[2]);
		}
	}

}
