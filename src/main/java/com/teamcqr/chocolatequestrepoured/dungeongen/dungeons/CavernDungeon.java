package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.CavernGenerator;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class CavernDungeon extends DungeonBase {
	
	private int rooms;
	private int minCaveSize = 5;
	private int maxCaveSize = 15;
	private int minHeight = 4;
	private int maxHeight = 8;
	
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
		
		
	}
}
