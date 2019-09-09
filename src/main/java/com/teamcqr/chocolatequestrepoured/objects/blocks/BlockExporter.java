package com.teamcqr.chocolatequestrepoured.objects.blocks;

import com.teamcqr.chocolatequestrepoured.client.gui.GuiExporter;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockExporter extends Block implements ITileEntityProvider {

	public BlockExporter() {
		super(Material.IRON);

		setSoundType(SoundType.METAL);
		setBlockUnbreakable();
		setResistance(Float.MAX_VALUE);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiExporter((TileEntityExporter) worldIn.getTileEntity(pos)));
		}

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
