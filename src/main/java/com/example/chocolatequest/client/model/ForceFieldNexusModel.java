package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.block.entity.ForceFieldNexusBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ForceFieldNexusModel extends GeoModel<ForceFieldNexusBlockEntity> {
    @Override
    public ResourceLocation getModelResource(ForceFieldNexusBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/block/force_field_nexus.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ForceFieldNexusBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/block/force_field_nexus.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ForceFieldNexusBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/block/force_field_nexus.animation.json");
    }
}
