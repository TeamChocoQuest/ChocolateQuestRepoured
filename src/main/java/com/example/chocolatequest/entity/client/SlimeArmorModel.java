package com.example.chocolatequest.entity.client;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.item.SlimeArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SlimeArmorModel extends GeoModel<SlimeArmorItem> {
    @Override
    public ResourceLocation getModelResource(SlimeArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/slime_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SlimeArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/models/armor/slime_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SlimeArmorItem animatable) {
        // No animation needed for standard armor
        return null; 
    }
}
