package com.teamcqr.chocolatequestrepoured.client.render.tileentity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * 06.01.2020
 * Author: DerToaster98
 * Github: https://github.com/DerToaster98
 */
@SideOnly(Side.CLIENT)
public class ModelNexusCrystal extends ModelBase {

	/** The cube model for the Ender Crystal. */
    private final ModelRenderer cube;
    /** The glass model for the Ender Crystal. */
    private final ModelRenderer glass = new ModelRenderer(this, "glass");

    public ModelNexusCrystal()
    {
        this.glass.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
        this.cube = new ModelRenderer(this, "cube");
        this.cube.setTextureOffset(32, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);

    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        GlStateManager.translate(0.0F, 0.3F, 0.0F);
        GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
        this.glass.rotateAngleY = this.glass.rotateAngleY + 0.015625F;
        this.glass.render(scale);
        GlStateManager.scale(0.875F, 0.875F, 0.875F);
        GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
        this.glass.rotateAngleY = this.glass.rotateAngleY + 0.015625F;
        this.glass.render(scale);
        GlStateManager.scale(0.875F, 0.875F, 0.875F);
        GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
        this.cube.rotateAngleY = this.cube.rotateAngleY + 0.015625F;
        this.cube.render(scale);
        GlStateManager.popMatrix();
    }
}
