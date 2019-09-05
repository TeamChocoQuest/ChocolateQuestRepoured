package com.teamcqr.chocolatequestrepoured.structuregen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.objects.base.ItemDungeonPlacer;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.AbandonedDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CavernDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.ClassicNetherCity;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DefaultSurfaceDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonOceanFloor;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.FloatingNetherCity;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.RuinDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.StrongholdLinearDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.StrongholdOpenDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.VillageDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.VolcanoDungeon;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class DungeonRegistry {
	
	private HashMap<Biome, List<DungeonBase>> biomeDungeonMap = new HashMap<Biome, List<DungeonBase>>();;
	private HashMap<BlockPos, List<DungeonBase>> coordinateSpecificDungeons = new HashMap<BlockPos, List<DungeonBase>>();
	
	//DONE: Improve this method by splitting it into multiple smaller parts
	//DONE: It seems that choosing a random dungeon does not work how it should, rewrite this section or correct it
	public void loadDungeonFiles() {
		System.out.println("Loading dungeon configs...");
		if(CQRMain.CQ_DUNGEON_FOLDER.exists() && CQRMain.CQ_DUNGEON_FOLDER.listFiles().length > 0) {
			System.out.println("Found " + CQRMain.CQ_DUNGEON_FOLDER.listFiles().length + " dungeon configs. Loading...");
			System.out.println("Searching dungeons in " + CQRMain.CQ_DUNGEON_FOLDER.getAbsolutePath() );
			//DONE: Make config "search" recursive, so that it also search in sub folders
			for(File dungeonConfigurationFile : getAllFilesInFolder(CQRMain.CQ_DUNGEON_FOLDER)) {
				System.out.println("Loading dungeon configuration " + dungeonConfigurationFile.getName() + "...");
				Properties dungeonConfig = new Properties();
				FileInputStream stream = null;
				try {
					stream = new FileInputStream(dungeonConfigurationFile);
					
					dungeonConfig.load(stream);
					
					String dunType = dungeonConfig.getProperty("generator", "TEMPLATE_SURFACE");
					
					if(EDungeonGenerator.isValidDungeonGenerator(dunType)) {
						
						DungeonBase dungeon = getDungeonByType(dunType, dungeonConfigurationFile);
						
						if(dungeon != null) {
							//Position restriction stuff here
							
							dungeon = handleLockedPos(dungeon, dungeonConfig);
							//DONE: do biome map filling
							//Biome map filling
							String[] biomes = PropertyFileHelper.getStringArrayProperty(dungeonConfig, "biomes", new String[]{"PLAINS"});
							System.out.println("Biomes where " + dungeon.getDungeonName() + " can spawn: ");
							for(String b : biomes) {
								System.out.println(" - " + b);
								//Add the biome to the map
								if(b.equalsIgnoreCase("*") || b.equalsIgnoreCase("ALL")) {
									addDungeonToAllBiomes(dungeon);
								} else {
									if(getBiomeByName(b) != null) {
										BiomeDictionary.Type biomeType = getBiomeByName(b);
										System.out.println("Dungeon " + dungeon.getDungeonName() + " may spawn in biomes:");
										for(Biome biome : BiomeDictionary.getBiomes(biomeType)) {
											if(this.biomeDungeonMap.containsKey(biome)) {
												addDungeonToBiome(dungeon, biome);
											}
										}
										
									}
								}
							}
							
							if(dungeon.isRegisteredSuccessful()) {
								System.out.println("Successfully registered dungeon " + dungeon.getDungeonName() + "!");
								new ItemDungeonPlacer(dungeon);
							} else {
								System.out.println("Cant load dungeon " + dungeon.getDungeonName() + "!");
							}
							System.out.println(" ");
							System.out.println(" ");
							
						}
						
					} else {
						System.out.println("Cant load dungeon configuration " + dungeonConfigurationFile.getName() + "!");
						System.out.println("Dungeon generator " + dunType + " is not a valid dungeon generator!");
					}
					
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Failed to load dungeon configuration " + dungeonConfigurationFile.getName() + "!");
				} finally {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		} else {
			System.err.println("There are no dungeon configs :( ");
		}
	}
	
	private DungeonBase handleLockedPos(DungeonBase dungeon, Properties dungeonConfig) {
		BlockPos lockedPos = new BlockPos(0,0,0);
		boolean posLocked = PropertyFileHelper.getBooleanProperty(dungeonConfig, "spawnAtCertainPosition", false);
		if(posLocked) {
			if(dungeonConfig.containsKey("spawnAt")) {
				String[] args = dungeonConfig.getProperty("spawnAt", "-;-;-").split(";");
				if(args.length == 3) {
					try {
						int x = Integer.parseInt(args[0]);
						int y = Integer.parseInt(args[1]);
						int z = Integer.parseInt(args[2]);
						
						lockedPos = new BlockPos(x, y, z);
						
					} catch(NumberFormatException ex) {
						posLocked = false;
					}
				} else {
					posLocked = false;
				}
			}  else {
				posLocked = false;
			}
		}
		
		if(posLocked) {
			System.out.println("Dungeon " + dungeon.getDungeonName() + " will spawn at X=" + lockedPos.getX() + " Y=" + lockedPos.getY() + " Z=" + lockedPos.getZ());
			dungeon.setLockPos(lockedPos, posLocked);
		}
		
		return dungeon;
	}

	private Set<File> getAllFilesInFolder(File cQ_DUNGEON_FOLDER) {
		Set<File> files = new HashSet<>();
		
		for(File f : cQ_DUNGEON_FOLDER.listFiles()) {
			if(f.isDirectory()) {
				files.addAll(getAllFilesInFolder(f));
			} else {
				files.add(f);
			}
		}
		
		return files;
	}

	private DungeonBase getDungeonByType(String dunType, File dungeonPropertiesFile) {
		
		switch(EDungeonGenerator.valueOf(dunType.toUpperCase())) {
		case ABANDONED:
			return new AbandonedDungeon(dungeonPropertiesFile);
		case CASTLE:
			return new CastleDungeon(dungeonPropertiesFile);
		case CAVERNS:
			return new CavernDungeon(dungeonPropertiesFile);
		case FLOATING_NETHER_CITY:
			return new FloatingNetherCity(dungeonPropertiesFile);
		case NETHER_CITY:
			return new ClassicNetherCity(dungeonPropertiesFile);
		case RUIN:
			return new RuinDungeon(dungeonPropertiesFile);
		case STRONGHOLD:
			return new StrongholdOpenDungeon(dungeonPropertiesFile);
		case TEMPLATE_OCEAN_FLOOR:
			return new DungeonOceanFloor(dungeonPropertiesFile);
		case TEMPLATE_SURFACE:
			return new DefaultSurfaceDungeon(dungeonPropertiesFile);
		case VILLAGE:
			return new VillageDungeon(dungeonPropertiesFile);
		case VOLCANO:
			return new VolcanoDungeon(dungeonPropertiesFile);
		case CLASSIC_STRONGHOLD:
			return new StrongholdLinearDungeon(dungeonPropertiesFile);
		case JUNGLE_CAVE:
			//TODO Jungle cave generator
			break;
		case SWAMP_CAVE:
			//TODO SWAMP CAVE GENERATOR
			break;
		default:
			return null;
		}
		return null;
	}
	
	public List<DungeonBase> getDungeonsForBiome(Biome b) {
		if(b != null && biomeDungeonMap.containsKey(b) && !biomeDungeonMap.get(b).isEmpty()) {
			return biomeDungeonMap.get(b);
		}
		return new ArrayList<DungeonBase>();
	}
	
	public void addBiomeEntryToMap(Biome b) {
		if(biomeDungeonMap != null) {
			if(!biomeDungeonMap.containsKey(b)) {
				biomeDungeonMap.put(b, new ArrayList<DungeonBase>());
			}
		}
	}
	
	public int getDungeonDistance() {
		return Reference.CONFIG_HELPER_INSTANCE.getDungeonDistance();
	}
	public int getDungeonSpawnDistance() {
		return Reference.CONFIG_HELPER_INSTANCE.getDungeonSpawnDistance();
	}
	
	public HashMap<BlockPos, List<DungeonBase>> getCoordinateSpecificsMap() {
		return this.coordinateSpecificDungeons;
	}
	
	private void addDungeonToAllBiomes(DungeonBase dungeon) {
		System.out.println("Dungeon " + dungeon.getDungeonName() + " may spawn in biomes:");
		for(Biome biome : this.biomeDungeonMap.keySet()) {
			addDungeonToBiome(dungeon, biome);
		}
	}
	private void addDungeonToBiome(DungeonBase dungeon, Biome biome) {
		if(this.biomeDungeonMap.containsKey(biome)) {
			List<DungeonBase> dungs = this.biomeDungeonMap.get(biome);
			if(!dungs.contains(dungeon)) {
				dungs.add(dungeon);
				this.biomeDungeonMap.replace(biome, dungs);
				//System.out.println(" - " + biome.getRegistryName());
			}
		}
	}
	private BiomeDictionary.Type getBiomeByName(String biomeName) {
		for(BiomeDictionary.Type bType : BiomeDictionary.Type.getAll()) {
			if(biomeName.equalsIgnoreCase(bType.getName()) || biomeName.equalsIgnoreCase(bType.toString())) {
				return bType;
			}
		}
		return null;
	}

}
