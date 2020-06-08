package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;
import com.teamcqr.chocolatequestrepoured.util.BlockPlacingHelper;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

public class BlockInfo extends AbstractBlockInfo {

	protected IBlockState blockstate;
	protected NBTTagCompound tileentityData;

	public BlockInfo(BlockPos pos, IBlockState blockstate, @Nullable NBTTagCompound tileentityData) {
		super(pos);
		this.blockstate = blockstate;
		this.tileentityData = tileentityData;
		if (this.tileentityData != null) {
			this.tileentityData.removeTag("x");
			this.tileentityData.removeTag("y");
			this.tileentityData.removeTag("z");
		}
	}

	public BlockInfo(BlockPos pos, NBTTagIntArray nbtTagIntArray, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		super(pos);
		this.readFromNBT(nbtTagIntArray, blockStatePalette, compoundTagList);
	}

	@Override
	public void generate(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, EDungeonMobType dungeonMob, ProtectedRegion protectedRegion) {
		BlockPos transformedPos = dungeonPartPos.add(Template.transformedBlockPos(settings, this.pos));
		IBlockState iblockstate = this.blockstate.withMirror(settings.getMirror()).withRotation(settings.getRotation());

		if (BlockPlacingHelper.setBlockState(world, transformedPos, iblockstate, 18, CQRConfig.advanced.instantLightUpdates) && this.tileentityData != null) {
			TileEntity tileentity = world.getTileEntity(transformedPos);

			if (tileentity != null) {
				this.tileentityData.setInteger("x", transformedPos.getX());
				this.tileentityData.setInteger("y", transformedPos.getY());
				this.tileentityData.setInteger("z", transformedPos.getZ());
				tileentity.readFromNBT(this.tileentityData);
				tileentity.mirror(settings.getMirror());
				tileentity.rotate(settings.getRotation());
			}
		}
	}

	@Override
	public int getId() {
		return BLOCK_INFO_ID;
	}

	@Override
	public NBTTagIntArray writeToNBT(BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		int[] ints;
		if (this.tileentityData != null) {
			ints = new int[3];
			ints[0] = this.getId();
			ints[1] = blockStatePalette.idFor(this.blockstate);
			ints[2] = compoundTagList.tagCount();
			compoundTagList.appendTag(this.tileentityData);
		} else {
			ints = new int[2];
			ints[0] = this.getId();
			ints[1] = blockStatePalette.idFor(this.blockstate);
		}
		return new NBTTagIntArray(ints);
	}

	@Override
	public void readFromNBT(NBTTagIntArray nbtTagIntArray, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		int[] ints = nbtTagIntArray.getIntArray();
		this.blockstate = blockStatePalette.stateFor(ints[1]);
		if (ints.length > 2) {
			this.tileentityData = compoundTagList.getCompoundTagAt(ints[2]);
		}
	}

}
