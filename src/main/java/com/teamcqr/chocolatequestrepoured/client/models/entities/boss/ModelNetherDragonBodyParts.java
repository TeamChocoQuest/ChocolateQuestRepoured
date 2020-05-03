package com.teamcqr.chocolatequestrepoured.client.models.entities.boss;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Nether Dragon - ArloTheEpic
 * Created using Tabula 7.0.1
 */
public class ModelNetherDragonBodyParts {
	//Standard body part
	public static class ModelNetherDragonBodyPart extends ModelBase {
		public ModelRenderer part;

		public ModelNetherDragonBodyPart() {
			this.textureWidth = 128;
			this.textureHeight = 64;
			this.part = new ModelRenderer(this, 0, 20);
			this.part.setRotationPoint(0.0F, -12.0F, 0.0F);
			this.part.addBox(-6.0F, -6.0F, -6.0F, 12, 12, 12, 0.0F);
			this.setRotateAngle(this.part, 0.0F, 1.5707963267948966F, -0.0F);
		}

		@Override
		public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
			this.part.render(f5);
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
	
	//Tail parts
	public static class ModelNetherDragonBodyTailStart extends ModelBase {
		public ModelRenderer part;

		public ModelNetherDragonBodyTailStart() {
			this.textureWidth = 128;
			this.textureHeight = 64;
			this.part = new ModelRenderer(this, 41, 0);
			this.part.setRotationPoint(0.0F, -12.0F, 0.0F);
			this.part.addBox(-7F, -5F, -5F, 14, 10, 10, 0F);
			this.setRotateAngle(this.part, 0.0F, 1.5707963267948966F, -0.0F);
		}

		@Override
		public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
			this.part.render(f5);
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
	
	//Tail tip
	public static class ModelNetherDragonBodyTailTip extends ModelBase {
		public ModelRenderer part;

		public ModelNetherDragonBodyTailTip() {
			this.textureWidth = 128;
			this.textureHeight = 64;
			this.part = new ModelRenderer(this, 0, 0);
			this.part.setRotationPoint(0.0F, -12.0F, 0.0F);
			this.part.addBox(-6.0F, -4.0F, -4.0F, 12, 8, 8, 0.0F);
			this.setRotateAngle(this.part, 0.0F, 1.5707963267948966F, -0.0F);
		}

		@Override
		public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
			this.part.render(f5);
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
}
