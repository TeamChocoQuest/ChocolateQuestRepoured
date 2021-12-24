package team.cqr.cqrepoured.client.model.entity.mobs.animation;

import net.minecraft.client.renderer.GlStateManager;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class SpinToWinAnimation implements IModelBipedAnimation {

	@Override
	public boolean canApply(AbstractEntityCQR entity) {
		return entity.isSpinToWinActive();
	}

	@Override
	public void apply(ModelCQRBiped model, float ageInTicks, AbstractEntityCQR entity) {
		model.bipedLeftArm.rotateAngleZ = (float) Math.toRadians(-90.0F);
		model.bipedRightArm.rotateAngleZ = (float) Math.toRadians(90.0F);

		GlStateManager.rotate(ageInTicks, 0.0F, 1.0F, 0.0F);
	}

}
