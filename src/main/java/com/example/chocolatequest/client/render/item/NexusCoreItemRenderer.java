package com.example.chocolatequest.client.render.item;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.item.NexusCoreItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class NexusCoreItemRenderer extends GeoItemRenderer<NexusCoreItem> {
    public NexusCoreItemRenderer() {
        super(new GeoModel<NexusCoreItem>() {
            @Override
            public ResourceLocation getModelResource(NexusCoreItem object) {
                return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/block/nexus_core.geo.json");
            }

            @Override
            public ResourceLocation getTextureResource(NexusCoreItem object) {
                return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/block/nexus_core.png");
            }

            @Override
            public ResourceLocation getAnimationResource(NexusCoreItem animatable) {
                return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/block/nexus_core.animation.json");
            }
        });
    }
}
