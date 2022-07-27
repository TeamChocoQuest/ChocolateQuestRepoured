package team.cqr.cqrepoured.world.structure;

import java.util.Random;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class StructurePieceCQR extends AbstractVillagePiece {

	protected final boolean createSupportHill;
	
	public StructurePieceCQR(TemplateManager p_i242037_1_, CompoundNBT p_i242037_2_) {
		super(p_i242037_1_, p_i242037_2_);
		this.createSupportHill = false;
	}
	
	
	private StructurePieceCQR(TemplateManager p_create_1_, JigsawPiece p_create_2_, BlockPos p_create_3_, int p_create_4_, Rotation p_create_5_, MutableBoundingBox p_create_6_, boolean supportHill) {
		super(p_create_1_, p_create_2_, p_create_3_, p_create_4_, p_create_5_, p_create_6_);
		this.createSupportHill = supportHill;
	}
	
	public static StructurePieceCQR createWithSupportHill(TemplateManager p_create_1_, JigsawPiece p_create_2_, BlockPos p_create_3_, int p_create_4_, Rotation p_create_5_, MutableBoundingBox p_create_6_) {
		return new StructurePieceCQR(p_create_1_, p_create_2_, p_create_3_, p_create_4_, p_create_5_, p_create_6_, true);
	}
	
	public static StructurePieceCQR createWithoutSupportHill(TemplateManager p_create_1_, JigsawPiece p_create_2_, BlockPos p_create_3_, int p_create_4_, Rotation p_create_5_, MutableBoundingBox p_create_6_) {
		return new StructurePieceCQR(p_create_1_, p_create_2_, p_create_3_, p_create_4_, p_create_5_, p_create_6_, false);
	}
	
	@Override
	public boolean postProcess(ISeedReader pLevel, StructureManager pStructureManager, ChunkGenerator pChunkGenerator, Random pRandom, MutableBoundingBox pBox, ChunkPos pChunkPos, BlockPos pPos) {
		if(!super.postProcess(pLevel, pStructureManager, pChunkGenerator, pRandom, pBox, pChunkPos, pPos)) {
			return false;
		}
		
		//Now, try to generate a support hill...
		if(this.createSupportHill) {
			
		}
		
		return true;
	}

}
