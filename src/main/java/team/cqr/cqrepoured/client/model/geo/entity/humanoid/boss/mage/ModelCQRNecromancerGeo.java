package team.cqr.cqrepoured.client.model.geo.entity.boss;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.boss.EntityCQRNecromancer;

public class ModelCQRNecromancerGeo extends AbstractModelMageGeo<EntityCQRNecromancer> {

	static final ResourceLocation ANIMATION_RESLOC = new ResourceLocation(CQRMain.MODID, "animations/biped_necromancer.animation.json");

	public ModelCQRNecromancerGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, STANDARD_MODEL_HIDDEN_MAGE, STANDARD_ANIMATIONS_HIDDEN_MAGE, STANDARD_TEXTURE_HIDDEN_MAGE, entityName);
	}
	
	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRNecromancer animatable) {
		if(animatable.isIdentityHidden()) {
			return super.getAnimationFileLocation(animatable);
		}
		return ANIMATION_RESLOC;
	}

}
