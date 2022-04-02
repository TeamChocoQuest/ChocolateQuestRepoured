package team.cqr.cqrepoured.client.render.projectile;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.render.RenderSpriteBase;
import team.cqr.cqrepoured.entity.projectiles.ProjectileVampiricSpell;

public class RenderProjectileVampiricSpell extends RenderSpriteBase<ProjectileVampiricSpell>
{
	public RenderProjectileVampiricSpell(EntityRendererManager renderManager)
	{
		super(renderManager, CQRMain.prefix("textures/entity/vampiric_spell.png"));
	}
}
