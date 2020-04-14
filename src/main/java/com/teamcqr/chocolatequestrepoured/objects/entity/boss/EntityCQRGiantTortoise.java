package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ELootTablesBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIIdleSit;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise.BossAITortoiseSpinAttack;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise.BossAITortoiseSwitchStates;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise.BossAITortoiseMoveToHome;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise.BossAITortoiseMoveToLeader;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise.BossAITortoiseHealing;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise.BossAITortoiseStun;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.EntityAICQRNearestAttackTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.EntityAIHurtByTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.subparts.EntityCQRGiantTortoisePart;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationAI;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemShield;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityCQRGiantTortoise extends AbstractEntityCQRBoss implements IEntityMultiPart, IRangedAttackMob, IAnimatedEntity {

	private static final DataParameter<Boolean> IN_SHELL = EntityDataManager.<Boolean>createKey(EntityCQRGiantTortoise.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IN_SHELL_BYPASS = EntityDataManager.<Boolean>createKey(EntityCQRGiantTortoise.class, DataSerializers.BOOLEAN);
	
	protected EntityCQRGiantTortoisePart[] parts = new EntityCQRGiantTortoisePart[5];

	static int EAnimStateGlobalID = 0;

	public static final int TARGET_MOVE_OUT = 1;
	public static final int TARGET_MOVE_IN = -1;
	private int targetedState = 0;
	private boolean partSoundFlag = false;
	private boolean stunned = false;
	private boolean canBeStunned = true;
	private boolean wantsToSpin = false;
	private boolean readyToSpin = true;
	private boolean spinning = false;
	private int spinsBlocked = 0;
	private int timesHealed = 1;
	private boolean isHealing = false;
	
	//Animations
	private Animation animation = NO_ANIMATION;
	private int animationTick;
	public AnimationAI<EntityCQRGiantTortoise> currentAnim;
	
	public static final Animation ANIMATION_MOVE_LEGS_IN = Animation.create(30).setLooping(false);
	public static final Animation ANIMATION_MOVE_LEGS_OUT = Animation.create(50).setLooping(false);
	public static final Animation ANIMATION_SPIN = Animation.create(250).setLooping(false);
	public static final Animation ANIMATION_IDLE = Animation.create(100);
	public static final Animation ANIMATION_STUNNED = Animation.create(140).setLooping(false);
	public static final Animation ANIMATION_DEATH = Animation.create(300);
	
	private static final Animation[] ANIMATIONS = {
			ANIMATION_MOVE_LEGS_IN, 
			ANIMATION_MOVE_LEGS_OUT, 
			ANIMATION_SPIN,
			ANIMATION_IDLE,
			ANIMATION_STUNNED,
			ANIMATION_DEATH,
		};
	//End of Animations

	
	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new BossAITortoiseSwitchStates(this, ANIMATION_MOVE_LEGS_IN, ANIMATION_MOVE_LEGS_OUT));
		this.tasks.addTask(2, new BossAITortoiseStun(this));
		this.tasks.addTask(4, new BossAITortoiseHealing(this));
		this.tasks.addTask(6, new BossAITortoiseSpinAttack(this));
		this.tasks.addTask(19, new BossAITortoiseMoveToLeader(this));
		this.tasks.addTask(20, new BossAITortoiseMoveToHome(this));
		this.tasks.addTask(21, new EntityAIIdleSit(this) {
			@Override
			public boolean shouldExecute() {
				if(super.shouldExecute() && ((EntityCQRGiantTortoise) entity).isInShell() && !isHealing && !isStunned() && !isSpinning()) {
					return true;
				} else if(super.shouldExecute() && !isHealing && !isStunned() && !isSpinning()){
					((EntityCQRGiantTortoise) entity).targetNewState(TARGET_MOVE_IN);
				}
				return false;
			}
		});

		this.targetTasks.addTask(0, new EntityAICQRNearestAttackTarget(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this));
	}

	@Override
	public float getDefaultWidth() {
		return 2.0F;
	}

	@Override
	public float getDefaultHeight() {
		return 1.7F;
	}

	@SideOnly(Side.CLIENT)
	private int animationProgress = 0;

	public EntityCQRGiantTortoise(World worldIn) {
		super(worldIn);

		this.bossInfoServer.setColor(Color.GREEN);

		for (int i = 0; i < this.parts.length - 1; i++) {
			this.parts[i] = new EntityCQRGiantTortoisePart(this, "tortoise_leg" + i, 0.7F, 1.1F, false);
		}
		this.parts[this.parts.length - 1] = new EntityCQRGiantTortoisePart(this, "tortoise_head", 0.7F, 0.7F, true);

		this.noClip = false;
		this.setNoGravity(false);
		this.experienceValue = 100;

		this.ignoreFrustumCheck = true;
	}

	@Override
	protected void entityInit() {
		super.entityInit();

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
		return this.attackEntityFrom(source, damage, true);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount, boolean sentFromPart) {
		if(source.isExplosion() && isInShell() && canBeStunned && !stunned) {
			stunned = true;
			canBeStunned = false;
		}
		partSoundFlag = sentFromPart;
		
		if(source.getTrueSource() instanceof EntityLivingBase && !(source.getTrueSource() instanceof EntityPlayer)) {
			if(getRNG().nextBoolean() && !sentFromPart) {
				sentFromPart = true;
			}
		}
		
		if (source.getTrueSource() instanceof EntityLivingBase) {
			this.setRevengeTarget((EntityLivingBase) source.getTrueSource());
		}
		
		if (source.isUnblockable()) {
			return super.attackEntityFrom(source, amount, sentFromPart);
		}
		if(!sentFromPart) {
			amount = 0;
			world.playSound(posX, posY, posZ, getHurtSound(source), SoundCategory.HOSTILE, 1.0F, 1.0F, true);
		}
		if (sentFromPart && (!this.isInShell() || source == DamageSource.IN_WALL)) {
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
		this.readyToSpin = !stunned;
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
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {

	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		this.setAir(999);
		for (EntityCQRGiantTortoisePart part : this.parts) {
			this.world.updateEntityWithOptionalForce(part, true);
		}

		this.alignParts();
		this.breakBlocksInWay();
	}

	private void breakBlocksInWay() {
		for(BlockPos pos : BlockPos.getAllInBoxMutable(getPosition().add(this.width +1, this.height, this.width +1), getPosition().add(-this.width -1, -1, -this.width -1))) {
			Block block = world.getBlockState(pos).getBlock();
			if((!block.isCollidable() || block.isPassable(world, pos)) && !(block == Blocks.FLOWING_WATER || block == Blocks.WATER || block == Blocks.FLOWING_LAVA || block == Blocks.LAVA)) {
				world.setBlockToAir(pos);
			}
		}
	}

	private void alignParts() {
		// Legs
		Vec3d v = new Vec3d(0, 0, this.width / 2 + this.width * 0.1);
		v = VectorUtil.rotateVectorAroundY(v, this.rotationYawHead);

		float vy = isInShell() || this.getAnimation() == ANIMATION_STUNNED ? 0.1F : 0.5F; 
		
		this.parts[this.parts.length - 1].setPosition(this.posX + v.x, this.posY + vy, this.posZ + v.z);
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
		if(isSpinning()) {
			boolean blocked = false;
			if(entityIn instanceof EntityLivingBase) {
				if(((EntityLivingBase)entityIn).getActiveItemStack().getItem() instanceof ItemShield) {
					if(getRNG().nextBoolean()) {
						spinsBlocked++;
					}
					spinsBlocked++;
					blocked = true;
				}
			}
			
			if(!blocked) {
				entityIn.attackEntityFrom(DamageSource.causeThornsDamage(this), 4F + (world.getDifficulty().getDifficultyId() *2F));
			}
			Vec3d v = entityIn.getPositionVector().subtract(getPositionVector());
			v = v.normalize();
			if(blocked) {
				v = v.scale(0.8D);
			} else {
				v = v.scale(1.5D);
			}
			entityIn.motionX = v.x;
			entityIn.motionY = v.y +0.75;
			entityIn.motionZ = v.z;
			entityIn.velocityChanged = true;
			if(blocked) {
				v = v.scale(1.7D);
				this.motionX = -v.x;
				this.motionY = v.y +0.25;
				this.motionZ = -v.z;
				this.velocityChanged = true;
				
				world.playSound(posX, posY, posZ, SoundEvents.ENTITY_BLAZE_HURT, SoundCategory.HOSTILE, 1.0F, 1.0F, true);
			}
		} else {
			super.collideWithEntity(entityIn);
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

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.ARTHROPOD;
	}

	public void setInShell(boolean val) {
		this.dataManager.set(IN_SHELL, val);
		this.readyToSpin = val;
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
		this.readyToSpin = !isHealing;
	}
	
	public boolean isHealing() {
		return isHealing;
	}
	
	public void setSpinning(boolean value) {
		this.spinning = value;
		this.readyToSpin = !spinning;
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
	
	public void setReadyToSpin(boolean value) {
		this.readyToSpin = value;
	}
	
	public boolean isReadyToSpin() {
		return readyToSpin;
	}
	
	@Override
	public Vec3d getPositionEyes(float partialTicks) {
		Vec3d headPos = parts[this.parts.length - 1].getPositionVector();
		return headPos.add(headPos.subtract(posX, 0, posZ)).normalize().scale(0.25D);
	}
	
	public int getSpinsBlocked() {
		return spinsBlocked;
	}
	
	public void resetSpinsBlocked() {
		spinsBlocked = 0;
	}

}
