package com.example.chocolatequest.entity.boss;

import com.example.chocolatequest.entity.ai.boss.BossAIMageShoot;
import com.example.chocolatequest.entity.ai.boss.BossAIMageSummon;
import com.example.chocolatequest.entity.ai.boss.MageLifeDrainGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.example.chocolatequest.registry.ModItems;

public class EntityCQRNecromancer extends AbstractEntityCQRMageBase {

    public EntityCQRNecromancer(EntityType<? extends EntityCQRNecromancer> type, Level level) {
        super(type, level);
    }

    @Override
    protected void equipBasedOnTier(com.example.chocolatequest.entity.mob.enums.MobTier tier) {
        super.equipBasedOnTier(tier);
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.OFFHAND, new net.minecraft.world.item.ItemStack(com.example.chocolatequest.registry.ModItems.CURSED_BONE.get()));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MageLifeDrainGoal(this));
        this.goalSelector.addGoal(2, new BossAIMageSummon(this, 240)); // Summon every 12 secs
        this.goalSelector.addGoal(3, new BossAIMageShoot(this, 50, 24.0F, false)); // Shoot every 2.5 secs (Fireball only)
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(serverLevel, damageSource, recentlyHit);
        this.spawnAtLocation(ModItems.CURSED_BONE.get());
        this.spawnAtLocation(ModItems.DARK_STAFF.get());
        this.spawnAtLocation(new ItemStack(ModItems.SOUL_BOTTLE.get(), 1 + this.random.nextInt(2)));

        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(ModItems.CAPE_SPECTERLORD.get());
        }

        this.spawnAtLocation(new ItemStack(Items.ECHO_SHARD, 1 + this.random.nextInt(2)));
        this.spawnAtLocation(new ItemStack(Items.BONE, 8 + this.random.nextInt(9)));
        this.spawnAtLocation(new ItemStack(Items.ENDER_PEARL, 4 + this.random.nextInt(5)));
        this.spawnAtLocation(new ItemStack(Items.WITHER_SKELETON_SKULL, 1 + this.random.nextInt(2)));
    }
}
