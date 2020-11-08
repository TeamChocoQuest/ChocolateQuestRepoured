package com.teamcqr.chocolatequestrepoured.objects.blocks;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporterChestCustom;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class BlockExporterChestCustom extends BlockExporterChest {

	public BlockExporterChestCustom(String resourceName) {
		super(resourceName);
	}

	public BlockExporterChestCustom(String resourceDomain, String resourcePath) {
		super(resourceDomain, resourcePath);
	}

	public BlockExporterChestCustom(ResourceLocation overlayTexture) {
		super(overlayTexture);
	}

	@Override
	public ResourceLocation getLootTable(World world, BlockPos pos) {
		TileEntity tileEntity = world.getTileEntity(pos);
		return tileEntity instanceof TileEntityExporterChestCustom ? ((TileEntityExporterChestCustom) tileEntity).getLootTable() : LootTableList.EMPTY;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityExporterChestCustom();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			playerIn.openGui(CQRMain.INSTANCE, Reference.EXPORTER_CHEST_GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

}
