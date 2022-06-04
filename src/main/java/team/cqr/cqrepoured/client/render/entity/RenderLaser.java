package team.cqr.cqrepoured.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.init.CQRRenderTypes;
import team.cqr.cqrepoured.client.model.entity.ModelLaser;
import team.cqr.cqrepoured.entity.boss.AbstractEntityLaser;

public class RenderLaser<T extends AbstractEntityLaser> extends EntityRenderer<T> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/effects/ray.png");
	private final ModelLaser model;

	public RenderLaser(EntityRendererManager renderManager) {
		super(renderManager);
		this.model = new ModelLaser();
	}

	@Override
	public void render(T entity, float pEntityYaw, float partialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
		super.render(entity, pEntityYaw, partialTicks, pMatrixStack, pBuffer, pPackedLight);
		
		if (entity.caster == null) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();
		double x1 = entity.caster.xOld + (entity.caster.getX() - entity.caster.xOld) * partialTicks;
		double y1 = entity.caster.yOld + (entity.caster.getY() - entity.caster.yOld) * partialTicks;
		double z1 = entity.caster.zOld + (entity.caster.getZ() - entity.caster.zOld) * partialTicks;
		Vector3d offset = entity.getOffsetVector();
		x1 += offset.x;
		y1 += offset.y;
		z1 += offset.z;
		float yaw = this.getYaw(entity, partialTicks);
		float pitch = this.getPitch(entity, partialTicks);
		Entity renderViewEntity = mc.getCameraEntity();
		double x2 = entity.xOld + (entity.getX() - entity.xOld) * partialTicks;
		double y2 = entity.yOld + (entity.getY() - entity.yOld) * partialTicks;
		double z2 = entity.zOld + (entity.getZ() - entity.zOld) * partialTicks;

		pMatrixStack.pushPose();
		//Replaced by rendertype
		/*GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
		GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
		GlStateManager.depthMask(false);
		this.bindEntityTexture(entity);*/
		pMatrixStack.translate(x1 - x2, y1 - y2, z1 - z2);
		pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - yaw));
		pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(-pitch));
		pMatrixStack.scale(-1.0F, -1.0F, 1.0F);

		float laserLength = this.getLaserLength(entity, pitch, yaw);
		
		IVertexBuilder ivb = pBuffer.getBuffer(CQRRenderTypes.laser());

		pMatrixStack.pushPose();
		float d3 = 1.0F;
		pMatrixStack.scale(d3, d3, laserLength);
		float f3 = 1.0F / 3.0F;
		//RenderSystem.color4f(entity.getColorR(), entity.getColorG(), entity.getColorB(), f3);
		this.model.renderToBuffer(pMatrixStack, ivb, pPackedLight, 0, entity.getColorR(), entity.getColorG(), entity.getColorB(), f3);
		pMatrixStack.popPose();

		pMatrixStack.pushPose();
		float d2 = 2.0F / 3.0F;
		pMatrixStack.scale(d2, d2, laserLength);
		float f2 = 2.0F / 3.0F;
		//RenderSystem.color4f(entity.getColorR(), entity.getColorG(), entity.getColorB(), f2);
		this.model.renderToBuffer(pMatrixStack, ivb, pPackedLight, 0, entity.getColorR(), entity.getColorG(), entity.getColorB(), f2);
		pMatrixStack.popPose();

		pMatrixStack.pushPose();
		float d1 = 1.0F / 3.0F;
		pMatrixStack.scale(d1, d1, laserLength);
		float f1 = 1.0F;
		//RenderSystem.color4f(1.0F, 1.0F, 1.0F, f1);
		this.model.renderToBuffer(pMatrixStack, ivb, pPackedLight, 0, 1.0F, 1.0F, 1.0F, f1);
		pMatrixStack.popPose();

		//Replaced by rendertype
		/*GlStateManager.depthMask(true);
		GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();*/
		
		pMatrixStack.popPose();
		
	}
	
	/*@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		EmissiveUtil.preEmissiveTextureRendering();

		if (entity.caster == null) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();
		double x1 = entity.caster.xOld + (entity.caster.getX() - entity.caster.xOld) * partialTicks;
		double y1 = entity.caster.yOld + (entity.caster.getY() - entity.caster.yOld) * partialTicks;
		double z1 = entity.caster.zOld + (entity.caster.getZ() - entity.caster.zOld) * partialTicks;
		Vec3d offset = entity.getOffsetVector();
		x1 += offset.x;
		y1 += offset.y;
		z1 += offset.z;
		float yaw = this.getYaw(entity, partialTicks);
		float pitch = this.getPitch(entity, partialTicks);
		Entity renderViewEntity = mc.getRenderViewEntity();
		double x2 = renderViewEntity.xOld + (renderViewEntity.getX() - renderViewEntity.xOld) * partialTicks;
		double y2 = renderViewEntity.yOld + (renderViewEntity.getY() - renderViewEntity.yOld) * partialTicks;
		double z2 = renderViewEntity.zOld + (renderViewEntity.getZ() - renderViewEntity.zOld) * partialTicks;

		GlStateManager.pushPose();
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

		GlStateManager.pushPose();
		double d3 = 1.0D;
		GlStateManager.scale(d3, d3, laserLength);
		float f3 = 1.0F / 3.0F;
		GlStateManager.color(entity.getColorR(), entity.getColorG(), entity.getColorB(), f3);
		this.model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popPose();

		GlStateManager.pushPose();
		double d2 = 2.0D / 3.0D;
		GlStateManager.scale(d2, d2, laserLength);
		float f2 = 2.0F / 3.0F;
		GlStateManager.color(entity.getColorR(), entity.getColorG(), entity.getColorB(), f2);
		this.model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popPose();

		GlStateManager.pushPose();
		double d1 = 1.0D / 3.0D;
		GlStateManager.scale(d1, d1, laserLength);
		float f1 = 1.0F;
		GlStateManager.color(1.0F, 1.0F, 1.0F, f1);
		this.model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popPose();

		GlStateManager.depthMask(true);
		GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.popPose();

		EmissiveUtil.postEmissiveTextureRendering();
	}*/

	protected float getPitch(T entity, float partialTicks) {
		return this.interpolateRotation(entity.prevRotationPitchCQR, entity.rotationPitchCQR, partialTicks);
	}

	protected float getYaw(T entity, float partialTicks) {
		return this.interpolateRotation(entity.prevRotationYawCQR, entity.rotationYawCQR, partialTicks);
	}

	protected double getLaserLength(T entity, float partialTicks) {
		return this.getLaserLength(entity, this.getPitch(entity, partialTicks), this.getYaw(entity, partialTicks));
	}

	protected float getLaserLength(T entity, float pitch, float yaw) {
		Vector3d start = entity.position();
		Vector3d end = start.add(Vector3d.directionFromRotation(pitch, yaw).scale(entity.length));
		RayTraceContext rtc = new RayTraceContext(start, end, BlockMode.COLLIDER, FluidMode.NONE, entity);
		BlockRayTraceResult result = entity.level.clip(rtc);//rayTraceBlocks(start, end, false, true, false);
		float d = result != null ? (float) result.getLocation().subtract(entity.position()).length() : entity.length;

		return d;
	}

	protected float interpolateRotation(float prevRotation, float rotation, float partialTicks) {
		return prevRotation + MathHelper.wrapDegrees(rotation - prevRotation) * partialTicks;
	}

	@Override
	public ResourceLocation getTextureLocation(T pEntity) {
		return TEXTURE;
	}

}
