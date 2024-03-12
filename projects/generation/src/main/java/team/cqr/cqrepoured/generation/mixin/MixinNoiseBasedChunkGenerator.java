package team.cqr.cqrepoured.generation.mixin;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import team.cqr.cqrepoured.common.CQRepoured;

@Mixin(NoiseBasedChunkGenerator.class)
public class MixinNoiseBasedChunkGenerator {

	@Unique
	private static final DecimalFormat FORMAT = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
	@Unique
	private static final ThreadLocal<AtomicLong> TEMP = ThreadLocal.withInitial(AtomicLong::new);

	@Inject(method = "doFill", at = @At("HEAD"))
	private void pre(Blender pBlender, StructureManager pStructureManager, RandomState pRandom, ChunkAccess pChunk, int pMinCellY, int pCellCountY,
			CallbackInfoReturnable<ChunkAccess> info) {
		TEMP.get().set(System.nanoTime());
	}

	@Inject(method = "doFill", at = @At("RETURN"))
	private void post(Blender pBlender, StructureManager pStructureManager, RandomState pRandom, ChunkAccess pChunk, int pMinCellY, int pCellCountY,
			CallbackInfoReturnable<ChunkAccess> info) {
		long t = System.nanoTime() - TEMP.get().get();
		CQRepoured.LOGGER.warn("Noise Time ({}): {}ms", pChunk.getPos(), FORMAT.format(t / 1_000_000.0));
	}

}
