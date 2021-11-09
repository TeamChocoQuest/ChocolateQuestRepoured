package team.cqr.cqrepoured.client.render.projectile;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.render.RenderSpriteBase;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileVampiricSpell;
import team.cqr.cqrepoured.util.Reference;

public class RenderProjectileVampiricSpell extends RenderSpriteBase<ProjectileVampiricSpell> {

	public RenderProjectileVampiricSpell(RenderManager renderManager) {
		super(renderManager, new ResourceLocation(Reference.MODID, "textures/entity/vampiric_spell.png"));
	}
}
