package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQMinotaurEntity;
import net.minecraft.resources.ResourceLocation;

public class ModelCQRMinotaurGeo extends AbstractModelHumanoidGeo<CQMinotaurEntity> {
    public ModelCQRMinotaurGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_minotaur.geo.json"),
            "minotaur",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/minotaur.animation.json"),
            1
        );
    }
}
