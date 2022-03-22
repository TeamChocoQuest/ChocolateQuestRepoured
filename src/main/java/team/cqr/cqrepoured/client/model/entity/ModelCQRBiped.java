package team.cqr.cqrepoured.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.item.ItemHookshotBase;
import team.cqr.cqrepoured.item.gun.ItemMusket;
import team.cqr.cqrepoured.item.gun.ItemRevolver;
import team.cqr.cqrepoured.item.spear.ItemSpearBase;
import team.cqr.cqrepoured.item.sword.ItemGreatSword;
import team.cqr.cqrepoured.util.PartialTicksUtil;

public class ModelCQRBiped<T extends LivingEntity> extends BipedModel<T> {

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
		this.bipedCape.setTexSize(64, 32);
		this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, 0.0F);

		this.leftArm = new ModelRenderer(this, 32, 48);
		this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.leftArm.setPos(5.0F, 2.0F, 0.0F);

		this.leftLeg = new ModelRenderer(this, 16, 48);
		this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.leftLeg.setPos(1.9F, 12.0F, 0.0F);

		this.initExtraLayer();
		this.setClothingLayerVisible(hasExtraLayer);
	}

	protected void initExtraLayer() {
		this.leftArm = new ModelRenderer(this, 32, 48);
		this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.leftArm.setPos(5.0F, 2.0F, 0.0F);

		this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
		this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F);
		this.bipedRightArmwear.setPos(-5.0F, 2.0F, 10.0F);

		this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
		this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F);
		this.bipedLeftArmwear.setPos(5.0F, 2.0F, 0.0F);

		this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
		this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
		this.bipedLeftLegwear.setPos(1.9F, 12.0F, 0.0F);

		this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
		this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
		this.bipedRightLegwear.setPos(-1.9F, 12.0F, 0.0F);

		this.bipedBodyWear = new ModelRenderer(this, 16, 32);
		this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.25F);
		this.bipedBodyWear.setPos(0.0F, 0.0F, 0.0F);
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	public void renderCape(MatrixStack matrix, IVertexBuilder vertexBuilder, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.bipedCape.render(matrix, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms and
	 * legs, where par1 represents the time(so
	 * that arms and legs swing back and forth) and par2 represents how "far" arms and legs
	 * can swing at most.
	 */
	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		final boolean isSpinToWinActive = (pEntity instanceof AbstractEntityCQR && ((AbstractEntityCQR) pEntity).isSpinToWinActive() && !this.riding);
		if (isSpinToWinActive) {
			pLimbSwing = 0;
			pLimbSwingAmount = 0;

			float f = (pEntity.tickCount - 1.0F + PartialTicksUtil.getCurrentPartialTicks()) * 16.0F;
			//GlStateManager.rotate(f, 0F, 1F, 0F);
			this.body.yRot = (float) Math.toRadians(f);
		}

		if (pEntity.isCrouching()) {
			this.bipedCape.y = 2.0F;
		} else {
			this.bipedCape.y = 0.0F;
		}
		//super.setRotationAngles(pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch, scaleFactor, pEntity);
		super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
		if (pEntity instanceof AbstractEntityCQR) {
			AbstractEntityCQR cqrEnt = ((AbstractEntityCQR) pEntity);
			if (isSpinToWinActive) {
				this.leftArm.zRot = (float) Math.toRadians(-90F);
				this.rightArm.zRot = (float) Math.toRadians(90F);
			} else if (cqrEnt.isSpellCharging() && cqrEnt.isSpellAnimated()) {
				this.renderSpellAnimation(cqrEnt, pAgeInTicks);
			} else {
				boolean flagSide = cqrEnt.isLeftHanded();
				if (cqrEnt.hasAttackTarget() && (cqrEnt.getMainHandItem().getItem() instanceof ItemRevolver || cqrEnt.getMainHandItem().getItem() instanceof ItemHookshotBase) && !(cqrEnt.getMainHandItem().getItem() instanceof ItemMusket)) {
					if (flagSide) {
						this.leftArm.xRot -= new Float(Math.toRadians(90F));
					} else {
						this.rightArm.xRot -= new Float(Math.toRadians(90F));
					}
				}
				if (cqrEnt.hasAttackTarget() && (cqrEnt.getMainHandItem().getItem() instanceof ItemRevolver || cqrEnt.getMainHandItem().getItem() instanceof ItemHookshotBase) && !(cqrEnt.getMainHandItem().getItem() instanceof ItemMusket)) {
					if (flagSide) {
						this.rightArm.xRot -= new Float(Math.toRadians(90F));
					} else {
						this.leftArm.xRot -= new Float(Math.toRadians(90F));
					}
				}
			}
		}

		ItemStack stack = ((AbstractEntityCQR) pEntity).getMainHandItem();
		if (stack.getItem() instanceof ItemSpearBase) {
			this.renderSpearHoldingAnimation();
		}
		if (true && stack.getItem() instanceof ItemGreatSword) {
			this.renderGreatSwordHoldingAnimation();
		}

		copyModelAngles(this.leftLeg, this.bipedLeftLegwear);
		copyModelAngles(this.rightLeg, this.bipedRightLegwear);
		copyModelAngles(this.leftArm, this.bipedLeftArmwear);
		copyModelAngles(this.rightArm, this.bipedRightArmwear);
		copyModelAngles(this.body, this.bipedBodyWear);
	}

	protected void copyModelAngles(ModelRenderer source, ModelRenderer target) {
		target.xRot = source.xRot;
		target.yRot = source.yRot;
		target.zRot = source.zRot;
	}

	protected void renderSpearHoldingAnimation() {
		float f = (float) Math.toRadians(40.0F);
		this.body.xRot = 0.0F;
		this.body.yRot = f;
		this.body.zRot = 0.0F;

		Vector3d v = new Vector3d(-5.0F, 0.0F, 0.0F).yRot(f);
		this.rightArm.x = (float) v.x;
		this.rightArm.z = (float) v.z;
		this.rightArm.xRot = 0.0F;
		this.rightArm.yRot = f;
		this.rightArm.zRot = 0.0F;

		Vector3d v1 = new Vector3d(5.0F, 0.0F, 0.0F).yRot(f);
		this.leftArm.x = (float) v1.x;
		this.leftArm.z = (float) v1.z;
		this.leftArm.xRot = 0.0F;
		this.leftArm.yRot = f;
		this.leftArm.zRot = 0.0F;

		float f1 = MathHelper.sin(this.attackTime * (float) Math.PI); //Correct replacement for swingProcess?
		this.rightArm.xRot += (float) Math.toRadians(-10.0F - 20.0F * f1);
		this.rightArm.yRot += (float) Math.toRadians(-45.0F);
		this.leftArm.xRot += (float) Math.toRadians(-45.0F - 25.0F * f1);
		this.leftArm.yRot += (float) Math.toRadians(30.0F - 10.0F * f1);
	}

	protected void renderGreatSwordHoldingAnimation() {
		// swingProgress = ageInTicks % 60F / 60F;
		float f3 = MathHelper.sin(this.attackTime * (float) Math.PI * 2.0F); //Correct replacement for swingProcess?
		float f = (float) Math.toRadians(20.0F + 30.0F * f3);
		this.body.xRot = 0.0F;
		this.body.yRot = f;
		this.body.zRot = 0.0F;

		if (this.attackTime > 0.0F) { //Correct replacement for swingProcess?

		}
		Vector3d v = new Vector3d(-5.0F, 0.0F, 0.0F).yRot(f);
		this.rightArm.x = (float) v.x;
		this.rightArm.z = (float) v.z;
		this.rightArm.xRot = 0.0F;
		this.rightArm.yRot = f;
		this.rightArm.zRot = 0.0F;

		Vector3d v1 = new Vector3d(5.0F, 0.0F, 0.0F).yRot(f);
		this.leftArm.x = (float) v1.x;
		this.leftArm.z = (float) v1.z;
		this.leftArm.xRot = 0.0F;
		this.leftArm.yRot = f;
		this.leftArm.zRot = 0.0F;

		float f1 = MathHelper.sin(this.attackTime * (float) Math.PI); //Correct replacement for swingProcess?
		this.rightArm.xRot += (float) Math.toRadians(-40.0F - 60.0F * f1);
		this.rightArm.yRot += (float) Math.toRadians(-40.0F);
		this.rightArm.zRot += (float) Math.toRadians(0.0F * f1);
		this.leftArm.xRot += (float) Math.toRadians(-35.0F - 60.0F * f1);
		this.leftArm.yRot += (float) Math.toRadians(50.0F);
		this.leftArm.zRot += (float) Math.toRadians(0.0F * f1);
	}

	protected void renderSpellAnimation(AbstractEntityCQR entity, float ageInTicks) {
		this.rightArm.z = 0.0F;
		this.rightArm.x = -5.0F;
		this.leftArm.z = 0.0F;
		this.leftArm.x = 5.0F;
		this.rightArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
		this.leftArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
		this.rightArm.zRot = 2.3561945F;
		this.leftArm.zRot = -2.3561945F;
		this.rightArm.yRot = 0.0F;
		this.leftArm.yRot = 0.0F;
	}

	@Override
	public void setAllVisible(boolean pVisible) {
		super.setAllVisible(pVisible);
		this.bipedCape.visible = pVisible;

		if (this.hasExtraLayers) {
			this.setClothingLayerVisible(pVisible);
		}
	}

	@Override
	public void renderToBuffer(MatrixStack pMatrixStack, IVertexBuilder pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		super.renderToBuffer(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		final float scale = 0.0F; //Default value
		pMatrixStack.pushPose();
		if(this.young) {
			pMatrixStack.scale(0.75F, 0.75F, 0.75F);
			pMatrixStack.translate(0.0F, 16.0F * scale , 0.0F);
			this.head.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			pMatrixStack.popPose();
			pMatrixStack.pushPose();
			pMatrixStack.scale(0.5F, 0.5F, 0.5F);
			pMatrixStack.translate(0.0F, 24.0F * scale, 0.0F);
			this.body.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			this.rightArm.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			this.leftArm.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			this.rightLeg.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			this.leftLeg.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			this.hat.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		} else {
			if(this.crouching) {
				pMatrixStack.translate(0.0F, 0.2F, 0.0F);
			}

			this.head.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			this.body.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			this.rightArm.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			this.leftArm.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			this.rightLeg.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			this.leftLeg.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			this.hat.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		}
		pMatrixStack.popPose();
		pMatrixStack.pushPose();
		
		if (this.hasExtraLayers) {
			this.bipedLeftLegwear.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			this.bipedRightLegwear.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			this.bipedLeftArmwear.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			this.bipedRightArmwear.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
			this.bipedBodyWear.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		}
		
		pMatrixStack.popPose();
	}
	
	/*@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		GlStateManager.pushMatrix();

		if (this.young) {
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
			this.head.render(scale);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
			this.body.render(scale);
			this.rightArm.render(scale);
			this.leftArm.render(scale);
			this.rightLeg.render(scale);
			this.leftLeg.render(scale);
			this.hat.render(scale);
		} else {
			if (entityIn.isSneaking()) {
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}

			this.head.render(scale);
			this.body.render(scale);
			this.rightArm.render(scale);
			this.leftArm.render(scale);
			this.rightLeg.render(scale);
			this.leftLeg.render(scale);
			this.hat.render(scale);
		}

		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();

		if (this.hasExtraLayers) {
			this.bipedLeftLegwear.render(scale);
			this.bipedRightLegwear.render(scale);
			this.bipedLeftArmwear.render(scale);
			this.bipedRightArmwear.render(scale);
			this.bipedBodyWear.render(scale);
		}

		GlStateManager.popMatrix();
	}*/

	protected void setClothingLayerVisible(boolean visible) {
		try {
			this.bipedLeftArmwear.visible = visible;
			this.bipedRightArmwear.visible = visible;
			this.bipedLeftLegwear.visible = visible;
			this.bipedRightLegwear.visible = visible;
			this.bipedBodyWear.visible = visible;
		} catch (NullPointerException npe) {
			// Can occur cause by default these fields are null
			// However this can be ignored
		}
	}

	public static void copyModelRotationPoint(ModelRenderer source, ModelRenderer target) {
		if (source == null || target == null) {
			return;
		}
		target.x = source.x;
		target.y = source.y;
		target.z = source.z;
	}

}
