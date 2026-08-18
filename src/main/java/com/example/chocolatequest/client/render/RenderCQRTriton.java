package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQRTritonGeo;
import com.example.chocolatequest.entity.mob.CQTritonEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQRTriton extends RenderCQRBipedBaseGeo<CQTritonEntity> {
    public RenderCQRTriton(EntityRendererProvider.Context context) {
        super(context, new ModelCQRTritonGeo());
    }
}
