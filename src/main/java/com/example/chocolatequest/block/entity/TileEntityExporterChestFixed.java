package com.example.chocolatequest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import com.example.chocolatequest.block.BlockExporterChestFixed;
import com.example.chocolatequest.registry.ModBlockEntities;

public class TileEntityExporterChestFixed extends TileEntityExporterChest {

	public TileEntityExporterChestFixed(BlockPos pos, BlockState state) {
		super(ModBlockEntities.EXPORTER_CHEST_FIXED.get(), pos, state);
	}

	@Override
	public ResourceLocation getLootTableCQR() {
		Block block = this.getBlockState().getBlock();
		if (block instanceof BlockExporterChestFixed) {
			return ((BlockExporterChestFixed) block).getLootTableCQR();
		}
		return BuiltInLootTables.EMPTY.location();
	}

}
