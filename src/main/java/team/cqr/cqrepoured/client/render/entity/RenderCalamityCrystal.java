package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.client.models.entities.ModelCalamityCrystal;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCalamityCrystal;

public class RenderCalamityCrystal extends Render<EntityCalamityCrystal> {

	private static final ResourceLocation ENDER_CRYSTAL_TEXTURES = new ResourceLocation("textures/entity/endercrystal/endercrystal.png");
	private final ModelBase modelEnderCrystal = new ModelCalamityCrystal(0.0F);

	public RenderCalamityCrystal(RenderManager renderManager) {
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
		float f = (float) entity.innerRotation + partialTicks;
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
			this.bindTexture(RenderDragon.ENDERCRYSTAL_BEAM_TEXTURES);
			float f2 = (float) blockpos.getX() + 0.5F;
			float f3 = (float) blockpos.getY() + 0.5F;
			float f4 = (float) blockpos.getZ() + 0.5F;
			double d0 = (double) f2 - entity.posX;
			double d1 = (double) f3 - entity.posY;
			double d2 = (double) f4 - entity.posZ;
			GlStateManager.pushAttrib();
			RenderDragon.renderCrystalBeams(x + d0, y - 0.3D + (double) (f1 * 0.4F) + d1, z + d2, partialTicks, (double) f2, (double) f3, (double) f4, entity.innerRotation, entity.posX, entity.posY, entity.posZ);
			GlStateManager.popAttrib();
		}

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

}
