package com.example.chocolatequest.item.staff;

import com.example.chocolatequest.entity.projectile.ProjectileCannonBall;
import com.example.chocolatequest.registry.ModItems;
import com.example.chocolatequest.registry.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GunStaffItem extends Item {
    private static final int COOLDOWN = 20;

    public GunStaffItem(Properties properties) {
        super(properties.durability(2048));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(this)) return InteractionResultHolder.fail(stack);

        if (!level.isClientSide) {
            ProjectileCannonBall cannonBall = new ProjectileCannonBall(player, level, false);
            cannonBall.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.5F, 0.0F);
            level.addFreshEntity(cannonBall);
            ModItems.applySharedCooldown(player, this, COOLDOWN);
            if (!player.getAbilities().instabuild) {
                stack.hurtAndBreak(1, player,
                        hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }

            if (level instanceof ServerLevel serverLevel) {
                Vec3 muzzle = player.getEyePosition().add(player.getLookAngle().scale(1.15D));
                serverLevel.sendParticles(ParticleTypes.FLAME, muzzle.x, muzzle.y, muzzle.z,
                        4, 0.06D, 0.06D, 0.06D, 0.02D);
                serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, muzzle.x, muzzle.y, muzzle.z,
                        9, 0.12D, 0.12D, 0.12D, 0.04D);
            }
        }

        level.playSound(null, player.getX(), player.getEyeY(), player.getZ(), ModSounds.MUSKET_SHOOT.get(),
                SoundSource.PLAYERS, 1.0F, 0.9F + level.random.nextFloat() * 0.2F);
        player.swing(hand, true);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.cqrepoured.gun_staff.tooltip").withStyle(ChatFormatting.BLUE));
    }
}
