package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ELootTablesBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIAttack;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIAttackRanged;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIIdleSit;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToHome;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToLeader;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise.AISpinAttackTurtle;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise.AISwitchStates;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise.BossAIHealingTurtle;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise.BossAIStunTurtle;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.EntityAICQRNearestAttackTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.EntityAIHurtByTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.subparts.EntityCQRGiantTortoisePart;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationAI;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfo.Overlay;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityCQRGiantTortoise extends AbstractEntityCQRBoss implements IEntityMultiPart, IRangedAttackMob, IAnimatedEntity {

	private static final DataParameter<Integer> ANIM_STATE = EntityDataManager.<Integer>createKey(EntityCQRGiantTortoise.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> IN_SHELL = EntityDataManager.<Boolean>createKey(EntityCQRGiantTortoise.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IN_SHELL_BYPASS = EntityDataManager.<Boolean>createKey(EntityCQRGiantTortoise.class, DataSerializers.BOOLEAN);
	
	protected EntityCQRGiantTortoisePart[] parts = new EntityCQRGiantTortoisePart[5];
	protected ETortoiseAnimState currentAnimation = ETortoiseAnimState.NONE;

	static int EAnimStateGlobalID = 0;

	public static final int TARGET_MOVE_OUT = 1;
	public static final int TARGET_MOVE_IN = -1;
	private int targetedState = 0;
	private boolean partSoundFlag = false;
	private boolean stunned = false;
	private boolean canBeStunned = true;
	private boolean wantsToSpin = false;
	private boolean spinning = false;
	private int timesHealed = 1;
	private boolean isHealing = false;
	
	//Animations
	private Animation animation = NO_ANIMATION;
	private int animationTick;
	public AnimationAI<EntityCQRGiantTortoise> currentAnim;
	
	public static final Animation ANIMATION_SHOOT_BUBBLES = Animation.create(80);
	public static final Animation ANIMATION_MOVE_LEGS_IN = Animation.create(30).setLooping(false);
	public static final Animation ANIMATION_MOVE_LEGS_OUT = Animation.create(50).setLooping(false);
	public static final Animation ANIMATION_SPIN_UP = Animation.create(40);
	public static final Animation ANIMATION_SPIN_DOWN = Animation.create(40);
	public static final Animation ANIMATION_SPIN = Animation.create(10).setLooping(true);
	public static final Animation ANIMATION_IDLE = Animation.create(100);
	public static final Animation ANIMATION_STUNNED = Animation.create(200).setLooping(false);
	public static final Animation ANIMATION_DEATH = Animation.create(300);
	
	private static final Animation[] ANIMATIONS = {
			ANIMATION_SHOOT_BUBBLES, 
			ANIMATION_MOVE_LEGS_IN, 
			ANIMATION_MOVE_LEGS_OUT, 
			ANIMATION_SPIN_UP, 
			ANIMATION_SPIN_DOWN,
			ANIMATION_SPIN,
			ANIMATION_IDLE,
			ANIMATION_STUNNED,
			ANIMATION_DEATH,
		};
	//End of Animations

	public enum ETortoiseAnimState {
		SPIN_UP, SPIN_DOWN, SPIN, MOVE_PARTS_IN, MOVE_PARTS_OUT, WALKING, HEALING, NONE;

		private int id;

		private ETortoiseAnimState() {
			this.id = EAnimStateGlobalID;
			EAnimStateGlobalID++;
		}

		public static ETortoiseAnimState valueOf(int i) {
			if (i >= values().length) {
				return NONE;
			}
			return values()[i];
		}

		public int getID() {
			return this.id;
		}
	}
	
	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new AISwitchStates(this, ANIMATION_MOVE_LEGS_IN, ANIMATION_MOVE_LEGS_OUT));
		this.tasks.addTask(2, new BossAIStunTurtle(this));
		this.tasks.addTask(5, new BossAIHealingTurtle(this));
		this.tasks.addTask(6, new AISpinAttackTurtle(this));
		this.tasks.addTask(8, new EntityAIAttackRanged(this));
		this.tasks.addTask(10, new EntityAIAttack(this) {
			@Override
			public boolean shouldExecute() {
				if(super.shouldExecute() && !((EntityCQRGiantTortoise) entity).isInShell() && !isHealing && !isStunned() && !isSpinning() && !wantsToSpin()) {
					return true;
				} else if(super.shouldExecute() && !isHealing){
					((EntityCQRGiantTortoise) entity).targetNewState(TARGET_MOVE_OUT);
				}
				return false;
			}
			
			@Override
			public boolean shouldContinueExecuting() {
				if(super.shouldContinueExecuting() && !((EntityCQRGiantTortoise) entity).isInShell() && !isHealing && !isStunned() && !isSpinning() && !wantsToSpin()) {
					return true;
				} else if(super.shouldContinueExecuting() && !isHealing){
					((EntityCQRGiantTortoise) entity).targetNewState(TARGET_MOVE_OUT);
				}
				return false;
			}
		});
		this.tasks.addTask(15, new EntityAIMoveToLeader(this) {
			@Override
			public boolean shouldExecute() {
				if(super.shouldExecute() && !((EntityCQRGiantTortoise) entity).isInShell() && !isHealing && !isStunned() && !isSpinning()) {
					return true;
				} else if(super.shouldExecute() && !isHealing){
					((EntityCQRGiantTortoise) entity).targetNewState(TARGET_MOVE_OUT);
				}
				return false;
			}
			
			@Override
			public boolean shouldContinueExecuting() {
				if(super.shouldContinueExecuting() && !((EntityCQRGiantTortoise) entity).isInShell() && !isHealing && !isStunned() && !isSpinning()) {
					return true;
				} else if(super.shouldContinueExecuting() && !isHealing){
					((EntityCQRGiantTortoise) entity).targetNewState(TARGET_MOVE_OUT);
				}
				return false;
			}
		});
		this.tasks.addTask(20, new EntityAIMoveToHome(this) {
			@Override
			public boolean shouldExecute() {
				if(super.shouldExecute() && !((EntityCQRGiantTortoise) entity).isInShell() && !isHealing && !isStunned() && !isSpinning()) {
					return true;
				} else if(super.shouldExecute() && !isHealing){
					((EntityCQRGiantTortoise) entity).targetNewState(TARGET_MOVE_OUT);
				}
				return false;
			}
			
			@Override
			public boolean shouldContinueExecuting() {
				if(super.shouldContinueExecuting() && !((EntityCQRGiantTortoise) entity).isInShell() && !isHealing && !isStunned() && !isSpinning()) {
					return true;
				} else if(super.shouldContinueExecuting() && !isHealing){
					((EntityCQRGiantTortoise) entity).targetNewState(TARGET_MOVE_OUT);
				}
				return false;
			}
		});
		this.tasks.addTask(21, new EntityAIIdleSit(this) {
			@Override
			public boolean shouldExecute() {
				if(super.shouldExecute() && ((EntityCQRGiantTortoise) entity).isInShell() && !isHealing && !isStunned() && !isSpinning()) {
					return true;
				} else if(super.shouldExecute() && !isHealing){
					((EntityCQRGiantTortoise) entity).targetNewState(TARGET_MOVE_IN);
				}
				return false;
			}
		});

		this.targetTasks.addTask(0, new EntityAICQRNearestAttackTarget(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this));
	}

	final float baseWidth = 2.0F;
	final float baseHeight = 1.7F;

	@SideOnly(Side.CLIENT)
	private int animationProgress = 0;

	public EntityCQRGiantTortoise(World worldIn) {
		super(worldIn, 1);

		this.bossInfoServer.setColor(Color.GREEN);
		this.bossInfoServer.setCreateFog(true);
		this.bossInfoServer.setOverlay(Overlay.PROGRESS);

		this.setSize(this.baseWidth, this.baseHeight);

		for (int i = 0; i < this.parts.length - 1; i++) {
			this.parts[i] = new EntityCQRGiantTortoisePart(this, "tortoise_leg" + i, 0.7F, 1.1F, false);
		}
		this.parts[this.parts.length - 1] = new EntityCQRGiantTortoisePart(this, "tortoise_head", 0.7F, 0.7F, true);

		this.noClip = false;
		this.setNoGravity(false);
		this.experienceValue = 150;

		this.ignoreFrustumCheck = true;
	}

	@Override
	protected void entityInit() {
		super.entityInit();

		this.dataManager.register(ANIM_STATE, ETortoiseAnimState.NONE.getID());
		this.dataManager.register(IN_SHELL, true);
		this.dataManager.register(IN_SHELL_BYPASS, false);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();

		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.99D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.125D);
	}

	@Override
	public World getWorld() {
		return this.world;
	}

	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart dragonPart, DamageSource source, float damage) {
		partSoundFlag = true;
		return this.attackEntityFrom(source, damage, true);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount, boolean sentFromPart) {
		//System.out.println("class direct: " + source.getImmediateSource().getClass().getName());
		//System.out.println("class true: " + source.getTrueSource().getClass().getName());
		if(source.isExplosion() && isInShell() && canBeStunned && !stunned) {
			stunned = true;
			canBeStunned = false;
		}
		if (sentFromPart && !this.isInShell()) {
			if(stunned) {
				amount *= 2F;
			}
			return super.attackEntityFrom(source, amount, sentFromPart);
		}
		return true;
	}

	public boolean isInShell() {
		return dataManager.get(IN_SHELL);
	}
	
	public boolean isStunned() {
		return this.stunned;
	}
	
	public boolean canBeStunned() {
		return this.canBeStunned;
	}
	
	public void setCanBeStunned(boolean value) {
		this.canBeStunned = value;
	}
	
	public void setStunned(boolean value) {
		this.stunned = value;
	}
	
	public boolean bypassInShell() {
		return dataManager.get(IN_SHELL_BYPASS);
	}
	public void setBypassInShell(boolean val) {
		this.dataManager.set(IN_SHELL_BYPASS, val);
	}

	@Override
	public int getAir() {
		return 100;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return ELootTablesBoss.BOSS_TURTLE.getLootTable();
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.GIANT_TORTOISE.getValue();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.BEASTS;
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
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSwingingArms(boolean swingingArms) {

	}

	public ETortoiseAnimState getCurrentAnimation() {
		if (!this.world.isRemote) {
			return this.currentAnimation;
		}
		return ETortoiseAnimState.valueOf(this.dataManager.get(ANIM_STATE));
		// return ETortoiseAnimState.MOVE_PARTS_OUT;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		this.setAir(999);
		for (EntityCQRGiantTortoisePart part : this.parts) {
			this.world.updateEntityWithOptionalForce(part, true);
		}

		this.alignParts();
	}

	private void alignParts() {
		// Legs
		Vec3d v = new Vec3d(0, 0, this.baseWidth / 2 + this.baseWidth * 0.1);
		v = VectorUtil.rotateVectorAroundY(v, this.rotationYawHead);

		this.parts[this.parts.length - 1].setPosition(this.posX + v.x, this.posY + 0.5, this.posZ + v.z);
		this.parts[this.parts.length - 1].setRotation(this.rotationYawHead, this.rotationPitch);

		v = VectorUtil.rotateVectorAroundY(v, 45D);

		for (int i = 0; i < this.parts.length - 1; i++) {
			if (i > 0) {
				v = VectorUtil.rotateVectorAroundY(v, 90D);
			}

			double x = this.posX + v.x;
			double y = this.posY;
			double z = this.posZ + v.z;

			this.parts[i].setPosition(x, y, z);
			this.parts[i].setRotation(this.rotationYaw + i * 45F, this.rotationPitch);
		}
	}

	@Override
	public Entity[] getParts() {
		return this.parts;
	}

	@Override
	public void setDead() {
		super.setDead();
		for (EntityCQRGiantTortoisePart part : this.parts) {
			// must use this instead of setDead
			// since multiparts are not added to the world tick list which is what checks isDead
			this.world.removeEntityDangerously(part);
		}
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		if(isInShell()) {
			return SoundEvents.ENTITY_BLAZE_HURT;
		}
		if(partSoundFlag) {
			partSoundFlag = false;
			return SoundEvents.ENTITY_SLIME_HURT;
		}
		return SoundEvents.ENTITY_BLAZE_HURT;
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
		super.collideWithEntity(entityIn);
		if(isSpinning()) {
			entityIn.attackEntityFrom(DamageSource.causeThornsDamage(this), 4F + (world.getDifficulty().getDifficultyId() *2F));
			Vec3d v = entityIn.getPositionVector().subtract(getPositionVector());
			v = v.normalize();
			v = v.scale(1.5D);
			entityIn.motionX = v.x;
			entityIn.motionY = v.y;
			entityIn.motionZ = v.z;
		}
	}
	
	@Override
	public boolean canBePushed() {
		return false;
	}
	
	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	public void setCurrentAnimation(ETortoiseAnimState newState) {
		this.currentAnimation = newState;
		this.dataManager.set(ANIM_STATE, newState.getID());
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEFINED;
	}

	public void setInShell(boolean val) {
		this.dataManager.set(IN_SHELL, val);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		compound.setInteger("timesHealed", timesHealed);
		compound.setBoolean("inShell", isInShell());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);

		setTimesHealed(compound.getInteger("timesHealed"));
		if(getTimesHealed() < 1) {
			setTimesHealed(1);
		}
		setInShell(compound.getBoolean("inShell"));
	}
	
	//IAnimatedEntity Interface
	@Override
	public int getAnimationTick() {
		return animationTick;
	}
	
	@Override
	public void setAnimationTick(int tick) {
		this.animationTick = tick;
	}
	
	@Override
	public Animation getAnimation() {
		return this.animation;
	}
	
	@Override
	public void setAnimation(Animation animation) {
		if(animation == NO_ANIMATION) {
			onAnimationFinish(this.animation);
			this.animation = animation;
			setAnimationTick(0);
		}
		else if(this.animation != animation) {
			this.animation = animation;
			//AnimationHandler.INSTANCE.sendAnimationMessage(this, this.animation);
		}
		
	}
	
	@Override
	public Animation[] getAnimations() {
		return ANIMATIONS;
	}
	
	protected void onAnimationFinish(Animation animation) {
		
	}
	
	public Animation getDeathAnimation() {
		return ANIMATION_DEATH;
	}
	
	protected void onDeathAIUpdate() {
		if(getAnimation() != ANIMATION_DEATH) {
			AnimationHandler.INSTANCE.sendAnimationMessage(this, ANIMATION_DEATH);
		}
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		
		if(getAnimation() != NO_ANIMATION) {
			animationTick++;
			if(world.isRemote && animationTick >= animation.getDuration()) {
				setAnimation(NO_ANIMATION);
				AnimationHandler.INSTANCE.sendAnimationMessage(this, NO_ANIMATION);
			}
		}
	}
	
	public void targetNewState(int newStateID) {
		if(newStateID != this.targetedState) {
			this.targetedState = newStateID;
			if(newStateID != 0) {
				if(newStateID < 0) {
					AnimationHandler.INSTANCE.sendAnimationMessage(this, ANIMATION_MOVE_LEGS_IN);
				} else {
					AnimationHandler.INSTANCE.sendAnimationMessage(this, ANIMATION_MOVE_LEGS_OUT);
				}
			} else {
				AnimationHandler.INSTANCE.sendAnimationMessage(this, NO_ANIMATION);
			}
		}
	}
	public int getTargetedState() {
		return targetedState;
	}
	public boolean wantsToChangeState() {
		return targetedState != 0;
	}
	public void changedState() {
		this.targetedState = 0;
	}
	
	public int getTimesHealed() {
		return timesHealed;
	}
	public void setTimesHealed(int val) {
		this.timesHealed = val;
	}
	public void setHealing(boolean val) {
		this.isHealing = val;
	}
	
	public boolean isHealing() {
		return isHealing;
	}
	
	public void setSpinning(boolean value) {
		this.spinning = value;
	}
	
	public boolean isSpinning() {
		return spinning;
	}
	
	@Override
	public boolean canOpenDoors() {
		return false;
	}
	
	public void setWantsToSpin(boolean value) {
		this.wantsToSpin = value;
	}
	
	public boolean wantsToSpin() {
		return wantsToSpin;
	}

}
