package team.cqr.cqrepoured.client.render.projectile;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import team.cqr.cqrepoured.entity.projectiles.ProjectileHotFireball;

public class RenderProjectileHotFireball extends EntityRenderer<ProjectileHotFireball> {

	public RenderProjectileHotFireball(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(ProjectileHotFireball entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}

	@Override
	public void doRender(ProjectileHotFireball entity, double x, double y, double z, float entityYaw, float partialTicks) {
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		this.bindEntityTexture(entity);
		GlStateManager.translate(-0.25F, 0F, 0.25F);
		// GlStateManager.rotate(entity.ticksExisted * 7, 1.0F, 1.0F, 1.0F);
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		ClientWorld world = Minecraft.getMinecraft().world;
		double dx = entity.posX + (-0.5 + (world.rand.nextDouble()));
		double dy = 0.25 + entity.posY + (-0.5 + (world.rand.nextDouble()));
		double dz = entity.posZ + (-0.5 + (world.rand.nextDouble()));
		world.spawnParticle(ParticleTypes.FLAME, dx, dy, dz, 0, 0, 0);
		blockrendererdispatcher.renderBlockBrightness(Blocks.OBSIDIAN.getDefaultState(), 8);
		// GlStateManager.translate(0.25F, 0.0F, 0.55F);
		GL11.glPopMatrix();
	}

}
