package team.cqr.cqrepoured.structuregen.structurefile;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockChest;
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
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.structuregen.WorldDungeonGenerator;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structureprot.ProtectedRegion;
import team.cqr.cqrepoured.util.BlockPlacingHelper;
import team.cqr.cqrepoured.util.Reference;

public class BlockInfoLootChest extends AbstractBlockInfo {

	protected ResourceLocation lootTable = LootTableList.EMPTY;
	protected EnumFacing facing = EnumFacing.NORTH;

	public BlockInfoLootChest(int x, int y, int z) {
		super(x, y, z);
	}

	public BlockInfoLootChest(BlockPos pos) {
		super(pos);
	}

	public BlockInfoLootChest(int x, int y, int z, ResourceLocation lootTable, EnumFacing facing) {
		super(x, y, z);
		this.lootTable = lootTable;
		this.facing = facing;
	}

	public BlockInfoLootChest(BlockPos pos, ResourceLocation lootTable, EnumFacing facing) {
		this(pos.getX(), pos.getY(), pos.getZ(), lootTable, facing);
	}

	@Override
	public void generateAt(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, DungeonInhabitant dungeonMob, ProtectedRegion protectedRegion, BlockPos pos) {
		IBlockState iblockstate = Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, this.facing).withMirror(settings.getMirror()).withRotation(settings.getRotation());
		BlockPlacingHelper.setBlockState(world, pos, iblockstate, 18, false);
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileEntityChest) {
			long seed = WorldDungeonGenerator.getSeed(world, pos.getX(), pos.getZ());
			((TileEntityChest) tileEntity).setLootTable(this.lootTable, seed);
		} else {
			CQRMain.logger.warn("Failed to place loot chest at {}", pos);
		}
	}

	@Override
	public byte getId() {
		return CHEST_INFO_ID;
	}

	@Override
	protected void writeToByteBufInternal(ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		ByteBufUtils.writeUTF8String(buf, this.lootTable.toString());
		buf.writeByte(this.facing.getHorizontalIndex());
	}

	@Override
	protected void readFromByteBufInternal(ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		this.lootTable = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
		this.facing = EnumFacing.byHorizontalIndex(buf.readByte());
	}

	public ResourceLocation getLootTable() {
		return this.lootTable;
	}

	public EnumFacing getFacing() {
		return this.facing;
	}

	@Deprecated
	public BlockInfoLootChest(int x, int y, int z, NBTTagIntArray nbtTagIntArray) {
		super(x, y, z);
		int[] ints = nbtTagIntArray.getIntArray();
		this.lootTable = this.getLootTableFromId(ints[1]);
		this.facing = EnumFacing.byHorizontalIndex(ints[2]);
	}

	@Deprecated
	private ResourceLocation getLootTableFromId(int id) {
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
				return new ResourceLocation(Reference.MODID, "chests/custom_" + (id - 17));
			}
			break;
		}
		CQRMain.logger.warn("Failed to read loottable for id {}!", id);
		return CQRLoottables.CHESTS_FOOD;
	}

}
