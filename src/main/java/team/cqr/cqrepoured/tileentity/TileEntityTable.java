package team.cqr.cqrepoured.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.init.CQRBlockEntities;
import team.cqr.cqrepoured.init.CQRContainerTypes;
import team.cqr.cqrepoured.inventory.InventoryBlockEntity;
import team.cqr.cqrepoured.network.datasync.DataEntryByte;
import team.cqr.cqrepoured.network.datasync.DataEntryInventory;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;

public class TileEntityTable extends RandomizableContainerBlockEntity implements ITileEntitySyncable<TileEntityTable> {

	private final TileEntityDataManager dataManager = new TileEntityDataManager(this);

	private InventoryBlockEntity items = new InventoryBlockEntity(this, 1, false);
	private final DataEntryInventory dataEntryInventory = new DataEntryInventory("inventory", this.items, false);
	private final DataEntryByte rotation = new DataEntryByte("rotation", (byte) 0, false);

	public TileEntityTable(BlockPos pos, BlockState state) {
		super(CQRBlockEntities.TABLE.get(), pos, state);
		this.dataManager.register(this.dataEntryInventory);
		this.dataManager.register(this.rotation);
	}

	@Override
	public TileEntityDataManager getDataManager() {
		return this.dataManager;
	}

	@Override
	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
		this.dataManager.write(pTag);
	}
	
	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		this.dataManager.read(pTag);
	}
	
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this, (be) -> {
			return ((TileEntityTable)be).dataManager.write(new CompoundTag());
		});
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = new CompoundTag();
		return this.dataManager.write(tag);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		this.dataManager.read(pkt.getTag());
	}

	/* Gone?
	@Override
	public ITextComponent getDisplayName() {
		ItemStack stack = this.inventory.get().getStackInSlot(0);
		return stack.hasDisplayName() ? new TextComponentString(stack.getDisplayName()) : null;
	}*/

	@Override
	@OnlyIn(Dist.CLIENT)
	public AABB getRenderBoundingBox() {
		return new AABB(this.getBlockPos(), this.getBlockPos().offset(1, 2, 1));
	}

	/*Not needed anymore
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}*/

	public void setRotation(int rotation) {
		rotation = rotation % 16;
		if (rotation < 0) {
			rotation += 16;
		}

		this.rotation.set((byte) rotation);
	}

	public int getRotation() {
		return this.rotation.getByte();
	}

	public float getRotationInDegree() {
		return this.rotation.getByte() * 22.5F;
	}

	@Override
	public int getContainerSize() {
		return 1;
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return NonNullList.<ItemStack>of(ItemStack.EMPTY, this.getItem(0));
	}

	@Override
	protected void setItems(NonNullList<ItemStack> pItemStacks) {
		this.setItem(0, pItemStacks.get(0));
	}

	@Override
	protected Component getDefaultName() {
		return null;
	}

}
