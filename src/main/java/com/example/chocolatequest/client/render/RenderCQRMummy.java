package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQRMummyGeo;
import com.example.chocolatequest.entity.mob.CQMummyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQRMummy extends RenderCQRBipedBaseGeo<CQMummyEntity> {
    public RenderCQRMummy(EntityRendererProvider.Context context) {
        super(context, new ModelCQRMummyGeo());
    }
}
