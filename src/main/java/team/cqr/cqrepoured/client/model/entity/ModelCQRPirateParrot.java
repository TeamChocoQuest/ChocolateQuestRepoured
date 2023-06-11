package team.cqr.cqrepoured.client.model.entity;

import org.joml.Vector3f;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.model.ParrotModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.ModelRenderer.ModelBox;
import net.minecraft.util.HandSide;
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;
import team.cqr.cqrepoured.entity.boss.EntityCQRPirateParrot;

public class ModelCQRPirateParrot extends SegmentedModel<EntityCQRPirateParrot> implements IHasArm {

	private final ModelRenderer body;
	private final ModelRenderer tail;
	private final ModelRenderer wingLeft;
	private final ModelRenderer wingRight;
	private final ModelRenderer head;
	private final ModelRenderer head2;
	private final ModelRenderer beak1;
	private final ModelRenderer beak2;
	private final ModelRenderer feather;
	private final ModelRenderer legLeft;
	private final ModelRenderer legRight;

	public ModelCQRPirateParrot() {
	      this.texWidth = 32;
	      this.texHeight = 32;
	      this.body = new ModelRenderer(this, 2, 8);
	      this.body.addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F);
	      this.body.setPos(0.0F, 16.5F, -3.0F);
	      this.tail = new ModelRenderer(this, 22, 1);
	      this.tail.addBox(-1.5F, -1.0F, -1.0F, 3.0F, 4.0F, 1.0F);
	      this.tail.setPos(0.0F, 21.07F, 1.16F);
	      this.wingLeft = new ModelRenderer(this, 19, 8);
	      this.wingLeft.addBox(-0.5F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F);
	      this.wingLeft.setPos(1.5F, 16.94F, -2.76F);
	      this.wingRight = new ModelRenderer(this, 19, 8);
	      this.wingRight.addBox(-0.5F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F);
	      this.wingRight.setPos(-1.5F, 16.94F, -2.76F);
	      this.head = new ModelRenderer(this, 2, 2);
	      this.head.addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F);
	      this.head.setPos(0.0F, 15.69F, -2.76F);
	      this.head2 = new ModelRenderer(this, 10, 0);
	      this.head2.addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F);
	      this.head2.setPos(0.0F, -2.0F, -1.0F);
	      this.head.addChild(this.head2);
	      this.beak1 = new ModelRenderer(this, 11, 7);
	      this.beak1.addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F);
	      this.beak1.setPos(0.0F, -0.5F, -1.5F);
	      this.head.addChild(this.beak1);
	      this.beak2 = new ModelRenderer(this, 16, 7);
	      this.beak2.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F);
	      this.beak2.setPos(0.0F, -1.75F, -2.45F);
	      this.head.addChild(this.beak2);
	      this.feather = new ModelRenderer(this, 2, 18);
	      this.feather.addBox(0.0F, -4.0F, -2.0F, 0.0F, 5.0F, 4.0F);
	      this.feather.setPos(0.0F, -2.15F, 0.15F);
	      this.head.addChild(this.feather);
	      this.legLeft = new ModelRenderer(this, 14, 18);
	      this.legLeft.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F);
	      this.legLeft.setPos(1.0F, 22.0F, -1.05F);
	      this.legRight = new ModelRenderer(this, 14, 18);
	      this.legRight.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F);
	      this.legRight.setPos(-1.0F, 22.0F, -1.05F);
	   }

	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.body, this.wingLeft, this.wingRight, this.tail, this.head, this.legLeft, this.legRight);
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

	public void renderOnShoulder(MatrixStack pMatrixStack, IVertexBuilder pBuffer, int pPackedLight, int pPackedOverlay, float p_228284_5_, float p_228284_6_, float p_228284_7_, float p_228284_8_, int p_228284_9_) {
		this.prepare(ParrotModel.State.ON_SHOULDER);
		this.setupAnim(ParrotModel.State.ON_SHOULDER, p_228284_9_, p_228284_5_, p_228284_6_, 0.0F, p_228284_7_, p_228284_8_);
		this.parts().forEach((p_228285_4_) -> {
			p_228285_4_.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
		});
	}

	private void setupAnim(ParrotModel.State p_217162_1_, int p_217162_2_, float p_217162_3_, float p_217162_4_, float p_217162_5_, float p_217162_6_, float p_217162_7_) {
		this.head.xRot = p_217162_7_ * ((float) Math.PI / 180F);
		this.head.yRot = p_217162_6_ * ((float) Math.PI / 180F);
		this.head.zRot = 0.0F;
		this.head.x = 0.0F;
		this.body.x = 0.0F;
		this.tail.x = 0.0F;
		this.wingRight.x = -1.5F;
		this.wingLeft.x = 1.5F;
		switch (p_217162_1_) {
		case SITTING:
			break;
		case PARTY:
			float f = MathHelper.cos((float) p_217162_2_);
			float f1 = MathHelper.sin((float) p_217162_2_);
			this.head.x = f;
			this.head.y = 15.69F + f1;
			this.head.xRot = 0.0F;
			this.head.yRot = 0.0F;
			this.head.zRot = MathHelper.sin((float) p_217162_2_) * 0.4F;
			this.body.x = f;
			this.body.y = 16.5F + f1;
			this.wingLeft.zRot = -0.0873F - p_217162_5_;
			this.wingLeft.x = 1.5F + f;
			this.wingLeft.y = 16.94F + f1;
			this.wingRight.zRot = 0.0873F + p_217162_5_;
			this.wingRight.x = -1.5F + f;
			this.wingRight.y = 16.94F + f1;
			this.tail.x = f;
			this.tail.y = 21.07F + f1;
			break;
		case STANDING:
			this.legLeft.xRot += MathHelper.cos(p_217162_3_ * 0.6662F) * 1.4F * p_217162_4_;
			this.legRight.xRot += MathHelper.cos(p_217162_3_ * 0.6662F + (float) Math.PI) * 1.4F * p_217162_4_;
		case FLYING:
		case ON_SHOULDER:
		default:
			float f2 = p_217162_5_ * 0.3F;
			this.head.y = 15.69F + f2;
			this.tail.xRot = 1.015F + MathHelper.cos(p_217162_3_ * 0.6662F) * 0.3F * p_217162_4_;
			this.tail.y = 21.07F + f2;
			this.body.y = 16.5F + f2;
			this.wingLeft.zRot = -0.0873F - p_217162_5_;
			this.wingLeft.y = 16.94F + f2;
			this.wingRight.zRot = 0.0873F + p_217162_5_;
			this.wingRight.y = 16.94F + f2;
			this.legLeft.y = 22.0F + f2;
			this.legRight.y = 22.0F + f2;
		}

	}

	private void prepare(ParrotModel.State p_217160_1_) {
		this.feather.xRot = -0.2214F;
		this.body.xRot = 0.4937F;
		this.wingLeft.xRot = -0.6981F;
		this.wingLeft.yRot = -(float) Math.PI;
		this.wingRight.xRot = -0.6981F;
		this.wingRight.yRot = -(float) Math.PI;
		this.legLeft.xRot = -0.0299F;
		this.legRight.xRot = -0.0299F;
		this.legLeft.y = 22.0F;
		this.legRight.y = 22.0F;
		this.legLeft.zRot = 0.0F;
		this.legRight.zRot = 0.0F;
		switch (p_217160_1_) {
		case SITTING:
			float f = 1.9F;
			this.head.y = 17.59F;
			this.tail.xRot = 1.5388988F;
			this.tail.y = 22.97F;
			this.body.y = 18.4F;
			this.wingLeft.zRot = -0.0873F;
			this.wingLeft.y = 18.84F;
			this.wingRight.zRot = 0.0873F;
			this.wingRight.y = 18.84F;
			++this.legLeft.y;
			++this.legRight.y;
			++this.legLeft.xRot;
			++this.legRight.xRot;
			break;
		case PARTY:
			this.legLeft.zRot = -0.34906584F;
			this.legRight.zRot = 0.34906584F;
		case STANDING:
		case ON_SHOULDER:
		default:
			break;
		case FLYING:
			this.legLeft.xRot += 0.6981317F;
			this.legRight.xRot += 0.6981317F;
		}

	}

	private static ParrotModel.State getState(EntityCQRPirateParrot p_217158_0_) {
		if (p_217158_0_.isPartyParrot()) {
			return ParrotModel.State.PARTY;
		} else if (p_217158_0_.isInSittingPose()) {
			return ParrotModel.State.SITTING;
		} else {
			return p_217158_0_.isFlying() ? ParrotModel.State.FLYING : ParrotModel.State.STANDING;
		}
	}

	@Override
	public void translateToHand(HandSide pSide, MatrixStack pMatrixStack) {
		ModelRenderer armRenderer = this.legLeft;
		if (!armRenderer.cubes.isEmpty()) {
			ModelBox armBox = armRenderer.cubes.get(0);
			float x = 0.125F - 0.03125F * (armBox.maxX - armBox.minX);
			float z = 0.125F;
			float sizeY = Math.abs(Math.max(armBox.minY, armBox.maxY) - Math.min(armBox.minY, armBox.maxY));
			float y = 0.0625F * (armBox.maxY - armBox.maxY + (sizeY * 4)); // 6F is the height of the body

			pMatrixStack.translate(x, y, z + 0.295);
			pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
		}
	}

}
