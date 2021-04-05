package team.cqr.cqrepoured.client.render.entity.boss.endercalamity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.client.render.entity.RenderLaser;
import team.cqr.cqrepoured.client.util.PentagramUtil;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityEndLaserTargeting;

public class RenderEndLaser extends RenderLaser<EntityEndLaserTargeting> {

	public RenderEndLaser(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityEndLaserTargeting entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushAttrib();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		GlStateManager.popAttrib();
		Minecraft mc = Minecraft.getMinecraft();

		float yaw = this.getYaw(entity, partialTicks);
		float pitch = this.getPitch(entity, partialTicks);

		double x1 = entity.caster.lastTickPosX + (entity.caster.posX - entity.caster.lastTickPosX) * partialTicks;
		double y1 = entity.caster.lastTickPosY + (entity.caster.posY - entity.caster.lastTickPosY) * partialTicks + entity.caster.height * 0.6D;
		double z1 = entity.caster.lastTickPosZ + (entity.caster.posZ - entity.caster.lastTickPosZ) * partialTicks;
		
		Vec3d vec = Vec3d.fromPitchYaw(pitch, yaw).scale(0.25D);
		x1 += vec.x;
		y1 += vec.y;
		z1 += vec.z;
		Entity renderViewEntity = mc.getRenderViewEntity();
		double x2 = renderViewEntity.lastTickPosX + (renderViewEntity.posX - renderViewEntity.lastTickPosX) * partialTicks;
		double y2 = renderViewEntity.lastTickPosY + (renderViewEntity.posY - renderViewEntity.lastTickPosY) * partialTicks;
		double z2 = renderViewEntity.lastTickPosZ + (renderViewEntity.posZ - renderViewEntity.lastTickPosZ) * partialTicks;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x1 - x2, y1 - y2, z1 - z2);
		
		double entityLength = this.getLaserLength(entity, pitch, yaw);
		Vec3d laserVector = Vec3d.fromPitchYaw(this.getPitch(entity, partialTicks), this.getYaw(entity, partialTicks)).scale(entityLength);
		Vec3d startPos = new Vec3d(x2,y2,z2);
		Vec3d increment = new Vec3d(laserVector.x, laserVector.y, laserVector.z).normalize().scale(5);

		// REnder ring 1
		renderRing(5, startPos, entity, pitch, yaw, 1D, partialTicks);
		entityLength /= 2;
		if (entityLength >= 3) {
			// Render ring 2
			startPos = startPos.add(increment);

			renderRing(7, startPos, entity, pitch, yaw, 1.5D, partialTicks);
		}
		if (entityLength >= 6) {
			// Render ring 3
			startPos = startPos.add(increment);

			renderRing(9, startPos, entity, pitch, yaw, 2D, partialTicks);
		}
		GlStateManager.popMatrix();

	}

	private void renderRing(double corners, Vec3d worldPos, EntityEndLaserTargeting entity, float pitch, float yaw, double scale, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.scale(scale, 1, scale);
		//TODO: Find a better and more reliable way to transform from world to view coordinates...
		/*Entity viewEntity = Minecraft.getMinecraft().getRenderViewEntity();
        double d3 = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * (double)partialTicks;
        double d4 = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * (double)partialTicks;
        double d5 = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * (double)partialTicks;
		double x = worldPos.x - d3;
		double y = worldPos.y - d4; 
		double z = worldPos.z - d5;*/
		
		PentagramUtil.preRenderPentagram(worldPos.x,worldPos.y,worldPos.z, entity.ticksExisted);

		// Rotate pentagram
		GlStateManager.rotate(180.0F - yaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-pitch - 90.0F, 1.0F, 0.0F, 0.0F);
		PentagramUtil.renderPentagram(entity.ticksExisted, entity.getColorR(), entity.getColorG(), entity.getColorB(), corners);

		WorldClient world = Minecraft.getMinecraft().world;
		double halfScale = scale / 2;
		double dx = - halfScale + (world.rand.nextDouble() * scale);
		double dy = - halfScale + (world.rand.nextDouble() * scale);
		double dz = - halfScale + (world.rand.nextDouble() * scale);
		world.spawnParticle(EnumParticleTypes.CRIT_MAGIC, worldPos.x + dx, worldPos.y + dy, worldPos.z + dz, 0, 0, 0);
		
		PentagramUtil.postRenderPentagram();
		GlStateManager.popMatrix();
	}

}
