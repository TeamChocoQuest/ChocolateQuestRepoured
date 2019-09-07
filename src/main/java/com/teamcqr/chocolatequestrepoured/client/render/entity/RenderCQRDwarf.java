package com.teamcqr.chocolatequestrepoured.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRDwarf;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCQRDwarf extends AbstractRenderCQREntity<EntityCQRDwarf> {

	public RenderCQRDwarf(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelBiped(), 0.5F, new ResourceLocation(Reference.MODID, "textures/entity/entity_mob_cqrdwarf.png"));
	}

	@Override
	protected void preRenderCallback(EntityCQRDwarf entitylivingbaseIn, float partialTickTime) {
		if (entitylivingbaseIn != null) {
			super.preRenderCallback(entitylivingbaseIn, partialTickTime);

			GL11.glScalef(0.9F, 0.65F, 0.9F);
		}
	}

}
