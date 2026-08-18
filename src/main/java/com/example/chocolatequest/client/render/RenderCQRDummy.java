package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQRDummyGeo;
import com.example.chocolatequest.entity.mob.CQDummyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQRDummy extends RenderCQRBipedBaseGeo<CQDummyEntity> {
    public RenderCQRDummy(EntityRendererProvider.Context context) {
        super(context, new ModelCQRDummyGeo());
    }
}
