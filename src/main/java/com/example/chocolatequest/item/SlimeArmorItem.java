package com.example.chocolatequest.item;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.client.SlimeArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class SlimeArmorItem extends CQArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public SlimeArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties.attributes(createCustomAttributes(type)));
    }

    private static ItemAttributeModifiers createCustomAttributes(Type type) {
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        EquipmentSlotGroup slotGroup = type.getSlot() == EquipmentSlot.HEAD ? EquipmentSlotGroup.HEAD :
                                       type.getSlot() == EquipmentSlot.CHEST ? EquipmentSlotGroup.CHEST :
                                       type.getSlot() == EquipmentSlot.LEGS ? EquipmentSlotGroup.LEGS : EquipmentSlotGroup.FEET;

        // Base Armor Stats
        int armorValue = type == Type.CHESTPLATE ? 6 : type == Type.LEGGINGS ? 5 : 2;
        builder.add(Attributes.ARMOR, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "slime_armor_" + type.getName()), armorValue, AttributeModifier.Operation.ADD_VALUE), slotGroup);
        
        // Custom Max Health
        double healthValue = 2.0; // 1 full heart per piece, total 4 hearts
        builder.add(Attributes.MAX_HEALTH, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "slime_health_" + type.getName()), healthValue, AttributeModifier.Operation.ADD_VALUE), slotGroup);

        return builder.build();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private SlimeArmorRenderer renderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null)
                    this.renderer = new SlimeArmorRenderer();

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
