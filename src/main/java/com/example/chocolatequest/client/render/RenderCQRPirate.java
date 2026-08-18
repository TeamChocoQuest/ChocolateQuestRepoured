package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQRPirateGeo;
import com.example.chocolatequest.entity.mob.CQPirateEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQRPirate extends RenderCQRBipedBaseGeo<CQPirateEntity> {
    public RenderCQRPirate(EntityRendererProvider.Context context) {
        super(context, new ModelCQRPirateGeo());
    }
}
