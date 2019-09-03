package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ICQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToHome;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToLeader;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityCQRNetherDragon extends EntityMob implements ICQREntity, IRangedAttackMob, IEntityMultiPart {

	protected BlockPos home;
	protected UUID leaderUUID;

	private MultiPartEntityPart[] dragonBodyParts;
	
	private MultiPartEntityPart headPart = new MultiPartEntityPart(this, "head", 2.5F, 1.25F);
	private MultiPartEntityPart body1 = new MultiPartEntityPart(this, "bodySegment1", 1.1f, 1.1f);
	private MultiPartEntityPart body2 = new MultiPartEntityPart(this, "bodySegment2", 1.1f, 1.1f);
	private MultiPartEntityPart body3 = new MultiPartEntityPart(this, "bodySegment3", 1.1f, 1.1f);
	private MultiPartEntityPart body4 = new MultiPartEntityPart(this, "bodySegment4", 1.1f, 1.1f);
	private MultiPartEntityPart body5 = new MultiPartEntityPart(this, "bodySegment5", 1.1f, 1.1f);
	private MultiPartEntityPart body6 = new MultiPartEntityPart(this, "bodySegment6", 1.1f, 1.1f);
	private MultiPartEntityPart body7 = new MultiPartEntityPart(this, "bodySegment7", 1.1f, 1.1f);
	private MultiPartEntityPart body8 = new MultiPartEntityPart(this, "bodySegment8", 1.1f, 1.1f);
	private MultiPartEntityPart body9 = new MultiPartEntityPart(this, "bodySegment9", 1.1f, 1.1f);
	private MultiPartEntityPart body10 = new MultiPartEntityPart(this, "bodySegment10", 1.1f, 1.1f);
	private MultiPartEntityPart body11 = new MultiPartEntityPart(this, "bodySegment11", 1.1f, 1.1f);
	private MultiPartEntityPart body12 = new MultiPartEntityPart(this, "bodySegment12", 1.1f, 1.1f);
	private MultiPartEntityPart body13 = new MultiPartEntityPart(this, "bodySegment13", 1.1f, 1.1f);
	private MultiPartEntityPart body14 = new MultiPartEntityPart(this, "bodySegment14", 1.1f, 1.1f);
	private MultiPartEntityPart body15 = new MultiPartEntityPart(this, "bodySegment15", 1.1f, 1.1f);
	private MultiPartEntityPart body16 = new MultiPartEntityPart(this, "bodySegment16", 1.1f, 1.1f);

	public EntityCQRNetherDragon(World worldIn) {
		super(worldIn);
		this.dragonBodyParts = new MultiPartEntityPart[] {
				this.headPart,
				this.body1,
				this.body2,
				this.body3,
				this.body4,
				this.body5,
				this.body6,
				this.body7,
				this.body8,
				this.body9,
				this.body10,
				this.body11,
				this.body12,
				this.body13,
				this.body14,
				this.body15,
				this.body16
		};
		this.setHealth(getBaseHealth());
		this.setSize(15.5f, 1.8f);
		this.noClip = true;
		this.setNoGravity(true);
	}

	@Override
	public World getWorld() {
		return getEntityWorld();
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		
		//TODO: Init the parts
		
		this.headPart.width = 2.5f;
		this.headPart.height = 1.125f;
		this.headPart.onUpdate();
		
		this.body1.width = 1.1f;
		this.body1.height = 1.1f;
		this.body1.onUpdate();
		
		this.body2.width = 1.1f;
		this.body2.height = 1.1f;
		this.body2.onUpdate();
		
		this.body3.width = 1.1f;
		this.body3.height = 1.1f;
		this.body3.onUpdate();
		
		this.body4.width = 1.1f;
		this.body4.height = 1.1f;
		this.body4.onUpdate();
		
		this.body5.width = 1.1f;
		this.body5.height = 1.1f;
		this.body5.onUpdate();
		
		this.body6.width = 1.1f;
		this.body6.height = 1.1f;
		this.body6.onUpdate();
		
		this.body7.width = 1.1f;
		this.body7.height = 1.1f;
		this.body7.onUpdate();
		
		this.body8.width = 1.1f;
		this.body8.height = 1.1f;
		this.body8.onUpdate();
		
		this.body9.width = 1.1f;
		this.body9.height = 1.1f;
		this.body9.onUpdate();
		
		this.body10.width = 1.1f;
		this.body10.height = 1.1f;
		this.body10.onUpdate();
		
		this.body11.width = 1.1f;
		this.body11.height = 1.1f;
		this.body11.onUpdate();
		
		this.body12.width = 1.1f;
		this.body12.height = 1.1f;
		this.body12.onUpdate();
		
		this.body13.width = 1.1f;
		this.body13.height = 1.1f;
		this.body13.onUpdate();
		
		this.body14.width = 1.1f;
		this.body14.height = 1.1f;
		this.body14.onUpdate();
		
		this.body15.width = 1.1f;
		this.body15.height = 1.1f;
		this.body15.onUpdate();
		
		this.body16.width = 1.1f;
		this.body16.height = 1.1f;
		this.body16.onUpdate();
	}
	
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getBaseHealth());
    }
	
	 /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (source instanceof EntityDamageSource && ((EntityDamageSource)source).getIsThornsDamage())
        {
            this.attackEntityFromPart(this.headPart, source, amount);
        }

        return false;
    }


	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart dragonPart, DamageSource source, float damage) {
        if (dragonPart != this.headPart)
        {
            damage = damage / 4.0F + Math.min(damage, 1.0F);
        }

        if (damage < 0.01F)
        {
            return false;
        }
        else
        {
            return true;
        }
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
		// TODO Auto-generated method stub

	}
	
	/**
     * Returns false if this Entity is a boss, true otherwise.
     */
    public boolean isNonBoss()
    {
        return false;
    }
    /**
     * adds a PotionEffect to the entity
     */
    public void addPotionEffect(PotionEffect potioneffectIn)
    {
    }

    protected boolean canBeRidden(Entity rider)
    {
        return false;
    }

	@Override
	public EFaction getFaction() {
		return null;
	}

	@Override
	public UUID getUUID() {
		return getUniqueID();
	}

	@Override
	public boolean isBoss() {
		return true;
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
		return false;
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.NETHER_DRAGON.getValue();
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
	public int getRemainingHealingPotions() {
		return 0;
	}

	@Override
	public void onSpawnFromCQRSpawnerInDungeon(int x, int y, int z) {
		if (!this.world.isRemote) {
			IAttributeInstance attribute = getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
			
			if (attribute != null) {
				float newHP = getBaseHealthForLocation(new BlockPos(x, y, z), this.getBaseHealth());
				attribute.setBaseValue(newHP);
				this.setHealth(newHP);
			}
			
			this.home = new BlockPos(x, y, z);
		}
	}

	@Override
	public EntityLivingBase getLeader() {
		for (Entity entity : this.world.loadedEntityList) {
			if (entity instanceof EntityLivingBase && this.leaderUUID.equals(entity.getPersistentID())) {
				return (EntityLivingBase) entity;
			}
		}
		return null;
	}

	@Override
	public void setLeader(EntityLivingBase leader) {
		this.leaderUUID = leader.getPersistentID();
	}

	@Override
	public BlockPos getHome() {
		return this.home;
	}

	@Override
	public void setHome(BlockPos home) {
		this.home = home;
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
			compound.setTag("home", NBTUtil.createPosTag(this.home));
		}

		boolean hasLeader = this.leaderUUID != null;
		compound.setBoolean("hasLeader", hasLeader);
		if (hasLeader) {
			compound.setTag("leader", NBTUtil.createUUIDTag(this.leaderUUID));
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);

		if (compound.getBoolean("hasHome")) {
			this.home = NBTUtil.getPosFromTag(compound.getCompoundTag("home"));
		}

		if (compound.getBoolean("hasLeader")) {
			this.leaderUUID = NBTUtil.getUUIDFromTag(compound.getCompoundTag("leader"));
		}
	}

}
