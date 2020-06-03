package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.RoomGridCell;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Optional;

public class CastleMainStructWall {
     public enum WallOrientation {
        HORIZONTAL_WALL,
        VERTICAL_WALL
    }

    private BlockPos origin;
    private WallOrientation orientation;
    private HashMap<EnumFacing, RoomGridCell> adjacentCells = new HashMap<>();
    private HashMap<EnumFacing, CastleCellCornerPillar> neighborPillars = new HashMap<>();

    public CastleMainStructWall(BlockPos origin, WallOrientation orientation) {
        this.origin = origin;
        this.orientation = orientation;
    }

    public void registerAdjacentCell(RoomGridCell cell, EnumFacing directionOfCell) {
        adjacentCells.put(directionOfCell, cell);
    }

    public Optional<RoomGridCell> getAdjacentCell(EnumFacing direction) {
        if (adjacentCells.containsKey(direction)) {
            return Optional.of(adjacentCells.get(direction));
        } else {
            return Optional.empty();
        }
    }

    public void registerAdjacentPillar(CastleCellCornerPillar pillar, EnumFacing directionOfPillar) {
        neighborPillars.put(directionOfPillar, pillar);
    }

    public Optional<CastleCellCornerPillar> getAdjacentPillar(EnumFacing direction) {
        if (neighborPillars.containsKey(direction)) {
            return Optional.of(neighborPillars.get(direction));
        } else {
            return Optional.empty();
        }
    }
}

