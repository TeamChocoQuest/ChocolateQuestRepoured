package team.cqr.cqrepoured.mixin.structure;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.feature.structure.StructureManager;
import team.cqr.cqrepoured.util.CQRChunkStatus;

@Mixin(StructureManager.class)
public class MixinStructureManager {

	@ModifyArg(method = "lambda$startsForFeature$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IWorld;getChunk(IILnet/minecraft/world/chunk/ChunkStatus;)Lnet/minecraft/world/chunk/IChunk;"), index = 2)
	public ChunkStatus chunkStatus(ChunkStatus original) {
		return CQRChunkStatus.CQR_STRUCTURE_STARTS;
	}

}
