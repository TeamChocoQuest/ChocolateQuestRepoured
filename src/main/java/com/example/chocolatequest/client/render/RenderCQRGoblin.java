package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQRGoblinGeo;
import com.example.chocolatequest.entity.mob.CQGoblinEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQRGoblin extends RenderCQRBipedBaseGeo<CQGoblinEntity> {
    public RenderCQRGoblin(EntityRendererProvider.Context context) {
        super(context, new ModelCQRGoblinGeo());
    }
}
