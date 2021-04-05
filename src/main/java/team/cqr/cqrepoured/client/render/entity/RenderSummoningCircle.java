package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.util.PentagramUtil;
import team.cqr.cqrepoured.objects.entity.misc.EntitySummoningCircle;
import team.cqr.cqrepoured.util.Reference;

public class RenderSummoningCircle extends Render<EntitySummoningCircle> {

	// TODO: Move corner count, color and radius to the entity
	// TODO: Render the letters on the corners

	public static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
			new ResourceLocation(Reference.MODID, "textures/entity/summoning_circles/zombie.png"),
			new ResourceLocation(Reference.MODID, "textures/entity/summoning_circles/skeleton.png"),
			new ResourceLocation(Reference.MODID, "textures/entity/summoning_circles/flying_skull.png"),
			new ResourceLocation(Reference.MODID, "textures/entity/summoning_circles/flying_sword.png"),
			new ResourceLocation(Reference.MODID, "textures/entity/summoning_circles/meteor.png") };

	public RenderSummoningCircle(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySummoningCircle entity) {
		if (entity.getTextureID() >= TEXTURES.length) {
			return TEXTURES[0];
		} else {
			return TEXTURES[entity.getTextureID()];
		}
	}

	@Override
	public void doRender(EntitySummoningCircle entity, double x, double y, double z, float entityYaw, float partialTicks) {
		// GlStateManager.translate((float) x, (float) y, (float) z);

		/*
		 * GlStateManager.color(new Float(0.3F * (Math.sin(0.125 * entity.ticksExisted) + 1)), 0F, 0F);
		 * 
		 * this.bindTexture(this.getEntityTexture(entity)); this.model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		 */

		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y + 0.02, (float) z);
		float f = (entity.ticksExisted - 1.0F + partialTicks) * 4.0F;
		GlStateManager.rotate(f, 0F, 1F, 0F);
		PentagramUtil.preRenderPentagram(x, y, z, entity.ticksExisted);
		PentagramUtil.renderPentagram(entity.ticksExisted);
		PentagramUtil.postRenderPentagram();
		
		GlStateManager.popMatrix();

	}

	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
		// Should be empty!!
	}

}
