package team.cqr.cqrepoured.world.template;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;

public class ExtendedStructureTemplate extends Template {
	
	private final CQStructure INNER_DATA_STRUCTURE = new CQStructure();
	
	public static final String CQR_VERSION_KEY = "cqr_file_version";
	
	protected final Set<BlockState> BLOCK_STATE_PALETTE = new HashSet<>();
	
	@Override
	public void fillFromWorld(World world, BlockPos origin, BlockPos size, boolean saveEntities, Block blockToIgnore) {
		//Load from blocks in world
		super.fillFromWorld(world, origin, size, saveEntities, blockToIgnore);
	}
	
	@Override
	public List<BlockInfo> filterBlocks(BlockPos pPos, PlacementSettings pSettings, Block pBlock) {
		return super.filterBlocks(pPos, pSettings, pBlock);
	}
	
	//Gets called by method above
	//Generatse the blockinfos for placement => Performs rotations and translations and so on...
	@Override
	public List<BlockInfo> filterBlocks(BlockPos pPos, PlacementSettings pSettings, Block pBlock, boolean pRelativePosition) {
		return super.filterBlocks(pPos, pSettings, pBlock, pRelativePosition);
	}
	
	@Override
	public void placeInWorldChunk(IServerWorld p_237144_1_, BlockPos p_237144_2_, PlacementSettings p_237144_3_, Random p_237144_4_) {
		//Calls method below
		super.placeInWorldChunk(p_237144_1_, p_237144_2_, p_237144_3_, p_237144_4_);
	}
	
	@Override
	public void placeInWorld(IServerWorld p_237152_1_, BlockPos p_237152_2_, PlacementSettings p_237152_3_, Random p_237152_4_) {
		//Calls method below with flags = 2
		super.placeInWorld(p_237152_1_, p_237152_2_, p_237152_3_, p_237152_4_);
	}
	
	@Override
	public boolean placeInWorld(IServerWorld pServerLevel, BlockPos p_237146_2_, BlockPos p_237146_3_, PlacementSettings pSettings, Random pRandom, int pFlags) {
		//Returns true when successful
		return super.placeInWorld(pServerLevel, p_237146_2_, p_237146_3_, pSettings, pRandom, pFlags);
	}
	
	@Override
	public CompoundNBT save(CompoundNBT pTag) {
		//Saves to nbt file
		return super.save(pTag);
	}
	
	@Override
	public void load(CompoundNBT pTag) {
		//loads from nbt file
		super.load(pTag);
	}

}
