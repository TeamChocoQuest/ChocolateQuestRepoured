package com.example.chocolatequest.world.structure.generation.piece;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.ArrayList;
import java.util.List;

public class VolcanoDungeonPiece {

    private static class GridNode {
        int x, y, z;
        Direction dirIn;
        Direction dirOut;
        boolean isStair;

        public GridNode(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static void generate(StructurePiecesBuilder builder, BlockPos startPos, RandomSource random) {
        int size = 7;
        int minX = 0, maxX = size - 1;
        int minZ = 0, maxZ = size - 1;
        int x = maxX, z = maxZ;
        Direction currentDir = Direction.WEST;
        boolean[][] visited = new boolean[size][size];

        List<GridNode> path = new ArrayList<>();

        // Generate the 3D inward spiral path
        while (true) {
            visited[x][z] = true;
            int distToCenter = Math.max(Math.abs(x - 3), Math.abs(z - 3));
            int y = -(3 - distToCenter);
            path.add(new GridNode(x, y, z));

            int nx = x + currentDir.getStepX();
            int nz = z + currentDir.getStepZ();

            if (nx < minX || nx > maxX || nz < minZ || nz > maxZ || visited[nx][nz]) {
                currentDir = currentDir.getClockWise(); // Turn right
                nx = x + currentDir.getStepX();
                nz = z + currentDir.getStepZ();

                if (nx < minX || nx > maxX || nz < minZ || nz > maxZ || visited[nx][nz]) {
                    break; // Reached the center
                }
            }
            x = nx;
            z = nz;
        }

        for (int i = 0; i < path.size(); i++) {
            GridNode node = path.get(i);
            if (i > 0) {
                GridNode prev = path.get(i - 1);
                int dx = node.x - prev.x;
                int dz = node.z - prev.z;
                node.dirIn = getDirFromOffset(dx, dz);
            } else {
                node.dirIn = Direction.WEST;
            }

            if (i < path.size() - 1) {
                GridNode next = path.get(i + 1);
                int dx = next.x - node.x;
                int dz = next.z - node.z;
                node.dirOut = getDirFromOffset(dx, dz);
                
                if (node.y > next.y) {
                    node.isStair = true;
                }
            } else {
                node.dirOut = null;
            }
        }

        List<BoundingBox> placedBoxes = new ArrayList<>();

        for (int i = 0; i < path.size(); i++) {
            GridNode node = path.get(i);
            String templateName = pickTemplate(node.dirIn, node.dirOut, node.isStair, random);
            
            int gridOffsetX = node.x - (size / 2);
            int gridOffsetZ = node.z - (size / 2);
            
            // world Y drops by 10 for every grid Y level
            BlockPos worldPos = startPos.offset(gridOffsetX * 15, node.y * 10, gridOffsetZ * 15);
            BoundingBox box = BoundingBox.fromCorners(worldPos, worldPos.offset(14, 9, 14));
            
            placeTemplate(builder, placedBoxes, worldPos, box, templateName);
        }
    }

    private static String pickTemplate(Direction in, Direction out, boolean isStair, RandomSource random) {
        int r = random.nextInt(3) + 1; // 1 to 3
        
        if (out == null) {
            return random.nextBoolean() ? "boss/volcano_boss_room1" : "boss/volcano_boss_room2";
        }

        if (isStair) {
            switch (out) {
                case NORTH: return "stairs/n/stronghold_stair_north_1";
                case SOUTH: return "stairs/s/stronghold_stair_south_1";
                case EAST: return "stairs/e/stronghold_stair_east_1";
                case WEST: return "stairs/w/stronghold_stair_west_1";
            }
        }

        if (in == out) {
            if (in == Direction.NORTH || in == Direction.SOUTH) {
                return "hallway/ns/stronghold_north_south_" + r;
            } else {
                return "hallway/ew/stronghold_east_west_" + r;
            }
        } else {
            Direction door1 = in.getOpposite();
            Direction door2 = out;
            
            if ((door1 == Direction.NORTH && door2 == Direction.EAST) || (door1 == Direction.EAST && door2 == Direction.NORTH)) {
                return "curves/ne/stronghold_north_east_" + r;
            }
            if ((door1 == Direction.NORTH && door2 == Direction.WEST) || (door1 == Direction.WEST && door2 == Direction.NORTH)) {
                return "curves/nw/stronghold_north_west_" + r;
            }
            if ((door1 == Direction.SOUTH && door2 == Direction.EAST) || (door1 == Direction.EAST && door2 == Direction.SOUTH)) {
                return "curves/se/stronghold_south_east_" + r;
            }
            if ((door1 == Direction.SOUTH && door2 == Direction.WEST) || (door1 == Direction.WEST && door2 == Direction.SOUTH)) {
                return "curves/sw/stronghold_south_west_" + r;
            }
        }
        
        return "hallway/ns/stronghold_north_south_1";
    }

    private static Direction getDirFromOffset(int dx, int dz) {
        if (dx > 0) return Direction.EAST;
        if (dx < 0) return Direction.WEST;
        if (dz > 0) return Direction.SOUTH;
        if (dz < 0) return Direction.NORTH;
        return Direction.NORTH;
    }

    private static void placeTemplate(StructurePiecesBuilder builder, List<BoundingBox> placedBoxes, BlockPos worldPos, BoundingBox box, String template) {
        ResourceLocation loc = ResourceLocation.parse("cqrepoured:structure/volcano/rooms/" + template.toLowerCase() + ".nbt");
        CQRTemplatePiece piece = new CQRTemplatePiece(loc, worldPos, box, "cqrepoured:cq_gremlin");
        builder.addPiece(piece);
        placedBoxes.add(box);
    }
}
