package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQDummyEntity;
import net.minecraft.resources.ResourceLocation;

public class ModelCQRDummyGeo extends AbstractModelHumanoidGeo<CQDummyEntity> {
    public ModelCQRDummyGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_base.geo.json"),
            "dummy",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/dummy.animation.json"),
            1
        );
    }
}
