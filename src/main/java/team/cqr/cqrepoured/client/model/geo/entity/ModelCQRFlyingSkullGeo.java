package team.cqr.cqrepoured.client.model.geo.entity;

import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.AbstractModelGeoCQRBase;
import team.cqr.cqrepoured.entity.misc.EntityFlyingSkullMinion;

public class ModelCQRFlyingSkullGeo extends AbstractModelGeoCQRBase<EntityFlyingSkullMinion> {

    public ModelCQRFlyingSkullGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationResource(EntityFlyingSkullMinion animatable) {
        return CQRAnimations.Entity.FLYING_SKULL;
    }
}
