package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQGolemEntity;
import net.minecraft.resources.ResourceLocation;

public class ModelCQRGolemGeo extends AbstractModelHumanoidGeo<CQGolemEntity> {
    public ModelCQRGolemGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_golem.geo.json"),
            "golem",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/golem.animation.json"),
            1
        );
    }
}
