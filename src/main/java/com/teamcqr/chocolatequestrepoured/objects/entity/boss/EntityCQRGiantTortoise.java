package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ELootTablesBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.subparts.EntityCQRGiantTortoisePart;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfo.Overlay;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCQRGiantTortoise extends AbstractEntityCQRBoss implements IEntityMultiPart, IRangedAttackMob {
	
	private static final DataParameter<Boolean> MOUTH_OPEN = EntityDataManager.<Boolean>createKey(EntityCQRGiantTortoise.class, DataSerializers.BOOLEAN);
	
	protected EntityCQRGiantTortoisePart[] parts = new EntityCQRGiantTortoisePart[5];
	
	final float baseWidth = 2.0F;
	final float baseHeight = 1.7F;

	public EntityCQRGiantTortoise(World worldIn) {
		//super(worldIn, size);
		super(worldIn, 1);
		
		bossInfoServer.setColor(Color.GREEN);
		bossInfoServer.setCreateFog(true);
		bossInfoServer.setOverlay(Overlay.PROGRESS);
		
		setSize(baseWidth, baseHeight);
		
		for(int i = 0; i < parts.length -1; i++) {
			parts[i] = new EntityCQRGiantTortoisePart(this, "tortoise_leg" +i, 0.7F, 1.1F, false);
		}
		parts[parts.length -1] = new EntityCQRGiantTortoisePart(this, "tortoise_head", 0.7F, 0.7F, true);
		
		noClip = false;
		setNoGravity(false);
		experienceValue = 150;
		
		ignoreFrustumCheck = true;
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		
		this.dataManager.register(MOUTH_OPEN, false);
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart dragonPart, DamageSource source, float damage) {
		return attackEntityFrom(source, damage, true);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount, boolean sentFromPart) {
		if(sentFromPart) {
			return super.attackEntityFrom(source, amount, sentFromPart);
		} 
		return true;
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {

	}

	@Override
	protected ResourceLocation getLootTable() {
		return ELootTablesBoss.BOSS_TURTLE.getLootTable();
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.GIANT_TORTOISE.getValue();
	}

	@Override
	public EFaction getFaction() {
		return EFaction.BEASTS;
	}

	@Override
	public int getTextureCount() {
		return 1;
	}

	@Override
	public boolean canRide() {
		return false;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
		
	}
	
	public void setMouthOpen(boolean open) {
		this.dataManager.set(MOUTH_OPEN, open);
	}
	
	public boolean isMouthOpen() {
		return this.dataManager.get(MOUTH_OPEN);
	}
	
	@Override
	public void writeSpawnData(ByteBuf buffer) {
		super.writeSpawnData(buffer);
		buffer.writeBoolean(this.dataManager.get(MOUTH_OPEN));
	}
	
	@Override
	public void readSpawnData(ByteBuf additionalData) {
		super.readSpawnData(additionalData);
		this.dataManager.set(MOUTH_OPEN, additionalData.readBoolean());
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		for(EntityCQRGiantTortoisePart part : parts) {
			this.world.updateEntityWithOptionalForce(part, true);
		}
		
		alignParts();
	}

	private void alignParts() {
		//Legs
		Vec3d v = new Vec3d(0,0,baseWidth/2 + baseWidth*0.1);
		v = VectorUtil.rotateVectorAroundY(v, this.rotationYawHead);
		
		parts[parts.length -1].setPosition(posX + v.x, posY +0.5, posZ +v.z);
		parts[parts.length -1].setRotation(rotationYawHead, rotationPitch);
		
		v = VectorUtil.rotateVectorAroundY(v, 45D);
		
		for(int i = 0; i < parts.length -1; i++) {
			if(i > 0) {
				v = VectorUtil.rotateVectorAroundY(v, 90D);
			}
			
			double x = posX +v.x;
			double y = posY;
			double z = posZ +v.z;
			
			parts[i].setPosition(x, y, z);
			parts[i].setRotation(this.rotationYaw +i*45F, this.rotationPitch);
		}
	}
	
	@Override
	public Entity[] getParts() {
		return parts;
	}
	

	@Override
	public void setDead() {
		super.setDead();
		for (EntityCQRGiantTortoisePart part : parts) {
			// must use this instead of setDead
			// since multiparts are not added to the world tick list which is what checks isDead
			this.world.removeEntityDangerously(part);
		}
	}

}
