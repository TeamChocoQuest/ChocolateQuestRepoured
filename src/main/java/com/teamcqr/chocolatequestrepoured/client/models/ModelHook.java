package com.teamcqr.chocolatequestrepoured.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelHook - DerToaster98
 * Created using Tabula 7.0.1
 */
public class ModelHook extends ModelBase {
	public ModelRenderer stem;
	public ModelRenderer clawLeft;
	public ModelRenderer clawRight;
	public ModelRenderer clawUp;
	public ModelRenderer clawDown;

	public ModelHook() {
		this.textureWidth = 16;
		this.textureHeight = 16;
		this.clawRight = new ModelRenderer(this, 0, 6);
		this.clawRight.setRotationPoint(-0.5F, 0.0F, 0.0F);
		this.clawRight.addBox(-3.0F, -0.5F, 0.0F, 3, 1, 1, 0.0F);
		this.setRotateAngle(this.clawRight, 0.0F, 0.7853981633974483F, 0.0F);
		this.stem = new ModelRenderer(this, 0, 8);
		this.stem.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.stem.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 6, 0.0F);
		this.clawLeft = new ModelRenderer(this, 0, 4);
		this.clawLeft.setRotationPoint(0.5F, 0.0F, 0.0F);
		this.clawLeft.addBox(0.0F, -0.5F, 0.0F, 3, 1, 1, 0.0F);
		this.setRotateAngle(this.clawLeft, 0.0F, -0.7853981633974483F, 0.0F);
		this.clawUp = new ModelRenderer(this, 0, 0);
		this.clawUp.setRotationPoint(0.0F, -0.5F, 0.0F);
		this.clawUp.addBox(-0.5F, -3.0F, 0.0F, 1, 3, 1, 0.0F);
		this.setRotateAngle(this.clawUp, -0.7853981633974483F, 0.0F, 0.0F);
		this.clawDown = new ModelRenderer(this, 4, 0);
		this.clawDown.setRotationPoint(0.0F, 0.5F, 0.0F);
		this.clawDown.addBox(-0.5F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
		this.setRotateAngle(this.clawDown, 0.7853981633974483F, 0.0F, 0.0F);
		this.stem.addChild(this.clawRight);
		this.stem.addChild(this.clawLeft);
		this.stem.addChild(this.clawUp);
		this.stem.addChild(this.clawDown);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.stem.render(f5);
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
