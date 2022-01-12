package team.cqr.cqrepoured.init;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import team.cqr.cqrepoured.world.structure.generation.thewall.wallparts.WallPieceTower;
import team.cqr.cqrepoured.world.structure.generation.thewall.wallparts.WallPieceWall;

public class CQRStructurePieces {
	
	public static IStructurePieceType WALL_PIECE_WALL = Registry.register(Registry.STRUCTURE_PIECE, "cqr-wall-part-wall", WallPieceWall::new);
	public static IStructurePieceType WALL_PIECE_GATE = Registry.register(Registry.STRUCTURE_PIECE, "cqr-wall-part-gate", null);
	public static IStructurePieceType WALL_PIECE_TOWER = Registry.register(Registry.STRUCTURE_PIECE, "cqr-wall-part-tower", WallPieceTower::new);

}
