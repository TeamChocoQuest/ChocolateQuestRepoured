package team.cqr.cqrepoured.client.render.projectile;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileThrownBlock;

public class RenderProjectileThrownBlock extends Render<ProjectileThrownBlock> {

	public RenderProjectileThrownBlock(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(ProjectileThrownBlock entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

	@Override
	public void doRender(ProjectileThrownBlock entity, double x, double y, double z, float entityYaw, float partialTicks) {
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		this.bindEntityTexture(entity);
		GlStateManager.translate(-0.25F, 0F, 0.25F);
		// GlStateManager.rotate(entity.ticksExisted * 7, 1.0F, 1.0F, 1.0F);
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		WorldClient world = Minecraft.getMinecraft().world;
		double dx = entity.posX + (-0.5 + (world.rand.nextDouble()));
		double dy = 0.25 + entity.posY + (-0.5 + (world.rand.nextDouble()));
		double dz = entity.posZ + (-0.5 + (world.rand.nextDouble()));
		world.spawnParticle(EnumParticleTypes.FLAME, dx, dy, dz, 0, 0, 0);
		blockrendererdispatcher.renderBlockBrightness(entity.getBlock(), 8);
		// GlStateManager.translate(0.25F, 0.0F, 0.55F);
		GL11.glPopMatrix();
	}
	
}
