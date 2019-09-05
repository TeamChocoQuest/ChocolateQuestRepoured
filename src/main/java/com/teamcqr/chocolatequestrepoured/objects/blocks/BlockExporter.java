package com.teamcqr.chocolatequestrepoured.objects.blocks;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockExporter extends Block implements ITileEntityProvider {

	// DONE: rewrite as a container
	// If that does not work, create a packet that tells the server to save the
	// structure -> Saving bug fixed
	public BlockExporter() {
		super(Material.IRON);

		setSoundType(SoundType.METAL);
		setBlockUnbreakable();
		setResistance(Float.MAX_VALUE);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		// DONE: Send data on server to client, otherwise it wont work

		// When it is server side -> Request sync???
		if (!worldIn.isRemote) {
			((TileEntityExporter) worldIn.getTileEntity(pos)).requestSync();
		}
		playerIn.openGui(CQRMain.INSTANCE, Reference.EXPORTER_GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityExporter();
	}

	public TileEntityExporter getTileEntity(IBlockAccess world, BlockPos pos) {
		return (TileEntityExporter) world.getTileEntity(pos);
	}

}