package com.example.chocolatequest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import com.example.chocolatequest.entity.projectile.AbstractEntityLaser;

public class ModelLaser extends EntityModel<AbstractEntityLaser> {

	private final ModelPart bone;

	public ModelLaser() {
		super(RenderType::entityTranslucentEmissive);
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -28.0F, -16.0F, 8.0F, 8.0F, 16.0F), PartPose.offset(0.0F, 24.0F, 0.0F));
		this.bone = LayerDefinition.create(meshdefinition, 64, 32).bakeRoot().getChild("bone");
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
		bone.render(matrixStack, buffer, packedLight, packedOverlay, color);
	}

	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		int color = ((int)(alpha * 255) << 24) | ((int)(red * 255) << 16) | ((int)(green * 255) << 8) | (int)(blue * 255);
		bone.render(matrixStack, buffer, packedLight, packedOverlay, color);
	}

	@Override
	public void setupAnim(AbstractEntityLaser pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
	}
}
