package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQGremlinEntity;
import net.minecraft.resources.ResourceLocation;

public class ModelCQRGremlinGeo extends AbstractModelHumanoidGeo<CQGremlinEntity> {
    public ModelCQRGremlinGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_gremlin.geo.json"),
            "gremlin",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/gremlin.animation.json"),
            1 // 1 texture variant
        );
    }
}
