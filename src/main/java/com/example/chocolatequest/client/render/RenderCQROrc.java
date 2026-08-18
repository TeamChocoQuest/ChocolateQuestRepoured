package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQROrcGeo;
import com.example.chocolatequest.entity.mob.CQOrcEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQROrc extends RenderCQRBipedBaseGeo<CQOrcEntity> {
    public RenderCQROrc(EntityRendererProvider.Context context) {
        super(context, new ModelCQROrcGeo());
    }
}
