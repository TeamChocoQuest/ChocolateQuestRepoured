package com.example.chocolatequest.client.render;

import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;

public class RenderCQRBipedBaseGeo<T extends AbstractEntityCQR> extends GeoEntityRenderer<T> {

    private static final net.minecraft.world.item.ItemStack HEALING_POTION_FOR_DISPLAY =
            new net.minecraft.world.item.ItemStack(com.example.chocolatequest.registry.ModItems.MINI_HEALING_POTION.get());

    public RenderCQRBipedBaseGeo(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
        this.addRenderLayer(new BlockAndItemGeoLayer<>(this, (bone, animatable) -> {
            if (bone.getName().equals("bipedHandRight")) {
                if (!animatable.isLeftHanded() && animatable.isDrinkingPotion()) {
                    return HEALING_POTION_FOR_DISPLAY;
                }
                return nonEmpty(animatable.getItemBySlot(animatable.isLeftHanded() ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND));
            } else if (bone.getName().equals("bipedHandLeft")) {
                if (animatable.isLeftHanded() && animatable.isDrinkingPotion()) {
                    return HEALING_POTION_FOR_DISPLAY;
                }
                return nonEmpty(animatable.getItemBySlot(animatable.isLeftHanded() ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
            } else if (bone.getName().equals("bipedBody")) {
                if (!animatable.canUsePotion()) {
                    return null;
                }
                if (animatable.isDrinkingPotion() || animatable.hasUsedPotion()) {
                    return null;
                }
                return HEALING_POTION_FOR_DISPLAY;
            } else if (bone.getName().equals("bipedHead")) {
                net.minecraft.world.item.ItemStack badge = animatable.extraInventory.getItem(1);
                return nonEmpty(badge);
            }
            return null;
        }, (bone, animatable) -> null) {
            @Override
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, net.minecraft.world.item.ItemStack stack, T animatable) {
                if (bone.getName().equals("bipedHandRight")) {
                    return ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                }
                if (bone.getName().equals("bipedHandLeft")) {
                    return ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                }
                if (bone.getName().equals("bipedBody")) {
                    return ItemDisplayContext.FIXED;
                }
                if (bone.getName().equals("bipedHead")) {
                    return ItemDisplayContext.HEAD;
                }
                return ItemDisplayContext.NONE;
            }

            @Override
            protected void renderStackForBone(com.mojang.blaze3d.vertex.PoseStack poseStack, GeoBone bone, net.minecraft.world.item.ItemStack stack, T animatable, net.minecraft.client.renderer.MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                poseStack.pushPose();
                if (bone.getName().equals("bipedHandRight") || bone.getName().equals("bipedHandLeft")) {
                    if (stack.getItem() instanceof net.minecraft.world.item.ShieldItem) {
                        if (animatable.isUsingItem() && animatable.getUseItem() == stack) {
                            poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0f));
                            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(10.0f));
                            poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(10.0f));
                            poseStack.translate(-0.15, 0.15, 0.0);
                        } else {
                            // Base transform for resting
                            poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0f));
                            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(180.0f));
                            poseStack.translate(0.0, 0.0, -0.3);
                        }
                    } else {
                        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0f));
                    }
                } else if (bone.getName().equals("bipedBody")) {
                    poseStack.translate(-0.25, -0.2, -0.15); // X: mob's left hip, Y: waist level (up from -0.3), Z: slightly forward
                    poseStack.scale(0.35f, 0.35f, 0.35f);
                } else if (bone.getName().equals("bipedHead")) {
                    // Shift feather up above the helmet and to the side slightly, rotate it
                    poseStack.translate(0.0, -0.65, -0.25);
                    poseStack.scale(0.7f, 0.7f, 0.7f);
                }
                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
                poseStack.popPose();
            }
        });
        this.addRenderLayer(new software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer<>(this) {
            @Nullable
            @Override
            protected net.minecraft.world.item.ItemStack getArmorItemForBone(GeoBone bone, T animatable) {
                net.minecraft.world.item.ItemStack stack = switch (bone.getName()) {
                    case "armorBipedHead" -> animatable.getItemBySlot(EquipmentSlot.HEAD);
                    case "armorBipedBody", "armorBipedRightArm", "armorBipedLeftArm" -> animatable.getItemBySlot(EquipmentSlot.CHEST);
                    case "armorBipedRightLeg", "armorBipedLeftLeg" -> animatable.getItemBySlot(EquipmentSlot.LEGS);
                    case "armorBipedRightFoot", "armorBipedLeftFoot" -> animatable.getItemBySlot(EquipmentSlot.FEET);
                    default -> null;
                };
                return nonEmpty(stack);
            }

            @Override
            protected EquipmentSlot getEquipmentSlotForBone(GeoBone bone, net.minecraft.world.item.ItemStack stack, T animatable) {
                return switch (bone.getName()) {
                    case "armorBipedHead" -> EquipmentSlot.HEAD;
                    case "armorBipedBody", "armorBipedRightArm", "armorBipedLeftArm" -> EquipmentSlot.CHEST;
                    case "armorBipedRightLeg", "armorBipedLeftLeg" -> EquipmentSlot.LEGS;
                    case "armorBipedRightFoot", "armorBipedLeftFoot" -> EquipmentSlot.FEET;
                    default -> super.getEquipmentSlotForBone(bone, stack, animatable);
                };
            }

            @Override
            protected net.minecraft.client.model.geom.ModelPart getModelPartForBone(GeoBone bone, EquipmentSlot slot, net.minecraft.world.item.ItemStack stack, T animatable, net.minecraft.client.model.HumanoidModel<?> baseModel) {
                return switch (bone.getName()) {
                    case "armorBipedHead" -> baseModel.head;
                    case "armorBipedBody" -> baseModel.body;
                    case "armorBipedRightArm" -> baseModel.rightArm;
                    case "armorBipedLeftArm" -> baseModel.leftArm;
                    case "armorBipedRightLeg", "armorBipedRightFoot" -> baseModel.rightLeg;
                    case "armorBipedLeftLeg", "armorBipedLeftFoot" -> baseModel.leftLeg;
                    default -> super.getModelPartForBone(bone, slot, stack, animatable, baseModel);
                };
            }

            @Override
            protected void prepModelPartForRender(com.mojang.blaze3d.vertex.PoseStack poseStack, GeoBone bone, net.minecraft.client.model.geom.ModelPart sourcePart) {
                float baseSizeX = 1.0F;
                float baseSizeY = 1.0F;
                float baseSizeZ = 1.0F;
                float scaleX = 1.0F;
                float scaleY = 1.0F;
                float scaleZ = 1.0F;
                  
                String name = bone.getName();
                if (!bone.getCubes().isEmpty()) {
                    software.bernie.geckolib.cache.object.GeoCube firstCube = bone.getCubes().get(0);
                    
                    if (name.equals("armorBipedHead")) {
                        baseSizeX = 8.0F; baseSizeY = 8.0F; baseSizeZ = 8.0F;
                    } else if (name.equals("armorBipedRightArm") || name.equals("armorBipedLeftArm")) {
                        baseSizeX = 4.0F; baseSizeY = 12.0F; baseSizeZ = 4.0F;
                    } else if (name.equals("armorBipedRightLeg") || name.equals("armorBipedLeftLeg") || name.equals("armorBipedRightFoot") || name.equals("armorBipedLeftFoot")) {
                        baseSizeX = 4.0F; baseSizeY = 12.0F; baseSizeZ = 4.0F;
                    } else if (name.equals("armorBipedBody")) {
                        baseSizeX = 8.0F; baseSizeY = 12.0F; baseSizeZ = 4.0F;
                    }
                    
                    if (baseSizeX > 0) scaleX = (float)(firstCube.size().x() / baseSizeX);
                    if (baseSizeY > 0) scaleY = (float)(firstCube.size().y() / baseSizeY);
                    if (baseSizeZ > 0) scaleZ = (float)(firstCube.size().z() / baseSizeZ);
                }
                
                float pivotX = bone.getPivotX();
                float pivotY = bone.getPivotY();
                float pivotZ = bone.getPivotZ();
                
                if (name.equals("armorBipedBody") && !bone.getCubes().isEmpty()) {
                    software.bernie.geckolib.cache.object.GeoCube firstCube = bone.getCubes().get(0);
                    if (pivotY < 12.0F) {
                        pivotY = pivotY + (float)firstCube.size().y() * 0.875F;
                    }
                }
                
                sourcePart.setPos(-(pivotX / scaleX), -(pivotY / scaleY), (pivotZ / scaleZ));
                
                sourcePart.xRot = -bone.getRotX();
                sourcePart.yRot = -bone.getRotY();
                sourcePart.zRot = bone.getRotZ();
                  
                poseStack.scale(scaleX, scaleY, scaleZ);

                T animatable = this.getRenderer().getAnimatable();
                if (animatable instanceof com.example.chocolatequest.entity.mob.CQGremlinEntity) {
                    if (name.equals("armorBipedRightArm") || name.equals("armorBipedLeftArm")) {
                        poseStack.translate(0.0F, -2.0F / 16.0F, 0.0F); 
                    }
                }
            }
        });
    }

    @Nullable
    private static net.minecraft.world.item.ItemStack nonEmpty(@Nullable net.minecraft.world.item.ItemStack stack) {
        return stack == null || stack.isEmpty() ? null : stack;
    }

    @Override
    public void renderCubesOfBone(PoseStack poseStack, GeoBone bone, VertexConsumer buffer,
                                  int packedLight, int packedOverlay, int colour) {
        // The armorBiped* cubes are transform anchors for ItemArmorGeoLayer. Rendering them
        // in the base pass duplicates the mob's body geometry (eight extra cubes per biped).
        if (bone.getName().startsWith("armorBiped")) {
            return;
        }

        super.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, colour);
    }

    
    protected boolean shouldRenderBuiltInCape(T entity) {
        return false;
    }

    @Override
    public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone,
                                  RenderType renderType, MultiBufferSource bufferSource,
                                  VertexConsumer buffer, boolean isReRender, float partialTick,
                                  int packedLight, int packedOverlay, int colour) {
        if (bone.getName().equals("bipedCape") && !shouldRenderBuiltInCape(animatable)) {
            return;
        }
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource,
                buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}

