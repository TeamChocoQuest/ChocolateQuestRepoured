package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQRHumanGeo;
import com.example.chocolatequest.entity.mob.CQHumanEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQRHuman extends RenderCQRBipedBaseGeo<CQHumanEntity> {
    public RenderCQRHuman(EntityRendererProvider.Context context) {
        super(context, new ModelCQRHumanGeo());
    }
}
