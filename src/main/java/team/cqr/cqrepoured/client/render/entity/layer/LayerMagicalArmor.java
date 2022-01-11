package team.cqr.cqrepoured.client.render.entity.layer;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class LayerMagicalArmor<T extends AbstractEntityCQR, M extends EntityModel<T>> extends LayerRenderer<T, M> {

	private final ResourceLocation texture;

	public LayerMagicalArmor(IEntityRenderer<T, M> renderer, ResourceLocation texture) {
		super(renderer);
		this.texture = texture;
	}

	@Override
	public void render(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount,
			float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		if (pLivingEntity.isMagicArmorActive()) {
			// TODO does this work?
			GlStateManager._matrixMode(GL11.GL_TEXTURE);
			GlStateManager._loadIdentity();
			float f = pLivingEntity.tickCount + pPartialTicks;
			float f1 = MathHelper.cos(f * 0.02F) * 3.0F;
			float f2 = f * 0.01F;
			GlStateManager._translated(f1, f2, 0.0F);
			GlStateManager._matrixMode(GL11.GL_MODELVIEW);

			// TODO use appropriate render type
			/**
			 * GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());
			 * GlStateManager.disableLighting();
			 * GlStateManager.enableBlend();
			 * GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
			 * Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
			 */
			this.getParentModel().renderToBuffer(pMatrixStack, pBuffer.getBuffer(RenderType.entityTranslucent(this.texture)), pPackedLight,
					OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);

			GlStateManager._matrixMode(GL11.GL_TEXTURE);
			GlStateManager._loadIdentity();
			GlStateManager._matrixMode(GL11.GL_MODELVIEW);
		}
	}

}
