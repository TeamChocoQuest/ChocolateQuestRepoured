package com.example.chocolatequest.client.render.item;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.item.ForceFieldNexusItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ForceFieldNexusItemRenderer extends GeoItemRenderer<ForceFieldNexusItem> {
    public ForceFieldNexusItemRenderer() {
        super(new GeoModel<ForceFieldNexusItem>() {
            @Override
            public ResourceLocation getModelResource(ForceFieldNexusItem object) {
                return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/block/force_field_nexus.geo.json");
            }

            @Override
            public ResourceLocation getTextureResource(ForceFieldNexusItem object) {
                return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/block/force_field_nexus.png");
            }

            @Override
            public ResourceLocation getAnimationResource(ForceFieldNexusItem animatable) {
                return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/block/force_field_nexus.animation.json");
            }
        });
        // addRenderLayer(new software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer<>(this));
    }
}
