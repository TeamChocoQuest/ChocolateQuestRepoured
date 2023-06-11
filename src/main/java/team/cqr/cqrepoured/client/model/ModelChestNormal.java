package team.cqr.cqrepoured.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelChestNormal extends EntityModel {

	public ModelRenderer chestBase;
	public ModelRenderer chestKnob;

	public ModelChestNormal() {
		this.texWidth = 64;
		this.texHeight = 32;

		this.chestBase = new ModelRenderer(this);
		this.chestBase.setPos(0.0F, 24.0F, 0.0F);
		this.chestBase.texOffs(0, 0).addBox(-7.0F, -14.0F, -7.0F, 14, 14, 14, 0.0F, false);

		this.chestKnob = new ModelRenderer(this);
		this.chestKnob.setPos(0.0F, 24.0F, 0.0F);
		this.chestKnob.texOffs(0, 0).addBox(-1.0F, -11.0F, -8.0F, 2, 4, 1, 0.0F, false);
	}

	@Override
	public void setupAnim(Entity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

	}
	@Override
	public void renderToBuffer(MatrixStack pMatrixStack, IVertexBuilder pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		this.chestBase.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
		this.chestKnob.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);

	}
}
