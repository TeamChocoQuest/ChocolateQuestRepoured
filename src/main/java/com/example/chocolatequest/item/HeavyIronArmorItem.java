package com.example.chocolatequest.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import com.example.chocolatequest.client.model.armor.CQHeavyArmorModel;
import net.minecraft.client.Minecraft;

public class HeavyIronArmorItem extends CQArmorItem {

    public HeavyIronArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void initializeClient(java.util.function.Consumer<net.neoforged.neoforge.client.extensions.common.IClientItemExtensions> consumer) {
        consumer.accept(new net.neoforged.neoforge.client.extensions.common.IClientItemExtensions() {
            private CQHeavyArmorModel<?> modelOuter;
            private CQHeavyArmorModel<?> modelInner;

            @Override
            public net.minecraft.client.model.HumanoidModel<?> getHumanoidArmorModel(net.minecraft.world.entity.LivingEntity livingEntity, net.minecraft.world.item.ItemStack itemStack, net.minecraft.world.entity.EquipmentSlot equipmentSlot, net.minecraft.client.model.HumanoidModel<?> original) {
                if (this.modelOuter == null) {
                    this.modelOuter = new CQHeavyArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(com.example.chocolatequest.event.ModClientEvents.HEAVY_ARMOR_OUTER));
                    this.modelInner = new CQHeavyArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(com.example.chocolatequest.event.ModClientEvents.HEAVY_ARMOR_INNER));
                }
                return equipmentSlot == net.minecraft.world.entity.EquipmentSlot.LEGS ? this.modelInner : this.modelOuter;
            }
        });
    }

    @Override
    public net.minecraft.resources.ResourceLocation getArmorTexture(net.minecraft.world.item.ItemStack stack, net.minecraft.world.entity.Entity entity, net.minecraft.world.entity.EquipmentSlot slot, net.minecraft.world.item.ArmorMaterial.Layer layer, boolean innerModel) {
        return net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.example.chocolatequest.ChocolateQuestReDone.MODID, "textures/models/armor/heavy_iron_layer_" + (innerModel ? "2" : "1") + ".png");
    }
}
