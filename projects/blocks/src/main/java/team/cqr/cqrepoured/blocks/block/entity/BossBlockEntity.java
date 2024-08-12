package team.cqr.cqrepoured.blocks.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import team.cqr.cqrepoured.blocks.init.CQRBlocksBlockEntityTypes;

public class BossBlockEntity extends BaseContainerBlockEntity {

	public BossBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(CQRBlocksBlockEntityTypes.BOSS_BLOCK.get(), pPos, pBlockState);
	}
	
	public static void onTick(Level level, BlockPos pos, BlockState state, BossBlockEntity blockEntity) {
		
	}

	@Override
	public int getContainerSize() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack getItem(int pSlot) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack removeItem(int pSlot, int pAmount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack removeItemNoUpdate(int pSlot) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setItem(int pSlot, ItemStack pStack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clearContent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Component getDefaultName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
		// TODO Auto-generated method stub
		return null;
	}

}
