package team.cqr.cqrepoured.client.render.projectile;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.render.RenderSpriteBase;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileBubble;
import team.cqr.cqrepoured.util.Reference;

public class RenderProjectileBubble extends RenderSpriteBase<ProjectileBubble> {

	public RenderProjectileBubble(RenderManager renderManager) {
		super(renderManager, new ResourceLocation(Reference.MODID, "textures/entity/bubble.png"));
	}

}
