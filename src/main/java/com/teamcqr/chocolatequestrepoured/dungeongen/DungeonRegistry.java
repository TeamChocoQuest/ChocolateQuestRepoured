package com.teamcqr.chocolatequestrepoured.dungeongen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.AbandonedDungeon;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.CavernDungeon;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.ClassicNetherCity;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.DefaultSurfaceDungeon;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.DungeonOceanFloor;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.FloatingNetherCity;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.RuinDungeon;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.StrongholdDungeon;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.VillageDungeon;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.VolcanoDungeon;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class DungeonRegistry {
	
	private int dungeonSpawnChance = 20;
	private int DungeonDistance = 125;
	private int DungeonSpawnDistance = 200;
	
	private HashMap<Biome, List<DungeonBase>> biomeDungeonMap = new HashMap<Biome, List<DungeonBase>>();;
	private HashMap<BlockPos, List<DungeonBase>> coordinateSpecificDungeons = new HashMap<BlockPos, List<DungeonBase>>();
	
	public void loadDungeonFiles() {
		System.out.println("Loading dungeon configs...");
		if(CQRMain.CQ_DUNGEON_FOLDER.exists() && CQRMain.CQ_DUNGEON_FOLDER.listFiles().length > 0) {
			System.out.println("Found " + CQRMain.CQ_DUNGEON_FOLDER.listFiles().length + " dungeon configs. Loading...");
			
			for(File f : CQRMain.CQ_DUNGEON_FOLDER.listFiles()) {
				System.out.println("Loading dungeon configuration " + f.getName() + "...");
				Properties dungeonConfig = new Properties();
				FileInputStream stream = null;
				try {
					stream = new FileInputStream(f);
					
					dungeonConfig.load(stream);
					
					String dunType = dungeonConfig.getProperty("generator", "TEMPLATE_SURFACE");
					
					if(EDungeonGenerator.isValidDungeonGenerator(dunType)) {
						
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
						
						DungeonBase dungeon = null;
						
						EDungeonGenerator generator = EDungeonGenerator.valueOf(dunType.toUpperCase());
						
						switch(generator) {
						case ABANDONED:
							dungeon = new AbandonedDungeon(f);
							break;
						case CAVERNS:
							dungeon = new CavernDungeon(f);
							break;
						case FLOATING_NETHER_CITY:
							dungeon = new FloatingNetherCity(f);
							break;
						case NETHER_CITY:
							dungeon = new ClassicNetherCity(f);
							break;
						case RUIN:
							dungeon = new RuinDungeon(f);
							break;
						case STRONGHOLD:
							dungeon = new StrongholdDungeon(f);
							break;
						case TEMPLATE_OCEAN_FLOOR:
							dungeon = new DungeonOceanFloor(f);
							break;
						case TEMPLATE_SURFACE:
							dungeon = new DefaultSurfaceDungeon(f);
							break;
						case VILLAGE:
							dungeon = new VillageDungeon(f);
							break;
						case VOLCANO:
							dungeon = new VolcanoDungeon(f);
							break;
						default:
							break;
						}
						
						if(dungeon != null) {
							//Position restriction stuff here
							if(posLocked) {
								dungeon.setLockPos(lockedPos, posLocked);
							}
							//TODO: do biome map filling
							//Biome map filling
						}
						
					} else {
						System.out.println("Cant load dungeon configuration " + f.getName() + "!");
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
					System.out.println("Failed to load dungeon configuration " + f.getName() + "!");
				} finally {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		} else {
			System.out.println("There are no dungeon configs :(");
		}
	}
	
	public List<DungeonBase> getDungeonsForBiome(Biome b) {
		if(b != null && biomeDungeonMap.containsKey(b) && !biomeDungeonMap.get(b).isEmpty()) {
			return biomeDungeonMap.get(b);
		}
		return new ArrayList<DungeonBase>();
	}
	
	public int getDungeonSpawnChance() {
		return dungeonSpawnChance;
	}
	
	public void addBiomeEntryToMap(Biome b) {
		if(biomeDungeonMap != null) {
			if(!biomeDungeonMap.containsKey(b)) {
				biomeDungeonMap.put(b, new ArrayList<DungeonBase>());
			}
		}
	}
	
	public int getDungeonDistance() {
		return DungeonDistance;
	}
	public int getDungeonSpawnDistance() {
		return DungeonSpawnDistance;
	}
	
	public HashMap<BlockPos, List<DungeonBase>> getCoordinateSpecificsMap() {
		return this.coordinateSpecificDungeons;
	}

}
