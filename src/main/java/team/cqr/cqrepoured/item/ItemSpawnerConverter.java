package team.cqr.cqrepoured.item;

import net.minecraft.core.BlockPos;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;
import net.minecraft.world.level.block.state.BlockState;
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
	public boolean canAttackBlock(BlockState state, World level, BlockPos pos, PlayerEntity player) {
		Block block = level.getBlockState(pos).getBlock();
		return block != CQRBlocks.SPAWNER.get() && block != Blocks.SPAWNER;
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
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
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.FAIL;
	}

}
