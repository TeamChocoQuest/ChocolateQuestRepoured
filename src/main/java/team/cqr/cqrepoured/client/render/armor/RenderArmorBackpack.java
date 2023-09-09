package team.cqr.cqrepoured.client.render.armor;

import software.bernie.geckolib.renderer.GeoArmorRenderer;
import team.cqr.cqrepoured.client.model.geo.armor.ModelBackpackArmorGeo;
import team.cqr.cqrepoured.item.armor.ItemBackpack;

public class RenderArmorBackpack extends GeoArmorRenderer<ItemBackpack> {

	public RenderArmorBackpack() {
		super(new ModelBackpackArmorGeo());
	}
	
}
