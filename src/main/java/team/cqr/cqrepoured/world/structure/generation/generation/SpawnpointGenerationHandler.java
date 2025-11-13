package team.cqr.cqrepoured.world.structure.generation.generation;

public interface SpawnpointGenerationHandler {

	boolean isDungeonGenerationDelayed(int chunkX, int chunkZ);

	boolean isGeneratingDelayedChunks();

}
