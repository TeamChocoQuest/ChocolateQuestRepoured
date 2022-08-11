package team.cqr.cqrepoured.mixin.structure;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.chunk.ChunkStatus;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.util.CQRChunkStatus;

@Mixin(ChunkStatus.class)
public class MixinChunkStatus {

	@Inject(method = "<clinit>", at = @At(value = "INVOKE_ASSIGN", target = "register", ordinal = 0, shift = Shift.AFTER))
	private static void clinit(CallbackInfo info) {
		CQRChunkStatus.CQR_STRUCTURE_STARTS = ChunkStatus.registerSimple(CQRMain.MODID + ":cqr_structure_starts", ChunkStatus.STRUCTURE_STARTS, 0, ChunkStatus.PRE_FEATURES, ChunkStatus.Type.PROTOCHUNK, CQRChunkStatus::executeCQRStructureStarts);
	}

	@ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "registerSimple", ordinal = 1), index = 1)
	private static ChunkStatus parent(ChunkStatus original) {
		return CQRChunkStatus.CQR_STRUCTURE_STARTS;
	}

}
