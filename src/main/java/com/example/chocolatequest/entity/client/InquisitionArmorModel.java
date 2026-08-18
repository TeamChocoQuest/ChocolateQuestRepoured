package com.example.chocolatequest.entity.client;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.item.InquisitionArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class InquisitionArmorModel extends GeoModel<InquisitionArmorItem> {
    @Override
    public ResourceLocation getModelResource(InquisitionArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/inquisition_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(InquisitionArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/models/armor/inquisition_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(InquisitionArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/inquisition_armor.animation.json");
    }
}
