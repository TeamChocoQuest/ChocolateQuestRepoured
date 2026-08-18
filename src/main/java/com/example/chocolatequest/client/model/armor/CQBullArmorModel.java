package com.example.chocolatequest.client.model.armor;

import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

public class CQBullArmorModel<T extends LivingEntity> extends HumanoidArmorModel<T> {

    public CQBullArmorModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createArmorLayer(CubeDeformation deformation) {
        MeshDefinition mesh = HumanoidArmorModel.createMesh(deformation, 0.0F);
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.getChild("head");
        PartDefinition rightArm = root.getChild("right_arm");
        PartDefinition leftArm = root.getChild("left_arm");

        PartDefinition hornR1 = head.addOrReplaceChild("hornR1",
                CubeListBuilder.create().texOffs(0, 64).addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3),
                PartPose.offsetAndRotation(-4.5F, -8.5F, -4.5F, 1.1780972F, 0.7853982F, 0.0F));
        PartDefinition hornR2 = hornR1.addOrReplaceChild("hornR2",
                CubeListBuilder.create().texOffs(0, 70).addBox(-1.0F, -1.0F, -1.0F, 2, 3, 2),
                PartPose.offsetAndRotation(0.0F, -3.0F, 0.6F, -0.38397244F, 0.0F, 0.0F));
        hornR2.addOrReplaceChild("hornR3",
                CubeListBuilder.create().texOffs(0, 75).addBox(-0.5F, -3.0F, -0.5F, 1, 3, 1),
                PartPose.offsetAndRotation(0.0F, -0.5F, -0.2F, -0.38397244F, 0.0F, 0.0F));

        PartDefinition hornL1 = head.addOrReplaceChild("hornL1",
                CubeListBuilder.create().texOffs(0, 64).mirror().addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3),
                PartPose.offsetAndRotation(4.5F, -8.5F, -4.5F, 1.1780972F, -0.7853982F, 0.0F));
        PartDefinition hornL2 = hornL1.addOrReplaceChild("hornL2",
                CubeListBuilder.create().texOffs(0, 70).mirror().addBox(-1.0F, -1.0F, -1.0F, 2, 3, 2),
                PartPose.offsetAndRotation(0.0F, -3.0F, 0.6F, -0.38397244F, 0.0F, 0.0F));
        hornL2.addOrReplaceChild("hornL3",
                CubeListBuilder.create().texOffs(0, 75).mirror().addBox(-0.5F, -3.0F, -0.5F, 1, 3, 1),
                PartPose.offsetAndRotation(0.0F, -0.5F, -0.2F, -0.38397244F, 0.0F, 0.0F));

        PartDefinition pauldronR1 = rightArm.addOrReplaceChild("pauldronR1",
                CubeListBuilder.create().texOffs(64, 0).addBox(-3.0F, -1.0F, -2.0F, 3, 2, 4),
                PartPose.offsetAndRotation(-3.3F, 0.5F, 0.0F, 0.0F, 0.0F, -0.3926991F));
        PartDefinition pauldronR2 = pauldronR1.addOrReplaceChild("pauldronR2",
                CubeListBuilder.create().texOffs(64, 6).addBox(-1.0F, -0.5F, -2.0F, 2, 1, 4),
                PartPose.offset(-0.5F, -1.5F, 0.0F));
        PartDefinition shoulderHornR1 = pauldronR2.addOrReplaceChild("shoulderHornR1",
                CubeListBuilder.create().texOffs(64, 11).addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3),
                PartPose.offsetAndRotation(2.0F, -1.6F, 0.0F, 0.0F, 0.0F, -0.19198622F));
        PartDefinition shoulderHornR2 = shoulderHornR1.addOrReplaceChild("shoulderHornR2",
                CubeListBuilder.create().texOffs(64, 17).addBox(-1.0F, -3.0F, -1.0F, 2, 3, 2),
                PartPose.offsetAndRotation(-0.2F, -1.0F, 0.0F, 0.0F, 0.0F, 0.38397244F));
        shoulderHornR2.addOrReplaceChild("shoulderHornR3",
                CubeListBuilder.create().texOffs(64, 22).addBox(-0.5F, -3.0F, -0.5F, 1, 3, 1),
                PartPose.offsetAndRotation(-0.1F, -2.8F, 0.1F, 0.0F, 0.0F, 0.38397244F));

        PartDefinition pauldronL1 = leftArm.addOrReplaceChild("pauldronL1",
                CubeListBuilder.create().texOffs(64, 0).mirror().addBox(0.0F, -1.0F, -2.0F, 3, 2, 4),
                PartPose.offsetAndRotation(3.3F, 0.5F, 0.0F, 0.0F, 0.0F, 0.3926991F));
        PartDefinition pauldronL2 = pauldronL1.addOrReplaceChild("pauldronL2",
                CubeListBuilder.create().texOffs(64, 6).mirror().addBox(0.0F, -0.5F, -2.0F, 2, 1, 4),
                PartPose.offset(-0.5F, -1.5F, 0.0F));
        PartDefinition shoulderHornL1 = pauldronL2.addOrReplaceChild("shoulderHornL1",
                CubeListBuilder.create().texOffs(64, 11).mirror().addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3),
                PartPose.offsetAndRotation(-0.8F, -1.6F, 0.0F, 0.0F, 0.0F, 0.19198622F));
        PartDefinition shoulderHornL2 = shoulderHornL1.addOrReplaceChild("shoulderHornL2",
                CubeListBuilder.create().texOffs(64, 17).mirror().addBox(-1.0F, -3.0F, -1.0F, 2, 3, 2),
                PartPose.offsetAndRotation(0.2F, -1.0F, 0.0F, 0.0F, 0.0F, -0.38397244F));
        shoulderHornL2.addOrReplaceChild("shoulderHornL3",
                CubeListBuilder.create().texOffs(64, 22).mirror().addBox(-0.5F, -3.0F, -0.5F, 1, 3, 1),
                PartPose.offsetAndRotation(0.1F, -2.8F, 0.1F, 0.0F, 0.0F, -0.38397244F));

        return LayerDefinition.create(mesh, 128, 128);
    }
}
