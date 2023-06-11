package team.cqr.cqrepoured.client.model.geo.entity.humanoid;

import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.entity.AbstractModelHumanoidGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRGremlin;

public class ModelCQRGremlinGeo extends AbstractModelHumanoidGeo<EntityCQRGremlin> {
	
	protected final ResourceLocation STANDARD_GREMLIN_ANIMATIONS = CQRMain.prefix("animations/biped_gremlin.animation.json");

	public ModelCQRGremlinGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName, CQRAnimations.Entity.GREMLIN);
	}

	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}
	
	@Override
	public ResourceLocation getAnimationResource(EntityCQRGremlin animatable) {
		return CQRAnimations.Entity.GREMLIN;
	}

}
