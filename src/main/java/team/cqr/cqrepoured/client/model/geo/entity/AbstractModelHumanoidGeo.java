package team.cqr.cqrepoured.client.model.geo.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.AbstractModelGeoCQRBase;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public abstract class AbstractModelHumanoidGeo<T extends AbstractEntityCQR & IAnimatableCQR> extends AbstractModelGeoCQRBase<T> {
	
	protected static final String STANDARD_HEAD_IDENT = "bipedHead";
	
	public AbstractModelHumanoidGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}

	protected abstract String getHeadBoneIdent();

	protected float getNetHeadYaw(final float partialTicks, T entity) {
		float f = MathHelper.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
		float f1 = MathHelper.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);
		float netHeadYaw = f1 - f;
		boolean shouldSit = entity.isPassenger() && (entity.getVehicle() != null && entity.getVehicle().shouldRiderSit());
		if (shouldSit && entity.getVehicle() instanceof LivingEntity) {
			LivingEntity livingentity = (LivingEntity) entity.getVehicle();
			f = MathHelper.rotLerp(partialTicks, livingentity.yBodyRotO, livingentity.yBodyRot);
			netHeadYaw = f1 - f;
			float f3 = MathHelper.wrapDegrees(netHeadYaw);
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

	protected float getHeadPitch(final float partialTicks, T entity) {
		return MathHelper.lerp(partialTicks, entity.xRotO, entity.xRot);
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

	protected void rotateHead(T entity, final IBone headBone, final float partialTick, final float netHeadYaw, final float headPitch) {
		boolean flag = entity.getFallFlyingTicks() > 4;
		boolean flag1 = entity.isVisuallySwimming();
		float swimAmount = entity.getSwimAmount(partialTick);
		headBone.setRotationY(netHeadYaw * ((float) Math.PI / 180F));
		if (flag) {
			headBone.setRotationX(-(float) Math.PI / 4F);
		} else if (swimAmount > 0.0F) {
			if (flag1) {
				headBone.setRotationX(this.rotlerpRad(swimAmount, headBone.getRotationX(), (-(float) Math.PI / 4F)));
			} else {
				headBone.setRotationX(this.rotlerpRad(swimAmount, headBone.getRotationX(), headPitch * ((float) Math.PI / 180F)));
			}
		} else {
			headBone.setRotationX(headPitch * ((float) Math.PI / 180F));
		}
	}

	@Override
	public void setLivingAnimations(T entity, Integer uniqueID, AnimationEvent customPredicate) {
		IBone head = this.getAnimationProcessor().getBone(this.getHeadBoneIdent());
		if (head != null) {
			//this.rotateHead(entity, head, customPredicate.getPartialTick(), this.getNetHeadYaw(customPredicate.getPartialTick(), entity), this.getHeadPitch(customPredicate.getPartialTick(), entity));
		}

		super.setLivingAnimations(entity, uniqueID, customPredicate);
	}

	@Override
	public ResourceLocation getAnimationFileLocation(T animatable) {
		return CQRAnimations.Entity._GENERIC_HUMANOID;
	}
	
}
