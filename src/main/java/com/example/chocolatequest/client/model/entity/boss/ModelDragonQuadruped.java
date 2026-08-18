package com.example.chocolatequest.client.model.entity.boss;

import com.example.chocolatequest.entity.boss.EntityCQRDragon;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.example.chocolatequest.ChocolateQuestReDone;

public class ModelDragonQuadruped extends HierarchicalModel<EntityCQRDragon> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "cqr_dragon"), "main");

    private final ModelPart root;
    private final ModelPart body1;
    private final ModelPart body2;
    private final ModelPart body3;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart tail;
    private final ModelPart tail2;
    private final ModelPart tailEnd;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    
    private final ModelPart leg0;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;

    public ModelDragonQuadruped(ModelPart root) {
        this.root = root;
        this.body1 = root.getChild("body1");
        this.body2 = root.getChild("body2");
        this.body3 = root.getChild("body3");
        this.neck = root.getChild("neck");
        this.head = root.getChild("head");
        this.tail = root.getChild("tail");
        this.tail2 = root.getChild("tail2");
        this.tailEnd = root.getChild("tailEnd");
        this.rightWing = root.getChild("rightWing");
        this.leftWing = root.getChild("leftWing");
        
        this.leg0 = root.getChild("leg0");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition root = meshdefinition.getRoot();

        root.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -7.0F, -7.0F, 14, 14, 14), PartPose.offsetAndRotation(0.0F, 0.0F, -10.0F, 0.7853982F, 0.0F, 0.0F));
        root.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(25, 40).addBox(-5.0F, -4.0F, -8.0F, 10, 8, 16), PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("body3", CubeListBuilder.create().texOffs(0, 28).addBox(-5.0F, -5.0F, -5.0F, 10, 10, 10), PartPose.offsetAndRotation(0.0F, 0.0F, 10.0F, 0.7853982F, 0.0F, 0.0F));
        
        root.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 51).addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6), PartPose.offset(0.0F, -6.0F, -14.0F));
        
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(74, 0).addBox(-2.5F, -3.0F, -4.0F, 5, 5, 8), PartPose.ZERO);
        head.addOrReplaceChild("mouthUp", CubeListBuilder.create().texOffs(106, 0).addBox(-2.0F, -2.0F, -11.0F, 4, 3, 7), PartPose.ZERO);
        head.addOrReplaceChild("mouthDown", CubeListBuilder.create().texOffs(92, 0).addBox(-2.0F, 1.0F, -10.0F, 4, 1, 6), PartPose.ZERO);
        head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(107, 11).addBox(-2.0F, -3.0F, -11.0F, 4, 1, 1), PartPose.ZERO);
        head.addOrReplaceChild("hornLeft", CubeListBuilder.create().texOffs(94, 8).addBox(1.5F, -2.0F, 3.0F, 1, 1, 8), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.5F, 0.098F, 0.0F));
        head.addOrReplaceChild("hornRight", CubeListBuilder.create().texOffs(94, 8).addBox(-2.5F, -2.0F, 3.0F, 1, 1, 8), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.5F, -0.298F, 0.0F));

        root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(40, 28).addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4), PartPose.offset(0.0F, -3.0F, 14.0F));
        root.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(42, 30).addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2), PartPose.offset(0.0F, -3.0F, 14.0F));
        root.addOrReplaceChild("tailEnd", CubeListBuilder.create().texOffs(44, 0).addBox(0.0F, -2.0F, -2.0F, 0, 4, 4), PartPose.ZERO);

        PartDefinition rightWing = root.addOrReplaceChild("rightWing", CubeListBuilder.create().texOffs(58, 43).addBox(0.0F, 0.0F, 0.0F, 14, 0, 21), PartPose.offset(6.0F, -4.0F, -10.0F));
        rightWing.addOrReplaceChild("rightWingArm", CubeListBuilder.create().texOffs(82, 17).addBox(0.0F, -1.0F, 0.0F, 14, 2, 2), PartPose.ZERO);
        PartDefinition rightWingPart = rightWing.addOrReplaceChild("rightWingPart", CubeListBuilder.create().texOffs(46, 21).addBox(0.0F, 0.0F, 0.0F, 30, 0, 22), PartPose.offset(14.0F, 0.0F, 0.0F));
        rightWingPart.addOrReplaceChild("rightWingArmPart", CubeListBuilder.create().texOffs(64, 18).addBox(0.0F, -1.0F, 0.0F, 30, 1, 1), PartPose.ZERO);

        PartDefinition leftWing = root.addOrReplaceChild("leftWing", CubeListBuilder.create().texOffs(58, 43).addBox(-14.0F, 0.0F, 0.0F, 14, 0, 21), PartPose.offset(-6.0F, -4.0F, -10.0F));
        leftWing.addOrReplaceChild("leftWingArm", CubeListBuilder.create().texOffs(82, 17).addBox(-14.0F, -1.0F, 0.0F, 14, 2, 2), PartPose.ZERO);
        PartDefinition leftWingPart = leftWing.addOrReplaceChild("leftWingPart", CubeListBuilder.create().texOffs(46, 21).addBox(-30.0F, 0.0F, 0.0F, 30, 0, 22), PartPose.offset(-14.0F, 0.0F, 0.0F));
        leftWingPart.addOrReplaceChild("leftWingArmPart", CubeListBuilder.create().texOffs(64, 18).addBox(-30.0F, -1.0F, 0.0F, 30, 1, 1), PartPose.ZERO);

        createLeg(root, "leg0", 8.0F, 5.0F, -10.0F);
        createLeg(root, "leg1", -10.0F, 5.0F, -10.0F);
        createLeg(root, "leg2", 6.0F, 5.0F, 10.0F);
        createLeg(root, "leg3", -8.0F, 5.0F, 10.0F);

        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    private static void createLeg(PartDefinition root, String name, float x, float y, float z) {
        PartDefinition leg = root.addOrReplaceChild(name, CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, -4.0F, -2.0F, 4, 14, 4), PartPose.offset(x, y, z));
        PartDefinition leg1 = leg.addOrReplaceChild(name + "_1", CubeListBuilder.create().texOffs(56, 19).addBox(-1.5F, 1.0F, -1.5F, 3, 10, 3), PartPose.offset(1.0F, 7.0F, -1.0F));
        leg1.addOrReplaceChild(name + "_foot", CubeListBuilder.create().texOffs(52, 32).addBox(-1.5F, -1.0F, -4.0F, 3, 2, 5), PartPose.offset(0.0F, 11.0F, 0.0F));
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    private float animF;
    private float animF1;
    private float animF2;

    @Override
    public void setupAnim(EntityCQRDragon entity, float f, float f1, float f2, float f3, float f4) {
        this.animF = f;
        this.animF1 = f1;
        this.animF2 = f2;
        
        float rx = Mth.cos(f * 0.4662F) * 1.4F * f1;
        
        this.leg0.xRot = rx;
        this.leg1.xRot = -rx;
        this.leg2.xRot = rx;
        this.leg3.xRot = -rx;
        
        ModelPart l01 = this.leg0.getChild("leg0_1");
        ModelPart l11 = this.leg1.getChild("leg1_1");
        ModelPart l21 = this.leg2.getChild("leg2_1");
        ModelPart l31 = this.leg3.getChild("leg3_1");
        
        l01.xRot = -0.436F - rx * 2.0F;
        l11.xRot = -0.436F - (-rx) * 2.0F;
        l21.xRot = -0.436F - rx * 2.0F;
        l31.xRot = -0.436F - (-rx) * 2.0F;
        
        float wingRotation = Mth.cos((f + f2) * 0.1F);
        this.rightWing.zRot = wingRotation;
        this.leftWing.zRot = -wingRotation;
        
        this.rightWing.getChild("rightWingPart").zRot = wingRotation / 2.0F;
        this.leftWing.getChild("leftWingPart").zRot = -wingRotation / 2.0F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        this.body1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.body2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.body3.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        
        this.leg0.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.leg1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.leg2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.leg3.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        
        this.rightWing.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.leftWing.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);

        float px = 0.0F;
        float py = -6.0F;
        float pz = -14.0F;
        
        float headBaseYRot = this.head.yRot;
        float headBaseXRot = this.head.xRot;

        for (int i = 0; i < 4; ++i) {
            this.neck.xRot = (float) i / 4.0F - 0.4F + headBaseXRot;
            this.neck.yRot = headBaseYRot * ((float) i / 1.8F);
            this.neck.zRot = headBaseYRot / 10.0F;
            this.neck.x = px;
            this.neck.y = py;
            this.neck.z = pz;
            px = (float) ((double) px - Math.sin(this.neck.yRot) * Math.cos(this.neck.xRot) * 6.0);
            py = (float) ((double) py + Math.sin(this.neck.xRot) * 6.0);
            pz = (float) ((double) pz - Math.cos(this.neck.yRot) * Math.cos(this.neck.xRot) * 6.0);
            this.neck.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        }

        this.head.x = px;
        this.head.y = py - 0.4F;
        this.head.z = pz;
        this.head.yRot = this.neck.yRot;
        this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);

        px = 0.0F;
        py = -3.0F;
        pz = 14.0F;

        for (int i = 0; i < 10; ++i) {
            float rotXVar = (float) Math.cos(((double) i / 10.0 + (double) (this.animF / 10.0F)) * Math.PI * 2.0) * 0.5F;
            float animOnTime = (float) Math.cos(((double) i / 10.0 + (double) (this.animF2 / 50.0F)) * Math.PI * 2.0);
            this.tail.xRot = 3.16F + rotXVar;
            this.tail.yRot = animOnTime;
            this.tail.zRot = 0.0F;
            this.tail.x = px;
            this.tail.y = py;
            this.tail.z = pz;
            px = (float) ((double) px - Math.sin(this.tail.yRot) * Math.cos(this.tail.xRot) * 4.0);
            py = (float) ((double) py + Math.sin(this.tail.xRot) * 4.0);
            pz = (float) ((double) pz - Math.cos(this.tail.yRot) * Math.cos(this.tail.xRot) * 4.0);
            this.tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        }

        px += 2.0F;
        pz -= 2.0F;

        for (int i = 0; i < 10; ++i) {
            float f10 = (float) Math.cos(((double) i / 10.0 + (double) (this.animF / 10.0F)) * Math.PI * 2.0);
            float animOnTime = (float) Math.cos(((double) i / 10.0 + (double) (this.animF2 / 40.0F)) * Math.PI * 2.0);
            this.tail2.xRot = 3.16F + f10;
            this.tail2.yRot = animOnTime;
            this.tail2.zRot = 0.0F;
            this.tail2.x = px;
            this.tail2.y = py;
            this.tail2.z = pz;
            px = (float) ((double) px - Math.sin(this.tail2.yRot) * Math.cos(this.tail.xRot) * 2.0);
            py = (float) ((double) py + Math.sin(this.tail2.xRot) * 2.0);
            pz = (float) ((double) pz - Math.cos(this.tail2.yRot) * Math.cos(this.tail.xRot) * 2.0);
            this.tail2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        }

        this.tailEnd.x = px;
        this.tailEnd.y = py;
        this.tailEnd.z = pz;
        this.tailEnd.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
