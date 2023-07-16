package team.cqr.cqrepoured.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.ParrotModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import team.cqr.cqrepoured.entity.boss.EntityCQRPirateParrot;

public class ModelCQRPirateParrot extends HierarchicalModel<EntityCQRPirateParrot> implements ArmedModel {

	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart tail;
	private final ModelPart leftWing;
	private final ModelPart rightWing;
	private final ModelPart head;
	private final ModelPart feather;
	private final ModelPart leftLeg;
	private final ModelPart rightLeg;

	public ModelCQRPirateParrot(ModelPart pRoot) {
		this.root = pRoot;
		this.body = pRoot.getChild("body");
		this.tail = pRoot.getChild("tail");
		this.leftWing = pRoot.getChild("left_wing");
		this.rightWing = pRoot.getChild("right_wing");
		this.head = pRoot.getChild("head");
		this.feather = this.head.getChild("feather");
		this.leftLeg = pRoot.getChild("left_leg");
		this.rightLeg = pRoot.getChild("right_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(2, 8).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F), PartPose.offset(0.0F, 16.5F, -3.0F));
		partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(22, 1).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 4.0F, 1.0F), PartPose.offset(0.0F, 21.07F, 1.16F));
		partdefinition.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(19, 8).addBox(-0.5F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F), PartPose.offset(1.5F, 16.94F, -2.76F));
		partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(19, 8).addBox(-0.5F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F), PartPose.offset(-1.5F, 16.94F, -2.76F));
		PartDefinition partdefinition1 = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(2, 2).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offset(0.0F, 15.69F, -2.76F));
		partdefinition1.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(10, 0).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F), PartPose.offset(0.0F, -2.0F, -1.0F));
		partdefinition1.addOrReplaceChild("beak1", CubeListBuilder.create().texOffs(11, 7).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offset(0.0F, -0.5F, -1.5F));
		partdefinition1.addOrReplaceChild("beak2", CubeListBuilder.create().texOffs(16, 7).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offset(0.0F, -1.75F, -2.45F));
		partdefinition1.addOrReplaceChild("feather", CubeListBuilder.create().texOffs(2, 18).addBox(0.0F, -4.0F, -2.0F, 0.0F, 5.0F, 4.0F), PartPose.offset(0.0F, -2.15F, 0.15F));
		CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(14, 18).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F);
		partdefinition.addOrReplaceChild("left_leg", cubelistbuilder, PartPose.offset(1.0F, 22.0F, -1.05F));
		partdefinition.addOrReplaceChild("right_leg", cubelistbuilder, PartPose.offset(-1.0F, 22.0F, -1.05F));
		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	public ModelPart root() {
		return this.root;
	}

	/**
	 * Sets this entity's model rotation angles
	 */
	public void setupAnim(EntityCQRPirateParrot pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		this.setupAnim(ModelCQRPirateParrot.getState(pEntity), pEntity.tickCount, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
	}

	public void prepareMobModel(EntityCQRPirateParrot pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick) {
		this.prepare(ModelCQRPirateParrot.getState(pEntity));
	}

	public void renderOnShoulder(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pLimbSwing, float pLimbSwingAmount, float pNetHeadYaw, float pHeadPitch, int pTickCount) {
		this.prepare(ParrotModel.State.ON_SHOULDER);
		this.setupAnim(ParrotModel.State.ON_SHOULDER, pTickCount, pLimbSwing, pLimbSwingAmount, 0.0F, pNetHeadYaw, pHeadPitch);
		this.root.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
	}

	private void setupAnim(ParrotModel.State pState, int pTickCount, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		this.head.xRot = pHeadPitch * ((float) Math.PI / 180F);
		this.head.yRot = pNetHeadYaw * ((float) Math.PI / 180F);
		this.head.zRot = 0.0F;
		this.head.x = 0.0F;
		this.body.x = 0.0F;
		this.tail.x = 0.0F;
		this.rightWing.x = -1.5F;
		this.leftWing.x = 1.5F;
		switch (pState) {
		case SITTING:
			break;
		case PARTY:
			float f = Mth.cos((float) pTickCount);
			float f1 = Mth.sin((float) pTickCount);
			this.head.x = f;
			this.head.y = 15.69F + f1;
			this.head.xRot = 0.0F;
			this.head.yRot = 0.0F;
			this.head.zRot = Mth.sin((float) pTickCount) * 0.4F;
			this.body.x = f;
			this.body.y = 16.5F + f1;
			this.leftWing.zRot = -0.0873F - pAgeInTicks;
			this.leftWing.x = 1.5F + f;
			this.leftWing.y = 16.94F + f1;
			this.rightWing.zRot = 0.0873F + pAgeInTicks;
			this.rightWing.x = -1.5F + f;
			this.rightWing.y = 16.94F + f1;
			this.tail.x = f;
			this.tail.y = 21.07F + f1;
			break;
		case STANDING:
			this.leftLeg.xRot += Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
			this.rightLeg.xRot += Mth.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 1.4F * pLimbSwingAmount;
		case FLYING:
		case ON_SHOULDER:
		default:
			float f2 = pAgeInTicks * 0.3F;
			this.head.y = 15.69F + f2;
			this.tail.xRot = 1.015F + Mth.cos(pLimbSwing * 0.6662F) * 0.3F * pLimbSwingAmount;
			this.tail.y = 21.07F + f2;
			this.body.y = 16.5F + f2;
			this.leftWing.zRot = -0.0873F - pAgeInTicks;
			this.leftWing.y = 16.94F + f2;
			this.rightWing.zRot = 0.0873F + pAgeInTicks;
			this.rightWing.y = 16.94F + f2;
			this.leftLeg.y = 22.0F + f2;
			this.rightLeg.y = 22.0F + f2;
		}

	}

	private void prepare(ParrotModel.State pState) {
		this.feather.xRot = -0.2214F;
		this.body.xRot = 0.4937F;
		this.leftWing.xRot = -0.6981F;
		this.leftWing.yRot = -(float) Math.PI;
		this.rightWing.xRot = -0.6981F;
		this.rightWing.yRot = -(float) Math.PI;
		this.leftLeg.xRot = -0.0299F;
		this.rightLeg.xRot = -0.0299F;
		this.leftLeg.y = 22.0F;
		this.rightLeg.y = 22.0F;
		this.leftLeg.zRot = 0.0F;
		this.rightLeg.zRot = 0.0F;
		switch (pState) {
		case SITTING:
			this.head.y = 17.59F;
			this.tail.xRot = 1.5388988F;
			this.tail.y = 22.97F;
			this.body.y = 18.4F;
			this.leftWing.zRot = -0.0873F;
			this.leftWing.y = 18.84F;
			this.rightWing.zRot = 0.0873F;
			this.rightWing.y = 18.84F;
			++this.leftLeg.y;
			++this.rightLeg.y;
			++this.leftLeg.xRot;
			++this.rightLeg.xRot;
			break;
		case PARTY:
			this.leftLeg.zRot = -0.34906584F;
			this.rightLeg.zRot = 0.34906584F;
		case STANDING:
		case ON_SHOULDER:
		default:
			break;
		case FLYING:
			this.leftLeg.xRot += 0.6981317F;
			this.rightLeg.xRot += 0.6981317F;
		}

	}

	private static ParrotModel.State getState(EntityCQRPirateParrot pParrot) {
		if (pParrot.isPartyParrot()) {
			return ParrotModel.State.PARTY;
		} else if (pParrot.isInSittingPose()) {
			return ParrotModel.State.SITTING;
		} else {
			return pParrot.isFlying() ? ParrotModel.State.FLYING : ParrotModel.State.STANDING;
		}
	}

	@Override
	public void translateToHand(HumanoidArm pSide, PoseStack pMatrixStack) {
		ModelPart armRenderer = this.leftLeg;
		if (!armRenderer.cubes.isEmpty()) {
			ModelPart.Cube armBox = armRenderer.cubes.get(0);
			float x = 0.125F - 0.03125F * (armBox.maxX - armBox.minX);
			float z = 0.125F;
			float sizeY = Math.abs(Math.max(armBox.minY, armBox.maxY) - Math.min(armBox.minY, armBox.maxY));
			float y = 0.0625F * (armBox.maxY - armBox.maxY + (sizeY * 4)); // 6F is the height of the body

			pMatrixStack.translate(x, y, z + 0.295);
			pMatrixStack.mulPose(Axis.XP.rotationDegrees(90.0F));
		}
	}

}
