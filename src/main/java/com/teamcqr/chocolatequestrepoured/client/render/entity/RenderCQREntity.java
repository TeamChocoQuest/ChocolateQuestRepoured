package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;

public class RenderCQREntity<T extends AbstractEntityCQR> extends AbstractRenderCQREntity<T> {

	public RenderCQREntity(RenderManager rendermanagerIn, String entityName) {
		super(rendermanagerIn, new ModelBiped(), 0.5F, new ResourceLocation(Reference.MODID, "textures/entity/" + entityName + ".png"));
	}

}
