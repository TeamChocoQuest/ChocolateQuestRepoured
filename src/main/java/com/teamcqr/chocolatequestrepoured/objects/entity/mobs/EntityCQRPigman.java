package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ICQREntity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCQRPigman extends EntityPigZombie implements ICQREntity {

	public EntityCQRPigman(World worldIn) {
		super(worldIn);
		
		this.setSize(1.0F, 2.3F);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.GREAT_SWORD_IRON));
		this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
	}
	
	@Override
	public EFaction getFaction() {
		return EFaction.UNDEAD;
	}
	
	@Override
	public float getBaseHealth() {
		return EBaseHealths.PIGMEN.getValue();
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
    protected SoundEvent getAmbientSound()
    {
        return super.getAmbientSound();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source)
    {
        return super.getHurtSound(source);
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return super.getDeathSound();
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
    	boolean res = super.attackEntityFrom(source, amount); 
    	handleArmorBreaking(getHealth(), getMaxHealth(), this);
    	
    	return res;
    }

    @Override
	public void setPosition(double x, double y, double z) {
		super.setPosition(x, y, z);
		
		spawnAt(new Double(x).intValue(), new Double(y).intValue(), new Double(z).intValue());
	}
    
    @Override
	public void spawnAt(int x, int y, int z) {
		if(getEntityWorld() != null && !getEntityWorld().isRemote) {
			//sets the actual health
			//changes the right attribute to apply
			IAttributeInstance attribute = getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
			float newHP = getBaseHealthForLocation(new BlockPos(x,y,z), this.getBaseHealth());
			//System.out.println("New HP: " + newHP);
			if(attribute != null) {
				attribute.setBaseValue(newHP);
				setHealth(getMaxHealth());
			}
			
			//setPosition(x, y, z);
		}
	}
	
	@Override
    public void onDeath(DamageSource cause) {
    	super.onDeath(cause);
    	
    	if(cause.getTrueSource() instanceof EntityPlayer) {
    		onKilled(cause.getTrueSource(), this);
    	}
    }

	@Override
	public int getRemainingHealingPotions() {
		return 0;
	}
	
}
