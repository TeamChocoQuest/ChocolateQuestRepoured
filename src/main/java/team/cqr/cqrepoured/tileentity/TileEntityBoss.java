package team.cqr.cqrepoured.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import team.cqr.cqrepoured.init.CQRBlockEntities;
import team.cqr.cqrepoured.init.CQRContainerTypes;

public class TileEntityBoss extends BlockEntityContainer {

	public TileEntityBoss(BlockPos pos, BlockState state) {
		super(CQRBlockEntities.BOSS.get(), 1, 1, pos, state);
	}

	@Override
	protected Component getDefaultName() {
		return null;
	}

	@Override
	protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
		return CQRContainerTypes.BOSS_BLOCK.get().create(pContainerId, pInventory);
	}

}
