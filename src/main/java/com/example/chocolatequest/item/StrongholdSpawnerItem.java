package com.example.chocolatequest.item;

import com.example.chocolatequest.world.structure.generation.generators.stronghold.spiral.SpiralStrongholdBuilder;
import com.example.chocolatequest.world.structure.generation.piece.CQRTemplatePiece;
import com.example.chocolatequest.world.structure.generation.piece.StrongholdConnectorPiece;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

import java.util.List;

public class StrongholdSpawnerItem extends Item {

    private static final int ENTRANCE_SIZE = 15;
    private static final int ENTRANCE_HEIGHT = 20;
    private static final int ENTRANCE_DEPTH = 14;
    private static final String UNDEAD_FACTION = "cqrepoured:cq_zombie";

    public StrongholdSpawnerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide && context.getLevel() instanceof ServerLevel serverLevel) {
            BlockPos clickedPos = context.getClickedPos().relative(context.getClickedFace());

            if (context.getPlayer() != null) {
                context.getPlayer().sendSystemMessage(Component.literal("§f[CQR] Spawning Stronghold at " + clickedPos.toShortString() + "..."));
            }

            RandomSource random = serverLevel.random;
            int surfaceY = clickedPos.getY();
            int dungeonY = surfaceY - ENTRANCE_DEPTH;

            BlockPos entranceStart = new BlockPos(clickedPos.getX() - ENTRANCE_SIZE / 2, dungeonY,
                    clickedPos.getZ() - ENTRANCE_SIZE / 2);
            Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);

            int connectorLength = 24;
            int connectorDrop = 10;
            BlockPos connectorStart = new BlockPos(clickedPos.getX(), dungeonY + 1, clickedPos.getZ());
            BlockPos dungeonEntrance = connectorStart
                    .relative(direction, connectorLength)
                    .below(connectorDrop);

            boolean keep = random.nextBoolean();
            ResourceLocation entrance = ResourceLocation.parse(keep
                    ? "cqrepoured:structure/stronghold/entrances/snow/stronghold_entry_keep.nbt"
                    : "cqrepoured:structure/stronghold/entrances/snow/stronghold_entry_tower.nbt");

            int padding = CQRTemplatePiece.TERRAIN_BLEND_RADIUS;
            BoundingBox entranceBox = new BoundingBox(
                    entranceStart.getX() - padding, dungeonY, entranceStart.getZ() - padding,
                    entranceStart.getX() + ENTRANCE_SIZE + padding, dungeonY + ENTRANCE_HEIGHT,
                    entranceStart.getZ() + ENTRANCE_SIZE + padding);

            StructurePiecesBuilder builder = new StructurePiecesBuilder();

            builder.addPiece(new CQRTemplatePiece(entrance, entranceStart, entranceBox,
                    UNDEAD_FACTION, true, surfaceY));

            builder.addPiece(new StrongholdConnectorPiece(
                    connectorStart, direction, connectorLength, connectorDrop));

            SpiralStrongholdBuilder stronghold = new SpiralStrongholdBuilder(
                    builder, direction, random, UNDEAD_FACTION,
                    "cqrepoured:structure/stronghold/normal/");
            stronghold.calculateFloors(dungeonEntrance);
            stronghold.buildFloors();

            PiecesContainer container = builder.build();
            for (StructurePiece piece : container.pieces()) {
                BoundingBox pieceBox = piece.getBoundingBox();
                ChunkPos chunkPos = new ChunkPos(pieceBox.minX() >> 4, pieceBox.minZ() >> 4);
                piece.postProcess(serverLevel, serverLevel.structureManager(), serverLevel.getChunkSource().getGenerator(),
                        random, pieceBox, chunkPos, dungeonEntrance);
            }

            if (context.getPlayer() != null) {
                context.getPlayer().sendSystemMessage(Component.literal("§f[CQR] Successfully spawned Stronghold!"));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }
}
