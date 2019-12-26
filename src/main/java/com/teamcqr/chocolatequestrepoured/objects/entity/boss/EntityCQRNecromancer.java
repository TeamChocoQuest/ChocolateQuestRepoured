package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ELootTablesBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIAttack;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAICQRNearestAttackTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIHealingPotion;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIIdleSit;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToHome;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAIBlindTargetSpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAIFangAttack;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAISummonMinionSpell;
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
		
		bossInfoServer.setColor(Color.RED);
		bossInfoServer.setCreateFog(true);
		bossInfoServer.setOverlay(Overlay.PROGRESS);
		
		setSize(0.6F, 1.8F);
	}
	
	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(5, new EntityAIHealingPotion(this));
		
		this.tasks.addTask(7, new EntityAIBlindTargetSpell(this));
		this.tasks.addTask(7, new EntityAIFangAttack(this));
		this.tasks.addTask(6, new EntityAISummonMinionSpell(this, new ResourceLocation(Reference.MODID, "flying_skull"), ECircleTexture.FLYING_SKULL, false, 6, 2, new Vec3d(0,2.5,0)));
		
		this.tasks.addTask(10, new EntityAIAttack(this));
		this.tasks.addTask(20, new EntityAIMoveToHome(this));
		this.tasks.addTask(21, new EntityAIIdleSit(this));

		this.targetTasks.addTask(0, new EntityAICQRNearestAttackTarget(this));
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		filterSummonLists();
		
		if(getAttackTarget() != null && !getAttackTarget().isDead && summonedSkulls.size() >= 3) {
			for(int i = 2; i < summonedSkulls.size(); i++) {
				EntityFlyingSkullMinion skull = summonedSkulls.get(i);
				if(!skull.hasTarget()) {
					skull.setTarget(getAttackTarget());
				}
			}
			for(int i = 2; i < summonedSkulls.size(); i++) {
				EntityFlyingSkullMinion skull = summonedSkulls.get(i);
				if(!skull.isAttacking()) {
					skull.startAttacking();
				}
			}
		}
	}
	
	private void filterSummonLists() {
		List<Entity> tmp = new ArrayList<>();
		for(Entity ent : summonedMinions) {
			if(ent == null  || ent.isDead) {
				tmp.add(ent);
			}
		}
		for(Entity e : tmp) {
			this.summonedMinions.remove(e);
		}
		tmp.clear();
		for(Entity ent : summonedSkulls) {
			if(ent == null  || ent.isDead) {
				tmp.add(ent);
			}
		}
		for(Entity e : tmp) {
			this.summonedSkulls.remove(e);
		}
	}

	@Override
	public void onDeath(DamageSource cause) {
		//Kill minions
		for(Entity e : getSummonedEntities()) {
			if(e != null && !e.isDead) {
				if(e instanceof EntityLivingBase) {
					((EntityLivingBase)e).onDeath(cause);
				}
				if(e != null) {
					e.setDead();
				}
			}
		}
		summonedMinions.clear();
		
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
	public EFaction getDefaultFaction() {
		return EFaction.UNDEAD;
	}

	@Override
	public EFaction getSummonerFaction() {
		return getFaction();
	}

	@Override
	public List<Entity> getSummonedEntities() {
		List<Entity> list = new ArrayList<>(summonedMinions);
		list.addAll(summonedSkulls);
		return list;
	}

	@Override
	public EntityLivingBase getSummoner() {
		return this;
	}

	@Override
	public void addSummonedEntityToList(Entity summoned) {
		if(summoned instanceof EntityFlyingSkullMinion) {
			summonedSkulls.add((EntityFlyingSkullMinion) summoned);
			return;
		}
		this.summonedMinions.add(summoned);
	}

}
