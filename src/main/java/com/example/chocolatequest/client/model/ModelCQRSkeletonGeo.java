package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQSkeletonEntity;
import net.minecraft.resources.ResourceLocation;

public class ModelCQRSkeletonGeo extends AbstractModelHumanoidGeo<CQSkeletonEntity> {
    public ModelCQRSkeletonGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_skeleton.geo.json"),
            "skeleton",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/skeleton.animation.json"),
            1 // 1 texture variant
        );
    }
}
