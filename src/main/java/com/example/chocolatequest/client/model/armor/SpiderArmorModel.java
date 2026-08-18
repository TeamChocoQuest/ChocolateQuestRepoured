package com.example.chocolatequest.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

public class SpiderArmorModel<T extends LivingEntity> extends HumanoidModel<T> {

    public SpiderArmorModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(deformation, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.getChild("head");
        head.addOrReplaceChild("helmR", CubeListBuilder.create().texOffs(0, 64).addBox(0.0F, -1.0F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -2.0F, -5.0F, 0.0F, -0.7853982F, 0.0F));
        head.addOrReplaceChild("helmL", CubeListBuilder.create().texOffs(0, 64).mirror().addBox(-1.0F, -1.0F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.0F, -2.0F, -5.0F, 0.0F, 0.7853982F, 0.0F));

        PartDefinition body = partdefinition.getChild("body");
        body.addOrReplaceChild("bipedBody_1", CubeListBuilder.create().texOffs(64, 84).addBox(-3.0F, 2.0F, 0.0F, 6.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.2617994F, 0.0F, 0.0F));

        PartDefinition arm1part1 = body.addOrReplaceChild("arm1part1", CubeListBuilder.create().texOffs(64, 64).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 2.0F, 3.0F, 2.5307274F, 0.0F, 0.7853982F));
        arm1part1.addOrReplaceChild("arm1part2", CubeListBuilder.create().texOffs(64, 74).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 1.5707964F, 0.0F, 0.0F));

        PartDefinition arm2part1 = body.addOrReplaceChild("arm2part1", CubeListBuilder.create().texOffs(64, 64).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 2.0F, 3.0F, 2.5307274F, 0.0F, -0.7853982F));
        arm2part1.addOrReplaceChild("arm2part2", CubeListBuilder.create().texOffs(64, 74).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 1.5707964F, 0.0F, 0.0F));

        PartDefinition arm3part1 = body.addOrReplaceChild("arm3part1", CubeListBuilder.create().texOffs(64, 64).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 5.0F, 3.0F, 1.5707964F, 0.7853982F, 0.0F));
        arm3part1.addOrReplaceChild("arm3part2", CubeListBuilder.create().texOffs(64, 74).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, 0.0F, -1.5707964F));

        PartDefinition arm4part1 = body.addOrReplaceChild("arm4part1", CubeListBuilder.create().texOffs(64, 64).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 5.0F, 3.0F, 1.5707964F, -0.7853982F, 0.0F));
        arm4part1.addOrReplaceChild("arm4part2", CubeListBuilder.create().texOffs(64, 74).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, 0.0F, 1.5707964F));

        PartDefinition arm5part1 = body.addOrReplaceChild("arm5part1", CubeListBuilder.create().texOffs(64, 64).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 8.0F, 3.0F, 0.7853982F, 0.7853982F, 0.0F));
        arm5part1.addOrReplaceChild("arm5part2", CubeListBuilder.create().texOffs(64, 74).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, 0.0F, -1.5707964F));

        PartDefinition arm6part1 = body.addOrReplaceChild("arm6part1", CubeListBuilder.create().texOffs(64, 64).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 8.0F, 3.0F, 0.7853982F, -0.7853982F, 0.0F));
        arm6part1.addOrReplaceChild("arm6part2", CubeListBuilder.create().texOffs(64, 74).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, 0.0F, 1.5707964F));

        PartDefinition rightArm = partdefinition.getChild("right_arm");
        rightArm.addOrReplaceChild("armRarm", CubeListBuilder.create().texOffs(64, 0).mirror().addBox(-1.0F, -4.0F, -5.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.9F, -0.75F, 0.0F, 0.0F, 0.7853982F, 0.0F));

        PartDefinition leftArm = partdefinition.getChild("left_arm");
        leftArm.addOrReplaceChild("armLarm", CubeListBuilder.create().texOffs(64, 0).mirror().addBox(-1.0F, -4.0F, -5.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.9F, -0.75F, 0.0F, 0.0F, -0.7853982F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
