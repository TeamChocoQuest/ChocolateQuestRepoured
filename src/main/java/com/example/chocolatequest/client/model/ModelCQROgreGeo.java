package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQOgreEntity;
import net.minecraft.resources.ResourceLocation;

public class ModelCQROgreGeo extends AbstractModelHumanoidGeo<CQOgreEntity> {
    public ModelCQROgreGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_ogre.geo.json"),
            "ogre",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/ogre.animation.json"),
            1
        );
    }
}
