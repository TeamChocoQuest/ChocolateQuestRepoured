package com.teamcqr.chocolatequestrepoured.client.models.armor;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelCustomArmor - Either Mojang or a mod author
 * Created using Tabula 7.0.1
 */
public class ModelArmorHeavyIron extends ModelCustomArmorBase {
    public ModelRenderer RightArmPauldon;
    public ModelRenderer LeftArmPauldron;
    public ModelRenderer HeadHelmet;
    public ModelRenderer ChestArmor;

    public ModelArmorHeavyIron(float scale) {
    	super(scale, 128, 128);
        this.HeadHelmet = new ModelRenderer(this, 0, 64);
        this.HeadHelmet.setRotationPoint(-4.0F, -8.0F, -4.0F);
        this.HeadHelmet.addBox(-1.0F, -1.0F, -1.0F, 10, 10, 10, 0.0F);
        this.ChestArmor = new ModelRenderer(this, 0, 94);
        this.ChestArmor.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.ChestArmor.addBox(-5.0F, -1.0F, -3.0F, 10, 14, 6, 0.0F);
        this.LeftArmPauldron = new ModelRenderer(this, 0, 84);
        this.LeftArmPauldron.mirror = true;
        this.LeftArmPauldron.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.LeftArmPauldron.addBox(-1.5F, -2.5F, -2.5F, 5, 5, 5, 0.0F);
        this.RightArmPauldon = new ModelRenderer(this, 0, 84);
        this.RightArmPauldon.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.RightArmPauldon.addBox(-3.5F, -2.5F, -2.5F, 5, 5, 5, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.HeadHelmet.render(f5);
        this.ChestArmor.render(f5);
        this.LeftArmPauldron.render(f5);
        this.RightArmPauldon.render(f5);
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