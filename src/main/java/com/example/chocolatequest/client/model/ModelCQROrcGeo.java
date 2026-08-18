package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQOrcEntity;
import net.minecraft.resources.ResourceLocation;

public class ModelCQROrcGeo extends AbstractModelHumanoidGeo<CQOrcEntity> {
    public ModelCQROrcGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_base.geo.json"),
            "orc",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/orc.animation.json"),
            3
        );
    }
}
