package team.cqr.cqrepoured.client.render.projectile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.render.RenderSpriteBase;
import team.cqr.cqrepoured.entity.projectiles.ProjectileHomingEnderEye;

public class RenderProjectileHomingEnderEye extends RenderSpriteBase<ProjectileHomingEnderEye> {

	public RenderProjectileHomingEnderEye(EntityRendererManager renderManager)
	{
		super(renderManager, new ResourceLocation("textures/items/ender_eye.png"));
	}

	@Override
	public void render(ProjectileHomingEnderEye entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {

		// if(entity.ticksExisted % 4 == 0) {
		ClientWorld world = Minecraft.getInstance().level;
		double dx = entity.getX() + (-0.25 + (0.5 * world.random.nextDouble()));
		double dy = 0.125 + entity.getY() + (-0.25 + (0.5 * world.random.nextDouble()));
		double dz = entity.getZ() + (-0.25 + (0.5 * world.random.nextDouble()));
		world.addParticle(ParticleTypes.DRAGON_BREATH, dx, dy, dz, 0, 0, 0);
		// }

		super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
	}

}
