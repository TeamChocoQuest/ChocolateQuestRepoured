package com.example.chocolatequest.client.render.npc;

import com.example.chocolatequest.entity.npc.EntityCQRMerchant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RenderCQRMerchant extends GeoEntityRenderer<EntityCQRMerchant> {
    public RenderCQRMerchant(EntityRendererProvider.Context context) {
        super(context, new ModelCQRNPC<>("human_1"));
    }
}
