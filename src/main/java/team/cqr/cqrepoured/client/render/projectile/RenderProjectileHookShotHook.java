package team.cqr.cqrepoured.client.render.projectile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.client.models.ModelChain;
import team.cqr.cqrepoured.client.models.ModelHook;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileHookShotHook;
import team.cqr.cqrepoured.util.Reference;

public class RenderProjectileHookShotHook extends Render<ProjectileHookShotHook> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/hook.png");
	private final ModelBase model = new ModelHook();
	private final ModelBase chainModel = new ModelChain();

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

	protected void doRenderHook(ProjectileHookShotHook entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.scale(2.0F, 2.0F, 2.0F);
		GlStateManager.translate(0.0F, 0.0F, -6.0F * 0.0625F);
		this.model.render(entity, 0, 0, 0, 0, 0, 0.0625F);
	}

	protected void renderChain(ProjectileHookShotHook entity, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.disableTexture2D();
		GlStateManager.color(0.25f, 0.25f, 0.25f, 1.0F);

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
		double segmentLength = 7.0D / 16.0D;
		int segmentCount = (int) (dist / segmentLength);
		float yaw = 180 + (float) Math.toDegrees(Math.atan2(x4, z4));
		float pitch = (float) Math.toDegrees(Math.atan2(y4, Math.sqrt(x4 * x4 + z4 * z4)));

		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
		for (int i = 0; i < segmentCount; i++) {
			double x = x1 - (x4 * i * segmentLength);
			double y = y1 - (y4 * i * segmentLength);
			double z = z1 - (z4 * i * segmentLength);

			mutablePos.setPos(x3 + x, y3 + y, z3 + z);
			IBlockState state = entity.world.getBlockState(mutablePos);
			int lightmapCoords = state.getPackedLightmapCoords(entity.world, mutablePos);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapCoords & 0xFFFF, lightmapCoords >>> 16);

			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);
			GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(40.0F + (i % 2 == 0 ? 90.0F : 0), 0.0F, 0.0F, 1.0F);
			this.chainModel.render(entity, 0.0F, 0.0F, entity.ticksExisted + partialTicks, 0.0F, 0.0F, 0.0625F);
			GlStateManager.popMatrix();
		}

		GlStateManager.enableTexture2D();
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(ProjectileHookShotHook entity) {
		return TEXTURE;
	}

}
