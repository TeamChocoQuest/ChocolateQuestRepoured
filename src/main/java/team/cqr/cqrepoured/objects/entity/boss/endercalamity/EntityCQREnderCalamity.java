package team.cqr.cqrepoured.objects.entity.boss.endercalamity;

import com.google.common.base.Optional;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import team.cqr.cqrepoured.factions.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.util.CQRConfig;

public class EntityCQREnderCalamity extends AbstractEntityCQRBoss implements IAnimatable {

	private static final int HURT_DURATION = 24; // 1.2 * 20
	private int cqrHurtTime = 0;
	protected static final DataParameter<Boolean> IS_HURT = EntityDataManager.<Boolean>createKey(EntityCQREnderCalamity.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> SHIELD_ACTIVE = EntityDataManager.<Boolean>createKey(EntityCQREnderCalamity.class, DataSerializers.BOOLEAN);

	private static final DataParameter<Optional<IBlockState>> BLOCK_LEFT_UPPER = EntityDataManager.<Optional<IBlockState>>createKey(EntityCQREnderCalamity.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	private static final DataParameter<Optional<IBlockState>> BLOCK_LEFT_MIDDLE = EntityDataManager.<Optional<IBlockState>>createKey(EntityCQREnderCalamity.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	private static final DataParameter<Optional<IBlockState>> BLOCK_LEFT_LOWER = EntityDataManager.<Optional<IBlockState>>createKey(EntityCQREnderCalamity.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	private static final DataParameter<Optional<IBlockState>> BLOCK_RIGHT_UPPER = EntityDataManager.<Optional<IBlockState>>createKey(EntityCQREnderCalamity.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	private static final DataParameter<Optional<IBlockState>> BLOCK_RIGHT_MIDDLE = EntityDataManager.<Optional<IBlockState>>createKey(EntityCQREnderCalamity.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	private static final DataParameter<Optional<IBlockState>> BLOCK_RIGHT_LOWER = EntityDataManager.<Optional<IBlockState>>createKey(EntityCQREnderCalamity.class, DataSerializers.OPTIONAL_BLOCK_STATE);

	public static enum HANDS {
		LEFT_UPPER, 
		LEFT_MIDDLE, 
		LEFT_LOWER, 
		RIGHT_UPPER, 
		RIGHT_MIDDLE, 
		RIGHT_LOWER
	}

	// Geckolib
	private AnimationFactory factory = new AnimationFactory(this);

	public EntityCQREnderCalamity(World worldIn) {
		super(worldIn);

		setSizeVariation(2.5F);
	}

	@Override
	protected void entityInit() {
		super.entityInit();

		this.dataManager.register(IS_HURT, false);
		this.dataManager.register(SHIELD_ACTIVE, true);

		this.dataManager.register(BLOCK_LEFT_UPPER, Optional.absent());
		this.dataManager.register(BLOCK_LEFT_MIDDLE, Optional.absent());
		this.dataManager.register(BLOCK_LEFT_LOWER, Optional.absent());
		this.dataManager.register(BLOCK_RIGHT_UPPER, Optional.absent());
		this.dataManager.register(BLOCK_RIGHT_MIDDLE, Optional.absent());
		this.dataManager.register(BLOCK_RIGHT_LOWER, Optional.absent());
	}

	@Override
	public float getDefaultHeight() {
		return 2F;
	}

	@Override
	public float getDefaultWidth() {
		return 2F;
	}

	private static final String ANIM_NAME_IDLE = "animation.ender_calamity.idle";
	private static final String ANIM_NAME_HURT = "animation.ender_calamity.hit";
	private static final String ANIM_NAME_SHOOT_LASER = "animation.ender_calamity.shootLaser";
	private static final String ANIM_NAME_DEFLECT_BALL = "animation.ender_calamity.deflectBall";
	private static final String ANIM_NAME_SHOOT_BALL = "animation.ender_calamity.shootEnergyBall";

	private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
		System.out.println("World is remote: " + this.world.isRemote);
		event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_IDLE, true));
		if (this.dataManager.get(IS_HURT)) {
			return PlayState.STOP;
		}
		return PlayState.CONTINUE;
	}

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		if (this.dataManager.get(IS_HURT)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_HURT, true));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}

	@Override
	public void registerControllers(AnimationData data) {
		// Idle
		data.addAnimationController(new AnimationController<EntityCQREnderCalamity>(this, "controllerIdle", 10, this::predicateIdle));
		// Everything else
		data.addAnimationController(new AnimationController<EntityCQREnderCalamity>(this, "controller", 10, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_ENDERMAN_BOSS;
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.EnderCalamity;
	}

	@Override
	protected EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.ENDERMEN;
	}

	@Override
	public boolean isSitting() {
		return false;
	}

	@Override
	public void enableBossBar() {
		super.enableBossBar();

		if (this.bossInfoServer != null) {
			this.bossInfoServer.setColor(Color.PURPLE);
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public void move(MoverType type, double x, double y, double z) {
		// super.move(type, x, y, z);
		return;
	}

	@Override
	public int getHealingPotions() {
		return 0;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_ENDERMEN_HURT;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_ENDERMEN_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_ENDERMEN_DEATH;
	}

	@Override
	protected float getSoundVolume() {
		return 2F * super.getSoundVolume();
	}

	@Override
	protected float getSoundPitch() {
		return 0.75F * super.getSoundPitch();
	}

	@Override
	public int getTalkInterval() {
		// Super: 80
		return 60;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(16.0D);
	}

	public boolean isShieldActive() {
		return this.dataManager.get(SHIELD_ACTIVE);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount, boolean sentFromPart) {
		// Projectile attack
		if (source instanceof EntityDamageSourceIndirect) {

			return false;
		}

		// Other attack
		if (!this.dataManager.get(IS_HURT) && !this.isShieldActive()) {
			if (!super.attackEntityFrom(source, amount, sentFromPart)) {
				return false;
			}
			if (!this.world.isRemote) {
				this.dataManager.set(IS_HURT, true);
				this.cqrHurtTime = HURT_DURATION;
			}
			return true;
		}
		return false;
	}

	@Override
	public void onLivingUpdate() {
		if (this.world.isRemote) {
			// Client
			for (int i = 0; i < 2; ++i) {
				this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + this.rand.nextDouble() * (double) this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, (this.rand.nextDouble() - 0.5D) * 2.0D,
						-this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
			}
		} else {
			// SErver
			if (this.cqrHurtTime > 0) {
				this.cqrHurtTime--;
			}
			this.dataManager.set(IS_HURT, cqrHurtTime > 0);
		}

		this.isJumping = false;
		super.onLivingUpdate();
	}
	
	public Optional<IBlockState> getBlockFromHand(HANDS hand) {
		switch(hand) {
		case LEFT_LOWER:
			return this.dataManager.get(BLOCK_LEFT_LOWER);
		case LEFT_MIDDLE:
			return this.dataManager.get(BLOCK_LEFT_MIDDLE);
		case LEFT_UPPER:
			return this.dataManager.get(BLOCK_LEFT_UPPER);
		case RIGHT_LOWER:
			return this.dataManager.get(BLOCK_RIGHT_LOWER);
		case RIGHT_MIDDLE:
			return this.dataManager.get(BLOCK_RIGHT_MIDDLE);
		case RIGHT_UPPER:
			return this.dataManager.get(BLOCK_RIGHT_UPPER);
		default:
			return Optional.absent();
		}
	}
	
	public void removeHandBlock(HANDS hand) {
		Optional<IBlockState> value = Optional.absent();
		this.equipBlock(hand, value);
	}
	
	public void equipBlock(HANDS hand, Block block) {
		this.equipBlock(hand, block.getDefaultState());
	}
	
	public void equipBlock(HANDS hand, Optional<IBlockState> value) {
		switch(hand) {
		case LEFT_LOWER:
			this.dataManager.set(BLOCK_LEFT_LOWER, value);
			break;
		case LEFT_MIDDLE:
			this.dataManager.set(BLOCK_LEFT_MIDDLE, value);
			break;
		case LEFT_UPPER:
			this.dataManager.set(BLOCK_LEFT_UPPER, value);
			break;
		case RIGHT_LOWER:
			this.dataManager.set(BLOCK_RIGHT_LOWER, value);
			break;
		case RIGHT_MIDDLE:
			this.dataManager.set(BLOCK_RIGHT_MIDDLE, value);
			break;
		case RIGHT_UPPER:
			this.dataManager.set(BLOCK_RIGHT_UPPER, value);
			break;
		default:
			break;
		}
	}
	
	public void equipBlock(HANDS hand, IBlockState blockstate) {
		equipBlock(hand, Optional.of(blockstate));
	}

}
