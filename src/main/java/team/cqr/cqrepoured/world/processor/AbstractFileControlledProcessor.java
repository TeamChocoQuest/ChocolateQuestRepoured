package team.cqr.cqrepoured.world.processor;

import java.io.File;
import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;

public abstract class AbstractFileControlledProcessor extends StructureProcessor {

	protected IStructureProcessorType<?> type = null;
	
	private boolean valid;
	
	public AbstractFileControlledProcessor(File file) {
		super();
	}
	
	protected void validate(final File file) {
		File nonNullFile = Objects.requireNonNull(file);
		this.valid = this.parseFile(nonNullFile);
	}
	
	public void setType(final IStructureProcessorType<?> type) {
		if(this.type == null) {
			this.type = type;
		}
	}
	
	protected abstract boolean parseFile(@Nonnull File file);
	
	@Override
	protected IStructureProcessorType<?> getType() {
		return this.type;
	}
	
	@Override
	public BlockInfo process(IWorldReader worldReader, BlockPos jigsawPos, BlockPos jigsawPieceBottomCenterPos, BlockInfo blockInfoLocal, BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, Template template) {
		if(this.valid) {
			return this.execProcess(worldReader, jigsawPos, jigsawPieceBottomCenterPos, blockInfoLocal, blockInfoGlobal, structurePlacementData, template);
		}
		return blockInfoGlobal;
	}
	
	protected abstract BlockInfo execProcess(IWorldReader worldReader, BlockPos jigsawPos, BlockPos jigsawPieceBottomCenterPos, BlockInfo blockInfoLocal, BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, Template template);

}
