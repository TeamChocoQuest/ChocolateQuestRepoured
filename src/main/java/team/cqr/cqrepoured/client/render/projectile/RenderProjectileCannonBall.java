package team.cqr.cqrepoured.client.render.projectile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.entity.ModelCannonBall;
import team.cqr.cqrepoured.entity.projectiles.ProjectileCannonBall;

public class RenderProjectileCannonBall extends EntityRenderer<ProjectileCannonBall> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/ball_cannon.png");

	private final EntityModel model = new ModelCannonBall();

	public RenderProjectileCannonBall(EntityRendererManager renderManager)
	{
		super(renderManager);
	}

	@Override
	public void render(ProjectileCannonBall cannonBall, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
		matrixStack.pushPose();
		RenderSystem.disableCull();
		matrixStack.scale(0.875F, 0.875F, 0.875F);
		matrixStack.scale(-1, -1, -1);

		IVertexBuilder builder = buffer.getBuffer(RenderType.entityCutout(TEXTURE));
		this.model.renderToBuffer(matrixStack, builder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

		RenderSystem.enableCull();
		matrixStack.popPose();
		super.render(cannonBall, entityYaw, partialTicks, matrixStack, buffer, packedLight);
		/*GlStateManager.pushMatrix();
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
		super.doRender(entity, x, y, z, entityYaw, partialTicks); */
	}

	@Override
	public ResourceLocation getTextureLocation(ProjectileCannonBall entity)
	{
		return TEXTURE;
	}
}
