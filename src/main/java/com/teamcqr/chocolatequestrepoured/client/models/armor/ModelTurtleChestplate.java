package com.teamcqr.chocolatequestrepoured.client.models.armor;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelTurtleChestplate - Sir Squidly
 * Created using Tabula 7.1.0
 */
public class ModelTurtleChestplate extends ModelCustomArmor {
    public ModelRenderer chest1;
    public ModelRenderer shell1;
    public ModelRenderer armR1;
    public ModelRenderer armL1;
    public ModelRenderer shell1_1;

    public ModelTurtleChestplate(float scale) {
    	super(scale, 64, 64);
    	
        this.armL1 = new ModelRenderer(this, 0, 0);
        this.armL1.setRotationPoint(-8.4F, 0.0F, -2.0F);
        this.armL1.addBox(0.0F, 0.0F, 0.0F, 4, 12, 4, 0.2F + scale);
        this.shell1_1 = new ModelRenderer(this, 32, 2);
        this.shell1_1.setRotationPoint(-4.0F, 0.0F, 5.0F);
        this.shell1_1.addBox(0.0F, 0.0F, 0.0F, 8, 12, 2, scale);
        this.chest1 = new ModelRenderer(this, 28, 32);
        this.chest1.setRotationPoint(-4.0F, 0.0F, -2.0F);
        this.chest1.addBox(0.0F, 0.0F, 0.0F, 8, 12, 4, 0.2F + scale);
        this.armR1 = new ModelRenderer(this, 16, 0);
        this.armR1.setRotationPoint(4.4F, 0.0F, -2.0F);
        this.armR1.addBox(0.0F, 0.0F, 0.0F, 4, 12, 4, 0.2F + scale);
        this.shell1 = new ModelRenderer(this, 0, 44);
        this.shell1.setRotationPoint(-6.0F, -2.0F, 1.0F);
        this.shell1.addBox(0.0F, 0.0F, 0.0F, 12, 16, 4, scale);
        
        this.bipedBody = chest1;
        this.bipedLeftArm = armL1;
        this.bipedRightArm = armR1;
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        //this.armL1.render(f5);
        this.shell1_1.render(f5);
        //this.chest1.render(f5);
        //this.armR1.render(f5);
        this.shell1.render(f5);
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