package team.cqr.cqrepoured.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChest;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChestCustom;
import team.cqr.cqrepoured.util.GuiHandler;

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
	public TileEntityExporterChest createTileEntity(World world, IBlockState state) {
		return new TileEntityExporterChestCustom();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX,
			float hitY, float hitZ) {
		if (worldIn.isRemote) {
			playerIn.openGui(CQRMain.INSTANCE, GuiHandler.EXPORTER_CHEST_GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

}
