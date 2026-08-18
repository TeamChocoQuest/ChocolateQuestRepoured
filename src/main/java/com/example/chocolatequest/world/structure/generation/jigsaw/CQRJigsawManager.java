package com.example.chocolatequest.world.structure.generation.jigsaw;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pools.EmptyPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Deque;
import java.util.List;
import java.util.Optional;

public class CQRJigsawManager {
    private static final Logger LOGGER = LogManager.getLogger();

    public interface IPieceFactory {
        PoolElementStructurePiece create(StructureTemplateManager templateManager, StructurePoolElement element, BlockPos pos, int groundLevelDelta, Rotation rotation, BoundingBox box);
    }

    public static void addPieces(RegistryAccess registryAccess, Holder<StructureTemplatePool> startPoolHolder, IPieceFactory factory, ChunkGenerator chunkGenerator, StructureTemplateManager templateManager, BlockPos pos, List<? super StructurePiece> pieceList, RandomSource random, boolean checkSurfaceHeight, int maxDepth) {
        addPieces(registryAccess, startPoolHolder, factory, chunkGenerator, templateManager, pos, pieceList, random, checkSurfaceHeight, maxDepth, Rotation.getRandom(random));
    }

    public static void addPieces(RegistryAccess registryAccess, Holder<StructureTemplatePool> startPoolHolder, IPieceFactory factory, ChunkGenerator chunkGenerator, StructureTemplateManager templateManager, BlockPos pos, List<? super StructurePiece> pieceList, RandomSource random, boolean checkSurfaceHeight, int maxDepth, Rotation rotation) {
        Registry<StructureTemplatePool> poolRegistry = registryAccess.registryOrThrow(Registries.TEMPLATE_POOL);
        StructureTemplatePool startPool = startPoolHolder.value();
        StructurePoolElement startElement = startPool.getRandomTemplate(random);
        if (startElement == EmptyPoolElement.INSTANCE) return;

        PoolElementStructurePiece startPiece = factory.create(templateManager, startElement, pos, startElement.getGroundLevelDelta(), rotation, startElement.getBoundingBox(templateManager, pos, rotation));
        BoundingBox startBox = startPiece.getBoundingBox();

        int midX = (startBox.maxX() + startBox.minX()) / 2;
        int midZ = (startBox.maxZ() + startBox.minZ()) / 2;
        int startY;

        if (checkSurfaceHeight) {
            startY = pos.getY() + chunkGenerator.getFirstOccupiedHeight(midX, midZ, Heightmap.Types.WORLD_SURFACE_WG, net.minecraft.world.level.LevelHeightAccessor.create(0, 384), net.minecraft.world.level.levelgen.RandomState.create(net.minecraft.world.level.levelgen.NoiseGeneratorSettings.dummy(), null, 0L));
        } else {
            startY = pos.getY();
        }

        int groundOffset = startBox.minY() + startPiece.getGroundLevelDelta();
        startPiece.move(0, startY - groundOffset, 0);
        pieceList.add(startPiece);

        if (maxDepth > 0) {
            AABB searchBox = new AABB(midX - 80, startY - 80, midZ - 80, midX + 81, startY + 81, midZ + 81);
            Assembler assembler = new Assembler(poolRegistry, maxDepth, factory, chunkGenerator, templateManager, pieceList, random);
            assembler.placing.addLast(new Entry(startPiece, new MutableObject<>(Shapes.join(Shapes.create(searchBox), Shapes.create(AABB.of(startBox)), BooleanOp.ONLY_FIRST)), startY + 80, 0));

            while (!assembler.placing.isEmpty()) {
                Entry entry = assembler.placing.removeFirst();
                assembler.tryPlacingChildren(entry.piece, entry.free, entry.boundsTop, entry.depth);
            }
        }
    }

    static final class Assembler {
        private final Registry<StructureTemplatePool> pools;
        private final int maxDepth;
        private final IPieceFactory factory;
        private final ChunkGenerator chunkGenerator;
        private final StructureTemplateManager structureManager;
        private final List<? super StructurePiece> pieces;
        private final RandomSource random;
        private final Deque<Entry> placing = Queues.newArrayDeque();

        private Assembler(Registry<StructureTemplatePool> pools, int maxDepth, IPieceFactory factory, ChunkGenerator chunkGenerator, StructureTemplateManager structureManager, List<? super StructurePiece> pieces, RandomSource random) {
            this.pools = pools;
            this.maxDepth = maxDepth;
            this.factory = factory;
            this.chunkGenerator = chunkGenerator;
            this.structureManager = structureManager;
            this.pieces = pieces;
            this.random = random;
        }

        private void tryPlacingChildren(PoolElementStructurePiece piece, MutableObject<VoxelShape> freeShape, int boundsTop, int depth) {
            StructurePoolElement poolElement = piece.getElement();
            BlockPos piecePos = piece.getPosition();
            Rotation pieceRotation = piece.getRotation();
            StructureTemplatePool.Projection projection = poolElement.getProjection();
            boolean isRigid = projection == StructureTemplatePool.Projection.RIGID;
            MutableObject<VoxelShape> pieceShape = new MutableObject<>();
            BoundingBox pieceBox = piece.getBoundingBox();
            int pieceMinY = pieceBox.minY();

            label139: for (StructureTemplate.StructureBlockInfo blockInfo : poolElement.getShuffledJigsawBlocks(this.structureManager, piecePos, pieceRotation, this.random)) {
                Direction facing = JigsawBlock.getFrontFacing(blockInfo.state());
                BlockPos jigsawPos = blockInfo.pos();
                BlockPos targetPos = jigsawPos.relative(facing);
                int yOffset = jigsawPos.getY() - pieceMinY;
                int surfaceY = -1;

                if (blockInfo.nbt() == null) continue;
                String poolStr = blockInfo.nbt().getString("pool");
                ResourceLocation poolLocation = ResourceLocation.tryParse(poolStr);
                if (poolLocation == null) continue;

                Optional<StructureTemplatePool> poolOpt = this.pools.getOptional(poolLocation);
                if (poolOpt.isPresent() && poolOpt.get().size() != 0) {
                    Holder<StructureTemplatePool> fallbackHolder = poolOpt.get().getFallback();
                    Optional<StructureTemplatePool> fallbackOpt = Optional.ofNullable(fallbackHolder.value());
                    if (fallbackOpt.isPresent()) {
                        boolean isInsideTarget = pieceBox.isInside(targetPos);
                        MutableObject<VoxelShape> targetShape;
                        int currentBoundsTop;

                        if (isInsideTarget) {
                            targetShape = pieceShape;
                            currentBoundsTop = pieceMinY;
                            if (pieceShape.getValue() == null) {
                                pieceShape.setValue(Shapes.create(AABB.of(pieceBox)));
                            }
                        } else {
                            targetShape = freeShape;
                            currentBoundsTop = boundsTop;
                        }

                        List<StructurePoolElement> templatesToTry = Lists.newArrayList();
                        if (depth != this.maxDepth) {
                            templatesToTry.addAll(poolOpt.get().getShuffledTemplates(this.random));
                        }
                        templatesToTry.addAll(fallbackOpt.get().getShuffledTemplates(this.random));

                        for (StructurePoolElement childElement : templatesToTry) {
                            if (childElement == EmptyPoolElement.INSTANCE) break;

                            for (Rotation childRotation : Rotation.getShuffled(this.random)) {
                                List<StructureTemplate.StructureBlockInfo> childJigsaws = childElement.getShuffledJigsawBlocks(this.structureManager, BlockPos.ZERO, childRotation, this.random);
                                BoundingBox childLocalBox = childElement.getBoundingBox(this.structureManager, BlockPos.ZERO, childRotation);

                                for (StructureTemplate.StructureBlockInfo childBlockInfo : childJigsaws) {
                                    if (JigsawBlock.canAttach(blockInfo, childBlockInfo)) {
                                        BlockPos childJigsawOffset = childBlockInfo.pos();
                                        BlockPos childPiecePos = new BlockPos(targetPos.getX() - childJigsawOffset.getX(), targetPos.getY() - childJigsawOffset.getY(), targetPos.getZ() - childJigsawOffset.getZ());
                                        BoundingBox childBox = childElement.getBoundingBox(this.structureManager, childPiecePos, childRotation);
                                        int childMinY = childBox.minY();
                                        StructureTemplatePool.Projection childProjection = childElement.getProjection();
                                        boolean isChildRigid = childProjection == StructureTemplatePool.Projection.RIGID;
                                        int childJigsawY = childJigsawOffset.getY();
                                        int stepY = yOffset - childJigsawY + facing.getStepY();
                                        int baseTargetY;

                                        if (isRigid && isChildRigid) {
                                            baseTargetY = pieceMinY + stepY;
                                        } else {
                                            if (surfaceY == -1) {
                                                surfaceY = this.chunkGenerator.getFirstOccupiedHeight(jigsawPos.getX(), jigsawPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, net.minecraft.world.level.LevelHeightAccessor.create(0, 384), net.minecraft.world.level.levelgen.RandomState.create(net.minecraft.world.level.levelgen.NoiseGeneratorSettings.dummy(), null, 0L));
                                            }
                                            baseTargetY = surfaceY - childJigsawY;
                                        }

                                        int yMove = baseTargetY - childMinY;
                                        BoundingBox movedChildBox = childBox.moved(0, yMove, 0);
                                        BlockPos finalChildPos = childPiecePos.offset(0, yMove, 0);

                                        if (!Shapes.joinIsNotEmpty(targetShape.getValue(), Shapes.create(AABB.of(movedChildBox).deflate(0.25D)), BooleanOp.ONLY_SECOND)) {
                                            targetShape.setValue(Shapes.joinUnoptimized(targetShape.getValue(), Shapes.create(AABB.of(movedChildBox)), BooleanOp.ONLY_FIRST));
                                            int parentDelta = piece.getGroundLevelDelta();
                                            int childDelta = isChildRigid ? (parentDelta - stepY) : childElement.getGroundLevelDelta();

                                            PoolElementStructurePiece childPiece = this.factory.create(this.structureManager, childElement, finalChildPos, childDelta, childRotation, movedChildBox);
                                            int junctionY;
                                            if (isRigid) {
                                                junctionY = pieceMinY + yOffset;
                                            } else if (isChildRigid) {
                                                junctionY = baseTargetY + childJigsawY;
                                            } else {
                                                if (surfaceY == -1) {
                                                    surfaceY = this.chunkGenerator.getFirstOccupiedHeight(jigsawPos.getX(), jigsawPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, net.minecraft.world.level.LevelHeightAccessor.create(0, 384), net.minecraft.world.level.levelgen.RandomState.create(net.minecraft.world.level.levelgen.NoiseGeneratorSettings.dummy(), null, 0L));
                                                }
                                                junctionY = surfaceY + stepY / 2;
                                            }

                                            piece.addJunction(new JigsawJunction(targetPos.getX(), junctionY - yOffset + parentDelta, targetPos.getZ(), stepY, childProjection));
                                            childPiece.addJunction(new JigsawJunction(jigsawPos.getX(), junctionY - childJigsawY + childDelta, jigsawPos.getZ(), -stepY, projection));
                                            this.pieces.add(childPiece);

                                            if (depth + 1 <= this.maxDepth) {
                                                this.placing.addLast(new Entry(childPiece, targetShape, currentBoundsTop, depth + 1));
                                            }
                                            continue label139;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        CQRJigsawManager.LOGGER.warn("Empty or non-existent fallback pool for pool: {}", poolLocation);
                    }
                } else {
                    CQRJigsawManager.LOGGER.warn("Empty or non-existent pool: {}", poolLocation);
                }
            }
        }
    }

    static final class Entry {
        private final PoolElementStructurePiece piece;
        private final MutableObject<VoxelShape> free;
        private final int boundsTop;
        private final int depth;

        private Entry(PoolElementStructurePiece piece, MutableObject<VoxelShape> free, int boundsTop, int depth) {
            this.piece = piece;
            this.free = free;
            this.boundsTop = boundsTop;
            this.depth = depth;
        }
    }
}
