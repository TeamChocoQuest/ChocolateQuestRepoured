package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ICQREntity;
import com.teamcqr.chocolatequestrepoured.util.handlers.SoundsHandler;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityCQRDwarf extends EntityVindicator implements ICQREntity {

	public EntityCQRDwarf(World worldIn) {
		super(worldIn);
		
		setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_PICKAXE, 1));
		setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET, 1));
		updateActiveHand();
	}

	@Override
	public EFaction getFaction() {
		return EFaction.DWARVES_AND_GOLEMS;
	}

	@Override
	public UUID getUUID() {
		return getUniqueID();
	}

	@Override
	public boolean isBoss() {
		return false;
	}

	@Override
	public boolean isRideable() {
		return false;
	}

	@Override
	public boolean isFriendlyTowardsPlayer() {
		return false;
	}

	@Override
	public boolean hasFaction() {
		return true;
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.DWARVES.getValue();
	}

	@Override
	public void spawnAt(int x, int y, int z) {
		if(getEntityWorld() != null && !getEntityWorld().isRemote) {
			//sets the actual health
			setHealth(getBaseHealth(new BlockPos(x,y,z)));
			//changes the right attribute to apply
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getBaseHealth(new BlockPos(x,y,z)));
			
			setPosition(x, y, z);
		}
	}
	
	@Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_VILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source)
    {
        return SoundsHandler.CLASSIC_HURT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return super.getDeathSound();
    }

}
