package team.cqr.cqrepoured.client.render.projectile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileHomingEnderEye;

public class RenderProjectileHomingEnderEye extends Render<ProjectileHomingEnderEye>{

	public ResourceLocation TEXTURE = new ResourceLocation("textures/items/ender_eye.png");

	public RenderProjectileHomingEnderEye(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(ProjectileHomingEnderEye entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		this.bindEntityTexture(entity);
		GlStateManager.translate((float) x, (float) y, (float) z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(.5F, .5F, .5F);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate((this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}

		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		bufferbuilder.pos(-0.5D, -0.25D, 0.0D).tex(0.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		bufferbuilder.pos(0.5D, -0.25D, 0.0D).tex(1.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		bufferbuilder.pos(0.5D, 0.75D, 0.0D).tex(1.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		bufferbuilder.pos(-0.5D, 0.75D, 0.0D).tex(0.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		tessellator.draw();

		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		
		if(entity.ticksExisted % 4 == 0) {
			WorldClient world = Minecraft.getMinecraft().world;
			double dx = entity.posX + (-0.25 + (0.5 * world.rand.nextDouble()));
			double dy = 0.125 + entity.posY + (-0.25 + (0.5 * world.rand.nextDouble()));
			double dz = entity.posZ + (-0.25 + (0.5 * world.rand.nextDouble()));
			world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, dx, dy, dz, 0, 0, 0);
		}
		
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(ProjectileHomingEnderEye entity) {
		return this.TEXTURE;
	}
}
