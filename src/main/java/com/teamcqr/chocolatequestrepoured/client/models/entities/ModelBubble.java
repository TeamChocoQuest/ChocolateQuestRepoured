package com.teamcqr.chocolatequestrepoured.client.models.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBubble extends ModelBase {

	private final ModelRenderer bone;
	private final ModelRenderer faceTop;
	private final ModelRenderer faceBottom;
	private final ModelRenderer faceNorth;
	private final ModelRenderer faceSouth;
	private final ModelRenderer faceEast;
	private final ModelRenderer faceWest;

	public ModelBubble() {
		this.textureWidth = 128;
		this.textureHeight = 128;

		this.bone = new ModelRenderer(this);
		this.bone.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bone.cubeList.add(new ModelBox(this.bone, 0, 0, -7.0F, -13.0F, -7.0F, 14, 12, 14, 0.0F, false));
		this.bone.cubeList.add(new ModelBox(this.bone, 0, 26, -6.0F, -14.0F, -7.0F, 12, 1, 14, 0.0F, false));
		this.bone.cubeList.add(new ModelBox(this.bone, 0, 41, -7.0F, -14.0F, -6.0F, 1, 1, 12, 0.0F, false));
		this.bone.cubeList.add(new ModelBox(this.bone, 26, 41, 6.0F, -14.0F, -6.0F, 1, 1, 12, 0.0F, false));
		this.bone.cubeList.add(new ModelBox(this.bone, 0, 54, -6.0F, -1.0F, -7.0F, 12, 1, 14, 0.0F, false));
		this.bone.cubeList.add(new ModelBox(this.bone, 0, 69, -7.0F, -1.0F, -6.0F, 1, 1, 12, 0.0F, false));
		this.bone.cubeList.add(new ModelBox(this.bone, 26, 69, 6.0F, -1.0F, -6.0F, 1, 1, 12, 0.0F, false));

		this.faceTop = new ModelRenderer(this);
		this.faceTop.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.faceTop.cubeList.add(new ModelBox(this.faceTop, 56, 0, -5.0F, -15.0F, -5.0F, 10, 1, 10, 0.0F, false));

		this.faceBottom = new ModelRenderer(this);
		this.faceBottom.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.faceBottom.cubeList.add(new ModelBox(this.faceBottom, 56, 11, -5.0F, 0.0F, -5.0F, 10, 1, 10, 0.0F, false));

		this.faceNorth = new ModelRenderer(this);
		this.faceNorth.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.faceNorth.cubeList.add(new ModelBox(this.faceNorth, 56, 22, -5.0F, -12.0F, -8.0F, 10, 10, 1, 0.0F, false));

		this.faceSouth = new ModelRenderer(this);
		this.faceSouth.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.faceSouth.cubeList.add(new ModelBox(this.faceSouth, 78, 22, -5.0F, -12.0F, 7.0F, 10, 10, 1, 0.0F, false));

		this.faceEast = new ModelRenderer(this);
		this.faceEast.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.faceEast.cubeList.add(new ModelBox(this.faceEast, 56, 33, -8.0F, -12.0F, -5.0F, 1, 10, 10, 0.0F, false));

		this.faceWest = new ModelRenderer(this);
		this.faceWest.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.faceWest.cubeList.add(new ModelBox(this.faceWest, 78, 33, 7.0F, -12.0F, -5.0F, 1, 10, 10, 0.0F, false));
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.bone.render(scale);
		this.faceTop.render(scale);
		this.faceBottom.render(scale);
		this.faceNorth.render(scale);
		this.faceSouth.render(scale);
		this.faceEast.render(scale);
		this.faceWest.render(scale);
	}

}
