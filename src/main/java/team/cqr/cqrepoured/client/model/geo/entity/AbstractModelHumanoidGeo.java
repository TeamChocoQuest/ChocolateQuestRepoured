package team.cqr.cqrepoured.client.model.geo.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.AbstractModelGeoCQRBase;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public abstract class AbstractModelHumanoidGeo<T extends AbstractEntityCQR & IAnimatableCQR> extends AbstractModelGeoCQRBase<T> {
	
	protected static final String STANDARD_HEAD_IDENT = "bipedHead";
	
	public AbstractModelHumanoidGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName, final ResourceLocation... animationFile) {
		super(model, textureDefault, entityName, merge(CQRAnimations.Entity._GENERIC_HUMANOID, animationFile));
	}
	
	protected abstract String getHeadBoneIdent();

	protected float getNetHeadYaw(final float partialTicks, T entity) {
		float f = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
		float f1 = Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);
		float netHeadYaw = f1 - f;
		boolean shouldSit = entity.isPassenger() && (entity.getVehicle() != null && entity.getVehicle().shouldRiderSit());
		if (shouldSit && entity.getVehicle() instanceof LivingEntity) {
			LivingEntity livingentity = (LivingEntity) entity.getVehicle();
			f = Mth.rotLerp(partialTicks, livingentity.yBodyRotO, livingentity.yBodyRot);
			netHeadYaw = f1 - f;
			float f3 = Mth.wrapDegrees(netHeadYaw);
			if (f3 < -85.0F) {
				f3 = -85.0F;
			}

			if (f3 >= 85.0F) {
				f3 = 85.0F;
			}

			f = f1 - f3;
			if (f3 * f3 > 2500.0F) {
				f += f3 * 0.2F;
			}

			netHeadYaw = f1 - f;
		}

		return netHeadYaw;
	}

	protected float rotlerpRad(float pAngle, float pMaxAngle, float pMul) {
		float f = (pMul - pMaxAngle) % ((float) Math.PI * 2F);
		if (f < -(float) Math.PI) {
			f += ((float) Math.PI * 2F);
		}

		if (f >= (float) Math.PI) {
			f -= ((float) Math.PI * 2F);
		}

		return pMaxAngle + pAngle * f;
	}

	protected void rotateHead(T entity, final CoreGeoBone headBone, final float partialTick, final float netHeadYaw, final float headPitch) {
		boolean flag = entity.getFallFlyingTicks() > 4;
		boolean flag1 = entity.isVisuallySwimming();
		float swimAmount = entity.getSwimAmount(partialTick);
		headBone.setRotX(netHeadYaw * ((float) Math.PI / 180F));
		if (flag) {
			headBone.setRotX(-(float) Math.PI / 4F);
		} else if (swimAmount > 0.0F) {
			if (flag1) {
				headBone.setRotX(this.rotlerpRad(swimAmount, headBone.getRotX(), (-(float) Math.PI / 4F)));
			} else {
				headBone.setRotX(this.rotlerpRad(swimAmount, headBone.getRotX(), headPitch * ((float) Math.PI / 180F)));
			}
		} else {
			headBone.setRotX(headPitch * ((float) Math.PI / 180F));
		}
	}

	@Override
	public ResourceLocation getAnimationResource(T animatable) {
		return CQRAnimations.Entity._GENERIC_HUMANOID;
	}
	
}
