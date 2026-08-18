package com.example.chocolatequest.item;

import com.example.chocolatequest.world.structure.generation.generators.stronghold.spiral.SpiralStrongholdBuilder;
import com.example.chocolatequest.world.structure.generation.piece.VolcanoEntrancePiece;
import com.example.chocolatequest.world.structure.generation.piece.VolcanoTerrainPiece;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class VolcanoSpawnerItem extends Item {

    public VolcanoSpawnerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide && context.getLevel() instanceof ServerLevel serverLevel) {
            BlockPos clickedPos = context.getClickedPos().relative(context.getClickedFace());
            BlockPos volcanoCenterPos = clickedPos;

            if (context.getPlayer() != null) {
                context.getPlayer().sendSystemMessage(Component.literal("§f[CQR] Spawning Volcano at " + volcanoCenterPos.toShortString() + "..."));
            }

            RandomSource random = serverLevel.random;
            StructurePiecesBuilder builder = new StructurePiecesBuilder();

            VolcanoTerrainPiece terrainPiece = new VolcanoTerrainPiece(volcanoCenterPos, random);
            builder.addPiece(terrainPiece);

            Direction entranceDirection = terrainPiece.getStairLandingDirection();
            int stairLandingY = terrainPiece.getStairLandingLocalY();
            int landingInnerRadius = terrainPiece.getInnerRadiusAt(stairLandingY);
            int landingOuterRadius = terrainPiece.getOuterRadiusAt(stairLandingY);
            
            int tunnelStartDist = Math.max(10, landingInnerRadius - 4);
            BlockPos tunnelStart = volcanoCenterPos
                    .relative(entranceDirection, tunnelStartDist)
                    .offset(0, stairLandingY, 0);
            
            int requiredTunnelLength = Math.max(16, (landingOuterRadius - tunnelStartDist) + 8);
            int tunnelSegments = (requiredTunnelLength + VolcanoEntrancePiece.SEGMENT_LENGTH - 1) / VolcanoEntrancePiece.SEGMENT_LENGTH;
            builder.addPiece(new VolcanoEntrancePiece(tunnelStart, entranceDirection, tunnelSegments));

            BlockPos strongholdEntrancePos = tunnelStart
                    .relative(entranceDirection, tunnelSegments * VolcanoEntrancePiece.SEGMENT_LENGTH);

            SpiralStrongholdBuilder strongholdBuilder = 
                new SpiralStrongholdBuilder(
                        builder, entranceDirection, random, terrainPiece.getDungeonFaction());
            strongholdBuilder.calculateFloors(strongholdEntrancePos);
            strongholdBuilder.buildFloors();

            PiecesContainer container = builder.build();
            
            for (StructurePiece piece : container.pieces()) {
                BoundingBox pieceBox = piece.getBoundingBox();
                ChunkPos chunkPos = new ChunkPos(pieceBox.minX() >> 4, pieceBox.minZ() >> 4);
                piece.postProcess(serverLevel, serverLevel.structureManager(), serverLevel.getChunkSource().getGenerator(),
                        random, pieceBox, chunkPos, volcanoCenterPos);
            }

            if (context.getPlayer() != null) {
                context.getPlayer().sendSystemMessage(Component.literal("§f[CQR] Successfully spawned Volcano!"));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }
}
