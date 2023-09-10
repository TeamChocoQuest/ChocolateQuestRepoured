package team.cqr.cqrepoured.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import team.cqr.cqrepoured.init.CQRBlockEntities;
import team.cqr.cqrepoured.network.datasync.DataEntryResourceLocation;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;

public class TileEntityExporterChestCustom extends TileEntityExporterChest implements ITileEntitySyncable<TileEntityExporterChestCustom> {

	private final TileEntityDataManager dataManager = new TileEntityDataManager(this);

	private final DataEntryResourceLocation lootTable = new DataEntryResourceLocation("lootTable", BuiltInLootTables.EMPTY, true);

	public TileEntityExporterChestCustom(BlockPos pos, BlockState state) {
		super(CQRBlockEntities.EXPORTER_CHEST_CUSTOM.get(), pos, state);
		this.dataManager.register(this.lootTable);
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
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.saveWithId();
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
