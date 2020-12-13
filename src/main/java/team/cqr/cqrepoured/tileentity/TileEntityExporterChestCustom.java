package team.cqr.cqrepoured.tileentity;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import team.cqr.cqrepoured.network.datasync.DataEntryResourceLocation;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;

public class TileEntityExporterChestCustom extends TileEntityExporterChest implements ITileEntitySyncable {

	private final TileEntityDataManager dataManager = new TileEntityDataManager(this);

	private final DataEntryResourceLocation lootTable = new DataEntryResourceLocation("lootTable", LootTableList.EMPTY, true);

	public TileEntityExporterChestCustom() {
		this.dataManager.register(this.lootTable);
	}

	@Override
	public TileEntityDataManager getDataManager() {
		return this.dataManager;
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
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}

	public void setLootTable(ResourceLocation lootTable) {
		this.lootTable.set(lootTable);
	}

	public ResourceLocation getLootTable() {
		return this.lootTable.get();
	}

	@Override
	@Nullable
	public ITextComponent getDisplayName() {
		return new TextComponentString("Loottable: " + this.lootTable.get().toString());
	}

}
