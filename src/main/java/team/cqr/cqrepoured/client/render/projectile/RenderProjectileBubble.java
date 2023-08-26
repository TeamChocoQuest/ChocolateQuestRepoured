package team.cqr.cqrepoured.client.render.projectile;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.client.render.RenderSpriteBase;
import team.cqr.cqrepoured.entity.projectiles.ProjectileBubble;

public class RenderProjectileBubble extends RenderSpriteBase<ProjectileBubble>
{
	public RenderProjectileBubble(EntityRendererProvider.Context renderManager)
	{
		super(renderManager, new ResourceLocation(CQRConstants.MODID, "textures/entity/bubble.png"));
	}
}