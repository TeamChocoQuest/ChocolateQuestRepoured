package team.cqr.cqrepoured.client.model.geo.entity.boss;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.model.geo.AbstractModelGeoCQRStandardBiped;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntityCQRSpectreLord;

public class ModelSpectrelordGeo extends AbstractModelGeoCQRStandardBiped<EntityCQRSpectreLord> {

	public ModelSpectrelordGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}

	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}

}
