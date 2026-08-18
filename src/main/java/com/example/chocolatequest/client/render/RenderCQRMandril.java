package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQRMandrilGeo;
import com.example.chocolatequest.entity.mob.CQMandrilEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQRMandril extends RenderCQRBipedBaseGeo<CQMandrilEntity> {
    public RenderCQRMandril(EntityRendererProvider.Context context) {
        super(context, new ModelCQRMandrilGeo());
    }
}
