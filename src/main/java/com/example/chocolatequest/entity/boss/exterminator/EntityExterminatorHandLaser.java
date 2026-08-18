package com.example.chocolatequest.entity.boss.exterminator;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import com.example.chocolatequest.entity.projectile.EntityTargetingLaser;
import com.example.chocolatequest.registry.ModEntities;

public class EntityExterminatorHandLaser extends EntityTargetingLaser {

	public EntityExterminatorHandLaser(Level worldIn) {
		super(ModEntities.EXTERMINATOR_HAND_LASER.get(), worldIn);
	}
	
	public EntityExterminatorHandLaser(EntityType<? extends EntityExterminatorHandLaser> type, Level worldIn) {
		super(type, worldIn);
	}

	public EntityExterminatorHandLaser(LivingEntity caster, LivingEntity target) {
		this(ModEntities.EXTERMINATOR_HAND_LASER.get(), caster.level(), caster, 48, target);
	}

	public EntityExterminatorHandLaser(EntityType<? extends EntityExterminatorHandLaser> type, Level worldIn, LivingEntity caster, float length, LivingEntity target) {
		super(type, worldIn, caster, length, target);
		this.maxRotationPerTick = 1.25F;
	}

	/*@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1 || pass == 0;
	}*/

	@Override
	public Vec3 getOffsetVector() {
		if (this.caster instanceof EntityCQRExterminator) {
			return ((EntityCQRExterminator) this.caster).getCannonFiringPointOffset();
		}
		return super.getOffsetVector();
	}

	@Override
	public float getColorR() {
		return 1.0F;
	}

	@Override
	public float getColorG() {
		return 0.0F;
	}

	@Override
	public float getColorB() {
		return 0.0F;
	}

	@Override
	public float getDamage() {
		return 1.25F;
	}

	@Override
	public void onEntityHit(LivingEntity entity) {
		super.onEntityHit(entity);
		entity.igniteForSeconds(5);
	}

	@Override
	public boolean canBreakBlocks() {
		return true;
	}

	@Override
	public int getBreakingSpeed() {
		return 12;
	}
	
	@Override
	public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket() {
		return null;
	}

}

