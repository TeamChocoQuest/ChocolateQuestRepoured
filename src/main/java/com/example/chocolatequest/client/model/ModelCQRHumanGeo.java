package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQHumanEntity;
import net.minecraft.resources.ResourceLocation;

public class ModelCQRHumanGeo extends AbstractModelHumanoidGeo<CQHumanEntity> {
    public ModelCQRHumanGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_base.geo.json"),
            "human",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/human.animation.json"),
            10
        );
    }
}
