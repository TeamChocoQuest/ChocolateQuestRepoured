package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQRIllagerGeo;
import com.example.chocolatequest.entity.mob.CQIllagerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQRIllager extends RenderCQRBipedBaseGeo<CQIllagerEntity> {
    public RenderCQRIllager(EntityRendererProvider.Context context) {
        super(context, new ModelCQRIllagerGeo());
    }
}
