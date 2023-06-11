package team.cqr.cqrepoured.mixin.structure;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.ImmutableList;

import net.minecraft.world.level.chunk.ChunkStatus;
import team.cqr.cqrepoured.util.CQRChunkStatus;

@Mixin(ChunkStatus.class)
public class MixinChunkStatus {

	@Shadow
	private static List<ChunkStatus> STATUS_BY_RANGE;

	@Inject(
			method = "<clinit>",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/world/chunk/ChunkStatus;register(Ljava/lang/String;Lnet/minecraft/world/chunk/ChunkStatus;ILjava/util/EnumSet;Lnet/minecraft/world/chunk/ChunkStatus$Type;Lnet/minecraft/world/chunk/ChunkStatus$IGenerationWorker;)Lnet/minecraft/world/chunk/ChunkStatus;",
					ordinal = 0,
					shift = Shift.AFTER
			)
	)
	private static void clinit(CallbackInfo info) {
		CQRChunkStatus.CQR_STRUCTURE_STARTS = ChunkStatus.registerSimple("minecraft:cqr_structure_starts", ChunkStatus.STRUCTURE_STARTS, 0, ChunkStatus.PRE_FEATURES, ChunkStatus.Type.PROTOCHUNK, CQRChunkStatus::executeCQRStructureStarts);
	}

	@Inject(method = "<clinit>", remap = false, at = @At(value = "INVOKE_ASSIGN", target = "Lcom/google/common/collect/ImmutableList;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;", shift = Shift.AFTER))
	private static void clinit_STATUS_BY_RANGE(CallbackInfo info) {
		STATUS_BY_RANGE = ImmutableList.of(
				ChunkStatus.FULL,
				ChunkStatus.FEATURES,
				ChunkStatus.LIQUID_CARVERS,
				CQRChunkStatus.CQR_STRUCTURE_STARTS,
				CQRChunkStatus.CQR_STRUCTURE_STARTS,
				CQRChunkStatus.CQR_STRUCTURE_STARTS,
				CQRChunkStatus.CQR_STRUCTURE_STARTS,
				CQRChunkStatus.CQR_STRUCTURE_STARTS,
				CQRChunkStatus.CQR_STRUCTURE_STARTS,
				CQRChunkStatus.CQR_STRUCTURE_STARTS,
				CQRChunkStatus.CQR_STRUCTURE_STARTS);
	}

	@ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ChunkStatus;registerSimple(Ljava/lang/String;Lnet/minecraft/world/chunk/ChunkStatus;ILjava/util/EnumSet;Lnet/minecraft/world/chunk/ChunkStatus$Type;Lnet/minecraft/world/chunk/ChunkStatus$ISelectiveWorker;)Lnet/minecraft/world/chunk/ChunkStatus;", ordinal = 1), index = 1)
	private static ChunkStatus parent(ChunkStatus original) {
		return CQRChunkStatus.CQR_STRUCTURE_STARTS;
	}

}
