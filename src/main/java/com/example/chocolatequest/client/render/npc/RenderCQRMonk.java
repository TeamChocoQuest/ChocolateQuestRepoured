package com.example.chocolatequest.client.render.npc;

import com.example.chocolatequest.entity.npc.EntityCQRMonk;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RenderCQRMonk extends GeoEntityRenderer<EntityCQRMonk> {
    public RenderCQRMonk(EntityRendererProvider.Context context) {
        super(context, new ModelCQRNPC<>("human_4"));
    }
}
