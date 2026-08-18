package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQRBoarmanGeo;
import com.example.chocolatequest.entity.mob.CQBoarmanEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQRBoarman extends RenderCQRBipedBaseGeo<CQBoarmanEntity> {
    public RenderCQRBoarman(EntityRendererProvider.Context context) {
        super(context, new ModelCQRBoarmanGeo());
    }
}
