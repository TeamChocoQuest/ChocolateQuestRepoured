package team.cqr.cqrepoured.client.model.entity.mobs.animation;

import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class SpellAnimation implements IModelBipedAnimation {

	@Override
	public boolean canApply(AbstractEntityCQR entity) {
		return entity.isSpellCharging() && entity.isSpellAnimated();
	}

	@Override
	public void apply(ModelCQRBiped model, float ageInTicks, AbstractEntityCQR entity) {
		model.bipedRightArm.rotationPointZ = 0.0F;
		model.bipedRightArm.rotationPointX = -5.0F;
		model.bipedLeftArm.rotationPointZ = 0.0F;
		model.bipedLeftArm.rotationPointX = 5.0F;
		model.bipedRightArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
		model.bipedLeftArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
		model.bipedRightArm.rotateAngleZ = 2.3561945F;
		model.bipedLeftArm.rotateAngleZ = -2.3561945F;
		model.bipedRightArm.rotateAngleY = 0.0F;
		model.bipedLeftArm.rotateAngleY = 0.0F;
	}

}
