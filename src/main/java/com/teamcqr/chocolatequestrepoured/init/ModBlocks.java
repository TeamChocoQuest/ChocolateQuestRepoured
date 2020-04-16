package com.teamcqr.chocolatequestrepoured.init;

import static com.teamcqr.chocolatequestrepoured.util.InjectionUtil.Null;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockBossBlock;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockDungeonBrick;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockExporter;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockExporterChest;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockFireCQR;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockForceFieldNexus;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockNull;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockPhylactery;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockPillarDungeonBrick;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockSpawner;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockTable;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockUnlitTorch;
import com.teamcqr.chocolatequestrepoured.structuregen.lootchests.ELootTable;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporterChest;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityForceFieldNexus;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityTable;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(Reference.MODID)
public class ModBlocks {

	// Dungeon Blocks
	// Andesite
	public static final BlockDungeonBrick ANDESITE_CARVED = Null();
	public static final BlockPillarDungeonBrick ANDESITE_PILLAR = Null();
	public static final BlockDungeonBrick ANDESITE_CUBE = Null();
	public static final BlockDungeonBrick ANDESITE_SCALE = Null();
	public static final BlockDungeonBrick ANDESITE_SQUARE = Null();
	public static final BlockDungeonBrick ANDESITE_SMALL = Null();
	public static final BlockDungeonBrick ANDESITE_LARGE = Null();

	// Diorite
	public static final BlockDungeonBrick DIORITE_CARVED = Null();
	public static final BlockPillarDungeonBrick DIORITE_PILLAR = Null();
	public static final BlockDungeonBrick DIORITE_CUBE = Null();
	public static final BlockDungeonBrick DIORITE_SCALE = Null();
	public static final BlockDungeonBrick DIORITE_SQUARE = Null();
	public static final BlockDungeonBrick DIORITE_SMALL = Null();
	public static final BlockDungeonBrick DIORITE_LARGE = Null();

	// Granite
	public static final BlockDungeonBrick GRANITE_CARVED = Null();
	public static final BlockPillarDungeonBrick GRANITE_PILLAR = Null();
	public static final BlockDungeonBrick GRANITE_CUBE = Null();
	public static final BlockDungeonBrick GRANITE_SCALE = Null();
	public static final BlockDungeonBrick GRANITE_SQUARE = Null();
	public static final BlockDungeonBrick GRANITE_SMALL = Null();
	public static final BlockDungeonBrick GRANITE_LARGE = Null();

	// Prismarine
	public static final BlockDungeonBrick PRISMARINE_CARVED = Null();
	public static final BlockPillarDungeonBrick PRISMARINE_PILLAR = Null();
	public static final BlockDungeonBrick PRISMARINE_CUBE = Null();
	public static final BlockDungeonBrick PRISMARINE_SQUARE = Null();
	public static final BlockDungeonBrick PRISMARINE_SMALL = Null();
	public static final BlockDungeonBrick PRISMARINE_LARGE = Null();

	// Endstone
	public static final BlockDungeonBrick ENDSTONE_CARVED = Null();
	public static final BlockPillarDungeonBrick ENDSTONE_PILLAR = Null();
	public static final BlockDungeonBrick ENDSTONE_CUBE = Null();
	public static final BlockDungeonBrick ENDSTONE_SCALE = Null();
	public static final BlockDungeonBrick ENDSTONE_SQUARE = Null();
	public static final BlockDungeonBrick ENDSTONE_SMALL = Null();

	// Purpur
	public static final BlockDungeonBrick PURPUR_CARVED = Null();
	public static final BlockDungeonBrick PURPUR_CUBE = Null();
	public static final BlockDungeonBrick PURPUR_SCALE = Null();
	public static final BlockDungeonBrick PURPUR_SMALL = Null();
	public static final BlockDungeonBrick PURPUR_LARGE = Null();

	// Red Netherbrick
	public static final BlockDungeonBrick RED_NETHERBRICK_CARVED = Null();
	public static final BlockPillarDungeonBrick RED_NETHERBRICK_PILLAR = Null();
	public static final BlockDungeonBrick RED_NETHERBRICK_CUBE = Null();
	public static final BlockDungeonBrick RED_NETHERBRICK_SCALE = Null();
	public static final BlockDungeonBrick RED_NETHERBRICK_SQUARE = Null();
	public static final BlockDungeonBrick RED_NETHERBRICK_LARGE = Null();

	// Stone
	public static final BlockPillarDungeonBrick STONE_PILLAR = Null();
	public static final BlockDungeonBrick STONE_CUBE = Null();
	public static final BlockDungeonBrick STONE_SCALE = Null();
	public static final BlockDungeonBrick STONE_SQUARE = Null();
	public static final BlockDungeonBrick STONE_SMALL = Null();

	// Other
	public static final BlockTable TABLE_OAK = Null();
	public static final BlockTable TABLE_SPRUCE = Null();
	public static final BlockTable TABLE_BIRCH = Null();
	public static final BlockTable TABLE_JUNGLE = Null();
	public static final BlockTable TABLE_ACACIA = Null();
	public static final BlockTable TABLE_DARK = Null();

	// Utility
	public static final BlockExporter EXPORTER = Null();
	public static final BlockUnlitTorch UNLIT_TORCH = Null();
	public static final BlockNull NULL_BLOCK = Null();
	public static final BlockSpawner SPAWNER = Null();
	public static final BlockBossBlock BOSS_BLOCK = Null();

	// Loot Chests
	// Standard
	public static final BlockExporterChest EXPORTER_CHEST_VALUABLE = Null();
	public static final BlockExporterChest EXPORTER_CHEST_FOOD = Null();
	public static final BlockExporterChest EXPORTER_CHEST_EQUIPMENT = Null();
	public static final BlockExporterChest EXPORTER_CHEST_UTILITY = Null();

	// Vanilla
	public static final BlockExporterChest EXPORTER_CHEST_VANILLA_BLACKSMITH = Null();
	public static final BlockExporterChest EXPORTER_CHEST_VANILLA_BONUS = Null();
	public static final BlockExporterChest EXPORTER_CHEST_VANILLA_DUNGEON = Null();
	public static final BlockExporterChest EXPORTER_CHEST_VANILLA_END_CITY = Null();
	public static final BlockExporterChest EXPORTER_CHEST_VANILLA_IGLOO = Null();
	public static final BlockExporterChest EXPORTER_CHEST_VANILLA_JUNGLE = Null();
	public static final BlockExporterChest EXPORTER_CHEST_VANILLA_JUNGLE_DISPENSER = Null();
	public static final BlockExporterChest EXPORTER_CHEST_VANILLA_MANSION = Null();
	public static final BlockExporterChest EXPORTER_CHEST_VANILLA_MINESHAFT = Null();
	public static final BlockExporterChest EXPORTER_CHEST_VANILLA_NETHER = Null();
	public static final BlockExporterChest EXPORTER_CHEST_VANILLA_PYRAMID = Null();
	public static final BlockExporterChest EXPORTER_CHEST_VANILLA_STRONGHOLD = Null();
	public static final BlockExporterChest EXPORTER_CHEST_VANILLA_STRONGHOLD_LIBRARY = Null();
	public static final BlockExporterChest EXPORTER_CHEST_VANILLA_STRONGHOLD_STOREROOM = Null();

	// Custom / User
	public static final BlockExporterChest EXPORTER_CHEST_CUSTOM_1 = Null();
	public static final BlockExporterChest EXPORTER_CHEST_CUSTOM_2 = Null();
	public static final BlockExporterChest EXPORTER_CHEST_CUSTOM_3 = Null();
	public static final BlockExporterChest EXPORTER_CHEST_CUSTOM_4 = Null();
	public static final BlockExporterChest EXPORTER_CHEST_CUSTOM_5 = Null();
	public static final BlockExporterChest EXPORTER_CHEST_CUSTOM_6 = Null();
	public static final BlockExporterChest EXPORTER_CHEST_CUSTOM_7 = Null();
	public static final BlockExporterChest EXPORTER_CHEST_CUSTOM_8 = Null();
	public static final BlockExporterChest EXPORTER_CHEST_CUSTOM_9 = Null();
	public static final BlockExporterChest EXPORTER_CHEST_CUSTOM_10 = Null();
	public static final BlockExporterChest EXPORTER_CHEST_CUSTOM_11 = Null();
	public static final BlockExporterChest EXPORTER_CHEST_CUSTOM_12 = Null();
	public static final BlockExporterChest EXPORTER_CHEST_CUSTOM_13 = Null();
	public static final BlockExporterChest EXPORTER_CHEST_CUSTOM_14 = Null();

	// Protected region
	public static final BlockForceFieldNexus FORCE_FIELD_NEXUS = Null();

	// Technical
	public static final BlockPhylactery PHYLACTERY = Null();

	@EventBusSubscriber(modid = Reference.MODID)
	public static class BlockRegistrationHandler {

		public static final List<Block> BLOCKS = new ArrayList<>();
		public static final List<ItemBlock> ITEM_BLOCKS = new ArrayList<>();

		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			final Block[] blocks = {
					setBlockName(new BlockDungeonBrick(), "andesite_carved"),
					setBlockName(new BlockPillarDungeonBrick(), "andesite_pillar"),
					setBlockName(new BlockDungeonBrick(), "andesite_cube"),
					setBlockName(new BlockDungeonBrick(), "andesite_scale"),
					setBlockName(new BlockDungeonBrick(), "andesite_square"),
					setBlockName(new BlockDungeonBrick(), "andesite_small"),
					setBlockName(new BlockDungeonBrick(), "andesite_large"),

					setBlockName(new BlockDungeonBrick(), "diorite_carved"),
					setBlockName(new BlockPillarDungeonBrick(), "diorite_pillar"),
					setBlockName(new BlockDungeonBrick(), "diorite_cube"),
					setBlockName(new BlockDungeonBrick(), "diorite_scale"),
					setBlockName(new BlockDungeonBrick(), "diorite_square"),
					setBlockName(new BlockDungeonBrick(), "diorite_small"),
					setBlockName(new BlockDungeonBrick(), "diorite_large"),

					setBlockName(new BlockDungeonBrick(), "granite_carved"),
					setBlockName(new BlockPillarDungeonBrick(), "granite_pillar"),
					setBlockName(new BlockDungeonBrick(), "granite_cube"),
					setBlockName(new BlockDungeonBrick(), "granite_scale"),
					setBlockName(new BlockDungeonBrick(), "granite_square"),
					setBlockName(new BlockDungeonBrick(), "granite_small"),
					setBlockName(new BlockDungeonBrick(), "granite_large"),

					setBlockName(new BlockDungeonBrick(), "prismarine_carved"),
					setBlockName(new BlockPillarDungeonBrick(), "prismarine_pillar"),
					setBlockName(new BlockDungeonBrick(), "prismarine_cube"),
					setBlockName(new BlockDungeonBrick(), "prismarine_square"),
					setBlockName(new BlockDungeonBrick(), "prismarine_small"),
					setBlockName(new BlockDungeonBrick(), "prismarine_large"),

					setBlockName(new BlockDungeonBrick(), "endstone_carved"),
					setBlockName(new BlockPillarDungeonBrick(), "endstone_pillar"),
					setBlockName(new BlockDungeonBrick(), "endstone_cube"),
					setBlockName(new BlockDungeonBrick(), "endstone_scale"),
					setBlockName(new BlockDungeonBrick(), "endstone_square"),
					setBlockName(new BlockDungeonBrick(), "endstone_small"),

					setBlockName(new BlockDungeonBrick(), "purpur_carved"),
					setBlockName(new BlockDungeonBrick(), "purpur_cube"),
					setBlockName(new BlockDungeonBrick(), "purpur_scale"),
					setBlockName(new BlockDungeonBrick(), "purpur_small"),
					setBlockName(new BlockDungeonBrick(), "purpur_large"),

					setBlockName(new BlockDungeonBrick(), "red_netherbrick_carved"),
					setBlockName(new BlockPillarDungeonBrick(), "red_netherbrick_pillar"),
					setBlockName(new BlockDungeonBrick(), "red_netherbrick_cube"),
					setBlockName(new BlockDungeonBrick(), "red_netherbrick_scale"),
					setBlockName(new BlockDungeonBrick(), "red_netherbrick_square"),
					setBlockName(new BlockDungeonBrick(), "red_netherbrick_large"),

					setBlockName(new BlockPillarDungeonBrick(), "stone_pillar"),
					setBlockName(new BlockDungeonBrick(), "stone_cube"),
					setBlockName(new BlockDungeonBrick(), "stone_scale"),
					setBlockName(new BlockDungeonBrick(), "stone_square"),
					setBlockName(new BlockDungeonBrick(), "stone_small"),

					setBlockName(new BlockTable(), "table_oak"),
					setBlockName(new BlockTable(), "table_spruce"),
					setBlockName(new BlockTable(), "table_birch"),
					setBlockName(new BlockTable(), "table_jungle"),
					setBlockName(new BlockTable(), "table_acacia"),
					setBlockName(new BlockTable(), "table_dark"),

					setBlockName(new BlockExporter(), "exporter"),
					setBlockName(new BlockUnlitTorch(), "unlit_torch"),
					setBlockName(new BlockNull(true), "null_block"),
					setBlockName(new BlockSpawner(), "spawner"),
					setBlockName(new BlockBossBlock(), "boss_block"),

					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_TREASURE, "textures/items/diamond.png"), "exporter_chest_valuable", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_FOOD, "textures/items/porkchop_raw.png"), "exporter_chest_food", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_EQUIPMENT, "textures/items/iron_pickaxe.png"), "exporter_chest_equipment", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_MATERIAL, "textures/items/iron_ingot.png"), "exporter_chest_utility", CQRMain.CQR_EXPORTER_CHEST_TAB),

					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_VANILLA_BLACKSMITH, Reference.MODID, "textures/blocks/exporter_chest_overlays/vanilla_blacksmith.png"), "exporter_chest_vanilla_blacksmith", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_VANILLA_BONUS_CHEST, Reference.MODID, "textures/blocks/exporter_chest_overlays/vanilla_bonus.png"), "exporter_chest_vanilla_bonus", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_VANILLA_DUNGEON, Reference.MODID, "textures/blocks/exporter_chest_overlays/vanilla_dungeon.png"), "exporter_chest_vanilla_dungeon", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_VANILLA_END_CITY, Reference.MODID, "textures/blocks/exporter_chest_overlays/vanilla_end_city.png"), "exporter_chest_vanilla_end_city", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_VANILLA_IGLOO, Reference.MODID, "textures/blocks/exporter_chest_overlays/vanilla_igloo.png"), "exporter_chest_vanilla_igloo", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_VANILLA_JUNGLE, Reference.MODID, "textures/blocks/exporter_chest_overlays/vanilla_jungle.png"), "exporter_chest_vanilla_jungle", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_VANILLA_JUNGLE_DISPENSER, Reference.MODID, "textures/blocks/exporter_chest_overlays/vanilla_jungle_dispenser.png"), "exporter_chest_vanilla_jungle_dispenser", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_VANILLA_WOODLAND_MANSION, Reference.MODID, "textures/blocks/exporter_chest_overlays/vanilla_mansion.png"), "exporter_chest_vanilla_mansion", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_VANILLA_MINESHAFT, Reference.MODID, "textures/blocks/exporter_chest_overlays/vanilla_mineshaft.png"), "exporter_chest_vanilla_mineshaft", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_VANILLA_NETHER_FORTRESS, Reference.MODID, "textures/blocks/exporter_chest_overlays/vanilla_nether.png"), "exporter_chest_vanilla_nether", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_VANILLA_PYRAMID, Reference.MODID, "textures/blocks/exporter_chest_overlays/vanilla_pyramid.png"), "exporter_chest_vanilla_pyramid", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_VANILLA_STRONGHOLD_CORRIDOR, Reference.MODID, "textures/blocks/exporter_chest_overlays/vanilla_stronghold.png"), "exporter_chest_vanilla_stronghold", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_VANILLA_STRONGHOLD_LIBRARY, Reference.MODID, "textures/blocks/exporter_chest_overlays/vanilla_stronghold_library.png"), "exporter_chest_vanilla_stronghold_library", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_VANILLA_STRONGHOLD_STOREROOM, Reference.MODID, "textures/blocks/exporter_chest_overlays/vanilla_stronghold_storeroom.png"), "exporter_chest_vanilla_stronghold_storeroom", CQRMain.CQR_EXPORTER_CHEST_TAB),

					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_CUSTOM_1, "textures/items/blaze_powder.png"), "exporter_chest_custom_1", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_CUSTOM_2, "textures/items/wood_pickaxe.png"), "exporter_chest_custom_2", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_CUSTOM_3, "textures/items/book_normal.png"), "exporter_chest_custom_3", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_CUSTOM_4, "textures/items/rotten_flesh.png"), "exporter_chest_custom_4", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_CUSTOM_5, "textures/items/cookie.png"), "exporter_chest_custom_5", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_CUSTOM_6, "textures/items/emerald.png"), "exporter_chest_custom_6", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_CUSTOM_7, "textures/items/feather.png"), "exporter_chest_custom_7", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_CUSTOM_8, "textures/items/fishing_rod_uncast.png"), "exporter_chest_custom_8", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_CUSTOM_9, "textures/items/gold_ingot.png"), "exporter_chest_custom_9", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_CUSTOM_10, "textures/items/iron_chestplate.png"), "exporter_chest_custom_10", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_CUSTOM_11, "textures/items/iron_hoe.png"), "exporter_chest_custom_11", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_CUSTOM_12, "textures/items/potato.png"), "exporter_chest_custom_12", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_CUSTOM_13, Reference.MODID, "textures/items/potion_healing.png"), "exporter_chest_custom_13", CQRMain.CQR_EXPORTER_CHEST_TAB),
					setBlockNameAndTab(new BlockExporterChest(ELootTable.CQ_CUSTOM_14, "textures/items/record_strad.png"), "exporter_chest_custom_14", CQRMain.CQR_EXPORTER_CHEST_TAB),

					setBlockName(new BlockForceFieldNexus(Material.IRON), "force_field_nexus"),

					setBlockName(new BlockPhylactery(Material.GLASS), "phylactery"),
					new BlockFireCQR().setRegistryName(Blocks.FIRE.getRegistryName()).setUnlocalizedName(Blocks.FIRE.getUnlocalizedName()) };

			IForgeRegistry<Block> registry = event.getRegistry();

			for (Block block : blocks) {
				registry.register(block);
				BLOCKS.add(block);
			}

			registerTileEntities();
		}

		private static void registerTileEntities() {
			GameRegistry.registerTileEntity(TileEntityExporter.class, new ResourceLocation(Reference.MODID, "TileEntityExporter"));
			GameRegistry.registerTileEntity(TileEntityTable.class, new ResourceLocation(Reference.MODID, "TileEntityTable"));
			GameRegistry.registerTileEntity(TileEntitySpawner.class, new ResourceLocation(Reference.MODID, "TileEntitySpawner"));
			GameRegistry.registerTileEntity(TileEntityForceFieldNexus.class, new ResourceLocation(Reference.MODID, "TileEntityForceFieldNexus"));
			GameRegistry.registerTileEntity(TileEntityExporterChest.class, new ResourceLocation(Reference.MODID, "TileEntityExporterChest"));
		}

		private static Block setBlockName(Block block, String name) {
			return setBlockNameAndTab(block, name, CQRMain.CQR_BLOCKS_TAB);
		}

		private static Block setBlockNameAndTab(Block block, String name, @Nullable CreativeTabs tab) {
			return block.setUnlocalizedName(name).setRegistryName(Reference.MODID, name).setCreativeTab(tab);
		}

		@SubscribeEvent
		public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
			IForgeRegistry<Item> registry = event.getRegistry();

			for (Block block : BLOCKS) {
				ItemBlock itemBlock = createItemBlock(block);
				registry.register(itemBlock);
				ITEM_BLOCKS.add(itemBlock);
			}
		}

		private static ItemBlock createItemBlock(Block block) {
			return (ItemBlock) new ItemBlock(block).setUnlocalizedName(block.getLocalizedName()).setRegistryName(block.getRegistryName());
		}

	}

}
