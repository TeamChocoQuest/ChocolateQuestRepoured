package com.example.chocolatequest.entity.boss;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.BossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.example.chocolatequest.registry.ModItems;
import com.example.chocolatequest.entity.bases.AbstractEntityCQR;

public class CQIceBullEntity extends CQBullEntity {

    public CQIceBullEntity(EntityType<? extends AbstractEntityCQR> type, Level level) {
        super(type, level);
    }

    @Override
    protected BossEvent.BossBarColor getBossBarColor() {
        return BossEvent.BossBarColor.BLUE;
    }

    @Override
    protected boolean canBreakBlocks() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.IS_FIRE)) {
            amount = amount <= 2.0F ? (amount + 3.0F) : (amount * 2.0F);
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean canUsePotion() {
        return false;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource damageSource, boolean recentlyHit) {
        // Do not call super to avoid warm bull drops
        this.spawnAtLocation(ModItems.CAPE_ICEBULL.get());
        this.spawnAtLocation(new ItemStack(ModItems.BULL_HORN.get(), 2));
        this.spawnAtLocation(new ItemStack(ModItems.BULL_LEATHER.get(), 3 + this.random.nextInt(4)));
        this.spawnAtLocation(new ItemStack(Items.BLUE_ICE, 2 + this.random.nextInt(3)));

        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(ModItems.ICE_STAFF.get());
        }

        this.spawnAtLocation(new ItemStack(ModItems.ICE_POTION.get(), 2 + this.random.nextInt(3)));
        this.spawnAtLocation(new ItemStack(ModItems.ICE_ARROW.get(), 8 + this.random.nextInt(9)));
        this.spawnAtLocation(new ItemStack(Items.PACKED_ICE, 4 + this.random.nextInt(5)));
        this.spawnAtLocation(new ItemStack(Items.DIAMOND, 2 + this.random.nextInt(3)));
    }
}
