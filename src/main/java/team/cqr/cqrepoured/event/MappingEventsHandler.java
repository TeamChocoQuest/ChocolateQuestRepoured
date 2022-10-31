package team.cqr.cqrepoured.event;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent.MissingMappings;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQRBlockEntities;
import team.cqr.cqrepoured.init.CQRBlocks;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MappingEventsHandler {

	@SubscribeEvent
	public static void onMissingBlockMappings(MissingMappings<Block> event) {
		ImmutableList<Mapping<Block>> entries = event.getMappings(CQRMain.MODID);
		if(entries.isEmpty()) {
			return;
		}
		entries.forEach(mapping -> {
			switch(mapping.key.toString().split(":")[0]) {
			case "null_block" :
				mapping.remap(CQRBlocks.NULL_BLOCK.get());
				break;
			default:
				mapping.warn();
				break;
			}
		});
	}
	
	@SubscribeEvent
	public static void onMissingBlockEntityMappings(final MissingMappings<TileEntityType<?>> event) {
		ImmutableList<Mapping<TileEntityType<?>>> entries = event.getMappings(CQRMain.MODID);
		if(entries.isEmpty()) {
			return;
		}
		entries.forEach(mapping -> {
			switch(mapping.key.toString().split(":")[1]) {
			case "tileentityspawner":
			case "TileEntitySpawner":
				mapping.remap(CQRBlockEntities.SPAWNER.get());
				break;
			case "tileentitytable":
			case "TileEntityTable":
				mapping.remap(CQRBlockEntities.TABLE.get());
				break;
			case "tileentityexporter":
			case "TileEntityExporter":
				mapping.remap(CQRBlockEntities.EXPORTER.get());
				break;
			case "tileentityforcefieldnexus":
			case "TileEntityForceFieldNexus":
				mapping.remap(CQRBlockEntities.FORCE_FIELD_NEXUS.get());
				break;
			case "tileentityexporterchestcqr":
			case "TileEntityExporterChestCQR":
				mapping.remap(CQRBlockEntities.EXPORTER_CHEST_CQR.get());
				break;
			case "tileentityexporterchestcustom":
			case "TileEntityExporterChestCustom":
				mapping.remap(CQRBlockEntities.EXPORTER_CHEST_CUSTOM.get());
				break;
			case "tileentityboss":
			case "TileEntityBoss":
				mapping.remap(CQRBlockEntities.BOSS.get());
				break;
			case "tileentitymapplaceholder":
			case "TileEntityMapPlaceholder":
				mapping.remap(CQRBlockEntities.MAP.get());
				break;
			default:
				mapping.warn();
				break;
			}
		});
	}
	
	@SubscribeEvent
	public static void onMissingItemMappings(MissingMappings<Item> event) {
		
	}
	
	@SubscribeEvent
	public static void onMissingEntityMappings(MissingMappings<EntityType<?>> event) {
		
	}
	
}
