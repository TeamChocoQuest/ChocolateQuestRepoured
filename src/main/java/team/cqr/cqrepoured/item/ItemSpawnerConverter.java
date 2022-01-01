package team.cqr.cqrepoured.item;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.util.SpawnerFactory;

public class ItemSpawnerConverter extends Item {

	public ItemSpawnerConverter() {
		this.setMaxStackSize(1);
	}

	@Override
	public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, PlayerEntity player) {
		Block block = world.getBlockState(pos).getBlock();
		return block != CQRBlocks.SPAWNER && block != Blocks.MOB_SPAWNER;
	}

	@Override
	public ActionResultType onItemUseFirst(PlayerEntity player, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, Hand hand) {
		if (player.isCreative()) {
			Block block = world.getBlockState(pos).getBlock();
			if (block == CQRBlocks.SPAWNER || block == Blocks.MOB_SPAWNER) {
				if (!world.isRemote) {
					if (block == CQRBlocks.SPAWNER) {
						CQRMain.logger.info("Converting: CQR -> Vanilla");
						SpawnerFactory.convertCQSpawnerToVanillaSpawner(world, pos, null);
					}
					if (block == Blocks.MOB_SPAWNER) {
						CQRMain.logger.info("Converting: Vanilla -> CQR");
						SpawnerFactory.convertVanillaSpawnerToCQSpawner(world, pos);
					}
					player.getCooldownTracker().setCooldown(this, 10);
				}
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.FAIL;
	}

}
