package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.decoration.objects;

import java.util.Set;

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
import team.cqr.cqrepoured.util.BlockStateGenArray;
import team.cqr.cqrepoured.world.structure.generation.WorldDungeonGenerator;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;
import team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.CastleRoomBase;

public class RoomDecorChest extends RoomDecorBlocksBase {
	public RoomDecorChest() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(
				new DecoBlockRotating(0, 0, 0, Blocks.CHEST.getDefaultState(), BlockChest.FACING, EnumFacing.SOUTH, BlockStateGenArray.GenerationPhase.MAIN));
	}

	@Override
	public void build(World world, BlockStateGenArray genArray, CastleRoomBase room, DungeonRandomizedCastle dungeon, BlockPos start, EnumFacing side,
			Set<BlockPos> decoMap) {
		// super.build(world, genArray, room, dungeon, start, side, decoMap);

		ResourceLocation[] chestIDs = room.getChestIDs();
		if (chestIDs != null && chestIDs.length > 0) {
			Block chestBlock = Blocks.CHEST;
			IBlockState state = this.schematic.get(0).getState(side);
			TileEntityChest chest = (TileEntityChest) chestBlock.createTileEntity(world, state);
			if (chest != null) {
				ResourceLocation resLoc = chestIDs[genArray.getRandom().nextInt(chestIDs.length)];
				if (resLoc != null) {
					long seed = WorldDungeonGenerator.getSeed(world, start.getX() + start.getY(), start.getZ() + start.getY());
					chest.setLootTable(resLoc, seed);
				}
				NBTTagCompound nbt = chest.writeToNBT(new NBTTagCompound());
				genArray.addBlockState(start, state, nbt, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.HIGH);
				decoMap.add(start);
			}
		} else {
			// CQRMain.logger.warn("Placed a chest but could not find a loot table for Room Type {}", room.getRoomType());
			// TODO fix rooms having no chests (or is this intended?)
		}
	}
}
