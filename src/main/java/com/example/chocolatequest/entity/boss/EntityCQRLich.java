package com.example.chocolatequest.entity.boss;

import com.example.chocolatequest.entity.ai.boss.BossAIMageShoot;
import com.example.chocolatequest.entity.ai.boss.BossAIMageSummon;
import com.example.chocolatequest.entity.ai.boss.MageLifeDrainGoal;
import com.example.chocolatequest.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.example.chocolatequest.registry.ModItems;

public class EntityCQRLich extends AbstractEntityCQRMageBase {
    
    private int phylacteryCheckTimer = 0;
    private boolean hasPhylacteryShield = false;

    public EntityCQRLich(EntityType<? extends EntityCQRLich> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MageLifeDrainGoal(this));
        this.goalSelector.addGoal(2, new BossAIMageShoot(this, 60, 24.0F, true)); // Shoot every 3 secs (Fireball + Wither Skull)
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    
    @Override
    protected net.minecraft.world.BossEvent.BossBarColor getBossBarColor() {
        return net.minecraft.world.BossEvent.BossBarColor.BLUE;
    }

    @Override
    public void tick() {
        super.tick();
        
        if (!this.level().isClientSide) {
            if (--this.phylacteryCheckTimer <= 0) {
                this.phylacteryCheckTimer = 20; // Check once a second
                this.hasPhylacteryShield = this.checkForPhylactery();
            }
            
            // Visual feedback when shielded
            if (this.hasPhylacteryShield && this.level().getGameTime() % 5 == 0) {
                // Could spawn particles here
                net.minecraft.server.level.ServerLevel serverLevel = (net.minecraft.server.level.ServerLevel) this.level();
                serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.ENCHANT, 
                    this.getX(), this.getY() + 1.0D, this.getZ(), 
                    5, 0.5D, 0.5D, 0.5D, 0.1D);
            }
        }
    }

    private boolean checkForPhylactery() {
        BlockPos center = this.blockPosition();
        int radius = 32;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = center.offset(x, y, z);
                    BlockState state = this.level().getBlockState(pos);
                    if (state.is(ModBlocks.PHYLACTERY.get())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.hasPhylacteryShield && source.getEntity() instanceof Player) {
            // Shield absorbs damage completely from players
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(serverLevel, damageSource, recentlyHit);
        this.spawnAtLocation(ModItems.DARK_STAFF.get());
        this.spawnAtLocation(ModItems.PHYLACTERY.get());
        this.spawnAtLocation(ModItems.CURSED_BONE.get());
        this.spawnAtLocation(new ItemStack(ModItems.SOUL_BOTTLE.get(), 2 + this.random.nextInt(3)));

        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(ModItems.CAPE_SPECTERLORD.get());
        }
        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(ModItems.MAGIC_BELL.get());
        }

        this.spawnAtLocation(new ItemStack(Items.ECHO_SHARD, 2 + this.random.nextInt(3)));

        if (this.random.nextFloat() < 0.35F) {
            this.spawnAtLocation(Items.NETHER_STAR);
        } else {
            this.spawnAtLocation(new ItemStack(Items.WITHER_SKELETON_SKULL, 1 + this.random.nextInt(2)));
        }

        this.spawnAtLocation(new ItemStack(Items.EMERALD, 6 + this.random.nextInt(7)));
        this.spawnAtLocation(new ItemStack(Items.DIAMOND, 3 + this.random.nextInt(4)));
    }
}
