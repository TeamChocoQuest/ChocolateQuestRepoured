package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;
import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ELootTablesBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIAttack;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIAttackRanged;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAICQRNearestAttackTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIHealingPotion;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIIdleSit;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToHome;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAIExplosionRay;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAIExplosionSpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAISummonFireWall;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAISummonMeteors;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.ISummoner;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfo.Overlay;
import net.minecraft.world.World;

public class EntityCQRBoarmage extends AbstractEntityCQRMageBase implements ISummoner {

	protected List<Entity> summonedMinions = new ArrayList<>();

	public EntityCQRBoarmage(World world) {
		this(world, 1);
	}

	public EntityCQRBoarmage(World worldIn, int size) {
		super(worldIn, size);
		this.bossInfoServer.setColor(Color.RED);
		this.bossInfoServer.setCreateFog(true);
		this.bossInfoServer.setOverlay(Overlay.PROGRESS);

		this.isImmuneToFire = true;

		this.setSize(0.6F, 1.8F);
	}

	@Override
	public boolean isImmuneToExplosions() {
		return true;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		List<Entity> tmp = new ArrayList<>();
		for (Entity ent : this.summonedMinions) {
			if (ent == null || ent.isDead) {
				tmp.add(ent);
			}
		}
		for (Entity e : tmp) {
			this.summonedMinions.remove(e);
		}
	}

	@Override
	public void onDeath(DamageSource cause) {
		// Kill minions
		for (Entity e : this.summonedMinions) {
			if (e != null && !e.isDead) {
				if (e instanceof EntityLivingBase) {
					((EntityLivingBase) e).onDeath(cause);
				}
				if (e != null) {
					e.setDead();
				}
			}
		}
		this.summonedMinions.clear();

		super.onDeath(cause);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(5, new EntityAIHealingPotion(this));
		this.tasks.addTask(6, new EntityAISummonMeteors(this));
		this.tasks.addTask(7, new EntityAIExplosionRay(this));
		this.tasks.addTask(7, new EntityAIExplosionSpell(this));
		this.tasks.addTask(6, new EntityAISummonFireWall(this));
		this.tasks.addTask(10, new EntityAIAttackRanged(this));
		this.tasks.addTask(11, new EntityAIAttack(this));
		this.tasks.addTask(20, new EntityAIMoveToHome(this));
		this.tasks.addTask(21, new EntityAIIdleSit(this));

		this.targetTasks.addTask(0, new EntityAICQRNearestAttackTarget(this));
	}

	@Override
	protected ResourceLocation getLootTable() {
		return ELootTablesBoss.BOSS_BOARMAGE.getLootTable();
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.BOAR_MAGE.getValue();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.UNDEAD;
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
	public CQRFaction getSummonerFaction() {
		return this.getFaction();
	}

	@Override
	public List<Entity> getSummonedEntities() {
		return this.summonedMinions;
	}

	@Override
	public EntityLivingBase getSummoner() {
		return this;
	}

	@Override
	public void addSummonedEntityToList(Entity summoned) {
		this.summonedMinions.add(summoned);
	}

}
