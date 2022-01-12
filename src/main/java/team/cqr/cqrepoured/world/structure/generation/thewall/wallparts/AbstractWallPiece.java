package team.cqr.cqrepoured.world.structure.generation.thewall.wallparts;

import java.util.Random;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon.Builder;

public abstract class AbstractWallPiece extends StructurePiece implements IWallPart {
	
	protected EWallPieceType type;
	
	public AbstractWallPiece(IStructurePieceType pType, CompoundNBT pTag) {
		super(pType, pTag);
		EWallPieceType optType = EWallPieceType.WALL;
		if(pTag.contains("wallparttype", Constants.NBT.TAG_BYTE) ) {
			optType = EWallPieceType.values()[pTag.getByte("wallparttype")];
		}
		this.type = optType;
	}
	
	@Override
	public void generateWall(int chunkX, int chunkZ, ChunkGenerator cg, Builder dungeonBuilder, ServerWorld sw) {
		
	}
	
	public AbstractWallPiece(IStructurePieceType pType, CompoundNBT pTag, EWallPieceType type) {
		super(pType, pTag);
		this.type = type;
	}

	public AbstractWallPiece(IStructurePieceType spt, int i) {
		super(spt, i);
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT p_143011_1_) {

	}
	
	@Override
	public boolean postProcess(ISeedReader pLevel, StructureManager pStructureManager, ChunkGenerator pChunkGenerator, Random pRandom, MutableBoundingBox pBox, ChunkPos pChunkPos, BlockPos pPos) {
		
		return true;
	}

}
