package team.cqr.cqrepoured.client.model.armor;

import java.util.function.Function;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelCustomArmorBase<T extends LivingEntity> extends BipedModel<T> {

	public ModelCustomArmorBase(float scale, int textureWidth, int textureHeight) {
		this(RenderType::entityCutoutNoCull, scale, 0.0F, textureWidth, textureHeight);
	}
	
	public ModelCustomArmorBase(Function<ResourceLocation, RenderType> renderType, float scale, int textureWidth, int textureHeight) {
		this(renderType, scale, 0.0F, textureWidth, textureHeight);
	}
	
	public ModelCustomArmorBase(Function<ResourceLocation, RenderType> renderType, float scale, float inflate, int texWidth, int texHeight) {
		super(renderType, scale, inflate, texWidth, texHeight);
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


}
