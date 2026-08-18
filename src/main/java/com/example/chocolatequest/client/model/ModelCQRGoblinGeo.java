package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQGoblinEntity;
import net.minecraft.resources.ResourceLocation;

public class ModelCQRGoblinGeo extends AbstractModelHumanoidGeo<CQGoblinEntity> {
    public ModelCQRGoblinGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_goblin.geo.json"),
            "goblin",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/goblin.animation.json"),
            1
        );
    }
}
