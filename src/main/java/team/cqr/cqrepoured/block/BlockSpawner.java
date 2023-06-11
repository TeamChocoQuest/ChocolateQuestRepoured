package team.cqr.cqrepoured.block;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import team.cqr.cqrepoured.inventory.ContainerSpawner;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;

public class BlockSpawner extends Block {

	private static final ITextComponent CONTAINER_TITLE = new TranslationTextComponent("container.spawner");

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
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntitySpawner();
	}

	@Override
	public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
		if (!pPlayer.isCreative()) {
			return ActionResultType.PASS;
		}
		if (!pLevel.isClientSide) {
			NetworkHooks.openGui((ServerPlayerEntity) pPlayer, this.getMenuProvider(pState, pLevel, pPos), pPos);
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	@Nullable
	public INamedContainerProvider getMenuProvider(BlockState pState, World pLevel, BlockPos pPos) {
		TileEntity tileEntity = pLevel.getBlockEntity(pPos);
		if (!(tileEntity instanceof TileEntitySpawner)) {
			return null;
		}
		return new SimpleNamedContainerProvider((id, playerInv, player) -> {
			return new ContainerSpawner(id, playerInv, ((TileEntitySpawner) tileEntity));
		}, CONTAINER_TITLE);
	}

	// TODO drop inventory when block gets broken? I find it rather annoying...

}
