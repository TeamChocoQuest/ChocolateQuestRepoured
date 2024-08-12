package team.cqr.cqrepoured.init;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.block.BlockBossBlock;
import team.cqr.cqrepoured.block.BlockExporter;
import team.cqr.cqrepoured.block.BlockExporterChest;
import team.cqr.cqrepoured.block.BlockExporterChestCustom;
import team.cqr.cqrepoured.block.BlockExporterChestFixed;
import team.cqr.cqrepoured.block.BlockForceFieldNexus;
import team.cqr.cqrepoured.block.BlockMapPlaceholder;
import team.cqr.cqrepoured.block.BlockNull;
import team.cqr.cqrepoured.block.BlockPhylactery;
import team.cqr.cqrepoured.block.BlockPoisonousWeb;
import team.cqr.cqrepoured.block.BlockSpawner;
import team.cqr.cqrepoured.block.BlockTNTCQR;
import team.cqr.cqrepoured.block.BlockTable;
import team.cqr.cqrepoured.block.BlockUnlitTorch;
import team.cqr.cqrepoured.block.BlockUnlitTorchWall;
import team.cqr.cqrepoured.client.render.tileentity.ItemStackExporterChestRenderer;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChestCustom;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChestFixed;

@EventBusSubscriber(modid = CQRConstants.MODID, bus = Bus.MOD)
public class CQRBlocks {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CQRConstants.MODID);

	// Dungeon Blocks
	// Andesite
	public static final RegistryObject<Block> ANDESITE_CARVED = register("andesite_carved", () -> new Block(Properties.copy(Blocks.ANDESITE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<RotatedPillarBlock> ANDESITE_PILLAR = register("andesite_pillar", () -> new RotatedPillarBlock(Properties.copy(Blocks.ANDESITE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> ANDESITE_CUBE = register("andesite_cube", () -> new Block(Properties.copy(Blocks.ANDESITE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> ANDESITE_SCALE = register("andesite_scale", () -> new Block(Properties.copy(Blocks.ANDESITE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> ANDESITE_SQUARE = register("andesite_square", () -> new Block(Properties.copy(Blocks.ANDESITE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> ANDESITE_SMALL = register("andesite_small", () -> new Block(Properties.copy(Blocks.ANDESITE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> ANDESITE_LARGE = register("andesite_large", () -> new Block(Properties.copy(Blocks.ANDESITE)), CQRMain.CQR_BLOCKS_TAB);

	// Diorite
	public static final RegistryObject<Block> DIORITE_CARVED = register("diorite_carved", () -> new Block(Properties.copy(Blocks.DIORITE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<RotatedPillarBlock> DIORITE_PILLAR = register("diorite_pillar", () -> new RotatedPillarBlock(Properties.copy(Blocks.DIORITE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> DIORITE_CUBE = register("diorite_cube", () -> new Block(Properties.copy(Blocks.DIORITE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> DIORITE_SCALE = register("diorite_scale", () -> new Block(Properties.copy(Blocks.DIORITE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> DIORITE_SQUARE = register("diorite_square", () -> new Block(Properties.copy(Blocks.DIORITE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> DIORITE_SMALL = register("diorite_small", () -> new Block(Properties.copy(Blocks.DIORITE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> DIORITE_LARGE = register("diorite_large", () -> new Block(Properties.copy(Blocks.DIORITE)), CQRMain.CQR_BLOCKS_TAB);

	// Granite
	public static final RegistryObject<Block> GRANITE_CARVED = register("granite_carved", () -> new Block(Properties.copy(Blocks.GRANITE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<RotatedPillarBlock> GRANITE_PILLAR = register("granite_pillar", () -> new RotatedPillarBlock(Properties.copy(Blocks.GRANITE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> GRANITE_CUBE = register("granite_cube", () -> new Block(Properties.copy(Blocks.GRANITE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> GRANITE_SCALE = register("granite_scale", () -> new Block(Properties.copy(Blocks.GRANITE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> GRANITE_SQUARE = register("granite_square", () -> new Block(Properties.copy(Blocks.GRANITE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> GRANITE_SMALL = register("granite_small", () -> new Block(Properties.copy(Blocks.GRANITE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> GRANITE_LARGE = register("granite_large", () -> new Block(Properties.copy(Blocks.GRANITE)), CQRMain.CQR_BLOCKS_TAB);

	// Prismarine
	public static final RegistryObject<Block> PRISMARINE_CARVED = register("prismarine_carved", () -> new Block(Properties.copy(Blocks.PRISMARINE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<RotatedPillarBlock> PRISMARINE_PILLAR = register("prismarine_pillar", () -> new RotatedPillarBlock(Properties.copy(Blocks.PRISMARINE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> PRISMARINE_CUBE = register("prismarine_cube", () -> new Block(Properties.copy(Blocks.PRISMARINE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> PRISMARINE_SQUARE = register("prismarine_square", () -> new Block(Properties.copy(Blocks.PRISMARINE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> PRISMARINE_SMALL = register("prismarine_small", () -> new Block(Properties.copy(Blocks.PRISMARINE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> PRISMARINE_LARGE = register("prismarine_large", () -> new Block(Properties.copy(Blocks.PRISMARINE)), CQRMain.CQR_BLOCKS_TAB);

	// Endstone
	public static final RegistryObject<Block> ENDSTONE_CARVED = register("endstone_carved", () -> new Block(Properties.copy(Blocks.END_STONE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<RotatedPillarBlock> ENDSTONE_PILLAR = register("endstone_pillar", () -> new RotatedPillarBlock(Properties.copy(Blocks.END_STONE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> ENDSTONE_CUBE = register("endstone_cube", () -> new Block(Properties.copy(Blocks.END_STONE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> ENDSTONE_SCALE = register("endstone_scale", () -> new Block(Properties.copy(Blocks.END_STONE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> ENDSTONE_SQUARE = register("endstone_square", () -> new Block(Properties.copy(Blocks.END_STONE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> ENDSTONE_SMALL = register("endstone_small", () -> new Block(Properties.copy(Blocks.END_STONE)), CQRMain.CQR_BLOCKS_TAB);

	// Purpur
	public static final RegistryObject<Block> PURPUR_CARVED = register("purpur_carved", () -> new Block(Properties.copy(Blocks.PURPUR_BLOCK)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> PURPUR_CUBE = register("purpur_cube", () -> new Block(Properties.copy(Blocks.PURPUR_BLOCK)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> PURPUR_SCALE = register("purpur_scale", () -> new Block(Properties.copy(Blocks.PURPUR_BLOCK)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> PURPUR_SMALL = register("purpur_small", () -> new Block(Properties.copy(Blocks.PURPUR_BLOCK)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> PURPUR_LARGE = register("purpur_large", () -> new Block(Properties.copy(Blocks.PURPUR_BLOCK)), CQRMain.CQR_BLOCKS_TAB);

	// Red Netherbrick
	public static final RegistryObject<Block> RED_NETHERBRICK_CARVED = register("red_netherbrick_carved", () -> new Block(Properties.copy(Blocks.RED_NETHER_BRICKS)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<RotatedPillarBlock> RED_NETHERBRICK_PILLAR = register("red_netherbrick_pillar", () -> new RotatedPillarBlock(Properties.copy(Blocks.RED_NETHER_BRICKS)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> RED_NETHERBRICK_CUBE = register("red_netherbrick_cube", () -> new Block(Properties.copy(Blocks.RED_NETHER_BRICKS)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> RED_NETHERBRICK_SCALE = register("red_netherbrick_scale", () -> new Block(Properties.copy(Blocks.RED_NETHER_BRICKS)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> RED_NETHERBRICK_SQUARE = register("red_netherbrick_square", () -> new Block(Properties.copy(Blocks.RED_NETHER_BRICKS)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> RED_NETHERBRICK_LARGE = register("red_netherbrick_large", () -> new Block(Properties.copy(Blocks.RED_NETHER_BRICKS)), CQRMain.CQR_BLOCKS_TAB);

	// Stone
	public static final RegistryObject<RotatedPillarBlock> STONE_PILLAR = register("stone_pillar", () -> new RotatedPillarBlock(Properties.copy(Blocks.STONE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> STONE_CUBE = register("stone_cube", () -> new Block(Properties.copy(Blocks.STONE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> STONE_SCALE = register("stone_scale", () -> new Block(Properties.copy(Blocks.STONE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> STONE_SQUARE = register("stone_square", () -> new Block(Properties.copy(Blocks.STONE)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<Block> STONE_SMALL = register("stone_small", () -> new Block(Properties.copy(Blocks.STONE)), CQRMain.CQR_BLOCKS_TAB);

	// Other
	//TODO: Register tables per wood type in the game dynamically
	public static final RegistryObject<BlockTable> TABLE_OAK = register("oak_table", () -> new BlockTable(Properties.copy(Blocks.OAK_PLANKS)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<BlockTable> TABLE_SPRUCE = register("spruce_table", () -> new BlockTable(Properties.copy(Blocks.SPRUCE_PLANKS)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<BlockTable> TABLE_BIRCH = register("birch_table", () -> new BlockTable(Properties.copy(Blocks.BIRCH_PLANKS)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<BlockTable> TABLE_JUNGLE = register("jungle_table", () -> new BlockTable(Properties.copy(Blocks.JUNGLE_PLANKS)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<BlockTable> TABLE_ACACIA = register("acacia_table", () -> new BlockTable(Properties.copy(Blocks.ACACIA_PLANKS)), CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<BlockTable> TABLE_DARK = register("dark_oak_table", () -> new BlockTable(Properties.copy(Blocks.DARK_OAK_PLANKS)), CQRMain.CQR_BLOCKS_TAB);

	//TODO: Maybe replace with a mixin into the torch block?
	public static final RegistryObject<BlockUnlitTorchWall> UNLIT_TORCH_WALL = register("unlit_torch_wall", BlockUnlitTorchWall::new);
	public static final RegistryObject<BlockUnlitTorch> UNLIT_TORCH = register("unlit_torch", BlockUnlitTorch::new, b -> new WallOrFloorItem(b, UNLIT_TORCH_WALL.get(), new Item.Properties().tab(CQRMain.CQR_BLOCKS_TAB)));

	// Creative
	public static final RegistryObject<BlockExporter> EXPORTER = register("exporter", BlockExporter::new, CQRMain.CQR_CREATIVE_TOOL_TAB);
	public static final RegistryObject<BlockSpawner> SPAWNER = register("spawner", BlockSpawner::new, CQRMain.CQR_CREATIVE_TOOL_TAB);
	public static final RegistryObject<BlockBossBlock> BOSS_BLOCK = register("boss_block", BlockBossBlock::new, CQRMain.CQR_CREATIVE_TOOL_TAB);
	public static final RegistryObject<BlockForceFieldNexus> FORCE_FIELD_NEXUS = register("force_field_nexus", BlockForceFieldNexus::new, CQRMain.CQR_CREATIVE_TOOL_TAB);
	public static final RegistryObject<BlockMapPlaceholder> MAP_PLACEHOLDER = register("map_placeholder", BlockMapPlaceholder::new, CQRMain.CQR_CREATIVE_TOOL_TAB);

	// Loot Chests
	// CQR
	public static final RegistryObject<BlockExporterChestFixed> EXPORTER_CHEST_VALUABLE = register("exporter_chest_valuable", () -> new BlockExporterChestFixed(CQRLoottables.CHESTS_TREASURE), b -> {
		return new BlockItem(b, new Item.Properties().tab(CQRMain.CQR_EXPORTER_CHEST_TAB).setISTER(() -> () -> new ItemStackExporterChestRenderer(TileEntityExporterChestFixed::new)));
	});
	public static final RegistryObject<BlockExporterChestFixed> EXPORTER_CHEST_FOOD = register("exporter_chest_food", () -> new BlockExporterChestFixed(CQRLoottables.CHESTS_FOOD), b -> {
		return new BlockItem(b, new Item.Properties().tab(CQRMain.CQR_EXPORTER_CHEST_TAB).setISTER(() -> () -> new ItemStackExporterChestRenderer(TileEntityExporterChestFixed::new)));
	});
	public static final RegistryObject<BlockExporterChestFixed> EXPORTER_CHEST_EQUIPMENT = register("exporter_chest_equipment", () -> new BlockExporterChestFixed(CQRLoottables.CHESTS_EQUIPMENT), b -> {
		return new BlockItem(b, new Item.Properties().tab(CQRMain.CQR_EXPORTER_CHEST_TAB).setISTER(() -> () -> new ItemStackExporterChestRenderer(TileEntityExporterChestFixed::new)));
	});
	public static final RegistryObject<BlockExporterChestFixed> EXPORTER_CHEST_UTILITY = register("exporter_chest_utility", () -> new BlockExporterChestFixed(CQRLoottables.CHESTS_MATERIAL), b -> {
		return new BlockItem(b, new Item.Properties().tab(CQRMain.CQR_EXPORTER_CHEST_TAB).setISTER(() -> () -> new ItemStackExporterChestRenderer(TileEntityExporterChestFixed::new)));
	});
	public static final RegistryObject<BlockExporterChestFixed> EXPORTER_CHEST_CLUTTER = register("exporter_chest_clutter", () -> new BlockExporterChestFixed(CQRLoottables.CHESTS_CLUTTER), b -> {
		return new BlockItem(b, new Item.Properties().tab(CQRMain.CQR_EXPORTER_CHEST_TAB).setISTER(() -> () -> new ItemStackExporterChestRenderer(TileEntityExporterChestFixed::new)));
	});

	// Custom
	public static final RegistryObject<BlockExporterChest> EXPORTER_CHEST_CUSTOM = register("exporter_chest_custom", BlockExporterChestCustom::new, b -> {
		return new BlockItem(b, new Item.Properties().tab(CQRMain.CQR_EXPORTER_CHEST_TAB).setISTER(() -> () -> new ItemStackExporterChestRenderer(TileEntityExporterChestCustom::new)));
	});

	// Vanilla
	public static final Set<RegistryObject<? extends BlockExporterChest>> VANILLA_LOOT_CHESTS = LootTables.all().stream()
			.filter(lootTable -> lootTable.getNamespace().equals("minecraft"))
			.filter(lootTable -> lootTable.getPath().contains("chest"))
			.map(lootTable -> {
				String p = lootTable.getPath();
				String n = "exporter_chest_vanilla_" + p.substring(p.lastIndexOf('/') + 1);
				return register(n, () -> new BlockExporterChestFixed(lootTable), b -> {
					return new BlockItem(b, new Item.Properties().tab(CQRMain.CQR_EXPORTER_CHEST_TAB).setISTER(() -> () -> new ItemStackExporterChestRenderer(TileEntityExporterChestFixed::new)));
				});
			})
			.collect(Collectors.toSet());

	// Technical
	public static final RegistryObject<BlockPhylactery> PHYLACTERY = register("phylactery", BlockPhylactery::new, CQRMain.CQR_BLOCKS_TAB);
	public static final RegistryObject<BlockPoisonousWeb> POISONOUS_WEB = register("poisonous_web", BlockPoisonousWeb::new, CQRMain.CQR_BLOCKS_TAB);

	private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier) {
		return register(name, blockSupplier, (Function<T, Item>) null);
	}

	private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, ItemGroup tab) {
		return register(name, blockSupplier, b -> new BlockItem(b, new Item.Properties().tab(tab)));
	}

	private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, Item> itemSupplier) {
		RegistryObject<T> registryObject = BLOCKS.register(name, blockSupplier);
		if (itemSupplier != null) {
			CQRItems.ITEMS.register(name, () -> itemSupplier.apply(registryObject.get()));
		}
		return registryObject;
	}

	public static void registerBlocks() {
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	@SubscribeEvent
	public static void setRenderLayers(RegistryEvent<Block> event) {
		RenderTypeLookup.setRenderLayer(CQRBlocks.BOSS_BLOCK.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(CQRBlocks.NULL_BLOCK.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(CQRBlocks.PHYLACTERY.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(CQRBlocks.SPAWNER.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(CQRBlocks.POISONOUS_WEB.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(CQRBlocks.UNLIT_TORCH.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(CQRBlocks.UNLIT_TORCH_WALL.get(), RenderType.cutout());
	}

	public static boolean never(BlockState state, IBlockReader blockReader, BlockPos pos, EntityType<?> entityType) {
		return false;
	}

	public static boolean never(BlockState state, IBlockReader blockReader, BlockPos pos) {
		return false;
	}

}
