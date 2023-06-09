package team.cqr.cqrepoured.client.model.geo.entity.humanoid;

import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.entity.AbstractModelHumanoidGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRZombie;

public class ModelCQRZombieGeo extends AbstractModelHumanoidGeo<EntityCQRZombie> {

	public ModelCQRZombieGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName, CQRAnimations.Entity.ZOMBIE);
	}

	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}
	
	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRZombie animatable) {
		return CQRAnimations.Entity.ZOMBIE;
	}

}
