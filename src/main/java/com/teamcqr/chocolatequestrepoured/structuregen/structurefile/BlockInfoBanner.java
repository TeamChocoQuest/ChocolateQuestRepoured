package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitant;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

public class BlockInfoBanner extends BlockInfo {

	public BlockInfoBanner(BlockPos pos, IBlockState blockstate, @Nullable NBTTagCompound tileentityData) {
		super(pos, blockstate, tileentityData);
	}

	public BlockInfoBanner(int x, int y, int z, IBlockState blockstate, @Nullable NBTTagCompound tileentityData) {
		super(x, y, z, blockstate, tileentityData);
	}

	public BlockInfoBanner(BlockPos pos, NBTTagIntArray nbtTagIntArray, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		super(pos, nbtTagIntArray, blockStatePalette, compoundTagList);
	}

	public BlockInfoBanner(int x, int y, int z, NBTTagIntArray nbtTagIntArray, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		super(x, y, z, nbtTagIntArray, blockStatePalette, compoundTagList);
	}

	@Override
	public void generate(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, String dungeonMob, ProtectedRegion protectedRegion) {
		super.generate(world, dungeonPos, dungeonPartPos, settings, dungeonMob, protectedRegion);
		BlockPos transformedPos = dungeonPartPos.add(Template.transformedBlockPos(settings, this.getPos()));
		TileEntity tileEntity = world.getTileEntity(transformedPos);

		if (tileEntity instanceof TileEntityBanner) {
			DungeonInhabitant inha = DungeonInhabitantManager.getInhabitantByName(dungeonMob);
			if (inha != null && inha.getBanner() != null) {
				((TileEntityBanner) tileEntity).setItemValues(inha.getBanner().getBanner(), true);
			}
		} else {
			CQRMain.logger.warn("Failed to place banner at {}", transformedPos);
		}
	}

	@Override
	public int getId() {
		return BANNER_INFO_ID;
	}

}
