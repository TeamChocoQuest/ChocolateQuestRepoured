package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;
import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.init.CQRItems;
import com.teamcqr.chocolatequestrepoured.init.CQRLoottables;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIAttack;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIFollowAttackTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIFollowPath;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToHome;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToLeader;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.giantspider.BossAISpiderHook;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.giantspider.BossAISpiderLeapAttack;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.giantspider.BossAISpiderSummonMinions;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.giantspider.BossAISpiderWebshot;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAIShootPoisonProjectiles;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.EntityAICQRNearestAttackTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.EntityAIHurtByTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.ISummoner;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCQRGiantSpider extends AbstractEntityCQRBoss implements ISummoner {

	private static final DataParameter<Byte> CLIMBING = EntityDataManager.<Byte>createKey(EntityCQRGiantSpider.class, DataSerializers.BYTE);

	protected List<Entity> activeEggs = new ArrayList<>();

	public EntityCQRGiantSpider(World worldIn) {
		super(worldIn);
	}

	@Override
	public float getDefaultHeight() {
		return 1F;
	}

	@Override
	public float getDefaultWidth() {
		return 2.3F;
	}

	@Override
	public boolean canOpenDoors() {
		return false;
	}

	@Override
	protected void initEntityAI() {
		this.spellHandler = this.createSpellHandler();
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new BossAISpiderSummonMinions(this));
		this.tasks.addTask(2, new BossAISpiderWebshot(this));
		this.tasks.addTask(3, new BossAISpiderHook(this));
		this.tasks.addTask(12, new BossAISpiderLeapAttack(this, 1.2F));
		this.tasks.addTask(14, new EntityAIAttack(this));

		this.tasks.addTask(20, new EntityAIFollowAttackTarget(this));

		this.tasks.addTask(30, new EntityAIMoveToLeader(this));
		this.tasks.addTask(31, new EntityAIFollowPath(this));
		this.tasks.addTask(32, new EntityAIMoveToHome(this));

		this.tasks.addTask(11, this.spellHandler);
		this.spellHandler.addSpell(0, new EntityAIShootPoisonProjectiles(this, 120, 20) {
			@Override
			protected SoundEvent getStartChargingSound() {
				return SoundEvents.ENTITY_SPIDER_HURT;
			}

			@Override
			protected SoundEvent getStartCastingSound() {
				return SoundEvents.ENTITY_SPIDER_AMBIENT;
			}
		});

		this.targetTasks.addTask(0, new EntityAICQRNearestAttackTarget(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(CLIMBING, (byte) 0);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!this.world.isRemote) {
			this.setBesideClimbableBlock(this.collidedHorizontally);
		}
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		this.setEquipmentBasedOnDifficulty(difficulty);
		return super.onInitialSpawn(difficulty, livingdata);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		super.setEquipmentBasedOnDifficulty(difficulty);
		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(CQRItems.SPIDERHOOK, 1));
	}

	@Override
	public void fall(float p_180430_1_, float p_180430_2_) {
		// What do we say to fall damge? Not today!
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		List<Entity> tmp = new ArrayList<>();
		for (Entity ent : this.activeEggs) {
			if (ent == null || ent.isDead) {
				tmp.add(ent);
			}
		}
		for (Entity e : tmp) {
			this.activeEggs.remove(e);
		}
	}

	@Override
	public void addPotionEffect(PotionEffect potioneffectIn) {
		if (potioneffectIn.getPotion() == MobEffects.POISON || potioneffectIn.getPotion() == MobEffects.WEAKNESS || potioneffectIn.getPotion() == MobEffects.WITHER) {
			return;
		}
		super.addPotionEffect(potioneffectIn);
	}

	/**
	 * Returns new PathNavigateGround instance
	 */
	@Override
	protected PathNavigate createNavigator(World worldIn) {
		return new PathNavigateClimber(this, worldIn);
	}

	/**
	 * Returns true if the WatchableObject (Byte) is 0x01 otherwise returns false. The WatchableObject is updated using setBesideClimableBlock.
	 */
	public boolean isBesideClimbableBlock() {
		return (((Byte) this.dataManager.get(CLIMBING)).byteValue() & 1) != 0;
	}

	/**
	 * Updates the WatchableObject (Byte) created in entityInit(), setting it to 0x01 if par1 is true or 0x00 if it is false.
	 */
	public void setBesideClimbableBlock(boolean climbing) {
		byte b0 = ((Byte) this.dataManager.get(CLIMBING));

		if (climbing) {
			b0 = (byte) (b0 | 1);
		} else {
			b0 = (byte) (b0 & -2);
		}

		this.dataManager.set(CLIMBING, b0);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8);
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(4);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.8D * 0.30000001192092896D);
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		boolean result = super.attackEntityAsMob(entityIn);
		if (result) {
			int effectlvl = 1;
			if (this.getRNG().nextDouble() > 0.7) {
				effectlvl = 2;
				this.heal(((EntityLivingBase) entityIn).getHealth() * 0.25F);
			}
			((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.POISON, 20 + entityIn.world.getDifficulty().ordinal() * 40, effectlvl));
		}
		return result;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_SPIDER;
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.GiantSpider;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.BEASTS;
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.ARTHROPOD;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SPIDER_AMBIENT;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_SPIDER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SPIDER_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) {
		this.playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15F, 1.0F);
	}

	@Override
	public void setInWeb() {
	}

	@Override
	public boolean isPotionApplicable(PotionEffect potioneffectIn) {
		if (potioneffectIn.getPotion() == MobEffects.POISON) {
			net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent event = new net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent(this, potioneffectIn);
			net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
			return event.getResult() == net.minecraftforge.fml.common.eventhandler.Event.Result.ALLOW;
		}
		return super.isPotionApplicable(potioneffectIn);
	}

	@Override
	public CQRFaction getSummonerFaction() {
		return this.getFaction();
	}

	@Override
	public List<Entity> getSummonedEntities() {
		return this.activeEggs;
	}

	@Override
	public EntityLivingBase getSummoner() {
		return this;
	}

	@Override
	public void addSummonedEntityToList(Entity summoned) {
		this.activeEggs.add(summoned);
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

}
