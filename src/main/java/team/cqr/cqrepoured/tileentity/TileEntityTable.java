package team.cqr.cqrepoured.tileentity;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import team.cqr.cqrepoured.network.datasync.DataEntryByte;
import team.cqr.cqrepoured.network.datasync.DataEntryItemStackHandler;
import team.cqr.cqrepoured.network.datasync.DataEntryItemStackHandler.CustomItemStackHandler;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;

public class TileEntityTable extends TileEntity implements ITileEntitySyncable {

	private final TileEntityDataManager dataManager = new TileEntityDataManager(this);

	private final DataEntryItemStackHandler inventory = new DataEntryItemStackHandler("inventory", new CustomItemStackHandler(1) {
		@Override
		public int getSlotLimit(int slot) {
			return 1;
		}
	}, false);
	private final DataEntryByte rotation = new DataEntryByte("rotation", (byte) 0, false);

	public TileEntityTable() {
		this.dataManager.register(this.inventory);
		this.dataManager.register(this.rotation);
	}

	@Override
	public TileEntityDataManager getDataManager() {
		return this.dataManager;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) this.inventory.get() : super.getCapability(capability, facing);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		this.dataManager.write(compound);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.dataManager.read(compound);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.dataManager.write(new NBTTagCompound()));
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.dataManager.read(pkt.getNbtCompound());
	}

	@Override
	public ITextComponent getDisplayName() {
		ItemStack stack = this.inventory.get().getStackInSlot(0);
		return stack.hasDisplayName() ? new TextComponentString(stack.getDisplayName()) : null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(this.getPos(), this.getPos().add(1, 2, 1));
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}

	public void setRotation(int rotation) {
		rotation = rotation % 16;
		if (rotation < 0) {
			rotation += 16;
		}

		this.rotation.set((byte) rotation);
	}

	public ItemStackHandler getInventory() {
		return this.inventory.get();
	}

	public int getRotation() {
		return this.rotation.getByte();
	}

	public float getRotationInDegree() {
		return this.rotation.getByte() * 22.5F;
	}

}
