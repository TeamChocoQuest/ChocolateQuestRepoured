package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQMandrilEntity;
import net.minecraft.resources.ResourceLocation;

public class ModelCQRMandrilGeo extends AbstractModelHumanoidGeo<CQMandrilEntity> {
    public ModelCQRMandrilGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_mandril.geo.json"),
            "mandril",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/mandril.animation.json"),
            1
        );
    }
}
