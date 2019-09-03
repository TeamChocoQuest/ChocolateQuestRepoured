package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import java.util.Random;
import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ICQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveHome;
import com.teamcqr.chocolatequestrepoured.util.NBTUtil;
import com.teamcqr.chocolatequestrepoured.util.handlers.SoundsHandler;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCQRDwarf extends EntityVindicator implements ICQREntity {


	private boolean hasExisted = false;

	public BlockPos home;
	public EntityLivingBase leader;


	public EntityCQRDwarf(World worldIn) {
		super(worldIn);
		
		this.setSize(0.55F, 1.4F);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		Item[] pickaxes = new Item[] {Items.STONE_PICKAXE, Items.IRON_PICKAXE, Items.GOLDEN_PICKAXE, Items.DIAMOND_PICKAXE};
		Item[] helmets = new Item[] {Items.IRON_HELMET, Items.DIAMOND_HELMET, Items.CHAINMAIL_HELMET};
		
		Random rdm = new Random();
		
		this.setItemStackToSlot(rdm.nextBoolean() ? EntityEquipmentSlot.OFFHAND : EntityEquipmentSlot.MAINHAND, new ItemStack(pickaxes[rdm.nextInt(pickaxes.length)], 1));
		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(helmets[rdm.nextInt(helmets.length)], 1));
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
	public void setPosition(double x, double y, double z) {
		super.setPosition(x, y, z);
		if(!hasExisted) {
			this.home = new BlockPos(x, y, z);
			
			spawnAt(new Double(x).intValue(), new Double(y).intValue(), new Double(z).intValue());
      
			this.hasExisted = true;

		}
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
    
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
    	boolean res = super.attackEntityFrom(source, amount); 
    	handleArmorBreaking(getHealth(), getMaxHealth(), this);
    	
    	return res;
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
