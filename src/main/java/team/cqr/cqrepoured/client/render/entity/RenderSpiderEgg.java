/*package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.entity.ModelSpiderEgg;
import team.cqr.cqrepoured.entity.misc.EntitySpiderEgg;

public class RenderSpiderEgg extends EntityRenderer<EntitySpiderEgg> {

	public static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
			new ResourceLocation(CQRConstants.MODID, "textures/entity/spider_egg/0.png"),
			new ResourceLocation(CQRConstants.MODID, "textures/entity/spider_egg/1.png"),
			new ResourceLocation(CQRConstants.MODID, "textures/entity/spider_egg/2.png"),
			new ResourceLocation(CQRConstants.MODID, "textures/entity/spider_egg/3.png"),
			new ResourceLocation(CQRConstants.MODID, "textures/entity/spider_egg/4.png"), };

	private final ModelBase model;

	public RenderSpiderEgg(EntityRendererManager renderManager) {
		super(renderManager);
		this.model = new ModelSpiderEgg();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySpiderEgg entity) {
		if (entity.getStage() >= TEXTURES.length) {
			return TEXTURES[0];
		} else {
			return TEXTURES[entity.getStage()];
		}
	}

	@Override
	public void doRender(EntitySpiderEgg entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		GlStateManager.translate(0.0F, -1.501F, 0.0F);
		this.bindTexture(this.getEntityTexture(entity));
		this.model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
	}

	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
		// Should be empty!!
	}

}*/
