package team.cqr.cqrepoured.client.models.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * SpiderEgg - DerToaster Created using Tabula 7.0.1
 */
public class ModelSpiderEgg extends ModelBase {
	public ModelRenderer shape1;
	public ModelRenderer shape2;
	public ModelRenderer shape3;

	public ModelSpiderEgg() {
		this.textureWidth = 64;
		this.textureHeight = 48;
		this.shape1 = new ModelRenderer(this, 0, 0);
		this.shape1.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.shape1.addBox(-4.0F, -2.0F, -4.0F, 8, 2, 8, 0.0F);
		this.shape2 = new ModelRenderer(this, 0, 10);
		this.shape2.setRotationPoint(0.0F, -2.0F, 0.0F);
		this.shape2.addBox(-6.0F, -10.0F, -6.0F, 12, 10, 12, 0.0F);
		this.shape3 = new ModelRenderer(this, 0, 32);
		this.shape3.setRotationPoint(0.0F, -10.0F, 0.0F);
		this.shape3.addBox(-4.0F, -4.0F, -4.0F, 8, 4, 8, 0.0F);
		this.shape1.addChild(this.shape2);
		this.shape2.addChild(this.shape3);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.shape1.render(f5);
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
