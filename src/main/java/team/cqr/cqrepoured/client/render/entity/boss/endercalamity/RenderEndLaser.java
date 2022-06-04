package team.cqr.cqrepoured.client.render.entity.boss.endercalamity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import team.cqr.cqrepoured.client.init.CQRRenderTypes;
import team.cqr.cqrepoured.client.render.entity.RenderLaser;
import team.cqr.cqrepoured.client.util.PentagramUtil;
import team.cqr.cqrepoured.entity.boss.AbstractEntityLaser;

public class RenderEndLaser<T extends AbstractEntityLaser> extends RenderLaser<T> {

	public RenderEndLaser(EntityRendererManager renderManager) {
		super(renderManager);
	}
	
	@Override
	public void render(T entity, float pEntityYaw, float partialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
		super.render(entity, pEntityYaw, partialTicks, pMatrixStack, pBuffer, pPackedLight);
		//int renderPass = MinecraftForgeClient.getRenderPass();
		// Solid objects
		//if (renderPass == 0) {
			float yaw = this.getYaw(entity, partialTicks);
			float pitch = this.getPitch(entity, partialTicks);

			// World coordinates
			double x1 = entity.caster.xOld + (entity.caster.getX() - entity.caster.xOld) * partialTicks;
			double y1 = entity.caster.yOld + (entity.caster.getY() - entity.caster.yOld) * partialTicks + entity.caster.getBbHeight() * 0.6D;
			double z1 = entity.caster.zOld + (entity.caster.getZ() - entity.caster.zOld) * partialTicks;

			Vector3d laserDirection = Vector3d.directionFromRotation(pitch, yaw).scale(0.5D);
			x1 += laserDirection.x;
			y1 += laserDirection.y;
			z1 += laserDirection.z;

			Vector3d worldPos = new Vector3d(x1, y1, z1);

			// REnder ring 1
			float ticks = 0.25F * entity.tickCount;
			float colorMultiplier = (float) (0.5F + 0.25F * (1 + Math.sin(ticks)));
			this.renderRing(pMatrixStack, 5, worldPos, entity, pitch, yaw, 1F, partialTicks, laserDirection, colorMultiplier);
			if (entity.length >= 4) {
				Vector3d increment = Vector3d.directionFromRotation(pitch, yaw).normalize().scale(4);
				worldPos = worldPos.add(increment);
				colorMultiplier = (float) (0.5F + 0.25F * (1 + Math.sin(ticks + (Math.PI / 2))));
				this.renderRing(pMatrixStack, 7, worldPos, entity, pitch, yaw, 1.5F, partialTicks, laserDirection, colorMultiplier);
				if (entity.length >= 8) {
					worldPos = worldPos.add(increment);
					colorMultiplier = (float) (0.5F + 0.25F * (1 + Math.sin(ticks + Math.PI)));
					this.renderRing(pMatrixStack, 9, worldPos, entity, pitch, yaw, 2F, partialTicks, laserDirection, colorMultiplier);
				}
			}
		/*}
		// Transparent objects
		else if (renderPass == 1) {
			GlStateManager.pushAttrib();
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
			GlStateManager.popAttrib();
		}*/

	}

	private void renderRing(MatrixStack matrixStack, int corners, Vector3d worldPos, T entity, float pitch, float yaw, float scale, float partialTicks, Vector3d laserDirection, float colorMultiplier) {
		matrixStack.pushPose();

		// View coordinates
		Entity renderViewEntity = Minecraft.getInstance().getCameraEntity();
		double x2 = renderViewEntity.xOld + (renderViewEntity.getX() - renderViewEntity.xOld) * partialTicks;
		double y2 = renderViewEntity.yOld + (renderViewEntity.getY() - renderViewEntity.yOld) * partialTicks;
		double z2 = renderViewEntity.zOld + (renderViewEntity.getZ() - renderViewEntity.zOld) * partialTicks;
		// From world to view coordinates...
		matrixStack.translate(worldPos.x - x2, worldPos.y - y2, worldPos.z - z2);

		matrixStack.scale(scale, scale, scale);

		// Rotate pentagram
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - yaw));
		matrixStack.mulPose(Vector3f.XP.rotationDegrees(-pitch - 90.0F));
		matrixStack.scale(-1.0F, -1.0F, 1.0F);
		PentagramUtil.renderPentagram(matrixStack, CQRRenderTypes.emissiveSolid(), entity.getColorR() * colorMultiplier, entity.getColorG() * colorMultiplier, entity.getColorB() * colorMultiplier, corners);
		matrixStack.popPose();
	}

}
