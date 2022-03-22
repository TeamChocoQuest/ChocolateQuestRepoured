package team.cqr.cqrepoured.entity.boss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.FollowMobGoal;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import team.cqr.cqrepoured.entity.ai.boss.piratecaptain.parrot.BossAIPirateParrotLandOnCaptainsShoulder;
import team.cqr.cqrepoured.entity.ai.boss.piratecaptain.parrot.BossAIPirateParrotThrowPotions;
import team.cqr.cqrepoured.entity.ai.target.EntityAIPetNearestAttackTarget;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class EntityCQRPirateParrot extends ParrotEntity {

	public EntityCQRPirateParrot(EntityType<? extends EntityCQRPirateParrot> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		// this.aiSit = new SitGoal(this);
		// this.tasks.addTask(0, new EntityAIPanic(this, 1.25D));
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new BossAIPirateParrotThrowPotions(this));
		// this.tasks.addTask(2, this.aiSit);
		this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 5.0F, 1.0F, true));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
		this.goalSelector.addGoal(5, new BossAIPirateParrotLandOnCaptainsShoulder(this));
		this.goalSelector.addGoal(3, new FollowMobGoal(this, 1.0D, 3.0F, 7.0F));

		this.targetSelector.addGoal(0, new EntityAIPetNearestAttackTarget<>(this, MobEntity.class, 100, true, false));
	}

	@Override
	public void forceAddEffect(EffectInstance effect) {
		if (!effect.getEffect().isBeneficial()) {
			return;
		}
		super.forceAddEffect(effect);
	}

	@Override
	public boolean addEffect(EffectInstance pEffectInstance) {
		if (pEffectInstance.getEffect().isBeneficial()) {
			return super.addEffect(pEffectInstance);
		}
		return false;
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		if (!this.level.isClientSide) {
			this.level.explode(this, this.getX(), this.getY(), this.getZ(), 2, Mode.DESTROY);
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

		List<Entity> ents = this.level.getEntities(this, new AxisAlignedBB(this.blockPosition().subtract(OWNER_RANGE_RADIUS), this.blockPosition().offset(OWNER_RANGE_RADIUS)), input -> input instanceof LivingEntity && input.getUUID().equals(uuid));
		return ents.isEmpty() ? null : (LivingEntity) ents.get(0);
	}

	public static AttributeModifierMap.MutableAttribute createAttributes() {
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
		CompoundNBT nbttagcompound = new CompoundNBT();
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
		this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.FIRE_CHARGE, 1));
	}

	@Override
	public boolean canSitOnShoulder() {
		return true;
	}

	@Override
	public boolean setEntityOnShoulder(ServerPlayerEntity p_213439_1_) {
		return super.setEntityOnShoulder(p_213439_1_);
	}

	@Override
	public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		if (this.isTame() && (player == this.getOwner() || this.getOwnerUUID().equals(player.getUUID()))) {
			return ActionResultType.SUCCESS;
		}
		return super.mobInteract(player, hand);
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
