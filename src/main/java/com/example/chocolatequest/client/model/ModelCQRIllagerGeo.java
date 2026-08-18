package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQIllagerEntity;
import net.minecraft.resources.ResourceLocation;

public class ModelCQRIllagerGeo extends AbstractModelHumanoidGeo<CQIllagerEntity> {
    public ModelCQRIllagerGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_illager.geo.json"),
            "illager",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/illager.animation.json"),
            2
        );
    }
}
