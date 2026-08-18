package com.example.chocolatequest.registry;

import com.example.chocolatequest.ChocolateQuestReDone;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.example.chocolatequest.world.structure.generation.structure.CQRStructure;
import com.example.chocolatequest.world.structure.generation.structure.CQRTemplateStructure;
import com.example.chocolatequest.world.structure.generation.piece.CQRStructurePiece;
import com.example.chocolatequest.world.structure.generation.piece.CQRTemplatePiece;
import com.example.chocolatequest.world.structure.generation.pool.CQRPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;

public class ModStructures {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, ChocolateQuestReDone.MODID);
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES = DeferredRegister.create(Registries.STRUCTURE_PIECE, ChocolateQuestReDone.MODID);
    public static final DeferredRegister<StructurePoolElementType<?>> POOL_ELEMENT_TYPES = DeferredRegister.create(Registries.STRUCTURE_POOL_ELEMENT, ChocolateQuestReDone.MODID);

    public static final DeferredHolder<StructureType<?>, StructureType<CQRStructure>> CQR_STRUCTURE = STRUCTURE_TYPES.register("cqr_structure", () -> () -> CQRStructure.CODEC);
    public static final DeferredHolder<StructureType<?>, StructureType<CQRTemplateStructure>> CQR_TEMPLATE_STRUCTURE = STRUCTURE_TYPES.register("cqr_template", () -> () -> CQRTemplateStructure.CODEC);

    public static final DeferredHolder<StructurePieceType, StructurePieceType> CQR_STRUCTURE_PIECE = STRUCTURE_PIECE_TYPES.register("cqr_structure_piece", () -> (StructurePieceType.ContextlessType) CQRStructurePiece::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> CQR_TEMPLATE_PIECE = STRUCTURE_PIECE_TYPES.register("cqr_template_piece", () -> (StructurePieceType.ContextlessType) CQRTemplatePiece::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> CQR_VOLCANO_PIECE = STRUCTURE_PIECE_TYPES.register("cqr_volcano_piece", () -> com.example.chocolatequest.world.structure.generation.piece.VolcanoTerrainPiece::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> CQR_VOLCANO_INTERSECTION_PIECE = STRUCTURE_PIECE_TYPES.register("cqr_volcano_intersection_piece", () -> (StructurePieceType.ContextlessType) com.example.chocolatequest.world.structure.generation.piece.VolcanoIntersectionPiece::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> CQR_VOLCANO_ENTRANCE_PIECE = STRUCTURE_PIECE_TYPES.register("cqr_volcano_entrance_piece", () -> (StructurePieceType.ContextlessType) com.example.chocolatequest.world.structure.generation.piece.VolcanoEntrancePiece::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> CQR_HANGING_CITY_PIECE = STRUCTURE_PIECE_TYPES.register("cqr_hanging_city_piece", () -> com.example.chocolatequest.world.structure.generation.piece.HangingCityTerrainPiece::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> CQR_PROCEDURAL_CASTLE_PIECE = STRUCTURE_PIECE_TYPES.register("cqr_procedural_castle_piece", () -> (StructurePieceType.ContextlessType) com.example.chocolatequest.world.structure.generation.piece.ProceduralCastlePiece::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> CQR_BOSS_GROTTO_PIECE = STRUCTURE_PIECE_TYPES.register("cqr_boss_grotto_piece", () -> (StructurePieceType.ContextlessType) com.example.chocolatequest.world.structure.generation.piece.BossGrottoPiece::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> CQR_SHULKER_GOLEM_DUNGEON_PIECE = STRUCTURE_PIECE_TYPES.register("cqr_shulker_golem_dungeon_piece", () -> (StructurePieceType.ContextlessType) com.example.chocolatequest.world.structure.generation.piece.ShulkerGolemDungeonPiece::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> CQR_STRONGHOLD_CONNECTOR_PIECE = STRUCTURE_PIECE_TYPES.register("cqr_stronghold_connector_piece", () -> (StructurePieceType.ContextlessType) com.example.chocolatequest.world.structure.generation.piece.StrongholdConnectorPiece::new);

    public static final DeferredHolder<StructureType<?>, StructureType<com.example.chocolatequest.world.structure.generation.structure.VolcanoStructure>> CQR_VOLCANO = STRUCTURE_TYPES.register("volcano", () -> () -> com.example.chocolatequest.world.structure.generation.structure.VolcanoStructure.CODEC);
    public static final DeferredHolder<StructureType<?>, StructureType<com.example.chocolatequest.world.structure.generation.structure.HangingCityStructure>> CQR_HANGING_CITY = STRUCTURE_TYPES.register("hanging_city", () -> () -> com.example.chocolatequest.world.structure.generation.structure.HangingCityStructure.CODEC);
    public static final DeferredHolder<StructureType<?>, StructureType<com.example.chocolatequest.world.structure.generation.structure.LavaLakeJigsawStructure>> CQR_LAVA_LAKE_JIGSAW = STRUCTURE_TYPES.register("lava_lake_jigsaw", () -> () -> com.example.chocolatequest.world.structure.generation.structure.LavaLakeJigsawStructure.CODEC);
    public static final DeferredHolder<StructureType<?>, StructureType<com.example.chocolatequest.world.structure.generation.structure.ProceduralCastleStructure>> CQR_PROCEDURAL_CASTLE = STRUCTURE_TYPES.register("procedural_castle", () -> () -> com.example.chocolatequest.world.structure.generation.structure.ProceduralCastleStructure.CODEC);
    public static final DeferredHolder<StructureType<?>, StructureType<com.example.chocolatequest.world.structure.generation.structure.BossGrottoStructure>> CQR_BOSS_GROTTO = STRUCTURE_TYPES.register("boss_grotto", () -> () -> com.example.chocolatequest.world.structure.generation.structure.BossGrottoStructure.CODEC);
    public static final DeferredHolder<StructureType<?>, StructureType<com.example.chocolatequest.world.structure.generation.structure.ShulkerGolemDungeonStructure>> CQR_SHULKER_GOLEM_DUNGEON = STRUCTURE_TYPES.register("shulker_golem_dungeon", () -> () -> com.example.chocolatequest.world.structure.generation.structure.ShulkerGolemDungeonStructure.CODEC);
    public static final DeferredHolder<StructureType<?>, StructureType<com.example.chocolatequest.world.structure.generation.structure.SpiralStrongholdStructure>> CQR_SPIRAL_STRONGHOLD = STRUCTURE_TYPES.register("spiral_stronghold", () -> () -> com.example.chocolatequest.world.structure.generation.structure.SpiralStrongholdStructure.CODEC);

    public static final DeferredHolder<StructurePoolElementType<?>, StructurePoolElementType<CQRPoolElement>> CQR_POOL_ELEMENT = POOL_ELEMENT_TYPES.register("cqr_pool_element", () -> () -> CQRPoolElement.CODEC);

    public static void register(IEventBus eventBus) {
        STRUCTURE_TYPES.register(eventBus);
        STRUCTURE_PIECE_TYPES.register(eventBus);
        POOL_ELEMENT_TYPES.register(eventBus);
    }

}
