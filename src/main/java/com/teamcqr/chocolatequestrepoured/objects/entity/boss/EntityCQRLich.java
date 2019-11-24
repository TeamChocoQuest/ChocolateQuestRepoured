package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ELootTablesBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIAttack;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAICQRNearestAttackTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIHealingPotion;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIIdleSit;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToHome;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.lich.BossAISummonZombie;

import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfo.Overlay;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCQRLich extends AbstractEntityCQRMageBase {

	public EntityCQRLich(World worldIn) {
		this(worldIn, 1);
	}
	
	public EntityCQRLich(World worldIn, int size) {
		super(worldIn, size);
		
		bossInfoServer.setColor(Color.RED);
		bossInfoServer.setCreateFog(true);
		bossInfoServer.setOverlay(Overlay.PROGRESS);
		
		setSize(0.6F, 1.8F);
	}
	
	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(5, new EntityAIHealingPotion(this));
		this.tasks.addTask(8, new BossAISummonZombie(this));
		this.tasks.addTask(10, new EntityAIAttack(this));
		this.tasks.addTask(20, new EntityAIMoveToHome(this));
		this.tasks.addTask(21, new EntityAIIdleSit(this));

		this.targetTasks.addTask(0, new EntityAICQRNearestAttackTarget(this));
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {

	}

	@Override
	protected ResourceLocation getLootTable() {
		return ELootTablesBoss.BOSS_LICH.getLootTable();
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.LICH.getValue();
	}

	@Override
	public EFaction getFaction() {
		return EFaction.UNDEAD;
	}

}
