package com.example.chocolatequest.item;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.client.model.armor.CQHeavyArmorModel;
import com.example.chocolatequest.event.ModClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class HeavyDiamondArmorItem extends CQArmorItem {

    public HeavyDiamondArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private CQHeavyArmorModel<?> modelOuter;
            private CQHeavyArmorModel<?> modelInner;

            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack,
                                                           EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.modelOuter == null) {
                    this.modelOuter = new CQHeavyArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModClientEvents.HEAVY_ARMOR_OUTER));
                    this.modelInner = new CQHeavyArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModClientEvents.HEAVY_ARMOR_INNER));
                }
                return equipmentSlot == EquipmentSlot.LEGS ? this.modelInner : this.modelOuter;
            }
        });
    }

    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot,
                                            ArmorMaterial.Layer layer, boolean innerModel) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID,
                "textures/models/armor/heavy_diamond_layer_" + (innerModel ? "2" : "1") + ".png");
    }
}
