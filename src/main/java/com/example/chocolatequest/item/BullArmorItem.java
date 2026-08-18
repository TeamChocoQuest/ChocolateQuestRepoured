package com.example.chocolatequest.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import com.example.chocolatequest.client.model.armor.CQBullArmorModel;
import net.minecraft.client.Minecraft;

public class BullArmorItem extends CQArmorItem {

    public BullArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void initializeClient(java.util.function.Consumer<net.neoforged.neoforge.client.extensions.common.IClientItemExtensions> consumer) {
        consumer.accept(new net.neoforged.neoforge.client.extensions.common.IClientItemExtensions() {
            private CQBullArmorModel<?> modelOuter;
            private CQBullArmorModel<?> modelInner;

            @Override
            public net.minecraft.client.model.HumanoidModel<?> getHumanoidArmorModel(net.minecraft.world.entity.LivingEntity livingEntity, net.minecraft.world.item.ItemStack itemStack, net.minecraft.world.entity.EquipmentSlot equipmentSlot, net.minecraft.client.model.HumanoidModel<?> original) {
                if (this.modelOuter == null) {
                    this.modelOuter = new CQBullArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(com.example.chocolatequest.event.ModClientEvents.BULL_ARMOR_OUTER));
                    this.modelInner = new CQBullArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(com.example.chocolatequest.event.ModClientEvents.BULL_ARMOR_INNER));
                }
                return equipmentSlot == net.minecraft.world.entity.EquipmentSlot.LEGS ? this.modelInner : this.modelOuter;
            }
        });
    }

    @Override
    public net.minecraft.resources.ResourceLocation getArmorTexture(net.minecraft.world.item.ItemStack stack, net.minecraft.world.entity.Entity entity, net.minecraft.world.entity.EquipmentSlot slot, net.minecraft.world.item.ArmorMaterial.Layer layer, boolean innerModel) {
        return net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.example.chocolatequest.ChocolateQuestReDone.MODID, "textures/models/armor/bull_layer_" + (innerModel ? "2" : "1") + ".png");
    }
}
