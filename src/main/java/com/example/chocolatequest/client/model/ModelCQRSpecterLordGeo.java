package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.boss.EntityCQRSpecterLord;
import net.minecraft.resources.ResourceLocation;

public class ModelCQRSpecterLordGeo extends AbstractModelHumanoidGeo<EntityCQRSpecterLord> {
    public ModelCQRSpecterLordGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/boss/biped_humanoid_boss.geo.json"),
            "specter_boss",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/spectre.animation.json"),
            1
        );
    }

    @Override
    public ResourceLocation getTextureResource(EntityCQRSpecterLord object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/entity/boss/specter_boss.png");
    }
}
