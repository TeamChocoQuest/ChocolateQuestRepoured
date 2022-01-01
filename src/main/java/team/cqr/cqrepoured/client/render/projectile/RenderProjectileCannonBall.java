package team.cqr.cqrepoured.client.render.projectile;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.entity.ModelCannonBall;
import team.cqr.cqrepoured.entity.projectiles.ProjectileCannonBall;

public class RenderProjectileCannonBall extends EntityRenderer<ProjectileCannonBall> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/ball_cannon.png");

	private final ModelBase model = new ModelCannonBall();

	public RenderProjectileCannonBall(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(ProjectileCannonBall entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		GlStateManager.disableCull();
		GlStateManager.scale(0.875F, 0.875F, 0.875F);
		GlStateManager.scale(-1, -1, -1);

		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}

		this.bindEntityTexture(entity);
		this.model.render(entity, 0, 0, 0, 0, 0, 0.0625F);

		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.enableCull();
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(ProjectileCannonBall entity) {
		return TEXTURE;
	}

}
