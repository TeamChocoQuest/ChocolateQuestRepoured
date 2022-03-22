package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.block.BlockExporterChest;
import team.cqr.cqrepoured.block.BlockExporterChestCQR;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChest;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChestCustom;
import team.cqr.cqrepoured.world.structure.generation.WorldDungeonGenerator;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.GeneratableBlockInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.GeneratablePosInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.IFactory;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.world.structure.generation.structurefile.BlockStatePalette;

import java.util.function.Supplier;

public class PreparableLootChestInfo extends PreparablePosInfo {

	private final ResourceLocation lootTable;
	private final Direction facing;

	public PreparableLootChestInfo(BlockPos pos, ResourceLocation lootTable, Direction facing) {
		this(pos.getX(), pos.getY(), pos.getZ(), lootTable, facing);
	}

	public PreparableLootChestInfo(int x, int y, int z, ResourceLocation lootTable, Direction facing) {
		super(x, y, z);
		this.lootTable = lootTable;
		this.facing = facing;
	}

	@Override
	protected GeneratablePosInfo prepare(World world, DungeonPlacement placement, BlockPos pos) {
		BlockState state = Blocks.CHEST.getDefaultState().withProperty(ChestBlock.FACING, this.facing);
		state = state.withMirror(placement.getMirror()).withRotation(placement.getRotation());
		TileEntity tileEntity = state.getBlock().createTileEntity(world, state);

		if (tileEntity instanceof ChestTileEntity) {
			long seed = WorldDungeonGenerator.getSeed(world, pos.getX(), pos.getZ());
			((ChestTileEntity) tileEntity).setLootTable(this.lootTable, seed);
		}

		return new GeneratableBlockInfo(pos, state, tileEntity);
	}

	@Override
	protected GeneratablePosInfo prepareDebug(World world, DungeonPlacement placement, BlockPos pos) {
		BlockExporterChest block = BlockExporterChest.getExporterChests().stream().filter(BlockExporterChestCQR.class::isInstance).filter(b -> ((BlockExporterChestCQR) b).getLootTable().equals(this.lootTable)).findFirst().orElse(CQRBlocks.EXPORTER_CHEST_CUSTOM);
		BlockState state = block.getDefaultState().withProperty(HorizontalBlock.FACING, this.facing);
		state = state.withMirror(placement.getMirror()).withRotation(placement.getRotation());

		if (block != CQRBlocks.EXPORTER_CHEST_CUSTOM) {
			return new GeneratableBlockInfo(pos, state, null);
		}

		TileEntityExporterChestCustom tileEntity = new TileEntityExporterChestCustom();
		tileEntity.setLootTable(this.lootTable);
		return new GeneratableBlockInfo(pos, state, tileEntity);
	}

	public ResourceLocation getLootTable() {
		return this.lootTable;
	}

	public Direction getFacing() {
		return this.facing;
	}

	public static class Factory implements IFactory<TileEntityExporterChest> {

		@Override
		public PreparablePosInfo create(World world, int x, int y, int z, BlockState state, Supplier<TileEntityExporterChest> tileEntitySupplier) {
			return new PreparableLootChestInfo(x, y, z, tileEntitySupplier.get().getLootTable(), state.getValue(HorizontalBlock.FACING));
		}

	}

	public static class Serializer implements ISerializer<PreparableLootChestInfo> {

		@Override
		public void write(PreparableLootChestInfo preparable, ByteBuf buf, BlockStatePalette palette, ListNBT nbtList) {
			ByteBufUtils.writeUTF8String(buf, preparable.lootTable.toString());
			buf.writeByte(preparable.facing.getHorizontalIndex());
		}

		@Override
		public PreparableLootChestInfo read(int x, int y, int z, ByteBuf buf, BlockStatePalette palette, ListNBT nbtList) {
			ResourceLocation lootTable = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
			Direction facing = Direction.byHorizontalIndex(buf.readByte());
			return new PreparableLootChestInfo(x, y, z, lootTable, facing);
		}

		@Override
		@Deprecated
		public PreparableLootChestInfo read(int x, int y, int z, IntArrayNBT nbtIntArray, BlockStatePalette palette, ListNBT nbtList) {
			int[] intArray = nbtIntArray.getIntArray();
			ResourceLocation lootTable = getLootTableFromId(intArray[1]);
			Direction facing = Direction.byHorizontalIndex(intArray[2]);
			return new PreparableLootChestInfo(x, y, z, lootTable, facing);
		}

		@Deprecated
		private static ResourceLocation getLootTableFromId(int id) {
			switch (id) {
			case 0:
				return CQRLoottables.CHESTS_FOOD;
			case 1:
				return CQRLoottables.CHESTS_EQUIPMENT;
			case 2:
				return CQRLoottables.CHESTS_TREASURE;
			case 3:
				return CQRLoottables.CHESTS_MATERIAL;
			case 32:
				return CQRLoottables.CHESTS_CLUTTER;
			case 4:
				return LootTables.CHESTS_ABANDONED_MINESHAFT;
			case 5:
				return LootTables.CHESTS_DESERT_PYRAMID;
			case 6:
				return LootTables.CHESTS_END_CITY_TREASURE;
			case 7:
				return LootTables.CHESTS_IGLOO_CHEST;
			case 8:
				return LootTables.CHESTS_JUNGLE_TEMPLE;
			case 9:
				return LootTables.CHESTS_JUNGLE_TEMPLE_DISPENSER;
			case 10:
				return LootTables.CHESTS_NETHER_BRIDGE;
			case 11:
				return LootTables.CHESTS_SPAWN_BONUS_CHEST;
			case 12:
				return LootTables.CHESTS_STRONGHOLD_CORRIDOR;
			case 13:
				return LootTables.CHESTS_STRONGHOLD_CROSSING;
			case 14:
				return LootTables.CHESTS_STRONGHOLD_LIBRARY;
			case 15:
				return LootTables.CHESTS_VILLAGE_BLACKSMITH;
			case 16:
				return LootTables.CHESTS_WOODLAND_MANSION;
			case 17:
				return LootTables.CHESTS_SIMPLE_DUNGEON;
			default:
				if (id >= 18 && id <= 31) {
					return new ResourceLocation(CQRMain.MODID, "chests/custom_" + (id - 17));
				}
				break;
			}
			CQRMain.logger.warn("Failed to read loottable for id {}!", id);
			return CQRLoottables.CHESTS_FOOD;
		}

	}

}
