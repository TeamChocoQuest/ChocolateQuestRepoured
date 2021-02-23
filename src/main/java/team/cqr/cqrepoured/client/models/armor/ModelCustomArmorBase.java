package team.cqr.cqrepoured.client.models.armor;

import java.util.Deque;
import java.util.LinkedList;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;

@SideOnly(Side.CLIENT)
public class ModelCustomArmorBase extends ModelBiped {

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

	private Deque<Float> rotations = new LinkedList<>();

	//TODO: Exchange type of "model" to something like "IBipedArmorPoseProvider", this one has methods to set the rotations of the armor bones => interface that has to be implemented by the model!
	//TODO: Move the "setupXOffsets" methods to an interface that needs to be implemented by the renderer
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
		this.rotations.addLast(target.offsetX);
		this.rotations.addLast(target.offsetY);
		this.rotations.addLast(target.offsetZ);
		this.rotations.addLast(target.rotateAngleX);
		this.rotations.addLast(target.rotateAngleY);
		this.rotations.addLast(target.rotateAngleZ);
		this.rotations.addLast(target.rotationPointX);
		this.rotations.addLast(target.rotationPointY);
		this.rotations.addLast(target.rotationPointZ);

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
		target.offsetX = this.rotations.removeFirst();
		target.offsetY = this.rotations.removeFirst();
		target.offsetZ = this.rotations.removeFirst();
		target.rotateAngleX = this.rotations.removeFirst();
		target.rotateAngleY = this.rotations.removeFirst();
		target.rotateAngleZ = this.rotations.removeFirst();
		target.rotationPointX = this.rotations.removeFirst();
		target.rotationPointY = this.rotations.removeFirst();
		target.rotationPointZ = this.rotations.removeFirst();
	}

}
