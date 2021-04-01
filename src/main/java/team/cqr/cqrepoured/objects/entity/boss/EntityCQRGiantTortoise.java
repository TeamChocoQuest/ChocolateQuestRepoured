package team.cqr.cqrepoured.objects.entity.boss;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemShield;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import team.cqr.cqrepoured.factions.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.objects.entity.ai.EntityAIIdleSit;
import team.cqr.cqrepoured.objects.entity.ai.boss.gianttortoise.BossAITortoiseHealing;
import team.cqr.cqrepoured.objects.entity.ai.boss.gianttortoise.BossAITortoiseMoveToHome;
import team.cqr.cqrepoured.objects.entity.ai.boss.gianttortoise.BossAITortoiseMoveToLeader;
import team.cqr.cqrepoured.objects.entity.ai.boss.gianttortoise.BossAITortoiseSpinAttack;
import team.cqr.cqrepoured.objects.entity.ai.boss.gianttortoise.BossAITortoiseStun;
import team.cqr.cqrepoured.objects.entity.ai.boss.gianttortoise.BossAITortoiseSwimming;
import team.cqr.cqrepoured.objects.entity.ai.boss.gianttortoise.BossAITortoiseSwitchStates;
import team.cqr.cqrepoured.objects.entity.ai.target.EntityAICQRNearestAttackTarget;
import team.cqr.cqrepoured.objects.entity.ai.target.EntityAIHurtByTarget;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.objects.entity.boss.subparts.EntityCQRGiantTortoisePart;
import team.cqr.cqrepoured.util.CQRConfig;
import team.cqr.cqrepoured.util.VectorUtil;

public class EntityCQRGiantTortoise extends AbstractEntityCQRBoss implements IEntityMultiPart, IRangedAttackMob, IAnimatable {
	
	public static class AnimationGecko {
		private final String animationName;
		private final int animationDuration;
		
		public AnimationGecko(String name, int duration) {
			this.animationDuration = duration;
			this.animationName = name;
		}
		
		public String getAnimationName() {
			return this.animationName;
		}
		public int getAnimationDuration() {
			return this.animationDuration;
		}
	}
	
	private static final DataParameter<Boolean> IN_SHELL = EntityDataManager.<Boolean>createKey(EntityCQRGiantTortoise.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IN_SHELL_BYPASS = EntityDataManager.<Boolean>createKey(EntityCQRGiantTortoise.class, DataSerializers.BOOLEAN);

	private static final DataParameter<Integer> CURRENT_ANIMATION_ID = EntityDataManager.<Integer>createKey(EntityCQRGiantTortoise.class, DataSerializers.VARINT);
	private int nextAnimationId = ANIMATION_ID_IN_SHELL;
	private int currentAnimationTick = 0;
	public static final int ANIMATION_ID_IN_SHELL = 5;
	public static final int ANIMATION_ID_EXIT_SHELL = 4;
	public static final int ANIMATION_ID_ENTER_SHELL = 3;
	public static final int ANIMATION_ID_STUNNED = 2;
	public static final int ANIMATION_ID_SPINNING = 1;
	public static final int ANIMATION_ID_WALK = 0;
	public static final String ANIMATION_NAME_DEATH = "animation.giant_tortoise.death";
	public static final AnimationGecko[] ANIMATIONS = {
			new AnimationGecko("animation.giant_tortoise.walk", 20),
			new AnimationGecko("animation.giant_tortoise.spin", 260),
			new AnimationGecko("animation.giant_tortoise.stun", 140),
			new AnimationGecko("animation.giant_tortoise.enter_shell", 31),
			new AnimationGecko("animation.giant_tortoise.exit_shell", 31),
			new AnimationGecko("animation.giant_tortoise.in_shell", 1)
	};
	
	
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

	private Vec3d lastTickPos = null;
	private int stuckTicks = 0;
	private static final int MAX_STUCK_TICKS = 60;

	// Animations
	/*private Animation animation = NO_ANIMATION;
	private int animationTick;
	public AnimationAI<EntityCQRGiantTortoise> currentAnim;*/

	private static ArrayList<ResourceLocation> hardBlocks = new ArrayList<>();

	/*public static final Animation ANIMATION_MOVE_LEGS_IN = Animation.create(30).setLooping(false);
	public static final Animation ANIMATION_MOVE_LEGS_OUT = Animation.create(50).setLooping(false);
	public static final Animation ANIMATION_SPIN = Animation.create(250).setLooping(false);
	public static final Animation ANIMATION_IDLE = Animation.create(100);
	public static final Animation ANIMATION_STUNNED = Animation.create(140).setLooping(false);
	public static final Animation ANIMATION_DEATH = Animation.create(300);*/

	//private static final Animation[] ANIMATIONS = { ANIMATION_MOVE_LEGS_IN, ANIMATION_MOVE_LEGS_OUT, ANIMATION_SPIN, ANIMATION_IDLE, ANIMATION_STUNNED, ANIMATION_DEATH, };
	// End of Animations

	public EntityCQRGiantTortoise(World worldIn) {
		super(worldIn);

		this.stepHeight = 2.1F;

		for (int i = 0; i < this.parts.length - 1; i++) {
			this.parts[i] = new EntityCQRGiantTortoisePart(this, "tortoise_leg" + i, 0.7F, 1.1F, false);
		}
		this.parts[this.parts.length - 1] = new EntityCQRGiantTortoisePart(this, "tortoise_head", 0.7F, 0.7F, true);

		this.noClip = false;
		this.setNoGravity(false);
		this.isImmuneToFire = true;
		this.experienceValue = 100;

		this.ignoreFrustumCheck = true;
	}

	@Override
	public void enableBossBar() {
		super.enableBossBar();

		if (this.bossInfoServer != null) {
			this.bossInfoServer.setColor(Color.GREEN);
		}
	}

	public static void realoadHardBlocks() {
		hardBlocks.clear();
		for (String s : CQRConfig.bosses.giantTortoiseHardBlocks) {
			ResourceLocation rs = new ResourceLocation(s);
			hardBlocks.add(rs);
		}
	}

	public static boolean isHardBlock(ResourceLocation rl) {
		return !EntityCQRGiantTortoise.hardBlocks.isEmpty() && EntityCQRGiantTortoise.hardBlocks.contains(rl);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new BossAITortoiseSwimming(this));
		this.tasks.addTask(1, new BossAITortoiseSwitchStates(this, ANIMATIONS[ANIMATION_ID_ENTER_SHELL], ANIMATIONS[ANIMATION_ID_EXIT_SHELL]));
		this.tasks.addTask(2, new BossAITortoiseStun(this));
		this.tasks.addTask(4, new BossAITortoiseHealing(this));
		this.tasks.addTask(6, new BossAITortoiseSpinAttack(this));
		this.tasks.addTask(19, new BossAITortoiseMoveToLeader(this));
		this.tasks.addTask(20, new BossAITortoiseMoveToHome(this));
		this.tasks.addTask(21, new EntityAIIdleSit(this) {
			@Override
			public boolean shouldExecute() {
				if (super.shouldExecute() && ((EntityCQRGiantTortoise) this.entity).isInShell() && !EntityCQRGiantTortoise.this.isHealing && !EntityCQRGiantTortoise.this.isStunned() && !EntityCQRGiantTortoise.this.isSpinning()) {
					return true;
				} else if (super.shouldExecute() && !EntityCQRGiantTortoise.this.isHealing && !EntityCQRGiantTortoise.this.isStunned() && !EntityCQRGiantTortoise.this.isSpinning()) {
					((EntityCQRGiantTortoise) this.entity).targetNewState(TARGET_MOVE_IN);
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

	@Override
	protected void entityInit() {
		super.entityInit();

		this.dataManager.register(IN_SHELL, true);
		this.dataManager.register(IN_SHELL_BYPASS, false);
		this.dataManager.register(CURRENT_ANIMATION_ID, this.nextAnimationId);
	}
	
	public int getCurrentAnimationId() {
		return this.dataManager.get(CURRENT_ANIMATION_ID);
	}
	private void setCurrentAnimation(int id) {
		if(this.isServerWorld()) {
			if(id >= 0 && id < ANIMATIONS.length) {
				this.dataManager.set(CURRENT_ANIMATION_ID, id);
			}
		}
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();

		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10);
		;

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
		if (source.canHarmInCreative() || source == DamageSource.OUT_OF_WORLD) {
			return super.attackEntityFrom(source, amount, sentFromPart);
		}

		/*
		 * if (source.isExplosion() && isInShell() && canBeStunned && !stunned) { stunned = true; canBeStunned = false; }
		 */
		this.partSoundFlag = sentFromPart;

		if (source.getTrueSource() instanceof EntityLivingBase && !(source.getTrueSource() instanceof EntityPlayer)) {
			if (this.getRNG().nextBoolean() && !sentFromPart) {
				sentFromPart = true;
			}
		}

		if (source.getTrueSource() instanceof EntityLivingBase) {
			this.setRevengeTarget((EntityLivingBase) source.getTrueSource());
		}

		if (!sentFromPart) {
			amount = 0;
			this.world.playSound(this.posX, this.posY, this.posZ, this.getHurtSound(source), SoundCategory.HOSTILE, 1.0F, 1.0F, true);
		}
		if (sentFromPart && (!this.isInShell() || source == DamageSource.IN_WALL)) {
			if (this.stunned) {
				amount *= 2F;
			}
			return super.attackEntityFrom(source, amount, sentFromPart);
		}
		return true;
	}

	public boolean isInShell() {
		/*if(this.isServerWorld()) {
			return this.getCurrentAnimationId() > ANIMATION_ID_STUNNED;
		}*/
		return this.dataManager.get(IN_SHELL);
	}

	public boolean isStunned() {
		return this.stunned /*|| this.getCurrentAnimationId() == ANIMATION_ID_STUNNED*/;
	}

	public boolean canBeStunned() {
		return this.canBeStunned;
	}

	public void setCanBeStunned(boolean value) {
		this.canBeStunned = value;
	}

	public void setStunned(boolean value) {
		this.stunned = value;
		this.readyToSpin = !this.stunned;
		//this.nextAnimationId = ANIMATION_ID_STUNNED;
	}

	public boolean bypassInShell() {
		return this.dataManager.get(IN_SHELL_BYPASS);
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
		return CQRLoottables.ENTITIES_TURTLE;
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.GiantTortoise;
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
	
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if (this.hasAttackTarget()) {
			if (this.lastTickPos == null) {
				this.lastTickPos = this.getPositionVector();
			}
			if (this.getHomePositionCQR() == null) {
				this.setHomePositionCQR(this.getPosition());
			}
			Vec3d curPos = this.getPositionVector();
			if (this.getHomePositionCQR().distanceSq(curPos.x, curPos.y, curPos.z) > 16) {
				if (curPos.distanceTo(this.lastTickPos) <= 0.05) {
					this.stuckTicks++;
				} else {
					this.lastTickPos = curPos;
				}
				if (this.stuckTicks >= MAX_STUCK_TICKS) {
					this.setAttackTarget(null);
					this.stuckTicks = 0;
				}
			}
		} else {
			this.stuckTicks = 0;
		}
		
		this.updateAnimations();
	}

	private void breakBlocksInWay() {
		for (BlockPos pos : BlockPos.getAllInBoxMutable(this.getPosition().add(this.width + 1, this.height, this.width + 1), this.getPosition().add(-this.width - 1, -1, -this.width - 1))) {
			Block block = this.world.getBlockState(pos).getBlock();
			if ((!block.isCollidable() || block.isPassable(this.world, pos)) && !(block == Blocks.FLOWING_WATER || block == Blocks.WATER || block == Blocks.FLOWING_LAVA || block == Blocks.LAVA)) {
				this.world.setBlockToAir(pos);
			}
		}
	}

	private void alignParts() {
		// Legs
		/*Vec3d v = new Vec3d(0, 0, this.width / 2 + this.width * 0.1);*/
		
		float rotYawHead = this.rotationYawHead;// - 90;
		if(rotYawHead > 180F) {
			rotYawHead -= 360F;
		}
		//v = VectorUtil.rotateVectorAroundY(v, rotYawHead);
		Vec3d v = this.getLookVec().scale(this.width / 2 + this.width * 0.1);

		float vy = this.isInShell() || this.isStunned() ? 0.1F : 0.5F;

		//First, position your head
		this.parts[this.parts.length - 1].setPosition(this.posX + v.x, this.posY + vy, this.posZ + v.z);
		this.parts[this.parts.length - 1].setRotation(rotYawHead, this.rotationPitch);

		//Then rotate 45 degrees to the right so that you face the front right leg
		v = VectorUtil.rotateVectorAroundY(v, 45D);

		//Now, position each individual leg
		for (int i = 0; i < (this.parts.length - 1); i++) {
			double x = this.posX + v.x;
			double y = this.posY;
			double z = this.posZ + v.z;

			this.parts[i].setPosition(x, y, z);
			this.parts[i].setRotation(rotYawHead + (i * 90F + 45F), this.rotationPitch);
			
			//leg positioned, now rotate to the next leg (clockwise)
			v = VectorUtil.rotateVectorAroundY(v, 90D);
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
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		if (this.isInShell()) {
			return SoundEvents.ENTITY_BLAZE_HURT;
		}
		if (this.partSoundFlag) {
			this.partSoundFlag = false;
			return SoundEvents.ENTITY_SLIME_HURT;
		}
		return SoundEvents.ENTITY_BLAZE_HURT;
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
		if (this.isSpinning()) {
			boolean blocked = false;
			if (entityIn instanceof EntityLivingBase) {
				if (((EntityLivingBase) entityIn).getActiveItemStack().getItem() instanceof ItemShield) {
					if (this.getRNG().nextBoolean()) {
						this.spinsBlocked++;
					}
					this.spinsBlocked++;
					blocked = true;
				}
			}

			if (!blocked) {
				entityIn.attackEntityFrom(DamageSource.causeThornsDamage(this), 4F * (Math.max(1, this.world.getDifficulty().getId()) * 1.5F));
			}
			Vec3d v = entityIn.getPositionVector().subtract(this.getPositionVector());
			v = v.normalize();
			if (blocked) {
				v = v.scale(0.8D);
			} else {
				v = v.scale(1.5D);
			}
			entityIn.motionX = v.x;
			entityIn.motionY = v.y + 0.75;
			entityIn.motionZ = v.z;
			entityIn.velocityChanged = true;
			if (blocked) {
				v = v.scale(1.7D);
				this.motionX = -v.x;
				this.motionY = v.y + 0.25;
				this.motionZ = -v.z;
				this.velocityChanged = true;

				this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_BLAZE_HURT, SoundCategory.HOSTILE, 1.0F, 1.0F, true);
			}
		} else {
			super.collideWithEntity(entityIn);
		}
	}

	@Override
	public void setFire(int seconds) {
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
		this.readyToSpin = val && !this.isHealing();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		compound.setInteger("timesHealed", this.timesHealed);
		compound.setBoolean("inShell", this.isInShell());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);

		this.setTimesHealed(compound.getInteger("timesHealed"));
		if (this.getTimesHealed() < 1) {
			this.setTimesHealed(1);
		}
		this.setInShell(compound.getBoolean("inShell"));
	}

	public void targetNewState(int newStateID) {
		if (newStateID != this.targetedState) {
			this.targetedState = newStateID;
			if (newStateID != 0) {
				if (newStateID < 0) {
					//AnimationHandler.INSTANCE.sendAnimationMessage(this, ANIMATION_MOVE_LEGS_IN);
					this.nextAnimationId = ANIMATION_ID_ENTER_SHELL;
				} else {
					//AnimationHandler.INSTANCE.sendAnimationMessage(this, ANIMATION_MOVE_LEGS_OUT);
					this.nextAnimationId = ANIMATION_ID_EXIT_SHELL;
				}
			} else {
				//AnimationHandler.INSTANCE.sendAnimationMessage(this, NO_ANIMATION);
				//this.nextAnimationId = ANIMATION_ID_IN_SHELL;
			}
		}
	}

	public int getTargetedState() {
		return this.targetedState;
	}

	public boolean wantsToChangeState() {
		return this.targetedState != 0;
	}

	public void changedState() {
		this.targetedState = 0;
	}

	public int getTimesHealed() {
		return this.timesHealed;
	}

	public void setTimesHealed(int val) {
		this.timesHealed = val;
	}

	public void setHealing(boolean val) {
		this.isHealing = val;
		this.readyToSpin = !this.isHealing;
	}

	public boolean isHealing() {
		return this.isHealing;
	}

	public void setSpinning(boolean value) {
		this.spinning = value;
		this.readyToSpin = !this.spinning;
		/*if(this.getCurrentAnimationId() != ANIMATION_ID_SPINNING) {
			this.nextAnimationId = ANIMATION_ID_SPINNING;
		}*/
	}

	public boolean isSpinning() {
		return this.spinning /*|| this.getCurrentAnimationId() == ANIMATION_ID_SPINNING*/;
	}

	@Override
	public boolean canOpenDoors() {
		return false;
	}

	public void setWantsToSpin(boolean value) {
		this.wantsToSpin = value;
	}

	public boolean wantsToSpin() {
		return this.wantsToSpin;
	}

	public void setReadyToSpin(boolean value) {
		this.readyToSpin = value;
	}

	public boolean isReadyToSpin() {
		return this.readyToSpin;
	}

	@Override
	public Vec3d getPositionEyes(float partialTicks) {
		Vec3d headPos = this.parts[this.parts.length - 1].getPositionVector();
		return headPos.add(headPos.subtract(this.posX, 0, this.posZ)).normalize().scale(0.25D);
	}

	public int getSpinsBlocked() {
		return this.spinsBlocked;
	}

	public void resetSpinsBlocked() {
		this.spinsBlocked = 0;
	}

	@Override
	public boolean canPutOutFire() {
		return false;
	}

	@Override
	public boolean canIgniteTorch() {
		return false;
	}
	
	
	// Geckolib
	private AnimationFactory factory = new AnimationFactory(this);
	@SideOnly(Side.CLIENT)
	private int currentAnimationClient = 0;
	//Animation controller
	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		//Death animation
		if(this.dead || this.getHealth() < 0.01 || this.isDead || !this.isEntityAlive()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIMATION_NAME_DEATH, false));
			return PlayState.CONTINUE;
		}
		//DONE: Idle animation
		if(this.getCurrentAnimationId() < 0) {
			return PlayState.STOP;
		}
		if(this.currentAnimationClient != this.getCurrentAnimationId()) {
			this.currentAnimationClient = this.getCurrentAnimationId();
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIMATIONS[this.currentAnimationClient].getAnimationName()));
		}
		return PlayState.CONTINUE;
	}
	
	//GeckoLib
	@Override
	public void registerControllers(AnimationData data) {
		AnimationController<EntityCQRGiantTortoise> controller = new AnimationController<EntityCQRGiantTortoise>(this, "controller", 10, this::predicate);
		data.addAnimationController(controller);
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}
	
	public void setNextAnimation(int id) {
		this.nextAnimationId = id;
	}
	
	//DONE: Call on entity update
	private void updateAnimations() {
		if(this.isServerWorld()) {
			int currentAnimation = this.getCurrentAnimationId();
			if(this.nextAnimationId != currentAnimation) {
				AnimationGecko nextAnimation = ANIMATIONS[this.nextAnimationId];
				this.currentAnimationTick = nextAnimation.getAnimationDuration();
				this.setCurrentAnimation(nextAnimationId);
				this.onAnimationEnd(nextAnimationId, false);
				return;
			}
			if(this.currentAnimationTick >= 0) {
				this.currentAnimationTick--;
			} else if(this.currentAnimationTick == -1) {
				this.onAnimationEnd(currentAnimation, true);
			}
		}
	}
	
	public int getCurrentAnimationTick() {
		return ANIMATIONS[this.getCurrentAnimationId()].getAnimationDuration() - this.currentAnimationTick;
	}
	
	public boolean shouldCurrentAnimationBePlaying() {
		if(!this.isServerWorld()) {
			return false;
		}
		return this.currentAnimationTick >= 0;
	}
	
	protected void onAnimationEnd(int animationID, boolean endByAnimationFinished) {
		if(endByAnimationFinished) {
			switch(animationID) {
			case ANIMATION_ID_EXIT_SHELL:
				this.setNextAnimation(ANIMATION_ID_WALK);
				break;
			case ANIMATION_ID_STUNNED:
			case ANIMATION_ID_ENTER_SHELL:
			case ANIMATION_ID_SPINNING:
				this.setNextAnimation(ANIMATION_ID_IN_SHELL);
				break;
			}
		}
	}
	
	private DamageSource deathCause = null;
	
	@Override
	public void onDeath(DamageSource cause) {
		this.deathCause = cause;
		super.onDeath(cause);
	}
	
	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
		//Nope
	}
	
	//Death animation
	//Death animation time: 1.44s => 29 ticks
	@Override
	protected void onDeathUpdate() {
		++this.deathTicks;
		if(this.deathTicks > 20) {
			float sizeVariation = this.getSizeVariation();
			sizeVariation *= 1.5F;
			double f = (this.rand.nextDouble() - 0.5D) * (this.getDefaultWidth() * sizeVariation);
			double f1 = (this.rand.nextDouble() - 0.5D) * (this.getDefaultHeight() * sizeVariation);
			double f2 = (this.rand.nextDouble() - 0.5D) * (this.getDefaultWidth() * sizeVariation);
			for(int i = 0; i < 20; i++) {
				this.world.spawnParticle(EnumParticleTypes.SLIME, this.posX + f, this.posY + (this.getDefaultHeight() * sizeVariation / 2) + f1, this.posZ + f2, 0.0D, 0.0D, 0.0D);
				this.world.spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, this.posX + f, this.posY + (this.getDefaultHeight() * sizeVariation / 2) + f1, this.posZ + f2, 0.0D, 0.0D, 0.0D);
			}
		}
		if(this.deathTicks == 34 && this.isServerWorld()) {
			this.world.createExplosion(this, this.posX, this.posY, this.posZ, 2.0F, false);
		}
		if(this.deathTicks >= 35 && this.isServerWorld()) {
			if(this.deathCause != null) {
				super.dropLoot(
						this.recentlyHit > 0,
						net.minecraftforge.common.ForgeHooks.getLootingLevel(this, this.deathCause.getTrueSource(), this.deathCause),
						this.deathCause);
			}
			this.setDead();

			this.onFinalDeath();
		}
	}
	
}
