package team.cqr.cqrepoured.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;
import team.cqr.cqrepoured.common.services.CQRServices;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateAnimationOfEntity;

public interface IServerAnimationReceiver {

	default LivingEntity getSelf() {
		if (this instanceof LivingEntity) {
			return (LivingEntity) this;
		}
		return null;
	}

	@OnlyIn(Dist.CLIENT)
	void processAnimationUpdate(String animationID);

	default void sendAnimationUpdate(final String animationName) {
		SPacketUpdateAnimationOfEntity message = SPacketUpdateAnimationOfEntity.builder(this).animate(animationName).build();
		LivingEntity entity = this.getSelf();
		if(entity != null) {
			CQRServices.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
		}
	}

}
