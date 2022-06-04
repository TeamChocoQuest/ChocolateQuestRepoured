package team.cqr.cqrepoured.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
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
		if (entity.caster == null) {
			return;
		}

		double x1 = entity.caster.xOld + (entity.caster.getX() - entity.caster.xOld) * partialTicks;
		double y1 = entity.caster.yOld + (entity.caster.getY() - entity.caster.yOld) * partialTicks;
		double z1 = entity.caster.zOld + (entity.caster.getZ() - entity.caster.zOld) * partialTicks;
		Vector3d offset = entity.getOffsetVector();
		x1 += offset.x;
		y1 += offset.y;
		z1 += offset.z;
		float yaw = this.getYaw(entity, partialTicks);
		float pitch = this.getPitch(entity, partialTicks);
		double x2 = entity.xOld + (entity.getX() - entity.xOld) * partialTicks;
		double y2 = entity.yOld + (entity.getY() - entity.yOld) * partialTicks;
		double z2 = entity.zOld + (entity.getZ() - entity.zOld) * partialTicks;

		pMatrixStack.pushPose();
		pMatrixStack.translate(x1 - x2, y1 - y2, z1 - z2);
		pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - yaw));
		pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(-pitch));
		pMatrixStack.scale(-1.0F, -1.0F, 1.0F);

		float laserLength = this.getLaserLength(entity, pitch, yaw);
		this.renderModel(entity, yaw, pitch, partialTicks, pMatrixStack, pBuffer, pPackedLight, laserLength);

		pMatrixStack.popPose();
	}

	public void renderModel(T entity, float yaw, float pitch, float partialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, float laserLength) {
		IVertexBuilder vertexBuilder = pBuffer.getBuffer(CQRRenderTypes.laser());

		pMatrixStack.pushPose();
		float width1 = 1.0F;
		pMatrixStack.scale(width1, width1, laserLength);
		float alpha1 = 1.0F / 3.0F;
		this.model.renderToBuffer(pMatrixStack, vertexBuilder, pPackedLight, 0, entity.getColorR(), entity.getColorG(), entity.getColorB(), alpha1);
		pMatrixStack.popPose();

		pMatrixStack.pushPose();
		float width2 = 2.0F / 3.0F;
		pMatrixStack.scale(width2, width2, laserLength);
		float alpha2 = 2.0F / 3.0F;
		this.model.renderToBuffer(pMatrixStack, vertexBuilder, pPackedLight, 0, entity.getColorR(), entity.getColorG(), entity.getColorB(), alpha2);
		pMatrixStack.popPose();

		pMatrixStack.pushPose();
		float width3 = 1.0F / 3.0F;
		pMatrixStack.scale(width3, width3, laserLength);
		float alpha3 = 1.0F;
		this.model.renderToBuffer(pMatrixStack, vertexBuilder, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha3);
		pMatrixStack.popPose();
	}

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
		BlockRayTraceResult result = entity.level.clip(rtc);
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
