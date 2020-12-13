package team.cqr.cqrepoured.client.models.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelIceSpike extends ModelBase {

	public ModelRenderer baseplate;
	public ModelRenderer base;
	public ModelRenderer middle;
	public ModelRenderer top;

	public ModelIceSpike() {
		this.textureWidth = 16;
		this.textureHeight = 16;
		this.base = new ModelRenderer(this, 0, 0);
		this.base.setRotationPoint(0.0F, -2.0F, 0.0F);
		this.base.addBox(-4.0F, -6.0F, -4.0F, 8, 6, 8, 0.0F);
		this.middle = new ModelRenderer(this, 0, 0);
		this.middle.setRotationPoint(0.0F, -6.0F, 0.0F);
		this.middle.addBox(-2.5F, -6.0F, -2.5F, 5, 6, 5, 0.0F);
		this.top = new ModelRenderer(this, 0, 0);
		this.top.setRotationPoint(0.0F, -6.0F, 0.0F);
		this.top.addBox(-1.0F, -6.0F, -1.0F, 2, 6, 2, 0.0F);
		this.baseplate = new ModelRenderer(this, 0, 0);
		this.baseplate.setRotationPoint(0.0F, 2.0F, 0.0F);
		this.baseplate.addBox(-6.0F, -4.0F, -6.0F, 12, 4, 12, 0.0F);
		this.baseplate.addChild(this.base);
		this.base.addChild(this.middle);
		this.middle.addChild(this.top);
	}

	@Override
	public void render(Entity entity, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
		float animationProgress = p_78088_2_;
		if (animationProgress > 1.0F) {
			animationProgress = 1.0F;
		}

		/* animationProgress = 1.0F - animationProgress * animationProgress * animationProgress; */
		/*
		 * Y when everything is in floor = 16 Y when everything is exposed = -2
		 */
		this.base.rotationPointY = 16 - 4 * (animationProgress * 4.5F);
		this.baseplate.render(scale);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}
