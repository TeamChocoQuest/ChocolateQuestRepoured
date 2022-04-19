package team.cqr.cqrepoured.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.world.TrackedEntity;
import net.minecraftforge.fml.network.PacketDistributor;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateCQRMultipart;

@Mixin(TrackedEntity.class)
public abstract class MixinTrackedEntity {
	
	@Inject(
			at = @At("HEAD"),
			method = "sendDirtyEntityData()V"
	)
	private void mixinSendMetadata(CallbackInfo ci) {
		if(this instanceof AccessorTrackedEntity) {
			AccessorTrackedEntity ate = (AccessorTrackedEntity) this;
			Entity trackedEntity = ate.getEntity();
			
			if(trackedEntity.isMultipartEntity()) {
				CQRMain.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> trackedEntity), new SPacketUpdateCQRMultipart(trackedEntity));
			}
		}
	}
	
}
