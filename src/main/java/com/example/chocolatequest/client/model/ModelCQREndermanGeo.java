package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQEndermanEntity;
import net.minecraft.resources.ResourceLocation;

public class ModelCQREndermanGeo extends AbstractModelHumanoidGeo<CQEndermanEntity> {
    public ModelCQREndermanGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_enderman.geo.json"),
            "enderman",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/enderman.animation.json"),
            1 // 1 texture variant
        );
    }
}
