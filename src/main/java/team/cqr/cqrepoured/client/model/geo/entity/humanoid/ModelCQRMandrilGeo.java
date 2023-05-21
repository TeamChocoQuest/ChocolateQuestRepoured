package team.cqr.cqrepoured.client.model.geo.entity;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.AbstractModelGeoCQRStandardBiped;
import team.cqr.cqrepoured.entity.mobs.EntityCQRMandril;

public class ModelCQRMandrilGeo extends AbstractModelGeoCQRStandardBiped<EntityCQRMandril> {
	
	protected final ResourceLocation STANDARD_MANDRIL_ANIMATIONS = CQRMain.prefix("animations/biped_mandril.animation.json");

	public ModelCQRMandrilGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}

	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}
	
	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRMandril animatable) {
		return STANDARD_MANDRIL_ANIMATIONS;
	}

}
