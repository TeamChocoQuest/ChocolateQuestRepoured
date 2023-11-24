package team.cqr.cqrepoured.client.render.armor;

import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.renderer.GeoArmorRenderer;
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
