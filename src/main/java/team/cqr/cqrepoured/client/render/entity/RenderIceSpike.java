package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.models.entities.ModelIceSpike;
import team.cqr.cqrepoured.objects.entity.misc.EntityIceSpike;

public class RenderIceSpike extends Render<EntityIceSpike> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/blocks/ice_packed.png");

	private final ModelIceSpike model = new ModelIceSpike();

	public RenderIceSpike(RenderManager p_i46179_1_) {
		super(p_i46179_1_);
	}

	@Override
	public void doRender(EntityIceSpike entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		// GlStateManager.translate(0.0F, -1.501F, 0.0F);
		this.bindTexture(this.getEntityTexture(entity));
		this.model.render(entity, 2 * entity.getAnimationProgress(partialTicks * 2), 0.0F, 0F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIceSpike p_110775_1_) {
		return TEXTURE;
	}
	
	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
	}

}
