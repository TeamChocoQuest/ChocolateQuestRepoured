package team.cqr.cqrepoured.client.model.geo.entity.boss;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.model.geo.AbstractModelGeoCQRStandardBiped;
import team.cqr.cqrepoured.entity.boss.EntityCQRPirateCaptain;

public class ModelPirateCaptainGeo extends AbstractModelGeoCQRStandardBiped<EntityCQRPirateCaptain> {

	public ModelPirateCaptainGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}

	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}

}
