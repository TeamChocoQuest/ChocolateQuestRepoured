package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.block.entity.NexusCoreBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class NexusCoreModel extends GeoModel<NexusCoreBlockEntity> {
    @Override
    public ResourceLocation getModelResource(NexusCoreBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/block/nexus_core.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(NexusCoreBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/block/nexus_core.png");
    }

    @Override
    public ResourceLocation getAnimationResource(NexusCoreBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/block/nexus_core.animation.json");
    }
}
