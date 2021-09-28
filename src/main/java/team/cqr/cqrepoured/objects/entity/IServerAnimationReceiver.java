package team.cqr.cqrepoured.objects.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateAnimationOfEntity;

public interface IServerAnimationReceiver {
	
	public default EntityLivingBase getEntity() {
		if(this instanceof EntityLivingBase) {
			return (EntityLivingBase) this;
		}
		return null;
	}

	public void processAnimationUpdate(String animationID);
	
	public default void sendAnimationUpdate(final String animationName) {
		IMessage message = SPacketUpdateAnimationOfEntity.builder(this).animate(animationName).build();
		CQRMain.NETWORK.sendToAllTracking(message, this.getEntity());
	}

}
