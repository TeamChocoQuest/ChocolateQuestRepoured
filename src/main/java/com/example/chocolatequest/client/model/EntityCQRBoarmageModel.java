package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.boss.EntityCQRBoarmage;
import net.minecraft.resources.ResourceLocation;

public class EntityCQRBoarmageModel extends AbstractModelHumanoidGeo<EntityCQRBoarmage> {
    
    public EntityCQRBoarmageModel() {
        super(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/boss/biped_mage_boar.geo.json"), "boarmage", ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/mage.animation.json"), 1);
    }

    @Override
    public ResourceLocation getModelResource(EntityCQRBoarmage object) {
        if (!object.getEntityData().get(com.example.chocolatequest.entity.boss.AbstractEntityCQRMageBase.IS_REVEALED)) {
            return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/boss/modelbipedhiddenmage.geo.json");
        }
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/boss/biped_mage_boar.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EntityCQRBoarmage object) {
        if (!object.getEntityData().get(com.example.chocolatequest.entity.boss.AbstractEntityCQRMageBase.IS_REVEALED)) {
            return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/entity/boss/mage_hidden.png");
        }
        int variant = Math.abs(object.getUUID().hashCode()) % 3;
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/entity/boss/pig_mage_" + variant + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(EntityCQRBoarmage animatable) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/mage.animation.json");
    }

    @Override
    public void setCustomAnimations(EntityCQRBoarmage animatable, long instanceId, software.bernie.geckolib.animation.AnimationState<EntityCQRBoarmage> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        
        software.bernie.geckolib.cache.object.GeoBone shieldBone = this.getAnimationProcessor().getBone("boneShieldBaseRotator");
        if (shieldBone != null) {
            shieldBone.setHidden(!animatable.getEntityData().get(com.example.chocolatequest.entity.boss.AbstractEntityCQRMageBase.IS_SHIELD_ACTIVE));
        }
    }
}
