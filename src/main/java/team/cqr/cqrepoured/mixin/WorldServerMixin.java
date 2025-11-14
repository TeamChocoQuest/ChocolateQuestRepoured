package team.cqr.cqrepoured.mixin;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.gen.ChunkProviderServer;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.world.structure.generation.generation.PortalGenerationHandler;
import team.cqr.cqrepoured.world.structure.generation.generation.SpawnpointGenerationHandler;

@Mixin(WorldServer.class)
public abstract class WorldServerMixin implements SpawnpointGenerationHandler, PortalGenerationHandler {

	@Unique
	private final Set<ChunkPos> delayedChunks = new HashSet<>();
	@Unique
	private boolean isGeneratingDelayedChunks;
	@Unique
	private boolean isGeneratingDestinationChunks;

	@Inject(method = "createSpawnPosition", at = @At(value = "FIELD", target = "Lnet/minecraft/world/WorldServer;findingSpawnPoint:Z", ordinal = 1, shift = Shift.AFTER))
	private void createSpawnPosition(WorldSettings settings, CallbackInfo info) {
		long worldSeed = ((WorldServer) (Object) this).getSeed();
		Random fmlRandom = new Random(worldSeed);
		long xSeed = fmlRandom.nextLong() >> 2 + 1L;
		long zSeed = fmlRandom.nextLong() >> 2 + 1L;

		isGeneratingDelayedChunks = true;
		for (ChunkPos chunkPos : delayedChunks) {
			long chunkSeed = (xSeed * chunkPos.x + zSeed * chunkPos.z) ^ worldSeed;
			fmlRandom.setSeed(chunkSeed);
			CQRMain.DUNGEON_GENERATOR.generate(fmlRandom, chunkPos.x, chunkPos.z, (WorldServer) (Object) this, this.getChunkProvider().chunkGenerator, this.getChunkProvider());
			CQRMain.WALL_GENERATOR.generate(fmlRandom, chunkPos.x, chunkPos.z, (WorldServer) (Object) this, this.getChunkProvider().chunkGenerator, this.getChunkProvider());
		}
		isGeneratingDelayedChunks = false;

		delayedChunks.clear();
	}

	@Shadow
	public abstract ChunkProviderServer getChunkProvider();

	@Override
	public boolean isDungeonGenerationDelayed(int chunkX, int chunkZ) {
		boolean delayed = ((WorldServer) (Object) this).findingSpawnPoint;
		if (delayed) {
			delayedChunks.add(new ChunkPos(chunkX, chunkZ));
		}
		return delayed;
	}

	@Override
	public boolean isGeneratingDelayedChunks() {
		return isGeneratingDelayedChunks;
	}

	@Override
	public void isGeneratingDestinationChunks(boolean value) {
		isGeneratingDestinationChunks = value;
	}

	@Override
	public boolean isGeneratingDestinationChunks() {
		return isGeneratingDestinationChunks;
	}

}
