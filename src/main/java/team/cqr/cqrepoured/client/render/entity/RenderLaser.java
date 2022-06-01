package team.cqr.cqrepoured.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.client.init.CQRRenderTypes;
import team.cqr.cqrepoured.entity.boss.AbstractEntityLaser;

public class RenderLaser<T extends AbstractEntityLaser> extends EntityRenderer<T> {

	public RenderLaser(EntityRendererManager renderManager) {
		super(renderManager);
	}
	
	@Override
	public void render(T entity, float pEntityYaw, float partialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
		super.render(entity, pEntityYaw, partialTicks, pMatrixStack, pBuffer, pPackedLight);
		
		double x1 = entity.caster.xOld + (entity.caster.getX() - entity.caster.xOld) * partialTicks;
		double y1 = entity.caster.yOld + (entity.caster.getY() - entity.caster.yOld) * partialTicks;
		double z1 = entity.caster.zOld + (entity.caster.getZ() - entity.caster.zOld) * partialTicks;
		Vector3d offset = entity.getOffsetVector();
		x1 += offset.x;
		y1 += offset.y;
		z1 += offset.z;
		float yaw = this.getYaw(entity, partialTicks);
		float pitch = this.getPitch(entity, partialTicks);
		final Vector3d laserDirection = Vector3d.directionFromRotation(pitch, yaw).scale(this.getLaserLength(entity, partialTicks));
		/*Entity renderViewEntity = Minecraft.getInstance().getCameraEntity();
		double x2 = renderViewEntity.xOld + (renderViewEntity.getX() - renderViewEntity.xOld) * partialTicks;
		double y2 = renderViewEntity.yOld + (renderViewEntity.getY() - renderViewEntity.yOld) * partialTicks;
		double z2 = renderViewEntity.zOld + (renderViewEntity.getZ() - renderViewEntity.zOld) * partialTicks;*/
		double x2 = x1 + laserDirection.x();
		double y2 = y1 + laserDirection.y();
		double z2 = z1 + laserDirection.z();
		final double laserHalfWidth = entity.laserEffectRadius();
		
		pMatrixStack.pushPose();
		
		pMatrixStack.translate(x1 - x2, y1 - y2, z1 - z2);
		
		IVertexBuilder builder = pBuffer.getBuffer(CQRRenderTypes.emissiveColorable());
		
		final float laserIncrease = 0.125F / 2;
		x1 -= (laserHalfWidth - 0.125F);
		y1 -= (laserHalfWidth - 0.125F);
		z1 -= (laserHalfWidth - 0.125F);
		x2 += (laserHalfWidth - 0.125F);
		y2 += (laserHalfWidth - 0.125F);
		z2 += (laserHalfWidth - 0.125F);
		
		//Inner white part
		drawCube(builder, x1, y1, z1, x2, y2, z2, 1F, 1F, 1F, 1F);
		
		//Middle part
		x1 -= laserIncrease;
		y1 -= laserIncrease;
		z1 -= laserIncrease;
		x2 += laserIncrease;
		y2 += laserIncrease;
		z2 += laserIncrease;
		
		float colorMultiplier = 2F / 3F;
		drawCube(builder, x1, y1, z1, x2, y2, z2, colorMultiplier * entity.getColorR(), colorMultiplier * entity.getColorG(), colorMultiplier * entity.getColorB(), colorMultiplier);
		
		//Outer most translucent part
		x1 -= laserIncrease;
		y1 -= laserIncrease;
		z1 -= laserIncrease;
		x2 += laserIncrease;
		y2 += laserIncrease;
		z2 += laserIncrease;
		
		colorMultiplier = 1F / 3F;
		drawCube(builder, x1, y1, z1, x2, y2, z2, colorMultiplier * entity.getColorR(), colorMultiplier * entity.getColorG(), colorMultiplier * entity.getColorB(), colorMultiplier);
		
		pMatrixStack.popPose();
	}
	
	protected void drawCube(IVertexBuilder builder, double x1, double y1, double z1, double x2, double y2, double z2, float colorR, float colorG, float colorB, float colorAlpha) {
		//Bottom face
		builder.vertex(x1, y1, z1).color(colorR, colorG, colorB, colorAlpha).endVertex();
		builder.vertex(x1, y1, z2).color(colorR, colorG, colorB, colorAlpha).endVertex();
		builder.vertex(x2, y1, z1).color(colorR, colorG, colorB, colorAlpha).endVertex();
		builder.vertex(x2, y1, z2).color(colorR, colorG, colorB, colorAlpha).endVertex();
		
		//Top face
		builder.vertex(x1, y2, z1).color(colorR, colorG, colorB, colorAlpha).endVertex();
		builder.vertex(x1, y2, z2).color(colorR, colorG, colorB, colorAlpha).endVertex();
		builder.vertex(x2, y2, z1).color(colorR, colorG, colorB, colorAlpha).endVertex();
		builder.vertex(x2, y2, z2).color(colorR, colorG, colorB, colorAlpha).endVertex();
		
		//X+ face
		builder.vertex(x1, y1, z1).color(colorR, colorG, colorB, colorAlpha).endVertex();
		builder.vertex(x1, y1, z2).color(colorR, colorG, colorB, colorAlpha).endVertex();
		builder.vertex(x1, y2, z1).color(colorR, colorG, colorB, colorAlpha).endVertex();
		builder.vertex(x1, y2, z2).color(colorR, colorG, colorB, colorAlpha).endVertex();
		
		//X- face
		builder.vertex(x2, y1, z1).color(colorR, colorG, colorB, colorAlpha).endVertex();
		builder.vertex(x2, y1, z2).color(colorR, colorG, colorB, colorAlpha).endVertex();
		builder.vertex(x2, y2, z1).color(colorR, colorG, colorB, colorAlpha).endVertex();
		builder.vertex(x2, y2, z2).color(colorR, colorG, colorB, colorAlpha).endVertex();
		
		//Z+ face
		builder.vertex(x1, y1, z1).color(colorR, colorG, colorB, colorAlpha).endVertex();
		builder.vertex(x1, y2, z1).color(colorR, colorG, colorB, colorAlpha).endVertex();
		builder.vertex(x2, y1, z1).color(colorR, colorG, colorB, colorAlpha).endVertex();
		builder.vertex(x2, y2, z1).color(colorR, colorG, colorB, colorAlpha).endVertex();
		
		//Z- face
		builder.vertex(x1, y1, z2).color(colorR, colorG, colorB, colorAlpha).endVertex();
		builder.vertex(x1, y2, z2).color(colorR, colorG, colorB, colorAlpha).endVertex();
		builder.vertex(x2, y1, z2).color(colorR, colorG, colorB, colorAlpha).endVertex();
		builder.vertex(x2, y2, z2).color(colorR, colorG, colorB, colorAlpha).endVertex();
	}
	
	/*@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		EmissiveUtil.preEmissiveTextureRendering();

		if (entity.caster == null) {
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
		double x1 = entity.caster.lastTickPosX + (entity.caster.posX - entity.caster.lastTickPosX) * partialTicks;
		double y1 = entity.caster.lastTickPosY + (entity.caster.posY - entity.caster.lastTickPosY) * partialTicks;
		double z1 = entity.caster.lastTickPosZ + (entity.caster.posZ - entity.caster.lastTickPosZ) * partialTicks;
		Vec3d offset = entity.getOffsetVector();
		x1 += offset.x;
		y1 += offset.y;
		z1 += offset.z;
		float yaw = this.getYaw(entity, partialTicks);
		float pitch = this.getPitch(entity, partialTicks);
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

	protected double getLaserLength(T entity, float pitch, float yaw) {
		Vector3d start = entity.position();
		Vector3d end = start.add(Vector3d.directionFromRotation(pitch, yaw).scale(entity.length));
		RayTraceContext rtc = new RayTraceContext(start, end, BlockMode.COLLIDER, FluidMode.NONE, null);
		RayTraceResult result = entity.level.clip(rtc);
		double d = result != null ? (float) result.getLocation().subtract(entity.position()).length() : entity.length;

		return d;
	}

	protected float interpolateRotation(float prevRotation, float rotation, float partialTicks) {
		return prevRotation + MathHelper.wrapDegrees(rotation - prevRotation) * partialTicks;
	}


	@Override
	public ResourceLocation getTextureLocation(T pEntity) {
		return null;
	}

}
