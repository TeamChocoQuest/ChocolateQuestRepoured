package com.example.chocolatequest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import com.example.chocolatequest.registry.ModBlockEntities;
import net.minecraft.core.HolderLookup;

public class TileEntityExporterChestCustom extends TileEntityExporterChest {

	private ResourceLocation lootTable = BuiltInLootTables.EMPTY.location();

	public TileEntityExporterChestCustom(BlockPos pos, BlockState state) {
		super(ModBlockEntities.EXPORTER_CHEST_CUSTOM.get(), pos, state);
	}

	public void setLootTable(ResourceLocation lootTable) {
		this.lootTable = lootTable;
		this.setChanged();
	}

	@Override
	public ResourceLocation getLootTableCQR() {
		return this.lootTable;
	}

	@Override
	protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
		super.saveAdditional(pTag, provider);
		if (this.lootTable != null) {
			pTag.putString("cqLootTable", this.lootTable.toString());
		}
	}

	@Override
	public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
		super.loadAdditional(pTag, provider);
		if (pTag.contains("cqLootTable")) {
			this.lootTable = ResourceLocation.parse(pTag.getString("cqLootTable"));
		}
	}

}
