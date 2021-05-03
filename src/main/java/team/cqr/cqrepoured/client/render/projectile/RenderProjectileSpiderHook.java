package team.cqr.cqrepoured.client.render.projectile;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileHookShotHook;

public class RenderProjectileSpiderHook extends RenderProjectileHookShotHook {

	public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/blocks/web.png");

	public RenderProjectileSpiderHook(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRenderHook(ProjectileHookShotHook entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		this.bindEntityTexture(entity);
		GlStateManager.scale(1.5, 1.5, 1.5);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		bufferbuilder.pos(-0.5D, -0.25D, 0.0D).tex(0.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		bufferbuilder.pos(0.5D, -0.25D, 0.0D).tex(1.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		bufferbuilder.pos(0.5D, 0.75D, 0.0D).tex(1.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		bufferbuilder.pos(-0.5D, 0.75D, 0.0D).tex(0.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		tessellator.draw();
		GlStateManager.popMatrix();
	}
	
	@Override
	protected ResourceLocation getEntityTexture(ProjectileHookShotHook entity) {
		return RenderProjectileSpiderHook.TEXTURE;
	}

}
