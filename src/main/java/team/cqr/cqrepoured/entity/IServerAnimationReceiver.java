package team.cqr.cqrepoured.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;
import team.cqr.cqrepoured.CQRMain;
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
		CQRMain.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(this::getSelf), message);
	}

}
