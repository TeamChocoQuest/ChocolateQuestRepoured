package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCQRNetherDragon extends AbstractEntityCQR implements IEntityMultiPart, IRangedAttackMob {

	private MultiPartEntityPart[] dragonBodyParts;

	private MultiPartEntityPart headPart = new MultiPartEntityPart(this, "head", 2.5F, 1.25F);
	private MultiPartEntityPart body1 = new MultiPartEntityPart(this, "bodySegment1", 1.1F, 1.1F);
	private MultiPartEntityPart body2 = new MultiPartEntityPart(this, "bodySegment2", 1.1F, 1.1F);
	private MultiPartEntityPart body3 = new MultiPartEntityPart(this, "bodySegment3", 1.1F, 1.1F);
	private MultiPartEntityPart body4 = new MultiPartEntityPart(this, "bodySegment4", 1.1F, 1.1F);
	private MultiPartEntityPart body5 = new MultiPartEntityPart(this, "bodySegment5", 1.1F, 1.1F);
	private MultiPartEntityPart body6 = new MultiPartEntityPart(this, "bodySegment6", 1.1F, 1.1F);
	private MultiPartEntityPart body7 = new MultiPartEntityPart(this, "bodySegment7", 1.1F, 1.1F);
	private MultiPartEntityPart body8 = new MultiPartEntityPart(this, "bodySegment8", 1.1F, 1.1F);
	private MultiPartEntityPart body9 = new MultiPartEntityPart(this, "bodySegment9", 1.1F, 1.1F);
	private MultiPartEntityPart body10 = new MultiPartEntityPart(this, "bodySegment10", 1.1F, 1.1F);
	private MultiPartEntityPart body11 = new MultiPartEntityPart(this, "bodySegment11", 1.1F, 1.1F);
	private MultiPartEntityPart body12 = new MultiPartEntityPart(this, "bodySegment12", 1.1F, 1.1F);
	private MultiPartEntityPart body13 = new MultiPartEntityPart(this, "bodySegment13", 1.1F, 1.1F);
	private MultiPartEntityPart body14 = new MultiPartEntityPart(this, "bodySegment14", 1.1F, 1.1F);
	private MultiPartEntityPart body15 = new MultiPartEntityPart(this, "bodySegment15", 1.1F, 1.1F);
	private MultiPartEntityPart body16 = new MultiPartEntityPart(this, "bodySegment16", 1.1F, 1.1F);
	
	/*
	 * Notes: This dragon is meant to "swim" through the skies, it moves like a snake, so the model needs animation, also the parts are meant to move like the parts from Twilight Forests Naga
	 * 
	 * Also the nether dragon destroys all blocks in its hitbox, if these are not lava, also if the block it moved through are leaves or burnable, it will set them on fire
	 * It will also break obsidian blocks, but not command blocks or structure blocks or bedrock
	 */

	public EntityCQRNetherDragon(World worldIn) {
		super(worldIn);
		this.dragonBodyParts = new MultiPartEntityPart[] { this.headPart, this.body1, this.body2, this.body3,
				this.body4, this.body5, this.body6, this.body7, this.body8, this.body9, this.body10, this.body11,
				this.body12, this.body13, this.body14, this.body15, this.body16 };
		this.setSize(2.0F, 2.0F);
		this.noClip = true;
		this.setNoGravity(true);
		
		allignPositionsOfBodySegments();
	}
	
	private void allignPositionsOfBodySegments() {
		//This is just for testing purposes, the final code to move the hitboxes will be in living update AND it will position the parts corresponding to the old rotation of the previous part
		this.headPart.onUpdate();
		this.headPart.setPositionAndRotation(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
		
		for(int i = 1; i < this.dragonBodyParts.length; i++) {
			this.dragonBodyParts[i].onUpdate();
			this.dragonBodyParts[i].setPositionAndRotation(i* this.posX, this.posY, i* this.posZ, 0.0F, 0.0F);
		}
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		// TODO: Init the parts

		this.headPart.width = 2.5F;
		this.headPart.height = 1.125F;
		this.headPart.onUpdate();

		this.body1.width = 1.1F;
		this.body1.height = 1.1F;
		this.body1.onUpdate();

		this.body2.width = 1.1F;
		this.body2.height = 1.1F;
		this.body2.onUpdate();

		this.body3.width = 1.1F;
		this.body3.height = 1.1F;
		this.body3.onUpdate();

		this.body4.width = 1.1F;
		this.body4.height = 1.1F;
		this.body4.onUpdate();

		this.body5.width = 1.1F;
		this.body5.height = 1.1F;
		this.body5.onUpdate();

		this.body6.width = 1.1F;
		this.body6.height = 1.1F;
		this.body6.onUpdate();

		this.body7.width = 1.1F;
		this.body7.height = 1.1F;
		this.body7.onUpdate();

		this.body8.width = 1.1F;
		this.body8.height = 1.1F;
		this.body8.onUpdate();

		this.body9.width = 1.1F;
		this.body9.height = 1.1F;
		this.body9.onUpdate();

		this.body10.width = 1.1F;
		this.body10.height = 1.1F;
		this.body10.onUpdate();

		this.body11.width = 1.1F;
		this.body11.height = 1.1F;
		this.body11.onUpdate();

		this.body12.width = 1.1F;
		this.body12.height = 1.1F;
		this.body12.onUpdate();

		this.body13.width = 1.1F;
		this.body13.height = 1.1F;
		this.body13.onUpdate();

		this.body14.width = 1.1F;
		this.body14.height = 1.1F;
		this.body14.onUpdate();

		this.body15.width = 1.1F;
		this.body15.height = 1.1F;
		this.body15.onUpdate();

		this.body16.width = 1.1F;
		this.body16.height = 1.1F;
		this.body16.onUpdate();
		
		allignPositionsOfBodySegments();
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source instanceof EntityDamageSource && ((EntityDamageSource) source).getIsThornsDamage()) {
			return this.attackEntityFromPart(this.headPart, source, amount);
		}

		return false;
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}

	@Override
	public boolean isPotionApplicable(PotionEffect potioneffectIn) {
		return false;
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {

	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.NETHER_DRAGON.getValue();
	}

	@Override
	public EFaction getFaction() {
		return null;
	}

	@Override
	public World getWorld() {
		return this.world;
	}

	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart dragonPart, DamageSource source, float damage) {
		if (dragonPart != this.headPart) {
			damage = damage / 4.0F + Math.min(damage, 1.0F);
		}

		return damage > 0.01F;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		//TODO: Shoot fireball OR spit fire if close enough
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
		//Unused?
	}

}
