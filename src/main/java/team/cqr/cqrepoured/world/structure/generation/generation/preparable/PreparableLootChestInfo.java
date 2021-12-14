package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

import java.util.function.Supplier;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
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

public class PreparableLootChestInfo extends PreparablePosInfo {

	private final ResourceLocation lootTable;
	private final EnumFacing facing;

	public PreparableLootChestInfo(BlockPos pos, ResourceLocation lootTable, EnumFacing facing) {
		this(pos.getX(), pos.getY(), pos.getZ(), lootTable, facing);
	}

	public PreparableLootChestInfo(int x, int y, int z, ResourceLocation lootTable, EnumFacing facing) {
		super(x, y, z);
		this.lootTable = lootTable;
		this.facing = facing;
	}

	@Override
	protected GeneratablePosInfo prepare(World world, DungeonPlacement placement, BlockPos pos) {
		IBlockState state = Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, this.facing);
		state = state.withMirror(placement.getMirror()).withRotation(placement.getRotation());
		TileEntity tileEntity = state.getBlock().createTileEntity(world, state);

		if (tileEntity instanceof TileEntityChest) {
			long seed = WorldDungeonGenerator.getSeed(world, pos.getX(), pos.getZ());
			((TileEntityChest) tileEntity).setLootTable(this.lootTable, seed);
		}

		return new GeneratableBlockInfo(pos, state, tileEntity);
	}

	@Override
	protected GeneratablePosInfo prepareDebug(World world, DungeonPlacement placement, BlockPos pos) {
		BlockExporterChest block = BlockExporterChest.getExporterChests().stream().filter(BlockExporterChestCQR.class::isInstance).filter(b -> ((BlockExporterChestCQR) b).getLootTable().equals(this.lootTable)).findFirst().orElse(CQRBlocks.EXPORTER_CHEST_CUSTOM);
		IBlockState state = block.getDefaultState().withProperty(BlockHorizontal.FACING, this.facing);
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

	public EnumFacing getFacing() {
		return this.facing;
	}

	public static class Factory implements IFactory<TileEntityExporterChest> {

		@Override
		public PreparablePosInfo create(World world, int x, int y, int z, IBlockState state, Supplier<TileEntityExporterChest> tileEntitySupplier) {
			return new PreparableLootChestInfo(x, y, z, tileEntitySupplier.get().getLootTable(), state.getValue(BlockHorizontal.FACING));
		}

	}

	public static class Serializer implements ISerializer<PreparableLootChestInfo> {

		@Override
		public void write(PreparableLootChestInfo preparable, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			ByteBufUtils.writeUTF8String(buf, preparable.lootTable.toString());
			buf.writeByte(preparable.facing.getHorizontalIndex());
		}

		@Override
		public PreparableLootChestInfo read(int x, int y, int z, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			ResourceLocation lootTable = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
			EnumFacing facing = EnumFacing.byHorizontalIndex(buf.readByte());
			return new PreparableLootChestInfo(x, y, z, lootTable, facing);
		}

		@Override
		@Deprecated
		public PreparableLootChestInfo read(int x, int y, int z, NBTTagIntArray nbtIntArray, BlockStatePalette palette, NBTTagList nbtList) {
			int[] intArray = nbtIntArray.getIntArray();
			ResourceLocation lootTable = getLootTableFromId(intArray[1]);
			EnumFacing facing = EnumFacing.byHorizontalIndex(intArray[2]);
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
				return LootTableList.CHESTS_ABANDONED_MINESHAFT;
			case 5:
				return LootTableList.CHESTS_DESERT_PYRAMID;
			case 6:
				return LootTableList.CHESTS_END_CITY_TREASURE;
			case 7:
				return LootTableList.CHESTS_IGLOO_CHEST;
			case 8:
				return LootTableList.CHESTS_JUNGLE_TEMPLE;
			case 9:
				return LootTableList.CHESTS_JUNGLE_TEMPLE_DISPENSER;
			case 10:
				return LootTableList.CHESTS_NETHER_BRIDGE;
			case 11:
				return LootTableList.CHESTS_SPAWN_BONUS_CHEST;
			case 12:
				return LootTableList.CHESTS_STRONGHOLD_CORRIDOR;
			case 13:
				return LootTableList.CHESTS_STRONGHOLD_CROSSING;
			case 14:
				return LootTableList.CHESTS_STRONGHOLD_LIBRARY;
			case 15:
				return LootTableList.CHESTS_VILLAGE_BLACKSMITH;
			case 16:
				return LootTableList.CHESTS_WOODLAND_MANSION;
			case 17:
				return LootTableList.CHESTS_SIMPLE_DUNGEON;
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
