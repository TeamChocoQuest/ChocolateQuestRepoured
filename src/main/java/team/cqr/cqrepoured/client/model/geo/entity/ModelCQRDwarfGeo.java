package team.cqr.cqrepoured.client.model.geo.entity;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.model.geo.AbstractModelGeoCQRBase;
import team.cqr.cqrepoured.entity.mobs.EntityCQRDwarf;

public class ModelCQRDwarfGeo extends AbstractModelGeoCQRBase<EntityCQRDwarf> {

	public ModelCQRDwarfGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}

	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRDwarf animatable) {
		return STANDARD_BIPED_ANIMATIONS;
	}

}
