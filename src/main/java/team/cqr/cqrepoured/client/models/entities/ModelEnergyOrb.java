package team.cqr.cqrepoured.client.models.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

// Code largely copied from vanilla ender crystal
public class ModelEnergyOrb extends ModelBase {
	/** The cube model for the Ender Crystal. */
	private final ModelRenderer cube;

	public ModelEnergyOrb(float p_i1170_1_) {
		this.cube = new ModelRenderer(this, "cube");
		this.cube.setTextureOffset(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16);
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GlStateManager.translate(0.0F, 0.5F, 0.0F);

		GlStateManager.scale(0.875F, 0.875F, 0.875F);
		GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
		GlStateManager.rotate(limbSwingAmount, 0.0F, 1.0F, 0.0F);
		float color = 0.5F + (float) (0.25F * (1+Math.sin(entity.ticksExisted / Math.PI)));
		GlStateManager.color(color, color, 0.0F);
		this.cube.render(scale);
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}
}
