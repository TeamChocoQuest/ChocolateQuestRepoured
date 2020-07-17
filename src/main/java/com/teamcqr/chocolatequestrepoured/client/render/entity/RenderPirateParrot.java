package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQRPirateParrotHeldItem;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderParrot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.util.ResourceLocation;

public class RenderPirateParrot extends RenderParrot {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/pirate_parrot.png");

	public RenderPirateParrot(RenderManager renderManager) {
		super(renderManager);

		this.addLayer(new LayerCQRPirateParrotHeldItem(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityParrot entity) {
		return TEXTURE;
	}

}
