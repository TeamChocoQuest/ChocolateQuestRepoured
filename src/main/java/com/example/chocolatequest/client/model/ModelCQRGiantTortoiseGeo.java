package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.boss.EntityCQRGiantTortoise;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ModelCQRGiantTortoiseGeo extends GeoModel<EntityCQRGiantTortoise> {
    @Override
    public ResourceLocation getModelResource(EntityCQRGiantTortoise object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/giant_tortoise.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EntityCQRGiantTortoise object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/entity/boss/giant_tortoise.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EntityCQRGiantTortoise animatable) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/giant_tortoise.animation.json");
    }
}
