package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.AbstractEntityCQRMageBase;

import net.minecraft.entity.monster.AbstractIllager.IllagerArmPose;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityCQRIllager extends AbstractEntityCQR {

	private static final DataParameter<Boolean> IS_AGGRESSIVE = EntityDataManager.<Boolean>createKey(AbstractEntityCQRMageBase.class, DataSerializers.BOOLEAN);
	
	public EntityCQRIllager(World worldIn) {
		super(worldIn);
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		
		this.dataManager.register(IS_AGGRESSIVE, false);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {

	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if(getAttackTarget() != null) {
			dataManager.set(IS_AGGRESSIVE, true);
		} else {
			dataManager.set(IS_AGGRESSIVE, false);
		}
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.ILLAGER.getValue();
	}

	@Override
	public EFaction getDefaultFaction() {
		return EFaction.BEASTS;
	}
	
	@Override
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_VINDICATION_ILLAGER;
	}
	
	@Override
	public int getTextureCount() {
		return 1;
	}
	
	@Override
	public boolean canRide() {
		return true;
	}

	public boolean isAggressive() {
		return dataManager.get(IS_AGGRESSIVE);
	}

	@SideOnly(Side.CLIENT)
	public IllagerArmPose getIllagerArmPose() {
		if(isAggressive()) {
			switch(getArmPose()) {
			case HOLDING_ITEM:
				return IllagerArmPose.ATTACKING;
			case PULLING_BOW:
				return IllagerArmPose.ATTACKING;
			case SPELLCASTING:
				return IllagerArmPose.SPELLCASTING;
			case STAFF_L:
			case STAFF_R:
				return IllagerArmPose.ATTACKING;
			default:
				return IllagerArmPose.CROSSED;
			}
		}
		
		return IllagerArmPose.CROSSED;
	}

}
