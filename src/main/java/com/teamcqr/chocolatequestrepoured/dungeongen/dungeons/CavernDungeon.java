package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.CavernGenerator;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class CavernDungeon extends DungeonBase {
	
	private int minRooms = 1;
	private int maxRooms = 8;
	private int minY = 30;
	private int maxY = 60;
	private int minCaveSize = 5;
	private int maxCaveSize = 15;
	private int minHeight = 4;
	private int maxHeight = 8;
	private int maxRoomDistance = 12;
	private int minRoomDistance = 4;
	private int chestChancePerRoom = 20;
	
	private boolean placeSpawners = false;
	private boolean placeBoss = true;
	private boolean lootChests = false;
	private String mobName = "minecraft:zombie";
	private String bossMobName = "minecraft:bear";
	private Block floorMaterial = Blocks.STONE;

	@Override
	public IDungeonGenerator getGenerator() {
		return new CavernGenerator();
	}
	
	@Override
	protected void generate(int x, int z, World world, Chunk chunk) {
		super.generate(x, z, world, chunk);
		
		List<CavernGenerator> caves = new ArrayList<CavernGenerator>();
		
		int rooms = maxRooms <= minRooms ? minRooms : DungeonGenUtils.getIntBetweenBorders(minRooms, maxRooms, world.getSeed());
		int y = DungeonGenUtils.getIntBetweenBorders(minY, maxY, world.getSeed());
		int roomIndex = 1;
		
		int OrigX = new Integer(x);
		int OrigZ = new Integer(z);
		
		Vec3i distance = new Vec3i(0, 0, 0);
		
		do {
			x += distance.getX();
			z +=distance.getZ();
					
			CavernGenerator cave = new CavernGenerator(this);
			cave.preProcess(world, chunk, x + distance.getX(), y, z + distance.getZ());
			
			int vLength = DungeonGenUtils.getIntBetweenBorders(minRoomDistance, maxRoomDistance, world.getSeed());
			distance = new Vec3i(vLength, 0, 0);
			double angle = ((Integer)new Random().nextInt(360)).doubleValue();
			distance = VectorUtil.rotateVectorAroundY(distance, angle);
			
			caves.add(cave);
		} while(roomIndex < rooms);
		
		
	}
}
