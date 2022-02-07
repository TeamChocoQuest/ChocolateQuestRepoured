package team.cqr.cqrepoured.client.model.armor;

import com.mojang.blaze3d.platform.GlStateManager;

import it.unimi.dsi.fastutil.floats.FloatArrayFIFOQueue;
import it.unimi.dsi.fastutil.floats.FloatPriorityQueue;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;

@OnlyIn(Dist.CLIENT)
public class ModelCustomArmorBase<T extends LivingEntity> extends BipedModel<T> {

	private final FloatPriorityQueue rotations = new FloatArrayFIFOQueue();

	public ModelCustomArmorBase(float scale, int textureWidth, int textureHeight) {
		super(scale, 0.0F, textureWidth, textureHeight);
	}

	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);

		if (pEntity instanceof ArmorStandEntity) {
			ArmorStandEntity entityarmorstand = (ArmorStandEntity) pEntity;
			this.head.xRot = 0.017453292F * entityarmorstand.getHeadPose().getX();
			this.head.yRot = 0.017453292F * entityarmorstand.getHeadPose().getY();
			this.head.zRot = 0.017453292F * entityarmorstand.getHeadPose().getZ();
			this.head.setPos(0.0F, 1.0F, 0.0F);
			this.body.xRot = 0.017453292F * entityarmorstand.getBodyPose().getX();
			this.body.yRot = 0.017453292F * entityarmorstand.getBodyPose().getY();
			this.body.zRot = 0.017453292F * entityarmorstand.getBodyPose().getZ();
			this.leftArm.xRot = 0.017453292F * entityarmorstand.getLeftArmPose().getX();
			this.leftArm.yRot = 0.017453292F * entityarmorstand.getLeftArmPose().getY();
			this.leftArm.zRot = 0.017453292F * entityarmorstand.getLeftArmPose().getZ();
			this.rightArm.xRot = 0.017453292F * entityarmorstand.getRightArmPose().getX();
			this.rightArm.yRot = 0.017453292F * entityarmorstand.getRightArmPose().getY();
			this.rightArm.zRot = 0.017453292F * entityarmorstand.getRightArmPose().getZ();
			this.leftLeg.xRot = 0.017453292F * entityarmorstand.getLeftLegPose().getX();
			this.leftLeg.yRot = 0.017453292F * entityarmorstand.getLeftLegPose().getY();
			this.leftLeg.zRot = 0.017453292F * entityarmorstand.getLeftLegPose().getZ();
			this.leftLeg.setPos(1.9F, 11.0F, 0.0F);
			this.rightLeg.xRot = 0.017453292F * entityarmorstand.getRightLegPose().getX();
			this.rightLeg.yRot = 0.017453292F * entityarmorstand.getRightLegPose().getY();
			this.rightLeg.zRot = 0.017453292F * entityarmorstand.getRightLegPose().getZ();
			this.rightLeg.setPos(-1.9F, 11.0F, 0.0F);

			this.rightArm.copyFrom(this.head);
		}
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
	
	// TODO: Exchange type of "model" to something like "IBipedArmorPoseProvider", this one has methods to set the rotations
	// of the armor bones => interface that
	// has to be implemented by the model!
	// TODO: Move the "setupXOffsets" methods to an interface that needs to be implemented by the renderer
	public void render(T entityIn, float scale, RenderCQREntity<?> renderer, ModelBiped model, EntityEquipmentSlot slot) {
		this.applyRotations(model);

		GlStateManager.pushMatrix();

		if (this.young) {
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);

			GlStateManager.pushMatrix();
			renderer.setupHeadOffsets(this.head, slot);
			this.head.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);

			GlStateManager.pushMatrix();
			renderer.setupBodyOffsets(this.body, slot);
			this.body.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupRightArmOffsets(this.rightArm, slot);
			this.rightArm.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupLeftArmOffsets(this.leftArm, slot);
			this.leftArm.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupRightLegOffsets(this.rightLeg, slot);
			this.rightLeg.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupLeftLegOffsets(this.leftLeg, slot);
			this.leftLeg.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupHeadwearOffsets(this.hat, slot);
			this.hat.render(scale);
			GlStateManager.popMatrix();
		} else {
			if (entityIn.isCrouching()) {
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}

			GlStateManager.pushMatrix();
			renderer.setupHeadOffsets(this.head, slot);
			this.head.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupBodyOffsets(this.body, slot);
			this.body.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupRightArmOffsets(this.rightArm, slot);
			this.rightArm.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupLeftArmOffsets(this.leftArm, slot);
			this.leftArm.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupRightLegOffsets(this.rightLeg, slot);
			this.rightLeg.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupLeftLegOffsets(this.leftLeg, slot);
			this.leftLeg.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupHeadwearOffsets(this.hat, slot);
			this.hat.render(scale);
			GlStateManager.popMatrix();
		}

		GlStateManager.popMatrix();

		this.resetRotations();
	}

	public void applyRotations(BipedModel<?> model) {
		this.applyRotations(this.hat, model.hat);
		this.applyRotations(this.head, model.head);
		this.applyRotations(this.body, model.body);
		this.applyRotations(this.leftArm, model.leftArm);
		this.applyRotations(this.rightArm, model.rightArm);
		this.applyRotations(this.leftLeg, model.leftLeg);
		this.applyRotations(this.rightLeg, model.rightLeg);
	}

	public void applyRotations(ModelRenderer target, ModelRenderer source) {
		this.rotations.enqueue(target.offsetX);
		this.rotations.enqueue(target.offsetY);
		this.rotations.enqueue(target.offsetZ);
		this.rotations.enqueue(target.xRot);
		this.rotations.enqueue(target.yRot);
		this.rotations.enqueue(target.zRot);
		this.rotations.enqueue(target.rotationPointX);
		this.rotations.enqueue(target.rotationPointY);
		this.rotations.enqueue(target.rotationPointZ);

		target.offsetX = source.offsetX;
		target.offsetY = source.offsetY;
		target.offsetZ = source.offsetZ;
		target.xRot = source.xRot;
		target.yRot = source.yRot;
		target.rotateAngleZ = source.rotateAngleZ;
		target.rotationPointX = source.rotationPointX;
		target.rotationPointY = source.rotationPointY;
		target.rotationPointZ = source.rotationPointZ;
	}

	public void resetRotations() {
		this.resetRotations(this.hat);
		this.resetRotations(this.head);
		this.resetRotations(this.body);
		this.resetRotations(this.leftArm);
		this.resetRotations(this.rightArm);
		this.resetRotations(this.leftLeg);
		this.resetRotations(this.rightLeg);
	}

	public void resetRotations(ModelRenderer target) {
		target.offsetX = this.rotations.dequeue();
		target.offsetY = this.rotations.dequeue();
		target.offsetZ = this.rotations.dequeue();
		target.rotateAngleX = this.rotations.dequeue();
		target.rotateAngleY = this.rotations.dequeue();
		target.rotateAngleZ = this.rotations.dequeue();
		target.rotationPointX = this.rotations.dequeue();
		target.rotationPointY = this.rotations.dequeue();
		target.rotationPointZ = this.rotations.dequeue();
	}

}
