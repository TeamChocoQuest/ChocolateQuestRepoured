package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import java.util.HashSet;

import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.CastleRoom;
import com.teamcqr.chocolatequestrepoured.structuregen.lootchests.ELootTable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RoomDecorChest extends RoomDecorBlocks {
	public RoomDecorChest() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockOffset(0, 0, 0, Blocks.CHEST));
	}

	@Override
	protected IBlockState getRotatedBlockState(Block block, EnumFacing side) {
		IBlockState result = block.getDefaultState();

		if (block == Blocks.CHEST) {
			result = result.withProperty(BlockFurnace.FACING, side.getOpposite());
		}

		return result;
	}

	@Override
	public void build(World world, CastleRoom room, CastleDungeon dungeon, BlockPos start, EnumFacing side, HashSet<BlockPos> decoMap) {
		super.build(world, room, dungeon, start, side, decoMap);

		int[] chestIDs = room.getChestIDs();
		if (chestIDs != null) {
			TileEntityChest chest = (TileEntityChest) world.getTileEntity(start);
			if (chest != null) {
				int eltID = chestIDs[dungeon.getRandom().nextInt(chestIDs.length)];
				ResourceLocation resLoc = null;
				try {
					resLoc = ELootTable.valueOf(eltID).getResourceLocation();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				if (resLoc != null) {
					long seed = WorldDungeonGenerator.getSeed(world, start.getX() + start.getY(), start.getZ() + start.getY());
					chest.setLootTable(resLoc, seed);
				}
			}
		} else {
			System.out.format("Placed a chest but could not find a loot table for Room Type {%s}", room.getRoomType().toString());
		}
	}
}
