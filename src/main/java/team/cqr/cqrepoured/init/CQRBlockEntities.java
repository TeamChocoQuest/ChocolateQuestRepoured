package team.cqr.cqrepoured.init;

import java.util.stream.Stream;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.block.BlockExporterChestFixed;
import team.cqr.cqrepoured.block.BlockTable;
import team.cqr.cqrepoured.tileentity.BlockEntityPhylactery;
import team.cqr.cqrepoured.tileentity.TileEntityBoss;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChestCustom;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChestFixed;
import team.cqr.cqrepoured.tileentity.TileEntityForceFieldNexus;
import team.cqr.cqrepoured.tileentity.TileEntityMap;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;
import team.cqr.cqrepoured.tileentity.TileEntityTable;

public class CQRBlockEntities {

	public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CQRConstants.MODID);

	public static final RegistryObject<BlockEntityType<TileEntityExporter>> EXPORTER = register("exporter", TileEntityExporter::new, CQRBlocks.EXPORTER);
	public static final RegistryObject<BlockEntityType<TileEntityTable>> TABLE = register("table", TileEntityTable::new, blocks(BlockTable.class));
	public static final RegistryObject<BlockEntityType<TileEntitySpawner>> SPAWNER = register("spawner", TileEntitySpawner::new, CQRBlocks.SPAWNER);
	public static final RegistryObject<BlockEntityType<TileEntityForceFieldNexus>> FORCE_FIELD_NEXUS = register("force_field_nexus", TileEntityForceFieldNexus::new, CQRBlocks.FORCE_FIELD_NEXUS);
	public static final RegistryObject<BlockEntityType<TileEntityExporterChestFixed>> EXPORTER_CHEST_CQR = register("exporter_chest_cqr", TileEntityExporterChestFixed::new, blocks(BlockExporterChestFixed.class));
	public static final RegistryObject<BlockEntityType<TileEntityExporterChestCustom>> EXPORTER_CHEST_CUSTOM = register("exporter_chest_custom", TileEntityExporterChestCustom::new, CQRBlocks.EXPORTER_CHEST_CUSTOM);
	public static final RegistryObject<BlockEntityType<TileEntityBoss>> BOSS = register("boss", TileEntityBoss::new, CQRBlocks.BOSS_BLOCK);
	public static final RegistryObject<BlockEntityType<TileEntityMap>> MAP = register("map", TileEntityMap::new, CQRBlocks.MAP_PLACEHOLDER);

	public static final RegistryObject<BlockEntityType<BlockEntityPhylactery>> PHYLACTERY = register("phylactery", BlockEntityPhylactery::new, CQRBlocks.PHYLACTERY);
	
	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, RegistryObject<Block> b) {
		return TILE_ENTITIES.register(name, () -> BlockEntityType.Builder.of(supplier, b.get()).build(null));
	}

	private static Stream<RegistryObject<Block>> blocks(Class<? extends Block> blockClass) {
		return CQRBlocks.BLOCKS.getEntries().stream().filter(b -> blockClass.isInstance(b.get()));
	}

	public static void registerBlockEntities() {
		TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

}
