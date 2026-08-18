package com.example.chocolatequest.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SpiderArmorItem extends CQArmorItem {
    public SpiderArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void initializeClient(java.util.function.Consumer<net.neoforged.neoforge.client.extensions.common.IClientItemExtensions> consumer) {
        consumer.accept(new net.neoforged.neoforge.client.extensions.common.IClientItemExtensions() {
            private com.example.chocolatequest.client.model.armor.SpiderArmorModel<?> modelOuter;
            private com.example.chocolatequest.client.model.armor.SpiderArmorModel<?> modelInner;

            @Override
            public net.minecraft.client.model.HumanoidModel<?> getHumanoidArmorModel(net.minecraft.world.entity.LivingEntity entityLiving, net.minecraft.world.item.ItemStack itemStack, net.minecraft.world.entity.EquipmentSlot armorSlot, net.minecraft.client.model.HumanoidModel<?> _default) {
                boolean isLegs = armorSlot == net.minecraft.world.entity.EquipmentSlot.LEGS;

                if (isLegs) {
                    if (this.modelInner == null) {
                        this.modelInner = new com.example.chocolatequest.client.model.armor.SpiderArmorModel<>(net.minecraft.client.Minecraft.getInstance().getEntityModels().bakeLayer(com.example.chocolatequest.event.ModClientEvents.SPIDER_ARMOR_INNER));
                    }
                    var model = this.modelInner;
                    // Hide 3D spider parts for leggings pass to prevent double rendering
                    model.body.getChild("bipedBody_1").visible = false;
                    model.body.getChild("arm1part1").visible = false;
                    model.body.getChild("arm2part1").visible = false;
                    model.body.getChild("arm3part1").visible = false;
                    model.body.getChild("arm4part1").visible = false;
                    model.body.getChild("arm5part1").visible = false;
                    model.body.getChild("arm6part1").visible = false;
                    return model;
                } else {
                    if (this.modelOuter == null) {
                        this.modelOuter = new com.example.chocolatequest.client.model.armor.SpiderArmorModel<>(net.minecraft.client.Minecraft.getInstance().getEntityModels().bakeLayer(com.example.chocolatequest.event.ModClientEvents.SPIDER_ARMOR_OUTER));
                    }
                    var model = this.modelOuter;
                    model.body.getChild("bipedBody_1").visible = true;
                    model.body.getChild("arm1part1").visible = true;
                    model.body.getChild("arm2part1").visible = true;
                    model.body.getChild("arm3part1").visible = true;
                    model.body.getChild("arm4part1").visible = true;
                    model.body.getChild("arm5part1").visible = true;
                    model.body.getChild("arm6part1").visible = true;
                    return model;
                }
            }
        });
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide() && entity instanceof Player player) {
            if (player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof SpiderArmorItem &&
                player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof SpiderArmorItem &&
                player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof SpiderArmorItem &&
                player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof SpiderArmorItem) {
                
                player.fallDistance = 0.0F;
                player.addEffect(new MobEffectInstance(MobEffects.JUMP, 20, 1, false, false, true));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 1, false, false, true));
            }
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
}
