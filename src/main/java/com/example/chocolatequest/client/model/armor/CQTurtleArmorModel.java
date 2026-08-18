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

public class CQTurtleArmorModel<T extends LivingEntity> extends HumanoidArmorModel<T> {

    public CQTurtleArmorModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createArmorLayer(CubeDeformation deformation) {
        MeshDefinition mesh = HumanoidArmorModel.createMesh(deformation, 0.0F);
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(30, 46)
                        .addBox(-4.5F, -8.0F, -4.0F, 9, 8, 8, new CubeDeformation(0.75F)),
                PartPose.ZERO);
        head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

        PartDefinition body = root.getChild("body");
        body.addOrReplaceChild("shell1",
                CubeListBuilder.create().texOffs(0, 32).addBox(-6.0F, 0.0F, 0.0F, 12, 16, 4),
                PartPose.offset(0.0F, -2.0F, 3.5F));
        body.addOrReplaceChild("shell2",
                CubeListBuilder.create().texOffs(32, 32).addBox(-4.0F, 0.0F, 0.0F, 8, 12, 2),
                PartPose.offset(0.0F, 0.0F, 7.5F));

        root.getChild("left_leg").addOrReplaceChild("tassetLeft",
                CubeListBuilder.create().texOffs(0, 52).mirror().addBox(-1.5F, 0.0F, -2.5F, 3, 7, 5),
                PartPose.offsetAndRotation(2.0F, -1.2F, -0.2F, 0.0F, 0.0F, -0.43633232F));
        root.getChild("right_leg").addOrReplaceChild("tassetRight",
                CubeListBuilder.create().texOffs(0, 52).addBox(-1.5F, 0.0F, -2.5F, 3, 7, 5),
                PartPose.offsetAndRotation(-2.0F, -1.2F, -0.2F, 0.0F, 0.0F, 0.43633232F));

        return LayerDefinition.create(mesh, 64, 64);
    }
}
