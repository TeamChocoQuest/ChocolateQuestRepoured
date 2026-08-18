package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQROgreGeo;
import com.example.chocolatequest.entity.mob.CQOgreEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQROgre extends RenderCQRBipedBaseGeo<CQOgreEntity> {
    public RenderCQROgre(EntityRendererProvider.Context context) {
        super(context, new ModelCQROgreGeo());
    }
}
