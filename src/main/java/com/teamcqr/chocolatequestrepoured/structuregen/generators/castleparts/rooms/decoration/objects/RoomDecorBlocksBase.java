package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.CastleRoomBase;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.IRoomDecor;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class RoomDecorBlocksBase implements IRoomDecor {

	protected List<DecoBlockBase> schematic; // Array of blockstates and their offsets

	protected RoomDecorBlocksBase() {
		this.schematic = new ArrayList<>();
		this.makeSchematic();
	}

	protected abstract void makeSchematic();

	@Override
	public boolean wouldFit(BlockPos start, EnumFacing side, HashSet<BlockPos> decoArea, HashSet<BlockPos> decoMap) {
		ArrayList<DecoBlockBase> rotated = this.alignSchematic(side);

		for (DecoBlockBase placement : rotated) {
			BlockPos pos = start.add(placement.offset);
			if (!decoArea.contains(pos) || decoMap.contains(pos)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void build(World world, BlockStateGenArray genArray, CastleRoomBase room, CastleDungeon dungeon, BlockPos start, EnumFacing side, HashSet<BlockPos> decoMap) {
		ArrayList<DecoBlockBase> rotated = this.alignSchematic(side);

		for (DecoBlockBase placement : rotated) {
			BlockPos pos = start.add(placement.offset);
			genArray.add(pos, placement.getState(side), placement.getGenPhase());

			if (placement.getState(side).getBlock() != Blocks.AIR) {
				decoMap.add(pos);
			}
		}
	}

	protected ArrayList<DecoBlockBase> alignSchematic(EnumFacing side) {
		ArrayList<DecoBlockBase> result = new ArrayList<>();

		for (DecoBlockBase p : this.schematic) {
			result.add(new DecoBlockBase(DungeonGenUtils.rotateVec3i(p.offset, side), p.getState(side), p.getGenPhase()));
		}

		return result;
	}
}
