package team.cqr.cqrepoured.objects.entity;

import net.minecraft.entity.EntityLivingBase;

public interface IServerAnimationReceiver {
	
	public EntityLivingBase getEntity();

	public void processAnimationUpdate(String animationID);

}
