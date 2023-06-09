package team.cqr.cqrepoured.block;

import javax.annotation.Nullable;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.network.NetworkHooks;
import team.cqr.cqrepoured.inventory.ContainerSpawner;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;

public class BlockSpawner extends Block {

	private static final TextComponent CONTAINER_TITLE = new TranslationTextComponent("container.spawner");

	public BlockSpawner() {
		super(Properties.of(Material.STONE)
				.sound(SoundType.METAL)
				.strength(-1.0F, 3600000.0F)
				.noDrops()
				.noOcclusion());
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new TileEntitySpawner();
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (!pPlayer.isCreative()) {
			return InteractionResult.PASS;
		}
		if (!pLevel.isClientSide) {
			NetworkHooks.openGui((ServerPlayer) pPlayer, this.getMenuProvider(pState, pLevel, pPos), pPos);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	@Nullable
	public INamedContainerProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
		BlockEntity tileEntity = pLevel.getBlockEntity(pPos);
		if (!(tileEntity instanceof TileEntitySpawner)) {
			return null;
		}
		return new SimpleNamedContainerProvider((id, playerInv, player) -> {
			return new ContainerSpawner(id, playerInv, ((TileEntitySpawner) tileEntity));
		}, CONTAINER_TITLE);
	}

	// TODO drop inventory when block gets broken? I find it rather annoying...

}
