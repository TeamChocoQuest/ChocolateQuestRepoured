package team.cqr.cqrepoured.data;

import static team.cqr.cqrepoured.data.CQRDataGenerators.*;

import net.minecraft.block.Block;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.block.BlockExporterChest;
import team.cqr.cqrepoured.block.BlockTable;
import team.cqr.cqrepoured.init.CQRBlocks;

public class CQRItemModelProvider extends ItemModelProvider {

	public CQRItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, CQRMain.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		registerBlockItemModels();
		registerItemModels();
	}

	private void registerBlockItemModels() {
		valuesOfClass(CQRBlocks.BLOCKS, Block.class).forEach(this::blockModel);
		valuesOfClass(CQRBlocks.BLOCKS, RotatedPillarBlock.class).forEach(this::blockModel);
		valuesOfClass(CQRBlocks.BLOCKS, BlockTable.class).forEach(this::blockModel);
		itemGenerated(CQRBlocks.UNLIT_TORCH.getId().getPath(), blockLoc(CQRBlocks.UNLIT_TORCH.getId()));
		blockModel(CQRBlocks.EXPORTER.get());
		blockModel(CQRBlocks.NULL_BLOCK.get());
		blockModel(CQRBlocks.SPAWNER.get());
		blockModel(CQRBlocks.BOSS_BLOCK.get());
		blockModel(CQRBlocks.FORCE_FIELD_NEXUS.get());
		blockModel(CQRBlocks.MAP_PLACEHOLDER.get());
		withExistingParent(CQRBlocks.TNT.getId().getPath(), blockLoc(extend(CQRBlocks.TNT.getId(), "_hidden")));
		valuesOfType(CQRBlocks.BLOCKS, BlockExporterChest.class).forEach(this::blockModel);
		blockModel(CQRBlocks.PHYLACTERY.get());
		itemGenerated(CQRBlocks.TEMPORARY_WEB.getId().getPath(), blockLoc(extend(CQRBlocks.TEMPORARY_WEB.getId(), "_0")));
	}

	private void registerItemModels() {

	}

	public void blockModel(Block block) {
		ResourceLocation registryName = block.getRegistryName();
		withExistingParent(registryName.getPath(), blockLoc(registryName));
	}

	public void itemGenerated(String name, ResourceLocation tex) {
		withExistingParent(name, itemLoc(mcLoc("generated"))).texture("layer0", tex);
	}

}
