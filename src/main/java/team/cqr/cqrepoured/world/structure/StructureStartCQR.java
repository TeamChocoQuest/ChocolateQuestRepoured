package team.cqr.cqrepoured.world.structure;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.world.CQRJigsawManager;
import team.cqr.cqrepoured.world.structure.generation.DungeonRegistry;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;

public class StructureStartCQR<T extends DungeonBase> extends StructureStart<T> {

	public StructureStartCQR(Structure<T> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
        super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
	}

	@Override
	public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, T config) {
		//Y Position => If fixed, set last bool to true, that will force the y position
		//If it is set to false => Heightmap is used
		//Offsets: At the end, shift all structures 
		int shiftOffsetY = 0;
		int x = chunkX * 16;
		int z = chunkZ * 16;
		int y = 0;
		if(config.isFixedY()) {
			y = DungeonGenUtils.randomBetween(config.getYOffsetMin(), config.getYOffsetMax(), this.random);
		} else {
			shiftOffsetY = DungeonGenUtils.randomBetween(config.getYOffsetMin(), config.getYOffsetMax(), this.random);
		}
		ResourceLocation startPool = null;
		BlockPos centered = new BlockPos(x,y,z);

		//Run the dugneon generators...
		this.pieces.add(config.runGenerator(dynamicRegistryManager, chunkGenerator, templateManagerIn, centered, random));
		
		/*CQRJigsawManager.addPieces(
				dynamicRegistryManager, 
				new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(startPool), 10),
				config.doBuildSupportPlatform() ? StructurePieceCQR::createWithSupportHill : StructurePieceCQR::createWithoutSupportHill,
				chunkGenerator,
				templateManagerIn,
				centered,
				this.pieces,
				this.random,
				false, 
				!config.isFixedY(),
				Rotation.NONE
		);*/
		final int tmpInt = shiftOffsetY;
		this.pieces.forEach(piece -> piece.move(0, tmpInt, 0));
		this.pieces.forEach(piece -> piece.getBoundingBox().y0 += tmpInt);
		this.pieces.forEach(piece -> piece.getBoundingBox().y1 += tmpInt);
		
		this.calculateBoundingBox();
	}
	
}
