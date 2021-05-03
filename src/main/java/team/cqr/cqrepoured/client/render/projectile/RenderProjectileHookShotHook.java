package team.cqr.cqrepoured.client.render.projectile;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.client.models.ModelHook;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileHookShotHook;
import team.cqr.cqrepoured.util.Reference;

public class RenderProjectileHookShotHook extends Render<ProjectileHookShotHook> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/hook.png");
	private final ModelBase model = new ModelHook();

	public RenderProjectileHookShotHook(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(ProjectileHookShotHook entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		float yaw = 180.0F - (entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks);
		float pitch = -(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks);
		GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
		GlStateManager.translate(0.0F, 0.0F, -0.35F);
		GlStateManager.scale(0.35F, 0.35F, 0.35F);

		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}

		this.bindEntityTexture(entity);
		this.doRenderHook(entity, x, y, z, entityYaw, partialTicks);

		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.popMatrix();

		this.renderChain(entity, partialTicks);

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	public void doRenderHook(ProjectileHookShotHook entity, double x, double y, double z, float entityYaw, float partialTicks) {
		this.model.render(entity, 0, 0, 0, 0, 0, 0.4F);
	}

	private void renderChain(ProjectileHookShotHook entity, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.disableTexture2D();
		GlStateManager.glLineWidth(2.0F);

		// calculate chain start and end point relative to the renderViewEntity
		double x1 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
		double y1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
		double z1 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
		double x2 = entity.getThrower().lastTickPosX + (entity.getThrower().posX - entity.getThrower().lastTickPosX) * partialTicks;
		double y2 = entity.getThrower().lastTickPosY + (entity.getThrower().posY - entity.getThrower().lastTickPosY) * partialTicks;
		y2 += entity.getThrower().height * 0.7D;
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
		double minSegmentLength = 0.125D;
		int segmentCount = (int) (dist / minSegmentLength);
		double segmentLength = dist / segmentCount;

		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex3d(x2, y2, z2);
		for (int i = 1; i < segmentCount; i++) {
			double dy = MathHelper.sin((float) (i * Math.PI / segmentCount));
			GL11.glVertex3d(x2 + (x4 * i * segmentLength), y2 + (y4 * i * segmentLength) + (dy * sagginess), z2 + (z4 * i * segmentLength));
		}
		GL11.glVertex3d(x1, y1, z1);
		GL11.glEnd();

		GlStateManager.glLineWidth(1.0F);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(ProjectileHookShotHook entity) {
		return TEXTURE;
	}

}
