package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.piratecaptain.parrot.BossAIPirateParrotLandOnCaptainsShoulder;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRPirate;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollow;
import net.minecraft.entity.ai.EntityAIFollowOwnerFlying;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWaterFlying;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityCQRPirateParrot extends EntityParrot {

	public EntityCQRPirateParrot(World worldIn) {
		super(worldIn);
	}
	
	protected void initEntityAI()
    {
        this.aiSit = new EntityAISit(this);
        //this.tasks.addTask(0, new EntityAIPanic(this, 1.25D));
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(1, new EntityAIWatchClosest(this, EntityCQRPirate.class, 8.0F));
        this.tasks.addTask(1, new EntityAIWatchClosest(this, EntityCQRPirateCaptain.class, 8.0F));
        //this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(2, new EntityAIFollowOwnerFlying(this, 1.0D, 5.0F, 1.0F));
        this.tasks.addTask(2, new EntityAIWanderAvoidWaterFlying(this, 1.0D));
        this.tasks.addTask(3, new BossAIPirateParrotLandOnCaptainsShoulder(this));
        this.tasks.addTask(3, new EntityAIFollow(this, 1.0D, 3.0F, 7.0F));
    }
	
	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		world.createExplosion(this, posX, posY, posZ, 2, true);
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40);
	}
	
	public boolean setCQREntityOnShoulder(AbstractEntityCQR p_191994_1_)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setString("id", this.getEntityString());
        this.writeToNBT(nbttagcompound);

        if (p_191994_1_.addShoulderEntity(nbttagcompound))
        {
            this.world.removeEntity(this);
            return true;
        }
        else
        {
            return false;
        }
    }
	
	@Override
	public boolean setEntityOnShoulder(EntityPlayer p_191994_1_) {
		return false;
	}

}
