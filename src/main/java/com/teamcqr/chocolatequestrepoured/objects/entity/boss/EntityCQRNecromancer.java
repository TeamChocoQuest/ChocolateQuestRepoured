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
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAIBlindTargetSpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAIFangAttack;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAISummonMinionSpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAIVampiricSpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.ISummoner;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityFlyingSkullMinion;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntitySummoningCircle.ECircleTexture;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfo.Overlay;
import net.minecraft.world.World;

public class EntityCQRNecromancer extends AbstractEntityCQRMageBase implements ISummoner {

	protected List<Entity> summonedMinions = new ArrayList<>();
	protected List<EntityFlyingSkullMinion> summonedSkulls = new ArrayList<>();

	public EntityCQRNecromancer(World worldIn) {
		super(worldIn, 1);
	}

	public EntityCQRNecromancer(World worldIn, int size) {
		super(worldIn, size);

		this.bossInfoServer.setColor(Color.RED);
		this.bossInfoServer.setCreateFog(true);
		this.bossInfoServer.setOverlay(Overlay.PROGRESS);

		this.setSize(0.6F, 1.8F);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(5, new EntityAIHealingPotion(this));

		this.tasks.addTask(7, new EntityAIBlindTargetSpell(this));
		this.tasks.addTask(7, new EntityAIFangAttack(this));
		this.tasks.addTask(8, new EntityAIVampiricSpell(this));
		this.tasks.addTask(6, new EntityAISummonMinionSpell(this, new ResourceLocation(Reference.MODID, "flying_skull"), ECircleTexture.FLYING_SKULL, false, 4, 2, new Vec3d(0, 2.5, 0)));

		this.tasks.addTask(10, new EntityAIAttackRanged(this));
		this.tasks.addTask(11, new EntityAIAttack(this));
		this.tasks.addTask(20, new EntityAIMoveToHome(this));
		this.tasks.addTask(21, new EntityAIIdleSit(this));

		this.targetTasks.addTask(0, new EntityAICQRNearestAttackTarget(this));
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.filterSummonLists();

		if (this.summonedSkulls.size() >= 1) {
			this.summonedSkulls.get(0).setSide(false);
			if (this.summonedSkulls.size() >= 2) {
				this.summonedSkulls.get(1).setSide(true);
			}
		}

		if (this.getAttackTarget() != null && !this.getAttackTarget().isDead && this.summonedSkulls.size() >= 3) {
			for (int i = 2; i < this.summonedSkulls.size(); i++) {
				EntityFlyingSkullMinion skull = this.summonedSkulls.get(i);
				if (!skull.hasTarget()) {
					skull.setTarget(this.getAttackTarget());
				}
			}
			for (int i = 2; i < this.summonedSkulls.size(); i++) {
				EntityFlyingSkullMinion skull = this.summonedSkulls.get(i);
				if (!skull.isAttacking()) {
					skull.startAttacking();
				}
			}
		}
	}

	private void filterSummonLists() {
		List<Entity> tmp = new ArrayList<>();
		for (Entity ent : this.summonedMinions) {
			if (ent == null || ent.isDead) {
				tmp.add(ent);
			}
		}
		for (Entity e : tmp) {
			this.summonedMinions.remove(e);
		}
		tmp.clear();
		for (Entity ent : this.summonedSkulls) {
			if (ent == null || ent.isDead) {
				tmp.add(ent);
			}
		}
		for (Entity e : tmp) {
			this.summonedSkulls.remove(e);
		}
	}

	@Override
	public void onDeath(DamageSource cause) {
		// Kill minions
		for (Entity e : this.getSummonedEntities()) {
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
	protected ResourceLocation getLootTable() {
		return ELootTablesBoss.BOSS_NECROMANCER.getLootTable();
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.NECROMANCER.getValue();
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
		List<Entity> list = new ArrayList<>(this.summonedMinions);
		list.addAll(this.summonedSkulls);
		return list;
	}

	@Override
	public EntityLivingBase getSummoner() {
		return this;
	}

	@Override
	public void addSummonedEntityToList(Entity summoned) {
		if (summoned instanceof EntityFlyingSkullMinion) {
			this.summonedSkulls.add((EntityFlyingSkullMinion) summoned);
			return;
		}
		this.summonedMinions.add(summoned);
	}

}
