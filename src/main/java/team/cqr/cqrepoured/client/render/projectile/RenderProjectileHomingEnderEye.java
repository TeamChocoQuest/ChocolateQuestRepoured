package team.cqr.cqrepoured.client.render.projectile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.render.RenderSpriteBase;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileHomingEnderEye;

public class RenderProjectileHomingEnderEye extends RenderSpriteBase<ProjectileHomingEnderEye> {

	public RenderProjectileHomingEnderEye(RenderManager renderManager) {
		super(renderManager, new ResourceLocation("textures/items/ender_eye.png"));
	}

	@Override
	public void doRender(ProjectileHomingEnderEye entity, double x, double y, double z, float entityYaw, float partialTicks) {

		// if(entity.ticksExisted % 4 == 0) {
		WorldClient world = Minecraft.getMinecraft().world;
		double dx = entity.posX + (-0.25 + (0.5 * world.rand.nextDouble()));
		double dy = 0.125 + entity.posY + (-0.25 + (0.5 * world.rand.nextDouble()));
		double dz = entity.posZ + (-0.25 + (0.5 * world.rand.nextDouble()));
		world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, dx, dy, dz, 0, 0, 0);
		// }

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

}
