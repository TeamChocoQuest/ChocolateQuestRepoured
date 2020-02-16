package com.teamcqr.chocolatequestrepoured.client.models.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBubble extends ModelBase {

	public ModelRenderer shape1;
    public ModelRenderer shape2;
    public ModelRenderer shape3;
    public ModelRenderer shape3_1;
    public ModelRenderer shape5;
    public ModelRenderer shape5_1;
    public ModelRenderer shape5_2;
    public ModelRenderer shape5_3;
    public ModelRenderer shape9;
    public ModelRenderer shape9_1;
    public ModelRenderer shape9_2;
    public ModelRenderer shape9_3;
    public ModelRenderer shape13;
    public ModelRenderer shape13_1;
    public ModelRenderer shape13_2;
    public ModelRenderer shape13_3;
    public ModelRenderer shape13_4;
    public ModelRenderer shape13_5;

    public ModelBubble() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.shape2 = new ModelRenderer(this, 0, 0);
        this.shape2.setRotationPoint(0.0F, 22.0F, 3.0F);
        this.shape2.addBox(-3.0F, 0.0F, -0.5F, 6, 1, 1, 0.0F);
        this.shape9_2 = new ModelRenderer(this, 0, 0);
        this.shape9_2.setRotationPoint(-3.0F, 16.0F, 0.0F);
        this.shape9_2.addBox(-3.0F, 0.0F, -0.5F, 6, 1, 1, 0.0F);
        this.setRotateAngle(shape9_2, 0.0F, 1.5707963267948966F, 0.0F);
        this.shape3 = new ModelRenderer(this, 0, 0);
        this.shape3.setRotationPoint(-3.0F, 22.0F, 0.0F);
        this.shape3.addBox(-3.0F, 0.0F, -0.5F, 6, 1, 1, 0.0F);
        this.setRotateAngle(shape3, 0.0F, 1.5707963267948966F, 0.0F);
        this.shape3_1 = new ModelRenderer(this, 0, 0);
        this.shape3_1.setRotationPoint(3.0F, 22.0F, 0.0F);
        this.shape3_1.addBox(-3.0F, 0.0F, -0.5F, 6, 1, 1, 0.0F);
        this.setRotateAngle(shape3_1, 0.0F, 1.5707963267948966F, 0.0F);
        this.shape1 = new ModelRenderer(this, 0, 0);
        this.shape1.setRotationPoint(0.0F, 22.0F, -3.0F);
        this.shape1.addBox(-3.0F, 0.0F, -0.5F, 6, 1, 1, 0.0F);
        this.shape5_3 = new ModelRenderer(this, 0, 7);
        this.shape5_3.setRotationPoint(-3.0F, 19.5F, 3.0F);
        this.shape5_3.addBox(-0.5F, -3.0F, -0.5F, 1, 6, 1, 0.0F);
        this.shape9 = new ModelRenderer(this, 0, 0);
        this.shape9.setRotationPoint(0.0F, 16.0F, -3.0F);
        this.shape9.addBox(-3.0F, 0.0F, -0.5F, 6, 1, 1, 0.0F);
        this.shape13 = new ModelRenderer(this, 0, 14);
        this.shape13.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.shape13.addBox(-2.5F, -0.5F, -2.5F, 5, 1, 5, 0.0F);
        this.shape13_1 = new ModelRenderer(this, 0, 14);
        this.shape13_1.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.shape13_1.addBox(-2.5F, -0.5F, -2.5F, 5, 1, 5, 0.0F);
        this.shape5_2 = new ModelRenderer(this, 0, 7);
        this.shape5_2.setRotationPoint(3.0F, 19.5F, 3.0F);
        this.shape5_2.addBox(-0.5F, -3.0F, -0.5F, 1, 6, 1, 0.0F);
        this.shape5 = new ModelRenderer(this, 0, 7);
        this.shape5.setRotationPoint(-3.0F, 19.5F, -3.0F);
        this.shape5.addBox(-0.5F, -3.0F, -0.5F, 1, 6, 1, 0.0F);
        this.shape9_1 = new ModelRenderer(this, 0, 0);
        this.shape9_1.setRotationPoint(0.0F, 16.0F, 3.0F);
        this.shape9_1.addBox(-3.0F, 0.0F, -0.5F, 6, 1, 1, 0.0F);
        this.shape13_5 = new ModelRenderer(this, 0, 20);
        this.shape13_5.setRotationPoint(4.0F, 19.5F, 0.0F);
        this.shape13_5.addBox(-2.5F, -2.5F, -1.0F, 5, 5, 1, 0.0F);
        this.setRotateAngle(shape13_5, 0.0F, 1.5707963267948966F, 0.0F);
        this.shape13_3 = new ModelRenderer(this, 0, 20);
        this.shape13_3.setRotationPoint(0.0F, 19.5F, 4.0F);
        this.shape13_3.addBox(-2.5F, -2.5F, -1.0F, 5, 5, 1, 0.0F);
        this.shape13_2 = new ModelRenderer(this, 0, 20);
        this.shape13_2.setRotationPoint(0.0F, 19.5F, -4.0F);
        this.shape13_2.addBox(-2.5F, -2.5F, 0.0F, 5, 5, 1, 0.0F);
        this.shape9_3 = new ModelRenderer(this, 0, 0);
        this.shape9_3.setRotationPoint(3.0F, 16.0F, 0.0F);
        this.shape9_3.addBox(-3.0F, 0.0F, -0.5F, 6, 1, 1, 0.0F);
        this.setRotateAngle(shape9_3, 0.0F, 1.5707963267948966F, 0.0F);
        this.shape13_4 = new ModelRenderer(this, 0, 20);
        this.shape13_4.setRotationPoint(-4.0F, 19.5F, 0.0F);
        this.shape13_4.addBox(-2.5F, -2.5F, 0.0F, 5, 5, 1, 0.0F);
        this.setRotateAngle(shape13_4, 0.0F, 1.5707963267948966F, 0.0F);
        this.shape5_1 = new ModelRenderer(this, 0, 7);
        this.shape5_1.setRotationPoint(3.0F, 19.5F, -3.0F);
        this.shape5_1.addBox(-0.5F, -3.0F, -0.5F, 1, 6, 1, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.shape2.render(f5);
        this.shape9_2.render(f5);
        this.shape3.render(f5);
        this.shape3_1.render(f5);
        this.shape1.render(f5);
        this.shape5_3.render(f5);
        this.shape9.render(f5);
        this.shape13.render(f5);
        this.shape13_1.render(f5);
        this.shape5_2.render(f5);
        this.shape5.render(f5);
        this.shape9_1.render(f5);
        this.shape13_5.render(f5);
        this.shape13_3.render(f5);
        this.shape13_2.render(f5);
        this.shape9_3.render(f5);
        this.shape13_4.render(f5);
        this.shape5_1.render(f5);
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
