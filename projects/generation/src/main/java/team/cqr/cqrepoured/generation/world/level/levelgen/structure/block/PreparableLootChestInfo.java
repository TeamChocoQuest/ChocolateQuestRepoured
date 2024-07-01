package team.cqr.cqrepoured.generation.world.level.levelgen.structure.block;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.SimplePalette;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.block.BlockExporterChest;
import team.cqr.cqrepoured.block.BlockExporterChestFixed;
import team.cqr.cqrepoured.generation.init.CQRBlocks;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonPlacement;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.StructureLevel;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.WorldDungeonGenerator;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChest;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChestCustom;

public class PreparableLootChestInfo implements IBlockInfo {

	private final ResourceLocation lootTable;
	private final Direction facing;

	public PreparableLootChestInfo(ResourceLocation lootTable, Direction facing) {
		this.lootTable = lootTable;
		this.facing = facing;
	}

	@Override
	public void prepare(StructureLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);
		BlockState transformedState = placement.transform(Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, this.facing));

		level.setBlockState(transformedPos, transformedState, blockEntity -> {
			if (blockEntity instanceof ChestBlockEntity) {
				long seed = WorldDungeonGenerator.getSeed(level.getSeed(), pos.getX(), pos.getZ());
				((ChestBlockEntity) blockEntity).setLootTable(this.lootTable, seed);
			}
		});
	}

	@Override
	public void prepareNoProcessing(StructureLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);
		BlockExporterChest block = CQRBlocks.BLOCKS.getEntries().stream()
				.map(RegistryObject::get)
				.filter(BlockExporterChest.class::isInstance)
				.map(BlockExporterChest.class::cast)
				.filter(BlockExporterChestFixed.class::isInstance)
				.filter(b -> ((BlockExporterChestFixed) b).getLootTable().equals(this.lootTable))
				.findFirst()
				.orElse(CQRBlocks.EXPORTER_CHEST_CUSTOM.get());
		BlockState transformedState = placement.transform(block.defaultBlockState().setValue(ChestBlock.FACING, this.facing));

		level.setBlockState(transformedPos, transformedState, blockEntity -> {
			if (blockEntity instanceof TileEntityExporterChestCustom) {
				((TileEntityExporterChestCustom) blockEntity).setLootTable(this.lootTable);
			}
		});
	}

	public ResourceLocation getLootTable() {
		return this.lootTable;
	}

	public Direction getFacing() {
		return this.facing;
	}

	public static class Factory implements IBlockInfoFactory<TileEntityExporterChest> {

		@Override
		public IBlockInfo create(Level world, BlockPos pos, BlockState state, LazyOptional<TileEntityExporterChest> blockEntitySupplier) {
			return new PreparableLootChestInfo(blockEntitySupplier.orElseThrow(NullPointerException::new).getLootTable(), state.getValue(ChestBlock.FACING));
		}

	}

	public static class Serializer implements IBlockInfoSerializer<PreparableLootChestInfo> {

		@Override
		public void write(PreparableLootChestInfo lootChestInfo, DataOutput out, SimplePalette palette) throws IOException {
			out.writeUTF(lootChestInfo.lootTable.toString());
			out.writeByte(lootChestInfo.facing.get2DDataValue());
		}

		@Override
		public PreparableLootChestInfo read(DataInput in, SimplePalette palette) throws IOException {
			ResourceLocation lootTable = new ResourceLocation(in.readUTF());
			Direction facing = Direction.from2DDataValue(in.readByte());
			return new PreparableLootChestInfo(lootTable, facing);
		}

	}

}
