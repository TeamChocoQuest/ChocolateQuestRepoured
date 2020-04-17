package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import java.util.HashSet;

import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.CastleRoomBase;
import com.teamcqr.chocolatequestrepoured.structuregen.lootchests.ELootTable;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RoomDecorChest extends RoomDecorBlocksBase {
	public RoomDecorChest() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockRotating(0, 0, 0, Blocks.CHEST.getDefaultState(), BlockChest.FACING, EnumFacing.SOUTH, BlockStateGenArray.GenerationPhase.MAIN));
	}

	@Override
	public void build(World world, BlockStateGenArray genArray, CastleRoomBase room, CastleDungeon dungeon, BlockPos start, EnumFacing side, HashSet<BlockPos> decoMap) {
		//super.build(world, genArray, room, dungeon, start, side, decoMap);

		int[] chestIDs = room.getChestIDs();
		if (chestIDs != null) {
			Block chestBlock = Blocks.CHEST;
			IBlockState state = this.schematic.get(0).getState(side);
			TileEntityChest chest = (TileEntityChest) chestBlock.createTileEntity(world, state);
			if (chest != null) {
				int eltID = chestIDs[dungeon.getRandom().nextInt(chestIDs.length)];
				ResourceLocation resLoc = null;
				try {
					resLoc = ELootTable.values()[eltID].getResourceLocation();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				if (resLoc != null) {
					long seed = WorldDungeonGenerator.getSeed(world, start.getX() + start.getY(), start.getZ() + start.getY());
					chest.setLootTable(resLoc, seed);
				}
				NBTTagCompound nbt = chest.writeToNBT(new NBTTagCompound());
				genArray.forceAddBlockState(start, state, nbt, BlockStateGenArray.GenerationPhase.MAIN);
				decoMap.add(start);
			}
		} else {
			System.out.format("Placed a chest but could not find a loot table for Room Type {%s}", room.getRoomType().toString());
		}
	}
}
