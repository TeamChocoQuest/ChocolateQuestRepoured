package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.init.ModLoottables;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.EntityEquipmentExtraSlot;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.piratecaptain.BossAIPirateSummonParrot;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQRBoss;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCQRPirateCaptain extends AbstractEntityCQRBoss {

	private boolean spawnedParrot = false;
	
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

}
