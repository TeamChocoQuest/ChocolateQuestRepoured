package team.cqr.cqrepoured.client.model.armor;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.LivingEntity;

public class ModelArmorTransparent<T extends LivingEntity> extends ModelCustomArmorBase<T> {

	public ModelArmorTransparent(float scale) {
		super(RenderType::entityTranslucent, scale, 64, 32);
	}

}
