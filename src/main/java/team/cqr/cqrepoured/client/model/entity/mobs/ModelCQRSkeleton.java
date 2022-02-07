package team.cqr.cqrepoured.client.model.entity.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;
import team.cqr.cqrepoured.entity.mobs.EntityCQRSkeleton;

/** Copied from vanilla skeleton model but modified */
public class ModelCQRSkeleton extends ModelCQRBiped<EntityCQRSkeleton> {

	public ModelCQRSkeleton() {
		super(64, 64, false);

		this.rightArm = new ModelRenderer(this, 40, 16);
		this.rightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, 0.0F);
		this.rightArm.setPos(-5.0F, 2.0F, 0.0F);
		this.leftArm = new ModelRenderer(this, 40, 16);
		this.leftArm.mirror = true;
		this.leftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, 0.0F);
		this.leftArm.setPos(5.0F, 2.0F, 0.0F);
		this.rightLeg = new ModelRenderer(this, 0, 16);
		this.rightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, 0.0F);
		this.rightLeg.setPos(-2.0F, 12.0F, 0.0F);
		this.leftLeg = new ModelRenderer(this, 0, 16);
		this.leftLeg.mirror = true;
		this.leftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, 0.0F);
		this.leftLeg.setPos(2.0F, 12.0F, 0.0F);

		this.setClothingLayerVisible(false);
	}

	@Override
	public void translateToHand(HandSide pSide, MatrixStack pMatrixStack) {
		float f = pSide == HandSide.RIGHT ? 1.0F : -1.0F;
		ModelRenderer modelrenderer = this.getArm(pSide);
		modelrenderer.x += f;
		modelrenderer.translateAndRotate(pMatrixStack);
		modelrenderer.x -= f;
	}

}
