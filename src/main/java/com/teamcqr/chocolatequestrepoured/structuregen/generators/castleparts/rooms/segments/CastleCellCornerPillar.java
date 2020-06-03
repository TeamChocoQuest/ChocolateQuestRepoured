package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.RoomGridCell;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Optional;

public class CastleCellCornerPillar {
    private BlockPos origin;
    private HashMap<EnumFacing, CastleMainStructWall> adjacentWalls = new HashMap<>();

    public CastleCellCornerPillar() {
        this.origin = origin;
    }

    public void registerAdjacentWall(CastleMainStructWall wall, EnumFacing directionOfWall) {
        adjacentWalls.put(directionOfWall, wall);
    }

    public Optional<CastleMainStructWall> getAdjacentWall(EnumFacing direction) {
        if (adjacentWalls.containsKey(direction)) {
            return Optional.of(adjacentWalls.get(direction));
        } else {
            return Optional.empty();
        }
    }
}
