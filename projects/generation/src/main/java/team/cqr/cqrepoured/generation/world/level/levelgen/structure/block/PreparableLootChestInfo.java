package team.cqr.cqrepoured.generation.world.level.levelgen.structure.block;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
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
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.StructureLevel;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonPlacement;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.WorldDungeonGenerator;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.PreparablePosInfo.Registry.IFactory;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChest;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChestCustom;
import team.cqr.cqrepoured.util.ByteBufUtil;

public class PreparableLootChestInfo extends PreparablePosInfo {

	private final ResourceLocation lootTable;
	private final Direction facing;

	public PreparableLootChestInfo(ResourceLocation lootTable, Direction facing) {
		this.lootTable = lootTable;
		this.facing = facing;
	}

	@Override
	protected void prepareNormal(StructureLevel level, BlockPos pos, DungeonPlacement placement) {
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
	protected void prepareDebug(StructureLevel level, BlockPos pos, DungeonPlacement placement) {
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

	public static class Factory implements IFactory<TileEntityExporterChest> {

		@Override
		public PreparablePosInfo create(Level world, BlockPos pos, BlockState state, LazyOptional<TileEntityExporterChest> blockEntityLazy) {
			return new PreparableLootChestInfo(blockEntityLazy.orElseThrow(NullPointerException::new).getLootTable(), state.getValue(ChestBlock.FACING));
		}

	}

	public static class Serializer implements ISerializer<PreparableLootChestInfo> {

		@Override
		public void write(PreparableLootChestInfo preparable, ByteBuf buf, SimplePalette palette, ListTag nbtList) {
			ByteBufUtil.writeUTF8String(buf, preparable.lootTable.toString());
			buf.writeByte(preparable.facing.get2DDataValue());
		}

		@Override
		public PreparableLootChestInfo read(ByteBuf buf, SimplePalette palette, ListTag nbtList) {
			ResourceLocation lootTable = new ResourceLocation(ByteBufUtil.readUTF8String(buf));
			Direction facing = Direction.from2DDataValue(buf.readByte());
			return new PreparableLootChestInfo(lootTable, facing);
		}

	}

}
