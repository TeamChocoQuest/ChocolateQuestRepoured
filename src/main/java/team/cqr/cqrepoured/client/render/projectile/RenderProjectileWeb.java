package team.cqr.cqrepoured.client.render.projectile;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.client.render.RenderSpriteBase;
import team.cqr.cqrepoured.entity.projectiles.ProjectileWeb;

public class RenderProjectileWeb extends RenderSpriteBase<ProjectileWeb> {

	public RenderProjectileWeb(Context renderManager)
	{
		super(renderManager, new ResourceLocation(CQRConstants.MODID, "textures/blocks/temporary_web/web_poison_00.png"));
	}
}
