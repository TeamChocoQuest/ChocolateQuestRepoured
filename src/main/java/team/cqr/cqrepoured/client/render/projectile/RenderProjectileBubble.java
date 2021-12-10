package team.cqr.cqrepoured.client.render.projectile;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.render.RenderSpriteBase;
import team.cqr.cqrepoured.entity.projectiles.ProjectileBubble;

public class RenderProjectileBubble extends RenderSpriteBase<ProjectileBubble> {

	public RenderProjectileBubble(RenderManager renderManager) {
		super(renderManager, new ResourceLocation(CQRMain.MODID, "textures/entity/bubble.png"));
	}

}
