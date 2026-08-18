package com.example.chocolatequest.client.model.boss;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.boss.CQBullEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CQBullModel extends GeoModel<CQBullEntity> {
    @Override
    public ResourceLocation getModelResource(CQBullEntity object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/boss/bull.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CQBullEntity object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/entity/boss/bull.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CQBullEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/boss/bull_fixed.animation.json");
    }
}
