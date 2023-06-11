package team.cqr.cqrepoured.client.render.armor;

import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import team.cqr.cqrepoured.client.model.geo.armor.ModelKingCrownGeo;
import team.cqr.cqrepoured.item.armor.ItemArmorCrown;

public class RenderArmorKingCrown extends GeoArmorRenderer<ItemArmorCrown> {

	public RenderArmorKingCrown() {
		super(new ModelKingCrownGeo());
		
		this.headBone = "bipedHead";
	}

}
