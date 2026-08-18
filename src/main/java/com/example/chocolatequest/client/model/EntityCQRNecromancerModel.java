package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.boss.EntityCQRNecromancer;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class EntityCQRNecromancerModel extends AbstractModelHumanoidGeo<EntityCQRNecromancer> {
    
    public EntityCQRNecromancerModel() {
        super(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/boss/modelbipednecromancer.geo.json"), "necromancer", ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/boss/necromancer.animation.json"), 1);
    }

    @Override
    public ResourceLocation getModelResource(EntityCQRNecromancer object) {
        if (!object.getEntityData().get(com.example.chocolatequest.entity.boss.AbstractEntityCQRMageBase.IS_REVEALED)) {
            return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/boss/modelbipedhiddenmage.geo.json");
        }
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/boss/modelbipednecromancer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EntityCQRNecromancer object) {
        if (!object.getEntityData().get(com.example.chocolatequest.entity.boss.AbstractEntityCQRMageBase.IS_REVEALED)) {
            return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/entity/boss/mage_hidden.png");
        }
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/entity/boss/necromancer.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EntityCQRNecromancer animatable) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/boss/necromancer.animation.json");
    }

    @Override
    public void setCustomAnimations(EntityCQRNecromancer animatable, long instanceId, software.bernie.geckolib.animation.AnimationState<EntityCQRNecromancer> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        
        software.bernie.geckolib.cache.object.GeoBone shieldBone = this.getAnimationProcessor().getBone("boneShieldBaseRotator");
        if (shieldBone != null) {
            shieldBone.setHidden(!animatable.getEntityData().get(com.example.chocolatequest.entity.boss.AbstractEntityCQRMageBase.IS_SHIELD_ACTIVE));
        }
    }
}
