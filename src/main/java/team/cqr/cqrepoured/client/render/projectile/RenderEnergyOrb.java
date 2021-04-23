package team.cqr.cqrepoured.client.render.projectile;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.client.models.entities.ModelEnergyOrb;
import team.cqr.cqrepoured.client.util.BossDeathRayHelper;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileEnergyOrb;
import team.cqr.cqrepoured.util.Reference;

public class RenderEnergyOrb extends Render<ProjectileEnergyOrb> {

	private static final ResourceLocation ENDER_CRYSTAL_TEXTURES = new ResourceLocation(Reference.MODID, "textures/entity/boss/energy_orb.png");
	private final ModelBase modelEnderCrystal = new ModelEnergyOrb(0.0F);
	private final BossDeathRayHelper rayHelper;

	public RenderEnergyOrb(RenderManager renderManager) {
		super(renderManager);
		this.rayHelper = new BossDeathRayHelper(255, 255, 0, 1, 15);
	}

	@Override
	protected ResourceLocation getEntityTexture(ProjectileEnergyOrb entity) {
		return RenderEnergyOrb.ENDER_CRYSTAL_TEXTURES;
	}

	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
	}

	@Override
	public void doRender(ProjectileEnergyOrb entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}


	@Override
	public boolean isMultipass() {
		return true;
	}
	
	@Override
	public void renderMultipass(ProjectileEnergyOrb entityIn, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GlStateManager.translate((float) x, (float) y, (float) z);
		GlStateManager.translate(0.0F, 0.5F, 0.0F);
		int ticks = entityIn.ticksExisted + 200;
		if (ticks > 0) {
			this.rayHelper.renderRays(ticks, partialTicks);
		}
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}
}
