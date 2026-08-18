package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQNPCEntity;
import net.minecraft.resources.ResourceLocation;

public class ModelCQRNPCGeo extends AbstractModelHumanoidGeo<CQNPCEntity> {
    public ModelCQRNPCGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_base.geo.json"),
            "human",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/npc.animation.json"),
            10
        );
    }
}
