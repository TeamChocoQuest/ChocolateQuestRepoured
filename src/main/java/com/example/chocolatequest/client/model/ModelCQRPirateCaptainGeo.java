package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.boss.PirateCaptainEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ModelCQRPirateCaptainGeo extends AbstractModelHumanoidGeo<PirateCaptainEntity> {
    
    public ModelCQRPirateCaptainGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_base.geo.json"),
            "pirate_captain",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/boss/pirate_captain.animation.json"),
            1
        );
    }

    @Override
    public ResourceLocation getTextureResource(PirateCaptainEntity object) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/entity/boss/pirate_captain.png");
    }
}
