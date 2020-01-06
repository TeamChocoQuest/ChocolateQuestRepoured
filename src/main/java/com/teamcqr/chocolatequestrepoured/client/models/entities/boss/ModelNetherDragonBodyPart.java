package com.teamcqr.chocolatequestrepoured.client.models.entities.boss;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Nether Dragon - ArloTheEpic
 * Created using Tabula 7.0.1
 */
public class ModelNetherDragonBodyPart extends ModelBase {
	public ModelRenderer Trunk1;

	public ModelNetherDragonBodyPart() {
		this.textureWidth = 128;
		this.textureHeight = 64;
		this.Trunk1 = new ModelRenderer(this, 0, 20);
		this.Trunk1.setRotationPoint(0.0F, -12.0F, 0.0F);
		this.Trunk1.addBox(-6.0F, -6.0F, -6.0F, 12, 12, 12, 0.0F);
		this.setRotateAngle(this.Trunk1, 0.0F, 1.5707963267948966F, -0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.Trunk1.render(f5);
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
