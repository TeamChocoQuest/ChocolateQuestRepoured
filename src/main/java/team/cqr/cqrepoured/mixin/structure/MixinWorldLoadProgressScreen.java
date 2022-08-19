package team.cqr.cqrepoured.mixin.structure;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.gui.screen.WorldLoadProgressScreen;
import net.minecraft.world.chunk.ChunkStatus;
import team.cqr.cqrepoured.util.CQRChunkStatus;

@Mixin(WorldLoadProgressScreen.class)
public class MixinWorldLoadProgressScreen {

	@Shadow
	private static Object2IntMap<ChunkStatus> COLORS;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void clinit(CallbackInfo info) {
		COLORS.put(CQRChunkStatus.CQR_STRUCTURE_STARTS, 0xB0B0B0);
	}

}
