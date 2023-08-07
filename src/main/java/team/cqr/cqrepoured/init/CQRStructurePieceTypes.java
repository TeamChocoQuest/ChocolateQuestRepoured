package team.cqr.cqrepoured.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType.ContextlessType;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;

public class CQRStructurePieceTypes {

	public static final StructurePieceType CQR_STRUCTURE_PIECE_TYPE = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, "cqr_structure_piece_type", (ContextlessType) GeneratableDungeon::new);

}
