package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.client.models.entities.ModelLaser;
import team.cqr.cqrepoured.objects.entity.boss.AbstractEntityLaser;
import team.cqr.cqrepoured.util.Reference;

public class RenderLaser<T extends AbstractEntityLaser> extends Render<T> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/effects/ray.png");
	private final ModelBase model;

	public RenderLaser(RenderManager renderManager) {
		super(renderManager);
		this.model = new ModelLaser();
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return TEXTURE;
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		Minecraft mc = Minecraft.getMinecraft();
		double x1 = entity.caster.lastTickPosX + (entity.caster.posX - entity.caster.lastTickPosX) * partialTicks;
		x1 += entity.getOffsetVector().x;
		double y1 = entity.caster.lastTickPosY + (entity.caster.posY - entity.caster.lastTickPosY) * partialTicks + entity.caster.height * 0.6D;
		y1 += entity.getOffsetVector().y;
		double z1 = entity.caster.lastTickPosZ + (entity.caster.posZ - entity.caster.lastTickPosZ) * partialTicks;
		z1 += entity.getOffsetVector().z;
		float yaw = this.getYaw(entity, partialTicks);
		float pitch = this.getPitch(entity, partialTicks);
		Vec3d vec = Vec3d.fromPitchYaw(pitch, yaw).scale(0.25D);
		x1 += vec.x;
		y1 += vec.y;
		z1 += vec.z;
		Entity renderViewEntity = mc.getRenderViewEntity();
		double x2 = renderViewEntity.lastTickPosX + (renderViewEntity.posX - renderViewEntity.lastTickPosX) * partialTicks;
		double y2 = renderViewEntity.lastTickPosY + (renderViewEntity.posY - renderViewEntity.lastTickPosY) * partialTicks;
		double z2 = renderViewEntity.lastTickPosZ + (renderViewEntity.posZ - renderViewEntity.lastTickPosZ) * partialTicks;

		GlStateManager.pushMatrix();
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
		GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
		GlStateManager.depthMask(false);
		this.bindEntityTexture(entity);
		GlStateManager.translate(x1 - x2, y1 - y2, z1 - z2);
		GlStateManager.rotate(180.0F - yaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-pitch, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(-1.0D, -1.0D, 1.0D);

		double laserLength = this.getLaserLength(entity, pitch, yaw);

		GlStateManager.pushMatrix();
		double d3 = 1.0D;
		GlStateManager.scale(d3, d3, laserLength);
		float f3 = 1.0F / 3.0F;
		GlStateManager.color(entity.getColorR(), entity.getColorG(), entity.getColorB(), f3);
		this.model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		double d2 = 2.0D / 3.0D;
		GlStateManager.scale(d2, d2, laserLength);
		float f2 = 2.0F / 3.0F;
		GlStateManager.color(entity.getColorR(), entity.getColorG(), entity.getColorB(), f2);
		this.model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		double d1 = 1.0D / 3.0D;
		GlStateManager.scale(d1, d1, laserLength);
		float f1 = 1.0F;
		GlStateManager.color(1.0F, 1.0F, 1.0F, f1);
		this.model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();

		GlStateManager.depthMask(true);
		GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.popMatrix();
	}
	
	protected float getPitch(T entity, float partialTicks) {
		return this.interpolateRotation(entity.prevRotationPitchCQR, entity.rotationPitchCQR, partialTicks);
	}
	
	protected float getYaw(T entity, float partialTicks) {
		return this.interpolateRotation(entity.prevRotationYawCQR, entity.rotationYawCQR, partialTicks);
	}
	
	protected double getLaserLength(T entity, float partialTicks) {
		return this.getLaserLength(entity, this.getPitch(entity, partialTicks), getYaw(entity, partialTicks));
	}
	
	protected double getLaserLength(T entity, float pitch, float yaw) {
		Vec3d start = entity.getPositionVector();
		Vec3d end = start.add(Vec3d.fromPitchYaw(pitch, yaw).scale(entity.length));
		RayTraceResult result = entity.world.rayTraceBlocks(start, end, false, true, false);
		double d = result != null ? (float) result.hitVec.subtract(entity.getPositionVector()).length() : entity.length;
		
		return d;
	}

	protected float interpolateRotation(float prevRotation, float rotation, float partialTicks) {
		return prevRotation + MathHelper.wrapDegrees(rotation - prevRotation) * partialTicks;
	}
	
	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
	}

}
