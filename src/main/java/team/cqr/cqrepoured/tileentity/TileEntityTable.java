package team.cqr.cqrepoured.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.init.CQRBlockEntities;
import team.cqr.cqrepoured.network.datasync.DataEntryByte;
import team.cqr.cqrepoured.network.datasync.DataEntryInventory;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;

public class TileEntityTable extends BlockEntityContainer implements ITileEntitySyncable {

	private final TileEntityDataManager dataManager = new TileEntityDataManager(this);

	private final DataEntryInventory dataEntryInventory = new DataEntryInventory("inventory", this.inventory, false);
	private final DataEntryByte rotation = new DataEntryByte("rotation", (byte) 0, false);

	public TileEntityTable() {
		super(CQRBlockEntities.TABLE.get(), 1, 1);
		this.dataManager.register(this.dataEntryInventory);
		this.dataManager.register(this.rotation);
	}

	@Override
	public TileEntityDataManager getDataManager() {
		return this.dataManager;
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		this.dataManager.write(compound);
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.dataManager.read(compound);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.worldPosition, 0, this.dataManager.write(new CompoundNBT()));
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.save(new CompoundNBT());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
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
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(this.getBlockPos(), this.getBlockPos().offset(1, 2, 1));
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

}
