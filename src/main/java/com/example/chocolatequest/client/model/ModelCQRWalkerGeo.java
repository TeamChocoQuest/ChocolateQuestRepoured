package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQWalkerEntity;
import net.minecraft.resources.ResourceLocation;

public class ModelCQRWalkerGeo extends AbstractModelHumanoidGeo<CQWalkerEntity> {
    public ModelCQRWalkerGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_base.geo.json"),
            "walker",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/walker.animation.json"),
            3
        );
    }
}
