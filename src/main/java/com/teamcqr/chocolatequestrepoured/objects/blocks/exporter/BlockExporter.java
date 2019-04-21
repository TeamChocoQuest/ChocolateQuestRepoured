package com.teamcqr.chocolatequestrepoured.objects.blocks.exporter;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.base.BlockBase;
import com.teamcqr.chocolatequestrepoured.objects.gui.GUIExporter;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockExporter extends BlockBase {

	public BlockExporter(String name, Material material) {
		super(name, material);
		setBlockUnbreakable();
		setCreativeTab(CQRMain.CQRExporterChestTab);
		setSoundType(SoundType.ANVIL);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		System.out.println("INTERACT!!!!!");
		playerIn.openGui(CQRMain.INSTANCE, GUIExporter.GUIID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

}
