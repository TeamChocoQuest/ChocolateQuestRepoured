package com.example.chocolatequest.item;

import com.example.chocolatequest.registry.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ItemMusket extends ItemRevolver {

    public ItemMusket(Properties properties) {
        super(properties, 250, 1, 90);
    }

    @Override
    public int getUseDuration(ItemStack stack, net.minecraft.world.entity.LivingEntity entity) {
        return 72000;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.cqrepoured.tooltip.bullet_damage", 7.5).withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.translatable("item.cqrepoured.tooltip.fire_rate", -60).withStyle(ChatFormatting.RED));
        tooltipComponents.add(Component.translatable("item.cqrepoured.tooltip.accuracy", "-10%").withStyle(ChatFormatting.RED));
        tooltipComponents.add(Component.translatable("item.cqrepoured.tooltip.firearm_ammo", this.getLoadedAmmo(stack), this.getCapacity()).withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("item.cqrepoured.tooltip.firearm_reload_hint").withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    protected float getRecoil() {
        return 1.5F;
    }

    @Override
    protected float getInaccuracy() {
        return 2.0F; // better accuracy than revolver
    }

    @Override
    protected int getShootCooldown() {
        return 30; // longer cooldown
    }

    @Override
    public SoundEvent getShootSound() {
        return ModSounds.MUSKET_SHOOT.get();
    }

    @Override
    protected SoundEvent getReloadStartSound() {
        return SoundEvents.CROSSBOW_LOADING_START.value();
    }

    @Override
    protected SoundEvent getReloadEndSound() {
        return SoundEvents.CROSSBOW_LOADING_END.value();
    }
}
