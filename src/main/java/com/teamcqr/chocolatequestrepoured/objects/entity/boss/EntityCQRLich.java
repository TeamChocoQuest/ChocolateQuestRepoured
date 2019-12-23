package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ELootTablesBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIAttack;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAICQRNearestAttackTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIHealingPotion;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIIdleSit;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToHome;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAIArmorSpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAIFangAttack;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAIShootPoisonProjectiles;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAISummonMinionSpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.ISummoner;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfo.Overlay;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCQRLich extends AbstractEntityCQRMageBase implements ISummoner {

	protected List<Entity> summonedMinions = new ArrayList<>();
	protected BlockPos currentPhylacteryPosition = null;
	
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
		//Phylactery
		if(currentPhylacteryPosition != null) {
			if(world.getBlockState(currentPhylacteryPosition).getBlock() == ModBlocks.PHYLACTERY) {
				setMagicArmorActive(true);
			} else {
				this.currentPhylacteryPosition = null;
				setMagicArmorActive(false);
			}
		}
	}
	
	public void setCurrentPhylacteryBlock(BlockPos pos) {
		this.setMagicArmorActive(true);
		this.currentPhylacteryPosition = pos;
	}
	
	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(5, new EntityAIHealingPotion(this));
		this.tasks.addTask(6, new EntityAIArmorSpell(this));
		this.tasks.addTask(7, new EntityAIFangAttack(this));
		this.tasks.addTask(8, new EntityAIShootPoisonProjectiles(this));
		this.tasks.addTask(9, new EntityAISummonMinionSpell(this));
		this.tasks.addTask(10, new EntityAIAttack(this));
		this.tasks.addTask(20, new EntityAIMoveToHome(this));
		this.tasks.addTask(21, new EntityAIIdleSit(this));

		this.targetTasks.addTask(0, new EntityAICQRNearestAttackTarget(this));
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
		return ELootTablesBoss.BOSS_LICH.getLootTable();
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.LICH.getValue();
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
	
	@Override
	protected void updateCooldownForMagicArmor() {
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		if(currentPhylacteryPosition != null) {
			compound.setTag("currentPhylactery", NBTUtil.createPosTag(currentPhylacteryPosition));
		}
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if(compound.hasKey("currentPhylactery")) {
			currentPhylacteryPosition = NBTUtil.getPosFromTag(compound.getCompoundTag("currentPhylactery"));
		}
	}

}
