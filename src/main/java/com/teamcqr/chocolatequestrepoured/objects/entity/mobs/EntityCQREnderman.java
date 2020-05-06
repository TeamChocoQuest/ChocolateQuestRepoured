package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.CQRLootTableList;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class EntityCQREnderman extends AbstractEntityCQR {

	public EntityCQREnderman(World worldIn) {
		super(worldIn);
		this.stepHeight = 1.0F;
		this.setPathPriority(PathNodeType.WATER, -1.0F);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (super.attackEntityFrom(source, amount)) {
			if (source instanceof EntityDamageSourceIndirect) {
				for (int i = 0; i < 64; ++i) {
					if (this.teleportRandomly()) {
						return true;
					}
				}

				return false;
			}
		}
		return super.attackEntityFrom(source, amount);
	}

	protected boolean teleportRandomly() {
		double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
		double d1 = this.posY + (double) (this.rand.nextInt(64) - 32);
		double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
		return this.teleportTo(d0, d1, d2);
	}

	private boolean teleportTo(double x, double y, double z) {
		EnderTeleportEvent event = new EnderTeleportEvent(this, x, y, z, 0);
		if (MinecraftForge.EVENT_BUS.post(event)) {
			return false;
		}
		boolean flag = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

		if (flag) {
			this.world.playSound((EntityPlayer) null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
			this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
		}

		return flag;
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.ENDERMAN.getValue();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.ENDERMEN;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return /* this.isScreaming() ? SoundEvents.ENTITY_ENDERMEN_SCREAM : */ SoundEvents.ENTITY_ENDERMEN_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_ENDERMEN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_ENDERMEN_DEATH;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLootTableList.ENTITIES_ENDERMAN;
	}

	@Override
	public boolean isSitting() {
		return false;
	}

	@Override
	public boolean canMountEntity() {
		return false;
	}

	@Override
	public float getEyeHeight() {
		return this.height * 0.875F;
	}

	@Override
	public float getDefaultWidth() {
		return 0.6F;
	}

	@Override
	public float getDefaultHeight() {
		return 2.9F;
	}

}
