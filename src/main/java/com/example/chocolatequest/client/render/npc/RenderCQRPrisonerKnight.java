package com.example.chocolatequest.client.render.npc;

import com.example.chocolatequest.client.render.RenderCQRBipedBaseGeo;
import com.example.chocolatequest.entity.npc.EntityCQRPrisonerKnight;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQRPrisonerKnight extends RenderCQRBipedBaseGeo<EntityCQRPrisonerKnight> {
    public RenderCQRPrisonerKnight(EntityRendererProvider.Context context) {
        super(context, new ModelCQRNPC<>("human_0"));
    }
}
