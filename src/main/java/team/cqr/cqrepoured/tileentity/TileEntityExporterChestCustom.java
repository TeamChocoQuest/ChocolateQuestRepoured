package team.cqr.cqrepoured.tileentity;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTables;
import team.cqr.cqrepoured.network.datasync.DataEntryResourceLocation;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;

public class TileEntityExporterChestCustom extends TileEntityExporterChest implements ITileEntitySyncable {

	private final TileEntityDataManager dataManager = new TileEntityDataManager(this);

	private final DataEntryResourceLocation lootTable = new DataEntryResourceLocation("lootTable", LootTables.EMPTY, true);

	public TileEntityExporterChestCustom() {
		this.dataManager.register(this.lootTable);
	}

	@Override
	public TileEntityDataManager getDataManager() {
		return this.dataManager;
	}

	@Override
	public CompoundNBT writeToNBT(CompoundNBT compound) {
		super.writeToNBT(compound);
		this.dataManager.write(compound);
		return compound;
	}

	@Override
	public void readFromNBT(CompoundNBT compound) {
		super.readFromNBT(compound);
		this.dataManager.read(compound);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 0, this.dataManager.write(new CompoundNBT()));
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.writeToNBT(new CompoundNBT());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.dataManager.read(pkt.getNbtCompound());
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}

	public void setLootTable(ResourceLocation lootTable) {
		this.lootTable.set(lootTable);
	}

	@Override
	public ResourceLocation getLootTable() {
		return this.lootTable.get();
	}

	@Override
	@Nullable
	public ITextComponent getDisplayName() {
		return new StringTextComponent("Loottable: " + this.lootTable.get().toString());
	}

}
