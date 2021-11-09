package team.cqr.cqrepoured.client.render.projectile;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.render.RenderSpriteBase;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileWeb;
import team.cqr.cqrepoured.util.Reference;

public class RenderProjectileWeb extends RenderSpriteBase<ProjectileWeb> {

	public RenderProjectileWeb(RenderManager renderManager) {
		super(renderManager, new ResourceLocation(Reference.MODID, "textures/blocks/temporary_web/web_poison_00.png"));
	}
}
