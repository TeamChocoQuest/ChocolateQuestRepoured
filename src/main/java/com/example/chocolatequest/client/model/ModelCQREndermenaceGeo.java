package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.boss.EntityCQREndermenace;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ModelCQREndermenaceGeo extends GeoModel<EntityCQREndermenace> {
    @Override
    public ResourceLocation getModelResource(EntityCQREndermenace object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/endermenace.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EntityCQREndermenace object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/entity/boss/endermenace.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EntityCQREndermenace animatable) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/endermenace.animation.json");
    }
}
