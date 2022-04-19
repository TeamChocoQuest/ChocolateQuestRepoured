package team.cqr.cqrepoured.mixinutil;

import net.minecraft.client.renderer.entity.EntityRendererManager;

public interface IEntityRendererSelfAccessorHelper {
	
	public default EntityRendererManager getSelfOrNull() {
		if(this instanceof EntityRendererManager) {
			return (EntityRendererManager) this;
		}
		return null;
	}

}
