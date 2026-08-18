package com.example.chocolatequest.entity.boss.exterminator;

import java.util.function.Supplier;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import com.example.chocolatequest.entity.ai.target.TargetUtil;
import net.neoforged.neoforge.entity.PartEntity;

public class SubEntityExterminatorBackpack extends net.neoforged.neoforge.entity.PartEntity<EntityCQRExterminator> {

	private Supplier<Boolean> funcGetAnyEmitterActive;
	private EntityCQRExterminator exterminator;
	
	public SubEntityExterminatorBackpack(EntityCQRExterminator parent, String partName, Supplier<Boolean> funcGetAnyEmitterActive) {
		super(parent); this.setBoundingBox(new net.minecraft.world.phys.AABB(0,0,0,1.0f,1.0f,1.0f));
		this.exterminator = parent;
		this.funcGetAnyEmitterActive = funcGetAnyEmitterActive;
	}

	@Override
	public boolean isPickable() {
		return true;
	}
	
	@Override
	public void kill() {
		this.exterminator.kill();
	}
	
	@Override
	public boolean hurt(DamageSource source, float amount) {
		// If at least one emitter emits electricity, we are vulnerable!!
		if (source.is(net.minecraft.tags.DamageTypeTags.IS_DROWNING)) {
			if (this.funcGetAnyEmitterActive.get()) {
				this.exterminator.setStunned(true, 100);
			}
		}

		if (source.is(net.minecraft.tags.DamageTypeTags.IS_LIGHTNING)) {
			this.exterminator.setStunned(true, 75);
		}

		if (!this.exterminator.isStunned() && this.funcGetAnyEmitterActive.get() && !TargetUtil.PREDICATE_IS_ELECTROCUTED.apply(this.exterminator)) {
			return super.hurt(this.damageSources().drown(), amount /= 2);
		}

		return super.hurt(source, amount * 2);
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (this.exterminator == null || !this.exterminator.isAlive()) {
			return InteractionResult.FAIL;
		}
		return this.exterminator.interact(player, hand);
	}

	@Override
	protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {}
	@Override
	protected void readAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {}
	@Override
	protected void addAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {}
}

