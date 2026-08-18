package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQRNPCGeo;
import com.example.chocolatequest.entity.mob.CQNPCEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQRNPC extends RenderCQRBipedBaseGeo<CQNPCEntity> {
    public RenderCQRNPC(EntityRendererProvider.Context context) {
        super(context, new ModelCQRNPCGeo());
    }
}
