package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQRPirateCaptainGeo;
import com.example.chocolatequest.entity.boss.PirateCaptainEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQRPirateCaptain extends RenderCQRBipedBaseGeo<PirateCaptainEntity> {
    public RenderCQRPirateCaptain(EntityRendererProvider.Context context) {
        super(context, new ModelCQRPirateCaptainGeo());
    }
}
