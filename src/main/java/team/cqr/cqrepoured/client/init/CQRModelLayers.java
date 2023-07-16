package team.cqr.cqrepoured.client.init;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import team.cqr.cqrepoured.CQRMain;

public class CQRModelLayers extends ModelLayers {
	
	public static final ModelLayerLocation PIRATE_PARROT = register("pirate_parrot");
			
	private static ModelLayerLocation register(String model) {
		return register(model, "main");
	}

	private static ModelLayerLocation register(String model, String layer) {
		return new ModelLayerLocation(CQRMain.prefix(model), layer);
	}

}
