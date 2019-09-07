package com.teamcqr.chocolatequestrepoured.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRPigman;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCQRPigman extends AbstractRenderCQREntity<EntityCQRPigman> {

	public RenderCQRPigman(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelBiped(), 0.5F, new ResourceLocation(Reference.MODID, "textures/entity/entity_mob_cqrpigman.png"));
	}

	@Override
	protected void preRenderCallback(EntityCQRPigman entitylivingbaseIn, float partialTickTime) {
		if (entitylivingbaseIn != null) {
			super.preRenderCallback(entitylivingbaseIn, partialTickTime);

			GL11.glScalef(1.2F, 1.1F, 1.2F);
		}
	}

}
