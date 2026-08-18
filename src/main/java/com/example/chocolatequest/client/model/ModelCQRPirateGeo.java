package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQPirateEntity;
import net.minecraft.resources.ResourceLocation;

public class ModelCQRPirateGeo extends AbstractModelHumanoidGeo<CQPirateEntity> {
    public ModelCQRPirateGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_base.geo.json"),
            "pirate",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/pirate.animation.json"),
            3 // 3 texture variants
        );
    }
}
