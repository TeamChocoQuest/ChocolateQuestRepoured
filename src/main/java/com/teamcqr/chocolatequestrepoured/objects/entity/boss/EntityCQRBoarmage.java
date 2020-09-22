package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;
import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.init.CQRLoottables;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.boarmage.BossAIBoarmageExplodeAreaAttack;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.boarmage.BossAIBoarmageTeleportSpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAIExplodeAreaStartSpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAIExplosionRay;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAISummonFireWall;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAISummonMeteors;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.ISummoner;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityCQRBoarmage extends AbstractEntityCQRMageBase implements ISummoner {

	protected List<Entity> summonedMinions = new ArrayList<>();

	protected boolean startedExplodeAreaAttack = false;

	public EntityCQRBoarmage(World worldIn) {
		super(worldIn);

		this.isImmuneToFire = true;
	}

	@Override
	public boolean isImmuneToExplosions() {
		return true;
	}

	public void startExplodeAreaAttack() {
		this.startedExplodeAreaAttack = true;
	}

	public boolean isExecutingExplodeAreaAttack() {
		return this.startedExplodeAreaAttack;
	}

	public void stopExplodeAreaAttack() {
		this.startedExplodeAreaAttack = false;
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

		if ((this.isInLava() || this.isBurning()) && this.ticksExisted % 5 == 0) {
			this.heal(1);
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
		super.initEntityAI();
		this.tasks.addTask(10, new BossAIBoarmageTeleportSpell(this));
		this.tasks.addTask(0, new BossAIBoarmageExplodeAreaAttack(this));
		this.spellHandler.addSpell(0, new EntityAISummonMeteors(this, 75, 20));
		this.spellHandler.addSpell(3, new EntityAIExplosionRay(this, 100, 10));
		this.spellHandler.addSpell(2, new EntityAISummonFireWall(this, 50, 25));
		this.spellHandler.addSpell(1, new EntityAIExplodeAreaStartSpell(this, 200, 20, 5));
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_BOARMAGE;
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

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}

	@Override
	public boolean canPutOutFire() {
		return false;
	}

}
