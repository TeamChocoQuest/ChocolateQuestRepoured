package team.cqr.cqrepoured.world.template;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import team.cqr.cqrepoured.init.CQRBlockTags;
import team.cqr.cqrepoured.world.structure.generation.structurefile.BlockStatePalette;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;

public class ExtendedStructureTemplate extends StructureTemplate {
	
	private final CQStructure INNER_DATA_STRUCTURE = new CQStructure();
	
	public static final String CQR_VERSION_KEY = "cqr_file_version";
	
	protected final Set<BlockState> BLOCK_STATE_PALETTE = new HashSet<>();
	

	@Override
	public void fillFromWorld(Level pLevel, BlockPos pPos, Vec3i pSize, boolean pWithEntities, Block pToIgnore) {
		 if (pSize.getX() >= 1 && pSize.getY() >= 1 && pSize.getZ() >= 1) {
	         BlockPos blockpos = pPos.offset(pSize).offset(-1, -1, -1);
	         BlockPos blockpos1 = new BlockPos(Math.min(pPos.getX(), blockpos.getX()), Math.min(pPos.getY(), blockpos.getY()), Math.min(pPos.getZ(), blockpos.getZ()));
	         BlockPos blockpos2 = new BlockPos(Math.max(pPos.getX(), blockpos.getX()), Math.max(pPos.getY(), blockpos.getY()), Math.max(pPos.getZ(), blockpos.getZ()));
	         this.size = pSize;
	         
	         BlockStatePalette pallete = new BlockStatePalette();

	         int counter = 0;
	         for(BlockPos pos : BlockPos.betweenClosed(blockpos1, blockpos2)) {
	            BlockPos relativePos = pos.subtract(blockpos1);
	            BlockState blockstate = pLevel.getBlockState(pos);
	            
	            if (pToIgnore == null || !blockstate.is(CQRBlockTags.STRUCTURE_EXPORT_IGNORE)) {
	               BlockEntity blockentity = pLevel.getBlockEntity(pos);
	               StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo;
	               if (blockentity != null) {
	               }
	            }
	         }

	      }
	}

}
