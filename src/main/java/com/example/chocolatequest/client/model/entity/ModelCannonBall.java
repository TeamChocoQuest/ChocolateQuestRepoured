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
import com.example.chocolatequest.entity.projectile.ProjectileCannonBall;

public class ModelCannonBall<T extends ProjectileCannonBall> extends EntityModel<T> {
	private final ModelPart bb_main;

	public ModelCannonBall(ModelPart root) {
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create()
			.texOffs(0, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 1.0F, 4.0F)
			.texOffs(0, 0).addBox(-2.0F, -5.0F, 2.0F, 4.0F, 4.0F, 1.0F)
			.texOffs(0, 0).addBox(-2.0F, -5.0F, -3.0F, 4.0F, 4.0F, 1.0F)
			.texOffs(0, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 1.0F, 4.0F)
			.texOffs(0, 0).addBox(2.0F, -5.0F, -2.0F, 1.0F, 4.0F, 4.0F)
			.texOffs(0, 0).addBox(-3.0F, -5.0F, -2.0F, 1.0F, 4.0F, 4.0F), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}
