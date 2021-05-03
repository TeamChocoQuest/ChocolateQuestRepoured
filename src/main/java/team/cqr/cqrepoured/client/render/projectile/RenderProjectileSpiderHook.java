package team.cqr.cqrepoured.client.render.projectile;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileHookShotHook;

public class RenderProjectileSpiderHook extends RenderProjectileHookShotHook {

	public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/blocks/web.png");

	public RenderProjectileSpiderHook(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected void doRenderHook(ProjectileHookShotHook entity, double x, double y, double z, float entityYaw, float partialTicks) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

		GlStateManager.pushMatrix();
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		BlockPos pos = new BlockPos(entity);
		IBlockState state = entity.world.getBlockState(pos);
		int lightmapCoords = state.getPackedLightmapCoords(entity.world, pos);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapCoords & 0xFFFF, lightmapCoords >>> 16);

		this.bindEntityTexture(entity);
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(-0.5D, -0.5D, -0.5D).tex(0.0D, 1.0D).endVertex();
		bufferbuilder.pos(0.5D, -0.5D, 0.5D).tex(1.0D, 1.0D).endVertex();
		bufferbuilder.pos(0.5D, 0.5D, 0.5D).tex(1.0D, 0.0D).endVertex();
		bufferbuilder.pos(-0.5D, 0.5D, -0.5D).tex(0.0D, 0.0D).endVertex();

		bufferbuilder.pos(-0.5D, -0.5D, 0.5D).tex(0.0D, 1.0D).endVertex();
		bufferbuilder.pos(0.5D, -0.5D, -0.5D).tex(1.0D, 1.0D).endVertex();
		bufferbuilder.pos(0.5D, 0.5D, -0.5D).tex(1.0D, 0.0D).endVertex();
		bufferbuilder.pos(-0.5D, 0.5D, 0.5D).tex(0.0D, 0.0D).endVertex();
		tessellator.draw();

		GlStateManager.enableLighting();
		GlStateManager.enableCull();

		GlStateManager.popMatrix();
	}

	protected void renderChain(ProjectileHookShotHook entity, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.glLineWidth(2.0F);
		GlStateManager.color(0.9F, 0.9F, 0.9F, 1.0F);

		// calculate chain start and end point relative to the renderViewEntity
		double x1 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
		double y1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
		double z1 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
		double x2 = entity.getThrower().lastTickPosX + (entity.getThrower().posX - entity.getThrower().lastTickPosX) * partialTicks;
		double y2 = entity.getThrower().lastTickPosY + (entity.getThrower().posY - entity.getThrower().lastTickPosY) * partialTicks;
		y2 += entity.getThrower().height * 0.65D;
		double z2 = entity.getThrower().lastTickPosZ + (entity.getThrower().posZ - entity.getThrower().lastTickPosZ) * partialTicks;
		Entity entity1 = Minecraft.getMinecraft().getRenderViewEntity();
		double x3 = entity1.lastTickPosX + (entity1.posX - entity1.lastTickPosX) * partialTicks;
		double y3 = entity1.lastTickPosY + (entity1.posY - entity1.lastTickPosY) * partialTicks;
		double z3 = entity1.lastTickPosZ + (entity1.posZ - entity1.lastTickPosZ) * partialTicks;
		x1 -= x3;
		y1 -= y3;
		z1 -= z3;
		x2 -= x3;
		y2 -= y3;
		z2 -= z3;

		// calculate chain direction, segmentCount and segmentLength
		double x4 = x1 - x2;
		double y4 = y1 - y2;
		double z4 = z1 - z2;
		double dist = Math.sqrt(x4 * x4 + y4 * y4 + z4 * z4);
		double d = 1.0D / dist;
		x4 *= d;
		y4 *= d;
		z4 *= d;
		double sagginess = -0.3D;
		double minSegmentLength = 8.0D / 16.0D;
		int segmentCount = (int) (dist / minSegmentLength);
		double segmentLength = dist / segmentCount;

		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex3d(x2, y2, z2);
		for (int i = 0; i < segmentCount; i++) {
			double dy = MathHelper.sin((float) (i * Math.PI / segmentCount));
			GL11.glVertex3d(x2 + (x4 * i * segmentLength), y2 + (y4 * i * segmentLength) + (dy * sagginess), z2 + (z4 * i * segmentLength));
		}
		GL11.glVertex3d(x1, y1, z1);
		GL11.glEnd();

		GlStateManager.glLineWidth(1.0F);
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(ProjectileHookShotHook entity) {
		return RenderProjectileSpiderHook.TEXTURE;
	}

}
