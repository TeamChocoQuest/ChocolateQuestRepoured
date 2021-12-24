package team.cqr.cqrepoured.client.render.entity.layer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class LayerMagicalArmor<R extends RenderCQREntity<E>, E extends AbstractEntityCQR> extends AbstractLayerCQR<R, E> {

	protected final ModelBase model;
	protected final ResourceLocation texture;

	public LayerMagicalArmor(R renderer, ModelBase model, ResourceLocation texture) {
		super(renderer);
		this.model = model;
		this.texture = texture;
	}

	@Override
	public void doRenderLayer(E entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch,
			float scale) {
		if (!entity.isMagicArmorActive()) {
			return;
		}
		GlStateManager.depthMask(!entity.isInvisible());
		this.renderer.bindTexture(this.texture);
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		float f = entity.ticksExisted + partialTicks;
		float f1 = MathHelper.cos(f * 0.02F) * 3.0F;
		float f2 = f * 0.01F;
		GlStateManager.translate(f1, f2, 0.0F);
		GlStateManager.matrixMode(5888);
		GlStateManager.enableBlend();
		GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
		this.model.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
		this.model.setModelAttributes(this.renderer.getMainModel());
		Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
		this.model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(5888);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
