/*package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.entity.ModelCalamityCrystal;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCalamityCrystal;

public class RenderCalamityCrystal extends EntityRenderer<EntityCalamityCrystal> {

	private static final ResourceLocation ENDER_CRYSTAL_TEXTURES = new ResourceLocation(CQRMain.MODID, "textures/entity/boss/calamity_crystal.png");
	private final ModelBase modelEnderCrystal = new ModelCalamityCrystal(0.0F);

	public RenderCalamityCrystal(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCalamityCrystal entity) {
		return RenderCalamityCrystal.ENDER_CRYSTAL_TEXTURES;
	}

	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
	}

	@Override
	public void doRender(EntityCalamityCrystal entity, double x, double y, double z, float entityYaw, float partialTicks) {
		float f = entity.innerRotation + partialTicks;
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		this.bindTexture(ENDER_CRYSTAL_TEXTURES);
		float f1 = MathHelper.sin(f * 0.2F) / 2.0F + 0.5F;
		f1 = f1 * f1 + f1;

		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}

		this.modelEnderCrystal.render(entity, 0.0F, f * 3.0F, f1 * 0.2F, 0.0F, 0.0F, 0.0625F);

		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.popMatrix();
		BlockPos blockpos = entity.getBeamTarget();

		if (blockpos != null) {
			this.bindTexture(EnderDragonRenderer.ENDERCRYSTAL_BEAM_TEXTURES);
			float targetX = blockpos.getX() + 0.5F;
			float targetY = blockpos.getY(); // + 0.5F;
			float targetZ = blockpos.getZ() + 0.5F;
			double targetVectorX = targetX - entity.posX;
			double targetVectorY = targetY - entity.posY;
			double targetVectorZ = targetZ - entity.posZ;
			GlStateManager.pushAttrib();
			EnderDragonRenderer.renderCrystalBeams(x + targetVectorX, y - 0.3D + f1 * 0.4F + targetVectorY, z + targetVectorZ, partialTicks, targetX, targetY, targetZ, entity.innerRotation, entity.posX, entity.posY, entity.posZ);
			GlStateManager.popAttrib();
		}

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

}*/
