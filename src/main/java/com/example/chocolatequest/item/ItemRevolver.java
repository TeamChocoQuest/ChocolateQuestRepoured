package com.example.chocolatequest.item;

import com.example.chocolatequest.entity.projectile.ProjectileBulletEntity;
import com.example.chocolatequest.registry.ModDataComponents;
import com.example.chocolatequest.registry.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ItemRevolver extends Item implements IRangedWeapon {

    private final int capacity;
    private final int reloadDuration;

    public ItemRevolver(Properties properties) {
        this(properties, 300, 6, 50);
    }

    protected ItemRevolver(Properties properties, int durability, int capacity, int reloadDuration) {
        super(properties.durability(durability));
        this.capacity = capacity;
        this.reloadDuration = reloadDuration;
    }

    @Override
    public int getUseDuration(ItemStack stack, net.minecraft.world.entity.LivingEntity entity) {
        return 72000;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.cqrepoured.tooltip.bullet_damage", 5.0).withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.translatable("item.cqrepoured.tooltip.fire_rate", -30).withStyle(ChatFormatting.RED));
        tooltipComponents.add(Component.translatable("item.cqrepoured.tooltip.accuracy", -50).withStyle(ChatFormatting.RED));
        tooltipComponents.add(Component.translatable("item.cqrepoured.tooltip.firearm_ammo", this.getLoadedAmmo(stack), this.getCapacity()).withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("item.cqrepoured.tooltip.firearm_reload_hint").withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.isReloading(stack, level)) {
            return InteractionResultHolder.fail(stack);
        }

        if (this.getLoadedAmmo(stack) <= 0) {
            if (level.isClientSide) {
                player.displayClientMessage(Component.translatable("message.cqrepoured.firearm_empty"), true);
            }
            return InteractionResultHolder.fail(stack);
        }

        this.shootPlayer(stack, level, player, hand);
        player.swing(hand, true);
        return InteractionResultHolder.consume(stack);
    }

    public void shootPlayer(ItemStack stack, Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && this.getLoadedAmmo(stack) > 0) {
            EBulletType type = this.getLoadedAmmoType(stack);
            ProjectileBulletEntity bulletEntity = new ProjectileBulletEntity(level, player, type);
            bulletEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.5F, this.getInaccuracy());
            com.example.chocolatequest.registry.ModItems.applySharedCooldown(player, this, this.getShootCooldown());
            level.addFreshEntity(bulletEntity);
            this.setLoadedAmmo(stack, this.getLoadedAmmo(stack) - 1);
            stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);

            if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                Vec3 muzzle = player.getEyePosition().add(player.getLookAngle().scale(1.15D));
                serverLevel.sendParticles(ParticleTypes.FLAME, muzzle.x, muzzle.y, muzzle.z, 2, 0.035D, 0.035D, 0.035D, 0.015D);
                serverLevel.sendParticles(ParticleTypes.SMOKE, muzzle.x, muzzle.y, muzzle.z, 7, 0.065D, 0.065D, 0.065D, 0.025D);
            }
        }

        level.playSound(null, player.getX(), player.getY() + player.getEyeHeight(), player.getZ(), this.getShootSound(), SoundSource.PLAYERS, 1.0F, 0.9F + level.random.nextFloat() * 0.2F);
        player.setXRot(player.getXRot() - level.random.nextFloat() * this.getRecoil());
    }

    public boolean startReload(Player player, ItemStack stack) {
        Level level = player.level();
        if (level.isClientSide || this.isReloading(stack, level) || this.getLoadedAmmo(stack) >= this.getCapacity()) {
            return false;
        }

        EBulletType type = this.getLoadedAmmo(stack) > 0 ? this.getLoadedAmmoType(stack) : this.findFirstAmmoType(player);
        if (!player.getAbilities().instabuild && this.findAmmo(player, type).isEmpty()) {
            return false;
        }

        stack.set(ModDataComponents.FIREARM_AMMO_TYPE.get(), type.ordinal());
        stack.set(ModDataComponents.FIREARM_RELOAD_END_TICK.get(), (int) level.getGameTime() + this.getReloadDuration());
        player.getCooldowns().addCooldown(this, this.getReloadDuration());
        level.playSound(null, player.blockPosition(), this.getReloadStartSound(), SoundSource.PLAYERS, 0.8F, 0.95F + level.random.nextFloat() * 0.1F);
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        int reloadEnd = stack.getOrDefault(ModDataComponents.FIREARM_RELOAD_END_TICK.get(), 0);
        if (reloadEnd == 0 || level.isClientSide) {
            return;
        }

        if (!(entity instanceof Player player) || (!isSelected && player.getOffhandItem() != stack)) {
            stack.set(ModDataComponents.FIREARM_RELOAD_END_TICK.get(), 0);
            return;
        }

        if ((int) level.getGameTime() >= reloadEnd) {
            this.finishReload(player, stack);
        }
    }

    private void finishReload(Player player, ItemStack stack) {
        int missing = this.getCapacity() - this.getLoadedAmmo(stack);
        if (missing <= 0) {
            stack.set(ModDataComponents.FIREARM_RELOAD_END_TICK.get(), 0);
            return;
        }

        EBulletType type = this.getLoadedAmmoType(stack);
        int loaded = player.getAbilities().instabuild ? missing : this.consumeAmmo(player, type, missing);
        if (loaded > 0) {
            this.setLoadedAmmo(stack, this.getLoadedAmmo(stack) + loaded);
            player.level().playSound(null, player.blockPosition(), this.getReloadEndSound(), SoundSource.PLAYERS, 0.9F, 0.95F + player.level().random.nextFloat() * 0.1F);
        }
        stack.set(ModDataComponents.FIREARM_RELOAD_END_TICK.get(), 0);
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getReloadDuration() {
        return this.reloadDuration;
    }

    public int getLoadedAmmo(ItemStack stack) {
        return Math.max(0, Math.min(this.getCapacity(), stack.getOrDefault(ModDataComponents.MAGAZINE_AMMO.get(), 0)));
    }

    public void setLoadedAmmo(ItemStack stack, int amount) {
        stack.set(ModDataComponents.MAGAZINE_AMMO.get(), Math.max(0, Math.min(this.getCapacity(), amount)));
    }

    public EBulletType getLoadedAmmoType(ItemStack stack) {
        int type = stack.getOrDefault(ModDataComponents.FIREARM_AMMO_TYPE.get(), EBulletType.IRON.ordinal());
        EBulletType[] values = EBulletType.values();
        return type >= 0 && type < values.length ? values[type] : EBulletType.IRON;
    }

    public boolean isReloading(ItemStack stack, Level level) {
        return stack.getOrDefault(ModDataComponents.FIREARM_RELOAD_END_TICK.get(), 0) > (int) level.getGameTime();
    }

    public float getReloadProgress(ItemStack stack, Level level, float partialTick) {
        int end = stack.getOrDefault(ModDataComponents.FIREARM_RELOAD_END_TICK.get(), 0);
        if (end <= 0) {
            return 0.0F;
        }
        float remaining = end - ((float) level.getGameTime() + partialTick);
        return net.minecraft.util.Mth.clamp(1.0F - remaining / (float) this.getReloadDuration(), 0.0F, 1.0F);
    }

    protected SoundEvent getReloadStartSound() {
        return SoundEvents.IRON_TRAPDOOR_OPEN;
    }

    protected SoundEvent getReloadEndSound() {
        return SoundEvents.IRON_TRAPDOOR_CLOSE;
    }

    protected float getRecoil() {
        return 3.0F;
    }

    protected float getInaccuracy() {
        return 5.0F;
    }

    protected int getShootCooldown() {
        return 10;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }

    protected boolean isBullet(ItemStack stack) {
        return stack.getItem() instanceof ItemBullet;
    }

    protected ItemStack findAmmo(Player player) {
        if (this.isBullet(player.getItemInHand(InteractionHand.OFF_HAND))) {
            return player.getItemInHand(InteractionHand.OFF_HAND);
        } else if (this.isBullet(player.getItemInHand(InteractionHand.MAIN_HAND))) {
            return player.getItemInHand(InteractionHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                ItemStack itemstack = player.getInventory().getItem(i);
                if (this.isBullet(itemstack)) {
                    return itemstack;
                }
            }
            return ItemStack.EMPTY;
        }
    }

    protected ItemStack findAmmo(Player player, EBulletType type) {
        for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof ItemBullet bullet && bullet.getType() == type) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStack findReloadAmmo(Player player, ItemStack firearmStack) {
        EBulletType type = this.getLoadedAmmo(firearmStack) > 0 ? this.getLoadedAmmoType(firearmStack) : this.findFirstAmmoType(player);
        return this.findAmmo(player, type);
    }

    private EBulletType findFirstAmmoType(Player player) {
        ItemStack ammo = this.findAmmo(player);
        return ammo.getItem() instanceof ItemBullet bullet ? bullet.getType() : EBulletType.IRON;
    }

    private int consumeAmmo(Player player, EBulletType type, int amount) {
        int consumed = 0;
        for (int i = 0; i < player.getInventory().getContainerSize() && consumed < amount; ++i) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof ItemBullet bullet && bullet.getType() == type) {
                int take = Math.min(stack.getCount(), amount - consumed);
                stack.shrink(take);
                consumed += take;
            }
        }
        return consumed;
    }

    @Override
    public void shoot(Level world, LivingEntity shooter, Entity target, InteractionHand hand) {
        if (!world.isClientSide) {
            EBulletType type = EBulletType.IRON;
            if (shooter instanceof net.minecraft.world.entity.Mob mob) {
                ItemStack ammoStack = ItemStack.EMPTY;
                if (this.isBullet(mob.getItemInHand(InteractionHand.OFF_HAND))) {
                    ammoStack = mob.getItemInHand(InteractionHand.OFF_HAND);
                } else if (this.isBullet(mob.getItemInHand(InteractionHand.MAIN_HAND))) {
                    ammoStack = mob.getItemInHand(InteractionHand.MAIN_HAND);
                }
                if (!ammoStack.isEmpty() && ammoStack.getItem() instanceof ItemBullet bullet) {
                    type = bullet.getType();
                }
            }
            if (shooter instanceof com.example.chocolatequest.entity.mob.CQPirateEntity pirate && pirate.getMobTier() == com.example.chocolatequest.entity.mob.enums.MobTier.BOSS) {
                type = EBulletType.FIRE;
            }
            ProjectileBulletEntity bulletEntity = new ProjectileBulletEntity(world, shooter, type);
            Vec3 v = target.position().subtract(shooter.position()).normalize().scale(3.5D);
            bulletEntity.setDeltaMovement(v);
            world.addFreshEntity(bulletEntity);
            world.playSound(null, shooter.getX(), shooter.getY() + shooter.getEyeHeight(), shooter.getZ(), this.getShootSound(), SoundSource.HOSTILE, 1.0F, 0.9F + world.random.nextFloat() * 0.2F);
        }
    }

    @Override
    public SoundEvent getShootSound() {
        return ModSounds.REVOLVER_SHOOT.get();
    }

    @Override
    public double getRange() {
        return 32.0D;
    }

    @Override
    public int getCooldown() {
        return 60;
    }

    @Override
    public int getChargeTicks() {
        return 0;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }
}
