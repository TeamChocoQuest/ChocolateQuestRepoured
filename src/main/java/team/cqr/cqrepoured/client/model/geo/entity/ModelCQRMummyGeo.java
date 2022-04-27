package team.cqr.cqrepoured.client.model.geo.entity;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.model.geo.AbstractModelGeoCQRStandardBiped;
import team.cqr.cqrepoured.entity.mobs.EntityCQRMummy;

public class ModelCQRMummyGeo extends AbstractModelGeoCQRStandardBiped<EntityCQRMummy> {

	public ModelCQRMummyGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}
	
	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}

}
