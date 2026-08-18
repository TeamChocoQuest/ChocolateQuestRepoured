package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.boss.EntityCQRLich;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class EntityCQRLichModel extends AbstractModelHumanoidGeo<EntityCQRLich> {
    
    public EntityCQRLichModel() {
        super(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/boss/modelbipedlich.geo.json"), "lich", ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/mage.animation.json"), 1);
    }

    @Override
    public ResourceLocation getModelResource(EntityCQRLich object) {
        if (!object.getEntityData().get(com.example.chocolatequest.entity.boss.AbstractEntityCQRMageBase.IS_REVEALED)) {
            return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/boss/modelbipedhiddenmage.geo.json");
        }
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/boss/modelbipedlich.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EntityCQRLich object) {
        if (!object.getEntityData().get(com.example.chocolatequest.entity.boss.AbstractEntityCQRMageBase.IS_REVEALED)) {
            return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/entity/boss/mage_hidden.png");
        }
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/entity/boss/lich.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EntityCQRLich animatable) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/mage.animation.json");
    }

    @Override
    public void setCustomAnimations(EntityCQRLich animatable, long instanceId, software.bernie.geckolib.animation.AnimationState<EntityCQRLich> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        
        software.bernie.geckolib.cache.object.GeoBone shieldBone = this.getAnimationProcessor().getBone("boneShieldBaseRotator");
        if (shieldBone != null) {
            shieldBone.setHidden(!animatable.getEntityData().get(com.example.chocolatequest.entity.boss.AbstractEntityCQRMageBase.IS_SHIELD_ACTIVE));
        }
    }
}
