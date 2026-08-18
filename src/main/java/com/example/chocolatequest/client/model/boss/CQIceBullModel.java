package com.example.chocolatequest.client.model.boss;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.boss.CQIceBullEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CQIceBullModel extends GeoModel<CQIceBullEntity> {
    @Override
    public ResourceLocation getModelResource(CQIceBullEntity object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/boss/bull.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CQIceBullEntity object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/entity/boss/icebull.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CQIceBullEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/boss/bull_fixed.animation.json");
    }
}
