package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQRGolemGeo;
import com.example.chocolatequest.entity.mob.CQGolemEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQRGolem extends RenderCQRBipedBaseGeo<CQGolemEntity> {
    public RenderCQRGolem(EntityRendererProvider.Context context) {
        super(context, new ModelCQRGolemGeo());
    }
}
