package team.cqr.cqrepoured.client.model.geo.entity.humanoid;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.AbstractModelHumanoidGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRTriton;

public class ModelCQRTritonGeo extends AbstractModelHumanoidGeo<EntityCQRTriton> {
	
	protected final ResourceLocation STANDARD_TRITON_ANIMATIONS = CQRMain.prefix("animations/biped_triton.animation.json");

	public ModelCQRTritonGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}

	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}
	
	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRTriton animatable) {
		return STANDARD_TRITON_ANIMATIONS;
	}

}
