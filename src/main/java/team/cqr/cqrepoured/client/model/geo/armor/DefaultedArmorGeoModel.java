package team.cqr.cqrepoured.client.model.geo.armor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.model.DefaultedGeoModel;

public class DefaultedArmorGeoModel<T extends ArmorItem & GeoItem> extends DefaultedGeoModel<T> {

	public DefaultedArmorGeoModel(ResourceLocation assetSubpath) {
		super(assetSubpath);
	}

	@Override
	protected String subtype() {
		return "armor";
	}

}
