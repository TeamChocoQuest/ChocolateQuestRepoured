package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQZombieEntity;
import net.minecraft.resources.ResourceLocation;

public class ModelCQRZombieGeo extends AbstractModelHumanoidGeo<CQZombieEntity> {
    public ModelCQRZombieGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_base.geo.json"),
            "zombie",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/zombie.animation.json"),
            3 // 3 texture variants: zombie_0, zombie_1, zombie_2
        );
    }
}
