package com.teamcqr.chocolatequestrepoured.client.models.armor;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelBackpack extends ModelBiped
{
	private ModelRenderer mainBag;
//	private ModelRenderer topBag;
	private ModelRenderer leftBag;
	private ModelRenderer rightBag;
//	private ModelRenderer backBag;
   
	public ModelBackpack()
	{
		this(1.0F);
	}
  
	public ModelBackpack(float size)
	{
		super(size);

		this.bipedBody = new ModelRenderer(this, 32, 16);
		this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 1.0F);
		this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
    
		this.mainBag = new ModelRenderer(this, 0, 0);
		this.mainBag.addBox(0.0F, 0.0F, 0.0F, 10, 12, 8, size);
		this.mainBag.setRotationPoint(-5.0F, 0.0F, 3.0F);
   
	//	this.topBag = new ModelRenderer(this, 36, 0);
	//	this.topBag.addBox(0.0F, -6.0F, 0.0F, 8, 8, 5, size);
	//	this.topBag.setRotationPoint(-4.0F, 0.0F, 4.0F);   
     
		this.leftBag = new ModelRenderer(this, 36, 0);
		this.leftBag.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 5, 0.0F);
		this.leftBag.setRotationPoint(3.0F, 4.25F, 8.0F);
		this.leftBag.rotateAngleY = ((float)Math.toRadians(90.0D));
   
		this.rightBag = new ModelRenderer(this, 36, 0);
		this.rightBag.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 5, 0.0F);
		this.rightBag.setRotationPoint(-3.0F, 4.25F, 6.0F);
		this.rightBag.rotateAngleY = ((float)Math.toRadians(-90.0D));
    
	//	this.backBag = new ModelRenderer(this, 36, 0);
	//	this.backBag.addBox(-4.0F, 0.0F, 0.0F, 8, 8, 5, 0.0F);
	//	this.backBag.setRotationPoint(0.0F, 4.25F, 9.0F);
    
		this.bipedBody.addChild(this.mainBag);
	//	this.bipedBody.addChild(this.topBag);
		this.bipedBody.addChild(this.leftBag);
		this.bipedBody.addChild(this.rightBag);
	//	this.bipedBody.addChild(this.backBag);
	}
  
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		GlStateManager.pushMatrix();

		if(this.isChild)
        {
            GlStateManager.scale(0.75F, 0.75F, 0.75F);
            GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
            this.bipedBody.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
        }
        else
        {
            if(entityIn.isSneaking())
            {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }

            this.bipedBody.render(scale);
        }

        GlStateManager.popMatrix();
	}
}