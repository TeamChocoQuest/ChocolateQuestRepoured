package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ELootTablesBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.ISummoner;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
		bossInfoServer.setColor(Color.RED);
		bossInfoServer.setCreateFog(true);
		bossInfoServer.setOverlay(Overlay.PROGRESS);
		
		setSize(0.6F, 1.8F);
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		List<Entity> tmp = new ArrayList<>();
		for(Entity ent : summonedMinions) {
			if(ent == null  || ent.isDead) {
				tmp.add(ent);
			}
		}
		for(Entity e : tmp) {
			this.summonedMinions.remove(e);
		}
	}
	
	@Override
	public void onDeath(DamageSource cause) {
		//Kill minions
		for(Entity e : summonedMinions) {
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
		return ELootTablesBoss.BOSS_BOARMAGE.getLootTable();
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.BOAR_MAGE.getValue();
	}

	@Override
	public EFaction getDefaultFaction() {
		return EFaction.BEASTS;
	}

	@Override
	public int getTextureCount() {
		return 0;
	}

	@Override
	public boolean canRide() {
		return false;
	}

	@Override
	public EFaction getSummonerFaction() {
		return this.getFaction();
	}

	@Override
	public List<Entity> getSummonedEntities() {
		return summonedMinions;
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
