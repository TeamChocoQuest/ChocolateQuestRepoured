package com.example.chocolatequest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import com.example.chocolatequest.block.entity.TileEntityExporterChestCustom;
import com.example.chocolatequest.registry.ModBlockEntities;

public class BlockExporterChestCustom extends BlockExporterChest {

	public BlockExporterChestCustom(Properties properties) {
		super(properties, () -> ModBlockEntities.EXPORTER_CHEST_CUSTOM.get());
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new TileEntityExporterChestCustom(pPos, pState);
	}

}
