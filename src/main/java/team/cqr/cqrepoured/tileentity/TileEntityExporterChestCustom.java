package team.cqr.cqrepoured.tileentity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.init.CQRBlockEntities;
import team.cqr.cqrepoured.network.datasync.DataEntryResourceLocation;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;

public class TileEntityExporterChestCustom extends TileEntityExporterChest implements ITileEntitySyncable {

	private final TileEntityDataManager dataManager = new TileEntityDataManager(this);

	private final DataEntryResourceLocation lootTable = new DataEntryResourceLocation("lootTable", LootTables.EMPTY, true);

	public TileEntityExporterChestCustom() {
		super(CQRBlockEntities.EXPORTER_CHEST_CUSTOM.get());
		this.dataManager.register(this.lootTable);
	}

	@Override
	public TileEntityDataManager getDataManager() {
		return this.dataManager;
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
		this.dataManager.write(compound);
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundTag compound) {
		super.load(state, compound);
		this.dataManager.read(compound);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return new ClientboundBlockEntityDataPacket(this.worldPosition, 0, this.dataManager.write(new CompoundTag()));
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.save(new CompoundTag());
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		this.dataManager.read(pkt.getTag());
	}

	public void setLootTable(ResourceLocation lootTable) {
		this.lootTable.set(lootTable);
	}

	@Override
	public ResourceLocation getLootTable() {
		return this.lootTable.get();
	}

}
