package com.example.chocolatequest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import com.example.chocolatequest.block.entity.TileEntityExporterChestFixed;
import com.example.chocolatequest.registry.ModBlockEntities;

public class BlockExporterChestFixed extends BlockExporterChest {

	private final ResourceLocation lootTable;

	public BlockExporterChestFixed(Properties properties, ResourceLocation lootTable) {
		super(properties, () -> ModBlockEntities.EXPORTER_CHEST_FIXED.get());
		this.lootTable = lootTable;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new TileEntityExporterChestFixed(pPos, pState);
	}

	public ResourceLocation getLootTableCQR() {
		return lootTable;
	}

}
