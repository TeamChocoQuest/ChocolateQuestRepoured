package com.example.chocolatequest.entity.boss.exterminator;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;

import net.neoforged.neoforge.entity.PartEntity;
import com.example.chocolatequest.registry.ModSounds;
import net.minecraft.util.Mth;

public class SubEntityExterminatorFieldEmitter extends net.neoforged.neoforge.entity.PartEntity<EntityCQRExterminator> {

	private EntityCQRExterminator exterminator;

	private final Supplier<LivingEntity> funcGetElectrocuteTarget;
	private final Supplier<Boolean> funcGetIsActive;
	private final Consumer<Boolean> funcSetIsActiveInParent;

	private int remainingActiveTime;
	private int activeTimeNoTarget;
	private int cooldown;

    public SubEntityExterminatorFieldEmitter(net.minecraft.world.entity.EntityType<?> type, net.minecraft.world.level.Level level) {
        super(null); // Dummy constructor for registry
		this.funcGetElectrocuteTarget = () -> null;
		this.funcGetIsActive = () -> false;
		this.funcSetIsActiveInParent = (b) -> {};
    }

	public SubEntityExterminatorFieldEmitter(EntityCQRExterminator parent, String partName, final Supplier<LivingEntity> funcGetElectrocuteTarget, final Supplier<Boolean> funcGetIsActive, final Consumer<Boolean> funcSetIsActiveInParent) {
		super(parent); this.setBoundingBox(new net.minecraft.world.phys.AABB(0,0,0,0.5f,0.5f,0.5f));
		this.exterminator = parent;
		this.funcGetElectrocuteTarget = funcGetElectrocuteTarget;
		this.funcGetIsActive = funcGetIsActive;
		this.funcSetIsActiveInParent = funcSetIsActiveInParent;
	}
	
	@Override
	public void kill() {
		this.exterminator.kill();
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	public boolean isActive() {
		if (this.level().isClientSide()) {
			return this.funcGetIsActive.get();
		}
		return this.exterminator.canElectricCoilsBeActive() && this.remainingActiveTime > 0;
	}

	public void baseTick() {
		super.baseTick();

		// The server owns the emitter timers and mirrors only the final active flag
		// through the parent entity data. Running this state machine on the client as
		// well caused it to fight the synced value and made arcs linger/restart.
		if (this.level().isClientSide() || this.exterminator == null) {
			return;
		}

		boolean canRun = this.exterminator.canElectricCoilsBeActive();
		if (canRun && this.remainingActiveTime > 0) {
			this.funcSetIsActiveInParent.accept(true);

			if (this.tickCount % 30 == 0) {
				this.playSound(net.minecraft.sounds.SoundEvents.BEACON_AMBIENT, 0.65F, 1.65F);
			}
			LivingEntity target = this.getTargetedEntity();
			this.remainingActiveTime--;
			// If we don't have a target we wait a bit and then deactivate
			if (target == null) {
				this.activeTimeNoTarget++;
				if (this.activeTimeNoTarget > 60) {
					// We didn'T have a target for 3 seconds, we deactivate forcefully
					this.remainingActiveTime = -1;
				}
			} else {
				// Damage the target by making it electrocuted
				if (this.tickCount % 10 == 0) {
					target.hurt(this.exterminator.damageSources().lightningBolt(), 2.0F);
					this.playSound(net.minecraft.sounds.SoundEvents.LIGHTNING_BOLT_IMPACT, 0.55F, 1.7F);
				}
			}

			if (this.remainingActiveTime <= 0) {
				this.finishActiveCycle();
			}
		} else {
			this.funcSetIsActiveInParent.accept(false);

			// A stun/ground smash interrupts the current discharge and starts a real
			// cooldown instead of allowing it to resume with a stale target immediately.
			if (this.remainingActiveTime > 0) {
				this.finishActiveCycle();
			}

			if (this.cooldown > 0) {
				this.cooldown--;
			} else if (canRun) {
				this.remainingActiveTime = net.minecraft.util.Mth.nextInt(this.getParent().getRandom(), 100, 200);
				this.activeTimeNoTarget = 0;
			}
		}
	}

	private void finishActiveCycle() {
		this.remainingActiveTime = 0;
		this.activeTimeNoTarget = 0;
		this.cooldown = net.minecraft.util.Mth.nextInt(this.getParent().getRandom(), 120, 360);
		this.funcSetIsActiveInParent.accept(false);
	}

	@Nullable
	public LivingEntity getTargetedEntity() {
		LivingEntity target = this.funcGetElectrocuteTarget.get();
		return target != null && target.isAlive() ? target : null;
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (this.exterminator == null || !this.exterminator.isAlive()) {
			return InteractionResult.FAIL;
		}
		return this.exterminator.interact(player, hand);
	}
	
	
	public boolean hasCustomRenderer() {
		return true;
	}

	@Override
	protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {}
	@Override
	protected void readAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {}
	@Override
	protected void addAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {}
}

