package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.init.ModLoottables;
import com.teamcqr.chocolatequestrepoured.objects.entity.Capes;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.EntityEquipmentExtraSlot;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.piratecaptain.BossAIPirateSummonParrot;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.piratecaptain.BossAIPirateTurnInvisible;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQRBoss;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCQRPirateCaptain extends AbstractEntityCQRBoss {
	
	private static final DataParameter<Boolean> IS_INVISIBLE = EntityDataManager.<Boolean>createKey(EntityCQRPirateCaptain.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> TURNING_INVISIBLE = EntityDataManager.<Boolean>createKey(EntityCQRPirateCaptain.class, DataSerializers.BOOLEAN);
	
	public static int TURN_INVISIBLE_ANIMATION_TIME = 60;

	private boolean spawnedParrot = false;
	
	public int turnInvisibleTime = 1;
	
	public EntityCQRPirateCaptain(World worldIn) {
		super(worldIn);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return ModLoottables.ENTITIES_PIRATE_CAPTAIN;
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.PIRATE_CAPTAIN.getValue();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.PIRATE;
	}
	
	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		
		this.spellHandler.addSpell(0, new BossAIPirateSummonParrot(this, 0, 20, 20));
		
		this.tasks.addTask(1, new BossAIPirateTurnInvisible(this));
	}
	
	@Override
	public void setSitting(boolean sitting) {
		super.setSitting(false);
	}
	
	@Override
	public boolean hasCape() {
		return true;
	}
	
	@Override
	public ResourceLocation getResourceLocationOfCape() {
		return Capes.CAPE_PIRATE;
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(IS_INVISIBLE, false);
		this.dataManager.register(TURNING_INVISIBLE, false);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		super.setEquipmentBasedOnDifficulty(difficulty);
		setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.CAPTAIN_REVOLVER, 1));
		
		setItemStackToExtraSlot(EntityEquipmentExtraSlot.ARROW, new ItemStack(ModItems.BULLET_FIRE, 64));
		setItemStackToExtraSlot(EntityEquipmentExtraSlot.POTION, new ItemStack(ModItems.POTION_HEALING, 2));
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		setEquipmentBasedOnDifficulty(difficulty);
		return super.onInitialSpawn(difficulty, livingdata);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("spawnedParrot", spawnedParrot);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		spawnedParrot = compound.getBoolean("spawnedParrot");
	}
	
	public boolean hasSpawnedParrot() {
		return spawnedParrot;
	}

	public void setSpawnedParrot(boolean b) {
		this.spawnedParrot = b;
	}
	
	public void setIsInvisible(boolean value) {
		this.dataManager.set(IS_INVISIBLE, value);
	}
	
	public void setIsTurningInvisible(boolean value) {
		this.dataManager.set(TURNING_INVISIBLE, value);
	}
	
	public boolean getIsInvisible() {
		return this.dataManager.get(IS_INVISIBLE);
	}
	
	public boolean isTurningInvisible() {
		return this.dataManager.get(TURNING_INVISIBLE);
	}

}
