package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.RegistryObject;
import team.cqr.cqrepoured.block.BlockExporterChest;
import team.cqr.cqrepoured.block.BlockExporterChestFixed;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChest;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChestCustom;
import team.cqr.cqrepoured.util.ByteBufUtil;
import team.cqr.cqrepoured.world.structure.generation.WorldDungeonGenerator;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.ICQRLevel;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.IFactory;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.world.structure.generation.structurefile.BlockStatePalette;

public class PreparableLootChestInfo extends PreparablePosInfo {

	private final ResourceLocation lootTable;
	private final Direction facing;

	public PreparableLootChestInfo(ResourceLocation lootTable, Direction facing) {
		this.lootTable = lootTable;
		this.facing = facing;
	}

	@Override
	protected void prepareNormal(ICQRLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);
		BlockState transformedState = placement.transform(Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, this.facing));

		level.setBlockState(transformedPos, transformedState, blockEntity -> {
			if (blockEntity instanceof ChestTileEntity) {
				long seed = WorldDungeonGenerator.getSeed(level.getSeed(), pos.getX(), pos.getZ());
				((ChestTileEntity) blockEntity).setLootTable(this.lootTable, seed);
			}
		});
	}

	@Override
	protected void prepareDebug(ICQRLevel level, BlockPos pos, DungeonPlacement placement) {
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
		public PreparablePosInfo create(World world, BlockPos pos, BlockState state, LazyOptional<TileEntityExporterChest> blockEntityLazy) {
			return new PreparableLootChestInfo(blockEntityLazy.orElseThrow(NullPointerException::new).getLootTable(), state.getValue(ChestBlock.FACING));
		}

	}

	public static class Serializer implements ISerializer<PreparableLootChestInfo> {

		@Override
		public void write(PreparableLootChestInfo preparable, ByteBuf buf, BlockStatePalette palette, ListNBT nbtList) {
			ByteBufUtil.writeUTF8String(buf, preparable.lootTable.toString());
			buf.writeByte(preparable.facing.get2DDataValue());
		}

		@Override
		public PreparableLootChestInfo read(ByteBuf buf, BlockStatePalette palette, ListNBT nbtList) {
			ResourceLocation lootTable = new ResourceLocation(ByteBufUtil.readUTF8String(buf));
			Direction facing = Direction.from2DDataValue(buf.readByte());
			return new PreparableLootChestInfo(lootTable, facing);
		}

	}

}
