package team.cqr.cqrepoured.client.model.geo.entity;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.AbstractModelGeoCQRStandardBiped;
import team.cqr.cqrepoured.entity.mobs.EntityCQRGoblin;

public class ModelCQRGoblinGeo extends AbstractModelGeoCQRStandardBiped<EntityCQRGoblin> {
	
	protected final ResourceLocation STANDARD_GOBLIN_ANIMATIONS = CQRMain.prefix("animations/biped_goblin.animation.json");

	public ModelCQRGoblinGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}
	
	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}
	
	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRGoblin animatable) {
		return STANDARD_GOBLIN_ANIMATIONS;
	}

}
