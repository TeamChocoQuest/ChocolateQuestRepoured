package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.objects.entity.misc.EntitySummoningCircle;
import team.cqr.cqrepoured.util.Reference;
import team.cqr.cqrepoured.util.VectorUtil;

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
		GlStateManager.rotate((float) (4 * entity.ticksExisted), 0F, 1F, 0F);
		GlStateManager.disableFog();
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		this.setLightmapDisabled(true);

		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();

		GlStateManager.glLineWidth(8.0F);
		builder.begin(3, DefaultVertexFormats.POSITION_COLOR);

		double radius = 0.75D;
		double corners = 5;
		int r = 55 + (int) Math.round(200 * 0.5 * (Math.sin(0.25 * entity.ticksExisted) + 1));
		int g = 1;
		int b = 1;

		Vec3d vector = new Vec3d(radius, 0, 0);
		double alpha = 360D / corners;
		int skipCorners = 2;
		alpha *= (double) skipCorners;
		// First, draw the diagonal lines (get one point, and move to the over-next point
		for (int i = 0; i <= corners; i++) {

			builder.pos(vector.x, 0, vector.z).color(r, g, b, 255).endVertex();

			vector = VectorUtil.rotateVectorAroundY(vector, alpha);
		}
		vector = VectorUtil.rotateVectorAroundY(vector, -alpha);
		alpha /= (double) skipCorners;
		vector = VectorUtil.rotateVectorAroundY(vector, alpha);
		// after this, we need to render the outline
		for (int i = 0; i < corners; i++) {
			builder.pos(vector.x, 0, vector.z).color(r, g, b, 255).endVertex();
			
			GlStateManager.pushAttrib();
			FontRenderer fontrenderer = Minecraft.getMinecraft().standardGalacticFontRenderer;
			GlStateManager.alphaFunc(516, 0.1F);
			EntityRenderer.drawNameplate(fontrenderer, "a", (float) x, (float) y, (float) z, 0, this.renderManager.playerViewY, this.renderManager.playerViewY, true, false);
			GlStateManager.popAttrib();
			
			vector = VectorUtil.rotateVectorAroundY(vector, alpha);
		}

		tess.draw();

		this.setLightmapDisabled(false);
		GlStateManager.glLineWidth(1.0F);
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.enableCull();
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.enableFog();
		GlStateManager.popMatrix();

	}

	protected void setLightmapDisabled(boolean disabled) {
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);

		if (disabled) {
			GlStateManager.disableTexture2D();
		} else {
			GlStateManager.enableTexture2D();
		}

		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
		// Should be empty!!
	}

}
