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

public abstract class AbstractRenderCQREntity<T extends AbstractEntityCQR> extends RenderLiving<T> {

	public ResourceLocation texture;

	public AbstractRenderCQREntity(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn, ResourceLocation textureLocation) {
		super(rendermanagerIn, modelbaseIn, shadowsizeIn);
		this.texture = textureLocation;
		this.addLayer(new LayerBipedArmor(this));
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerArrow(this));
		this.addLayer(new LayerElytra(this));
	}

	protected ResourceLocation getEntityTexture(T entity) {
		return texture;
	}

}
