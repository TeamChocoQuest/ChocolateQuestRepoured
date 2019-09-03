package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ICQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveHome;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCQRPigman extends EntityPigZombie implements ICQREntity {

	private boolean hasExisted = false;

	public BlockPos home;
	public EntityLivingBase leader;

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
		this.home = new BlockPos(x, y, z);
		
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

	@Override
	public void setHome(BlockPos home) {
		this.home = home;
	}

	@Override
	public BlockPos getHome() {
		return home;
	}

	@Override
	public void setLeader(EntityLivingBase leader) {
		this.leader = leader;
	}

	@Override
	public EntityLivingBase getLeader() {
		return leader;
	}

	@Override
	protected PathNavigate createNavigator(World worldIn) {
		return new PathNavigateGround(this, worldIn) {
			@Override
			public float getPathSearchRange() {
				return 128.0F;
			}
		};
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(5, new EntityAIMoveToHome(this));
		this.tasks.addTask(6, new EntityAIMoveToLeader(this));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		boolean hasHome = this.home != null;
		compound.setBoolean("hasHome", hasHome);
		if (hasHome) {
			compound.setTag("home", NBTUtil.BlockPosToNBTTag(this.home));
		}

		boolean hasLeader = this.leader != null;
		compound.setBoolean("hasLeader", hasLeader);
		if (hasLeader) {
			compound.setInteger("leader", this.leader.getEntityId());
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		this.hasExisted = true;

		super.readEntityFromNBT(compound);

		if (compound.getBoolean("hasHome")) {
			this.home = NBTUtil.BlockPosFromNBT(compound.getCompoundTag("home"));
		}

		if (compound.getBoolean("hasLeader")) {
			this.leader = (EntityLivingBase) this.world.getEntityByID(compound.getInteger("leader"));
		}
	}
}
