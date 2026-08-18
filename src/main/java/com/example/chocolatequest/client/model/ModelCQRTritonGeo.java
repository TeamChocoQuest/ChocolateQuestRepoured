package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQTritonEntity;
import net.minecraft.resources.ResourceLocation;

public class ModelCQRTritonGeo extends AbstractModelHumanoidGeo<CQTritonEntity> {
    public ModelCQRTritonGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_triton.geo.json"),
            "triton",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/triton.animation.json"),
            2
        );
    }
}
