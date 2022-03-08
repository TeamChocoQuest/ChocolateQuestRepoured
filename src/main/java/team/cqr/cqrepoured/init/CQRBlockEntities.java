package team.cqr.cqrepoured.init;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.block.BlockExporterChestCQR;
import team.cqr.cqrepoured.block.BlockTable;
import team.cqr.cqrepoured.tileentity.TileEntityBoss;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChestCQR;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChestCustom;
import team.cqr.cqrepoured.tileentity.TileEntityForceFieldNexus;
import team.cqr.cqrepoured.tileentity.TileEntityMap;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;
import team.cqr.cqrepoured.tileentity.TileEntityTable;

public class CQRBlockEntities {

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, CQRMain.MODID);

	public static final RegistryObject<TileEntityType<TileEntityExporter>> EXPORTER = register("exporter", TileEntityExporter::new, CQRBlocks.EXPORTER.get());
	public static final RegistryObject<TileEntityType<TileEntityTable>> TABLE = register("table", TileEntityTable::new, blocks(BlockTable.class));
	public static final RegistryObject<TileEntityType<TileEntitySpawner>> SPAWNER = register("spawner", TileEntitySpawner::new, CQRBlocks.SPAWNER.get());
	public static final RegistryObject<TileEntityType<TileEntityForceFieldNexus>> FORCE_FIELD_NEXUS = register("force_field_nexus", TileEntityForceFieldNexus::new, CQRBlocks.FORCE_FIELD_NEXUS.get());
	public static final RegistryObject<TileEntityType<TileEntityExporterChestCQR>> EXPORTER_CHEST_CQR = register("exporter_chest_cqr", TileEntityExporterChestCQR::new, blocks(BlockExporterChestCQR.class));
	public static final RegistryObject<TileEntityType<TileEntityExporterChestCustom>> EXPORTER_CHEST_CUSTOM = register("exporter_chest_custom", TileEntityExporterChestCustom::new, CQRBlocks.EXPORTER_CHEST_CUSTOM.get());
	public static final RegistryObject<TileEntityType<TileEntityBoss>> BOSS = register("boss", TileEntityBoss::new, CQRBlocks.BOSS_BLOCK.get());
	public static final RegistryObject<TileEntityType<TileEntityMap>> MAP = register("map", TileEntityMap::new, CQRBlocks.MAP_PLACEHOLDER.get());

	private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> s, Block... b) {
		return TILE_ENTITIES.register(name, () -> TileEntityType.Builder.of(s, b).build(Util.fetchChoiceType(TypeReferences.BLOCK_ENTITY, name)));
	}

	@SuppressWarnings("unused")
	private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> s, Collection<? extends Block> b) {
		return register(name, s, b.stream());
	}

	private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> s, Stream<? extends Block> b) {
		return register(name, s, b.toArray(Block[]::new));
	}

	private static Stream<Block> blocks(Class<? extends Block> blockClass) {
		return CQRBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(blockClass::isInstance);
	}

	public static void registerBlockEntities() {
		TILE_ENTITIES.register(MinecraftForge.EVENT_BUS);
	}

}
