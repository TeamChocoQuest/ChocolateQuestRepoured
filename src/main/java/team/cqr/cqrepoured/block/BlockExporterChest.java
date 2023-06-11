package team.cqr.cqrepoured.block;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;

public abstract class BlockExporterChest extends ChestBlock {

	public BlockExporterChest() {
		super(Properties.of(Material.WOOD)
				.sound(SoundType.WOOD)
				.strength(-1.0F, 3600000.0F)
				.noDrops()
				.noOcclusion(),
				() -> BlockEntityType.CHEST);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public abstract BlockEntity createTileEntity(BlockState state, BlockGetter world);

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult result) {
		return InteractionResult.PASS;
	}

	@Override
	public boolean is(Block block) {
		return block.getClass() == ChestBlock.class || block instanceof BlockExporterChest;
	}

}
