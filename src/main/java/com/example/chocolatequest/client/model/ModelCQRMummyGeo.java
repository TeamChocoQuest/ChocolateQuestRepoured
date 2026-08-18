package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQMummyEntity;
import net.minecraft.resources.ResourceLocation;

public class ModelCQRMummyGeo extends AbstractModelHumanoidGeo<CQMummyEntity> {
    public ModelCQRMummyGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_base.geo.json"),
            "mummy",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/mummy.animation.json"),
            1
        );
    }
}
