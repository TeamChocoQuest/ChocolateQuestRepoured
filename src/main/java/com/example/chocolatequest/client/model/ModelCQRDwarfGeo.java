package com.example.chocolatequest.client.model;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.mob.CQDwarfEntity;
import net.minecraft.resources.ResourceLocation;

public class ModelCQRDwarfGeo extends AbstractModelHumanoidGeo<CQDwarfEntity> {
    public ModelCQRDwarfGeo() {
        super(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "geo/entity/biped_base.geo.json"),
            "dwarf",
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "animations/entity/humanoid/dwarf.animation.json"),
            3
        );
    }
}
