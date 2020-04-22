package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.CastleRoomBase;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.IRoomDecor;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class RoomDecorEntityBase implements IRoomDecor {
	protected List<Vec3i> footprint; // Array of blockstates and their offsets

	protected RoomDecorEntityBase() {
		this.footprint = new ArrayList<>();
	}

	@Override
	public boolean wouldFit(BlockPos start, EnumFacing side, HashSet<BlockPos> decoArea, HashSet<BlockPos> decoMap, CastleRoomBase room) {
		ArrayList<Vec3i> rotated = this.alignFootprint(this.footprint, side);

		for (Vec3i placement : rotated) {
			BlockPos pos = start.add(placement);
			if (!decoArea.contains(pos) || decoMap.contains(pos)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void build(World world, BlockStateGenArray genArray, CastleRoomBase room, DungeonCastle dungeon, BlockPos start, EnumFacing side, HashSet<BlockPos> decoMap) {
		ArrayList<Vec3i> rotated = this.alignFootprint(this.footprint, side);

		for (Vec3i placement : rotated) {
			BlockPos pos = start.add(placement);
			decoMap.add(pos);
		}
		this.createEntityDecoration(world, start, genArray, side);
	}

	protected abstract void createEntityDecoration(World world, BlockPos pos, BlockStateGenArray genArray, EnumFacing side);


	protected ArrayList<Vec3i> alignFootprint(List<Vec3i> unrotated, EnumFacing side) {
		ArrayList<Vec3i> result = new ArrayList<>();

		unrotated.forEach(v -> result.add(DungeonGenUtils.rotateVec3i(v, side)));

		return result;
	}
}
