package com.example.chocolatequest.entity.client;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.item.BackpackItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BackpackRenderer extends GeoArmorRenderer<BackpackItem> {
    public BackpackRenderer() {
        super(new GeoModel<BackpackItem>() {
            @Override
            public ResourceLocation getModelResource(BackpackItem object) {
                return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/armor/backpack.geo.json");
            }

            @Override
            public ResourceLocation getTextureResource(BackpackItem object) {
                return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/armor/backpack.png");
            }

            @Override
            public ResourceLocation getAnimationResource(BackpackItem animatable) {
                return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/armor/backpack.animation.json");
            }
        });
    }
}
