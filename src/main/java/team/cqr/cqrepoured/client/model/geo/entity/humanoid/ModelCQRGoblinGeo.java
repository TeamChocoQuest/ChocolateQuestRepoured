package team.cqr.cqrepoured.client.model.geo.entity.humanoid;

import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.entity.AbstractModelHumanoidGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRGoblin;

public class ModelCQRGoblinGeo extends AbstractModelHumanoidGeo<EntityCQRGoblin> {
	
	protected final ResourceLocation STANDARD_GOBLIN_ANIMATIONS = CQRMain.prefix("animations/biped_goblin.animation.json");

	public ModelCQRGoblinGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName, CQRAnimations.Entity.GOBLIN);
	}
	
	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}
	
	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRGoblin animatable) {
		return CQRAnimations.Entity.GOBLIN;
	}

}
