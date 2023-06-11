package team.cqr.cqrepoured.data;

import static team.cqr.cqrepoured.data.CQRDataGenerators.blockLoc;
import static team.cqr.cqrepoured.data.CQRDataGenerators.extend;
import static team.cqr.cqrepoured.data.CQRDataGenerators.itemLoc;
import static team.cqr.cqrepoured.data.CQRDataGenerators.valuesOfClass;
import static team.cqr.cqrepoured.data.CQRDataGenerators.valuesOfType;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder.Perspective;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
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
		valuesOfType(CQRBlocks.BLOCKS, BlockExporterChest.class).forEach(b -> {
			getBuilder(b.getRegistryName().getPath())
					.parent(new UncheckedModelFile("builtin/entity"))
					.texture("particle", blockLoc(Blocks.OAK_PLANKS.getRegistryName()))
					.transforms()
					.transform(Perspective.GUI).rotation(30, 45, 0).translation(0, 0, 0).scale(0.625F).end()
					.transform(Perspective.GROUND).rotation(0, 0, 0).translation(0, 3, 0).scale(0.25F).end()
					.transform(Perspective.HEAD).rotation(0, 180, 0).translation(0, 0, 0).scale(1.0F).end()
					.transform(Perspective.FIXED).rotation(0, 180, 0).translation(0, 0, 0).scale(0.5F).end()
					.transform(Perspective.THIRDPERSON_RIGHT).rotation(75, 315, 0).translation(0, 2.5F, 0).scale(0.375F).end()
					.transform(Perspective.FIRSTPERSON_RIGHT).rotation(0, 315, 0).translation(0, 0, 0).scale(0.4F).end()
					.end();
		});
		blockModel(CQRBlocks.PHYLACTERY.get());
		itemGenerated(CQRBlocks.POISONOUS_WEB.getId().getPath(), blockLoc(extend(CQRBlocks.POISONOUS_WEB.getId(), "_0")));
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
