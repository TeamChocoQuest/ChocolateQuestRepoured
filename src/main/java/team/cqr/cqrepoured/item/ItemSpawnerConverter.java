package team.cqr.cqrepoured.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.util.SpawnerFactory;

public class ItemSpawnerConverter extends Item {

	public ItemSpawnerConverter(Properties properties)
	{
		super(properties.stacksTo(1));
		//this.setMaxStackSize(1);
	}

	@Override
	public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
		Block block = level.getBlockState(pos).getBlock();
		return block != CQRBlocks.SPAWNER.get() && block != Blocks.SPAWNER;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (context.getPlayer().isCreative()) {
			Block block = context.getLevel().getBlockState(context.getClickedPos()).getBlock();
			if (block == CQRBlocks.SPAWNER.get() || block == Blocks.SPAWNER) {
				if (!context.getLevel().isClientSide) {
					if (block == CQRBlocks.SPAWNER.get()) {
						CQRMain.logger.info("Converting: CQR -> Vanilla");
						SpawnerFactory.convertCQSpawnerToVanillaSpawner(context.getLevel(), context.getClickedPos(), null);
					}
					if (block == Blocks.SPAWNER) {
						CQRMain.logger.info("Converting: Vanilla -> CQR");
						SpawnerFactory.convertVanillaSpawnerToCQSpawner(context.getLevel(), context.getClickedPos());
					}
					context.getPlayer().getCooldowns().addCooldown(this, 10);
				}
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.FAIL;
	}

}
