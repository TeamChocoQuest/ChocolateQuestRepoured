package team.cqr.cqrepoured.client.model.entity.mobs.animation;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.item.gun.ItemRevolver;

public class FirearmAnimation implements IModelBipedAnimation {

	@Override
	public boolean canApply(AbstractEntityCQR entity) {
		if (!entity.hasAttackTarget()) {
			return false;
		}

		ItemStack stackMain = entity.getHeldItemMainhand();
		Item itemMain = stackMain.getItem();
		if (itemMain instanceof ItemRevolver) {
			return true;
		}

		ItemStack stackOff = entity.getHeldItemMainhand();
		Item itemOff = stackOff.getItem();
		return itemOff instanceof ItemRevolver;
	}

	@Override
	public void apply(ModelCQRBiped model, float ageInTicks, AbstractEntityCQR entity) {
		boolean animateRightArm = false;
		boolean animateLeftArm = false;

		ItemStack stackMain = entity.getHeldItemMainhand();
		Item itemMain = stackMain.getItem();
		if (itemMain instanceof ItemRevolver) {
			if (entity.isLeftHanded()) {
				animateLeftArm = true;
			} else {
				animateRightArm = true;
			}
		}

		ItemStack stackOff = entity.getHeldItemMainhand();
		Item itemOff = stackOff.getItem();
		if (itemOff instanceof ItemRevolver) {
			if (entity.isLeftHanded()) {
				animateRightArm = true;
			} else {
				animateLeftArm = true;
			}
		}

		if (animateRightArm) {
			model.bipedRightArm.rotateAngleX -= Math.toRadians(90.0F);
		}
		if (animateLeftArm) {
			model.bipedLeftArm.rotateAngleX -= Math.toRadians(90.0F);
		}
	}

}
