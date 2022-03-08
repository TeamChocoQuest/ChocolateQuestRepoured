package team.cqr.cqrepoured.init;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.loot.LootTables;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.block.BlockBossBlock;
import team.cqr.cqrepoured.block.BlockDungeonBrick;
import team.cqr.cqrepoured.block.BlockExporter;
import team.cqr.cqrepoured.block.BlockExporterChest;
import team.cqr.cqrepoured.block.BlockExporterChestCQR;
import team.cqr.cqrepoured.block.BlockExporterChestCustom;
import team.cqr.cqrepoured.block.BlockForceFieldNexus;
import team.cqr.cqrepoured.block.BlockMapPlaceholder;
import team.cqr.cqrepoured.block.BlockNull;
import team.cqr.cqrepoured.block.BlockPhylactery;
import team.cqr.cqrepoured.block.BlockPillarDungeonBrick;
import team.cqr.cqrepoured.block.BlockSpawner;
import team.cqr.cqrepoured.block.BlockTNTCQR;
import team.cqr.cqrepoured.block.BlockTable;
import team.cqr.cqrepoured.block.BlockTemporaryWeb;
import team.cqr.cqrepoured.block.BlockUnlitTorch;

public class CQRBlocks {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CQRMain.MODID);

	// Dungeon Blocks
	// Andesite
	public static final RegistryObject<BlockDungeonBrick> ANDESITE_CARVED = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockPillarDungeonBrick> ANDESITE_PILLAR = BLOCKS.register("", BlockPillarDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> ANDESITE_CUBE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> ANDESITE_SCALE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> ANDESITE_SQUARE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> ANDESITE_SMALL = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> ANDESITE_LARGE = BLOCKS.register("", BlockDungeonBrick::new);

	// Diorite
	public static final RegistryObject<BlockDungeonBrick> DIORITE_CARVED = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockPillarDungeonBrick> DIORITE_PILLAR = BLOCKS.register("", BlockPillarDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> DIORITE_CUBE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> DIORITE_SCALE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> DIORITE_SQUARE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> DIORITE_SMALL = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> DIORITE_LARGE = BLOCKS.register("", BlockDungeonBrick::new);

	// Granite
	public static final RegistryObject<BlockDungeonBrick> GRANITE_CARVED = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockPillarDungeonBrick> GRANITE_PILLAR = BLOCKS.register("", BlockPillarDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> GRANITE_CUBE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> GRANITE_SCALE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> GRANITE_SQUARE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> GRANITE_SMALL = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> GRANITE_LARGE = BLOCKS.register("", BlockDungeonBrick::new);

	// Prismarine
	public static final RegistryObject<BlockDungeonBrick> PRISMARINE_CARVED = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockPillarDungeonBrick> PRISMARINE_PILLAR = BLOCKS.register("", BlockPillarDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> PRISMARINE_CUBE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> PRISMARINE_SQUARE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> PRISMARINE_SMALL = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> PRISMARINE_LARGE = BLOCKS.register("", BlockDungeonBrick::new);

	// Endstone
	public static final RegistryObject<BlockDungeonBrick> ENDSTONE_CARVED = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockPillarDungeonBrick> ENDSTONE_PILLAR = BLOCKS.register("", BlockPillarDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> ENDSTONE_CUBE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> ENDSTONE_SCALE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> ENDSTONE_SQUARE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> ENDSTONE_SMALL = BLOCKS.register("", BlockDungeonBrick::new);

	// Purpur
	public static final RegistryObject<BlockDungeonBrick> PURPUR_CARVED = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> PURPUR_CUBE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> PURPUR_SCALE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> PURPUR_SMALL = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> PURPUR_LARGE = BLOCKS.register("", BlockDungeonBrick::new);

	// Red Netherbrick
	public static final RegistryObject<BlockDungeonBrick> RED_NETHERBRICK_CARVED = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockPillarDungeonBrick> RED_NETHERBRICK_PILLAR = BLOCKS.register("", BlockPillarDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> RED_NETHERBRICK_CUBE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> RED_NETHERBRICK_SCALE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> RED_NETHERBRICK_SQUARE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> RED_NETHERBRICK_LARGE = BLOCKS.register("", BlockDungeonBrick::new);

	// Stone
	public static final RegistryObject<BlockPillarDungeonBrick> STONE_PILLAR = BLOCKS.register("", BlockPillarDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> STONE_CUBE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> STONE_SCALE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> STONE_SQUARE = BLOCKS.register("", BlockDungeonBrick::new);
	public static final RegistryObject<BlockDungeonBrick> STONE_SMALL = BLOCKS.register("", BlockDungeonBrick::new);

	// Other
	public static final RegistryObject<BlockTable> TABLE_OAK = BLOCKS.register("", BlockTable::new);
	public static final RegistryObject<BlockTable> TABLE_SPRUCE = BLOCKS.register("", BlockTable::new);
	public static final RegistryObject<BlockTable> TABLE_BIRCH = BLOCKS.register("", BlockTable::new);
	public static final RegistryObject<BlockTable> TABLE_JUNGLE = BLOCKS.register("", BlockTable::new);
	public static final RegistryObject<BlockTable> TABLE_ACACIA = BLOCKS.register("", BlockTable::new);
	public static final RegistryObject<BlockTable> TABLE_DARK = BLOCKS.register("", BlockTable::new);
	public static final RegistryObject<BlockUnlitTorch> UNLIT_TORCH = BLOCKS.register("", BlockUnlitTorch::new);

	// Creative
	public static final RegistryObject<BlockExporter> EXPORTER = BLOCKS.register("", BlockExporter::new);
	public static final RegistryObject<BlockNull> NULL_BLOCK = BLOCKS.register("", BlockNull::new);
	public static final RegistryObject<BlockSpawner> SPAWNER = BLOCKS.register("", BlockSpawner::new);
	public static final RegistryObject<BlockBossBlock> BOSS_BLOCK = BLOCKS.register("", BlockBossBlock::new);
	public static final RegistryObject<BlockForceFieldNexus> FORCE_FIELD_NEXUS = BLOCKS.register("", BlockForceFieldNexus::new);
	public static final RegistryObject<BlockMapPlaceholder> MAP_PLACEHOLDER = BLOCKS.register("", BlockMapPlaceholder::new);
	public static final RegistryObject<BlockTNTCQR> TNT = BLOCKS.register("", BlockTNTCQR::new);

	// Loot Chests
	// CQR
	public static final RegistryObject<BlockExporterChestCQR> EXPORTER_CHEST_VALUABLE = BLOCKS.register("", () -> new BlockExporterChestCQR(CQRLoottables.CHESTS_TREASURE, "textures/items/diamond.png"));
	public static final RegistryObject<BlockExporterChestCQR> EXPORTER_CHEST_FOOD = BLOCKS.register("", () -> new BlockExporterChestCQR(CQRLoottables.CHESTS_FOOD, "textures/items/porkchop_raw.png"));
	public static final RegistryObject<BlockExporterChestCQR> EXPORTER_CHEST_EQUIPMENT = BLOCKS.register("", () -> new BlockExporterChestCQR(CQRLoottables.CHESTS_EQUIPMENT, "textures/items/iron_pickaxe.png"));
	public static final RegistryObject<BlockExporterChestCQR> EXPORTER_CHEST_UTILITY = BLOCKS.register("", () -> new BlockExporterChestCQR(CQRLoottables.CHESTS_MATERIAL, "textures/items/iron_ingot.png"));
	public static final RegistryObject<BlockExporterChestCQR> EXPORTER_CHEST_CLUTTER = BLOCKS.register("", () -> new BlockExporterChestCQR(CQRLoottables.CHESTS_CLUTTER, "textures/items/gunpowder.png"));

	// Custom
	public static final RegistryObject<BlockExporterChestCustom> EXPORTER_CHEST_CUSTOM = BLOCKS.register("", () -> new BlockExporterChestCustom("textures/items/blaze_rod.png"));

	// Vanilla
	public static final Set<RegistryObject<? extends BlockExporterChest>> VANILLA_LOOT_CHESTS = new HashSet<>();

	// Technical
	public static final RegistryObject<BlockPhylactery> PHYLACTERY = BLOCKS.register("", BlockPhylactery::new);
	public static final RegistryObject<BlockTemporaryWeb> TEMPORARY_WEB = BLOCKS.register("", BlockTemporaryWeb::new);

	public static void registerBlocks() {
		LootTables.all().stream()
				.filter(lootTable -> lootTable.getNamespace().equals("minecraft"))
				.filter(lootTable -> lootTable.getPath().contains("chest"))
				.map(lootTable -> BLOCKS.register(lootTable.getPath().substring(lootTable.getPath().lastIndexOf('/') + 1), () -> new BlockExporterChestCQR(lootTable)))
				.forEach(VANILLA_LOOT_CHESTS::add);

		// TODO override vanilla fire block

		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

}
