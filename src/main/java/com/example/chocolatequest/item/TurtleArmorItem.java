package com.example.chocolatequest.item;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.client.model.armor.CQTurtleArmorModel;
import com.example.chocolatequest.event.ModClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class TurtleArmorItem extends CQArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public TurtleArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private CQTurtleArmorModel<?> modelOuter;
            private CQTurtleArmorModel<?> modelInner;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.modelOuter == null) {
                    this.modelOuter = new CQTurtleArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModClientEvents.TURTLE_ARMOR_OUTER));
                    this.modelInner = new CQTurtleArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModClientEvents.TURTLE_ARMOR_INNER));
                }
                return equipmentSlot == EquipmentSlot.LEGS ? this.modelInner : this.modelOuter;
            }
        });
    }

    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot,
                                            ArmorMaterial.Layer layer, boolean innerModel) {
        return ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID,
                "textures/models/armor/turtle_layer_" + (innerModel ? "2" : "1") + ".png");
    }
}
