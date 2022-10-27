package team.cqr.cqrepoured.client.model.geo.entity;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.AbstractModelGeoCQRBase;
import team.cqr.cqrepoured.entity.misc.EntityFlyingSkullMinion;

public class ModelCQRFlyingSkullGeo extends AbstractModelGeoCQRBase<EntityFlyingSkullMinion> {

    public ModelCQRFlyingSkullGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    protected final ResourceLocation STANDARD_FLYING_SKULL_ANIMATIONS = CQRMain.prefix("animations/flying_skull.animation.json");

    @Override
    public ResourceLocation getAnimationFileLocation(EntityFlyingSkullMinion animatable) {
        return STANDARD_FLYING_SKULL_ANIMATIONS;
    }
}
