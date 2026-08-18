package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.boss.ShelobEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ModelShelob extends GeoModel<ShelobEntity> {
    @Override
    public ResourceLocation getModelResource(ShelobEntity object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/boss/shelob.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ShelobEntity object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/entity/boss/shelob.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ShelobEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/boss/shelob.animation.json");
    }
}
