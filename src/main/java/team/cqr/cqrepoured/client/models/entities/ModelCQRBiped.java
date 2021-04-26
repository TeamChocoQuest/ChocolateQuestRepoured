package team.cqr.cqrepoured.client.models.entities;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.items.ItemHookshotBase;
import team.cqr.cqrepoured.objects.items.guns.ItemMusket;
import team.cqr.cqrepoured.objects.items.guns.ItemRevolver;
import team.cqr.cqrepoured.objects.items.spears.ItemSpearBase;
import team.cqr.cqrepoured.objects.items.swords.ItemGreatSword;

public class ModelCQRBiped extends ModelBiped {

	public ModelRenderer bipedLeftArmwear = null;
	public ModelRenderer bipedRightArmwear = null;
	public ModelRenderer bipedLeftLegwear = null;
	public ModelRenderer bipedRightLegwear = null;
	public ModelRenderer bipedBodyWear = null;
	public ModelRenderer bipedCape = null;

	public boolean hasExtraLayers = true;

	public ModelCQRBiped(int textureWidthIn, int textureHeightIn, boolean hasExtraLayer) {
		super(0.0F, 0.0F, textureWidthIn, textureHeightIn);
		this.hasExtraLayers = hasExtraLayer;

		this.bipedCape = new ModelRenderer(this, 0, 0);
		this.bipedCape.setTextureSize(64, 32);
		this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, 0.0F);

		this.bipedLeftArm = new ModelRenderer(this, 32, 48);
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

		this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);

		this.initExtraLayer();
		this.setClothingLayerVisible(hasExtraLayer);
	}

	protected void initExtraLayer() {
		this.bipedLeftArm = new ModelRenderer(this, 32, 48);
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

		this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
		this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F);
		this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);

		this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
		this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F);
		this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
		
		this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
		this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
		this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);

		this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
		this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
		this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);

		this.bipedBodyWear = new ModelRenderer(this, 16, 32);
		this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.25F);
		this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public void renderCape(float scale) {
		this.bipedCape.render(scale);
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms and legs, where par1 represents the time(so
	 * that arms and legs swing back and forth) and par2 represents how "far" arms and legs
	 * can swing at most.
	 */
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		if (entityIn.isSneaking()) {
			this.bipedCape.rotationPointY = 2.0F;
		} else {
			this.bipedCape.rotationPointY = 0.0F;
		}
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		if (entityIn instanceof AbstractEntityCQR) {
			AbstractEntityCQR cqrEnt = ((AbstractEntityCQR) entityIn);
			if (cqrEnt.isSpellCharging() && cqrEnt.isSpellAnimated()) {
				this.renderSpellAnimation(cqrEnt, ageInTicks);
			} else {
				boolean flagSide = cqrEnt.getPrimaryHand() == EnumHandSide.LEFT;
				if (cqrEnt.hasAttackTarget() && (cqrEnt.getHeldItemMainhand().getItem() instanceof ItemRevolver || cqrEnt.getHeldItemMainhand().getItem() instanceof ItemHookshotBase) && !(cqrEnt.getHeldItemMainhand().getItem() instanceof ItemMusket)) {
					if (flagSide) {
						this.bipedLeftArm.rotateAngleX -= new Float(Math.toRadians(90F));
					} else {
						this.bipedRightArm.rotateAngleX -= new Float(Math.toRadians(90F));
					}
				}
				if (cqrEnt.hasAttackTarget() && (cqrEnt.getHeldItemOffhand().getItem() instanceof ItemRevolver || cqrEnt.getHeldItemOffhand().getItem() instanceof ItemHookshotBase) && !(cqrEnt.getHeldItemOffhand().getItem() instanceof ItemMusket)) {
					if (flagSide) {
						this.bipedRightArm.rotateAngleX -= new Float(Math.toRadians(90F));
					} else {
						this.bipedLeftArm.rotateAngleX -= new Float(Math.toRadians(90F));
					}
				}
			}
		}

		ItemStack stack = ((AbstractEntityCQR) entityIn).getHeldItemMainhand();
		if (stack.getItem() instanceof ItemSpearBase) {
			float f = (float) Math.toRadians(40.0F);
			this.bipedBody.rotateAngleX = 0.0F;
			this.bipedBody.rotateAngleY = f;
			this.bipedBody.rotateAngleZ = 0.0F;

			Vec3d v = new Vec3d(-5.0F, 0.0F, 0.0F).rotateYaw(f);
			this.bipedRightArm.rotationPointX = (float) v.x;
			this.bipedRightArm.rotationPointZ = (float) v.z;
			this.bipedRightArm.rotateAngleX = 0.0F;
			this.bipedRightArm.rotateAngleY = f;
			this.bipedRightArm.rotateAngleZ = 0.0F;

			Vec3d v1 = new Vec3d(5.0F, 0.0F, 0.0F).rotateYaw(f);
			this.bipedLeftArm.rotationPointX = (float) v1.x;
			this.bipedLeftArm.rotationPointZ = (float) v1.z;
			this.bipedLeftArm.rotateAngleX = 0.0F;
			this.bipedLeftArm.rotateAngleY = f;
			this.bipedLeftArm.rotateAngleZ = 0.0F;

			float f1 = MathHelper.sin(this.swingProgress * (float) Math.PI);
			this.bipedRightArm.rotateAngleX += (float) Math.toRadians(-10.0F - 20.0F * f1);
			this.bipedRightArm.rotateAngleY += (float) Math.toRadians(-45.0F);
			this.bipedLeftArm.rotateAngleX += (float) Math.toRadians(-45.0F - 25.0F * f1);
			this.bipedLeftArm.rotateAngleY += (float) Math.toRadians(30.0F - 10.0F * f1);
		}
		if (true && stack.getItem() instanceof ItemGreatSword) {
			//swingProgress = ageInTicks % 60F / 60F;
			float f3 = MathHelper.sin(this.swingProgress * (float) Math.PI * 2.0F);
			float f = (float) Math.toRadians(20.0F + 30.0F * f3);
			this.bipedBody.rotateAngleX = 0.0F;
			this.bipedBody.rotateAngleY = f;
			this.bipedBody.rotateAngleZ = 0.0F;

			if (this.swingProgress > 0.0F) {
				
			}
			Vec3d v = new Vec3d(-5.0F, 0.0F, 0.0F).rotateYaw(f);
			this.bipedRightArm.rotationPointX = (float) v.x;
			this.bipedRightArm.rotationPointZ = (float) v.z;
			this.bipedRightArm.rotateAngleX = 0.0F;
			this.bipedRightArm.rotateAngleY = f;
			this.bipedRightArm.rotateAngleZ = 0.0F;

			Vec3d v1 = new Vec3d(5.0F, 0.0F, 0.0F).rotateYaw(f);
			this.bipedLeftArm.rotationPointX = (float) v1.x;
			this.bipedLeftArm.rotationPointZ = (float) v1.z;
			this.bipedLeftArm.rotateAngleX = 0.0F;
			this.bipedLeftArm.rotateAngleY = f;
			this.bipedLeftArm.rotateAngleZ = 0.0F;

			float f1 = MathHelper.sin(this.swingProgress * (float) Math.PI);
			this.bipedRightArm.rotateAngleX += (float) Math.toRadians(-40.0F - 60.0F * f1);
			this.bipedRightArm.rotateAngleY += (float) Math.toRadians(-40.0F);
			this.bipedRightArm.rotateAngleZ += (float) Math.toRadians(0.0F * f1);
			this.bipedLeftArm.rotateAngleX += (float) Math.toRadians(-35.0F - 60.0F * f1);
			this.bipedLeftArm.rotateAngleY += (float) Math.toRadians(50.0F);
			this.bipedLeftArm.rotateAngleZ += (float) Math.toRadians(0.0F * f1);
		}

		copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
		copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
		copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
		copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
		copyModelAngles(this.bipedBody, this.bipedBodyWear);
	}

	protected void renderSpellAnimation(AbstractEntityCQR entity, float ageInTicks) {
		this.bipedRightArm.rotationPointZ = 0.0F;
		this.bipedRightArm.rotationPointX = -5.0F;
		this.bipedLeftArm.rotationPointZ = 0.0F;
		this.bipedLeftArm.rotationPointX = 5.0F;
		this.bipedRightArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
		this.bipedLeftArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
		this.bipedRightArm.rotateAngleZ = 2.3561945F;
		this.bipedLeftArm.rotateAngleZ = -2.3561945F;
		this.bipedRightArm.rotateAngleY = 0.0F;
		this.bipedLeftArm.rotateAngleY = 0.0F;
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		this.bipedCape.showModel = visible;

		if (this.hasExtraLayers) {
			this.setClothingLayerVisible(visible);
		}
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        GlStateManager.pushMatrix();

        if (this.isChild)
        {
            float f = 2.0F;
            GlStateManager.scale(0.75F, 0.75F, 0.75F);
            GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
            this.bipedHead.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.bipedBody.render(scale);
            this.bipedRightArm.render(scale);
            this.bipedLeftArm.render(scale);
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
            this.bipedHeadwear.render(scale);
        }
        else
        {
            if (entityIn.isSneaking())
            {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }

            this.bipedHead.render(scale);
            this.bipedBody.render(scale);
            this.bipedRightArm.render(scale);
            this.bipedLeftArm.render(scale);
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
            this.bipedHeadwear.render(scale);
        }

        GlStateManager.popMatrix();
		GlStateManager.pushMatrix();

		if (this.hasExtraLayers) {
			this.bipedLeftLegwear.render(scale);
			this.bipedRightLegwear.render(scale);
			//this.bipedLeftArmwear.render(scale);
			//this.bipedRightArmwear.render(scale);
			this.bipedBodyWear.render(scale);
		}
		if (entityIn instanceof AbstractEntityCQR) {
			AbstractEntityCQR entCQR = ((AbstractEntityCQR) entityIn);
			if (entCQR.hasCape()) {
				this.bipedCape.render(scale);
			}
		}

		GlStateManager.popMatrix();
	}

	protected void setClothingLayerVisible(boolean visible) {
		this.bipedLeftArmwear.showModel = visible;
		this.bipedRightArmwear.showModel = visible;
		this.bipedLeftLegwear.showModel = visible;
		this.bipedRightLegwear.showModel = visible;
		this.bipedBodyWear.showModel = visible;
	}

	public static void copyModelRotationPoint(ModelRenderer source, ModelRenderer target) {
		target.rotationPointX = source.rotationPointX;
		target.rotationPointY = source.rotationPointY;
		target.rotationPointZ = source.rotationPointZ;
	}

}
