package team.cqr.cqrepoured.entity;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateAnimationOfEntity;

public interface IServerAnimationReceiver {

	default EntityLivingBase getEntity() {
		if (this instanceof EntityLivingBase) {
			return (EntityLivingBase) this;
		}
		return null;
	}

	@Dist(OnlyIn.CLIENT)
	void processAnimationUpdate(String animationID);

	default void sendAnimationUpdate(final String animationName) {
		IMessage message = SPacketUpdateAnimationOfEntity.builder(this).animate(animationName).build();
		CQRMain.NETWORK.sendToAllTracking(message, this.getEntity());
	}

}
