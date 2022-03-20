package team.cqr.cqrepoured.init;

import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import team.cqr.cqrepoured.world.structure.generation.thewall.wallparts.WallPieceTower;
import team.cqr.cqrepoured.world.structure.generation.thewall.wallparts.WallPieceWall;

public class CQRStructurePieces {
	
	public static IStructurePieceType WALL_PIECE_WALL = WallPieceWall::new;
	public static IStructurePieceType WALL_PIECE_GATE = null;
	public static IStructurePieceType WALL_PIECE_TOWER = WallPieceTower::new;

}
