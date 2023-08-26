package team.cqr.cqrepoured.client.render.projectile;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.client.model.entity.ModelEnergyOrb;
import team.cqr.cqrepoured.client.util.BossDeathRayHelper;
import team.cqr.cqrepoured.entity.projectiles.ProjectileEnergyOrb;

public class RenderEnergyOrb extends EntityRenderer<ProjectileEnergyOrb> {

	private static final ResourceLocation ENDER_CRYSTAL_TEXTURES = new ResourceLocation(CQRConstants.MODID, "textures/entity/boss/energy_orb.png");
	private final Model modelEnderCrystal = new ModelEnergyOrb();
	private final BossDeathRayHelper rayHelper;

	public RenderEnergyOrb(EntityRendererProvider.Context renderManager) {
		super(renderManager);
		this.rayHelper = new BossDeathRayHelper(255, 255, 0, 1, 15);
	}

	@Override
	public ResourceLocation getTextureLocation(ProjectileEnergyOrb pEntity) {
		return RenderEnergyOrb.ENDER_CRYSTAL_TEXTURES;
	}

	@Override
	public void render(ProjectileEnergyOrb entity, float pEntityYaw, float partialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
		float f = entity.innerRotation + partialTicks;
		pMatrixStack.pushPose();
		//GlStateManager.translate((float) x, (float) y, (float) z);
		//this.bindTexture(ENDER_CRYSTAL_TEXTURES);
		float f1 = Mth.sin(f * 0.2F) / 2.0F + 0.5F;
		f1 = f1 * f1 + f1;

		/*if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}*/

		float color = 0.5F + (float) (0.25F * (1 + Math.sin(entity.tickCount / Math.PI)));

		this.modelEnderCrystal.renderToBuffer(pMatrixStack, pBuffer.getBuffer(RenderType.entityCutout(ENDER_CRYSTAL_TEXTURES)), pPackedLight, OverlayTexture.NO_OVERLAY, color, color, 0, 0);
		
		/*if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}*/
		pMatrixStack.popPose();
		super.render(entity, pEntityYaw, partialTicks, pMatrixStack, pBuffer, pPackedLight);
		int ticks = entity.tickCount + 200;
		this.rayHelper.renderRays(pMatrixStack, pBuffer.getBuffer(RenderType.lightning()), ticks, partialTicks);
	}

}
