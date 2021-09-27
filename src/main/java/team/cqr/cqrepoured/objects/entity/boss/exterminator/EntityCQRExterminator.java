package team.cqr.cqrepoured.objects.entity.boss.exterminator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.factions.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.objects.entity.IDontRenderFire;
import team.cqr.cqrepoured.objects.entity.IMechanical;
import team.cqr.cqrepoured.objects.entity.ISizable;
import team.cqr.cqrepoured.objects.entity.MultiPartEntityPartSizable;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.util.PartialTicksUtil;
import team.cqr.cqrepoured.util.VectorUtil;

public class EntityCQRExterminator extends AbstractEntityCQRBoss implements IMechanical, IDontRenderFire, IEntityMultiPart, IAnimatable {

	// Entity parts
	/*
	 * private SubEntityExterminatorBackpack backpackEntity;
	 * private SubEntityExterminatorFieldEmitter emitterLeft;
	 * private SubEntityExterminatorFieldEmitter emitterRight;
	 */
	// 0 => Backpack
	// 1 => Emitter left
	// 2 => Emitter right
	// 3 & 4 => Artificial hitbox (left and right), purpose is to avoid entities punching though the boss when it is in non-stunned state
	private MultiPartEntityPart[] parts;

	protected static final DataParameter<Boolean> IS_STUNNED = EntityDataManager.<Boolean>createKey(EntityCQREnderCalamity.class, DataSerializers.BOOLEAN);

	// Geckolib
	private AnimationFactory factory = new AnimationFactory(this);
	private boolean partSoundFlag;

	// Entity state handling
	// Arm state

	public EntityCQRExterminator(World worldIn) {
		super(worldIn);
		this.experienceValue = 100;

		this.parts = new MultiPartEntityPart[5];
		/*
		 * this.backpackEntity = new SubEntityExterminatorBackpack(this, "exterminator_backpack");
		 * this.emitterLeft = new SubEntityExterminatorFieldEmitter(this, "emitter_left");
		 * this.emitterRight = new SubEntityExterminatorFieldEmitter(this, "emitter_right");
		 */
		this.parts[0] = new SubEntityExterminatorBackpack(this, "exterminator_backpack");
		this.parts[1] = new SubEntityExterminatorFieldEmitter(this, "emitter_left");
		this.parts[2] = new SubEntityExterminatorFieldEmitter(this, "emitter_right");

		this.parts[3] = new MultiPartEntityPartSizable<EntityCQRExterminator>(this, "main_hitbox_left", this.getDefaultWidth() / 3, this.getDefaultHeight());
		this.parts[4] = new MultiPartEntityPartSizable<EntityCQRExterminator>(this, "main_hitbox_right", this.getDefaultWidth() / 3, this.getDefaultHeight());
	}

	@Override
	protected void entityInit() {
		super.entityInit();

		this.dataManager.register(IS_STUNNED, false);
	}

	public void setStunned(boolean value) {
		this.dataManager.set(IS_STUNNED, value);
	}

	public boolean isStunned() {
		return this.dataManager.get(IS_STUNNED);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_EXTERMINATOR;
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Exterminatior;
	}

	@Override
	protected EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.ILLAGERS;
	}

	@Override
	public World getWorld() {
		return this.getEntityWorld();
	}

	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart part, DamageSource source, float damage) {
		boolean isMainHBPart = !(part == this.parts[3] || part == this.parts[4]);
		return this.attackEntityFrom(source, damage, isMainHBPart);
	}

	@Override
	public void registerControllers(AnimationData data) {
		// Spin animation for tesla coils
		data.addAnimationController(new AnimationController<EntityCQRExterminator>(this, "controller_spin_coils", 0, this::predicateSpinCoils));

		// Punch
		data.addAnimationController(new AnimationController<EntityCQRExterminator>(this, "controller_left_hand_punch", 0, this::predicatePunch));

		// Kick, Throw and smash animation

		// Cannon controller (raising, lowering and shooting)

		// Main animations (Stun, inactive, death)
		data.addAnimationController(new AnimationController<EntityCQRExterminator>(this, "controller_main", 30, this::predicateAnimationMain));
	}

	public static final String ANIM_NAME_PREFIX = "animation.exterminator.";

	public static final String ANIM_NAME_SPIN_COILS = ANIM_NAME_PREFIX + "spin_tesla_coils";

	private <E extends IAnimatable> PlayState predicateSpinCoils(AnimationEvent<E> event) {
		if (event.getController().getCurrentAnimation() == null) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_SPIN_COILS, true));
		}
		return PlayState.CONTINUE;
	}

	public static final String ANIM_NAME_INACTIVE = ANIM_NAME_PREFIX + "inactive";
	public static final String ANIM_NAME_DEATH = ANIM_NAME_PREFIX + "death";
	public static final String ANIM_NAME_STUN = ANIM_NAME_PREFIX + "stun";

	@SuppressWarnings("unchecked")
	private <E extends IAnimatable> PlayState predicateAnimationMain(AnimationEvent<E> event) {
		// Death animation
		if (this.dead || this.getHealth() < 0.01 || this.isDead || !this.isEntityAlive()) {
			event.getController().transitionLengthTicks = 0.0D;
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_DEATH, false));
			return PlayState.CONTINUE;
		}

		// Stunned
		if (this.isStunned()) {
			event.getController().transitionLengthTicks = 0.0D;
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_STUN, true));
			return PlayState.CONTINUE;
		}

		event.getController().transitionLengthTicks = 30;

		// Inactive animation
		if (super.isSitting()) {
			// if(event.getController().getCurrentAnimation() == null || event.getController().isJustStarting) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_INACTIVE, false));
			// }
			return PlayState.CONTINUE;
		}
		event.getController().setAnimation(null);
		return PlayState.STOP;
	}

	private <E extends IAnimatable> PlayState predicateCannonArm(AnimationEvent<E> event) {
		return PlayState.STOP;
	}

	public static final String ANIM_NAME_PUNCH = ANIM_NAME_PREFIX + "punch";

	private <E extends IAnimatable> PlayState predicatePunch(AnimationEvent<E> event) {
		if (this.getSwingProgress(PartialTicksUtil.getCurrentPartialTicks()) > 0.0F) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_PUNCH, false));

		} else {
			event.getController().setAnimation(null);
			event.getController().clearAnimationCache();
		}
		return PlayState.CONTINUE;
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	// Default hitbox size
	@Override
	public float getDefaultHeight() {
		return 2.75F;
	}

	@Override
	public float getDefaultWidth() {
		return 2F;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		/*
		 * this.world.updateEntityWithOptionalForce(this.backpackEntity, true);
		 * this.world.updateEntityWithOptionalForce(this.emitterLeft, true);
		 * this.world.updateEntityWithOptionalForce(this.emitterRight, true);
		 */
		for (MultiPartEntityPart part : this.parts) {
			this.world.updateEntityWithOptionalForce(part, true);
			part.onUpdate();
		}

		this.alignParts();
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount, boolean sentFromPart) {

		if (source.canHarmInCreative() || source == DamageSource.OUT_OF_WORLD || (source.getTrueSource() instanceof EntityPlayer && ((EntityPlayer) source.getTrueSource()).isCreative())) {
			return super.attackEntityFrom(source, amount, sentFromPart);
		}

		if (source.isFireDamage()) {
			return false;
		}

		if (this.isStunned()) {
			amount *= 2.0F;
		}

		this.partSoundFlag = sentFromPart;
		if (!sentFromPart && !this.isStunned()) {
			this.playSound(this.getHurtSound(source), 1.0F, 1.0F);
			return true;
		}
		return super.attackEntityFrom(source, amount, sentFromPart);
	}

	@Override
	public boolean canBeCollidedWith() {
		return this.isStunned();
	}

	// Multipart stuff
	@Override
	public Entity[] getParts() {
		return this.parts;
	}

	private void alignParts() {
		// Artificial main hitbox
		final Vec3d offsetMainHitbox = VectorUtil.rotateVectorAroundY(this.getLookVec().normalize().scale((this.getDefaultWidth() * this.getSizeVariation()) / 6), 90.0D);
		this.parts[4].setPosition(this.posX + offsetMainHitbox.x, this.posY, this.posZ + offsetMainHitbox.z);
		this.parts[3].setPosition(this.posX - offsetMainHitbox.x, this.posY, this.posZ - offsetMainHitbox.z);

		// Backpack and emitters
		Vec3d offset = this.getLookVec().normalize().scale(-0.25D * this.getSizeVariation());
		offset = offset.add(0, 1.25D * this.getSizeVariation(), 0);

		this.parts[0].setPosition(this.posX + offset.x, this.posY + offset.y, this.posZ + offset.z);

		Vec3d offsetEmittersHorizontal = this.getLookVec().normalize().scale(0.5 * this.getSizeVariation());

		Vec3d offsetEmitters = this.getLookVec().normalize().scale(-0.4D * this.getSizeVariation());
		offsetEmitters = offsetEmitters.add(0, 2.375D * this.getSizeVariation(), 0);

		offsetEmittersHorizontal = VectorUtil.rotateVectorAroundY(offsetEmittersHorizontal, 90);
		this.parts[2].setPosition(this.posX + offsetEmitters.x + offsetEmittersHorizontal.x, this.posY + offsetEmitters.y, this.posZ + offsetEmitters.z + offsetEmittersHorizontal.z);

		offsetEmittersHorizontal = VectorUtil.rotateVectorAroundY(offsetEmittersHorizontal, 180);
		this.parts[1].setPosition(this.posX + offsetEmitters.x + offsetEmittersHorizontal.x, this.posY + offsetEmitters.y, this.posZ + offsetEmitters.z + offsetEmittersHorizontal.z);
	}

	@Override
	public void resize(float widthScale, float heightSacle) {
		super.resize(widthScale, heightSacle);

		/*
		 * this.backpackEntity.resize(widthScale, heightSacle);
		 * this.emitterLeft.resize(widthScale, heightSacle);
		 * this.emitterRight.resize(widthScale, heightSacle);
		 */
		for (MultiPartEntityPart part : this.parts) {
			if (part instanceof ISizable) {
				((ISizable) part).resize(widthScale, heightSacle);
			}
		}
	}

	@Override
	public boolean isSitting() {
		return false;
	}

	@Override
	public void setDead() {
		/*
		 * this.world.removeEntityDangerously(this.backpackEntity);
		 * this.world.removeEntityDangerously(this.emitterLeft);
		 * this.world.removeEntityDangerously(this.emitterRight);
		 */
		for (MultiPartEntityPart part : this.parts) {
			this.world.removeEntityDangerously(part);
		}

		super.setDead();
	}

	// Cannon arm
	public boolean isCannonArmReadyToShoot() {

		return false;
	}

	public boolean isExecutingThrow() {

		return false;
	}

	public boolean isExecutingGroundSlam() {

		return false;
	}

	@Override
	public boolean isMagicArmorActive() {
		// When stunned it uses the same render effect
		return super.isMagicArmorActive() || this.isStunned();
	}

	@Override
	protected void updateCooldownForMagicArmor() {
		if (this.isStunned()) {
			return;
		}
		super.updateCooldownForMagicArmor();
	}

	// Death code
	private DamageSource deathCause = null;

	@Override
	public void onDeath(DamageSource cause) {
		this.deathCause = cause;
		super.onDeath(cause);
	}

	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
		// Nope
	}

	// Death animation
	// Death animation time: 1.44s => 29 ticks
	@Override
	protected void onDeathUpdate() {
		++this.deathTime;
		if (this.deathTime >= 70 && this.isServerWorld()) {
			if (this.deathCause != null) {
				super.dropLoot(this.recentlyHit > 0, net.minecraftforge.common.ForgeHooks.getLootingLevel(this, this.deathCause.getTrueSource(), this.deathCause), this.deathCause);
			}
			this.setDead();

			this.onFinalDeath();
		}
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		if (!this.partSoundFlag) {
			return SoundEvents.BLOCK_ANVIL_LAND;
		}
		return SoundEvents.ENTITY_IRONGOLEM_HURT;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_IRONGOLEM_STEP;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_IRONGOLEM_DEATH;
	}

	@Override
	public boolean isImmuneToExplosions() {
		return true;
	}

}
