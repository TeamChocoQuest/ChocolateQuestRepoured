package team.cqr.cqrepoured.block;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChestCustom;

public class BlockExporterChestCustom extends BlockExporterChest {

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new TileEntityExporterChestCustom();
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult result) {
		if (!player.isCreative()) {
			return InteractionResult.PASS;
		}
		BlockEntity tileEntity = level.getBlockEntity(pos);
		if (!(tileEntity instanceof TileEntityExporterChestCustom)) {
			return InteractionResult.FAIL;
		}
		if (level.isClientSide) {
			this.openScreen((TileEntityExporterChestCustom) tileEntity);
		}
		return InteractionResult.SUCCESS;
	}

	@OnlyIn(Dist.CLIENT)
	private void openScreen(TileEntityExporterChestCustom tileEntity) {
		// Minecraft.getInstance().setScreen(new GuiExporterChestCustom(tileEntity));
	}

}
