package com.example.chocolatequest.entity.projectile;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class ProjectileBase extends ThrowableProjectile {

	protected ProjectileBase(EntityType<? extends ProjectileBase> throwableEntity, Level world) {
		super(throwableEntity, world);
	}

	protected ProjectileBase(EntityType<? extends ProjectileBase> throwableEntity, double pX, double pY, double pZ, Level world) {
		this(throwableEntity, world);
		this.setPos(pX, pY, pZ);
	}

	protected ProjectileBase(EntityType<? extends ProjectileBase> throwableEntity, LivingEntity shooter, Level world) {
		this(throwableEntity, shooter.getX(), shooter.getEyeY() - (double)0.1F, shooter.getZ(), world);
		this.setOwner(shooter);
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	public void tick() {
		if (!this.level().isClientSide() && !this.level().hasChunkAt(this.blockPosition())) {
			this.discard();
			return;
		}
		if (this.tickCount > 400) {
			this.discard();
		}
		super.tick();
		this.onUpdateInAir();
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		BlockState state = this.level().getBlockState(result.getBlockPos());
		state.onProjectileHit(this.level(), state, result, this);
		if (state.blocksMotion()) {
			this.onDestroyedByBlockImpact();
			this.discard();
		}
		super.onHitBlock(result);
	}

	protected void onDestroyedByBlockImpact() {}

	protected void onUpdateInAir() {}
}
