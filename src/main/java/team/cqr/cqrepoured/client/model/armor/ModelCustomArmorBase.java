package team.cqr.cqrepoured.client.model.armor;

import it.unimi.dsi.fastutil.floats.FloatArrayFIFOQueue;
import it.unimi.dsi.fastutil.floats.FloatPriorityQueue;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;

@Dist(OnlyIn.CLIENT)
public class ModelCustomArmorBase extends ModelBiped {

	private final FloatPriorityQueue rotations = new FloatArrayFIFOQueue();

	public ModelCustomArmorBase(float scale, int textureWidth, int textureHeight) {
		super(scale, 0.0F, textureWidth, textureHeight);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		if (entityIn instanceof EntityArmorStand) {
			EntityArmorStand entityarmorstand = (EntityArmorStand) entityIn;
			this.bipedHead.rotateAngleX = 0.017453292F * entityarmorstand.getHeadRotation().getX();
			this.bipedHead.rotateAngleY = 0.017453292F * entityarmorstand.getHeadRotation().getY();
			this.bipedHead.rotateAngleZ = 0.017453292F * entityarmorstand.getHeadRotation().getZ();
			this.bipedHead.setRotationPoint(0.0F, 1.0F, 0.0F);
			this.bipedBody.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
			this.bipedBody.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
			this.bipedBody.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
			this.bipedLeftArm.rotateAngleX = 0.017453292F * entityarmorstand.getLeftArmRotation().getX();
			this.bipedLeftArm.rotateAngleY = 0.017453292F * entityarmorstand.getLeftArmRotation().getY();
			this.bipedLeftArm.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftArmRotation().getZ();
			this.bipedRightArm.rotateAngleX = 0.017453292F * entityarmorstand.getRightArmRotation().getX();
			this.bipedRightArm.rotateAngleY = 0.017453292F * entityarmorstand.getRightArmRotation().getY();
			this.bipedRightArm.rotateAngleZ = 0.017453292F * entityarmorstand.getRightArmRotation().getZ();
			this.bipedLeftLeg.rotateAngleX = 0.017453292F * entityarmorstand.getLeftLegRotation().getX();
			this.bipedLeftLeg.rotateAngleY = 0.017453292F * entityarmorstand.getLeftLegRotation().getY();
			this.bipedLeftLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftLegRotation().getZ();
			this.bipedLeftLeg.setRotationPoint(1.9F, 11.0F, 0.0F);
			this.bipedRightLeg.rotateAngleX = 0.017453292F * entityarmorstand.getRightLegRotation().getX();
			this.bipedRightLeg.rotateAngleY = 0.017453292F * entityarmorstand.getRightLegRotation().getY();
			this.bipedRightLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getRightLegRotation().getZ();
			this.bipedRightLeg.setRotationPoint(-1.9F, 11.0F, 0.0F);
			copyModelAngles(this.bipedHead, this.bipedHeadwear);
		}
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	// TODO: Exchange type of "model" to something like "IBipedArmorPoseProvider", this one has methods to set the rotations
	// of the armor bones => interface that
	// has to be implemented by the model!
	// TODO: Move the "setupXOffsets" methods to an interface that needs to be implemented by the renderer
	public void render(Entity entityIn, float scale, RenderCQREntity<?> renderer, ModelBiped model, EntityEquipmentSlot slot) {
		this.applyRotations(model);

		GlStateManager.pushMatrix();

		if (this.isChild) {
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);

			GlStateManager.pushMatrix();
			renderer.setupHeadOffsets(this.bipedHead, slot);
			this.bipedHead.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);

			GlStateManager.pushMatrix();
			renderer.setupBodyOffsets(this.bipedBody, slot);
			this.bipedBody.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupRightArmOffsets(this.bipedRightArm, slot);
			this.bipedRightArm.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupLeftArmOffsets(this.bipedLeftArm, slot);
			this.bipedLeftArm.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupRightLegOffsets(this.bipedRightLeg, slot);
			this.bipedRightLeg.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupLeftLegOffsets(this.bipedLeftLeg, slot);
			this.bipedLeftLeg.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupHeadwearOffsets(this.bipedHeadwear, slot);
			this.bipedHeadwear.render(scale);
			GlStateManager.popMatrix();
		} else {
			if (entityIn.isSneaking()) {
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}

			GlStateManager.pushMatrix();
			renderer.setupHeadOffsets(this.bipedHead, slot);
			this.bipedHead.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupBodyOffsets(this.bipedBody, slot);
			this.bipedBody.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupRightArmOffsets(this.bipedRightArm, slot);
			this.bipedRightArm.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupLeftArmOffsets(this.bipedLeftArm, slot);
			this.bipedLeftArm.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupRightLegOffsets(this.bipedRightLeg, slot);
			this.bipedRightLeg.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupLeftLegOffsets(this.bipedLeftLeg, slot);
			this.bipedLeftLeg.render(scale);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			renderer.setupHeadwearOffsets(this.bipedHeadwear, slot);
			this.bipedHeadwear.render(scale);
			GlStateManager.popMatrix();
		}

		GlStateManager.popMatrix();

		this.resetRotations();
	}

	public void applyRotations(ModelBiped model) {
		this.applyRotations(this.bipedHeadwear, model.bipedHeadwear);
		this.applyRotations(this.bipedHead, model.bipedHead);
		this.applyRotations(this.bipedBody, model.bipedBody);
		this.applyRotations(this.bipedLeftArm, model.bipedLeftArm);
		this.applyRotations(this.bipedRightArm, model.bipedRightArm);
		this.applyRotations(this.bipedLeftLeg, model.bipedLeftLeg);
		this.applyRotations(this.bipedRightLeg, model.bipedRightLeg);
	}

	public void applyRotations(ModelRenderer target, ModelRenderer source) {
		this.rotations.enqueue(target.offsetX);
		this.rotations.enqueue(target.offsetY);
		this.rotations.enqueue(target.offsetZ);
		this.rotations.enqueue(target.rotateAngleX);
		this.rotations.enqueue(target.rotateAngleY);
		this.rotations.enqueue(target.rotateAngleZ);
		this.rotations.enqueue(target.rotationPointX);
		this.rotations.enqueue(target.rotationPointY);
		this.rotations.enqueue(target.rotationPointZ);

		target.offsetX = source.offsetX;
		target.offsetY = source.offsetY;
		target.offsetZ = source.offsetZ;
		target.rotateAngleX = source.rotateAngleX;
		target.rotateAngleY = source.rotateAngleY;
		target.rotateAngleZ = source.rotateAngleZ;
		target.rotationPointX = source.rotationPointX;
		target.rotationPointY = source.rotationPointY;
		target.rotationPointZ = source.rotationPointZ;
	}

	public void resetRotations() {
		this.resetRotations(this.bipedHeadwear);
		this.resetRotations(this.bipedHead);
		this.resetRotations(this.bipedBody);
		this.resetRotations(this.bipedLeftArm);
		this.resetRotations(this.bipedRightArm);
		this.resetRotations(this.bipedLeftLeg);
		this.resetRotations(this.bipedRightLeg);
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
