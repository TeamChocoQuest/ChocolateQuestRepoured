package team.cqr.cqrepoured.client.render.entity.boss.spectrelord;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.client.models.entities.boss.spectrelord.ModelLaser;
import team.cqr.cqrepoured.objects.entity.boss.spectrelord.AbstractEntityLaser;
import team.cqr.cqrepoured.util.Reference;

public class RenderLaser extends Render<AbstractEntityLaser> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/effects/ray.png");
	private final ModelBase model;

	public RenderLaser(RenderManager renderManager) {
		super(renderManager);
		this.model = new ModelLaser();
	}

	@Override
	protected ResourceLocation getEntityTexture(AbstractEntityLaser entity) {
		return TEXTURE;
	}

	@Override
	public void doRender(AbstractEntityLaser entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
		GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
		this.bindEntityTexture(entity);
		GlStateManager.translate(x, y, z);
		float yaw = this.interpolateRotation(entity.prevRotationYawCQR, entity.rotationYawCQR, partialTicks);
		float pitch = this.interpolateRotation(entity.prevRotationPitchCQR, entity.rotationPitchCQR, partialTicks);
		GlStateManager.rotate(180.0F - yaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-pitch, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(-1.0D, -1.0D, 1.0D);

		Vec3d start = entity.getPositionVector();
		Vec3d end = start.add(Vec3d.fromPitchYaw(entity.rotationPitchCQR, entity.rotationYawCQR).scale(entity.length));
		RayTraceResult result = entity.world.rayTraceBlocks(start, end, false, true, false);
		double d = result != null ? (float) result.hitVec.subtract(entity.getPositionVector()).length() : entity.length;

		GlStateManager.pushMatrix();
		double d3 = 1.0D;
		GlStateManager.scale(d3, d3, d);
		float f3 = 1.0F / 3.0F;
		GlStateManager.color(0.1F, 0.7F, 0.9F, f3);
		this.model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		double d2 = 2.0D / 3.0D;
		GlStateManager.scale(d2, d2, d);
		float f2 = 2.0F / 3.0F;
		GlStateManager.color(0.1F, 0.7F, 0.9F, f2);
		this.model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		double d1 = 1.0D / 3.0D;
		GlStateManager.scale(d1, d1, d);
		float f1 = 1.0F;
		GlStateManager.color(1.0F, 1.0F, 1.0F, f1);
		this.model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();

		GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.popMatrix();
	}

	protected float interpolateRotation(float prevRotation, float rotation, float partialTicks) {
		return prevRotation + MathHelper.wrapDegrees(rotation - prevRotation) * partialTicks;
	}

}
