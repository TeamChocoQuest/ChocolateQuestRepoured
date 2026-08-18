package com.example.chocolatequest.entity.boss.exterminator;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.entity.PartEntity;

public class SubEntityExterminatorHitboxPart extends net.neoforged.neoforge.entity.PartEntity<EntityCQRExterminator> {

	public SubEntityExterminatorHitboxPart(EntityCQRExterminator parent, String partName, float width, float height) {
		super(parent); this.setBoundingBox(new net.minecraft.world.phys.AABB(0,0,0,width,height,width));
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (this.getParent() == null || !((LivingEntity) this.getParent()).isAlive()) {
			return InteractionResult.FAIL;
		}
		return ((LivingEntity) this.getParent()).interact(player, hand);
	}

	@Override
	protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {}
	@Override
	protected void readAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {}
	@Override
	protected void addAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {}
}

