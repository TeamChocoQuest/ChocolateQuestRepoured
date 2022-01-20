package team.cqr.cqrepoured.entity.boss.gianttortoise;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.IEntityMultiPart;
import team.cqr.cqrepoured.entity.ai.EntityAIIdleSit;
import team.cqr.cqrepoured.entity.ai.boss.gianttortoise.BossAITortoiseHealing;
import team.cqr.cqrepoured.entity.ai.boss.gianttortoise.BossAITortoiseMoveToHome;
import team.cqr.cqrepoured.entity.ai.boss.gianttortoise.BossAITortoiseMoveToLeader;
import team.cqr.cqrepoured.entity.ai.boss.gianttortoise.BossAITortoiseSpinAttack;
import team.cqr.cqrepoured.entity.ai.boss.gianttortoise.BossAITortoiseStun;
import team.cqr.cqrepoured.entity.ai.boss.gianttortoise.BossAITortoiseSwimming;
import team.cqr.cqrepoured.entity.ai.target.EntityAICQRNearestAttackTarget;
import team.cqr.cqrepoured.entity.ai.target.EntityAIHurtByTarget;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.util.VectorUtil;

public class EntityCQRGiantTortoise extends AbstractEntityCQRBoss implements IEntityMultiPart<EntityCQRGiantTortoise>, IRangedAttackMob, IAnimatable, IAnimationTickable {

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

	private static final DataParameter<Boolean> IN_SHELL = EntityDataManager.<Boolean>defineId(EntityCQRGiantTortoise.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IN_SHELL_BYPASS = EntityDataManager.<Boolean>defineId(EntityCQRGiantTortoise.class, DataSerializers.BOOLEAN);

	private static final DataParameter<Integer> CURRENT_ANIMATION_ID = EntityDataManager.<Integer>defineId(EntityCQRGiantTortoise.class, DataSerializers.INT);
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
			new AnimationGecko("animation.giant_tortoise.in_shell", 1) };

	protected SubEntityGiantTortoisePart[] parts = new SubEntityGiantTortoisePart[5];

	public static final int TARGET_MOVE_OUT = 1;
	public static final int TARGET_MOVE_IN = -1;
	private boolean partSoundFlag = false;
	private boolean stunned = false;
	private boolean canBeStunned = true;
	private boolean readyToSpin = true;
	private boolean spinning = false;
	private int spinsBlocked = 0;
	private int timesHealed = 1;
	private boolean isHealing = false;

	private Vector3d lastTickPos = null;
	private int stuckTicks = 0;
	private static final int MAX_STUCK_TICKS = 60;

	private static List<ResourceLocation> hardBlocks = new ArrayList<>();

	public EntityCQRGiantTortoise(World worldIn) {
		this(CQREntityTypes.GIANT_TORTOISE.get(), worldIn);
	}
	
	public EntityCQRGiantTortoise(EntityType<? extends EntityCQRGiantTortoise> type, World worldIn) {
		super(type, worldIn);

		this.maxUpStep = 2.1F;

		for (int i = 0; i < this.parts.length - 1; i++) {
			this.parts[i] = new SubEntityGiantTortoisePart(this, "tortoise_leg" + i, 0.7F, 1.1F, false);
		}
		this.parts[this.parts.length - 1] = new SubEntityGiantTortoisePart(this, "tortoise_head", 0.7F, 0.7F, true);

		this.setNoGravity(false);
		this.xpReward = 100;
	}
	
	@Override
	public boolean fireImmune() {
		return true;
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
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new BossAITortoiseSwimming(this));
		this.goalSelector.addGoal(2, new BossAITortoiseStun(this));
		this.goalSelector.addGoal(4, new BossAITortoiseHealing(this));
		this.goalSelector.addGoal(6, new BossAITortoiseSpinAttack(this));
		this.goalSelector.addGoal(19, new BossAITortoiseMoveToLeader(this));
		this.goalSelector.addGoal(20, new BossAITortoiseMoveToHome(this));
		this.goalSelector.addGoal(21, new EntityAIIdleSit(this) {
			@Override
			public boolean canUse() {
				if (super.canUse() && ((EntityCQRGiantTortoise) this.entity).isInShell() && !EntityCQRGiantTortoise.this.isHealing && !EntityCQRGiantTortoise.this.isStunned() && !EntityCQRGiantTortoise.this.isSpinning()) {
					return true;
				} else if (super.canUse() && !EntityCQRGiantTortoise.this.isHealing && !EntityCQRGiantTortoise.this.isStunned() && !EntityCQRGiantTortoise.this.isSpinning()) {
					if (EntityCQRGiantTortoise.this.getCurrentAnimationId() != ANIMATION_ID_IN_SHELL) {
						if (EntityCQRGiantTortoise.this.getCurrentAnimationId() == ANIMATION_ID_WALK) {
							EntityCQRGiantTortoise.this.setNextAnimation(ANIMATION_ID_ENTER_SHELL);
						}
					}
				}
				return false;
			}
		});

		this.targetSelector.addGoal(0, new EntityAICQRNearestAttackTarget(this));
		this.targetSelector.addGoal(1, new EntityAIHurtByTarget(this));
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
	protected void defineSynchedData() {
		super.defineSynchedData();

		this.entityData.define(IN_SHELL, true);
		this.entityData.define(IN_SHELL_BYPASS, false);
		this.entityData.define(CURRENT_ANIMATION_ID, this.nextAnimationId);
	}

	public int getCurrentAnimationId() {
		return this.entityData.get(CURRENT_ANIMATION_ID);
	}

	private void setCurrentAnimation(int id) {
		if (this.isServerWorld()) {
			if (id >= 0 && id < ANIMATIONS.length) {
				this.entityData.set(CURRENT_ANIMATION_ID, id);
			}
		}
	}

	@Override
	protected void applyAttributeValues() {
		super.applyAttributeValues();

		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(10);

		this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.99D);
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.125D);
	}

	@Override
	public World getWorld() {
		return this.level;
	}

	@Override
	public boolean hurt(PartEntity dragonPart, DamageSource source, float damage) {
		return this.hurt(source, damage, true);
	}

	@Override
	public boolean hurt(DamageSource source, float amount, boolean sentFromPart) {
		if (source.isBypassInvul() || source == DamageSource.OUT_OF_WORLD || (source.getEntity() instanceof PlayerEntity && ((PlayerEntity) source.getEntity()).isCreative())) {
			return super.hurt(source, amount, sentFromPart);
		}

		/*
		 * if (source.isExplosion() && isInShell() && canBeStunned && !stunned) { stunned = true; canBeStunned = false; }
		 */
		this.partSoundFlag = sentFromPart;

		if (source.getEntity() instanceof LivingEntity && !(source.getEntity() instanceof PlayerEntity)) {
			if (this.getRandom().nextBoolean() && !sentFromPart) {
				sentFromPart = true;
			}
		}

		if (source.getEntity() instanceof LivingEntity) {
			this.setLastHurtByMob((LivingEntity) source.getEntity());
			//this.setRevengeTarget((LivingEntity) source.getEntity());
		}

		if (!sentFromPart) {
			amount = 0;
			this.playSound(this.getHurtSound(source), 1.0F, 1.0F);
		}
		if (sentFromPart && (!this.isInShell() || source == DamageSource.IN_WALL)) {
			if (this.stunned) {
				amount *= 2F;
			}
			return super.hurt(source, amount, sentFromPart);
		}
		return true;
	}

	public boolean isInShell() {
		/*
		 * if(this.isServerWorld()) {
		 * return this.getCurrentAnimationId() > ANIMATION_ID_STUNNED;
		 * }
		 */
		return this.entityData.get(IN_SHELL);
	}

	public boolean isStunned() {
		return this.stunned /* || this.getCurrentAnimationId() == ANIMATION_ID_STUNNED */;
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
		// this.nextAnimationId = ANIMATION_ID_STUNNED;
	}

	public boolean bypassInShell() {
		return this.entityData.get(IN_SHELL_BYPASS);
	}

	public void setBypassInShell(boolean val) {
		this.entityData.set(IN_SHELL_BYPASS, val);
	}

	@Override
	public int getAirSupply() {
		return 100;
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
	public void performRangedAttack(LivingEntity target, float distanceFactor) {
	}

	/*@Override
	public void setSwingingArms(boolean swingingArms) {

	}*/

	@Override
	public void tick() {
		super.tick();

		this.setAirSupply(999);
		for (SubEntityGiantTortoisePart part : this.parts) {
			//this.level.updateEntityWithOptionalForce(part, true);
			part.tick();
		}

		this.alignParts();
		this.breakBlocksInWay();
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (this.hasAttackTarget()) {
			if (this.lastTickPos == null) {
				this.lastTickPos = this.position();
			}
			if (this.getHomePositionCQR() == null) {
				this.setHomePositionCQR(this.blockPosition());
			}
			if (this.getHomePositionCQR().distSqr(this.blockPosition()) > 16) {
				if (this.position().distanceTo(this.lastTickPos) <= 0.05) {
					this.stuckTicks++;
				} else {
					this.lastTickPos = this.position();
				}
				if (this.stuckTicks >= MAX_STUCK_TICKS) {
					this.setTarget(null);
					this.stuckTicks = 0;
				}
			}
		} else {
			this.stuckTicks = 0;
		}

		this.updateAnimations();
	}

	private void breakBlocksInWay() {
		for (BlockPos pos : BlockPos.betweenClosed(this.blockPosition().offset(this.getBbWidth() + 1, this.getBbHeight(), this.getBbWidth() + 1), this.blockPosition().offset(-this.getBbWidth() - 1, -1, -this.getBbWidth() - 1))) {
			Block block = this.level.getBlockState(pos).getBlock();
			if ((!block.isCollidable() || block.isPassable(this.level, pos)) && this.level.getFluidState(pos) == Fluids.EMPTY.defaultFluidState()) {
				this.level.destroyBlock(pos, true);
			}
		}
	}

	private void alignParts() {
		// Legs
		/* Vec3d v = new Vec3d(0, 0, this.width / 2 + this.width * 0.1); */

		float rotYawHead = this.yHeadRot;// - 90;
		if (rotYawHead > 180F) {
			rotYawHead -= 360F;
		}
		// v = VectorUtil.rotateVectorAroundY(v, rotYawHead);
		Vector3d v = this.getLookAngle().scale(this.getBbWidth() / 2 + this.getBbWidth() * 0.1);

		float vy = this.getCurrentAnimationId() != ANIMATION_ID_WALK ? 0.15F : 0.5F;
		vy *= this.getSizeVariation();

		// First, position your head
		this.parts[this.parts.length - 1].setPos(this.getX() + v.x, this.getY() + vy, this.getZ() + v.z);
		this.parts[this.parts.length - 1].setYHeadRot(rotYawHead);

		// Then rotate 45 degrees to the right so that you face the front right leg
		v = VectorUtil.rotateVectorAroundY(v, 45D);

		// Now, position each individual leg
		for (int i = 0; i < (this.parts.length - 1); i++) {
			double x = this.getX() + v.x;
			double y = this.getY();
			double z = this.getZ() + v.z;

			this.parts[i].setPos(x, y, z);
			this.parts[i].setYHeadRot(rotYawHead + (i * 90F + 45F));

			// leg positioned, now rotate to the next leg (clockwise)
			v = VectorUtil.rotateVectorAroundY(v, 90D);
		}
	}

	@Override
	public PartEntity<?>[] getParts() {
		return this.parts;
	}

	@Override
	public void remove() {
		for (SubEntityGiantTortoisePart part : this.parts) {
			// must use this instead of setDead
			// since multiparts are not added to the world tick list which is what checks isDead
			//this.level.removeEntityDangerously(part);
			part.remove();
		}
		super.remove();
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		if (this.isInShell()) {
			return SoundEvents.BLAZE_HURT;
		}
		if (this.partSoundFlag) {
			this.partSoundFlag = false;
			return SoundEvents.SLIME_HURT;
		}
		return SoundEvents.BLAZE_HURT;
	}

	@Override
	protected void doPush(Entity entityIn) {
		if (this.isSpinning()) {
			boolean blocked = false;
			if (entityIn instanceof LivingEntity) {
				if (((LivingEntity) entityIn).getUseItem().getItem() instanceof ShieldItem || ((LivingEntity) entityIn).getUseItem().getItem().isShield( ((LivingEntity) entityIn).getUseItem(), (LivingEntity) entityIn)) {
					if (this.getRandom().nextBoolean()) {
						this.spinsBlocked++;
					}
					this.spinsBlocked++;
					blocked = true;
				}
			}

			if (!blocked) {
				entityIn.hurt(DamageSource.thorns(this), 4F * (Math.max(1, this.level.getDifficulty().getId()) * 1.5F));
			}
			Vector3d v = entityIn.position().subtract(this.position());
			v = v.normalize();
			if (blocked) {
				v = v.scale(0.8D);
			} else {
				v = v.scale(1.5D);
			}
			/*entityIn.motionX = v.x;
			entityIn.motionY = v.y + 0.75;
			entityIn.motionZ = v.z;
			entityIn.velocityChanged = true;*/
			entityIn.setDeltaMovement(v.add(0, 0.75, 0));
			entityIn.hasImpulse = true;
			if (blocked) {
				v = v.scale(1.7D);
				/*this.motionX = -v.x;
				this.motionY = v.y + 0.25;
				this.motionZ = -v.z;
				this.velocityChanged = true;*/
				this.setDeltaMovement(-v.x, v.y + 0.25, -v.z);
				this.hasImpulse = true;

				this.playSound(SoundEvents.BLAZE_HURT, 1.0F, 1.0F);
			}
		} else {
			super.doPush(entityIn);
		}
	}
	
	@Override
	public void setSecondsOnFire(int seconds) {
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public CreatureAttribute getMobType() {
		return CreatureAttribute.ARTHROPOD;
	}

	public void setInShell(boolean val) {
		this.entityData.set(IN_SHELL, val);
		this.readyToSpin = val && !this.isHealing();
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);

		compound.putInt("timesHealed", this.timesHealed);
		compound.putBoolean("inShell", this.isInShell());
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);

		this.setTimesHealed(compound.getInt("timesHealed"));
		if (this.getTimesHealed() < 1) {
			this.setTimesHealed(1);
		}
		this.setInShell(compound.getBoolean("inShell"));
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
		/*
		 * if(this.getCurrentAnimationId() != ANIMATION_ID_SPINNING) {
		 * this.nextAnimationId = ANIMATION_ID_SPINNING;
		 * }
		 */
	}

	public boolean isSpinning() {
		return this.spinning /* || this.getCurrentAnimationId() == ANIMATION_ID_SPINNING */;
	}

	@Override
	public boolean canOpenDoors() {
		return false;
	}

	public void setReadyToSpin(boolean value) {
		this.readyToSpin = value;
	}

	public boolean isReadyToSpin() {
		return this.readyToSpin;
	}
	
	/*@Override
	public Vector3d getPositionEyes(float partialTicks) {
		Vector3d headPos = this.parts[this.parts.length - 1].position();
		return headPos.add(headPos.subtract(this.posX, 0, this.posZ)).normalize().scale(0.25D);
	}*/

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
	@OnlyIn(Dist.CLIENT)
	private int currentAnimationClient/* = 0 */; // Important: For SideOnly fields => DO NOT set an initial value at declaration, that WON'T work

	// Animation controller
	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		// Death animation
		if (this.dead || this.getHealth() < 0.01 /*|| this.isDead*/ || !this.isAlive()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIMATION_NAME_DEATH, false));
			return PlayState.CONTINUE;
		}
		// DONE: Idle animation
		if (this.getCurrentAnimationId() < 0) {
			return PlayState.STOP;
		}
		if (this.currentAnimationClient != this.getCurrentAnimationId()) {
			this.currentAnimationClient = this.getCurrentAnimationId();
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIMATIONS[this.currentAnimationClient].getAnimationName()));
		}
		return PlayState.CONTINUE;
	}

	// GeckoLib
	@Override
	public void registerControllers(AnimationData data) {
		AnimationController<EntityCQRGiantTortoise> controller = new AnimationController<>(this, "controller", 10, this::predicate);
		data.addAnimationController(controller);
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	public void setNextAnimation(int id) {
		this.nextAnimationId = id;
	}

	// DONE: Call on entity update
	private void updateAnimations() {
		if (this.isServerWorld()) {
			int currentAnimation = this.getCurrentAnimationId();
			if (this.nextAnimationId != currentAnimation) {
				AnimationGecko nextAnimation = ANIMATIONS[this.nextAnimationId];
				this.currentAnimationTick = nextAnimation.getAnimationDuration();
				this.setCurrentAnimation(this.nextAnimationId);
				this.onAnimationEnd(this.nextAnimationId, false);
				return;
			}
			if (this.currentAnimationTick >= 0) {
				this.currentAnimationTick--;
			} else if (this.currentAnimationTick == -1) {
				this.onAnimationEnd(currentAnimation, true);
			}
		}
	}

	public int getCurrentAnimationTick() {
		return ANIMATIONS[this.getCurrentAnimationId()].getAnimationDuration() - this.currentAnimationTick;
	}

	public void resetCurrentAnimationTickTime() {
		this.currentAnimationTick = ANIMATIONS[this.getCurrentAnimationId()].getAnimationDuration();
	}

	public boolean shouldCurrentAnimationBePlaying() {
		if (!this.isServerWorld()) {
			return false;
		}
		return this.currentAnimationTick >= 0;
	}

	protected void onAnimationEnd(int animationID, boolean endByAnimationFinished) {
		if (endByAnimationFinished) {
			switch (animationID) {
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
	public void die(DamageSource cause) {
		this.deathCause = cause;
		super.die(cause);
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource deathCause, int lootingModifier, boolean wasRecentlyHit) {
		//Nope
	}
	
	/*@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
		// Nope
		// 1.16: Replaced by above method
	}*/

	// Death animation
	// Death animation time: 1.44s => 29 ticks
	@Override
	protected void tickDeath() {
		++this.deathTime;
		if (this.deathTime > 20) {
			float sizeVariation = this.getSizeVariation();
			sizeVariation *= 1.5F;
			double f = (this.random.nextDouble() - 0.5D) * (this.getDefaultWidth() * sizeVariation);
			double f1 = (this.random.nextDouble() - 0.5D) * (this.getDefaultHeight() * sizeVariation);
			double f2 = (this.random.nextDouble() - 0.5D) * (this.getDefaultWidth() * sizeVariation);
			for (int i = 0; i < 20; i++) {
				this.level.addParticle(ParticleTypes.ITEM_SLIME, this.getX() + f, this.getY() + (this.getDefaultHeight() * sizeVariation / 2) + f1, this.getZ() + f2, 0.0D, 0.0D, 0.0D);
				this.level.addParticle(ParticleTypes.DAMAGE_INDICATOR, this.getX() + f, this.getY() + (this.getDefaultHeight() * sizeVariation / 2) + f1, this.getZ() + f2, 0.0D, 0.0D, 0.0D);
			}
		}
		if (this.deathTime == 34 && this.isServerWorld()) {
			this.level.explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, Mode.NONE);
		}
		if (this.deathTime >= 35 && this.isServerWorld()) {
			if (this.deathCause != null) {
				super.dropCustomDeathLoot(this.deathCause, net.minecraftforge.common.ForgeHooks.getLootingLevel(this, this.deathCause.getEntity(), this.deathCause), this.lastHurt > 0);
			}
			this.remove();

			this.onFinalDeath();
		}
	}

	@Override
	public int tickTimer() {
		return this.tickCount;
	}

}
