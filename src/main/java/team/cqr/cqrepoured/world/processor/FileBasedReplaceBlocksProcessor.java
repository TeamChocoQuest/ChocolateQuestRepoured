package team.cqr.cqrepoured.world.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.command.arguments.BlockStateInput;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import team.cqr.cqrepoured.util.CQRWeightedRandom;

public class FileBasedReplaceBlocksProcessor extends AbstractFileControlledProcessor implements IReplaceBlocksProcessor {
	
	static final Gson GSON = new Gson();

	private final CQRWeightedRandom<BlockState> resultList;
	private final Set<BlockStateInput> inputList;
	
	public FileBasedReplaceBlocksProcessor(File file) {
		super(file);
		
		this.resultList = new CQRWeightedRandom<>();
		this.inputList = new HashSet<>();
		
		this.validate(file);
	}

	@Override
	public CQRWeightedRandom<BlockState> getResultingBlockStates() {
		return this.resultList;
	}

	@Override
	public Set<BlockStateInput> getFilters() {
		return this.inputList;
	}

	@Override
	protected boolean parseFile(File file) {
		if(file == null) {
			return false;
		}
		JsonReader reader;
		try {
			reader = new JsonReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		JsonElement je = new JsonParser().parse(reader);
		if(je.isJsonNull()) {
			return false;
		}
		if(!je.isJsonObject()) {
			return false;
		}
		
		JsonObject jo = je .getAsJsonObject();
		JsonArray inputs = jo.getAsJsonArray("inputs");
		inputs.forEach(jae -> {
			if(jae.isJsonPrimitive()) {
				if(!this.tryAddFilter(jae.getAsString())) {
					System.out.println("JSON entry " + jae.toString() + " couldn't be parsed!");
				}
			}
		});
		JsonArray outputs = jo.getAsJsonArray("outputs");
		outputs.forEach(jae -> {
			if(jae.isJsonObject()) {
				if(!this.tryAddResult(jae.getAsJsonObject())) {
					System.out.println("JSON entry " + jae.toString() + " couldn't be parsed!");
				}
			}
		});
		return true;
	}
	
	static class AccessedCachedBlockInfo extends CachedBlockInfo {

		final BlockInfo blockInfo;
		
		public AccessedCachedBlockInfo(BlockGetter pLevel, final BlockInfo bi) {
			super(pLevel, bi.pos, false);
			this.blockInfo = bi;
		}
		
		@Override
		public BlockState getState() {
			return this.blockInfo.state;
		};
		
		@Override
		public BlockEntity getEntity() {
			return null;
		}
		
	}

	@Override
	protected BlockInfo execProcess(BlockGetter worldReader, BlockPos jigsawPos, BlockPos jigsawPieceBottomCenterPos, BlockInfo blockInfoLocal, BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, Template template) {
		AccessedCachedBlockInfo cbi = new AccessedCachedBlockInfo(worldReader, blockInfoGlobal);
		this.inputList.forEach(bsi -> {
			if(bsi.test(cbi)) {
				worldReader.getChunk(cbi.getPos()).setBlockState(cbi.getPos(), this.getResultingBlockStates().next(structurePlacementData.getRandom(cbi.getPos())), false);
					
				return;
			}
		});
		return blockInfoGlobal;
	}

}
