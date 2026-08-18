package com.example.chocolatequest.entity.npc;

import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import com.example.chocolatequest.entity.mob.enums.MobTier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import java.util.UUID;
import java.util.EnumSet;

public class EntityCQRPrisonerKnight extends AbstractEntityCQR {
    
    private UUID ownerUUID = null;
    private boolean isFreed = false;

    public EntityCQRPrisonerKnight(EntityType<? extends AbstractEntityCQR> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractEntityCQR.createCQRAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    @Override
    public double getBaseHealth() {
        return 20.0D;
    }

    @Override
    public com.example.chocolatequest.faction.EDefaultFaction getDefaultFaction() {
        return com.example.chocolatequest.faction.EDefaultFaction.NPC;
    }

    @Override
    public int getTextureCount() {
        return 1;
    }

    @Override
    protected void equipBasedOnTier(MobTier tier) {
        // They spawn with basic weapons
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        
        // Custom Follow Goal
        this.goalSelector.addGoal(2, new FollowFreerGoal(this, 1.2D, 5.0F, 20.0F));
        
        // Attack Goal
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));

        // Target Goals (Only target monsters if freed, or whoever hurts the owner)
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new DefendFreerGoal(this));
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.isFreed && !this.level().isClientSide) {
            // Free the knight
            this.isFreed = true;
            this.ownerUUID = player.getUUID();
            
            // Drop a reward
            this.spawnAtLocation(new ItemStack(Items.DIAMOND, 1));
            this.spawnAtLocation(new ItemStack(Items.GOLDEN_APPLE, 1));
            
            player.displayClientMessage(net.minecraft.network.chat.Component.literal("Dziękuję za uwolnienie! Będę z tobą walczył."), true);
            
            // Play a sound or effect here if desired
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }
    
    public Player getOwner() {
        if (this.ownerUUID != null) {
            return this.level().getPlayerByUUID(this.ownerUUID);
        }
        return null;
    }

    public boolean isFreed() {
        return this.isFreed;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsFreed", this.isFreed);
        if (this.ownerUUID != null) {
            tag.putUUID("OwnerUUID", this.ownerUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.isFreed = tag.getBoolean("IsFreed");
        if (tag.hasUUID("OwnerUUID")) {
            this.ownerUUID = tag.getUUID("OwnerUUID");
        }
    }

    // --- CUSTOM GOALS ---

    class FollowFreerGoal extends Goal {
        private final EntityCQRPrisonerKnight knight;
        private Player owner;
        private final double speedModifier;
        private final float stopDistance;
        private final float startDistance;

        public FollowFreerGoal(EntityCQRPrisonerKnight knight, double speedModifier, float stopDistance, float startDistance) {
            this.knight = knight;
            this.speedModifier = speedModifier;
            this.stopDistance = stopDistance;
            this.startDistance = startDistance;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (!this.knight.isFreed()) return false;
            Player p = this.knight.getOwner();
            if (p == null) return false;
            if (p.isSpectator()) return false;
            if (this.knight.distanceToSqr(p) < (double)(this.startDistance * this.startDistance)) return false;
            this.owner = p;
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            if (this.knight.getNavigation().isDone()) return false;
            if (this.knight.distanceToSqr(this.owner) <= (double)(this.stopDistance * this.stopDistance)) return false;
            return true;
        }

        @Override
        public void start() {
            this.knight.getNavigation().moveTo(this.owner, this.speedModifier);
        }

        @Override
        public void stop() {
            this.owner = null;
            this.knight.getNavigation().stop();
        }

        @Override
        public void tick() {
            this.knight.getLookControl().setLookAt(this.owner, 10.0F, (float)this.knight.getMaxHeadXRot());
            if (--this.knight.tickCount % 10 == 0) {
                this.knight.getNavigation().moveTo(this.owner, this.speedModifier);
            }
        }
    }

    class DefendFreerGoal extends net.minecraft.world.entity.ai.goal.target.TargetGoal {
        private final EntityCQRPrisonerKnight knight;
        private net.minecraft.world.entity.LivingEntity attacker;
        private int timestamp;

        public DefendFreerGoal(EntityCQRPrisonerKnight knight) {
            super(knight, false);
            this.knight = knight;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            if (!this.knight.isFreed()) return false;
            Player owner = this.knight.getOwner();
            if (owner == null) return false;
            this.attacker = owner.getLastHurtByMob();
            int i = owner.getLastHurtByMobTimestamp();
            return i != this.timestamp && this.canAttack(this.attacker, net.minecraft.world.entity.ai.targeting.TargetingConditions.DEFAULT);
        }

        @Override
        public void start() {
            this.mob.setTarget(this.attacker);
            Player owner = this.knight.getOwner();
            if (owner != null) {
                this.timestamp = owner.getLastHurtByMobTimestamp();
            }
            super.start();
        }
    }
}
