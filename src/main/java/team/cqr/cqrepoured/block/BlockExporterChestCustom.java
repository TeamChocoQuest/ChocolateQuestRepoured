package team.cqr.cqrepoured.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChestCustom;

public class BlockExporterChestCustom extends BlockExporterChest {

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityExporterChestCustom();
	}

	@Override
	public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult result) {
		if (!player.isCreative()) {
			return ActionResultType.PASS;
		}
		TileEntity tileEntity = level.getBlockEntity(pos);
		if (!(tileEntity instanceof TileEntityExporterChestCustom)) {
			return ActionResultType.FAIL;
		}
		if (level.isClientSide) {
			this.openScreen((TileEntityExporterChestCustom) tileEntity);
		}
		return ActionResultType.SUCCESS;
	}

	@OnlyIn(Dist.CLIENT)
	private void openScreen(TileEntityExporterChestCustom tileEntity) {
		// Minecraft.getInstance().setScreen(new GuiExporterChestCustom(tileEntity));
	}

}
