package team.cqr.cqrepoured.client.render.armor;

import mod.azure.azurelib.renderer.GeoArmorRenderer;
import team.cqr.cqrepoured.client.model.geo.armor.ModelBullArmorGeo;
import team.cqr.cqrepoured.item.armor.ItemArmorBull;

public class RenderArmorBull extends GeoArmorRenderer<ItemArmorBull> {

	public RenderArmorBull() {
		super(new ModelBullArmorGeo());
	}
	
}
