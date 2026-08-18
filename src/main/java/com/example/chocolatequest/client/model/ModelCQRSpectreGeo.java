package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQSpecterEntity;
import net.minecraft.resources.ResourceLocation;

public class ModelCQRSpectreGeo extends AbstractModelHumanoidGeo<CQSpecterEntity> {
    public ModelCQRSpectreGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_base.geo.json"),
            "spectre",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/spectre.animation.json"),
            3 // 3 texture variants: spectre_0, spectre_1, spectre_2
        );
    }
}
