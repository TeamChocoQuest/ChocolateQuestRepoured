package com.example.chocolatequest.entity.client;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.item.TurtleArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TurtleArmorModel extends GeoModel<TurtleArmorItem> {
    @Override
    public ResourceLocation getModelResource(TurtleArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/turtle_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TurtleArmorItem object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/models/armor/turtle_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TurtleArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/turtle_armor.animation.json");
    }
}

