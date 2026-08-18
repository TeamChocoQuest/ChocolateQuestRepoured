package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQRWalkerGeo;
import com.example.chocolatequest.entity.mob.CQWalkerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQRWalker extends RenderCQRBipedBaseGeo<CQWalkerEntity> {
    public RenderCQRWalker(EntityRendererProvider.Context context) {
        super(context, new ModelCQRWalkerGeo());
    }
}
