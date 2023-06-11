package team.cqr.cqrepoured.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCalamityCrystal;

// Code largely copied from vanilla ender crystal
public class ModelCalamityCrystal extends EntityModel<EntityCalamityCrystal> {
	/** The cube model for the Ender Crystal. */
	private final ModelRenderer cube;
	/** The glass model for the Ender Crystal. */
	private final ModelRenderer glass = new ModelRenderer(this);

	public ModelCalamityCrystal(float p_i1170_1_) {
		this.glass.texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
		this.cube = new ModelRenderer(this);
		this.cube.texOffs(32, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	/*@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GlStateManager.scale(2.0F, 2.0F, 2.0F);
		GlStateManager.translate(0.0F, -0.5F, 0.0F);

		GlStateManager.rotate(limbSwingAmount, 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(0.0F, 0.8F + ageInTicks, 0.0F);
		GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
		this.glass.render(scale);
		GlStateManager.scale(0.875F, 0.875F, 0.875F);
		GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
		GlStateManager.rotate(limbSwingAmount, 0.0F, 1.0F, 0.0F);
		this.glass.render(scale);
		GlStateManager.scale(0.875F, 0.875F, 0.875F);
		GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
		GlStateManager.rotate(limbSwingAmount, 0.0F, 1.0F, 0.0F);
		if (entity instanceof EntityCalamityCrystal) {
			if (((EntityCalamityCrystal) entity).isAbsorbing()) {
				GlStateManager.color(1.0F, 0.0F, 0.0F);
			} else {
				GlStateManager.color(0.0F, 1.0F, 0.0F);
			}
		}
		this.cube.render(scale);
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}*/

	@Override
	public void setupAnim(EntityCalamityCrystal pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		
	}

	@Override
	public void renderToBuffer(MatrixStack pMatrixStack, IVertexBuilder pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		
	}
}
