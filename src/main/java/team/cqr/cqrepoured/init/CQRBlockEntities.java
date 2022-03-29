package team.cqr.cqrepoured.init;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.block.BlockExporterChestFixed;
import team.cqr.cqrepoured.block.BlockTable;
import team.cqr.cqrepoured.tileentity.*;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CQRBlockEntities {

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, CQRMain.MODID);

	public static final RegistryObject<TileEntityType<TileEntityExporter>> EXPORTER = register("exporter", TileEntityExporter::new, CQRBlocks.EXPORTER);
	public static final RegistryObject<TileEntityType<TileEntityTable>> TABLE = register("table", TileEntityTable::new, blocks(BlockTable.class));
	public static final RegistryObject<TileEntityType<TileEntitySpawner>> SPAWNER = register("spawner", TileEntitySpawner::new, CQRBlocks.SPAWNER);
	public static final RegistryObject<TileEntityType<TileEntityForceFieldNexus>> FORCE_FIELD_NEXUS = register("force_field_nexus", TileEntityForceFieldNexus::new, CQRBlocks.FORCE_FIELD_NEXUS);
	public static final RegistryObject<TileEntityType<TileEntityExporterChestFixed>> EXPORTER_CHEST_CQR = register("exporter_chest_cqr", TileEntityExporterChestFixed::new, blocks(BlockExporterChestFixed.class));
	public static final RegistryObject<TileEntityType<TileEntityExporterChestCustom>> EXPORTER_CHEST_CUSTOM = register("exporter_chest_custom", TileEntityExporterChestCustom::new, CQRBlocks.EXPORTER_CHEST_CUSTOM);
	public static final RegistryObject<TileEntityType<TileEntityBoss>> BOSS = register("boss", TileEntityBoss::new, CQRBlocks.BOSS_BLOCK);
	public static final RegistryObject<TileEntityType<TileEntityMap>> MAP = register("map", TileEntityMap::new, CQRBlocks.MAP_PLACEHOLDER);

	private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> s, Stream<RegistryObject<Block>> b) {
		return TILE_ENTITIES.register(name, () -> TileEntityType.Builder.of(s, b.map(Supplier::get).toArray(Block[]::new)).build(Util.fetchChoiceType(TypeReferences.BLOCK_ENTITY, name)));
	}

	@SuppressWarnings("unchecked")
	private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> s, RegistryObject<?>... b) {
		return register(name, s, Arrays.stream((RegistryObject<Block>[]) b));
	}

	private static Stream<RegistryObject<Block>> blocks(Class<? extends Block> blockClass) {
		return CQRBlocks.BLOCKS.getEntries().stream().filter(b -> blockClass.isInstance(b.get()));
	}

	public static void registerBlockEntities() {
		TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

}
