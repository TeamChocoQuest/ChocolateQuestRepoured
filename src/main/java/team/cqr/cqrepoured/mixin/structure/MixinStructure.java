package team.cqr.cqrepoured.mixin.structure;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.sun.jna.Structure;

import net.minecraft.world.chunk.ChunkStatus;
import team.cqr.cqrepoured.util.CQRChunkStatus;

@Mixin(Structure.class)
public class MixinStructure {

	@ModifyArg(method = "getNearestGeneratedFeature", at = @At(value = "INVOKE", target = "getChunk"), index = 2)
	public ChunkStatus chunkStatus(ChunkStatus original) {
		return CQRChunkStatus.CQR_STRUCTURE_STARTS;
	}

}
