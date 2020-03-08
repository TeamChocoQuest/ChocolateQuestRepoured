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
    public ModelRenderer facetop;
    public ModelRenderer facenorth;
    public ModelRenderer facewest;
    public ModelRenderer facesouth;
    public ModelRenderer faceeast;
    public ModelRenderer facebottom;

    public ModelBubble() {
    	this.textureWidth = 32;
        this.textureHeight = 32;
        this.shape1 = new ModelRenderer(this, 0, 0);
        this.shape1.setRotationPoint(0.0F, 22.0F, -3.0F);
        this.shape1.addBox(-3.0F, 0.0F, -0.5F, 6, 1, 1, 0.0F);
        this.shape3 = new ModelRenderer(this, 0, 0);
        this.shape3.setRotationPoint(-3.0F, 22.0F, 0.0F);
        this.shape3.addBox(-3.0F, 0.0F, -0.5F, 6, 1, 1, 0.0F);
        this.setRotateAngle(shape3, 0.0F, 1.5707963267948966F, 0.0F);
        this.shape2 = new ModelRenderer(this, 0, 0);
        this.shape2.setRotationPoint(0.0F, 22.0F, 3.0F);
        this.shape2.addBox(-3.0F, 0.0F, -0.5F, 6, 1, 1, 0.0F);
        this.shape3_1 = new ModelRenderer(this, 0, 0);
        this.shape3_1.setRotationPoint(3.0F, 22.0F, 0.0F);
        this.shape3_1.addBox(-3.0F, 0.0F, -0.5F, 6, 1, 1, 0.0F);
        this.setRotateAngle(shape3_1, 0.0F, 1.5707963267948966F, 0.0F);
        this.shape9_3 = new ModelRenderer(this, 0, 0);
        this.shape9_3.setRotationPoint(3.0F, 16.0F, 0.0F);
        this.shape9_3.addBox(-3.0F, 0.0F, -0.5F, 6, 1, 1, 0.0F);
        this.setRotateAngle(shape9_3, 0.0F, 1.5707963267948966F, 0.0F);
        this.shape5_3 = new ModelRenderer(this, 0, 7);
        this.shape5_3.setRotationPoint(-3.0F, 19.5F, 3.0F);
        this.shape5_3.addBox(-0.5F, -3.0F, -0.5F, 1, 6, 1, 0.0F);
        this.shape9_2 = new ModelRenderer(this, 0, 0);
        this.shape9_2.setRotationPoint(-3.0F, 16.0F, 0.0F);
        this.shape9_2.addBox(-3.0F, 0.0F, -0.5F, 6, 1, 1, 0.0F);
        this.setRotateAngle(shape9_2, 0.0F, 1.5707963267948966F, 0.0F);
        this.shape5_2 = new ModelRenderer(this, 0, 7);
        this.shape5_2.setRotationPoint(3.0F, 19.5F, 3.0F);
        this.shape5_2.addBox(-0.5F, -3.0F, -0.5F, 1, 6, 1, 0.0F);
        this.facenorth = new ModelRenderer(this, 0, 20);
        this.facenorth.setRotationPoint(0.0F, 19.5F, 0.0F);
        this.facenorth.addBox(-2.5F, -2.5F, 3.0F, 5, 5, 1, 0.0F);
        this.facewest = new ModelRenderer(this, 0, 20);
        this.facewest.setRotationPoint(0.0F, 19.5F, 0.0F);
        this.facewest.addBox(-2.5F, -2.5F, 3.0F, 5, 5, 1, 0.0F);
        this.setRotateAngle(facewest, 0.0F, -1.5707963267948966F, 0.0F);
        this.shape5_1 = new ModelRenderer(this, 0, 7);
        this.shape5_1.setRotationPoint(3.0F, 19.5F, -3.0F);
        this.shape5_1.addBox(-0.5F, -3.0F, -0.5F, 1, 6, 1, 0.0F);
        this.facesouth = new ModelRenderer(this, 0, 20);
        this.facesouth.setRotationPoint(0.0F, 19.5F, 0.0F);
        this.facesouth.addBox(-2.5F, -2.5F, 3.0F, 5, 5, 1, 0.0F);
        this.setRotateAngle(facesouth, 0.0F, -3.148573970597771F, 0.0F);
        this.shape9_1 = new ModelRenderer(this, 0, 0);
        this.shape9_1.setRotationPoint(0.0F, 16.0F, 3.0F);
        this.shape9_1.addBox(-3.0F, 0.0F, -0.5F, 6, 1, 1, 0.0F);
        this.shape9 = new ModelRenderer(this, 0, 0);
        this.shape9.setRotationPoint(0.0F, 16.0F, -3.0F);
        this.shape9.addBox(-3.0F, 0.0F, -0.5F, 6, 1, 1, 0.0F);
        this.facetop = new ModelRenderer(this, 0, 14);
        this.facetop.setRotationPoint(0.0F, 19.5F, 0.0F);
        this.facetop.addBox(-2.5F, -4.0F, -2.5F, 5, 1, 5, 0.0F);
        this.facebottom = new ModelRenderer(this, 0, 14);
        this.facebottom.setRotationPoint(0.0F, 19.5F, 0.0F);
        this.facebottom.addBox(-2.5F, -4.0F, -2.5F, 5, 1, 5, 0.0F);
        this.setRotateAngle(facebottom, -3.141592653589793F, 0.0F, 0.0F);
        this.shape5 = new ModelRenderer(this, 0, 7);
        this.shape5.setRotationPoint(-3.0F, 19.5F, -3.0F);
        this.shape5.addBox(-0.5F, -3.0F, -0.5F, 1, 6, 1, 0.0F);
        this.faceeast = new ModelRenderer(this, 0, 20);
        this.faceeast.setRotationPoint(0.0F, 19.5F, 0.0F);
        this.faceeast.addBox(-2.5F, -2.5F, 3.0F, 5, 5, 1, 0.0F);
        this.setRotateAngle(faceeast, 0.0F, -4.71238898038469F, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
    	this.shape1.render(f5);
        this.shape3.render(f5);
        this.shape2.render(f5);
        this.shape3_1.render(f5);
        this.shape9_3.render(f5);
        this.shape5_3.render(f5);
        this.shape9_2.render(f5);
        this.shape5_2.render(f5);
        this.facenorth.render(f5);
        this.facewest.render(f5);
        this.shape5_1.render(f5);
        this.facesouth.render(f5);
        this.shape9_1.render(f5);
        this.shape9.render(f5);
        this.facetop.render(f5);
        this.facebottom.render(f5);
        this.shape5.render(f5);
        this.faceeast.render(f5);
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
