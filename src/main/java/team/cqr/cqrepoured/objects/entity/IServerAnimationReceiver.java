package team.cqr.cqrepoured.objects.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateAnimationOfEntity;

public interface IServerAnimationReceiver {
	
	public default EntityLivingBase getEntity() {
		if(this instanceof EntityLivingBase) {
			return (EntityLivingBase) this;
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	public void processAnimationUpdate(String animationID);
	
	public default void sendAnimationUpdate(final String animationName) {
		IMessage message = SPacketUpdateAnimationOfEntity.builder(this).animate(animationName).build();
		CQRMain.NETWORK.sendToAllTracking(message, this.getEntity());
	}

}
