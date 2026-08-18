package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQBoarmanEntity;
import net.minecraft.resources.ResourceLocation;

public class ModelCQRBoarmanGeo extends AbstractModelHumanoidGeo<CQBoarmanEntity> {
    public ModelCQRBoarmanGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_boarman.geo.json"),
            "boarman",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/boarman.animation.json"),
            3
        );
    }
}
