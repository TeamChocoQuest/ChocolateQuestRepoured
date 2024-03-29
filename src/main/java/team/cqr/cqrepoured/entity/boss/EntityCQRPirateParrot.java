package team.cqr.cqrepoured.entity.boss;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.FollowMobGoal;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.Explosion.Mode;
import net.minecraftforge.network.NetworkHooks;
import team.cqr.cqrepoured.entity.ai.boss.piratecaptain.parrot.BossAIPirateParrotLandOnCaptainsShoulder;
import team.cqr.cqrepoured.entity.ai.boss.piratecaptain.parrot.BossAIPirateParrotThrowPotions;
import team.cqr.cqrepoured.entity.ai.target.EntityAIPetNearestAttackTarget;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class EntityCQRPirateParrot extends Parrot {

	public EntityCQRPirateParrot(EntityType<? extends EntityCQRPirateParrot> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		// this.aiSit = new SitGoal(this);
		// this.tasks.addTask(0, new EntityAIPanic(this, 1.25D));
		this.goalSelector.addGoal(0, new RandomSwimmingGoal(this));
		this.goalSelector.addGoal(1, new BossAIPirateParrotThrowPotions(this));
		// this.tasks.addTask(2, this.aiSit);
		this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 5.0F, 1.0F, true));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
		this.goalSelector.addGoal(5, new BossAIPirateParrotLandOnCaptainsShoulder(this));
		this.goalSelector.addGoal(3, new FollowMobGoal(this, 1.0D, 3.0F, 7.0F));

		this.targetSelector.addGoal(0, new EntityAIPetNearestAttackTarget<>(this, MobEntity.class, 100, true, false));
	}

	@Override
	public void forceAddEffect(MobEffectInstance effect) {
		if (!effect.getEffect().isBeneficial()) {
			return;
		}
		super.forceAddEffect(effect);
	}

	@Override
	public boolean addEffect(MobEffectInstance pEffectInstance) {
		if (pEffectInstance.getEffect().isBeneficial()) {
			return super.addEffect(pEffectInstance);
		}
		return false;
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		if (!this.level().isClientSide) {
			this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2, Mode.DESTROY);
		}
	}

	@Override
	public void setInSittingPose(boolean p_233686_1_) {
	}

	@Override
	public boolean isInSittingPose() {
		return false;
	}

	@Nullable
	@Override
	public LivingEntity getOwner() {
		try {
			UUID uuid = this.getOwnerUUID();
			return uuid == null ? null : this.getOwnerInRange(uuid);
		} catch (IllegalArgumentException var2) {
			return null;
		}
	}

	final BlockPos OWNER_RANGE_RADIUS = new BlockPos(20, 20, 20);

	private LivingEntity getOwnerInRange(UUID uuid) {

		List<Entity> ents = this.level().getEntities(this, new AABB(this.blockPosition().subtract(OWNER_RANGE_RADIUS), this.blockPosition().offset(OWNER_RANGE_RADIUS)), input -> input instanceof LivingEntity && input.getUUID().equals(uuid));
		return ents.isEmpty() ? null : (LivingEntity) ents.get(0);
	}

	public static AttributeSupplier.MutableAttribute createAttributes() {
		return MobEntity
				.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 40.0D)
				.add(Attributes.FLYING_SPEED, (double) 0.8F)
				.add(Attributes.MOVEMENT_SPEED, (double) 0.4F);
	}

	/*
	 * @Override protected void applyEntityAttributes() { super.applyEntityAttributes(); this.getEntityAttribute(Attributes.MAX_HEALTH).setBaseValue(40); }
	 */

	public boolean setCQREntityOnShoulder(AbstractEntityCQR p_191994_1_) {
		CompoundTag nbttagcompound = new CompoundTag();
		nbttagcompound.putString("id", this.getEncodeId()); // Correct?
		this.saveWithoutId(nbttagcompound);

		if (p_191994_1_.addShoulderEntity(nbttagcompound)) {
			this.remove();
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance pDifficulty) {
		super.populateDefaultEquipmentSlots(pDifficulty);
		// WHY TF?!
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.FIRE_CHARGE, 1));
	}

	@Override
	public boolean canSitOnShoulder() {
		return true;
	}

	@Override
	public boolean setEntityOnShoulder(ServerPlayer p_213439_1_) {
		return super.setEntityOnShoulder(p_213439_1_);
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (this.isTame() && (player == this.getOwner() || this.getOwnerUUID().equals(player.getUUID()))) {
			return InteractionResult.SUCCESS;
		}
		return super.mobInteract(player, hand);
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
