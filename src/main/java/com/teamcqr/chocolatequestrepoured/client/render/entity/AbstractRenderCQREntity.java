package com.teamcqr.chocolatequestrepoured.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractRenderCQREntity<T extends AbstractEntityCQR> extends RenderLiving<T> {

	public ResourceLocation texture;
	public double widthScale;
	public double heightScale;

	public AbstractRenderCQREntity(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn,
			ResourceLocation textureLocation, double widthScale, double heightScale) {
		super(rendermanagerIn, modelbaseIn, shadowsizeIn);
		this.texture = textureLocation;
		this.widthScale = widthScale;
		this.heightScale = heightScale;
		this.addLayer(new LayerBipedArmor(this));
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerArrow(this));
		this.addLayer(new LayerElytra(this));
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GL11.glPushMatrix();
		GL11.glScaled(this.widthScale, this.heightScale, this.widthScale);
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		GL11.glPopMatrix();
	}

	protected ResourceLocation getEntityTexture(T entity) {
		return texture;
	}

}
