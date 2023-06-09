package team.cqr.cqrepoured.block;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.Component;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.client.gui.ScreenExporter;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;

public class BlockExporter extends Block {

	private static final Component SCREEN_TITLE = new TranslationTextComponent("tile.exporter.name");

	public BlockExporter() {
		super(Properties.of(Material.STONE)
				.strength(-1.0F, 3600000.0F)
				.noDrops()
				.isValidSpawn(CQRBlocks::never));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new TileEntityExporter();
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (!pPlayer.isCreative()) {
			return InteractionResult.PASS;
		}
		BlockEntity tileEntity = pLevel.getBlockEntity(pPos);
		if (!(tileEntity instanceof TileEntityExporter)) {
			return InteractionResult.FAIL;
		}
		if (pLevel.isClientSide) {
			this.openScreen((TileEntityExporter) tileEntity);
		}
		return InteractionResult.SUCCESS;
	}

	@OnlyIn(Dist.CLIENT)
	private void openScreen(TileEntityExporter tileEntity) {
		Minecraft.getInstance().setScreen(new ScreenExporter(SCREEN_TITLE, tileEntity));
	}

}
