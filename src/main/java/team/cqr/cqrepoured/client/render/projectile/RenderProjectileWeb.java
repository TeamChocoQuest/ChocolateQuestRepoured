package team.cqr.cqrepoured.client.render.projectile;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.render.RenderSpriteBase;
import team.cqr.cqrepoured.entity.projectiles.ProjectileWeb;

public class RenderProjectileWeb extends RenderSpriteBase<ProjectileWeb> {

	public RenderProjectileWeb(EntityRendererManager renderManager)
	{
		super(renderManager, new ResourceLocation(CQRMain.MODID, "textures/blocks/temporary_web/web_poison_00.png"));
	}
}
