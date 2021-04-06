package team.cqr.cqrepoured.client.render.entity.boss.endercalamity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.MinecraftForgeClient;
import team.cqr.cqrepoured.client.render.entity.RenderLaser;
import team.cqr.cqrepoured.client.util.PentagramUtil;
import team.cqr.cqrepoured.objects.entity.boss.AbstractEntityLaser;

public class RenderEndLaser<T extends AbstractEntityLaser> extends RenderLaser<T> {

	public RenderEndLaser(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		int renderPass = MinecraftForgeClient.getRenderPass();
		//Solid objects
		if(renderPass == 0) {
			float yaw = this.getYaw(entity, partialTicks);
			float pitch = this.getPitch(entity, partialTicks);

			//World coordinates
			double x1 = entity.caster.lastTickPosX + (entity.caster.posX - entity.caster.lastTickPosX) * partialTicks;
			double y1 = entity.caster.lastTickPosY + (entity.caster.posY - entity.caster.lastTickPosY) * partialTicks + entity.caster.height * 0.6D;
			double z1 = entity.caster.lastTickPosZ + (entity.caster.posZ - entity.caster.lastTickPosZ) * partialTicks;

			Vec3d laserDirection = Vec3d.fromPitchYaw(pitch, yaw).scale(0.5D);
			x1 += laserDirection.x;
			y1 += laserDirection.y;
			z1 += laserDirection.z;
			
			Vec3d worldPos = new Vec3d(x1,y1,z1);
			
			// REnder ring 1
			float ticks = 0.25F * entity.ticksExisted;
			float colorMultiplier = (float) (0.5F + 0.25F * (1+ Math.sin(ticks)));
			renderRing(5, worldPos, entity, pitch, yaw, 1D, partialTicks, laserDirection, colorMultiplier);
			if (entity.length >= 4) {
				Vec3d increment = Vec3d.fromPitchYaw(pitch, yaw).normalize().scale(4);
				worldPos = worldPos.add(increment);
				colorMultiplier = (float) (0.5F + 0.25F * (1+ Math.sin(ticks + (Math.PI / 2))));
				renderRing(7, worldPos, entity, pitch, yaw, 1.5D, partialTicks, laserDirection, colorMultiplier);
				if(entity.length >= 8) {
					worldPos = worldPos.add(increment);
					colorMultiplier = (float) (0.5F + 0.25F * (1+ Math.sin(ticks + Math.PI)));
					renderRing(9, worldPos, entity, pitch, yaw, 2D, partialTicks, laserDirection, colorMultiplier);
				}
			} 
		}
		//Transparent objects
		else if (renderPass == 1) {
			GlStateManager.pushAttrib();
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
			GlStateManager.popAttrib();
		}
		
		
	}

	private void renderRing(double corners, Vec3d worldPos, T entity, float pitch, float yaw, double scale, float partialTicks, Vec3d laserDirection, float colorMultiplier) {
		GlStateManager.pushMatrix();
		
		//View coordinates
		Entity renderViewEntity = Minecraft.getMinecraft().getRenderViewEntity();
		double x2 = renderViewEntity.lastTickPosX + (renderViewEntity.posX - renderViewEntity.lastTickPosX) * partialTicks;
		double y2 = renderViewEntity.lastTickPosY + (renderViewEntity.posY - renderViewEntity.lastTickPosY) * partialTicks;
		double z2 = renderViewEntity.lastTickPosZ + (renderViewEntity.posZ - renderViewEntity.lastTickPosZ) * partialTicks;
		//From world to view coordinates...
		GlStateManager.translate(worldPos.x - x2, worldPos.y - y2, worldPos.z - z2);
		
		GlStateManager.scale(scale, scale, scale);
		PentagramUtil.preRenderPentagram(worldPos.x, worldPos.y, worldPos.z, entity.ticksExisted);

		// Rotate pentagram
		GlStateManager.rotate(180.0F - yaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-pitch - 90.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(-1.0D, -1.0D, 1.0D);
		PentagramUtil.renderPentagram(entity.ticksExisted, entity.getColorR() * colorMultiplier, entity.getColorG() * colorMultiplier, entity.getColorB() * colorMultiplier, corners);
		PentagramUtil.postRenderPentagram();
		GlStateManager.popMatrix();
	}

}
