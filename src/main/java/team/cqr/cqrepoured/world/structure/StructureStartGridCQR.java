package team.cqr.cqrepoured.world.structure;

import com.google.common.base.Optional;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.loading.FMLEnvironment;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager.DungeonSpawnType;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;
import team.cqr.cqrepoured.world.structure.generation.grid.DungeonGrid;

public class StructureStartGridCQR<T extends DungeonGrid> extends StructureStart<T> {

	private final DungeonBase dungeonObj;
	
	public StructureStartGridCQR(Structure<T> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn, final DungeonBase dungeonObj) {
        	super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        
        	this.dungeonObj = dungeonObj;
	}

	@Override
	public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, T config) {
		if(this.dungeonObj == null) {
			return;
		}
		
		//Y Position => If fixed, set last bool to true, that will force the y position
		//If it is set to false => Heightmap is used
		//Offsets: At the end, shift all structures 
		int shiftOffsetY = 0;
		int x = chunkX * 16;
		int z = chunkZ * 16;
		int y = 0;
		if(this.dungeonObj.isFixedY()) {
			y = DungeonGenUtils.randomBetween(this.dungeonObj.getYOffsetMin(), this.dungeonObj.getYOffsetMax(), this.random);
		} else {
			y = chunkGenerator.getBaseHeight(x, z, Type.WORLD_SURFACE_WG);
			shiftOffsetY = DungeonGenUtils.randomBetween(this.dungeonObj.getYOffsetMin(), this.dungeonObj.getYOffsetMax(), this.random);
		}
		shiftOffsetY -= this.dungeonObj.getUnderGroundOffset();
		ResourceLocation startPool = null;
		BlockPos centered = new BlockPos(x,y,z);

		//Run the dugneon generators...
		//this.pieces.add(this.dungeonObj.runGenerator(dynamicRegistryManager, chunkGenerator, templateManagerIn, centered.offset(0, shiftOffsetY, 0), random));
		Optional<ServerWorld> osw = StructureGridCQR.tryFindWorldForChunkGenerator(chunkGenerator);
		if(!FMLEnvironment.production && osw.isPresent()) {
			ServerWorld sw = osw.get();
			sw.getServer().getPlayerList().broadcastMessage(new StringTextComponent("Generated dungeon at: " + centered.toString()), ChatType.SYSTEM, Util.NIL_UUID);
		}
		this.pieces.add(this.dungeonObj.generate(dynamicRegistryManager, chunkGenerator, templateManagerIn, centered.offset(0, shiftOffsetY, 0), random, DungeonSpawnType.DUNGEON_GENERATION));
		
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
		//this.pieces.forEach(piece -> piece.move(0, tmpInt, 0));
		this.pieces.forEach(piece -> piece.getBoundingBox().y0 += tmpInt);
		this.pieces.forEach(piece -> piece.getBoundingBox().y1 += tmpInt);
		
		this.calculateBoundingBox();
	}
	
}
