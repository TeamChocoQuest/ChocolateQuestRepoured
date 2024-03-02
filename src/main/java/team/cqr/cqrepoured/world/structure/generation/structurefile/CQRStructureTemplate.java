package team.cqr.cqrepoured.world.structure.generation.structurefile;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.common.serialization.CodecUtil;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo;

public class CQRStructureTemplate extends StructureTemplate {
	
	// Vanilla generation order (each list is sorted after position in beforehand!): 
	// 1) normal blocks
	// 2) blocks with special shape => pBlockInfo.state.getBlock().hasDynamicShape() || !pBlockInfo.state.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)
	// 3) nbt blocks (world has tile entity at position
	
	protected final List<BlockPos> unprotectedBlocks = new ArrayList<>();
	
	protected String version = "";

	// Create codec instead?
	
	@Override
	public void load(HolderGetter<Block> pBlockGetter, CompoundTag pTag) {
		// First: Make sure that IS a cqr template file
		if (!pTag.contains(CQRConstants.NBT.StructureTemplate.KEY_CQR_FILE_VERSION, Tag.TAG_STRING)) {
			super.load(pBlockGetter, pTag);
		}
		this.version = pTag.getString(CQRConstants.NBT.StructureTemplate.KEY_CQR_FILE_VERSION);
		this.setAuthor(pTag.getString(CQRConstants.NBT.StructureTemplate.KEY_AUTHOR));
		CodecUtil.decodeNBT(Vec3i.CODEC, pTag.getCompound(CQRConstants.NBT.StructureTemplate.KEY_SIZE)).ifPresent(v -> {
			this.size = v;
		});
		// Load from NBT file
		// when loading the blocks => add them to individual lists
		// Sort the lists afterwards
		// Add them to the global list
		ListTag nbtTagList = pTag.getList(CQRConstants.NBT.StructureTemplate.KEY_NBT_TAG_LIST, Tag.TAG_COMPOUND);
		List<BlockState> blockstates = new ArrayList<>();
		CodecUtil.decodeNBT(BlockState.CODEC.listOf(), pTag.getCompound(CQRConstants.NBT.StructureTemplate.KEY_BLOCKSTATES_LIST)).ifPresent(l -> blockstates = l);
		
		ByteBuf dataBuffer = Unpooled.wrappedBuffer(pTag.getByteArray(CQRConstants.NBT.StructureTemplate.KEY_BLOCKDATA_BYTES));
		
		// Lists for vanilla
		List<StructureTemplate.StructureBlockInfo> normalBlocks = Lists.newArrayList();
        List<StructureTemplate.StructureBlockInfo> entityBlocks = Lists.newArrayList();
        List<StructureTemplate.StructureBlockInfo> specialShapeBlocks = Lists.newArrayList();
        
        for (int i = 0; i < this.getSize().getX() * this.getSize().getY() * this.getSize().getZ(); i++) {
			// TODO: Do we really need the preparers again? We shoudl replace those entirely with structure processors.
        	// We can keep them though... => Change them to return a StructureBlockInfo
		}
	}
	
	@Override
	public CompoundTag save(CompoundTag pTag) {
		return super.save(pTag);
		// Saving:
		//  - author
		//  - name
		//  - unprotected mask
		//  - entity list
		//  - cqr marker + cqr file version
		//  - size
		//  - nbt data
		//  - palette => unique block states
		//  - block info list => no position infos => pretty much just the index of the individual data position
	}
	
	public String getVersion() {
		return this.version;
	}
	
}
