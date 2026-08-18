package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQRMinotaurGeo;
import com.example.chocolatequest.entity.mob.CQMinotaurEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQRMinotaur extends RenderCQRBipedBaseGeo<CQMinotaurEntity> {
    public RenderCQRMinotaur(EntityRendererProvider.Context context) {
        super(context, new ModelCQRMinotaurGeo());
    }
}
