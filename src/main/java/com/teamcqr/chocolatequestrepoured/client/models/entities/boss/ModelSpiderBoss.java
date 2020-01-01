package com.teamcqr.chocolatequestrepoured.client.models.entities.boss;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * SpiderBoss - TheFunnyFace Created using Tabula 7.0.1
 */
public class ModelSpiderBoss extends ModelBase {
	public ModelRenderer BodyMiddle;
	public ModelRenderer back;
	public ModelRenderer head;
	public ModelRenderer legleft4;
	public ModelRenderer legleft1;
	public ModelRenderer legleft3;
	public ModelRenderer legleft2;
	public ModelRenderer legright1;
	public ModelRenderer legright2;
	public ModelRenderer legright3;
	public ModelRenderer legright4;
	public ModelRenderer armleft1;
	public ModelRenderer armright1;
	public ModelRenderer teethleft;
	public ModelRenderer teethright;
	public ModelRenderer armleft11;
	public ModelRenderer armright11;
	public ModelRenderer legleft41;
	public ModelRenderer legleft11;
	public ModelRenderer legleft31;
	public ModelRenderer legleft21;
	public ModelRenderer legright11;
	public ModelRenderer legright21;
	public ModelRenderer legright31;
	public ModelRenderer legright41;

	public ModelSpiderBoss() {
		this(0F);
	}

	public ModelSpiderBoss(float modelSize) {
		this(modelSize, 0F, 64, 64);
	}

	public ModelSpiderBoss(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn) {
		this.textureWidth = textureWidthIn;
		this.textureHeight = textureHeightIn;
		this.legright1 = new ModelRenderer(this, 3, 0);
		this.legright1.setRotationPoint(-4.0F, 1.0F, -1.5F);
		this.legright1.addBox(-18.0F, -1.5F, -1.5F, 19, 3, 3, modelSize);
		this.setRotateAngle(this.legright1, -0.6806784082777886F, -0.5235987755982988F, 0.17453292519943295F);
		this.legleft41 = new ModelRenderer(this, 5, 2);
		this.legleft41.setRotationPoint(18.0F, -1.5F, 0.0F);
		this.legleft41.addBox(0.0F, 0.0F, -1.5F, 19, 3, 3, modelSize);
		this.setRotateAngle(this.legleft41, 0.0F, 0.0F, 1.2217304763960306F);
		this.legleft2 = new ModelRenderer(this, 13, 4);
		this.legleft2.setRotationPoint(4.0F, 1.0F, 0.5F);
		this.legleft2.addBox(-1.0F, -1.5F, -1.5F, 19, 3, 3, modelSize);
		this.setRotateAngle(this.legleft2, -0.05235987755982988F, 0.12217304763960307F, -0.2792526803190927F);
		this.legleft21 = new ModelRenderer(this, 4, 4);
		this.legleft21.setRotationPoint(18.0F, -1.5F, 0.0F);
		this.legleft21.addBox(0.0F, 0.0F, -1.5F, 19, 3, 3, modelSize);
		this.setRotateAngle(this.legleft21, 0.0F, 0.0F, 1.2217304763960306F);
		this.armleft1 = new ModelRenderer(this, 1, 3);
		this.armleft1.setRotationPoint(5.0F, 2.0F, -7.0F);
		this.armleft1.addBox(-1.0F, -1.0F, -1.0F, 7, 2, 2, modelSize);
		this.setRotateAngle(this.armleft1, 0.9599310885968813F, 0.8726646259971648F, 0.4363323129985824F);
		this.armleft11 = new ModelRenderer(this, 18, 3);
		this.armleft11.setRotationPoint(6.0F, 0.0F, 1.0F);
		this.armleft11.addBox(0.0F, -1.0F, -2.0F, 7, 2, 2, modelSize);
		this.setRotateAngle(this.armleft11, 0.0F, 1.2566370614359172F, 0.0F);
		this.armright1 = new ModelRenderer(this, 26, 5);
		this.armright1.setRotationPoint(-5.0F, 2.0F, -7.0F);
		this.armright1.addBox(-6.0F, -1.0F, -1.0F, 7, 2, 2, modelSize);
		this.setRotateAngle(this.armright1, 0.9599310885968813F, -0.8726646259971648F, -0.4363323129985824F);
		this.legright3 = new ModelRenderer(this, 7, 4);
		this.legright3.setRotationPoint(-4.0F, 1.0F, 2.5F);
		this.legright3.addBox(-18.0F, -1.5F, -1.5F, 19, 3, 3, modelSize);
		this.setRotateAngle(this.legright3, 0.05235987755982988F, 0.10471975511965977F, 0.2792526803190927F);
		this.legleft4 = new ModelRenderer(this, 0, 6);
		this.legleft4.setRotationPoint(4.0F, 1.0F, 4.5F);
		this.legleft4.addBox(-1.0F, -1.5F, -1.5F, 19, 3, 3, modelSize);
		this.setRotateAngle(this.legleft4, 0.7155849933176751F, -0.5759586531581287F, -0.17453292519943295F);
		this.BodyMiddle = new ModelRenderer(this, 0, 38);
		this.BodyMiddle.setRotationPoint(0.0F, 14.0F, 0.0F);
		this.BodyMiddle.addBox(-4.0F, -4.0F, -7.0F, 8, 8, 18, modelSize);
		this.legleft31 = new ModelRenderer(this, 10, 1);
		this.legleft31.setRotationPoint(18.0F, -1.5F, 0.0F);
		this.legleft31.addBox(0.0F, 0.0F, -1.5F, 19, 3, 3, modelSize);
		this.setRotateAngle(this.legleft31, 0.0F, 0.0F, 1.2217304763960306F);
		this.legright4 = new ModelRenderer(this, 0, 0);
		this.legright4.setRotationPoint(-4.0F, 1.0F, 4.5F);
		this.legright4.addBox(-18.0F, -1.5F, -1.5F, 19, 3, 3, modelSize);
		this.setRotateAngle(this.legright4, 0.7155849933176751F, 0.5759586531581287F, 0.17453292519943295F);
		this.legright2 = new ModelRenderer(this, 5, 3);
		this.legright2.setRotationPoint(-4.0F, 1.0F, 0.5F);
		this.legright2.addBox(-18.0F, -1.5F, -1.5F, 19, 3, 3, modelSize);
		this.setRotateAngle(this.legright2, -0.05235987755982988F, -0.10471975511965977F, 0.2792526803190927F);
		this.back = new ModelRenderer(this, 2, 35);
		this.back.setRotationPoint(0.0F, 0.0F, 10.0F);
		this.back.addBox(-7.5F, -6.5F, 0.0F, 15, 13, 16, modelSize);
		this.teethleft = new ModelRenderer(this, 0, 25);
		this.teethleft.setRotationPoint(4.0F, 2.0F, -11.0F);
		this.teethleft.addBox(-2.0F, -1.0F, -3.0F, 2, 2, 3, modelSize);
		this.setRotateAngle(this.teethleft, 0.0F, 0.08726646259971647F, 0.0F);
		this.legright21 = new ModelRenderer(this, 17, 4);
		this.legright21.setRotationPoint(-18.0F, -1.5F, 0.0F);
		this.legright21.addBox(-19.0F, 0.0F, -1.5F, 19, 3, 3, modelSize);
		this.setRotateAngle(this.legright21, 0.0F, 0.0F, -1.2217304763960306F);
		this.head = new ModelRenderer(this, 20, 13);
		this.head.setRotationPoint(0.0F, 0.0F, -6.0F);
		this.head.addBox(-5.5F, -5.5F, -11.0F, 11, 11, 11, modelSize);
		this.teethright = new ModelRenderer(this, 0, 20);
		this.teethright.setRotationPoint(-4.0F, 2.0F, -11.0F);
		this.teethright.addBox(0.0F, -1.0F, -3.0F, 2, 2, 3, modelSize);
		this.setRotateAngle(this.teethright, 0.0F, -0.08726646259971647F, 0.0F);
		this.legright41 = new ModelRenderer(this, 6, 2);
		this.legright41.setRotationPoint(-18.0F, -1.5F, 0.0F);
		this.legright41.addBox(-19.0F, 0.0F, -1.5F, 19, 3, 3, modelSize);
		this.setRotateAngle(this.legright41, 0.0F, 0.0F, -1.2217304763960306F);
		this.legleft3 = new ModelRenderer(this, 19, 0);
		this.legleft3.setRotationPoint(4.0F, 1.0F, 2.5F);
		this.legleft3.addBox(-1.0F, -1.5F, -1.5F, 19, 3, 3, modelSize);
		this.setRotateAngle(this.legleft3, 0.05235987755982988F, -0.12217304763960307F, -0.2792526803190927F);
		this.legleft1 = new ModelRenderer(this, 15, 5);
		this.legleft1.setRotationPoint(4.0F, 1.0F, -1.5F);
		this.legleft1.addBox(-1.0F, -1.5F, -1.5F, 19, 3, 3, modelSize);
		this.setRotateAngle(this.legleft1, -0.6806784082777886F, 0.5235987755982988F, -0.17453292519943295F);
		this.legright11 = new ModelRenderer(this, 0, 3);
		this.legright11.setRotationPoint(-18.0F, -1.5F, 0.0F);
		this.legright11.addBox(-19.0F, 0.0F, -1.5F, 19, 3, 3, modelSize);
		this.setRotateAngle(this.legright11, 0.0F, 0.0F, -1.2217304763960306F);
		this.legleft11 = new ModelRenderer(this, 12, 1);
		this.legleft11.setRotationPoint(18.0F, -1.5F, 0.0F);
		this.legleft11.addBox(0.0F, 0.0F, -1.5F, 19, 3, 3, modelSize);
		this.setRotateAngle(this.legleft11, 0.0F, 0.0F, 1.2217304763960306F);
		this.legright31 = new ModelRenderer(this, 12, 2);
		this.legright31.setRotationPoint(-18.0F, -1.5F, 0.0F);
		this.legright31.addBox(-19.0F, 0.0F, -1.5F, 19, 3, 3, modelSize);
		this.setRotateAngle(this.legright31, 0.0F, 0.0F, -1.2217304763960306F);
		this.armright11 = new ModelRenderer(this, 32, 5);
		this.armright11.setRotationPoint(-6.0F, 0.0F, 1.0F);
		this.armright11.addBox(-7.0F, -1.0F, -2.0F, 7, 2, 2, modelSize);
		this.setRotateAngle(this.armright11, 0.0F, -1.2566370614359172F, 0.0F);

		this.BodyMiddle.addChild(this.legright1);
		this.legleft4.addChild(this.legleft41);
		this.BodyMiddle.addChild(this.legleft2);
		this.legleft2.addChild(this.legleft21);
		this.head.addChild(this.armleft1);
		this.armleft1.addChild(this.armleft11);
		this.head.addChild(this.armright1);
		this.BodyMiddle.addChild(this.legright3);
		this.BodyMiddle.addChild(this.legleft4);
		this.legleft3.addChild(this.legleft31);
		this.BodyMiddle.addChild(this.legright4);
		this.BodyMiddle.addChild(this.legright2);
		this.BodyMiddle.addChild(this.back);
		this.head.addChild(this.teethleft);
		this.legright2.addChild(this.legright21);
		this.BodyMiddle.addChild(this.head);
		this.head.addChild(this.teethright);
		this.legright4.addChild(this.legright41);
		this.BodyMiddle.addChild(this.legleft3);
		this.BodyMiddle.addChild(this.legleft1);
		this.legright1.addChild(this.legright11);
		this.legleft1.addChild(this.legleft11);
		this.legright3.addChild(this.legright31);
		this.armright1.addChild(this.armright11);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.BodyMiddle.render(f5);
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
