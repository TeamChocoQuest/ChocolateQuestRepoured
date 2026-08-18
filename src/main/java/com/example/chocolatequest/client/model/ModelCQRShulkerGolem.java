package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.boss.EntityCQRShulkerGolem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ModelCQRShulkerGolem extends GeoModel<EntityCQRShulkerGolem> {
    @Override
    public ResourceLocation getModelResource(EntityCQRShulkerGolem object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/shulker_golem.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EntityCQRShulkerGolem object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/entity/boss/shulker_golem.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EntityCQRShulkerGolem object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/shulker_golem.animation.json");
    }
}
