package com.example.chocolatequest.client.model;

import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;

public abstract class AbstractModelHumanoidGeo<T extends AbstractEntityCQR> extends AbstractModelGeoCQRBase<T> {

    private static final ResourceLocation[] GENERIC_HUMANOID_ANIMATION = {
            ResourceLocation.fromNamespaceAndPath("cqrepoured", "animations/entity/_generic_humanoid.animation.json")
    };

    public AbstractModelHumanoidGeo(ResourceLocation modelLocation, String textureName, ResourceLocation animationLocation, int textureCount) {
        super(modelLocation, textureName, animationLocation, textureCount);
    }

    @Override
    public ResourceLocation[] getAnimationResourceFallbacks(T animatable) {
        return GENERIC_HUMANOID_ANIMATION;
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        GeoBone head = this.getAnimationProcessor().getBone("bipedHead");
        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * ((float) Math.PI / 180F));
            head.setRotY(entityData.netHeadYaw() * ((float) Math.PI / 180F));
        }

        GeoBone rightArm = this.getAnimationProcessor().getBone("bipedArmRight");
        GeoBone leftArm = this.getAnimationProcessor().getBone("bipedArmLeft");
        GeoBone rightLeg = this.getAnimationProcessor().getBone("bipedLegRight");
        GeoBone leftLeg = this.getAnimationProcessor().getBone("bipedLegLeft");

        float limbSwing = animationState.getLimbSwing();
        float limbSwingAmount = animationState.getLimbSwingAmount();

        if (!animatable.isSitting()) {
            if (rightLeg != null) {
                rightLeg.setRotX(rightLeg.getInitialSnapshot().getRotX() + net.minecraft.util.Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
            }
            if (leftLeg != null) {
                leftLeg.setRotX(leftLeg.getInitialSnapshot().getRotX() + net.minecraft.util.Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount);
            }
        }

        boolean isTwoHanded = (animatable.getMainHandItem().getItem() instanceof com.example.chocolatequest.item.BigswordItem) ||
                              (animatable.getOffhandItem().getItem() instanceof com.example.chocolatequest.item.BigswordItem) ||
                              (animatable.getMainHandItem().getItem() instanceof com.example.chocolatequest.item.SpearItem) ||
                              (animatable.getOffhandItem().getItem() instanceof com.example.chocolatequest.item.SpearItem) ||
                              animatable.isSpinningToWin();

        boolean isUsingItem = animatable.isUsingItem();
        boolean isSwinging = animatable.swinging;
        String itemName = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(animatable.getMainHandItem().getItem()).getPath();
        boolean hasRangedWeapon = animatable.getMainHandItem().getItem() instanceof net.minecraft.world.item.BowItem || 
                                  animatable.getMainHandItem().getItem() instanceof net.minecraft.world.item.CrossbowItem ||
                                  itemName.contains("staff");
        
        boolean isGun = itemName.contains("pistol") || itemName.contains("musket") || itemName.contains("revolver");
        boolean isCastingSpell = animatable instanceof com.example.chocolatequest.entity.boss.AbstractEntityCQRMageBase && animatable.getEntityData().get(com.example.chocolatequest.entity.boss.AbstractEntityCQRMageBase.IS_CASTING_SPELL);
        boolean isShieldActive = animatable instanceof com.example.chocolatequest.entity.boss.AbstractEntityCQRMageBase && animatable.getEntityData().get(com.example.chocolatequest.entity.boss.AbstractEntityCQRMageBase.IS_SHIELD_ACTIVE);

        if (!isTwoHanded && !isUsingItem && !hasRangedWeapon && !isGun && !isCastingSpell && !isShieldActive) {
            float attackTime = animatable.getAttackAnim(animationState.getPartialTick());
            
            float rightArmX = net.minecraft.util.Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
            float leftArmX = net.minecraft.util.Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
            
            float attackXRight = 0;
            float attackXLeft = 0;
            float attackYRight = 0;
            float attackYLeft = 0;

            if (attackTime > 0.0F) {
                float f = net.minecraft.util.Mth.sin(attackTime * (float)Math.PI);
                float f1 = 1.0F - attackTime;
                f1 = f1 * f1;
                f1 = f1 * f1;
                f1 = 1.0F - f1;
                float f2 = net.minecraft.util.Mth.sin(f1 * (float)Math.PI);
                
                float headPitch = (head != null) ? head.getRotX() : 0;
                
                if (animatable.swingingArm == net.minecraft.world.InteractionHand.MAIN_HAND && !animatable.isLeftHanded() || 
                    animatable.swingingArm == net.minecraft.world.InteractionHand.OFF_HAND && animatable.isLeftHanded()) {
                    attackXRight = (f2 * 1.2F + f * headPitch);
                    attackYRight = net.minecraft.util.Mth.sin(attackTime * (float)Math.PI) * -0.4F;
                } else {
                    attackXLeft = (f2 * 1.2F + f * headPitch);
                    attackYLeft = net.minecraft.util.Mth.sin(attackTime * (float)Math.PI) * 0.4F;
                }
            }

            if (rightArm != null) {
                rightArm.setRotX(rightArm.getInitialSnapshot().getRotX() + rightArmX + attackXRight);
                if (attackYRight != 0) rightArm.setRotY(rightArm.getInitialSnapshot().getRotY() + attackYRight);
            }
            if (leftArm != null) {
                leftArm.setRotX(leftArm.getInitialSnapshot().getRotX() + leftArmX + attackXLeft);
                if (attackYLeft != 0) leftArm.setRotY(leftArm.getInitialSnapshot().getRotY() + attackYLeft);
            }
        }

        // Shield blocking pose: raise the arm holding the shield
        if (animatable.isUsingItem() && animatable.getUseItem().getItem() instanceof net.minecraft.world.item.ShieldItem) {
            boolean offhandBlocking = animatable.getUsedItemHand() == net.minecraft.world.InteractionHand.OFF_HAND;
            GeoBone shieldArm = offhandBlocking ? 
                (animatable.isLeftHanded() ? rightArm : leftArm) : 
                (animatable.isLeftHanded() ? leftArm : rightArm);
            if (shieldArm != null) {
                // Raise arm up slightly (lower than face, closer to belt) and keep it perfectly straight
                shieldArm.setRotX(0.8F); // Lower than before
                shieldArm.setRotY(0.0F); // Perfectly straight (not angled inward)
                shieldArm.setRotZ(0.0F);
            }
        }
    }
}
