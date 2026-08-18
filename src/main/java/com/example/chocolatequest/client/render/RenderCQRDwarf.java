package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQRDwarfGeo;
import com.example.chocolatequest.entity.mob.CQDwarfEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQRDwarf extends RenderCQRBipedBaseGeo<CQDwarfEntity> {
    public RenderCQRDwarf(EntityRendererProvider.Context context) {
        super(context, new ModelCQRDwarfGeo());
    }
}
