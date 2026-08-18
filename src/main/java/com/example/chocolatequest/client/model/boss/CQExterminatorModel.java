package com.example.chocolatequest.client.model.boss;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.boss.exterminator.EntityCQRExterminator;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CQExterminatorModel extends GeoModel<EntityCQRExterminator> {
    @Override
    public ResourceLocation getModelResource(EntityCQRExterminator object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/boss/exterminator.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EntityCQRExterminator object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/entity/boss/exterminator.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EntityCQRExterminator animatable) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/boss/exterminator.animation.json");
    }
}
