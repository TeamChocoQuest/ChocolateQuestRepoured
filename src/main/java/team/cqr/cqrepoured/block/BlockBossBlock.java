package team.cqr.cqrepoured.block;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import team.cqr.cqrepoured.inventory.ContainerBossBlock;
import team.cqr.cqrepoured.tileentity.BlockEntityContainer;
import team.cqr.cqrepoured.tileentity.TileEntityBoss;

import javax.annotation.Nullable;

public class BlockBossBlock extends Block {

	private static final TextComponent CONTAINER_TITLE = new TranslationTextComponent("container.boss_block");

	public BlockBossBlock() {
		super(Properties.of(Material.STONE)
				.noDrops()
				.strength(-1.0F, 3600000.0F)
				.sound(SoundType.METAL)
				.noOcclusion());
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new TileEntityBoss();
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (!pPlayer.isCreative()) {
			return InteractionResult.PASS;
		}
		if (!pLevel.isClientSide) {
			pPlayer.openMenu(this.getMenuProvider(pState, pLevel, pPos));
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	@Nullable
	public INamedContainerProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
		BlockEntity tileEntity = pLevel.getBlockEntity(pPos);
		if (!(tileEntity instanceof BlockEntityContainer)) {
			return null;
		}
		return new SimpleNamedContainerProvider((id, playerInv, player) -> {
			return new ContainerBossBlock(id, playerInv, ((BlockEntityContainer) tileEntity).getInventory());
		}, CONTAINER_TITLE);
	}

}
