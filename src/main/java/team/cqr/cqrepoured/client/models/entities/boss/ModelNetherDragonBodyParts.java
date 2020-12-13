package team.cqr.cqrepoured.client.models.entities.boss;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Nether Dragon - ArloTheEpic Created using Tabula 7.0.1
 */
public class ModelNetherDragonBodyParts {
	// Standard body part
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

	// Tail parts
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

	// Tail tip
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

	public static class ModelNetherDragonBodyPartSkeletal extends ModelBase {
		public ModelRenderer spineFront;
		public ModelRenderer spineConnectorFront;
		public ModelRenderer ribLUpper;
		public ModelRenderer RibRUpper;
		public ModelRenderer spineBack;
		public ModelRenderer spineConnectorBack;
		public ModelRenderer ribLUpperBack;
		public ModelRenderer RibRUpperBack;
		public ModelRenderer ribLMiddleBack;
		public ModelRenderer ribLLower;
		public ModelRenderer ribRMiddleBack;
		public ModelRenderer ribRLowerBack;
		public ModelRenderer ribLMiddle;
		public ModelRenderer ribLLower_1;
		public ModelRenderer ribRMiddle;
		public ModelRenderer ribRLower;

		public ModelNetherDragonBodyPartSkeletal() {
			this.textureWidth = 128;
			this.textureHeight = 64;
			this.ribRLowerBack = new ModelRenderer(this, 0, 23);
			this.ribRLowerBack.setRotationPoint(-0.1F, 5.1F, 0.0F);
			this.ribRLowerBack.addBox(-1.0F, 0.1F, -1.0F, 2, 4, 2, 0.0F);
			this.setRotateAngle(this.ribRLowerBack, 0.0F, 0.0F, -1.1780972450961724F);
			this.spineConnectorBack = new ModelRenderer(this, 16, 0);
			this.spineConnectorBack.setRotationPoint(0.0F, 0.0F, 3.0F);
			this.spineConnectorBack.addBox(-1.5F, -1.5F, -1.0F, 3, 3, 2, 0.0F);
			this.ribRMiddleBack = new ModelRenderer(this, 0, 14);
			this.ribRMiddleBack.setRotationPoint(0.0F, 4.0F, 0.0F);
			this.ribRMiddleBack.addBox(-0.4F, -0.9F, -1.0F, 2, 7, 2, 0.0F);
			this.setRotateAngle(this.ribRMiddleBack, 0.0F, 0.0F, -1.1780972450961724F);
			this.ribLUpper = new ModelRenderer(this, 0, 8);
			this.ribLUpper.setRotationPoint(1.3F, -0.5F, 0.0F);
			this.ribLUpper.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
			this.setRotateAngle(this.ribLUpper, 0.0F, 0.0F, -1.1780972450961724F);
			this.ribLMiddleBack = new ModelRenderer(this, 0, 14);
			this.ribLMiddleBack.setRotationPoint(0.0F, 4.0F, 0.0F);
			this.ribLMiddleBack.addBox(-1.6F, -0.9F, -1.0F, 2, 7, 2, 0.0F);
			this.setRotateAngle(this.ribLMiddleBack, 0.0F, 0.0F, 1.1780972450961724F);
			this.RibRUpperBack = new ModelRenderer(this, 0, 8);
			this.RibRUpperBack.setRotationPoint(-1.3F, -0.5F, 0.0F);
			this.RibRUpperBack.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
			this.setRotateAngle(this.RibRUpperBack, 0.0F, 0.0F, 1.1780972450961724F);
			this.spineFront = new ModelRenderer(this, 0, 0);
			this.spineFront.setRotationPoint(0.0F, -14.5F, -4.0F);
			this.spineFront.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
			this.ribRMiddle = new ModelRenderer(this, 0, 14);
			this.ribRMiddle.setRotationPoint(0.0F, 4.0F, 0.0F);
			this.ribRMiddle.addBox(-0.4F, -0.9F, -1.0F, 2, 7, 2, 0.0F);
			this.setRotateAngle(this.ribRMiddle, 0.0F, 0.0F, -1.1780972450961724F);
			this.spineConnectorFront = new ModelRenderer(this, 16, 0);
			this.spineConnectorFront.setRotationPoint(0.0F, 0.0F, 3.0F);
			this.spineConnectorFront.addBox(-1.5F, -1.5F, -1.0F, 3, 3, 2, 0.0F);
			this.ribLLower = new ModelRenderer(this, 0, 23);
			this.ribLLower.setRotationPoint(0.1F, 5.1F, 0.0F);
			this.ribLLower.addBox(-1.0F, 0.1F, -1.0F, 2, 4, 2, 0.0F);
			this.setRotateAngle(this.ribLLower, 0.0F, 0.0F, 1.1780972450961724F);
			this.ribLLower_1 = new ModelRenderer(this, 0, 23);
			this.ribLLower_1.setRotationPoint(0.1F, 5.1F, 0.0F);
			this.ribLLower_1.addBox(-1.0F, 0.1F, -1.0F, 2, 4, 2, 0.0F);
			this.setRotateAngle(this.ribLLower_1, 0.0F, 0.0F, 1.1780972450961724F);
			this.RibRUpper = new ModelRenderer(this, 0, 8);
			this.RibRUpper.setRotationPoint(-1.3F, -0.5F, 0.0F);
			this.RibRUpper.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
			this.setRotateAngle(this.RibRUpper, 0.0F, 0.0F, 1.1780972450961724F);
			this.ribRLower = new ModelRenderer(this, 0, 23);
			this.ribRLower.setRotationPoint(-0.1F, 5.1F, 0.0F);
			this.ribRLower.addBox(-1.0F, 0.1F, -1.0F, 2, 4, 2, 0.0F);
			this.setRotateAngle(this.ribRLower, 0.0F, 0.0F, -1.1780972450961724F);
			this.ribLUpperBack = new ModelRenderer(this, 0, 8);
			this.ribLUpperBack.setRotationPoint(1.3F, -0.5F, 0.0F);
			this.ribLUpperBack.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
			this.setRotateAngle(this.ribLUpperBack, 0.0F, 0.0F, -1.1780972450961724F);
			this.spineBack = new ModelRenderer(this, 0, 0);
			this.spineBack.setRotationPoint(0.0F, 0.0F, 3.0F);
			this.spineBack.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
			this.ribLMiddle = new ModelRenderer(this, 0, 14);
			this.ribLMiddle.setRotationPoint(0.0F, 4.0F, 0.0F);
			this.ribLMiddle.addBox(-1.6F, -0.9F, -1.0F, 2, 7, 2, 0.0F);
			this.setRotateAngle(this.ribLMiddle, 0.0F, 0.0F, 1.1780972450961724F);
			this.ribRMiddleBack.addChild(this.ribRLowerBack);
			this.spineBack.addChild(this.spineConnectorBack);
			this.RibRUpperBack.addChild(this.ribRMiddleBack);
			this.spineFront.addChild(this.ribLUpper);
			this.ribLUpperBack.addChild(this.ribLMiddleBack);
			this.spineBack.addChild(this.RibRUpperBack);
			this.RibRUpper.addChild(this.ribRMiddle);
			this.spineFront.addChild(this.spineConnectorFront);
			this.ribLMiddleBack.addChild(this.ribLLower);
			this.ribLMiddle.addChild(this.ribLLower_1);
			this.spineFront.addChild(this.RibRUpper);
			this.ribRMiddle.addChild(this.ribRLower);
			this.spineBack.addChild(this.ribLUpperBack);
			this.spineConnectorFront.addChild(this.spineBack);
			this.ribLUpper.addChild(this.ribLMiddle);
		}

		@Override
		public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
			this.spineFront.render(f5);
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
