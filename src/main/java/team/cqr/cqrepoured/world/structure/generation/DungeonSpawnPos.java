package team.cqr.cqrepoured.world.structure.generation;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public class DungeonSpawnPos {

	private final int x;
	private final int z;
	private final boolean spawnPointRelative;

	public DungeonSpawnPos(int x, int z, boolean spawnPointRelative) {
		this.x = x;
		this.z = z;
		this.spawnPointRelative = spawnPointRelative;
	}

	public int getX() {
		return this.x;
	}

	public int getZ() {
		return this.z;
	}

	public boolean isSpawnPointRelative() {
		return this.spawnPointRelative;
	}

	public boolean isInChunk(Level level, ChunkPos chunkPos) {
		return (this.getX(level) >> 4) == chunkPos.x && (this.getZ(level) >> 4) == chunkPos.z;
	}

	public int getX(Level level) {
		return this.spawnPointRelative ? this.x + level.getLevelData().getXSpawn() : this.x;
	}

	public int getZ(Level level) {
		return this.spawnPointRelative ? this.z + level.getLevelData().getZSpawn() : this.z;
	}

}
