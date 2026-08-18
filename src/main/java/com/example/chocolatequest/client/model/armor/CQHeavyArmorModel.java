package com.example.chocolatequest.client.model.armor;

import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class CQHeavyArmorModel<T extends LivingEntity> extends HumanoidArmorModel<T> {

    public CQHeavyArmorModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createArmorLayer(CubeDeformation deformation) {
        MeshDefinition meshdefinition = HumanoidArmorModel.createMesh(deformation, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.getChild("head");
        head.addOrReplaceChild("lowerHeadArmor", 
            CubeListBuilder.create().texOffs(0, 64).addBox(-4.5F, -8.5F, -4.5F, 9, 9, 9, new CubeDeformation(0.49F)), 
            PartPose.ZERO);

        PartDefinition body = partdefinition.getChild("body");
        body.addOrReplaceChild("chestExtension", 
            CubeListBuilder.create().texOffs(24, 96).addBox(-4.5F, -0.5F, -2.5F, 9, 13, 5, new CubeDeformation(0.6F)), 
            PartPose.ZERO);

        PartDefinition rightArm = partdefinition.getChild("right_arm");
        PartDefinition pauldronR1 = rightArm.addOrReplaceChild("pauldronR1", 
            CubeListBuilder.create().texOffs(0, 96).addBox(-4.0F, -3.5F, -2.5F, 5, 5, 5, new CubeDeformation(0.75F)), 
            PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.08726646259971647F));
            
        pauldronR1.addOrReplaceChild("pauldronR2", 
            CubeListBuilder.create().texOffs(0, 106).addBox(0.0F, -3.75F, -3.0F, 6, 6, 6, new CubeDeformation(0.75F)), 
            PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.17453292519943295F));

        PartDefinition leftArm = partdefinition.getChild("left_arm");
        PartDefinition pauldronL1 = leftArm.addOrReplaceChild("pauldronL1", 
            CubeListBuilder.create().texOffs(0, 96).mirror().addBox(-1.0F, -3.5F, -2.5F, 5, 5, 5, new CubeDeformation(0.75F)), 
            PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.08726646259971647F));
            
        pauldronL1.addOrReplaceChild("pauldronL2", 
            CubeListBuilder.create().texOffs(0, 106).mirror().addBox(-6.0F, -3.75F, -3.0F, 6, 6, 6, new CubeDeformation(0.75F)), 
            PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.17453292519943295F));

        PartDefinition rightLeg = partdefinition.getChild("right_leg");
        rightLeg.addOrReplaceChild("skirtR", 
            CubeListBuilder.create().texOffs(64, 64).addBox(-0.5F, -0.5F, -3.0F, 6, 6, 6, new CubeDeformation(-0.25F)), 
            PartPose.offsetAndRotation(-2.6F, -2.5F, 0.0F, 0.0F, 0.0F, 0.2617993877991494F));

        PartDefinition leftLeg = partdefinition.getChild("left_leg");
        leftLeg.addOrReplaceChild("skirtL", 
            CubeListBuilder.create().texOffs(64, 64).mirror().addBox(-5.6F, -0.5F, -3.0F, 6, 6, 6, new CubeDeformation(-0.25F)), 
            PartPose.offsetAndRotation(2.6F, -2.5F, 0.0F, 0.0F, 0.0F, -0.2617993877991494F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
