package team.cqr.cqrepoured.client.render.entity.layer.geo;

import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import team.cqr.cqrepoured.entity.IShoulderEntityProvider;

public class LayerShoulderEntitiesGeo<T extends LivingEntity & GeoEntity & IShoulderEntityProvider> extends GeoRenderLayer<T> {

	public LayerShoulderEntitiesGeo(GeoRenderer<T> entityRendererIn) {
		super((GeoRenderer<T>) entityRendererIn);
	}
	
}
