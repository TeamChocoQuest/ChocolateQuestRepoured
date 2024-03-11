package team.cqr.cqrepoured.client.render.armor;

import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import team.cqr.cqrepoured.client.model.geo.armor.ModelKingCrownGeo;
import team.cqr.cqrepoured.item.armor.ItemArmorCrown;

public class RenderArmorKingCrown extends GeoArmorRenderer<ItemArmorCrown> {

	public RenderArmorKingCrown() {
		super(new ModelKingCrownGeo());
	}

	@Override
	public GeoBone getHeadBone() {
		return this.model.getBone("bipedHead").orElse(null);
	}
	
}
